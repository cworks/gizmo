package cworks.gizmo

import github.cworks.gizmo.Terminal

def static void newGradleProject() {

    def Terminal terminal = new Terminal("(gizmo)", "New Gradle Project", "Sinaria boss");
    def projectRoot = terminal.prompt("Project Root: ");
    def description = terminal.prompt("Project Description: ", "Gizmo Generated Project on " + new Date());
    def version = terminal.prompt("Project Version: ", "1.0.0");
    def groupName = terminal.prompt("Group Name [typically a java package]: ", "cworks.app");
    def javaVersion = terminal.prompt("Java Version [7,8]: ", "8");

    Templates.render("gradle/build.gradle",
        new File(projectRoot, "/build.gradle"),
        [description: description]);

    Templates.render("gradle/gradle.properties",
        new File(projectRoot, "/gradle.properties"),
        [projectName: new File(projectRoot).getName(),
         javaVersion: javaVersion,
         version: version,
         groupName: groupName]);

    Templates.copy("gradle/settings.gradle", new File(projectRoot, "/settings.gradle"));
    Templates.copy("gradle/gradlew", new File(projectRoot, "/gradlew"));
    Templates.copy("gradle/gradlew.bat", new File(projectRoot, "/gradlew.bat"));

    new File(projectRoot, "gradle/wrapper").mkdirs();
    Templates.copy("gradle/wrapper/gradle-wrapper.properties",
        new File(projectRoot, "/gradle/wrapper/gradle-wrapper.properties"));
    Templates.copyBinary("gradle/wrapper/gradle-wrapper.jar",
        new File(projectRoot, "/gradle/wrapper/gradle-wrapper.jar"));

}

newGradleProject();
