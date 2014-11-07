/**
 * Created with love by corbett.
 * User: corbett
 * Date: 11/7/14
 * Time: 8:33 AM
 */
package github.cworks.gizmo
import github.cworks.gizmo.tasks.GizmoProject
import groovy.text.GStringTemplateEngine
import net.cworks.json.JsonObject
import org.apache.commons.io.FileUtils

class Gizmo {

    /**
     * A way cool input prompt for gizmo
     */
    static final String gizmoPrompt = "(oo) "

    /**
     * Gizmo home directory
     */
    private File gizmoHome = null;

    /**
     * Holds variables and such needed by Gizmo tasks
     */
    private JsonObject context = null;

    /**
     * You got to start it up...with a gizmo home directory as one and only argument
     * @param args
     */
    public static void main(String[] args) {

        Gizmo gizmo = Gizmo.newGizmo(args[0]);
        gizmo.go();
    }

    /**
     * Private creation, needs a gizmo home directory
     * @param gizmoHome
     */
    private Gizmo(File gizmoHome) {
        this.gizmoHome = gizmoHome;
        this.context = new JsonObject();
    }

    /**
     * Creation method
     * @param gizmoHome
     * @return
     */
    static Gizmo newGizmo(String gizmoHome) {
        File dir = new File(gizmoHome);
        if(!dir.exists()) {
            throw new RuntimeException("Can't have a Gizmo without a Gizmo Home.");
        }
        Gizmo gizmo = new Gizmo(dir);
        return gizmo;
    }

    static void copyFile(String source, String target) {
        FileUtils.copyFile(new File(source), new File(target));
    }

    static void copyDirectory(String source, String target) {
        FileUtils.copyDirectory(new File(source), new File(target));
    }

    /**
     * Prompt for some information specified by message argument and return
     * users input as a String.
     * @param message
     * @param defaultValue
     * @return
     */
    static String prompt(String message, String defaultValue = null) {

        return readLine(message, defaultValue);
    }

    /**
     * Render a template to the target and use data in arguments map to
     * resolve variable.
     *
     * @param template
     * @param target
     * @param arguments
     */
    static String render(String template, String target, Map args = [:]) {

        File templateFile = new File(template);

        BufferedReader reader = templateFile?.newReader();
        if(!reader) {
            throw new RuntimeException("Woopsie could not find template: $template");
        }

        String rendered = new GStringTemplateEngine()
            .createTemplate(reader)?.make(args)?.toString();

        FileWriter fw = new FileWriter(target);
        fw.write(rendered);
        fw.close();
    }

    private static readLine(String message, String defaultValue = null) {
        String gizmoMessage = "$gizmoPrompt $message " + (defaultValue ? "[$defaultValue] " : "")
        println("$gizmoMessage (waiting...)")
        return System.in.newReader().readLine() ?: defaultValue
    }

    /**
     * go() is the top-level function call that kicks everything off
     */
    void go() {

        String name = Gizmo.prompt("Project Name: ",
            GizmoProject.defaultProjectName());
        String path = Gizmo.prompt("Project Path: ",
            GizmoProject.defaultProjectPath());
        String description = Gizmo.prompt("Project Description: ",
            GizmoProject.defaultProjectDescription());
        String version = Gizmo.prompt("Project Version: ",
            GizmoProject.defaultProjectVersion());
        String packageName = Gizmo.prompt("Top Package: ",
            GizmoProject.defaultPackage());
        String buildTool = Gizmo.prompt("Gradle or Maven: ",
            GizmoProject.defaultBuildTool());

        this.context().setString("name", name)
            .setString("path", path)
            .setString("description", description)
            .setString("version", version)
            .setString("packageName", packageName)
            .setString("buildTool", buildTool);

        println(this.context().asString())

        GizmoProject.newProject(this).create();

    }

    String getGizmoHome() {
        return this.gizmoHome.getPath();
    }

    String getTemplateFolder() {
        return getGizmoHome() + "/templates/";
    }

    JsonObject context() {
        return this.context;
    }
}
