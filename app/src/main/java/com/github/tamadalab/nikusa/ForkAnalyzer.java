package com.github.tamadalab.nikusa;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
        readCacheCSV(nameWithOwner);


    }

    /**
     * CSVファイルの読み込み
     * @param nameWithOwner オーナー名/リポジトリ名
     */
    private static void readCacheCSV(String nameWithOwner) {
        String[] splitRepoName = nameWithOwner.split("[/]");

        String currentDirectory = System.getProperty("user.dir");
        String csvFileName = Paths.get(currentDirectory, splitRepoName[0], splitRepoName[1], "CSV").toString();

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


    private static void writeSortedCSV() {

    }

}
