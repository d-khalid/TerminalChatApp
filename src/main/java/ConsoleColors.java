import java.util.HashMap;
import java.util.Map;

public class ConsoleColors {
    // ANSI Escape Codes for Colored Printing to the Console
    public static final String RESET = "\033[0m";  // Text Reset

    private static final Map<String, String> COLORS = new HashMap<>();

    static {
        // Initialize all color codes
        COLORS.put("RESET", "\033[0m");
        COLORS.put("BLACK", "\033[0;30m");
        COLORS.put("RED", "\033[0;31m");
        COLORS.put("GREEN", "\033[0;32m");
        COLORS.put("YELLOW", "\033[0;33m");
        COLORS.put("BLUE", "\033[0;34m");
        COLORS.put("PURPLE", "\033[0;35m");
        COLORS.put("CYAN", "\033[0;36m");
        COLORS.put("WHITE", "\033[0;37m");
    }

    public static String getColor(String colorName) {
        return COLORS.getOrDefault(colorName.toUpperCase(), COLORS.get("RESET"));
    }

    public static String apply(String text, String colorCode)
    {
        return colorCode + text + COLORS.get("RESET");
    }
}
