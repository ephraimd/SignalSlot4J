import ephraim.StaticJDispatcher;

import javax.swing.*;


/**
 * Utility functions for our test suites
 */
public class TestUtility {
    public static final String CMD_INPUT_SIGNAL = "cmdInput";
    public static final String DIALOG_INPUT_SIGNAL = "dialogInput";
    public static final String NO_TAG_INPUT_SIGNAL = "noTagInput";

    public static void receiveDialogInputs() {
        String buf;

        while (true) {
            buf = JOptionPane.showInputDialog("Enter command with tag prefix. Eg: tag:command");
            if (buf.startsWith("cmd:")) {
                StaticJDispatcher.getDispatcher().emit(CMD_INPUT_SIGNAL, buf.replace("cmd:", ""));
            } else if (buf.startsWith("dialog:")) {
                StaticJDispatcher.getDispatcher().emit(DIALOG_INPUT_SIGNAL, buf.replace("dialog:", ""));
            } else if (buf.equalsIgnoreCase("quit")) {
                System.out.println("Quitting because you're boss!");
                break;
            } else {
                StaticJDispatcher.getDispatcher().emit(NO_TAG_INPUT_SIGNAL, buf);
            }
        }
    }
}
