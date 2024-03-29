package com.github.tamadalab.nikusa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.nio.file.Paths;
import java.util.List;

/**
 * Fetcher；データを取得する
 */
public class Fetcher extends Object{
    /**
     * Argoを利用し，データを取得する．
     * @param nameWithOwner リポジトリのオーナー名とリポジトリ名．(owner/name)
     */
    public static void fetch(String nameWithOwner) {
        String currentDirectory = System.getProperty("user.dir") + "/cache";
        String containerDirectory = "/home/Argo/cache";
        String dockerImageName = "ghcr.io/ji-ua/argo:0.0.2";
        String api_key = System.getenv("GITHUB_API_KEY");

        List<String> commands = new ArrayList<>();
        commands.add("docker");
        commands.add("run");
        commands.add("-e");
        commands.add("API_KEY=" + api_key);
        commands.add("-v");
        commands.add(currentDirectory + ":" + containerDirectory);
        commands.add(dockerImageName);
        commands.add("Fork");
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
