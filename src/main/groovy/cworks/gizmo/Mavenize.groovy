package cworks.gizmo

import github.cworks.gizmo.Terminal

def static void newMavenProject() {

    def Terminal terminal = new Terminal("(gizmo)", "New Maven Project", "Sinaria boss");
    def projectRoot = terminal.prompt("Project Root: ");
    def description = terminal.prompt("Project Description: ", "Gizmo Generated Project on " + new Date());
    def version = terminal.prompt("Project Version: ", "1.0.0");
    def groupId = terminal.prompt("Group Id [typically a java package]: ", "cworks.app");
    def javaVersion = terminal.prompt("Java Version [7,8]: ", "8");

    Templates.render("maven/pom.xml",
            new File(projectRoot, "/pom.xml"),
            [projectName: new File(projectRoot).getName(),
             javaVersion: javaVersion,
             version: version,
             groupId: groupId,
             description: description]);

}

newMavenProject();
