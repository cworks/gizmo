package github.cworks.gizmo.tasks

import cworks.json.JsonObject
import github.cworks.gizmo.Gizmo

/**
 * Do gradle stuff
 */
class GradlizeTask extends GizmoTask {

    /**
     * Hardwired gradle default version 
     */
    public static final String DEFAULT_GRADLE_VERSION = "2.3"

    /**
     * Gradlize this GizmoProject
     */
    private JavaProjectTask project;
    
    /**
     * Create us one of them thar GizmoTasks
     * @param context
     * @param project
     */
    GradlizeTask(final JsonObject context, final JavaProjectTask project) {
        super(context);
        this.project = project;
    }
    
    /**
     * Render gradle assets into a project that's rooted at a location
     * specified by project.root()
     */
    @Override
    void gizIt() {

        // create gradle wrapper directory
        new File(project.root(), "gradle/wrapper").mkdirs();

        Gizmo.renderFromClasspath("/templates/gradle/build.gradle",
            project.root() + "/build.gradle",
            [
                description: context.getString("description"),
                gradleVersion: context.getString("gradleVersion")
            ]
        );

        Gizmo.renderFromClasspath("/templates/gradle/gradle.properties",
            project.root() + "/gradle.properties",
            [
                projectName: context.getString("name"),
                javaVersion: context.getString("javaVersion", "8"),
                version: context.getString("version"),
                packageName: context.getString("packageName")
            ]
        );

        Gizmo.copyFileFromClasspath("/templates/gradle/settings.gradle",
            project.root() + "/settings.gradle"
        );

        Gizmo.copyFileFromClasspath("/templates/gradle/gradlew",
            project.root() + "/gradlew"
        );

        Gizmo.copyFileFromClasspath("/templates/gradle/gradlew.bat",
            project.root() + "/gradlew.bat"
        );

        Gizmo.copyBinaryFileFromClasspath("/templates/gradle/wrapper/gradle-wrapper.jar",
            gradleWrapper("gradle-wrapper.jar")
        );

        Gizmo.renderFromClasspath("/templates/gradle/wrapper/gradle-wrapper.properties",
            gradleWrapper("gradle-wrapper.properties"),
            [
               gradleVersion: context.getString("gradleVersion")
            ]
        );

        new File(project.root(), "gradlew").setExecutable(true, true);
        new File(project.root(), "gradlew.bat").setExecutable(true, true);
    }
    
    def String gradleWrapper(asset) {
        return this.project.root() + "/gradle/wrapper/" + asset;
    }
}
