/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Baked with love by comartin
 * Package: github.cworks.auto.tasks
 * User: comartin
 * Created: 11/6/2014 1:25 PM
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package github.cworks.gizmo.tasks

import github.cworks.gizmo.Gizmo;

/**
 * Contains all the details for creating a basic Java project
 * when generate is called.
 */
class JavaProjectTask extends GizmoTask {

    /**
     * Parent project path: /some/parent/path
     */
    private File parentPath;

    /**
     * Project name: a simple name
     */
    private String projectName;

    /**
     * Project path: /some/parent/path/projectName
     */
    private File projectPath;

    /**
     * Create a JavaProjectTask from a parentPath and projectName
     * @param parentPath
     * @param projectName
     */
    JavaProjectTask(final Gizmo gizmo) {
        super(gizmo);

        this.parentPath = new File(gizmo.context().getString("path"));
        this.projectName = gizmo.context().getString("name");
        this.projectPath = new File(this.parentPath, this.projectName);
    }

    String root() {
        return this.projectPath.getPath();
    }

    /**
     * When this is called all the magic happens
     */
    @Override
    void gizIt() {
        createDirectories()
        createPackage()
        createFiles()
    }

    /**
     * Create directories
     */
    void createDirectories() {
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
    void createPackage() {
        if(gizmo.context().getString("packageName") != null) {
            new File(this.projectPath, "src/main/java/"
                + gizmo.context().getString("packageName").replaceAll("\\.", "/")).mkdirs();
            new File(this.projectPath, "src/test/java/"
                + gizmo.context().getString("packageName").replaceAll("\\.", "/")).mkdirs();
        }
    }

    /**
     * Render core files that a basic Java project should happen
     */
    void createFiles() {

        Gizmo.render(gizmo.getTemplateFolder() + "README.md.giz",
            this.projectPath.getPath() + "/README.md",
            [
                projectName: gizmo.context().getString("name"),
            ]
        );

        Gizmo.copyFile(gizmo.getTemplateFolder() + ".gitignore.giz",
            this.projectPath.getPath() + "/.gitignore"
        );

        Gizmo.copyFile(gizmo.getTemplateFolder() + ".gitattributes.giz",
            this.projectPath.getPath() + "/.gitattributes"
        );
    }

}
 

