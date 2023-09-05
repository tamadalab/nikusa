package com.github.tamadalab.nikusa;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Fork：Forkが保持するべきデータを記憶
 */
public class Fork extends Object {
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
    public Fork(String nameWithOwner, String url, String createdAt, String commitedDate, String commitCountAfterFork) {
        setNameWithOwner(nameWithOwner);
        setUrl(url);
        setCreatedAt(createdAt);
        setCommitedDate(commitedDate);
        setCommitCountAfterFork(commitCountAfterFork);
    }

    /**
     * 自身を文字列で応答
     * @return 自身を表す文字列
     */
    public String toCsvRowString() {
        StringBuffer aBuffer = new StringBuffer();

        aBuffer.append(this.getNameWithOwner());
        aBuffer.append(",");
        aBuffer.append(this.getUrl().toString());
        aBuffer.append(",");
        aBuffer.append(calendarToString(this.getCreatedAt()));
        aBuffer.append(",");
        aBuffer.append(calendarToString(this.getCommitedDate()));
        aBuffer.append(",");
        aBuffer.append(this.getCommitCountAfterFork().toString());

        return aBuffer.toString();
    }

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
    public void setCreatedAt(String createdAt) {
        this.createdAt = stringToCalendar(createdAt);

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
    public void setCommitedDate(String commitedDate) {
        this.commitedDate = stringToCalendar(commitedDate);

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
    public void setCommitCountAfterFork(String countAfterFork) {
        this.commitCountAfterFork = Integer.valueOf(countAfterFork);

        return;
    }

    /**
     * 日付をCalender型に変換する
     * @param date String型の日付
     * @return Calender型の日付
     */
    private Calendar stringToCalendar (String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM/dd HH:mm:ss");
        Date aDate = null;

        try {
            aDate = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(aDate);

        return calendar;
    }

    private String calendarToString (Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return sdf.format(calendar.getTime());
    }
}
