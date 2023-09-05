package com.github.tamadalab.nikusa;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * ForkAnalyzer：Fork一覧を分析する
 */
public class ForkAnalyzer {
    /**
     * クラス変数：生きているForkを記憶するリスト
     */
    static List<Fork> forks = new ArrayList<Fork>();

    /**
     * 解析する
     * @param nameWithOwner オーナー名/リポジトリ名
     */
    public static void analyze(String nameWithOwner) {
        String[] splitedRepoName = nameWithOwner.split("[/]");
        ForkAnalyzer.readCacheCSV(splitedRepoName[0], splitedRepoName[1]);
        ForkAnalyzer.writeSortedCSV(splitedRepoName[0], splitedRepoName[1]);
    }

    /**
     * CSVファイルの読み込み
     * @param nameWithOwner オーナー名/リポジトリ名
     */
    private static void readCacheCSV(String owner, String repository) {
        String currentDirectory = System.getProperty("user.dir");
        String csvFileName = Paths.get(currentDirectory, "cache", owner, repository, "Fork", "CSV", "total.csv").toString();

        try {
            FileInputStream fileInputStream = new FileInputStream(csvFileName);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String aLine = bufferedReader.readLine(); // 1行目はヘッダーのため，読み飛ばす
            while ((aLine = bufferedReader.readLine()) != null)  {
                String[] anArray = aLine.split("[,]");
                Fork aFork = new Fork(anArray[0], anArray[1], anArray[2],anArray[3], anArray[4]);
                ForkAnalyzer.forks.add(aFork);
            }

            bufferedReader.close();
        }
        catch (FileNotFoundException e) { throw new RuntimeException(e); }
        catch (UnsupportedEncodingException e) { throw new RuntimeException(e); }
        catch (IOException e) { throw new RuntimeException(e); }
    }

    /**
     * ソートしCSVファイルに書き込む
     */
    private static void writeSortedCSV(String owner, String repository) {
        write(owner, repository ,Sort.commitCountAfterFork);
    }

    private static void write(String owner, String repository,Sort sortType) {
        String currentDirectory = System.getProperty("user.dir");
        Path resultDirectoryPath = Paths.get(currentDirectory, "result", owner, repository);
        mkdir(resultDirectoryPath);

        String csvFileName = Paths.get(resultDirectoryPath.toString(), sortType.toString().concat(".csv")).toString();

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(csvFileName);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

            bufferedWriter.write("nameWithOwner,url,createdAt,commitedDate,commitCountAfterFork");
            bufferedWriter.newLine();

            List<Fork> sortedForks = sort(sortType);

            for (Fork aFork : sortedForks) {
                bufferedWriter.write(aFork.toCsvRowString());
                bufferedWriter.newLine();
            }

            bufferedWriter.close();

            System.out.printf("writen %s%n", csvFileName);
        }
        catch (FileNotFoundException anException) { anException.printStackTrace(); }
        catch (UnsupportedEncodingException anException) { anException.printStackTrace(); }
        catch (IOException anException) { anException.printStackTrace(); }
    }

    /**
     * ディレクトリを作成する．
     * @param path 作りたいディレクトリパス
     */
    private static void mkdir(Path path) {
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static List<Fork> sort(Sort sortType) {
        List<Fork> listOfRemoveZeroCommitFork = ForkAnalyzer.forks;

        listOfRemoveZeroCommitFork.removeIf(fork -> fork.getCommitCountAfterFork() <= 0);


        switch (sortType) {
            /**
             * fork作成後のコミット数を降順でソート
             */
            case commitCountAfterFork:
                Collections.sort(listOfRemoveZeroCommitFork, new Comparator<Fork>() {
                    @Override
                    public int compare(Fork f1, Fork f2) {
                        return Integer.compare(f2.getCommitCountAfterFork(), f1.getCommitCountAfterFork());
                    }
                });
        }

        return listOfRemoveZeroCommitFork;
    }

}
