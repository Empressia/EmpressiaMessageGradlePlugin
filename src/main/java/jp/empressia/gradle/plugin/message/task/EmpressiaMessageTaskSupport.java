package jp.empressia.gradle.plugin.message.task;

import java.io.File;
import java.nio.file.Path;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import org.gradle.work.Incremental;
import org.gradle.work.InputChanges;

import jp.empressia.gradle.plugin.message.Extension;
import jp.empressia.message.generator.MessageGenerator;
import jp.empressia.message.generator.MessageGenerator.ClassInformation;
import jp.empressia.message.generator.MessageGenerator.Configuration;
import jp.empressia.message.generator.MessageGenerator.EmptyClassNameException;
import jp.empressia.message.generator.MessageGenerator.FailedToAutoDetectException;
import jp.empressia.message.generator.MessageGenerator.MissingFileException;
import jp.empressia.message.generator.MessageGenerator.Utilities;

/**
 * Empressia Message用のファイル生成を支援するタスク定義です。
 * @author すふぃあ
 */
public class EmpressiaMessageTaskSupport extends DefaultTask {

	/** 入力となるメッセージプロパティファイルです（Incremental用のプロパティです）。 */
	private ConfigurableFileCollection MessagePropertyFiles;
	/** 入力となるメッセージプロパティファイルです（Incremental用のプロパティです）。 */
	@Incremental
	@InputFiles
	public ConfigurableFileCollection getMessagePropertyFilePaths() { return this.MessagePropertyFiles; }

	/** 著作者です（Incremental用のプロパティです）。 */
	private Property<String> Author;
	/** 著作者です（Incremental用のプロパティです）。 */
	@Optional
	@Input
	public Property<String> getAuthor() { return this.Author; }

	/** メッセージクラスの出力先となるファイルパスです（Incremental用のプロパティです）。 */
	private RegularFileProperty MessageClassFile;
	/** メッセージクラスの出力先となるファイルパスです（Incremental用のプロパティです）。 */
	@Optional
	@OutputFile
	public RegularFileProperty getMessageClassFile() { return this.MessageClassFile; }

	/** メッセージクラスの出力先となるファイルパスです（Incremental用のプロパティです）。 */
	private RegularFileProperty ExceptionClassFile;
	/** メッセージクラスの出力先となるファイルパスです（Incremental用のプロパティです）。 */
	@Optional
	@OutputFile
	public RegularFileProperty getExceptionClassFile() { return this.ExceptionClassFile; }

	/** アダプタークラスの出力先となるファイルパスです（Incremental用のプロパティです）。 */
	private RegularFileProperty AdaptorClassFile;
	/** アダプタークラスの出力先となるファイルパスです（Incremental用のプロパティです）。 */
	@Optional
	@OutputFile
	public RegularFileProperty getAdaptorClassFile() { return this.AdaptorClassFile; }

	/** Generatedアノテーションをコメントアウトするかどうか（Incremental用のプロパティです）。 */
	private Property<Boolean> CommentoutGeneratedAnnotation;
	/** Generatedアノテーションをコメントアウトするかどうか（Incremental用のプロパティです）。 */
	@Optional
	@Input
	public Property<Boolean> getCommentoutGeneratedAnnotation() { return this.CommentoutGeneratedAnnotation; }

	/** メッセージのIDの文字列表現の定数を作成するかどうか（Incremental用のプロパティです）。 */
	private Property<Boolean> CreateMessageIDConstants;
	/** メッセージのIDの文字列表現の定数を作成するかどうか（Incremental用のプロパティです）。 */
	@Optional
	@Input
	public Property<Boolean> getCreateMessageIDConstants() { return this.CreateMessageIDConstants; }

	/** EmpressiaMessageのgenerateタスクを実行するための設定です。 */
	private Configuration Configuration;
	/** EmpressiaMessageのgenerateタスクを実行するための設定です。 */
	@Internal
	public Configuration getConfiguration() { return this.Configuration; }

	/** 設定したときのエラー情報です。 */
	private RuntimeException Exception;
	/** 設定したときのエラー情報です。 */
	@Internal
	public RuntimeException getException() { return this.Exception; }

	/** このタスクの設定をします。 */
	private void setConfiguration(Configuration configuration) {
		try {
			Path sourceDirectoryPath = Utilities.getSourceDirectoryPath(configuration, MessageGenerator.DEFAULT_SOuRTH_DIRECTORY_PATH);
			{
				File[] newMessagePropertyFiles = Stream.of(Utilities.getMessagePropertyFilePaths(configuration)).map(p -> p.toFile()).toArray(File[]::new);
				this.MessagePropertyFiles.setFrom((Object[])newMessagePropertyFiles);
			}
			{
				this.Author.set(configuration.Author);
			}
			{
				ClassInformation messageClassInformation = Utilities.createClassInformation(configuration.MessageClassName, configuration.PackageName, MessageGenerator.DEFAULT_PACKAGE_NAME, MessageGenerator.DEFAULT_MESSAGE_CLASS_NAME);
				Path messageClassFilePath = messageClassInformation.getSourceFilePath(sourceDirectoryPath);
				File newMessageClassFile = messageClassFilePath.toFile();
				this.MessageClassFile.fileValue(newMessageClassFile);
			}
			if(configuration.SuppressOutputExceptionClass) {
				this.ExceptionClassFile.fileValue(null);
			} else {
				ClassInformation exceptionClassInformation = Utilities.createClassInformation(configuration.ExceptionClassName, configuration.PackageName, MessageGenerator.DEFAULT_PACKAGE_NAME, MessageGenerator.DEFAULT_EXCEPTION_CLASS_NAME);
				Path exceptionClassFilePath = exceptionClassInformation.getSourceFilePath(sourceDirectoryPath);
				File newExceptionClassFile = exceptionClassFilePath.toFile();
				this.ExceptionClassFile.fileValue(newExceptionClassFile);
			}
			if(configuration.SuppressOutputAdaptorClass) {
				this.AdaptorClassFile.fileValue(null);
			} else {
				ClassInformation adaptorClassInformation = Utilities.createClassInformation(configuration.AdaptorClassName, configuration.PackageName, MessageGenerator.DEFAULT_PACKAGE_NAME, MessageGenerator.DEFAULT_ADAPTOR_CLASS_NAME);
				Path adaptorClassFilePath = adaptorClassInformation.getSourceFilePath(sourceDirectoryPath);
				File newAdaptorClassFile = adaptorClassFilePath.toFile();
				this.AdaptorClassFile.fileValue(newAdaptorClassFile);
			}
			{
				this.CommentoutGeneratedAnnotation.set(configuration.CommentoutGeneratedAnnotation);
			}
			{
				this.CreateMessageIDConstants.set(configuration.CreateMessageIDConstants);
			}
			this.Configuration = configuration;
		} catch(RuntimeException ex) {
			this.Exception = ex;
		}
	}

	/** このタスクの設定をします。BaseDirectoryPathの指定がない場合は、Projectのディレクトリになります。 */
	public void setExtension(Extension extension) {
		Configuration configuration = extension.convert();
		if(configuration.BaseDirectoryPath == null) {
			this.getProject().getProjectDir();
		}
		this.setConfiguration(configuration);
	}
	/** このタスクの設定をします。BaseDirectoryPathの指定がない場合は、Projectのディレクトリになります。 */
	public void extension(Action<Extension> configureAction) {
		ObjectFactory objectFactory = this.getProject().getObjects();
		Extension extension = new Extension(objectFactory);
		configureAction.execute(extension);
		this.setExtension(extension);
	}

	/** コンストラクタ。 */
	@Inject
	public EmpressiaMessageTaskSupport(ObjectFactory objectFactory) {
		this.setGroup("Help");
		this.setDescription("generate Empressia Message classes.");
		this.MessagePropertyFiles = objectFactory.fileCollection();
		this.Author = objectFactory.property(String.class);
		this.MessageClassFile = objectFactory.fileProperty();
		this.ExceptionClassFile = objectFactory.fileProperty();
		this.AdaptorClassFile = objectFactory.fileProperty();
		this.CommentoutGeneratedAnnotation = objectFactory.property(Boolean.class);
		this.CreateMessageIDConstants = objectFactory.property(Boolean.class);
	}

	/** タスクとして、EmperssiaMessageGeneratorを呼び出します。 */
	@TaskAction
	public void generate(InputChanges inputChanges) {
		// 設定したときの問題を確認する。
		{
			RuntimeException ex = this.Exception;
			if(ex == null) {
			} else if(ex instanceof FailedToAutoDetectException) {
				this.getLogger().info(ex.getMessage(), ex);
				return;
			} else if(ex instanceof MissingFileException) {
				this.getLogger().error(ex.getMessage(), ex);
				throw ex;
			} else if(ex instanceof EmptyClassNameException) {
				this.getLogger().error(ex.getMessage(), ex);
				throw ex;
			} else {
				throw ex;
			}
		}
		new MessageGenerator(this.getConfiguration()).perform();
	}

}
