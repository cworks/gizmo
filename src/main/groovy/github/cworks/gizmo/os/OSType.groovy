package github.cworks.gizmo.os

enum OSType {
    WINDOWS(1),
    UNIX(2),
    MAC_OSX(3);

    private static OSType current;
    static {
        current = osType(System.getProperty("os.name"));
    }

    public static OSType osType() {
        return current;
    }

    public static boolean isMac() {
        return current == MAC_OSX;
    }

    public static boolean isWin() {
        return current == WINDOWS;
    }

    public static boolean isUnix() {
        return current == UNIX;
    }

    /**
     * number value bound to this OSType enum 
     */
    private int value;

    /**
     * Create an OSType enum 
     * @param value
     */
    private OSType(int value) {
        this.value = value;
    }

    /**
     * Return the correct OSType enum from an osName string, typically
     * obtained from os.name system property 
     * @param osName
     * @return
     */
    public static OSType osType(String osName) {
        if(osName.toLowerCase().startsWith("win")) {
            return WINDOWS;
        } else if(osName.toLowerCase().indexOf("os x") != -1) {
            return MAC_OSX;
        } else {
            return UNIX;
        }
    }
    
}

