package github.cworks.gizmo

class Terminal {

    def String prompt;
    def String helloMessage;
    def String byeMessage;

    public Terminal(prompt, helloMessage, byeMessage) {
        this.prompt = prompt + " ";
        this.helloMessage = helloMessage;
        this.byeMessage = byeMessage;
    }

    def start(Closure interpreter) {

        def quit = false;
        def BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            Terminal.println(helloMessage);
            while(!quit) {
                Terminal.print(prompt);
                def line = reader.readLine().trim();
                quit = interpreter(line.replaceFirst(prompt, ""));
            }
            Terminal.println(byeMessage);

        } catch (IOException ex) {
            System.err.println(ex);
        }

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

        return System.in.newReader()
            .readLine()
                .replaceFirst(line, "")
                    .trim() ?: defaultValue;
    }

    def log(String message) {
        String line = "$prompt $message";
        print("$line ");
    }

    /**
     * print line and flush
     * @param output
     */
    def static println(String output){
        System.out.println(output);
        System.out.flush();
    }

    /**
     * print and flush
     * @param output
     */
    def static print(String output){
        System.out.print(output);
        System.out.flush();
    }

    /**
     * print list, print each element with {@link #println}
     * @param output
     */
    def static print(List<String> output) {
        output.forEach({line -> println(line)});
    }

}
