import ephraim.StaticJDispatcher;

import java.util.Scanner;


/**
 * Utility functions for our test suites
 */
public class TestUtility {
    public static final String CMD_INPUT_SIGNAL = "cmdInput";
    public static final String NO_TAG_INPUT_SIGNAL = "noTagInput";

    public static void receiveDialogInputs() {
        String buf;
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.println("Enter command with tag prefix. Eg: tag:command");
            buf = scan.nextLine();
            if (buf.startsWith("cmd:")) {
                StaticJDispatcher.getDispatcher().emit(CMD_INPUT_SIGNAL, buf.replace("cmd:", ""));
            } else if (buf.equalsIgnoreCase("quit")) {
                System.out.println("Quitting because you're boss!");
                break;
            } else {
                StaticJDispatcher.getDispatcher().emit(NO_TAG_INPUT_SIGNAL, buf);
            }
        }
        scan.close();
    }
}
