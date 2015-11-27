package github.cworks.gizmo

class Shell {
    
    private static class Command {
        def String name;
        def Closure closure;
    }
    
    final def String prompt;
    final def String helloMessage;
    final def String byeMessage;
    final def Map<String, Command> commands;
    private Command helpCommand;
    private Command quitCommand;
    private Command splashCommand;
    private Command errorHandler;


    public Shell(prompt, helloMessage, byeMessage) {
        this.prompt = prompt + " ";
        this.helloMessage = helloMessage;
        this.byeMessage = byeMessage;
        this.commands = new HashMap<>();
    }
    
    def start() {
        def quit = false;
        def ShellInput  input  = new ShellInput(prompt);
        def ShellOutput output = new ShellOutput();
        try {
            output.println(helloMessage);
            if(splashCommand != null) {
                splashCommand.closure(input, output);
            }
            while(!quit) {
                output.print(prompt);
                def line = input.readLine();
                Command command = commands.get(line);
                if(command == quitCommand) {
                    quit = true;
                } else if(command != null) {
                    command.closure(input, output);
                }
            }
            if(quitCommand != null) {
                quitCommand.closure(output);
            }
            output.println(byeMessage);
        } catch (Exception ex) {
            if(errorHandler != null) {
                errorHandler.closure(ex);
            }
        } finally {
            input?.close();
            output?.close();
        }
        
    }

    def start(Closure interpreter) {
        def quit = false;
        def BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            //final List<String> output = new ArrayList<>();
            final ShellOutput output = new ShellOutput();
            output.println(helloMessage);
            if(this.splashCommand != null) {
                splashCommand(output);
            }
            //println(helloMessage);
            while(!quit) {
                //print(prompt);
                output.print(prompt);
                def line = reader.readLine().trim();
                quit = interpreter(line.replaceFirst(prompt, ""), output);
                print(output);
                output.clear();
            }
            println(byeMessage);

        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
    
    def splash(Closure closure) {
        splashCommand = new Command(name: "-splashCommand", closure: closure);
        addCommand(splashCommand);
        return this;
    }

    def errorHandler(Closure closure) {
        errorHandler = new Command(name: "-errorHandler", closure: closure);
        addCommand(errorHandler);
        return this;
    }
    
    def quit(String commandName, Closure closure) {
        quitCommand = new Command(name: commandName, closure: closure);
        addCommand(quitCommand);
        return this;
    }
    
    def help(String commandName, Closure closure) {
        helpCommand = new Command(name: commandName, closure: closure);
        addCommand(helpCommand);
        return this;
    }
    
    /**
     * Add another command to this Shell and call the given Closure when the command is entered.
     * @param command
     * @param closure
     * @return
     */
    def command(String command, Closure closure) {
        // TODO prob should do some validation
        commands.put(command, new Command(name: command, closure: closure));
        return this;
    }
    
    private def addCommand(Command command) {
        commands.put(command.name, command);
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

        //return System.in.newReader()
        //    .readLine()
        //        .replaceFirst(line, "")
        //            .trim() ?: defaultValue;
    }

    def log(String message) {
        String line = "$prompt $message";
        print("$line ");
    }

    def printLine(String message) {
        String line = "$prompt $message";
        println("$line ");
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
        output.forEach({
            line -> println(line)
        });
    }

}
