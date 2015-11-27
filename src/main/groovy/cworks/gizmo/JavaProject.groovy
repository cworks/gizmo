package cworks.gizmo

import github.cworks.gizmo.Terminal

/**
 * Create a plain jane java project
 */
def static void newProject() {

    def Terminal terminal = new Terminal("(gizmo)", "New Java Project", "Sinaria boss");

    def defaultProjectName = "newProject";
    def defaultProjectPath = System.getProperty("user.dir");
    def defaultPackage = "cworks.app";

    def _projectName = terminal.prompt("Project Name: ", defaultProjectName);
    def _path = new File(terminal.prompt("Project Path: ", defaultProjectPath));
    def _packageName = terminal.prompt("Top Package: ", defaultPackage);
    def _projectPath = new File(_path, _projectName);

    createDirectories(_projectPath);
    createPackage(_projectPath, _packageName);
    createFiles(_projectPath, _projectName, _packageName);
}

/**
 * Create core Java project directories
 * @param projectPath
 */
def static void createDirectories(File projectPath) {
    new File(projectPath, "src/main").mkdirs();
    new File(projectPath, "src/main/java").mkdirs();
    new File(projectPath, "src/main/resources").mkdirs();
    new File(projectPath, "src/test/java").mkdirs();
    new File(projectPath, "src/test/resources").mkdirs();
}

/**
 * Create user specified java package
 */
def static void createPackage(File projectPath, String packageName) {
    new File(projectPath, "src/main/java/" + packageName.replaceAll("\\.", "/")).mkdirs();
    new File(projectPath, "src/test/java/" + packageName.replaceAll("\\.", "/")).mkdirs();
}

/**
 * Render core java project files to a location specified
 * by user on gizmo command line.
 */
def static void createFiles(File projectPath, String projectName, String packageName) {
    def output = new File(projectPath,
        "src/main/java/" + packageName.replaceAll("\\.", "/") + "/App.java");
    Templates.render("src/main/java/App.java", output, [packageName:packageName]);

    output = new File(projectPath,
        "src/test/java/" + packageName.replaceAll("\\.", "/") + "/TestApp.java");
    Templates.render("src/test/java/TestApp.java", output, [packageName:packageName]);

    output = new File(projectPath, "README.md");
    Templates.render("README.md", output, [projectName:projectName]);

    Templates.copy("gitignore", new File(projectPath, ".gitignore"));
    Templates.copy("gitattributes", new File(projectPath, ".gitattributes"));
}

/**
 * Functional entry point to the JavaProject module
 */
newProject();
