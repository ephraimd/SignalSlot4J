# SignalSlot4J
A pretty simple signal and slot events system.

The library can help connect different parts of an application with a loose mechanism that works like PyQt/C++ Qt's signal and slot system - with a little tweak to allow for more general purpose use.

Example?:
```java

import ephraim.StaticJDispatcher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.Arrays;


public class JDispatcherComponentTest {
    private int lastTestState;

    @BeforeAll
    public static void beforeAll() {
        //making the class instance as distant as possible
        //imagine this instance is created somewhere far away from this Test Suite
        new InputProcessor();
    }

    @Test
    @Order(1)
    public void testSignalDisconnect() {
        //make sure non-existent signal IDs are caught and reported
        Throwable exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> StaticJDispatcher.getDispatcher().disconnect("dialogInputs"));
        Assertions.assertEquals("No signal ID dialogInputs exists", exception.getMessage());
        //make sure signal disconnection works well
        Assertions.assertTrue(StaticJDispatcher.getDispatcher().isSignalRegistered("noInput"));
        StaticJDispatcher.getDispatcher().disconnect("noInput");
        Assertions.assertFalse(StaticJDispatcher.getDispatcher().isSignalRegistered("noInput"));
    }

    @Test
    @Order(2)
    public void signalConnectivityTest() {
        //this unit tests the order of the execution of slots and also ensures emit/slot connection is working
        StaticJDispatcher.getDispatcher().addSlot(TestUtility.NO_TAG_INPUT_SIGNAL, (args) -> {
            lastTestState = InputProcessor.testState++;
            System.out.println("lastTestState = " + lastTestState);
        });
        StaticJDispatcher.getDispatcher().addSlot(TestUtility.NO_TAG_INPUT_SIGNAL, (args) -> {
            System.out.println("InputProcessor.testState = " + InputProcessor.testState);
            Assertions.assertEquals(lastTestState, InputProcessor.testState - 1);
        });
    }

    public void signalAvailabilityTest() {
        Assertions.assertTrue(StaticJDispatcher.getDispatcher().isSignalRegistered(TestUtility.CMD_INPUT_SIGNAL));
        Assertions.assertTrue(StaticJDispatcher.getDispatcher().isSignalRegistered(TestUtility.DIALOG_INPUT_SIGNAL));
        Assertions.assertTrue(StaticJDispatcher.getDispatcher().isSignalRegistered(TestUtility.NO_TAG_INPUT_SIGNAL));
    }

    @Test
    @Order(3)
    public void staticDispatcherUsageTest() {
        signalAvailabilityTest();
        //Please check the test folder under the TestUtility class for receiveDialogInputs implementation 
        TestUtility.receiveDialogInputs();
    }

}

class InputProcessor {
    public static int testState;

    public InputProcessor() {
        StaticJDispatcher.getDispatcher().addSlot(TestUtility.CMD_INPUT_SIGNAL, this::processCmdInput);
        StaticJDispatcher.getDispatcher().addSlot("noInput", System.out::println);
        StaticJDispatcher.getDispatcher().addSlot(TestUtility.DIALOG_INPUT_SIGNAL, this::processDialogInput);
        StaticJDispatcher.getDispatcher().addSlot(TestUtility.NO_TAG_INPUT_SIGNAL, (args) -> processNoTagInput(String.valueOf(args[0])));
    }

    public static void processNoTagInput(String errorMsg) {
        System.out.println("Your inputs without tags are: " + errorMsg);
    }

    public void processCmdInput(Object... args) {
        System.out.println("Cmd Inputs received are: " + Arrays.toString(args));
    }

    public void processDialogInput(Object... args) {
        System.out.println("Dialog Inputs received are: " + Arrays.toString(args));
    }
}
```