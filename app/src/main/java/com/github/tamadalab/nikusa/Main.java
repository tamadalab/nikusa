package com.github.tamadalab.nikusa;

/**
 * Main
 */
public class Main extends Object {
    /**
     * データ取得・解析
     * @param args 引数文字列群
     */
    public static void main(String[] args) {
        for (String arg : args) {
            System.out.println("data fetching...");
            Fetcher.fetch(arg);
            System.out.println("data analying...");
            ForkAnalyzer.analyze(arg);
        }
    }
}