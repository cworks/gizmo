package github.cworks.gizmo.tasks

import cworks.json.JsonObject

class CodeHeaderTask extends GizmoTask {

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
        return "Header.java"
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

        // Read header file into a String
        // Traverse the sourcePath and each time we hit a java file,
        //     Concatenate the header with the java file
        //     Move the original java file to a temp file (just in case we brick)
        //     Rename the new java (that contains the header) to the original java filename
        //     close both files
        //     if(cleanup) then remove temp java file (i.e. the original file)

        String headerContent = headerFile.getText('UTF-8');
        def filterJavaFiles = ~/.*\.java$/
        def totalFileSize = 0;
        def fileCount = 0;
        def sumFileSize = {
            totalFileSize += it.size();
            fileCount++;
        }

        sourcePath.traverse(type: FILES, visit: sumFileSize, nameFilter: filterJavaFiles);

        println("Total file size for $fileCount Java source files is: $totalFileSize");
    }
}
