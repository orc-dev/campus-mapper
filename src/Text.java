/**
 * @file   Text.java
 * @brief  A static class contains rendered messages and texts used by MapApp.
 *
 * @author Xin Cai
 */
class Text {
    /** Usage message */
    final static String USAGE = "\n" +
        "[ " + Color.C222.val + "MapApp Usage" + Color.RESET.val + " ]\n" +
        "| - Press 'M' to show the building list and campus map.\n" +
        "| - Press 'D' to highlight buildings with the 'Dining' service.\n" +
        "| - Press 'L' to highlight buildings with the 'Library' service.\n" +
        "| - Press 'P' to highlight buildings with the 'Parking' service.\n" +
        "| - Press two building ID's separated by space to find shartest path.\n" +
        "| - Press 'X' to exit.";
    
    /** Prompt message */
    final static String PROMPT =
        "\n[ " + Color.C222.val + "User Input" + Color.RESET.val + " ]\n" +
        "| - Please enter command here: ";

    /** Termination message */
    final static String EXIT = 
        "\n[ " + Color.C222.val + "Program Closed" + Color.RESET.val + " ]\n" +
        "| - MapApp has terminated. Thank you for using MapApp.\n";
    
    /** Warning message */
    final static String ERROR = 
        "\n[ " + Color.C160.val + "Error Message" + Color.RESET.val + " ]\n" +
        "| - ";

    /** Separator */
    final static String SEPARATOR = 
        "\n" + ". ".repeat(44) + 
        "\n" + ". ".repeat(44);

    /** Building List title */
    final static String BUILDING_LIST = 
        "[ " + Color.C222.val + "Building List" + Color.RESET.val + " ]";

    /** Campus map title */
    final static String CAMPUS_MAP =
        "[ " + Color.C222.val + "Campus Map" + Color.RESET.val + " ]";

    /** Shortest path title */
    final static String SHORTEST_PATH = 
            "\n[ " + Color.C222.val + "Shortest Path" + Color.RESET.val + " ]";

    /** Selected service title */
    public static String selectedService(String service) {
        return 
            "\n[ " + Color.C222.val + "Selected Service: " + Color.RESET.val +
            Color.C111.val + service + Color.RESET.val + " ]";
    }
}
