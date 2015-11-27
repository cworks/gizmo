package cworks.gizmo
import freemarker.template.Configuration
import freemarker.template.Template

/**
 * Copy a template to the targetFile
 * @param sourceFile
 * @param targetFile
 */
def static void copy(String sourceFile, File targetFile) {
    def src = new File("src/main/resources/templates/" + sourceFile);
    def dst = targetFile;
    dst << src.text;
}

/**
 * Copy a binary file to the targetFile
 * @param sourceFile
 * @param targetFile
 */
def static void copyBinary(String sourceFile, File targetFile) {
    def src = new File("src/main/resources/templates/" + sourceFile);
    def dst = targetFile;
    def input = src.newInputStream();
    def output = dst.newOutputStream();

    output << input;

    input.close();
    output.close();
}

/**
 * Render the template to an outputFile
 * @param templateName
 * @param outputFile
 * @param dataModel
 */
def static void render(String templateName, File outputFile) throws IOException {
    render(templateName, outputFile, [:]);
}

/**
 * Render the template to an outputFile
 * @param templateName
 * @param outputFile
 * @param dataModel
 */
def static void render(String templateName, File outputFile, Map dataModel) throws IOException {
    Configuration config = new Configuration();
    config.setClassForTemplateLoading(Templates.class, "/templates");
    Template template = config.getTemplate(templateName);

    Writer writer;
    try {
        writer = new BufferedWriter(new FileWriter(outputFile));
        template.process(dataModel, writer);
    } finally {
        try {
            writer.close();
        } catch(Exception ex) { }
    }
}
