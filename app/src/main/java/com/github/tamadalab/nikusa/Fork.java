package com.github.tamadalab.nikusa;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

/**
 * Fork：Forkが保持するべきデータを記憶
 */
public class Fork extends Object{
    /**
     * オーナー名とリポジトリ名
     */
    private String nameWithOwner;

    /**
     * ForkリポジトリのURL
     */
    private URL url;

    /**
     * Forkリポジトリが作成された日時
     */
    private Calendar createdAt;

    /**
     * 最新コミットのコミット日時
     */
    private Calendar commitedDate;

    /**
     * Forkリポジトリ作成後のコミットの数
     */
    private Integer commitCountAfterFork;

    /**
     * Forkのコンストラクタ
     */
    public Fork() {}

    /**
     * nameWithOwnerのゲッタ
     * @return オーナー名とリポジトリ名
     */
    public String getNameWithOwner() {
        return this.nameWithOwner;
    }

    /**
     * nameWithOnwerのセッタ
     */
    public void setNameWithOwner(String nameWithOwner) {
        this.nameWithOwner = nameWithOwner;

        return;
    }

    /**
     * urlのゲッタ
     * @return ForkリポジトリのURL
     */
    public URL getUrl() {
        return this.url;
    }

    /**
     * urlのセッタ
     */
    public void setUrl(String url) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        return;
    }

    /**
     * createdAtのゲッタ
     * @return Forkリポジトリが作成された日時
     */
    public Calendar getCreatedAt() {
        return this.createdAt;
    }

    /**
     * createdAtのセッタ
     */
    public void setCreatedAt(Calendar createdAt) {
        this.createdAt = createdAt;

        return;
    }

    /**
     * commitedDateのゲッタ
     * @return 最新コミットのコミット日時
     */
    public Calendar getCommitedDate() {
        return this.commitedDate;
    }

    /**
     * commitedDateのセッタ
     */
    public void setCommitedDate(Calendar commitedDate) {
        this.commitedDate = commitedDate;

        return;
    }

    /**
     * commitCountAfterForkのゲッタ
     * @return Forkリポジトリ作成後のコミットの数
     */
    public Integer getCommitCountAfterFork() {
        return this.commitCountAfterFork;
    }

    /**
     * commitCountAfterForkのセッタ
     */
    public void setCommitCountAfterFork(Integer countAfterFork) {
        this.commitCountAfterFork = commitCountAfterFork;

        return;
    }
}
