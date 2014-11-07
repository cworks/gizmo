/**
 * Created with love by corbett.
 * User: corbett
 * Date: 11/7/14
 * Time: 8:37 AM
 */
package github.cworks.gizmo.tasks

import github.cworks.gizmo.Gizmo

class GizmoProject {

    private Gizmo gizmo;

    private GizmoProject(Gizmo gizmo) {
        this.gizmo = gizmo;
    }

    static String defaultProjectName() {
        return "newProject"
    }

    static String defaultProjectPath() {
        return System.getProperty("user.dir")
    }

    static String defaultBuildTool() {
        return "gradle"
    }

    static String defaultProjectDescription() {
        return "Gizmo Generated Project on " + new Date();
    }

    static String defaultProjectVersion() {
        return "0.0.1";
    }

    static String defaultPackage() {
        return "github.cworks.app";
    }

    static GizmoProject newProject(Gizmo gizmo) {

        return new GizmoProject(gizmo);
    }

    /**
     * Final method that makes the project
     */
    void create() {

        GizmoTask projectTask = new JavaProjectTask(gizmo);
        projectTask.gizIt();

        if(gizmo.context().getString("buildTool").equals("gradle")) {
            GizmoTask gradlizeTask = new GradlizeTask(gizmo, projectTask);
            gradlizeTask.gizIt();
        }
    }
}
