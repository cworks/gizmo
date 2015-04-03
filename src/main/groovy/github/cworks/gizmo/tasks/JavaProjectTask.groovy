/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Baked with love by comartin
 * Package: github.cworks.auto.tasks
 * User: comartin
 * Created: 11/6/2014 1:25 PM
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package github.cworks.gizmo.tasks

import cworks.json.JsonObject
import github.cworks.gizmo.Gizmo;

/**
 * Contains all the details for creating a basic Java project
 * when generate is called.
 */
class JavaProjectTask extends GizmoTask {

    /**
     * Parent project path: /some/parent/path
     */
    def File parentPath;

    /**
     * Project name: a simple name
     */
    def String projectName;

    /**
     * Project path: /some/parent/path/projectName
     */
    def File projectPath;

    /**
     * Create a JavaProjectTask from a parentPath and projectName
     * @param parentPath
     * @param projectName
     */
    JavaProjectTask(final JsonObject context) {
        super(context);

        this.parentPath = new File(context.getString("path"));
        this.projectName = context.getString("name");
        this.projectPath = new File(this.parentPath, this.projectName);
    }

    def String root() {
        return this.projectPath.getPath();
    }

    /**
     * When this is called all the magic happens
     */
    @Override
    def void gizIt() {
        createDirectories()
        createPackage()
        createFiles()
    }

    /**
     * Create directories
     */
    def void createDirectories() {
        // create core directories
        new File(this.projectPath, "src/main").mkdirs();
        new File(this.projectPath, "src/main/java").mkdirs();
        new File(this.projectPath, "src/main/resources").mkdirs();
        new File(this.projectPath, "src/test/java").mkdirs();
        new File(this.projectPath, "src/test/resources").mkdirs();
    }

    /**
     * Create user specified java package
     */
    def void createPackage() {
        if(context.getString("packageName") != null) {
            new File(this.projectPath, "src/main/java/"
                + context.getString("packageName").replaceAll("\\.", "/")).mkdirs();
            new File(this.projectPath, "src/test/java/"
                + context.getString("packageName").replaceAll("\\.", "/")).mkdirs();
        }
    }

    /**
     * Render core java project files to a location specified
     * by user on context command line.
     */
    def void createFiles() {

        Gizmo.renderFromClasspath("/templates/src/main/java/App.java",
            projectSrc("App.java"),
            [packageName: context.getString("packageName")]
        );

        Gizmo.renderFromClasspath("/templates/src/test/java/TestApp.java",
            projectTest("TestApp.java"),
            [packageName: context.getString("packageName")]
        );

        Gizmo.renderFromClasspath("/templates/README.md",
            this.projectPath.getPath() + "/README.md",
            [projectName: context.getString("name"),]
        );

        Gizmo.copyFileFromClasspath("/templates/gitignore",
            this.projectPath.getPath() + "/.gitignore"
        );

        Gizmo.copyFileFromClasspath("/templates/gitattributes",
            this.projectPath.getPath() + "/.gitattributes"
        );
    }

    def String projectSrc(sourceFile) {
        return this.projectPath.getPath() +
            "/src/main/java/" +
                context.getString("packageName").replaceAll("\\.", "/") +
                    "/" + sourceFile;
    }

    def String projectTest(testFile) {
        return this.projectPath.getPath() +
            "/src/test/java/" +
                context.getString("packageName").replaceAll("\\.", "/") +
                    "/" + testFile;
    }

}
 

