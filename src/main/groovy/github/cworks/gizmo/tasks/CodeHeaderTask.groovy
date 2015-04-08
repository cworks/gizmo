package github.cworks.gizmo.tasks
import cworks.json.JsonObject
import github.cworks.gizmo.Gizmo

class CodeHeaderTask extends GizmoTask {

    def final static String NONE = "NONE";
    
    /**
     * Project path, typically: /some/path/project/src
     */
    def File sourcePath;

    /**
     * The code header template file we're going to apply to each file under sourcePath
     */
    def File headerFile;

    CodeHeaderTask(final JsonObject context) {
        super(context);
        this.headerFile = new File(context.getString("codeHeader.headerFile"))
        this.sourcePath = new File(context.getString("codeHeader.sourcePath"));
    }

    @Override
    void gizIt() {
        checkCodeHeader();
        checkSourcePath();
        checkScratchSpace();
        applyCodeHeader();
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

    /**
     * Check that we can read the custom header file or user is willing to accept default
     */
    def void checkCodeHeader() {

    }

    /**
     * Check source path that we're going to dive into and apply the given code header to
     * source files.
     */
    def void checkSourcePath() {

        if(!sourcePath.exists()) {
            throw new RuntimeException("CodeHeaderTask requires a valid sourcePath!");
        }

        // try to write into a temp file inside the folder,
        // this will tell us if we have write permissions
        try {
            new File(sourcePath, 'header.log').withWriterAppend { writer ->
                writer << "Starting CodeHeaderTask: " + new Date().toString() + "\n";
            }
            new File(sourcePath, 'header.log').delete();
        } catch (Exception ex) {
            throw new RuntimeException("CodeHeaderTask create header.log failed.", ex);
        }

    }

    /**
     * Check the available disk space required to help us gauge if this operation
     * has what it needs to do its thing, which is applying the header to each file.
     */
    def void checkScratchSpace() {

    }

    /**
     * The heart and soul of this task, which applies the given header to each
     * source file under sourcePath.
     */
    def void applyCodeHeader() {
        
        String headerTemplate = "";

        if(defaultCodeHeader().equals(headerFile.getPath())) {
            headerTemplate = Gizmo.readFileFromClasspath("/templates/codeheader/Header.txt");
        } else {
            headerTemplate = headerFile.getText('UTF-8');
        }

        def args = [user: context.getString("codeHeader.user")];
        
        if(!defaultProjectName().equals(context.getString("codeHeader.projectName"))) {
            args.put("projectName", context.getString("codeHeader.projectName"));
        }
        
        if(!defaultTags().equals(context.getString("codeHeader.tags"))) {
            args.put("tags", context.getString("codeHeader.tags"));
        }
        
        if(!defaultProjectOrganization().equals(context.getString("codeHeader.organization"))) {
            args.put("organization", context.getString("codeHeader.organization"));
        }
        
        if(!defaultLicense().equals(context.getString("codeHeader.license"))) {
            args.put("license", context.getString("codeHeader.license"));
        }
        
        if(!defaultBody().equals(context.getString("codeHeader.body"))) {
            args.put("body", context.getString("codeHeader.body"));
        }
        
        if(defaultTagLine().equals(context.getString("codeHeader.tagLine"))) {
            args.put("tagLine", context.getString("codeHeader.tagLine"));
        }
        
        sourcePath.eachDirRecurse() { dir ->
            dir.eachFileMatch(~/.*\.java$/) { file ->
                args.put("dateTime", new Date().format("MM-dd-yyyy HH:mm:ss"));
                args.put("packageName", Gizmo.toPackageName(file));
                String renderedHeader = Gizmo.render(headerTemplate, args);
                String content = file.getText("UTF-8");
                if(hasHeader(content)) {
                    content = removeHeader(content);
                }
                
                file.write(renderedHeader + content.trim(), "UTF-8");
            }
        }

    }
    
}
