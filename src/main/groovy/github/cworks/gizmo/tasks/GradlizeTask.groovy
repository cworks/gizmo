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

        Gizmo.renderFromClasspath("/templates/gradle/build.gradle",
            project.root() + "/build.gradle",
            [
                description: gizmo.context().getString("description")
            ]
        );

        Gizmo.renderFromClasspath("/templates/gradle/gradle.properties",
            project.root() + "/gradle.properties",
            [
                projectName: gizmo.context().getString("name"),
                javaVersion: gizmo.context().getString("javaVersion", "8"),
                version: gizmo.context().getString("version"),
                packageName: gizmo.context().getString("packageName")
            ]
        );

        Gizmo.copyFileFromClasspath("/templates/gradle/settings.gradle",
            project.root() + "/settings.gradle"
        );

        Gizmo.copyFileFromClasspath("/templates/gradle/profile_dev.gradle",
            project.root() + "/profile_dev.gradle"
        );

        Gizmo.copyFileFromClasspath("/templates/gradle/profile_prod.gradle",
            project.root() + "/profile_prod.gradle"
        );

        Gizmo.copyFileFromClasspath("/templates/gradle/profile_qa.gradle",
            project.root() + "/profile_qa.gradle"
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

        Gizmo.copyFileFromClasspath("/templates/gradle/wrapper/gradle-wrapper.properties",
            gradleWrapper("gradle-wrapper.properties")
        );
    }

    /**
     * Create us one of them thar GizmoTasks
     * @param gizmo
     * @param project
     */
    GradlizeTask(final GizmoApp gizmo, final JavaProjectTask project) {
        super(gizmo);
        this.project = project;
    }

    def String gradleWrapper(asset) {
        return this.project.root() + "/gradle/wrapper/" + asset;
    }

}
