package datastructure1;

import java.util.*;

/*public class 倒排索引 {
    // 倒排表：Map<关键词, Set<文档ID>>
    private final Map<String, Set<Integer>> index = new HashMap<>();

    // 文档总数
    private int docCount = 0;

    *//**
     * 添加文档到索引
     * @param docId 文档ID
     * @param content 文档内容
     *//*
    public void addDocument(int docId, String content) {
        // 简单分词：按空格分割并转为小写
        String[] terms = content.toLowerCase().split("\\s+");

        // 为每个词添加到倒排表
        for (String term : terms) {
            // 跳过空词
            if (term.isEmpty()) continue;

            // 如果索引中不存在该词，创建新的文档集合
            index.computeIfAbsent(term, k -> new HashSet<>())
                    .add(docId);
        }

        docCount++;
    }

    *//**
     * 根据关键词查询包含的文档ID集合
     * @param term 关键词（小写）
     * @return 文档ID集合，若关键词不存在返回空集合
     *//*
    public Set<Integer> search(String term) {
        return index.getOrDefault(term.toLowerCase(), Collections.emptySet());
    }

    *//**
     * 批量查询多个关键词的交集
     * @param terms 关键词列表
     * @return 同时包含所有关键词的文档ID集合
     *//*
    public Set<Integer> searchAll(String... terms) {
        // 按关键词频率排序，优先处理频率低的词以减少计算量
        List<String> sortedTerms = Arrays.stream(terms)
                .map(String::toLowerCase)
                .sorted(Comparator.comparingInt(t -> index.getOrDefault(t, Collections.emptySet()).size()))
                .toList();

        // 从第一个关键词的文档集合开始求交集
        Set<Integer> result = new HashSet<>(search(sortedTerms.get(0)));

        // 依次与后续关键词的文档集合求交集
        for (int i = 1; i < sortedTerms.size(); i++) {
            result.retainAll(search(sortedTerms.get(i)));

            // 如果交集已为空，提前终止
            if (result.isEmpty()) break;
        }

        return result;
    }

    *//**
     * 获取索引中所有关键词
     *//*
    public Set<String> getTerms() {
        return index.keySet();
    }

    *//**
     * 获取文档总数
     *//*
    public int getDocCount() {
        return docCount;
    }

    *//**
     * 获取关键词的文档频率（DF）
     *//*
    public int getDocumentFrequency(String term) {
        return index.getOrDefault(term.toLowerCase(), Collections.emptySet()).size();
    }

    *//**
     * 打印倒排索引结构（调试用）
     *//*
    public void printIndex() {
        for (Map.Entry<String, Set<Integer>> entry : index.entrySet()) {
            System.out.println("Term: " + entry.getKey() + ", Docs: " + entry.getValue());
        }
    }
}*/


/*
倒排索引（Inverted Index）的概念与原理
倒排索引是搜索引擎的核心数据结构，它之所以被称为 "倒排"，是因为它与传统的"正向索引" 方向相反。下面通过对比和实例来解释这个概念。
        1. 正向索引（Forward Index）
传统的索引方式是 "文档→内容" 的映射，例如：

文档 1："苹果公司发布了新款 iPhone"
文档 2："iPhone 的拍照功能非常出色"
文档 3："华为和苹果都是知名手机品牌"

低重复率场景：如果所有文档的词条几乎无重复（比如每个文档都是独特的专业术语），
倒排索引的 “词条聚合” 优势消失，反而会因为要存储词条→文档的映射关系（额外的指针 / 索引结构），
比正向索引（直接按文档存储词条列表）更费空间。

正向索引结构：

plaintext
文档1 → [苹果, 公司, 发布, 了, 新款, iPhone]
文档2 → [iPhone, 的, 拍照, 功能, 非常, 出色]
文档3 → [华为, 和, 苹果, 都, 是, 知名, 手机, 品牌]

缺点：
当用户搜索关键词（如 "iPhone"）时，需要遍历所有文档的内容，时间复杂度为 O(n)（n 为文档数），在海量数据下效率极低。
        2. 倒排索引（Inverted Index）
倒排索引将映射关系反转，变成 "内容→文档" 的结构：

倒排索引结构：

plaintext
苹果 → [文档1, 文档3]
iPhone → [文档1, 文档2]
华为 → [文档3]
发布 → [文档1]
拍照 → [文档2]
        ...

更详细的结构通常包含：

词条（Term）：经过分词后的单词（如 "苹果"、"iPhone"）
文档频率（DF）：该词条出现的文档数量
倒排列表（Posting List）：包含每个文档的：
文档 ID
词频（Term Frequency）：词条在文档中出现的次数
位置信息（Position）：词条在文档中的位置（用于短语查询）
偏移量（Offset）：词条在原文中的起始和结束位置

示例：

plaintext
iPhone →
文档频率: 2
倒排列表: [
        {文档ID: 1, 词频: 1, 位置: [5]},
        {文档ID: 2, 词频: 1, 位置: [1]}
        ]
        3. 为什么叫 "倒排"？
正向索引：从文档出发，索引其包含的内容
倒排索引：从内容出发，索引其出现的文档

这种反转的结构使得：

查询效率大幅提升：搜索 "iPhone" 时，直接定位到对应的倒排列表，时间复杂度接近 O(1)
空间占用更小：通过压缩技术（如 FST）优化词条存储
4. 倒排索引的组成部分
倒排索引通常由两部分组成：
        4.1 词条字典（Term Dictionary）
存储所有词条的字典，支持快速查找。在 Elasticsearch 中，使用 **FST（有限状态转移器）** 实现：

支持前缀匹配（如查找以 "苹果" 开头的所有词条）
压缩存储，减少内存占用
        支持快速定位词条
4.2 倒排列表（Posting List）
存储每个词条对应的文档列表。通常进行以下优化：

压缩存储：使用增量编码（如 Delta 编码）压缩文档 ID
分块存储：将倒排列表分成多个块，块内使用统一的压缩策略
索引缓存：热门词条的倒排列表会被缓存到内存中
5. 倒排索引的优缺点
优点：
查询效率极高：单次查询时间复杂度接近 O (1)
适合全文检索：支持关键词搜索、短语查询、前缀查询等
空间利用率高：通过压缩技术减少存储开销
缺点：
写入性能较差：每次新增文档都需要更新倒排索引，涉及复杂的分词和索引构建过程
不适合实时数据：索引更新存在延迟（ES 默认 1 秒刷新一次）
不适合范围查询：对数值范围查询（如 "价格在 100-200 之间"）支持较弱（需结合 BKD 树等结构）*/
