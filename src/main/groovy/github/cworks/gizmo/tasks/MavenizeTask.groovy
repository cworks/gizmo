package github.cworks.gizmo.tasks

import cworks.json.JsonObject
import github.cworks.gizmo.Gizmo

class MavenizeTask extends GizmoTask {

    def JavaProjectTask project = null;

    /**
     * Create us one of them thar GizmoTasks
     * @param context
     * @param project
     */
    MavenizeTask(final JsonObject context, final JavaProjectTask project) {
        super(context);
        this.project = project;
    }

    @Override
    void gizIt() {
        Gizmo.renderFromClasspath("/templates/maven/pom.xml",
            project.root() + "/pom.xml",
            [
                projectName: context.getString("name"),
                javaVersion: context.getString("javaVersion", "8"),
                version: context.getString("version"),
                packageName: context.getString("packageName")
            ]
        );
    }
}
