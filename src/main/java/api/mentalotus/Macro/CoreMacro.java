package api.mentalotus.Macro;


public class CoreMacro {
    public static void checkError(boolean condition, String errorMessage) {
        if (!condition) {
            throw new RuntimeException(errorMessage);
        }
    }

    public static boolean processException(boolean condition, String errorMessage)
    {
        try {
            checkError(condition, errorMessage);
        } catch(RuntimeException e)
        {
            System.out.println(e.getCause() + " " + e.getMessage());
            return true;
        }

        return false;
    }
}
