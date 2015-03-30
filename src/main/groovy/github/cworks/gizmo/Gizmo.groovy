package github.cworks.gizmo

import cworks.json.JsonObject
import cworks.json.Json;
import github.cworks.gizmo.tasks.GizmoProject
import github.cworks.gizmo.tasks.GradlizeTask
import groovy.text.GStringTemplateEngine

class Gizmo {

    /**
     * Class utility to print the help menu
     */
    def static void printHelp(List<String> output) {
        output.add("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
        output.add("| g-i-z-m-o version 1.0.0                                     |");
        output.add("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
        output.add("| -help                 | this menu                           |");
        output.add("| -quit                 | bye-bye                             |");
        output.add("| -wizard               | new project wizard                  |");
        output.add("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
    }
    
    /**
     * Gizmo home directory
     */
    def File gizmoHome = null;

    /**
     * Holds variables and such needed by Gizmo tasks
     */
    def JsonObject context = null;
    
    /**
     * You got to start it up...with a gizmo home directory as one and only argument
     * @param args
     */
    public static void main(String[] args) {
        
        final Gizmo gizmo = new Gizmo();
        final Terminal terminal = new Terminal("(gizmo)", "Welcome, -help for menu", "Sinaria");
        terminal.start { line, output ->
            try {
                line = line.trim();
                if ("-quit".equalsIgnoreCase(line)) {
                    return true; // true means stop
                } else if("-help".equalsIgnoreCase(line)) {
                    printHelp(output);
                } else if("-wizard".equalsIgnoreCase(line)){
                    gizmo.wizard(terminal, output);
                } else if("-codeheader".equalsIgnoreCase(line)) {
                    //gizmo.loadTask("github.cworks.gizmo.tasks.CodeHeaderTask", terminal, output);
                }
            } catch(Exception ex) {
                output.add(ex.getMessage());
            }
        };
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
    def wizard(Terminal terminal, List<String> output) {

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
        if("gradle".equalsIgnoreCase(buildTool)) {
            String buildToolVersion = terminal.prompt("Gradle Version: ",
                GradlizeTask.DEFAULT_GRADLE_VERSION);
            context().setString("buildToolVersion", buildToolVersion);
            context().setString("gradleVersion", buildToolVersion);
        }

        context().setString("name", name)
            .setString("path", path)
            .setString("description", description)
            .setString("version", version)
            .setString("packageName", packageName)
            .setString("buildTool", buildTool);

        output.add(Json.asPrettyJson(context()));

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
