/**
 * Created with love by corbett.
 * User: corbett
 * Date: 11/7/14
 * Time: 8:37 AM
 */
package github.cworks.gizmo.tasks

import github.cworks.gizmo.GizmoApp

class GizmoProject {

    private GizmoApp gizmo;

    private GizmoProject(GizmoApp gizmo) {
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

    static GizmoProject newProject(GizmoApp gizmo) {

        return new GizmoProject(gizmo);
    }

    /**
     * Final method that makes the project
     */
    void create() {

        GizmoTask projectTask = new JavaProjectTask(gizmo);
        projectTask.gizIt();

        if(gizmo.context().getString("buildTool").equals("gradle")) {
            new GradlizeTask(gizmo, projectTask).gizIt();
        } else if(gizmo.context().getString("buildTool").equals("maven")) {
            new MavenizeTask(gizmo, projectTask).gizIt();
        }
    }
}
