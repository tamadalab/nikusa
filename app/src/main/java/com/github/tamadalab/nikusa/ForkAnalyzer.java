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
     * @param owner
     * @param repository
     */
    private static void readCacheCSV(String owner, String repository) {
        String currentDirectory = System.getProperty("user.dir");
        String csvFileName = Paths.get(currentDirectory, "cache", owner, repository, "Fork", "CSV", "total.csv").toString();

        if (!new File(csvFileName).exists()) {
            System.out.printf("%s dose not exist.%n", csvFileName);
            return;
        }

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
     * ソート&書き込み
     */
    private static void writeSortedCSV(String owner, String repository) {
        String currentDirectory = System.getProperty("user.dir");
        Path resultDirectoryPath = Paths.get(currentDirectory, "result", owner, repository);
        mkdir(resultDirectoryPath);
        ForkAnalyzer.forks.removeIf(fork -> fork.getCommitCountAfterFork() <= 0);
        writeUnsortedFroks(resultDirectoryPath.toString());
        writeSortedForks(resultDirectoryPath.toString() ,Sort.commitCountAfterFork);
        writeSortedForks(resultDirectoryPath.toString() ,Sort.commitedDate);
        writeSortedForks(resultDirectoryPath.toString() ,Sort.topPercentFilter);
    }

    private static void writeUnsortedFroks(String resultDirectoryPath) {
        String csvFileName = Paths.get(resultDirectoryPath, "unSorted.csv").toString();

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(csvFileName);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

            bufferedWriter.write("nameWithOwner,url,createdAt,commitedDate,commitCountAfterFork");
            bufferedWriter.newLine();

            for (Fork aFork : ForkAnalyzer.forks) {
                bufferedWriter.write(aFork.toCsvRowString());
                bufferedWriter.newLine();
            }

            bufferedWriter.close();

            System.out.printf("wrote %s%n", csvFileName);
        }
        catch (FileNotFoundException anException) { anException.printStackTrace(); }
        catch (UnsupportedEncodingException anException) { anException.printStackTrace(); }
        catch (IOException anException) { anException.printStackTrace(); }
    }

    /**
     * ソートしファイルを書き込む
     * @param resultDirectoryPath 保存するディレクトリパス
     * @param sortType ソート方法
     */
    private static void writeSortedForks(String resultDirectoryPath, Sort sortType) {
        String csvFileName = Paths.get(resultDirectoryPath, sortType.toString().concat(".csv")).toString();

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(csvFileName);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

            bufferedWriter.write("nameWithOwner,url,createdAt,commitedDate,commitCountAfterFork");
            bufferedWriter.newLine();

            List<Fork> sortedForks = sort(sortType, ForkAnalyzer.forks);

            for (Fork aFork : sortedForks) {
                bufferedWriter.write(aFork.toCsvRowString());
                bufferedWriter.newLine();
            }

            bufferedWriter.close();

            System.out.printf("wrote %s%n", csvFileName);
        }
        catch (FileNotFoundException anException) { anException.printStackTrace(); }
        catch (UnsupportedEncodingException anException) { anException.printStackTrace(); }
        catch (IOException anException) { anException.printStackTrace(); }
    }

    /**
     * ディレクトリを作成する．
     * @param path 作成したいディレクトリパス
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

    /**
     * 指定されたソート方法でソートを行う
     * @param sortType ソート方法
     * @return ソートされたForkリスト
     */
    private static List<Fork> sort(Sort sortType, List<Fork> forks) {
        switch (sortType) {
            case commitCountAfterFork:
                Collections.sort(forks, new Comparator<Fork>() {
                    @Override
                    public int compare(Fork f1, Fork f2) {
                        return Integer.compare(f2.getCommitCountAfterFork(), f1.getCommitCountAfterFork());
                    }
                });
                break;

            case commitedDate:
                Collections.sort(forks, new Comparator<Fork>() {
                    @Override
                    public int compare(Fork f1, Fork f2) {
                        if (f1.getCommitedDate() == null || f2.getCommitedDate() == null) {
                            return 0;
                        }
                        if (f1.getCommitedDate() == null) {
                            return 1;
                        }
                        if (f2.getCommitedDate() == null) {
                            return -1;
                        }
                        return f2.getCommitedDate().compareTo(f1.getCommitedDate());
                    }
                });
                break;

            case topPercentFilter:
                List<Fork> sortedForks = sort(Sort.commitCountAfterFork, forks);

                Integer percent = 10;
                Integer cutOffIndex = (Integer) (sortedForks.size() * 10 / 100);

                List<Fork> topForks = sortedForks.subList(0,cutOffIndex);

                return  sort(Sort.commitedDate, topForks);

            default:
                break;
        }

        return forks;
    }

}
