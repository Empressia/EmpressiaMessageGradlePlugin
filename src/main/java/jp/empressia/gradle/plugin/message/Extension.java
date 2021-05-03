package jp.empressia.gradle.plugin.message;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

import javax.inject.Inject;

import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.file.Directory;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.FileCollection;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.plugins.ExtensionAware;
import org.gradle.api.tasks.TaskProvider;

import jp.empressia.gradle.plugin.message.task.EmpressiaMessageTaskSupport;
import jp.empressia.message.generator.MessageGenerator.Configuration;

/**
 * EmpressiaMessageの設定を表現します。
 * @author すふぃあ
 */
public class Extension {

	/** 相対パスを解決するための元となるディレクトリのパスです。 */
	private DirectoryProperty BaseDirectoryPath;
	/** 相対パスを解決するための元となるディレクトリのパスです。 */
	public DirectoryProperty getBaseDirectoryPath() { return this.BaseDirectoryPath; }
	/** 相対パスを解決するための元となるディレクトリのパスです。 */
	public void setBaseDirectoryPath(DirectoryProperty BaseDirectoryPath) { this.BaseDirectoryPath.set(BaseDirectoryPath); }
	/** 相対パスを解決するための元となるディレクトリのパスです。 */
	public void setBaseDirectoryPath(Path BaseDirectoryPath) {
		this.getBaseDirectoryPath().set(BaseDirectoryPath.toFile());
	}
	/** 相対パスを解決するための元となるディレクトリのパスです。 */
	public void setBaseDirectoryPath(File BaseDirectoryPath) {
		this.getBaseDirectoryPath().set(BaseDirectoryPath);
	}
	/** 相対パスを解決するための元となるディレクトリのパスです。 */
	public void setBaseDirectoryPath(String BaseDirectoryPath) {
		this.getBaseDirectoryPath().set(new File(BaseDirectoryPath));
	}
	/** 相対パスを解決するための元となるディレクトリのパスです。 */
	public void BaseDirectoryPath(DirectoryProperty BaseDirectoryPath) { this.getBaseDirectoryPath().set(BaseDirectoryPath); }
	/** 相対パスを解決するための元となるディレクトリのパスです。 */
	public void BaseDirectoryPath(Path BaseDirectoryPath) { this.setBaseDirectoryPath(BaseDirectoryPath); }
	/** 相対パスを解決するための元となるディレクトリのパスです。 */
	public void BaseDirectoryPath(File BaseDirectoryPath) { this.setBaseDirectoryPath(BaseDirectoryPath); }
	/** 相対パスを解決するための元となるディレクトリのパスです。 */
	public void BaseDirectoryPath(String BaseDirectoryPath) { this.setBaseDirectoryPath(BaseDirectoryPath); }

	/** 入力となるメッセージプロパティファイルです。 */
	private FileCollection MessagePropertyFilePaths;
	/** 入力となるメッセージプロパティファイルです。 */
	public FileCollection getMessagePropertyFilePaths() { return this.MessagePropertyFilePaths; }
	/** 入力となるメッセージプロパティファイルです。 */
	public void setMessagePropertyFilePaths(FileCollection MessagePropertyFilePaths) { this.MessagePropertyFilePaths = MessagePropertyFilePaths; }
	/** 入力となるメッセージプロパティファイルです。 */
	public FileCollection MessagePropertyFilePaths() { return this.MessagePropertyFilePaths; }
	/** 入力となるメッセージプロパティファイルです。 */
	public void MessagePropertyFilePaths(FileCollection MessagePropertyFilePaths) { this.setMessagePropertyFilePaths(MessagePropertyFilePaths); }

	/** 著作者です。 */
	private String Author;
	/** 著作者です。 */
	public String getAuthor() { return this.Author; }
	/** 著作者です。 */
	public void setAuthor(String Author) { this.Author = Author; }
	/** 著作者です。 */
	public String Author() { return this.Author; }
	/** 著作者です。 */
	public void Author(String Author) { this.setAuthor(Author); }

	/** 出力先となるソースディレクトリです。 */
	private DirectoryProperty SourceDirectoryPath;
	/** 出力先となるソースディレクトリです。 */
	public DirectoryProperty getSourceDirectoryPath() { return this.SourceDirectoryPath; }
	/** 出力先となるソースディレクトリです。 */
	public void setSourceDirectoryPath(DirectoryProperty SourceDirectoryPath) { this.SourceDirectoryPath.set(SourceDirectoryPath); }
	/** 出力先となるソースディレクトリです。 */
	public void setSourceDirectoryPath(Path SourceDirectoryPath) {
		this.getSourceDirectoryPath().set(SourceDirectoryPath.toFile());
	}
	/** 出力先となるソースディレクトリです。 */
	public void setSourceDirectoryPath(File SourceDirectoryPath) {
		this.getSourceDirectoryPath().set(SourceDirectoryPath);
	}
	/** 出力先となるソースディレクトリです。 */
	public void setSourceDirectoryPath(String SourceDirectoryPath) {
		this.getSourceDirectoryPath().set(new File(SourceDirectoryPath));
	}
	/** 出力先となるソースディレクトリです。 */
	public void SourceDirectoryPath(DirectoryProperty SourceDirectoryPath) { this.getSourceDirectoryPath().set(SourceDirectoryPath); }
	/** 出力先となるソースディレクトリです。 */
	public void SourceDirectoryPath(Path SourceDirectoryPath) { this.setSourceDirectoryPath(SourceDirectoryPath); }
	/** 出力先となるソースディレクトリです。 */
	public void SourceDirectoryPath(File SourceDirectoryPath) { this.setSourceDirectoryPath(SourceDirectoryPath); }
	/** 出力先となるソースディレクトリです。 */
	public void SourceDirectoryPath(String SourceDirectoryPath) { this.setSourceDirectoryPath(SourceDirectoryPath); }

	/** パッケージの名前です。 */
	private String PackageName;
	/** パッケージの名前です。 */
	public String getPackageName() { return this.PackageName; }
	/** パッケージの名前です。 */
	public void setPackageName(String PackageName) { this.PackageName = PackageName; }
	/** パッケージの名前です。 */
	public String PackageName() { return this.PackageName; }
	/** パッケージの名前です。 */
	public void PackageName(String PackageName) { this.setPackageName(PackageName); }

	/** メッセージクラスの名前です。 */
	private String MessageClassName;
	/** メッセージクラスの名前です。 */
	public String getMessageClassName() { return this.MessageClassName; }
	/** メッセージクラスの名前です。 */
	public void setMessageClassName(String MessageClassName) { this.MessageClassName = MessageClassName; }
	/** メッセージクラスの名前です。 */
	public String MessageClassName() { return this.MessageClassName; }
	/** メッセージクラスの名前です。 */
	public void MessageClassName(String MessageClassName) { this.setMessageClassName(MessageClassName); }

	/** 例外クラスの名前です。 */
	private String ExceptionClassName;
	/** 例外クラスの名前です。 */
	public String getExceptionClassName() { return this.ExceptionClassName; }
	/** 例外クラスの名前です。 */
	public void setExceptionClassName(String ExceptionClassName) { this.ExceptionClassName = ExceptionClassName; }
	/** 例外クラスの名前です。 */
	public String ExceptionClassName() { return this.ExceptionClassName; }
	/** 例外クラスの名前です。 */
	public void ExceptionClassName(String ExceptionClassName) { this.setExceptionClassName(ExceptionClassName); }

	/** アダプタークラスの名前です。 */
	private String AdaptorClassName;
	/** アダプタークラスの名前です。 */
	public String getAdaptorClassName() { return this.AdaptorClassName; }
	/** アダプタークラスの名前です。 */
	public void setAdaptorClassName(String AdaptorClassName) { this.AdaptorClassName = AdaptorClassName; }
	/** アダプタークラスの名前です。 */
	public String AdaptorClassName() { return this.AdaptorClassName; }
	/** アダプタークラスの名前です。 */
	public void AdaptorClassName(String AdaptorClassName) { this.setAdaptorClassName(AdaptorClassName); }

	/** 例外クラスを出力するかどうか。 */
	private boolean SuppressOutputExceptionClass;
	/** 例外クラスを出力するかどうか。 */
	public boolean getSuppressOutputExceptionClass() { return this.SuppressOutputExceptionClass; }
	/** 例外クラスを出力するかどうか。 */
	public void setSuppressOutputExceptionClass(boolean SuppressOutputExceptionClass) { this.SuppressOutputExceptionClass = SuppressOutputExceptionClass; }
	/** 例外クラスを出力するかどうか。 */
	public boolean SuppressOutputExceptionClass() { return this.SuppressOutputExceptionClass; }
	/** 例外クラスを出力するかどうか。 */
	public void SuppressOutputExceptionClass(boolean SuppressOutputExceptionClass) { this.setSuppressOutputExceptionClass(SuppressOutputExceptionClass); }

	/** アダプタークラスを出力するかどうか。 */
	private boolean SuppressOutputAdaptorClass;
	/** アダプタークラスを出力するかどうか。 */
	public boolean getSuppressOutputAdaptorClass() { return this.SuppressOutputAdaptorClass; }
	/** アダプタークラスを出力するかどうか。 */
	public void setSuppressOutputAdaptorClass(boolean SuppressOutputAdaptorClass) { this.SuppressOutputAdaptorClass = SuppressOutputAdaptorClass; }
	/** アダプタークラスを出力するかどうか。 */
	public boolean SuppressOutputAdaptorClass() { return this.SuppressOutputAdaptorClass; }
	/** アダプタークラスを出力するかどうか。 */
	public void SuppressOutputAdaptorClass(boolean SuppressOutputAdaptorClass) { this.setSuppressOutputAdaptorClass(SuppressOutputAdaptorClass); }

	/** Generatedアノテーションをコメントアウトするかどうか。 */
	private boolean CommentoutGeneratedAnnotation;
	/** Generatedアノテーションをコメントアウトするかどうか。 */
	public boolean getCommentoutGeneratedAnnotation() { return this.CommentoutGeneratedAnnotation; }
	/** Generatedアノテーションをコメントアウトするかどうか。 */
	public void setCommentoutGeneratedAnnotation(boolean CommentoutGeneratedAnnotation) { this.CommentoutGeneratedAnnotation = CommentoutGeneratedAnnotation; }
	/** Generatedアノテーションをコメントアウトするかどうか。 */
	public boolean CommentoutGeneratedAnnotation() { return this.CommentoutGeneratedAnnotation; }
	/** Generatedアノテーションをコメントアウトするかどうか。 */
	public void CommentoutGeneratedAnnotation(boolean CommentoutGeneratedAnnotation) { this.setCommentoutGeneratedAnnotation(CommentoutGeneratedAnnotation); }

	/** メッセージのIDの文字列表現の定数を作成するかどうか。 */
	private boolean CreateMessageIDConstants;
	/** メッセージのIDの文字列表現の定数を作成するかどうか。 */
	public boolean getCreateMessageIDConstants() { return this.CreateMessageIDConstants; }
	/** メッセージのIDの文字列表現の定数を作成するかどうか。 */
	public void setCreateMessageIDConstants(boolean CreateMessageIDConstants) { this.CreateMessageIDConstants = CreateMessageIDConstants; }
	/** メッセージのIDの文字列表現の定数を作成するかどうか。 */
	public boolean CreateMessageIDConstants() { return this.CreateMessageIDConstants; }
	/** メッセージのIDの文字列表現の定数を作成するかどうか。 */
	public void CreateMessageIDConstants(boolean CreateMessageIDConstants) { this.setCreateMessageIDConstants(CreateMessageIDConstants); }

	/** コンストラクタです。 */
	@Inject
	public Extension(ObjectFactory ObjectFactory) {
		this.BaseDirectoryPath = ObjectFactory.directoryProperty();
		this.MessagePropertyFilePaths = ObjectFactory.fileCollection();
		this.SourceDirectoryPath = ObjectFactory.directoryProperty();
	}

	/** GeneratorのConfiguration形式に変換します。 */
	public Configuration convert() {
		Configuration configuration = new Configuration();
		Directory baseDirectory = this.getBaseDirectoryPath().getOrNull();
		String baseDirectoryPath = (baseDirectory != null) ? baseDirectory.getAsFile().getPath() : null;
		configuration.BaseDirectoryPath = baseDirectoryPath;
		ArrayList<String> messagePropertyFilePaths = new ArrayList<String>();
		for(File f : this.getMessagePropertyFilePaths()) {
			messagePropertyFilePaths.add(f.getPath());
		}
		configuration.MessagePropertyFilePaths = messagePropertyFilePaths.toArray(String[]::new);
		configuration.Author = this.getAuthor();
		Directory sourceDirectory = this.getSourceDirectoryPath().getOrNull();
		String sourceDirectoryPath = (sourceDirectory != null) ? sourceDirectory.getAsFile().getPath() : null;
		configuration.SourceDirectoryPath = sourceDirectoryPath;
		configuration.PackageName = this.getPackageName();
		configuration.MessageClassName = this.getMessageClassName();
		configuration.ExceptionClassName = this.getExceptionClassName();
		configuration.AdaptorClassName = this.getAdaptorClassName();
		configuration.SuppressOutputExceptionClass = this.getSuppressOutputExceptionClass();
		configuration.SuppressOutputAdaptorClass = this.getSuppressOutputAdaptorClass();
		configuration.CommentoutGeneratedAnnotation = this.getCommentoutGeneratedAnnotation();
		configuration.CreateMessageIDConstants = this.getCreateMessageIDConstants();
		return configuration;
	}

}
