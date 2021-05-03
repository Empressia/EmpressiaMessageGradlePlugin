# Empressia Message Gradle Plugin

## 目次

* [概要](#概要)
* [使い方](#使い方)
* [ライセンス](#ライセンス)
* [使用しているライブラリ](#使用しているライブラリ)

## 概要

Empressia製のメッセージ管理ライブラリ＆ツールであるEmpressia Messageの一部です。  
GeneratorのGradle用プラグインです。  

## 使い方

### プラグインの適用

```groovy
plugins {
	id "jp.empressia.gradle.plugin.message" version "1.0.0";
}
```

※上記のサンプルのバージョンは、最新ではない可能性があります。  

この設定により、generateEmpressiaMessageタスクが追加されます。  
タスクが実行されると、Generatorが実行されます。  
これは、Javaプラグインが先に読み込まれている場合、compileJavaタスクの前に動作します。  

Generatorで生成されたクラスの使い方は、Empressia Messageを参照してください。  

### タスクの設定

empressia.messageで、設定を行い、generateEmpressiaMessageタスクの動作を変更できます。  

```groovy
empressia {
	message {
		// 入力となるメッセージプロパティファイルです。
		messagePropertyFilePaths = files("src/main/resources/message.properties");
		// 著作者です。
		author = "すふぃあ";
		// 出力先となるソースディレクトリです。
		sourceDirectoryPath = file("src/main/java/");
		// パッケージの名前です。
		packageName = "jp.empressia.message.generated";
		// メッセージクラスの名前です。
		messageClassName = "Message";
		// 例外クラスの名前です。
		exceptionClassName = "MessageException";
		// アダプタークラスの名前です。
		adaptorClassName = "Messages";
		// 例外クラスを出力するかどうか。
		suppressOutputExceptionClass = false;
		// アダプタークラスを出力するかどうか。
		suppressOutputAdaptorClass = false;
		// Generatedアノテーションをコメントアウトするかどうか。
		commentoutGeneratedAnnotation = false;
		// メッセージのIDの文字列表現の定数を作成するかどうか。
		createMessageIDConstants = false;
	}
}
```

最新の設定情報は、Extensionクラスを参照してください。  

### 別にタスクを定義する方法

EmpressiaMessageTaskSupportを使用してください。  
これは、setConfigurationすることで、動作を変更することができます。  

例えば、『src/tool/java/』を対象とするタスクを新しく追加する場合は、  
以下のように設定しできます。  

```groovy
task generateToolEmpressiaMessage(type: jp.empressia.gradle.plugin.message.task.EmpressiaMessageTaskSupport) {
	extension {
		messagePropertyFilePaths = files("src/tool/resources/message.properties");
		sourceDirectoryPath = file("src/tool/java/");
	}
}
```

## ライセンス

いつも通りのライセンスです。  
zlibライセンス、MITライセンスでも利用できます。  

ただし、チーム（複数人）で使用する場合は、MITライセンスとしてください。  

## 使用しているライブラリ

* Empressia Message Generator
	* https://github.com/Empressia/EmpressiaMessageGenerator
