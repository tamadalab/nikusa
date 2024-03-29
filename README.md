# nikusa

数あるforkのうち、生きているforkを検出するツール。

![nikusa_logo](https://github.com/tamadalab/nikusa/blob/main/images/logo.svg)

https://tegakisozai.com/archives/279 より引用

## 概要

公開されているプロジェクトのオーナーが何らかの理由で呼びかけに反応しなくなることがある。過去にも、管理者権限を移管せずにプロジェクトを離れ、その後オーナーの応答がなくなりプロジェクトが停止してしまったり、無償提供に不満を感じ、管理を放棄しプロジェクトが停止してしまうといった事例がある。このような事態になってしまった際、開発者は後継のプロジェクトを探す必要がある。探す手段としてForkの中から探すことが挙げられるが、大きなプロジェクトになればなるほどForkの数は大きく、自力で見つけることは困難である。そこで、forkされたリポジトリ一覧から元のリポジトリとの差分と最終更新日時を調べることで、生きているリポジトリを探す。

## 設定

シェルの初期化ファイルに，Acsess Tokenを以下の変数名（GITHUB_API_KEY）で設定する．
以下は，`.bash_prifile`の例
```
export GITHUB_API_KEY="API_KEY"
```

## Usage

```
build: gradle build
```

```
Usage: gradle run --args <"owner"/"repository">
or
Usage: java -jar app/build/libs/nikusa.jar <"owner"/"repository">
```
それぞれで，`cahche`ディレクトリ，`result`ディレクトリの保存先が違うので注意．
gradleで動かす場合は`./app/`に，jarファイルを動かす場合は`./`に保存される．

## 活動率の計測
得られたデータに対して以下も取得する。

- 総変更行数
- README.mdの変更行数
- ファイルの追加、削除行数

```
./insight/search_activity_rate_of_fork <"owner"/"repository">
```
