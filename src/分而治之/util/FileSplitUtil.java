package 分而治之.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileSplitUtil {
    public static void split(String path, int splitSize) {
        for(int i=0;i<splitSize;i++){
            File file=new File("input/split_"+i+".txt");
            if(!file.exists()){
                try {
                    boolean newFile = file.createNewFile();
                    if(newFile){
                        System.out.println("创建文件成功");
                        continue;
                    }
                    System.out.println("文件已存在");
                    file.delete();
                    file.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        File file=new File(path);
        if(!file.exists()){
            System.out.println("文件不存在");
            return;
        }
        try(BufferedReader br=new BufferedReader(new java.io.FileReader(file))) {
            while(br.readLine()!=null&&!br.readLine().isEmpty()){
                String line = br.readLine();
                String[] split = line.split("\\.");
                if(split.length!=4){
                    System.out.println("输入的ip地址不合法");
                }
                Integer[]result=new Integer[split.length];
                for (int i = 0; i < split.length; i++){
                    if(Integer.parseInt(split[i])>255||Integer.parseInt(split[i])<0){
                        System.out.println("输入的ip地址不合法");
                    }
                    result[i]=Integer.parseInt(split[i]);
                }
                //把这个整数数组的值拼接为一个整数，通过位运算
                int num=0;
                for (int i = 0; i < result.length; i++) {
                    num=num|(result[i]<<(i*8));
                }
                int index=num%splitSize;
                File file1=new File("input/split_"+index+".txt");
                try(java.io.FileWriter fw=new java.io.FileWriter(file1,true)){
                    fw.write(line+"\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("写入成功");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            System.out.println("文件已关闭");
        }
    }
    public static List<String> getBucketFileList(){
        List<String> list=new ArrayList<>();
        //读取input目录下的所有文件
        File file=new File("input");
        if (!file.exists()||!file.isDirectory()){
            System.out.println("input目录不存在");
        }
        for (File file1 : Objects.requireNonNull(file.listFiles())) {
            if (file1.isFile()) {
                list.add(file1.getName());
            }
        }
        return list;
    }
}
