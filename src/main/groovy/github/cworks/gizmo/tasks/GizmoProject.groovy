/**
 * Created with love by corbett.
 * User: corbett
 * Date: 11/7/14
 * Time: 8:37 AM
 */
package github.cworks.gizmo.tasks
import cworks.json.JsonObject

class GizmoProject {

    private JsonObject context;

    private GizmoProject(JsonObject context) {
        this.context = context;
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
        return "cworks.app";
    }

    static GizmoProject newProject(JsonObject context) {
        return new GizmoProject(context);
    }

    /**
     * Final method that makes the project
     */
    void create() {

        GizmoTask projectTask = new JavaProjectTask(context);
        projectTask.gizIt();

        if(context.getString("buildTool").equals("gradle")) {
            new GradlizeTask(context, projectTask).gizIt();
        } else if(context.getString("buildTool").equals("maven")) {
            new MavenizeTask(context, projectTask).gizIt();
        }
    }
}
