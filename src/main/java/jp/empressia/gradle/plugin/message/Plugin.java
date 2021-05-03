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
 * Empressia製のGradle用のプラグインです。
 * @author すふぃあ
 */
public class Plugin implements org.gradle.api.Plugin<Project> {
	
	private static String EXTENSION_GROUP_NAME = "empressia";
	private static String EXTENSION_NAME = "message";
	private static String TASK_NAME = "generateEmpressiaMessage";

	/** コンストラクタです。 */
	public Plugin() {
	}

	/** プラグインをプロジェクトに適用します。 */
	public void apply(Project project) {
		if(project.getExtensions().findByName(EXTENSION_GROUP_NAME) == null) {
			project.getExtensions().create(EXTENSION_GROUP_NAME, EmpressiaExtension.class);
		}
		ExtensionAware empressiaExtension = (ExtensionAware)project.getExtensions().findByName(EXTENSION_GROUP_NAME);
		Extension extension = empressiaExtension.getExtensions().create(EXTENSION_NAME, Extension.class);
		project.getLogger().info("extension {0}.{1} created.", EXTENSION_GROUP_NAME, EXTENSION_NAME);

		TaskProvider<? extends Task> taskProvider = project.getTasks().register(TASK_NAME, EmpressiaMessageTaskSupport.class, (t) -> {
			t.setExtension(extension);
		});
		project.getLogger().info("task {0} registered.", TASK_NAME);

		Task compileJavaTask = project.getTasks().findByName("compileJava");
		if(compileJavaTask != null) {
			compileJavaTask.dependsOn(taskProvider);
			project.getLogger().info("task dependency compileJava -> {0} registered.", TASK_NAME);
		}
	}

	/** グループ用のExtensionを確保するためのクラス。 */
	public static class EmpressiaExtension {
	}

}
