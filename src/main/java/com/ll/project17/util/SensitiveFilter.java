package com.ll.project17.util;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class SensitiveFilter {

   //根节点
    private final TrieNode root = new TrieNode();

    // 替换符号
    private static final String REPLACEMENT = "***";

    @PostConstruct
    public void init() {
        try {
            ClassPathResource resource = new ClassPathResource("sensitive-words.txt");
            InputStream is = resource.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    addWord(line);
                    count++;
                }
            }
            reader.close();
            log.info("敏感词库加载完成，共 {} 个词", count);
        } catch (Exception e) {
            log.error("加载敏感词库失败", e);
        }
    }

    //向 Trie 中添加一个敏感词
    private void addWord(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);
        }
        node.isEnd = true;
    }

   //过滤文本中的敏感词，替换为 ***
    public String filter(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder result = new StringBuilder();
        int begin = 0;  // 当前扫描的起始位置

        while (begin < text.length()) {
            TrieNode node = root;
            int matchEnd = -1;  // 记录匹配到的最长敏感词结束位置

            // 从 begin 位置开始尝试匹配
            for (int i = begin; i < text.length(); i++) {
                char c = text.charAt(i);
                node = node.children.get(c);
                if (node == null) {
                    break;  // 匹配中断
                }
                if (node.isEnd) {
                    matchEnd = i;  // 找到一个敏感词，继续尝试更长的
                }
            }

            if (matchEnd >= 0) {
                // 命中敏感词：追加匹配位置前的内容 + 替换符号
                result.append(text, begin, matchEnd + 1);
                int start = result.length() - (matchEnd - begin + 1);
                result.replace(start, result.length(), REPLACEMENT);
                begin = matchEnd + 1;
            } else {
                // 未命中：保留当前字符，继续下一个
                result.append(text.charAt(begin));
                begin++;
            }
        }

        return result.toString();
    }

   //Trie 节点
    private static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        boolean isEnd;
    }
}