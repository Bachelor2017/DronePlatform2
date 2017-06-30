/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class FaultLogic implements Runnable {

    ///FRA eventStates
    private ArrayList<Event> differentFaultStates;
    private ArrayList<Event> faults;
    private ArrayList<String> faultList;

   
    TimerTask tTask;
    java.util.Timer timer;
    boolean runState = true;

    private Thread t;
    private DataHandler dataHandler;
    private byte[] dataFromArduino;
    private int caseScenario;
    private Semaphore semaphore;
    private boolean platformMode = false;

    boolean value = false;

    public FaultLogic(DataHandler dh, Semaphore semaphore) {
        this.dataHandler = dh;
        this.semaphore = semaphore;

        caseScenario = 500;

        //FRA EVENTSTATES
        faultList = new ArrayList<>();
        faults = new ArrayList<>();
        differentFaultStates = new ArrayList<>();
        addingFaultStates();
        Event fault = new Event(8, "");
        faults.add(fault);
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
                dataFromArduino = dataHandler.getDataFromArduino();
                semaphore.release();

                if (caseScenario != dataFromArduino[7]) {
                    caseScenario = dataFromArduino[7];
                    switchCases(caseScenario);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(FaultLogic.class.getName()).log(Level.SEVERE, null, ex);
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

                break;
            case (1):
                //(runState) {
                case1();            //setting the event time to 13, and time used to 0
                // }
                break;
            case (2):
                // if (runState) {
                case2();           //setting the event time to 13, and time used to 0
                // }
                break;
            case (3):
                case3();            //setting the event time to 13, and time used to 0

                break;
            case (4):
                case4();           //setting the event time to 13, and time used to 0

                break;
            case (5):
                case5();            //setting the event time to 13, and time used to 0

                break;
            case (16):
                case6();           //setting the event time to 13, and time used to 0

                break;
        }
    }

    /**
     * case 1: setting the total time left to the total time value. Setting the
     * cycletime used to 0 to state that a new cycle is starting
     *
     * @param cTime setting the cycle time of the cycle
     * @param totTimeLeft setting the total time left in case the earlier
     * prosess is finished before planed. This also updates the progreessbar in
     * GUI
     * @throws InterruptedException
     */
    public void case1() throws InterruptedException {

        //  System.out.println("test case 1");
        //  System.out.println(differentEventStates.get(0).getEventName());
        faults.add(differentFaultStates.get(0));

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
    public void case2() throws InterruptedException {

        //   System.out.println("test case 2");
        //   System.out.println(differentEventStates.get(1).getEventName());
        faults.add(differentFaultStates.get(1));

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
    public void case3() throws InterruptedException {

        //   System.out.println("test case 3");
        //   System.out.println(differentEventStates.get(2).getEventName());
        faults.add(differentFaultStates.get(2));

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
    public void case4() throws InterruptedException {

        //  System.out.println("test case 4");
        //  System.out.println(differentEventStates.get(3).getEventName());
        faults.add(differentFaultStates.get(3));

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
    public void case5() throws InterruptedException {

        //    System.out.println("test case 5");
        //    System.out.println(differentEventStates.get(4).getEventName());
        faults.add(differentFaultStates.get(4));
    }

    /**
     * case 6: setting the total time left to the total time value. Setting the
     * cycletime used to 0 to state that a new cycle is starting
     *
     * @param cTime setting the cycle time of the cycle
     * @param totTimeLeft setting the total time left in case the earlier
     * prosess is finished before planed. This also updates the progreessbar in
     * GUI
     * @throws InterruptedException
     */
    public void case6() throws InterruptedException {

        //    System.out.println("test case 6");
        //    System.out.println(differentEventStates.get(4).getEventName());
        faults.add(differentFaultStates.get(5));
    }

    /**
     * increments the totalTimeused and cycletimeused by one each 1000ms. does
     * the calculations for the percentage and timeleft for both cycle and total
     * time
     */
    public void runTimer() {

        tTask = new TimerTask() {
            public void run() {

                if (caseScenario != 0) {

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
        int eventListSize = faults.size();
        return faults.get(eventListSize - 1).getEventName();

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

        faultList.add(fault);
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
    public void addingFaultStates() {

        Event event1 = new Event(1, "Fault 1: run lift only up");
        Event event2 = new Event(2, "Fault 2: Cant detach battery from drone");
        Event event3 = new Event(3, "Fault 3: Cant detach battery from station");
        Event event4 = new Event(4, "Fault 4: Cant attach battery to dockingstation");
        Event event5 = new Event(5, "Fault 5: Cant attach battery to drone");
        Event event6 = new Event(6, "Fault 3");
        Event event7 = new Event(7, "Fault 4");
        Event event8 = new Event(8, "Fault 5");
        Event event9 = new Event(9, "Fault 3");
        Event event10 = new Event(10, "Fault 4");
        Event event11 = new Event(11, "Fault 5");
        Event event12 = new Event(12, "idle");
        differentFaultStates.add(event1);
        differentFaultStates.add(event2);
        differentFaultStates.add(event3);
        differentFaultStates.add(event4);
        differentFaultStates.add(event5);
        differentFaultStates.add(event6);
        differentFaultStates.add(event7);
        differentFaultStates.add(event8);
        differentFaultStates.add(event9);
        differentFaultStates.add(event10);
        differentFaultStates.add(event11);
    }

}
