package github.cworks.gizmo

import github.cworks.gizmo.tasks.GizmoProject
import groovy.text.GStringTemplateEngine
import net.cworks.json.JsonObject

class Gizmo {

    /**
     * Gizmo home directory
     */
    def File gizmoHome = null;

    /**
     * Holds variables and such needed by Gizmo tasks
     */
    def JsonObject context = null;

    /**
     * Terminal for prompting and processing commands
     */
    def Terminal terminal = new Terminal("(gizmo)", "Welcome to Gizmo", "Sinaria boss");;

    /**
     * You got to start it up...with a gizmo home directory as one and only argument
     * @param args
     */
    public static void main(String[] args) {
        Gizmo gizmo = new Gizmo();
        gizmo.wizard();
    }

    private Gizmo() {
        this.context = new JsonObject();
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

    static copyFileFromClasspath(String source, String target) {
        BufferedReader reader = Gizmo.class.getResource(source).newReader();
        if(!reader) {
            throw new RuntimeException("Woopsie could not read file from classpath: $source");
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(target));
        if(!writer) {
            throw new RuntimeException("Woopsie could not create writer for: $target");
        }

        reader.eachLine { line ->
            writer.writeLine(line);
        };

        writer.close();
        reader.close();
    }

    static copyBinaryFileFromClasspath(String source, String target) {
        InputStream input = Gizmo.class.getResource(source).newInputStream();
        if(!input) {
            throw new RuntimeException("Woopsie could not obtain input stream classpath: $source");
        }

        OutputStream output = new BufferedOutputStream(new FileOutputStream(target));
        if(!output) {
            throw new RuntimeException("Woopsie could not write file for: $target");
        }

        def byteCount = 0;
        try {
            def buffer = new byte[4096];
            def bytesRead = -1;
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
                byteCount += bytesRead;
            }
            output.flush();
        } finally {
            try { input?.close(); } catch (Exception ex) { }
            try { output?.close(); } catch (IOException ex) { }
        }

        return byteCount;
    }

    /**
     * Render a template to the target and use data in arguments map to
     * resolve variable.
     *
     * @param template
     * @param target
     * @param arguments
     */
    static render(String template, String target, Map args = [:]) {

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
        reader.close();
    }

    static renderFromClasspath(String template, String target, Map args = [:]) {

        BufferedReader reader = Gizmo.class.getResource(template).newReader();
        if(!reader) {
            throw new RuntimeException("Woopsie could not find template from classpath: $template");
        }

        String rendered = new GStringTemplateEngine()
            .createTemplate(reader)?.make(args)?.toString();

        FileWriter fw = new FileWriter(target);
        fw.write(rendered);
        fw.close();
        reader.close();
    }

    /**
     * wizard() is the top-level function call that kicks everything off
     */
    def wizard() {

        String name = terminal.prompt("Project Name: ",
            GizmoProject.defaultProjectName());
        String path = terminal.prompt("Project Path: ",
            GizmoProject.defaultProjectPath());
        String description = terminal.prompt("Project Description: ",
            GizmoProject.defaultProjectDescription());
        String version = terminal.prompt("Project Version: ",
            GizmoProject.defaultProjectVersion());
        String packageName = terminal.prompt("Top Package: ",
            GizmoProject.defaultPackage());
        String buildTool = terminal.prompt("Gradle or Maven: ",
            GizmoProject.defaultBuildTool());

        this.context().setString("name", name)
            .setString("path", path)
            .setString("description", description)
            .setString("version", version)
            .setString("packageName", packageName)
            .setString("buildTool", buildTool);

        terminal.log(this.context().asString())

        GizmoProject.newProject(this).create();

    }

    def getGizmoHome() {
        return this.gizmoHome.getPath();
    }

    def getTemplateFolder() {
        return getGizmoHome() + "/templates/";
    }

    def context() {
        return this.context;
    }
}
