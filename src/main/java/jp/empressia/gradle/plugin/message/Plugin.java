package jp.empressia.gradle.plugin.message;

import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.plugins.ExtensionAware;
import org.gradle.api.tasks.TaskProvider;

import jp.empressia.gradle.plugin.message.task.EmpressiaMessageTaskSupport;

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
		project.getLogger().debug("extension {}.{} created.", EXTENSION_GROUP_NAME, EXTENSION_NAME);

		TaskProvider<? extends Task> taskProvider = project.getTasks().register(TASK_NAME, EmpressiaMessageTaskSupport.class, (t) -> {
			t.setExtension(extension);
		});
		project.getLogger().debug("task {} registered.", TASK_NAME);

		Task compileJavaTask = project.getTasks().findByName("compileJava");
		if(compileJavaTask != null) {
			compileJavaTask.dependsOn(taskProvider);
			project.getLogger().debug("task dependency compileJava -> {} registered.", TASK_NAME);
		}
	}

	/** グループ用のExtensionを確保するためのクラス。 */
	public static class EmpressiaExtension {
	}

}
