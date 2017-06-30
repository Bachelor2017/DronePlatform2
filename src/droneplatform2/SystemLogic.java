package droneplatform2;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * this class does all the logic and calculations
 *
 */
//DENNE KLASSEN MÅ RYDDEST I. SAMME SOM MED FAULTLOGIC. SLÅ SAMMEN CASE
public class SystemLogic implements Runnable {

    ///FRA eventStates
    private ArrayList<Event> differentEventStates;
    private ArrayList<Event> events;
    private ArrayList<String> eventList;

    int test = 1;

    boolean calibrated = false;
    int oldState = 100;
    int newState = 0;
    //time
    int totalTime = 10;
    int totalTimeUsed = 0;
    int totalTimeLeft = 10;
    int cycleTimeUsed = 0;
    int cyclustimeLeft = 0;
    int cycleTime = 0;
    int totalPercentageCompleted = 0;
    int cyclusPercentageCompleted = 0;
    boolean canRun = false;

    TimerTask tTask;
    java.util.Timer timer;
    boolean runState = true;
    int oldReadData = 100;

    private Thread t;
    private DataHandler dataHandler;
    private byte[] dataFromTeensy;
    private byte[] datatoTeensy;
    private int caseScenario = 100;
    private Semaphore semaphore;
    private boolean platformMode = false;

    boolean value = false;

    public SystemLogic(DataHandler dh, Semaphore semaphore) {
        this.dataHandler = dh;
        this.semaphore = semaphore;

        caseScenario = 500;

        //FRA EVENTSTATES
        eventList = new ArrayList<>();
        events = new ArrayList<>();
        differentEventStates = new ArrayList<>();
        addingEventStates();
        Event event12 = new Event(8, "System powered on");
        events.add(event12);
        runTimer();
        ///////
    }

    /**
     * start a new thread containing the logic
     */
    public void start() {
        t = new Thread(this, "controller thread");
        t.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                semaphore.acquire();
                platformMode = dataHandler.getPlatformMode();
                dataFromTeensy = dataHandler.getDataFromMega();
                datatoTeensy = dataHandler.getDataToMega();
                semaphore.release();

                if (!platformMode) {

                    int readData = dataFromTeensy[8];
                    if (readData != oldReadData) {
                       // System.out.println("ReadData Manual Mode: " + readData);
                    }
                    oldReadData = readData;
                    if ((readData == 0)) {
                        newState = 0;
                        canRun = false;
                    }
                    //  if((readData==1)||(readData==2))
                    if ((readData == 1)) {
                        newState = 1;
                        canRun = true;
                    } else if ((readData == 2)) {
                        newState = 2;
                        canRun = true;
                    } //else if ((readData == 3) || (readData == 4))
                    else if ((readData == 4) || (readData == 5)){
                        newState = 3;
                        canRun = true;
                    } else if ((readData == 6)) {
                        newState = 4;
                        canRun = false;
                    }

                    switchCalibrationCases(newState);
                } else if (platformMode) {
                    int readData = dataFromTeensy[6];
                    if (readData != oldReadData) {
                     //   System.out.println("ReadData Auto mode: " + readData);
                    }
                    oldReadData = readData;
                    /*    if((readData==0))
                    {
                        newState = 0; 
                        canRun=true;
                    }*/
                    if ((readData >= 0) && (readData < 7)) {
                        newState = 1;
                        canRun = true;
                    } else if ((readData >= 7) && (readData < 11)) {
                        newState = 2;
                        canRun = true;
                    } else if ((readData == 11)) {
                        newState = 3;
                        canRun = false;
                    } else if ((readData >= 12) && (readData < 17)) {
                        newState = 4;
                        canRun = true;
                    } else if ((readData >= 17) && (readData <= 18)) {
                        newState = 5;
                        canRun = true;
                    } else if ((readData >= 19) && (readData < 23)) {
                        newState = 6;
                        canRun = true;
                    } else if ((readData >= 23) && (readData < 30)) {
                        newState = 7;
                        canRun = true;
                    } else if ((readData >= 30) && (readData < 37)) {
                        newState = 8;
                        canRun = true;
                    } else if ((readData >= 37)) {
                        newState = 9;
                        canRun = true;
                    }
                    switchCases(newState);
                }

                caseScenario = newState;

            } catch (InterruptedException ex) {
                Logger.getLogger(SystemLogic.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    /**
     * The switch cases. each case representing a event.
     *
     * @param caseNumber
     * @throws InterruptedException
     */
    protected void switchCases(int caseNumber) throws InterruptedException {
        int number = caseNumber;
        switch (number) {
            case (0):

                if (oldState != newState) {
                    // if (runState) {
                    totalTime = 150;
                    totalTimeLeft = 150;
                    cyclustimeLeft = 0;
                    totalTimeUsed = 0;
                    cycleTimeUsed = 0;
                    totalTimeLeft = 150;
                    caseLogic(0, totalTimeLeft, 4);

                    //caseLogic(1, totalTimeLeft, 1);
                    oldState = newState;//setting the event time to 13, and time left to total time
                }

                break;
            case (1):      //Calibrating slider
                //(runState) {
                if (oldState != newState) {
                    // if (runState) {
                    totalTime = 150;
                    totalTimeLeft = 150;
                    cyclustimeLeft = 0;
                    totalTimeUsed = 0;
                    cycleTimeUsed = 0;

                    caseLogic(20, totalTimeLeft, 4);

                    oldState = newState;//setting the event time to 13, and time left to total time
                }
                break;
            case (2):       //Calibrating Lift
                if (oldState != newState) {
                    // totalTime = 40;
                    totalTimeLeft = 130;
                    caseLogic(10, totalTimeLeft, 5);

                    oldState = newState;//setting the event time to 13, and time used to 0
                }
                break;
            case (3):       //Calibrating Lift
                if (oldState != newState) {
                    // totalTime = 40;
                    totalTimeLeft = 120;
                    caseLogic(0, totalTimeLeft, 6);

                    oldState = newState;//setting the event time to 13, and time used to 0
                }
                break;
            case (4):       //Calibrating Lift
                if (oldState != newState) {
                    // totalTime = 40;
                    totalTimeLeft = 120;
                    caseLogic(31, totalTimeLeft, 7);

                    oldState = newState;//setting the event time to 13, and time used to 0
                }
                break;
            case (5):       //Calibrating Lift
                if (oldState != newState) {
                    // totalTime = 40;
                    totalTimeLeft = 88;
                    caseLogic(12, totalTimeLeft, 8);

                    oldState = newState;//setting the event time to 13, and time used to 0
                }
                break;
            case (6):       //Calibrating Lift
                if (oldState != newState) {
                    // totalTime = 40;
                    totalTimeLeft = 76;
                    caseLogic(10, totalTimeLeft, 9);

                    oldState = newState;//setting the event time to 13, and time used to 0
                }
                break;
            case (7):       //Calibrating Lift
                if (oldState != newState) {
                    // totalTime = 40;
                    totalTimeLeft = 63;
                    caseLogic(27, totalTimeLeft, 10);

                    oldState = newState;//setting the event time to 13, and time used to 0
                }
                break;
            case (8):       //Calibrating Lift
                if (oldState != newState) {
                    // totalTime = 40;
                    totalTimeLeft = 34;
                    caseLogic(26, totalTimeLeft, 11);

                    oldState = newState;//setting the event time to 13, and time used to 0
                }
                break;

            case (9):       //Calibrating Lift
                if (oldState != newState) {
                    // totalTime = 40;
                    totalTimeLeft = 13;
                    caseLogic(13, totalTimeLeft, 12);

                    oldState = newState;//setting the event time to 13, and time used to 0
                }

                break;
            case (10):        //Calibrating arm
                if (oldState != newState) {

                    caseLogic(0, 0, 13);

                    oldState = newState;//setting the event time to 13, and time used to 0
                }
                break;
        }
    }

    /**
     * The switch cases. each case representing a event.
     *
     * @param caseNumber
     * @throws InterruptedException
     */
    protected void switchCalibrationCases(int caseNumber) throws InterruptedException {
        int number = caseNumber;
        switch (number) {
            case (0):

                if (oldState != newState) {
                    // if (runState) {
                    totalTime = 37;
                    totalTimeLeft = 37;
                    cyclustimeLeft = 0;
                    totalTimeUsed = 0;
                    cycleTimeUsed = 0;

                    //caseLogic(1, totalTimeLeft, 1);
                    oldState = newState;//setting the event time to 13, and time left to total time
                }

                break;
            case (1):      //Calibrating slider
                //(runState) {

                if (oldState != newState) {

                    totalTimeLeft = 37;
                    caseLogic(2, totalTimeLeft, 0);

                    oldState = newState;//setting the event time to 13, and time left to total time
                }
            case (2):      //Calibrating slider
                //(runState) {
                if (oldState != newState) {
                    totalTimeUsed = 2;
                    totalTimeLeft = 37;
                    caseLogic(25, totalTimeLeft, 1);

                    oldState = newState;//setting the event time to 13, and time left to total time
                }
                break;
            case (3):       //Calibrating Lift
                if (oldState != newState) {
                    // totalTime = 40;
                    totalTimeUsed = 25;
                    totalTimeLeft = 12;
                    caseLogic(12, totalTimeLeft, 2);

                    oldState = newState;//setting the event time to 13, and time used to 0
                }
                break;
            case (4):        //Calibrating arm
                if (oldState != newState) {

                    caseLogic(0, 0, 3);

                    oldState = newState;//setting the event time to 13, and time used to 0
                }
                break;

        }
    }

    /**
     * case: setting the total time left to the total time value. Setting the
     * cycletime used to 0 to state that a new cycle is starting
     *
     * @param cTime setting the cycle time of the cycle
     * @param totTimeLeft setting the total time left in case the earlier
     * prosess is finished before planed. This also updates the progreessbar in
     * GUI
     * @throws InterruptedException
     */
    public void caseLogic(int cTime, int totTimeLeft, int x) throws InterruptedException {
        cycleTimeUsed = 1;
        totalTimeLeft = totTimeLeft;
        cycleTime = cTime;
        cyclustimeLeft = cycleTime;
        //     System.out.println("Case Number: " + x);
        //  System.out.println(differentEventStates.get(0).getEventName());
        events.add(differentEventStates.get(x));

    }

    /**
     * increments the totalTimeused and cycletimeused by one each 1000ms. does
     * the calculations for the percentage and timeleft for both cycle and total
     * time
     */
    public void runTimer() {

        tTask = new TimerTask() {
            public void run() {

                try {

                    /*    calculateCyclusTimeLeft();
                        calculateTotalTimeLeft();
                        calculateTotalPercentageCompleted();
                        calculateCyclusPercentageCompleted();

                        totalTimeUsed++;
                        cycleTimeUsed++;
                     */
                    if ((canRun == true)) {

                        calculateCyclusTimeLeft();
                        calculateTotalTimeLeft();
                        calculateTotalPercentageCompleted();
                        calculateCyclusPercentageCompleted();

                        totalTimeUsed++;
                        cycleTimeUsed++;

                    } else if (canRun == false) {
                       // totalTimeLeft = totalTime;
                        cyclustimeLeft = 0;
                      //  totalTimeUsed = 0;
                        cycleTimeUsed = 0;
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(SystemLogic.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        };
        timer = new java.util.Timer();
        timer.scheduleAtFixedRate(tTask, 1000, 1000);
    }

    /**
     * get the name of the last event added to the events list
     *
     * @return the eventmae as a string
     */
    public String getLastEventState() {
        int eventListSize = events.size();
        String eventName = "";
        if (eventListSize == 0) {
            eventName = events.get(eventListSize).getEventName();
        } else {
            eventName = events.get(eventListSize - 1).getEventName();
        }

        return eventName;

    }

    /**
     * get the name of the last event added to the events list
     *
     * @return the eventmae as a string
     */
    public String getSpesificEventState(int event) {
        int eventListSize = events.size();
        String eventName = "";
        if (eventListSize == 0) {
            eventName = "";
        } else {
            eventName = events.get(eventListSize - 1).getEventName();
        }

        return eventName;

    }

    /**
     * get the time used in the complete battery change
     *
     * @return totalTimeUsed (int)
     */
    public int getTotalTimeUsed() {
        return totalTimeUsed;
    }

    /**
     * get the time used in the cycle
     *
     * @return cycleTimeUsed (int)
     */
    public int getCycleTimeUsed() {
        return cycleTimeUsed;
    }

    /**
     * get the time left of the cyclus
     *
     * @return cyclustimeLeft (int)
     */
    public int getCycleTimeRemaining() {
        return cyclustimeLeft;
    }

    /**
     * get the time left of the total battery change
     *
     * @return totalTimeLeft (int)
     */
    public int getTotalTimeRemaining() {
        return totalTimeLeft;
    }

    /**
     * calculates the time left of the battery change
     *
     * @throws InterruptedException
     */
    public void calculateTotalTimeLeft() throws InterruptedException {
        //System.out.println("Total time left : " + totalTimeLeft);
        if (totalTimeLeft > 0) {
            totalTimeLeft = totalTimeLeft - 1;

        } else {
            // caseScenario = caseScenario + 1;
            totalTimeLeft = 0;
            // switchCases(caseScenario);

        }
        ;
    }

    /**
     * calculates the time left of the cycle to be completed
     */
    public void calculateCyclusTimeLeft() {
        //System.out.println("Cyclus left : " + cyclustimeLeft);
        if (cyclustimeLeft > 0) {
            cyclustimeLeft = cyclustimeLeft - 1;
        } else {
            //CyclustimeLeft = getLastEventTimeSyclus();
            cyclustimeLeft = 0;
        }

    }

    /**
     * calculates the percentage completed for the total batteryChange. updates
     * the totalPercentageCompleted int. does the calculations as a float to get
     * descimals, and casts it back to int
     */
    public void calculateTotalPercentageCompleted() {
        float calculatedPercentage = ((float) totalTime - (float) totalTimeLeft) / (float) totalTime * 100;
        totalPercentageCompleted = (int) calculatedPercentage;
    }

    /**
     * calculates the percentage completed for each cycle. updates the
     * cyclusPercentageCompleted int. does the calculations as a float to get
     * descimals, and casts it back to int
     */
    public void calculateCyclusPercentageCompleted() {
        float calculatedPercentage = (float) cycleTimeUsed / (float) cycleTime * 100;
        cyclusPercentageCompleted = (int) calculatedPercentage;

    }

    /**
     * returns the percentage of the cyclus completed
     *
     * @return an integer cyclusPercentageCompleted for amount completed
     */
    public int getCyclusPercentageCompleted() {
        return cyclusPercentageCompleted;
    }

    /**
     * returns the percentage of the total change completed
     *
     * @return an integer totalPercentageCompleted for amount completed
     */
    public int getTotalPercentageCompleted() {
        return totalPercentageCompleted;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////////////////Needed to add the events///////////////////////////
    public void addEventToList(String command) {
        String time = "";
        String fault = "";
        if (command == "") {
            fault = "";
        } else {
            fault = "Command: " + command;
        }

        eventList.add(fault);
    }

    /**
     * getting the timestamp of the
     *
     * @return the timestamp in format HH:mm:ss
     */
    public String getTimeStamp() {
        String time = "";
        try {
            DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();
            time = " Time: " + sdf.format(date);
        } catch (NullPointerException e) {
        }
        return time;

    }

    /**
     * setting up the different events. Adding an event with name
     */
    public void addingEventStates() {
        Event event1 = new Event(1, "Calibrating arm");
        Event event2 = new Event(2, "Calibrating lift");
        Event event3 = new Event(3, "Calibrating slider");
        Event event4 = new Event(4, "Calibration Complete");
        Event event5 = new Event(5, "going for new battery");
        Event event6 = new Event(6, "Battery retreived, going to idle pos");
        Event event7 = new Event(7, "idle pos await signal from drone");
        Event event8 = new Event(8, "signal received, running lift up for batterychange");
        Event event9 = new Event(9, "Starting drone detection");
        Event event10 = new Event(10, "Detaching battery from drone");
        Event event11 = new Event(11, "Attaching battery to drone");
        Event event12 = new Event(12, "Retreive battery to charging station");
        Event event13 = new Event(13, "Battery attached to station, going to idle pos");
        differentEventStates.add(event1);
        differentEventStates.add(event2);
        differentEventStates.add(event3);
        differentEventStates.add(event4);
        differentEventStates.add(event5);
        differentEventStates.add(event6);
        differentEventStates.add(event7);
        differentEventStates.add(event8);
        differentEventStates.add(event9);
        differentEventStates.add(event10);
        differentEventStates.add(event11);
        differentEventStates.add(event12);
        differentEventStates.add(event13);

    }

}

//////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/**
 * case 1: setting the total time left to the total time value. Setting the
 * cycletime used to 0 to state that a new cycle is starting
 *
 * @param cTime setting the cycle time of the cycle
 * @param totTimeLeft setting the total time left in case the earlier prosess is
 * finished before planed. This also updates the progreessbar in GUI
 * @throws InterruptedException
 */
/*  public void case1(int cTime, int totTimeLeft) throws InterruptedException {
        cycleTimeUsed = 1;
        totalTimeLeft = totTimeLeft;
        cycleTime = cTime;
        cyclustimeLeft = cycleTime;
     //   System.out.println("test case 1");
        //  System.out.println(differentEventStates.get(0).getEventName());
        events.add(differentEventStates.get(0));

    }

    /**
     * case 2: setting the total time left to the total time value. Setting the
     * cycletime used to 0 to state that a new cycle is starting
     *
     * @param cTime setting the cycle time of the cycle
     * @param totTimeLeft setting the total time left in case the earlier
     * prosess is finished before planed. This also updates the progreessbar in
     * GUI
     * @throws InterruptedException
 */
 /*public void case2(int cTime, int totTimeLeft) throws InterruptedException {
        cycleTimeUsed = 1;
        totalTimeLeft = totTimeLeft;
        cycleTime = cTime;
        cyclustimeLeft = cycleTime;
    //    System.out.println("test case 2");
        //   System.out.println(differentEventStates.get(1).getEventName());
        events.add(differentEventStates.get(1));

    }

    /**
     * case 3: setting the total time left to the total time value. Setting the
     * cycletime used to 0 to state that a new cycle is starting
     *
     * @param cTime setting the cycle time of the cycle
     * @param totTimeLeft setting the total time left in case the earlier
     * prosess is finished before planed. This also updates the progreessbar in
     * GUI
     * @throws InterruptedException
 */
 /* public void case3(int cTime, int totTimeLeft) throws InterruptedException {
        cycleTimeUsed = 1;
        totalTimeLeft = totTimeLeft;
        cycleTime = cTime;
        cyclustimeLeft = cycleTime;
   //     System.out.println("test case 3");
        //   System.out.println(differentEventStates.get(2).getEventName());
        events.add(differentEventStates.get(2));

    }

    /**
     * case 4: setting the total time left to the total time value. Setting the
     * cycletime used to 0 to state that a new cycle is starting
     *
     * @param cTime setting the cycle time of the cycle
     * @param totTimeLeft setting the total time left in case the earlier
     * prosess is finished before planed. This also updates the progreessbar in
     * GUI
     * @throws InterruptedException
 */
 /* public void case4(int cTime, int totTimeLeft) throws InterruptedException {
        cycleTimeUsed = 1;
        totalTimeLeft = totTimeLeft;
        cycleTime = cTime;
        cyclustimeLeft = cycleTime;
        System.out.println("test case 4");
        //  System.out.println(differentEventStates.get(3).getEventName());
        events.add(differentEventStates.get(3));

    }

    /**
     * case 5: setting the total time left to the total time value. Setting the
     * cycletime used to 0 to state that a new cycle is starting
     *
     * @param cTime setting the cycle time of the cycle
     * @param totTimeLeft setting the total time left in case the earlier
     * prosess is finished before planed. This also updates the progreessbar in
     * GUI
     * @throws InterruptedException
 */
 /* public void case5(int cTime, int totTimeLeft) throws InterruptedException {
        cycleTimeUsed = 1;
        totalTimeLeft = totTimeLeft;
        cycleTime = cTime;
        cyclustimeLeft = cycleTime;
        //    System.out.println("test case 5");
        //    System.out.println(differentEventStates.get(4).getEventName());
        events.add(differentEventStates.get(4));
    }*/
/**
 * case 6: setting the total time left to the total time value. Setting the
 * cycletime used to 0 to state that a new cycle is starting
 *
 * @param cTime setting the cycle time of the cycle
 * @param totTimeLeft setting the total time left in case the earlier prosess is
 * finished before planed. This also updates the progreessbar in GUI
 * @throws InterruptedException
 */
/* public void case6(int cTime, int totTimeLeft) throws InterruptedException {
        cycleTimeUsed = 1;
        totalTimeLeft = totTimeLeft;
        cycleTime = cTime;
        cyclustimeLeft = cycleTime;
        //    System.out.println("test case 6");
        //    System.out.println(differentEventStates.get(4).getEventName());
        events.add(differentEventStates.get(4));
    }*/
