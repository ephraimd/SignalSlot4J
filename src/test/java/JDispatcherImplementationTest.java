import ephraim.JDispatcher;
import ephraim.StaticJDispatcher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class JDispatcherImplementationTest {
    @BeforeAll
    public static void beforeAll() {
        //the towns people will receive the news first even before the watch men...somehow
        //but the aim is to show that the order of slot connection determines the order of execution
        //so the TownsPeople's slots will be executed for when the TestUtility.NO_TAG_INPUT_SIGNAL signal is emitted
        new TownsPeople();
        new WatchTower();
    }

    @Test
    public void testDispatchObjectsConnectivity() {
        TestUtility.receiveDialogInputs();
    }

}

class WatchTower extends JDispatcher {
    //only the watch tower has the secret comms system to the king
    King king = new King();

    public WatchTower() {
        setup();
    }

    private void setup() {
        //connect a slot to some signals on the global dispatcher object
        //we're only connected to the TestUtility.NO_TAG_INPUT_SIGNAL, all other signals are ignored
        //so we should only have one signal emission connecting
        StaticJDispatcher.getDispatcher().addSlot(TestUtility.NO_TAG_INPUT_SIGNAL, (args) -> {
            String message = String.valueOf(args[0]).toLowerCase();
            if (message.matches(".*peace|.*calm|.*no\\sproblem")) {
                king.emit("calm()", message);
            } else if (message.matches(".*war|.*army|.*invasion|.*got\\strouble")) {
                king.emit("danger()", message);
            } else {
                System.out.println("The king should not be bothered!");
            }
        });
    }
}

/**
 * Well because word gets around easily, the towns people get news pretty quickly...i wonder how
 */
class TownsPeople extends JDispatcher {

    public TownsPeople() {
        StaticJDispatcher.getDispatcher().addSlot(TestUtility.NO_TAG_INPUT_SIGNAL, args -> messageLeaked(String.valueOf(args[0])));
    }

    public void messageLeaked(String message) {
        System.out.printf("We heard that: '%s'. We'll wait for the king's word\n", message);
    }
}

class King extends JDispatcher {

    public King() {
        //we can be more creative with our signal ID naming
        addSlot("danger()", args -> alarmRaised(String.valueOf(args[0])));
        addSlot("calm()", args -> cityCalmed(String.valueOf(args[0])));
    }

    public void alarmRaised(String message) {
        System.out.printf("The watch tower reported: '%s'. \n So, Release The Dragons\n", message);
    }

    public void cityCalmed(String message) {
        System.out.printf("The watch tower reported: '%s'. \n So, Lets have a feast!\n", message);
    }
}