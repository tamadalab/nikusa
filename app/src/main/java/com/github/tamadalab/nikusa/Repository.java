package com.github.tamadalab.nikusa;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Repository：元のリポジトリ
 */
public class Repository {
    /**
     * 元のリポジトリのオーナー名とリポジトリ名
     */
    private String nameWithOwner;

    /**
     * 元のリポジトリのURL
     */
    private URL url;

    /**
     * 生きている可能性のあるFork一覧
     */
    private List<Fork> aliveForks;

    /**
     * Repositoryのコンストラクタ
     * @param nameWithOwner
     */
    public Repository(String nameWithOwner) {
        setNameWithOwner(nameWithOwner);
        setUrl();
    }

    /**
     * nameWithOwnerのゲッタ
     */
    public String getNameWithOwner() {
        return this.nameWithOwner;
    }

    /**
     * nameWithOwnerのセッタ
     */
    public void setNameWithOwner(String nameWithOwner) {
        this.nameWithOwner = nameWithOwner;

        return;
    }

    /**
     * urlのゲッタ
     */
    public URL getUrl() {
        return this.url;
    }

    /**
     * urlのセッタ
     */
    public void setUrl() {
        try {
            this.url = new URL("https://github.com/" + this.nameWithOwner);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * aliveForkのゲッタ
     */
    public List<Fork> getAliveForks() {
        return this.aliveForks;
    }
}
