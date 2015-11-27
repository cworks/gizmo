package github.cworks.gizmo.tasks

import github.cworks.gizmo.GizmoApp;

class MavenizeTask extends GizmoTask {

    def JavaProjectTask project = null;

    /**
     * Create us one of them thar GizmoTasks
     * @param gizmo
     * @param project
     */
    MavenizeTask(final GizmoApp gizmo, final JavaProjectTask project) {
        super(gizmo);
        this.project = project;
    }

    @Override
    void gizIt() {

        Gizmo.renderFromClasspath("/templates/maven/pom.xml",
            project.root() + "/pom.xml",
            [
                projectName: gizmo.context().getString("name"),
                javaVersion: gizmo.context().getString("javaVersion", "8"),
                version: gizmo.context().getString("version"),
                packageName: gizmo.context().getString("packageName")
            ]
        );

    }
}
