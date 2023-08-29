package com.github.tamadalab.nikusa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.nio.file.Paths;
import java.util.List;

/**
 * Fetcher；Argoを利用しデータを取得する
 */
public class Fetcher extends Object{
    /**
     * Argoを利用し，データを取得する．
     * @param nameWithOwner リポジトリのオーナー名とリポジトリ名．(owner/name)
     */
    public static void fetch(String nameWithOwner) {
        String currentDirectory = Paths.get(System.getProperty("user.dir"), "/cache").toString();
        String containerDirectory = Paths.get("home", "Argo", "cache").toAbsolutePath().toString();
        String dockerImageName = Paths.get("ghcr.io", "ji-ua", "argo:0.0.2").toString();

        List<String> commands = new ArrayList<>();
        commands.add("docker");
        commands.add("run");
        commands.add("-v");
        commands.add(currentDirectory + ":" + containerDirectory);
        commands.add(dockerImageName);
        commands.add(nameWithOwner);

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(commands);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // コンテナの標準出力をリアルタイムで読み取る
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            process.waitFor();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
