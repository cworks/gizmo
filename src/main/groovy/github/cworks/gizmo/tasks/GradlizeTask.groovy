/**
 * Created with love by corbett.
 * User: corbett
 * Date: 11/7/14
 * Time: 8:26 AM
 */
package github.cworks.gizmo.tasks

import github.cworks.gizmo.Gizmo

/**
 * Do gradle stuff
 */
class GradlizeTask extends GizmoTask {

    /**
     * Gradlize this GizmoProject
     */
    private JavaProjectTask project;

    /**
     * Render gradle assets into a project that's rooted at a location
     * specified by project.root()
     */
    @Override
    void gizIt() {

        // create gradle wrapper directory
        new File(project.root(), "gradle/wrapper").mkdirs();

        Gizmo.renderFromClasspath("/templates/build.gradle",
            project.root() + "/build.gradle",
            [
                description: gizmo.context().getString("description"),
                version: gizmo.context().getString("version"),
                packageName: gizmo.context().getString("packageName")
            ]
        );

        Gizmo.renderFromClasspath("/templates/gradle.properties",
            project.root() + "/gradle.properties",
            [
                projectName: gizmo.context().getString("name"),
                javaVersion: gizmo.context().getString("javaVersion", "8")
            ]
        );

        Gizmo.copyFileFromClasspath("/templates/settings.gradle",
            project.root() + "/settings.gradle"
        );

        Gizmo.copyFileFromClasspath("/templates/profile_dev.gradle",
            project.root() + "/profile_dev.gradle"
        );

        Gizmo.copyFileFromClasspath("/templates/profile_prod.gradle",
            project.root() + "/profile_prod.gradle"
        );

        Gizmo.copyFileFromClasspath("/templates/profile_qa.gradle",
            project.root() + "/profile_qa.gradle"
        );

        Gizmo.copyFileFromClasspath("/templates/gradlew",
            project.root() + "/gradlew"
        );

        Gizmo.copyFileFromClasspath("/templates/gradlew.bat",
            project.root() + "/gradlew.bat"
        );

        Gizmo.copyFileFromClasspath("/templates/gradle/wrapper/gradle-wrapper.jar",
            gradleWrapper("gradle-wrapper.jar")
        );

        Gizmo.copyFileFromClasspath("/templates/gradle/wrapper/gradle-wrapper.properties",
            gradleWrapper("gradle-wrapper.properties")
        );
    }

    /**
     * Create us one of them thar GizmoTasks
     * @param gizmo
     * @param project
     */
    GradlizeTask(final Gizmo gizmo, final JavaProjectTask project) {
        super(gizmo);
        this.project = project;
    }

    def String gradleWrapper(asset) {
        return this.project.root() + "/gradle/wrapper/" + asset;
    }

    def String projectTest(testFile) {
        return this.projectPath.getPath() +
            "/src/test/java/" +
                gizmo.context().getString("packageName").replaceAll("\\.", "/") +
                    "/" + testFile;
    }
}
