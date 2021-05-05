package jp.empressia.gradle.plugin.message;

import org.gradle.api.Action;
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
		project.getLogger().info("extension {}.{} created.", EXTENSION_GROUP_NAME, EXTENSION_NAME);

		TaskProvider<? extends Task> taskProvider = project.getTasks().register(TASK_NAME, EmpressiaMessageTaskSupport.class, (t) -> {
			t.setExtension(extension);
		});
		project.getLogger().info("task {} registered.", TASK_NAME);

		Action<Task> taskDependencyRegister = (t) -> {
			if(t.getName().equals("compileJava")) {
				t.dependsOn(taskProvider);
			}
			project.getLogger().info("task dependency {} -> {} registered.", t.getName(), TASK_NAME);
		};
		Task compileJavaTask = project.getTasks().findByName("compileJava");
		if(compileJavaTask != null) {
			taskDependencyRegister.execute(compileJavaTask);
		} else {
			project.getTasks().whenTaskAdded(taskDependencyRegister);
		}
	}

	/** グループ用のExtensionを確保するためのクラス。 */
	public static class EmpressiaExtension {
	}

}
