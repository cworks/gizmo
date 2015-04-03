package github.cworks.gizmo

import cworks.json.Json
import cworks.json.JsonObject
import github.cworks.gizmo.tasks.CodeHeaderTask
import github.cworks.gizmo.tasks.GizmoProject
import github.cworks.gizmo.tasks.GradlizeTask
import groovy.text.GStringTemplateEngine

class Gizmo {

    def static printHelp = { ShellInput input, ShellOutput output ->
        output.println("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
        output.println("| g-i-z-m-o version 1.0.0                                     |");
        output.println("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
        output.println("| -help                 | this menu                           |");
        output.println("| -quit                 | bye-bye                             |");
        output.println("| -wizard               | new project wizard                  |");
        output.println("| -codeheader           | apply code header to sources        |");
        output.println("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
    };

    /**
     * Gizmo home directory
     */
    def File gizmoHome = null;

    /**
     * Holds variables and such needed by Gizmo tasks
     */
    def JsonObject context = null;

    /**
     * You got to start it up...with a context home directory as one and only argument
     * @param args
     */
    public static void main(String[] args) {

        final Gizmo gizmo = new Gizmo();

        final Shell shell = new Shell("(gizmo)", "Welcome, -help for menu", "Sinaria");
        shell.splash(printHelp)
            .quit("-quit", { ShellOutput output ->
                    output.println("-quit command closure called.");
                })
            .help("-help", printHelp)
            .command("-test", { ShellInput input, ShellOutput output ->
                    output.println("-test command closure called.")
                })
            .command("-wizard", { ShellInput input, ShellOutput output ->
                    gizmo.wizard(input, output);
                })
            .command("-codeheader", { ShellInput input, ShellOutput output ->
                    output.println("-codeheader command closure called.")
                })
            .errorHandler({ Exception ex ->
                    ex.printStackTrace();
                })
            .start();

//        shell.quit("-quit").start { line, output ->
//            try {
//                if ("-quit".equalsIgnoreCase(line)) {
//                    return true; // true means stop
//                } else if("-help".equalsIgnoreCase(line)) {
//                    printHelp(output);
//                } else if("-wizard".equalsIgnoreCase(line)){
//                    context.wizard(shell, output);
//                } else if("-codeheader".equalsIgnoreCase(line)) {
//                    context.codeHeader(shell, output);
//                }
//            } catch(Exception ex) {
//                output.add(ex.getMessage());
//            }
//        };
    }

    private Gizmo() {
        this.context = new JsonObject();
    }

    /**
     * Private creation, needs a context home directory
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
     * Run the new project wizard
     * @param terminal
     * @param output
     * @return
     */
    def wizard(ShellInput input, ShellOutput output) {

        String name = input.prompt("Project Name: ",
                GizmoProject.defaultProjectName());
        String path = input.prompt("Project Path: ",
                GizmoProject.defaultProjectPath());
        String description = input.prompt("Project Description: ",
                GizmoProject.defaultProjectDescription());
        String version = input.prompt("Project Version: ",
                GizmoProject.defaultProjectVersion());
        String packageName = input.prompt("Top Package: ",
                GizmoProject.defaultPackage());
        String buildTool = input.prompt("Gradle or Maven: ",
                GizmoProject.defaultBuildTool());
        if("gradle".equalsIgnoreCase(buildTool)) {
            String buildToolVersion = input.prompt("Gradle Version: ",
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

        output.println(Json.asPrettyJson(context()));

        GizmoProject.newProject(getContext()).create();
    }

    /**
     * Run and apply the code header to all source files specified
     * @param terminal
     * @param output
     */
    def codeHeader(Shell terminal, List<String> output) {

        String sourcePath = terminal.prompt("Project source path: ",
            "typically this is /some/path/project/src")
        context().setString("codeHeader.sourcePath", sourcePath);
        output.add("-quit");
        
        String headerTemplate = terminal.prompt("Source Code Header: ",
            CodeHeaderTask.defaultCodeHeader());
        
        context().setString("codeHeader.headerTemplate", headerTemplate);

        output.add(Json.asPrettyJson(context()));

        new CodeHeaderTask(getContext()).gizIt();
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
