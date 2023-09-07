package com.github.tamadalab.nikusa;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Main
 */
public class Main extends Object {
    /**
     * データ取得・解析
     * @param args 引数文字列群
     */
    public static void main(String[] args) {
        List<String> repositories = parseArgument(args);

        for (String aRepository : repositories) {
            System.out.printf("%ndata fetching...%n");
            Fetcher.fetch(aRepository);
            System.out.printf("%ndata analying...%n");
            ForkAnalyzer.analyze(aRepository);
        }
    }

    /**
     * 引数を解析する．
     * @param args 引数リスト
     * @return 解析結果
     */
    private static List<String> parseArgument(String[] args) {
        List<String> repositories = new ArrayList<>();

        String currentDirectory = System.getProperty("user.dir");
        String csvFileName = Paths.get(currentDirectory, args[0]).toString();
        File file = new File(csvFileName);

        if (file.isFile()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(csvFileName);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = bufferedReader.readLine();
                while ((line = bufferedReader.readLine()) != null) {
                    String[] aLine = line.split("[,]");
                    String name = aLine[1].replace("\"", "");
                    repositories.add(name);
                }
            }
            catch (FileNotFoundException e) { throw new RuntimeException(e); }
            catch (UnsupportedEncodingException e) { throw new RuntimeException(e); }
            catch (IOException e) { throw new RuntimeException(e); }
        }
        else {
            repositories = Arrays.stream(args).collect(Collectors.toList());
        }

        return repositories;
    }
}