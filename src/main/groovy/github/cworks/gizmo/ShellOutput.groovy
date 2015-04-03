package github.cworks.gizmo

class ShellOutput extends PrintWriter {

    ShellOutput() {
        super(System.out);
    }

    ShellOutput(OutputStream out) {
        super(out)
    }

    public void println(String x) {
        super.println(x);
        flush();
    }

    public void print(String s) {
        super.print(s);
        flush();
    }
}