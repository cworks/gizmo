package github.cworks.gizmo

class ShellInput extends BufferedReader {

    private String prompt;
    
    ShellInput(String prompt) {
        super(new BufferedReader(new InputStreamReader(System.in)));
        this.prompt = prompt;
    }

    public String readLine() {
        return super.readLine().trim().replaceFirst(prompt, "");
    }

    /**
     * Prompt for some information specified by message argument and return
     * users input as a String.
     * @param message
     * @param defaultValue
     * @return
     */
    def prompt(String message, String defaultValue = null) {
        def val = readLine(message, defaultValue);
        return val;
    }

    def readLine(String message, String defaultValue = null) {
        String line = "$prompt $message" + (defaultValue ? "[$defaultValue]" : "");
        print("$line ");

        String inputLine = System.in.newReader().readLine();
        if(inputLine.trim().startsWith(line)) {
            return inputLine.substring(line.length()).trim() ?: defaultValue;
        } else {
            return inputLine.trim() ?: defaultValue;
        }
    }

}
