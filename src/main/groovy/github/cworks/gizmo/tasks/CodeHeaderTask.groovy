package github.cworks.gizmo.tasks

import cworks.json.JsonObject
import github.cworks.gizmo.Gizmo
import github.cworks.gizmo.ShellInput
import github.cworks.gizmo.ShellOutput

import java.nio.file.Paths
import java.util.regex.Matcher
import java.util.regex.Pattern

class CodeHeaderTask extends GizmoTask {

    /**
     * None option
     */
    def final static String NONE = "NONE";

    /**
     * pattern string for matching basic java expressions (i.e. ${something}) 
     */
    def final static String TEMPLATE_VARIABLE_PATTERN = "\\\$\\{(.*?)\\}";

    /**
     * Pattern used to parse java expressions 
     */
    def final static Pattern VARIABLE_PATTERN = Pattern.compile(TEMPLATE_VARIABLE_PATTERN);
    
    /**
     * Project source folder, typically: /some/path/project/src
     */
    def File sourceFolder;

    /**
     * The code header template file we're going to apply to each file under sourceFolder
     */
    def File headerFile;

    CodeHeaderTask(final JsonObject context, final ShellInput input, final ShellOutput output) {
        super(context, input, output);
    }

    @Override
    void gizIt() {

        context.setString("sourceFolder",
            input.prompt("sourceFolder: ", defaultSourceFolder()));

        context.setString("headerFile",
            input.prompt("headerFile: ", defaultCodeHeader()));

        this.headerFile = new File(context.getString("headerFile"))
        this.sourceFolder = new File(context.getString("sourceFolder"));

        try {
            checkSourcePath();
        } catch(Exception ex) {
            output.println("Whoopsie! " + ex.getMessage());
            return;
        }
        
        try {
            applyCodeHeader();
        } catch(Exception ex) {
            output.println("Whoopsie! " + ex.getMessage());
        }
    }

    /**
     * Return the pre-baked source code header template
     * @return
     */
    static String defaultCodeHeader() {
        return "Header.txt"
    }

    /**
     * Return the pre-backed default source folder
     * @return
     */
    static String defaultSourceFolder() {
        return "src"
    }

    /**
     * Return a default Project name 
     * @return
     */
    static String defaultProjectName() {
        return NONE;
    }

    /**
     * Return a best guess on the default Project name by using the 
     * given sourceFolder.  Will typically return the folder that contains
     * sourceFolder as the projectName
     *
     * @param sourcePath
     * @return
     */
    static String defaultProjectName(String sourcePath) {
        return Paths.get(sourcePath).getParent().getName(0);
    }

    /**
     * Return a default Organization name
     * @return
     */
    static String defaultProjectOrganization() {
        return NONE;
    }

    /**
     * Return a default License 
     * @return
     */
    static String defaultLicense() {
        return NONE;
    }

    /**
     * Return a default Tag Line
     * @return
     */
    static String defaultTagLine() {
        return NONE;
    }

    /**
     * Return a default code header Body
     * @return
     */
    static String defaultBody() {
        return NONE;
    }

    /**
     * Return default tags
     * @return
     */
    static String defaultTags() {
        return NONE;
    }

    /**
     * Return true if sourceText already contains a header 
     * @param sourceText
     * @return
     */
    static boolean hasHeader(String sourceText) {
        String text = sourceText.trim();
        if(text.startsWith("/**")) {
            for(def i = 0; i < text.length(); i++) {
                if(i + 1 < text.length()) {
                    if (text.charAt(i) == '*' && text.charAt(i + 1) == '/') {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Return sourceText without a code header
     * @param sourceText
     * @return
     */
    static String removeHeader(String sourceText) {
        String text = sourceText.trim();
        if(text.startsWith("/**")) {
            for(def i = 0; i < text.length(); i++) {
                if(i + 1 < text.length()) {
                    if (text.charAt(i) == '*' && text.charAt(i + 1) == '/') {
                        return text.substring(i + 2);
                    }
                }
            }
        }
        return text;
    }

    static void parseVariable(String expression, List<String> variables) {
        Matcher matcher = VARIABLE_PATTERN.matcher(expression);
        while (matcher.find()) {
            String key = matcher.group(1);
            variables.add(key);
        }
    }

    /**
     * Check source path that we're going to dive into and apply the given code header to
     * source files.
     */
    def void checkSourcePath() {

        if(!sourceFolder.exists()) {
            throw new RuntimeException("sourceFolder [" + sourceFolder.getPath() + "] does not exist.");
        }

        // try to write into a temp file inside the folder,
        // this will tell us if we have write permissions
        try {
            new File(sourceFolder, 'header.log').withWriterAppend { writer ->
                writer << "Starting CodeHeaderTask: " + new Date().toString() + "\n";
            }
            new File(sourceFolder, 'header.log').delete();
        } catch (Exception ex) {
            throw new RuntimeException("cannot write to: " + sourceFolder.getPath(), ex);
        }

    }

    /**
     * The heart and soul of this task, which applies the given header to each
     * source file under sourceFolder.
     */
    def void applyCodeHeader() {
        String headerTemplate  = "";
        List<String> variables = [];
        if(defaultCodeHeader().equals(headerFile.getPath())) {
            Reader reader = Gizmo.readerFromClasspath("/templates/codeheader/Header.txt");
            reader.eachLine { line ->
                parseVariable(line, variables);
                headerTemplate += line + System.getProperty("line.separator");
            };
        } else {
            headerFile.eachLine { line ->
                parseVariable(line, variables);
                headerTemplate += line + System.getProperty("line.separator");
            }
        }
        
        variables.eachWithIndex { variable, i ->
            String defaultValue = NONE;
            if("user".equalsIgnoreCase(variable)) {
                defaultValue = System.getProperty("user.name");
            } else if("projectName".equalsIgnoreCase(variable)) {
                defaultValue = defaultProjectName(sourceFolder.getPath());
            } else if("dateTime".equalsIgnoreCase(variable)) {
                defaultValue = new Date().format("MM-dd-yyyy HH:mm:ss");
            } else if("packageName".equalsIgnoreCase(variable)) {
                defaultValue = "auto-complete";
            }
            
            context.setString(variable,
                input.prompt("\t" + (i+1) + ") " + variable + ": ", defaultValue));
        };

        sourceFolder.eachDirRecurse() { dir ->
            dir.eachFileMatch(~/.*\.java$/) { file ->
                if(context.getString("packageName") != null) {
                    context.setString("packageName", Gizmo.toPackageName(file));
                }
                String renderedHeader = Gizmo.render(headerTemplate, context.toMap());
                String content = file.getText("UTF-8");
                if(hasHeader(content)) {
                    content = removeHeader(content);
                }
                
                file.write(renderedHeader + content.trim(), "UTF-8");
            }
        }

    }
    
}
