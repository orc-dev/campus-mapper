/**
 * @file   Color.java
 * @brief  This is a enum contains constant Strings representing
 *         ANSI escape code of various colors.
 *
 * @author Xin Cai
 */
 enum Color {
    /** Instance of the Color, associated with 256-color mode */
    RESET("\u001B[0m"),
    C020("\033[38;5;20m"),
    C037("\033[38;5;37m"),
    C043("\033[38;5;43m"),
    C054("\033[38;5;54m"),
    C056("\033[38;5;56m"),
    C111("\033[38;5;111m"),
    C160("\033[38;5;160m"),
    C222("\033[38;5;222m");
    
    /** ANSI escape seqence */
    public final String val;

    Color(String val) {
        this.val = val;
    }

    /**
     * Display the color board to the console.
     */
    protected static void displayColorBoard() {
        for (Color color : Color.values()) {
            if (color != Color.RESET)
                System.out.println(color.val + color.name() + Color.RESET.val);
        }
    }
}
