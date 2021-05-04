package jp.empressia.gradle.plugin.message;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.gradle.api.UncheckedIOException;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;

/**
 * Empressia Message Gralde　Pluginのテストです。
 * @author すふぃあ
 */
public class PluginTests {

	/** Gradleテスト用のクラスパスです。 */
	private static List<File> ClasspathFiles;
	
	@BeforeAll
	public static void prepare() {
		// Visual Studio空のテストの時は、java-gradle-pluginが動いていないから、
		// 必要なメタデータ生成をうごかします。
		String TestFrom = System.getProperty("TestFrom");
		if((TestFrom != null) && (TestFrom.equals("Visual Studio Code"))) {
			GradleRunner.create()
				.withProjectDir(new File(""))
				.withArguments("pluginUnderTestMetadata")
				.build();
		}
		// java-gradle-pluginが生成したテスト用のメタデータから、
		// Gradleテスト用のクラスパスを読み込みます。
		PropertyResourceBundle b;
		Path pluginClasspathSupportFilePath = Path.of("build/pluginUnderTestMetadata/plugin-under-test-metadata.properties");
		try(Reader reader = Files.newBufferedReader(pluginClasspathSupportFilePath, StandardCharsets.UTF_8)) {
			b = new PropertyResourceBundle(reader);
		} catch(IOException ex) {
			throw new UncheckedIOException(ex);
		}
		List<File> classpathFiles = Stream.of(b.getString("implementation-classpath").split("\\s*;\\s*"))
			.map(File::new)
			.collect(Collectors.toList());
		PluginTests.ClasspathFiles = classpathFiles;
	}

	/** 指定されたパスとその配下を削除します。 */
	private void deletePath(Path targetPath) throws IOException {
		if(Files.exists(targetPath) == false) { return; }
		Iterable<Path> paths = Files.walk(targetPath)
			.sorted(Comparator.reverseOrder())::iterator;
		for(Path p : paths) {
			Files.delete(p);
		}
	}

	/** メッセージファイルがなければ、生成されずに正常に終了する。 */
	@Test
	public void メッセージファイルがなければ生成されずに正常に終了する() {
		Path projectDirectoryPath = Path.of("TestProjects", "NoMessageProject");
		Path generatedDirectoryPath = projectDirectoryPath.resolve("src/main/java/jp/empressia/message/generated/");
		try {
			this.deletePath(generatedDirectoryPath);
		} catch(IOException ex) {
			throw new IllegalStateException("テストで使用するパスの準備に失敗しました。", ex);
		}

		File f = projectDirectoryPath.toFile();
		BuildResult result = GradleRunner.create()
			.withPluginClasspath(PluginTests.ClasspathFiles)
			.withProjectDir(f)
			.withArguments("generateEmpressiaMessage")
			.build();
		System.out.println(result.getOutput());
		TaskOutcome taskResult = result.task(":generateEmpressiaMessage").getOutcome();

		assertAll(
			() -> assertThat("失敗していない。", taskResult, oneOf(TaskOutcome.UP_TO_DATE, TaskOutcome.SUCCESS)),
			() -> assertThat("何も生成されていない。", Files.exists(generatedDirectoryPath), is(false))
		);
	}

	/** コンパイルしようとすると、ソースコードが一通り生成されて、コンパイルも正常に終了する。 */
	@Test
	public void コンパイルしようとするとソースコードが一通り生成されてコンパイルも正常に終了する() {
		Path projectDirectoryPath = Path.of("TestProjects", "SimpleMessageProject");
		Path generatedDirectoryPath = projectDirectoryPath.resolve("src/main/java/jp/empressia/message/generated/");
		try {
			this.deletePath(generatedDirectoryPath);
		} catch(IOException ex) {
			throw new IllegalStateException("テストで使用するパスの準備に失敗しました。", ex);
		}

		File f = projectDirectoryPath.toFile();
		f = new File(f.getAbsolutePath());
		BuildResult result = GradleRunner.create()
			.withPluginClasspath(PluginTests.ClasspathFiles)
			.withProjectDir(f)
			.withArguments("compileJava")
			.build();
		System.out.println(result.getOutput());
		TaskOutcome taskResult = result.task(":generateEmpressiaMessage").getOutcome();

		assertAll(
			() -> assertThat("失敗していない。", taskResult, oneOf(TaskOutcome.UP_TO_DATE, TaskOutcome.SUCCESS)),
			() -> assertThat("生成先のディレクトリが作られている。", Files.exists(generatedDirectoryPath), is(true))
		);
	}

	/** タスクを2回実行すると、2回目はUP_TO_DATEになる。 */
	@Test
	public void タスクを2回実行すると2回目はUP_TO_DATEになる() {
		Path projectDirectoryPath = Path.of("TestProjects", "SimpleMessageProject");
		Path generatedDirectoryPath = projectDirectoryPath.resolve("src/main/java/jp/empressia/message/generated/");
		try {
			this.deletePath(generatedDirectoryPath);
		} catch(IOException ex) {
			throw new IllegalStateException("テストで使用するパスの準備に失敗しました。", ex);
		}

		File f = projectDirectoryPath.toFile();
		f = new File(f.getAbsolutePath());
		GradleRunner gr = GradleRunner.create()
			.withPluginClasspath(PluginTests.ClasspathFiles)
			.withProjectDir(f)
			.withArguments("generateEmpressiaMessage");
		BuildResult result1 = gr.build();
		System.out.println(result1.getOutput());
		TaskOutcome taskResult1 = result1.task(":generateEmpressiaMessage").getOutcome();
		BuildResult result2 = gr.build();
		System.out.println(result2.getOutput());
		TaskOutcome taskResult2 = result2.task(":generateEmpressiaMessage").getOutcome();

		assertAll(
			() -> assertThat("1回目が失敗していない。", taskResult1, oneOf(TaskOutcome.UP_TO_DATE, TaskOutcome.SUCCESS)),
			() -> assertThat("2回目がUP_TO_DATEになっている。", taskResult2, is(TaskOutcome.UP_TO_DATE)),
			() -> assertThat("生成先のディレクトリが作られている。", Files.exists(generatedDirectoryPath), is(true))
		);
	}

	/** 設定が反映されたソースコードが生成されてコンパイルできる。 */
	@Test
	public void 設定が反映されたソースコードが生成されてコンパイルできる() {
		Path projectDirectoryPath = Path.of("TestProjects", "ConfiguredProject");
		Path generatedDirectoryPath = projectDirectoryPath.resolve("src/main/java/jp/empressia/message/generated/configured/");
		Path messageSourceFilePath = generatedDirectoryPath.resolve("EMessage.java");
		Path exceptionSourceFilePath = generatedDirectoryPath.resolve("EMessageException.java");
		Path adapterSourceFilePath = generatedDirectoryPath.resolve("EMessages.java");
		try {
			this.deletePath(generatedDirectoryPath);
		} catch(IOException ex) {
			throw new IllegalStateException("テストで使用するパスの準備に失敗しました。", ex);
		}

		File f = projectDirectoryPath.toFile();
		f = new File(f.getAbsolutePath());
		BuildResult result = GradleRunner.create()
			.withPluginClasspath(PluginTests.ClasspathFiles)
			.withProjectDir(f)
			.withArguments("generateEmpressiaMessage", "compileJava")
			.build();
		System.out.println(result.getOutput());
		TaskOutcome taskResult = result.task(":generateEmpressiaMessage").getOutcome();

		assertAll(
			() -> assertThat("失敗していない。", taskResult, oneOf(TaskOutcome.UP_TO_DATE, TaskOutcome.SUCCESS)),
			() -> assertThat("生成先のディレクトリが作られている。", Files.exists(generatedDirectoryPath), is(true)),
			() -> assertThat("名前が変更されたメッセージクラスが作られている。", Files.exists(messageSourceFilePath), is(true)),
			() -> assertThat("名前が変更された例外クラスが作られている。", Files.exists(exceptionSourceFilePath), is(true)),
			() -> assertThat("名前が変更されたアダプタークラスが作られている。", Files.exists(adapterSourceFilePath), is(true)),
			() -> assertThat("Generatedアノテーションのライブラリがなくてもコンパイルできる。", Files.exists(adapterSourceFilePath), is(true))
		);
	}

	/** 設定が反映されて、出力を抑えたソースコードが生成されない。 */
	@Test
	public void 設定が反映されて出力を抑えたソースコードが生成されない() {
		Path projectDirectoryPath = Path.of("TestProjects", "SuppressedProject");
		Path generatedDirectoryPath = projectDirectoryPath.resolve("src/main/java/jp/empressia/message/generated/");
		Path messageSourceFilePath = generatedDirectoryPath.resolve("Message.java");
		Path exceptionSourceFilePath = generatedDirectoryPath.resolve("MessageException.java");
		Path adapterSourceFilePath = generatedDirectoryPath.resolve("Messages.java");
		try {
			this.deletePath(generatedDirectoryPath);
		} catch(IOException ex) {
			throw new IllegalStateException("テストで使用するパスの準備に失敗しました。", ex);
		}

		File f = projectDirectoryPath.toFile();
		f = new File(f.getAbsolutePath());
		BuildResult result = GradleRunner.create()
			.withPluginClasspath(PluginTests.ClasspathFiles)
			.withProjectDir(f)
			.withArguments("generateEmpressiaMessage")
			.build();
		System.out.println(result.getOutput());
		TaskOutcome taskResult = result.task(":generateEmpressiaMessage").getOutcome();

		assertAll(
			() -> assertThat("失敗していない。", taskResult, oneOf(TaskOutcome.UP_TO_DATE, TaskOutcome.SUCCESS)),
			() -> assertThat("生成先のディレクトリが作られている。", Files.exists(generatedDirectoryPath), is(true)),
			() -> assertThat("メッセージクラスが作られている。", Files.exists(messageSourceFilePath), is(true)),
			() -> assertThat("出力が抑えられた例外クラスが作られていない。", Files.exists(exceptionSourceFilePath), is(false)),
			() -> assertThat("出力が抑えられたアダプタークラスが作られていない。", Files.exists(adapterSourceFilePath), is(false))
		);
	}

	/** 別のタスクを登録して動かせる。 */
	@Test
	public void 別のタスクを登録して動かせる() {
		Path projectDirectoryPath = Path.of("TestProjects", "AnotherTaskProject");
		Path generatedDirectoryPath = projectDirectoryPath.resolve("src/tool/java/jp/empressia/message/generated/");
		Path messageSourceFilePath = generatedDirectoryPath.resolve("Message.java");
		Path exceptionSourceFilePath = generatedDirectoryPath.resolve("MessageException.java");
		Path adapterSourceFilePath = generatedDirectoryPath.resolve("Messages.java");
		try {
			this.deletePath(generatedDirectoryPath);
		} catch(IOException ex) {
			throw new IllegalStateException("テストで使用するパスの準備に失敗しました。", ex);
		}

		File f = projectDirectoryPath.toFile();
		f = new File(f.getAbsolutePath());
		BuildResult result = GradleRunner.create()
			.withPluginClasspath(PluginTests.ClasspathFiles)
			.withProjectDir(f)
			.withArguments("generateToolEmpressiaMessage")
			.build();
		System.out.println(result.getOutput());
		TaskOutcome taskResult = result.task(":generateToolEmpressiaMessage").getOutcome();

		assertAll(
			() -> assertThat("失敗していない。", taskResult, oneOf(TaskOutcome.UP_TO_DATE, TaskOutcome.SUCCESS)),
			() -> assertThat("生成先のディレクトリが作られている。", Files.exists(generatedDirectoryPath), is(true)),
			() -> assertThat("メッセージクラスが作られている。", Files.exists(messageSourceFilePath), is(true)),
			() -> assertThat("例外クラスが作られている。", Files.exists(exceptionSourceFilePath), is(true)),
			() -> assertThat("アダプタークラスが作られている。", Files.exists(adapterSourceFilePath), is(true))
		);
	}

}
