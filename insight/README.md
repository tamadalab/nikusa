# 活動率の計測

各フォークリポジトリの特徴量を取得し、それらを元に活動率を計測する。

```bash
app/result/SpongePowered/
└── Configurate
    ├── commitCountAfterFork.csv
    ├── commitedDate.csv
    ├── topPercentFilter.csv
    └── unSorted.csv
```

取得する際は、上記のうち、`unSorted.csv`を入力ファイルとする。

## Usage

事前にnikusaでデータを取得しておく必要がある。
実行は `gradle run` コマンドを用いて実行する。（取得データを`app`ディレクトリに保存する必要があるため。）

```txt
Usage: ./insight [-r owner/repository] [-f] [-m] [-h]
    -r:repository = [owner/repository]
    -f:fetch
    -m:measure
    -h:help
    
    [-r]は必須。
    [-f]と[-m]どちらも指定せず[-r]のみの場合は、fetch、measureどちらも行う。
```

## 特徴量

以下を取得する。

- 総追加行数
- 総削除行数
- README.mdの追加行数
- README.mdの削除行数
- ファイルの追加数
- ファイルの削除数

上記に加え、取得済みである以下の特徴量も活動率の指標とする。

- 最終コミット日時
- フォーク作成以降のコミット数

## 計測方法

### 正規化

最小最大スケーリングで正規化する。

### 重み付け

8つの特徴量があるが、初期段階では全て`0.125`としている。

### 活動率

active rates = x(1)*w(i) + ... + x(i)*w(i) + ... + x(8)*w(8)

x:正規化された特徴量

w:重み付け

## 取得データの保存先

- 特徴量
  - `./"owner"/"repository"/features.csv`
- 活動率
  - `./"owner"/"repository"/active_rates.csv`
