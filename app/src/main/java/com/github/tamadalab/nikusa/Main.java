package com.github.tamadalab.nikusa;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


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
    public static List<String> parseArgument(String[] args) {
        List<String> repositories = new ArrayList<>();

        String currentDirectory = System.getProperty("user.dir");
        String fileName = Paths.get(currentDirectory, args[0]).toString();
        File file = new File(fileName);

        if (file.isFile()) {
            repositories = Main.parseFile(fileName);
        }
        else if(file.isDirectory()) {
            File[] fileList = file.listFiles();
            for (File afile : fileList) {
                repositories.addAll(Main.parseFile(afile.toString()));
            }
        }
        else {
            repositories = Arrays.stream(args).collect(Collectors.toList());
        }

        return repositories;
    }

    public static List<String> parseFile(String fileName) {
        String extension = Main.getFileExtension(fileName);

        switch (extension) {
            case "csv":
                return Main.readCsvFile(new File(fileName));

            case "json":
                return Main.readJsonFile(new File(fileName));
        
            default:
                break;
        }

        return null;
    }

    public static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if(dotIndex >= 0) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return "";
    }

    public static List<String> readCsvFile(File file) {
        List<String> repo = new ArrayList<>();

        try {
                FileInputStream fileInputStream = new FileInputStream(file);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = bufferedReader.readLine();
                while ((line = bufferedReader.readLine()) != null) {
                    String[] aLine = line.split("[,]");
                    String name = aLine[1].replace("\"", "");
                    repo.add(name);
                }
            }
            catch (FileNotFoundException e) { throw new RuntimeException(e); }
            catch (UnsupportedEncodingException e) { throw new RuntimeException(e); }
            catch (IOException e) { throw new RuntimeException(e); }

            return repo;
    }

    public static List<String> readJsonFile(File file) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // JSONファイルを読み込む
            JsonNode rootNode = objectMapper.readTree(file);

            // "feature" -> "data" -> "search" -> "edges" の順にトラバース
            JsonNode edges = rootNode.path("feature").path("data").path("search").path("edges");

            if (!edges.isMissingNode()) {
                List<String> repositories = new ArrayList<>();

                for (JsonNode edge : edges) {
                    JsonNode repositoryNode = edge.path("node").path("nameWithOwner");
                    if (!repositoryNode.isMissingNode()) {
                        repositories.add(repositoryNode.asText());
                    }
                }

                return repositories;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }
}