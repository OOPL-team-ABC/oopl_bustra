# gitの使い方

- 基本的にはターミナルorコマンドプロンプトで操作する
- git --versionでインストールされているかわかる(インストールされているとgit version 2.~とでる)

## はじめに
- 作業リポジトリを持ってくる
```
  git clone git@github.com:OOPL-team-ABC/oopl_bustra.git
  git clone https://github.com/OOPL-team-ABC/oopl_bustra.git
  どちらかでできると思う
  Receiving objects: 100% (3/3), done.
  みたいなのが出たら成功
```
- git branch 名前でブランチ作成
- git switch さっき作ったブランチ名でブランチ移動
- あとはここで作業する

## 大体の流れ
- コード作成 例:test.java
- git add 保存したいファイル名
  - git add test.java 
  - gti add *で全部
- コミットメッセージを書く
  - git commit
  - vimが開くのでiを押して変更内容を入力
  - escを押して:wqで終了
- リモートに保存
  - git push origin さっき作ったブランチ名


