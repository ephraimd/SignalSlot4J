# SignalSlot4J
A pretty simple signal and slot events system.

The library can help connect different parts of an application with a loose mechanism that works like PyQt/C++ Qt's signal and slot system - with a little tweak to allow for more general purpose use.

Example?
```java
import java.util.Arrays;
import java.util.Scanner;

public class MainTest {
    public static void main(String[] args){
        test2();

        Scanner scan = new Scanner(System.in);
        while(true){
            System.out.println("Enter Message: ");
            String input = scan.nextLine().trim();
            if(input.equals("quit")){
                break;
            }else{
                JSSDispatcher.getDispatcher().emit("gotInput()", input);
            }
        }

    }

    private static void test2(){
        new Caller();
        WatchMan man = new WatchMan();
        King king = new King();

        JSSDispatcher.getDispatcher().addSlot("gotInput()", args-> {
            String arg = args[0].toString();
            if(arg.startsWith("danger:")){
                man.emit("danger()", arg.replace("danger:", ""));
                king.emit("danger()", arg.replace("danger:", ""));
            }else if(arg.startsWith("calm:")){
                man.emit("calm()", arg.replace("calm:", ""));
                king.emit("calm()", arg.replace("calm:", ""));
            }
        });
    }
}

class Caller{
    public Caller(){
        JSSDispatcher.getDispatcher().addSlot("gotInput()", args-> System.out.println("Global Message is: " + Arrays.toString(args)));
    }
}

class WatchMan extends Dispatcher{
    public WatchMan(){
        addSlot("danger()", args -> raiseAlarm(String.valueOf(args[0])));
        addSlot("calm()", args -> calmCity(String.valueOf(args[0])));
    }
    public void raiseAlarm(String message){
        System.out.println("WatchMAN Alarm: "+message);
    }
    public void calmCity(String message){
        System.out.println("WatchMAN says 'be calm' and "+message);
    }
}

class King extends Dispatcher{
    public King(){
        addSlot("danger()", args -> alarmRaised(String.valueOf(args[0])));
        addSlot("calm()", args -> cityCalmed(String.valueOf(args[0])));
    }
    public void alarmRaised(String message){
        System.out.println("Release The Dragons");
    }
    public void cityCalmed(String message){
        System.out.println("Lets have a feast");
    }
}
```