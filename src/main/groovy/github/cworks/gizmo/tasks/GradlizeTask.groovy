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

    @Override
    void gizIt() {

        // Gizmo has a baked template engine and will render gradle assets into the project
        // thats rooted at project.root()
        Gizmo.render(gizmo.getTemplateFolder() + "build.gradle.giz",
            project.root() + "/build.gradle",
            [
                    description: gizmo.context().getString("description"),
                    version: gizmo.context().getString("version"),
                    packageName: gizmo.context().getString("packageName")
            ]
        );
        Gizmo.render(gizmo.getTemplateFolder() + "gradle.properties.giz",
            project.root() + "/gradle.properties",
            [
                    projectName: gizmo.context().getString("name"),
                    javaVersion: gizmo.context().getString("javaVersion", "8")
            ]
        );
        Gizmo.render(gizmo.getTemplateFolder() + "settings.gradle.giz",
            project.root() + "/settings.gradle"
        );
        Gizmo.render(gizmo.getTemplateFolder() + "profile_dev.gradle.giz",
            project.root() + "/profile_dev.gradle"
        );
        Gizmo.render(gizmo.getTemplateFolder() + "profile_prod.gradle.giz",
            project.root() + "/profile_prod.gradle"
        );
        Gizmo.render(gizmo.getTemplateFolder() + "profile_qa.gradle.giz",
            project.root() + "/profile_qa.gradle"
        );
        Gizmo.copyFile(gizmo.getTemplateFolder() + "gradlew.giz",
            project.root() + "/gradlew"
        );
        Gizmo.copyFile(gizmo.getTemplateFolder() + "gradlew.bat.giz",
            project.root() + "/gradlew.bat"
        );
        Gizmo.copyDirectory(gizmo.getTemplateFolder() + "gradle",
             project.root() + "/gradle")

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
}
