#include <stdio.h>
#include <stdlib.h>
typedef struct String{
    char*data;
    int cur;//当前字符串的长度
}String;

int BruteForce(String*text,String*pattern){
    //查找text，看有没有包含 pattern
    int j=0;
    for(int i=0;i<text->cur;i++){
        int pre=i;
        while(j<pattern->cur&&text->data[i]==pattern->data[j]){
            i++;
            j++;
        }
        if(j==pattern->cur){
            return pre;
        }
        i=i-j;
        j=0;
    }
    return -1;
}



// 1. 构建next数组（KMP核心预处理）
// next数组：next[j]表示模式串前j个字符的「最长相等前后缀长度」
void getNext(String *pattern, int *next) {
    int len = pattern->cur;
    next[0] = -1;  // 第一个位置固定为-1（无前后缀）
    int i = 0;     // 模式串指针（遍历）
    int j = -1;    // 最长相等前后缀的长度

    while (i < len - 1) {
        if (j == -1 || pattern->data[i] == pattern->data[j]) {
            i++;
            j++;
            // 优化版next（避免重复匹配，可选，基础版可直接next[i]=j）
            if (pattern->data[i] != pattern->data[j]) {
                next[i] = j;
            } else {
                next[i] = next[j];  // 跳过重复的匹配
            }
        } else {
            j = next[j];  // 不相等则回退j（核心：利用已计算的next值）
        }
    }
}

// 2. KMP匹配核心函数
// 返回值：模式串在文本串中首次出现的起始下标；匹配失败返回-1
int KMP(String *text, String *pattern) {
    int text_len = text->cur;
    int pat_len = pattern->cur;

    // 边界条件：模式串为空 或 文本串比模式串短，直接返回-1
    if (pat_len == 0 || text_len < pat_len) {
        return -1;
    }

    // 初始化next数组
    int *next = (int*)malloc(sizeof(int) * pat_len);
    getNext(pattern, next);

    int i = 0;  // 文本串指针（只前进，不回退！）
    int j = 0;  // 模式串指针

    while (i < text_len && j < pat_len) {
        // 两种情况：j=-1（模式串从头开始） 或 字符匹配成功
        if (j == -1 || text->data[i] == pattern->data[j]) {
            i++;
            j++;
        } else {
            j = next[j];  // 匹配失败，模式串指针回退（文本串i不回退）
        }
    }

    free(next);  // 释放next数组内存

    // 匹配成功：j遍历完模式串，返回起始下标
    if (j == pat_len) {
        return i - j;
    }
    // 匹配失败
    return -1;
}