package algthrom;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class 分块上传 {
    //分块上传对于当前应用开发已经是必不可少的需求了，比如上传一个100M的文件，如果一次上传，那么上传速度会很慢，
    // 所以，将文件进行分块上传，每次上传一个块，这样上传速度就会大大提升。
    //为了引入分块上传，我决定从讯飞音频上传这个Demo入手


    private static final String BASE_URL = "https://upload-ost-api.xfyun.cn";
    private static final String API_KEY = "00fcb30884ee773b118c3d84c10b99a6"; // 请替换为你的实际API KEY
    private static final String API_SECRET = "MzhjOGU1ZmVjZTkzYTY3MDdhNDhlZTZi"; // 请替换为你的实际API SECRET

    private static final String APP_ID="b27234ef";

    private static final String INIT_URL="https://upload-ost-api.xfyun.cn/file/mpupload/init";
    private static final String UPLOAD_URL="https://upload-ost-api.xfyun.cn/file/mpupload/upload";
    private static final String COMPLETE_URL="https://upload-ost-api.xfyun.cn/file/mpupload/complete";
    private static final String PRO_CREATE_URL="https://ost-api.xfyun.cn/v2/ost/pro_create";
    private static final String PRO_QUERY_URL="https://ost-api.xfyun.cn/v2/ost/query";
//    2、大文件分块上传
//（1）初始化分块信息 https://upload-ost-api.xfyun.cn/file/mpupload/init
//            （2）分块上传 https://upload-ost-api.xfyun.cn/file/mpupload/upload
//            （3）分块上传完成 https://upload-ost-api.xfyun.cn/file/mpupload/complete
//            3、创建任务 https://ost-api.xfyun.cn/v2/ost/pro_create
//            4、查询任务 https://ost-api.xfyun.cn/v2/ost/query

    public void uploadChunkedFile(String filePath){
        try{
            File file = new File(filePath);
            long fileSize = file.length();
            long chunkSize=30*1024*1024;//每次传送的文件块大小
            int chunkCount = (int) Math.ceil((double) fileSize / chunkSize);
            String requestId = java.util.UUID.randomUUID().toString();
            String uploadId = init(INIT_URL,requestId);
            
            // 上传每个分块
            for(int i = 0; i < chunkCount; i++){
                long start = i * chunkSize;
                long end = Math.min(start + chunkSize, fileSize);
                long currentChunkSize = end - start;
                
                // 读取文件块
                byte[] chunkData = new byte[(int) currentChunkSize];
                java.io.RandomAccessFile accessFile = new java.io.RandomAccessFile(file, "r");
                accessFile.seek(start);
                int bytesRead = accessFile.read(chunkData);
                accessFile.close();
                
                // 发起分块上传请求
                uploadChunk(uploadId, i, chunkData,requestId);
            }
            String s = uploadComplete(uploadId, requestId);
            String task = createTask(s, requestId);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    // 分块上传完成方法
    private String uploadComplete(String uploadId, String requestId) throws Exception {
        // 构建请求体JSON
        String body = "{\n" +
                     "\t\"request_id\": \"" + requestId + "\",\n" +
                     "\t\"app_id\": \"" + APP_ID + "\",\n" +
                     "\t\"upload_id\": \"" + uploadId + "\"\n" +
                     "}";
        
        // 发送HTTP请求
        java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create(COMPLETE_URL))
                .header("Content-Type", "application/json")
                .header("Date", getCurrentGMTDate())
                .header("Authorization", buildAuthorizationHeader(getCurrentGMTDate(), body, COMPLETE_URL))
                .POST(java.net.http.HttpRequest.BodyPublishers.ofString(body))
                .build();
        
        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
        System.out.println("Upload complete response: " + response.body());
        
        // 解析响应，获取返回的URL
        String responseBody = response.body();
        if (responseBody.contains("url")) {
            int startIndex = responseBody.indexOf("\"url\": \"") + 8; // "url": "的长度
            int endIndex = responseBody.indexOf("\"", startIndex);
            if (startIndex > 7 && endIndex > startIndex) {
                String returnUrl = responseBody.substring(startIndex, endIndex);

                System.out.println("Upload completed successfully. Return URL: " + returnUrl);
                return returnUrl;
            }
        }
        return null;
    }
    
    // 分块上传方法
    private void uploadChunk(String uploadId, int sliceId, byte[] chunkData, String requestId) throws Exception {
        String boundary = "----WebKitFormBoundary" + java.util.UUID.randomUUID().toString().replace("-", "");
        
        // 构建multipart请求体
        java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
        
        // 添加data部分
        String dataHeader = "--" + boundary + "\r\n" +
                           "Content-Disposition: form-data; name=\"data\"; filename=\"chunk_" + sliceId + "\"\r\n" +
                           "Content-Type: application/octet-stream\r\n\r\n";
        outputStream.write(dataHeader.getBytes("UTF-8"));
        outputStream.write(chunkData);
        outputStream.write("\r\n".getBytes("UTF-8"));
        
        // 添加app_id部分
        String appIdPart = "--" + boundary + "\r\n" +
                          "Content-Disposition: form-data; name=\"app_id\"\r\n\r\n" +
                          APP_ID + "\r\n";
        outputStream.write(appIdPart.getBytes("UTF-8"));
        
        // 添加request_id部分
        String requestIdPart = "--" + boundary + "\r\n" +
                              "Content-Disposition: form-data; name=\"request_id\"\r\n\r\n" +
                               requestId+ "\r\n";
        outputStream.write(requestIdPart.getBytes("UTF-8"));
        
        // 添加upload_id部分
        String uploadIdPart = "--" + boundary + "\r\n" +
                             "Content-Disposition: form-data; name=\"upload_id\"\r\n\r\n" +
                             uploadId + "\r\n";
        outputStream.write(uploadIdPart.getBytes("UTF-8"));
        
        // 添加slice_id部分
        String sliceIdPart = "--" + boundary + "\r\n" +
                            "Content-Disposition: form-data; name=\"slice_id\"\r\n\r\n" +
                            sliceId + "\r\n";
        outputStream.write(sliceIdPart.getBytes("UTF-8"));
        
        // 添加结束标记
        String endBoundary = "--" + boundary + "--\r\n";
        outputStream.write(endBoundary.getBytes("UTF-8"));
        
        byte[] multipartBody = outputStream.toByteArray();
        
        // 发送HTTP请求
        java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create(UPLOAD_URL))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .header("Date", getCurrentGMTDate())
                .header("Authorization", buildAuthorizationHeader(getCurrentGMTDate(), "", UPLOAD_URL))
                .POST(java.net.http.HttpRequest.BodyPublishers.ofByteArray(multipartBody))
                .build();
        
        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
        System.out.println("Chunk " + sliceId + " upload response: " + response.body());
    }

    private String init(String url, String requestId) throws Exception {
        //先拿到请求头所需的参数
        String body="{\n" +
                "\t\"request_id\": \"" + requestId + "\",\n" +
                "\t\"app_id\": \"" + APP_ID + "\"\n" +
                "}";
        String date=getCurrentGMTDate();
        String host=getHostFromUrl(url);
        String content_type="application/json";
        String authorization=buildAuthorizationHeader(date,body,url);
        String digest=buildDigest(body);

        //发起请求
        java.net.http.HttpClient httpClient = java.net.http.HttpClient.newHttpClient();
        java.net.http.HttpRequest httpRequest = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create(url))
                .header("Content-Type", content_type)
                .header("Date", date)
                .header("Host", host)
                .header("Authorization", authorization)
                .header("Digest", digest)
                .POST(java.net.http.HttpRequest.BodyPublishers.ofString(body))
                .build();

        try {
            java.net.http.HttpResponse<String> response = httpClient.send(httpRequest, java.net.http.HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            System.out.println("Response: " + responseBody);
            
            // 解析响应JSON，提取upload_id
            // 简单解析JSON响应
            int startIndex = responseBody.indexOf("\"upload_id\": \"") + 13; // "upload_id": "的长度
            int endIndex = responseBody.indexOf("\"", startIndex);
            if (startIndex > 12 && endIndex > startIndex) {
                String uploadId = responseBody.substring(startIndex, endIndex);
                System.out.println("Upload ID: " + uploadId);
                return uploadId;
            } else {
                // 如果没有找到upload_id，抛出异常
                throw new RuntimeException("Failed to extract upload_id from response: " + responseBody);
            }
        } catch (java.io.IOException | InterruptedException e) {
            System.err.println("Error occurred while sending request: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    // 创建任务方法
    public String createTask(String audioUrl,String requestId) throws Exception {
        // 构建请求体JSON
        String body = "{\n" +
                     "\t\"common\": {\n" +
                     "\t\t\"app_id\": \"" + APP_ID + "\"\n" +
                     "\t},\n" +
                     "\t\"business\": {\n" + 
                     "\t\t\"request_id\": \"" + requestId + "\",\n" +
                     "\t\t\"language\": \"zh_cn\",\n" +
                     "\t\t\"domain\": \"pro_ost_ed\",\n" +
                     "\t\t\"accent\": \"mandarin\"\n" +
                     "\t},\n" +
                     "\t\"data\": {\n" +
                     "\t\t\"audio_url\": \"" + audioUrl + "\",\n" +
                     "\t\t\"audio_src\": \"http\",\n" +
                     "\t\t\"format\": \"audio/L16;rate=16000\",\n" +
                     "\t\t\"encoding\": \"raw\"\n" +
                     "\t}\n" +
                     "}";

        // 发送HTTP请求
        java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create(PRO_CREATE_URL))
                .header("Content-Type", "application/json")
                .header("Date", getCurrentGMTDate())
                .header("Authorization", buildAuthorizationHeader(getCurrentGMTDate(), body, PRO_CREATE_URL))
                .POST(java.net.http.HttpRequest.BodyPublishers.ofString(body))
                .build();
        
        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
        System.out.println("Create task response: " + response.body());
        
        // 解析响应，提取task_id
        String responseBody = response.body();
        if (responseBody.contains("task_id")) {
            int startIndex = responseBody.indexOf("\"task_id\": \"") + 11; // "task_id": "的长度
            int endIndex = responseBody.indexOf("\"", startIndex);
            if (startIndex > 10 && endIndex > startIndex) {
                String taskId = responseBody.substring(startIndex, endIndex);
                System.out.println("Task created successfully. Task ID: " + taskId);
                return taskId;
            }
        }
        
        throw new RuntimeException("Failed to create task: " + responseBody);
    }
    
    // 查询任务方法
    public String queryTask(String taskId) throws Exception {
        // 构建请求体JSON
        String body = "{\n" +
                     "\t\"common\": {\n" +
                     "\t\t\"app_id\": \"" + APP_ID + "\"\n" +
                     "\t},\n" +
                     "\t\"business\": {\n" + 
                     "\t\t\"task_id\": \"" + taskId + "\"\n" +
                     "\t}\n" +
                     "}";
        
        // 发送HTTP请求
        java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create(PRO_QUERY_URL))
                .header("Content-Type", "application/json")
                .header("Date", getCurrentGMTDate())
                .header("Authorization", buildAuthorizationHeader(getCurrentGMTDate(), body, PRO_QUERY_URL))
                .POST(java.net.http.HttpRequest.BodyPublishers.ofString(body))
                .build();
        
        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
        System.out.println("Query task response: " + response.body());
        
        // 解析响应，提取识别结果
        String responseBody = response.body();
        
        // 检查任务状态
        if (responseBody.contains("\"task_status\": \"3\"")) {
            System.out.println("Task status: Processing completed");
        } else if (responseBody.contains("\"task_status\": \"4\"")) {
            System.out.println("Task status: Callback completed");
        } else {
            System.out.println("Task is still processing or failed");
            return "Task is still processing or failed";
        }
        
        // 提取完整的识别文字
        String fullText = extractFullText(responseBody);
        System.out.println("Full recognized text: " + fullText);
        
        return fullText;
    }
    
    // 从响应中提取完整文本
    private String extractFullText(String responseBody) {
        try {
            // 寻找lattice数组中的识别结果
            int latticeStart = responseBody.indexOf("\"lattice\"");
            if (latticeStart == -1) {
                return "No recognition result found";
            }
            
            // 从lattice开始查找所有识别的文字
            StringBuilder fullText = new StringBuilder();
            
            // 查找所有的w字段（识别的文字）
            int wIndex = responseBody.indexOf("\"w\":\", latticeStart");
            while (wIndex != -1) {
                int wStart = wIndex + 5; // 跳过"w":" 
                int wEnd = responseBody.indexOf("\"", wStart);
                if (wEnd != -1) {
                    String word = responseBody.substring(wStart, wEnd);
                    // 过滤掉空字符串和特殊标记
                    if (!word.isEmpty() && !word.equals("")) {
                        fullText.append(word);
                    }
                }
                wIndex = responseBody.indexOf("\"w\":\", wEnd");
            }
            
            return fullText.toString();
        } catch (Exception e) {
            System.err.println("Error extracting text from response: " + e.getMessage());
            return "Error extracting text: " + e.getMessage();
        }
    }





    private String buildDigest(String body) throws Exception {
        byte[] bodyBytes = body.getBytes("UTF-8");
        byte[] digestBytes = MessageDigest.getInstance("SHA-256").digest(bodyBytes);
        return "SHA-256=" + Base64.getEncoder().encodeToString(digestBytes);
    }

    // 构建认证头部
    private String buildAuthorizationHeader(String date, String body, String path) throws Exception {
        // 计算body的SHA-256摘要
        byte[] bodyBytes = body.getBytes("UTF-8");
        byte[] digestBytes = MessageDigest.getInstance("SHA-256").digest(bodyBytes);
        String digest = "SHA-256=" + Base64.getEncoder().encodeToString(digestBytes);

        // 构建签名原串
        String host = getHostFromUrl(BASE_URL); // 或根据具体URL获取主机名
        String requestLine = "POST " + path + " HTTP/1.1";
        String signatureOrigin = "host: " + host + "\n" +
                "date: " + date + "\n" +
                requestLine + "\n" +
                "digest: " + digest;

        // 使用hmac-sha256算法签名
        SecretKeySpec secretKeySpec = new SecretKeySpec(API_SECRET.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKeySpec);
        byte[] signatureSha = mac.doFinal(signatureOrigin.getBytes("UTF-8"));

        // base64编码得到最终signature
        String signature = Base64.getEncoder().encodeToString(signatureSha);

        // 拼接authorization字符串
        return String.format("api_key=\"%s\",algorithm=\"hmac-sha256\",headers=\"host date request-line digest\",signature=\"%s\"",
                API_KEY, signature);
    }

    // 获取当前GMT日期
    private String getCurrentGMTDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(new Date());
    }

    // 从URL提取主机名
    private String getHostFromUrl(String url) {
        int start = url.indexOf("//") + 2;
        int end = url.indexOf('/', start);
        if (end == -1) {
            end = url.length();
        }
        return url.substring(start, end);
    }
}