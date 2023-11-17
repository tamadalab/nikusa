#!/usr/bin/env python3

import pandas as pd
import sys

def print_usage():
    print("Usage: python script.py <arg1> <arg2> ...")
    print("<arg1>: csvファイル")
    # 必要に応じて更に詳細を追加

if len(sys.argv) <= 1:  # スクリプト名のみが引数の場合
    print_usage()
    sys.exit(1)  # エラー終了

# CSVファイルのパス
csv_file_path = sys.argv[1]

# CSVファイルからデータを読み込む
df = pd.read_csv(csv_file_path)

# commitedDateを日付型に変換してUnixタイムスタンプにする
df['commitedDate'] = pd.to_datetime(df['commitedDate'])
df['commitedDate_unix'] = df['commitedDate'].astype('int64') // 10**9

# 最小最大スケーリングで各指標を正規化
df['commitedDate_norm'] = (df['commitedDate_unix'] - df['commitedDate_unix'].min()) / (df['commitedDate_unix'].max() - df['commitedDate_unix'].min())
df['commit_count_norm'] = (df['commitCountAfterFork'] - df['commitCountAfterFork'].min()) / (df['commitCountAfterFork'].max() - df['commitCountAfterFork'].min())
df['insertion_lines_norm'] = (df['number_of_insertion_lines'] - df['number_of_insertion_lines'].min()) / (df['number_of_insertion_lines'].max() - df['number_of_insertion_lines'].min())
df['deletion_lines_norm'] = (df['number_of_deletion_lines'] - df['number_of_deletion_lines'].min()) / (df['number_of_deletion_lines'].max() - df['number_of_deletion_lines'].min())
df['insertion_lines_readme_norm'] = (df['number_of_insertion_lines_in_readme'] - df['number_of_insertion_lines_in_readme'].min()) / (df['number_of_insertion_lines_in_readme'].max() - df['number_of_insertion_lines_in_readme'].min())
df['deletion_lines_readme_norm'] = (df['number_of_deletion_lines_in_readme'] - df['number_of_deletion_lines_in_readme'].min()) / (df['number_of_deletion_lines_in_readme'].max() - df['number_of_deletion_lines_in_readme'].min())
df['create_files_norm'] = (df['number_of_create_files'] - df['number_of_create_files'].min()) / (df['number_of_create_files'].max() - df['number_of_create_files'].min())
df['delete_files_norm'] = (df['number_of_delete_files'] - df['number_of_delete_files'].min()) / (df['number_of_delete_files'].max() - df['number_of_delete_files'].min())

# 重み付けとスコアの算出
weights = {
    'commitedDate': 0.125,
    'commit_count': 0.125,
    'insertion_lines': 0.125,
    'deletion_lines': 0.125,
    'insertion_lines_readme': 0.125,
    'deletion_lines_readme': 0.125,
    'create_files': 0.125,
    'delete_files': 0.125
}

df['score'] = (df['commit_count_norm'] * weights['commit_count'] +
               df['insertion_lines_norm'] * weights['insertion_lines'] +
               df['deletion_lines_norm'] * weights['deletion_lines'] +
               df['insertion_lines_readme_norm'] * weights['insertion_lines_readme'] +
               df['deletion_lines_readme_norm'] * weights['deletion_lines_readme'] +
               df['create_files_norm'] * weights['create_files'] +
               df['delete_files_norm'] * weights['delete_files'] +
               df['commitedDate_norm'] * weights['commitedDate'])

# スコアでソートして上位のForkを表示
# top_forks = df.sort_values(by='score', ascending=False)
# print(top_forks[['fork_name', 'score']])

# CSVファイルとして保存
output_file_path = 'test_result.csv'
df.sort_values(by='score', ascending=False).to_csv(output_file_path, index=False)