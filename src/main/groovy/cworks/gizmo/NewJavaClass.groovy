package cworks.gizmo

import github.cworks.gizmo.Terminal

/**
 * Creates a new Java class by default in src/main/java if it exists, otherwise
 * it prompts to create src/main/java, if user selects 'Y' then new class is
 * created in src/main/java, if 'N' then new class is created from current directory.
 */
def static void newJavaClass() {

    def Terminal terminal = new Terminal("(gizmo)", "New Java Class", "Sinaria boss");
    def fullyQualifiedClass = terminal.prompt("Class Name: ");
    def classPath = fullyQualifiedClass.replaceAll("\\.", "/");

    def srcDir = getSrcDir();
    def javaFile = new File(srcDir, classPath + ".java");
    new File(javaFile.getParent()).mkdirs();
    javaFile.createNewFile();

    def className = getClassName(fullyQualifiedClass);
    def packageName = getPackageName(fullyQualifiedClass);


    Templates.render("src/main/java/NewClass.java", javaFile,
        [packageName: packageName,
         className: className]);
}

/**
 * Returns String to src dir if one exists otherwise empty String
 * @return
 */
def static String getSrcDir() {
    if(new File("src/main/java").exists()) {
        return "src/main/java";
    } else if(new File("src/java").exists()) {
        return "src/java";
    } else if(new File("src").exists()) {
        return "src";
    }

    return System.getProperty("user.dir");
}

/**
 * Returns just the String of the simple Java
 * @param javaFile
 * @return
 */
def static String getClassNameFromFile(File javaFile) {
    javaFile.name.lastIndexOf('.').with {
        it != -1 ? javaFile.name[0..<it] : javaFile.name
    }
}

/**
 * Returns just the String after last '.'
 * @param fullyQualifiedClass
 * @return
 */
def static String getClassName(String fullyQualifiedClass) {
    def extensionPos = fullyQualifiedClass.lastIndexOf(".");
    return fullyQualifiedClass.substring(extensionPos + 1);
}

/**
 * Returns the package name from a fully qualified class name
 * @param fullyQualifiedClass
 * @return
 */
def static String getPackageName(String fullyQualifiedClass) {
    def extensionPos = fullyQualifiedClass.lastIndexOf(".");
    return fullyQualifiedClass.substring(0, extensionPos);
}



newJavaClass();
