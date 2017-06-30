package droneplatform2;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

/**
 * an observer updating the fields in the GUI
 *
 */
public class GUIObservable extends Observable {

   // private FaultHandler faultHandler;
    private DataHandler dataHandler;
     Semaphore semaphore = new Semaphore(1, true);
     private FaultLogic faultHandler;
    private BatteryStationLogic batteryStationLogic;
     private byte[] dataFromTeensy;      //The byteArray retrieved from Teensy 
    private SystemLogic events;
    // private EventStates events;
    private ArrayList<BatteryStation> batteries;
    private String[] faultArray;
    private String[] eventArray;
    ArrayList<String> faultList;
    ArrayList<String> eventList;
    private Thread t;
    private boolean limitSwitch;
    private int timeLeft;
    private int timeLeftCyclus;

  
   // public GUIObservable(FaultHandler faultHandler, BatteryStationLogic batteryStationLogic, SystemLogic events) {
        public GUIObservable(FaultLogic faultHandler, BatteryStationLogic batteryStationLogic, SystemLogic events, DataHandler dataHandler, Semaphore semaphore) {
        this.faultHandler = faultHandler;
        this.batteryStationLogic = batteryStationLogic;
        this.events = events;
        this.dataHandler = dataHandler;
        this.semaphore = semaphore;

    }

    /**
     * adds a observer to class
     * @param o 
     */
    @Override
    public synchronized void addObserver(Observer o) {
        super.addObserver(o); //To change body of generated methods, choose Tools | Templates.

    }

    /**
     * setting the data retrieved from system
     */
    public void setData() throws InterruptedException {
      //  faultList = faultHandler.getFaultList();
        batteries = batteryStationLogic.getArrayListBatteries();
        semaphore.acquire();
        dataFromTeensy = dataHandler.getDataFromMega();
        semaphore.release();
        setChanged();
        notifyObservers();
    }

 
    ///////////////////////////////////////////////////////////////
    ////////////////////////FAULT HANDLING/////////////////////////
    /**
     * get the faultmessages
     *
     * @param x the message number in the list
     * @return the message as string
     */
    public String getLastErrorMessage(){
        return faultHandler.getLastEventState();
    }

    
    
    
   
    
    ///////////////////////////////////////////////////////////////
    /////////////////////////EVENT HANDLING////////////////////////
  

    /**
     * get the last events state of the process
     * @return the eventName as String of the last event
     */
    public String getLastEventState() {
        return events.getLastEventState();
    }
    
    
    
            
            /**
     * get the last events state of the process
     * @return the eventName as String of the last event
     */
    public String getSpesificEventState(int event) {
        return events.getSpesificEventState(event);
    }
    
    /**
     * get the total time used in the total process
     * @return the int value of the time used
     */
    public int getTotalTimeUsed() {
        return events.getTotalTimeUsed();
    }
    
    /**
     * get the total time left in the total process
     * @return the int value of the time left
     */
          public int getTimeLeft() {
        return events.getTotalTimeRemaining();
    }
    
          /**
     * get the total time used in the current cycle
     * @return the int value of the time used
     */
     public int getCycleTimeUsed() {
        return events.getCycleTimeUsed();
    }
     
  
     
           /**
     * get the total time left in the current cycle
     * @return the int value of the time left
     */
     public int getCycleTimeLeft()
     {
          return events.getCycleTimeRemaining();
        
     }
     
     
           /**
     * get the percentage progress in the total process
     * @return the int value of the percentage progress (0-100)
     */
     public int getTotalPercentageCompleted()
     {
         return events.getTotalPercentageCompleted();
     }
     
     
       
           /**
     * get the percentage progress in the current cycle
     * @return the int value of the percentage progress in the current cycle (0-100)
     */
     public int getCyclusPercentageCompleted()
     {
         return events.getCyclusPercentageCompleted();
     }
     
  
     
      //////////////////////////////////////////////////////////////////
    /////////////////////////STEPPER ENIGNE CONTROLLER INFORMATION//////////////////////
     
     public int getSpesificValueFromByte(int value)
    {
        return dataFromTeensy[value];
    }
     
     
    
    //////////////////////////////////////////////////////////////////
    /////////////////////////BATTERY INFORMATION//////////////////////
     
  
    /**
     * retrieves the last docked battery
     *
     * @return the int of the last doicked battery
     */
    public int getLastDockedBattery() {
        return batteryStationLogic.getActiveBatteryPlacement();
    }

    /**
     * get spesifik battery cvhargingvalue
     *
     * @param x the number of the battery
     * @return the int of the charging level
     */
    public int getSpesificBatteryChargingLevel(int x) {
        return this.batteryStationLogic.getBatteryChargingPercentage(x);

    }

    /**
     * Returns battery temperatire
     *
     * @param x the number of the battery
     * @return the int of the temperature level
     */
    public float getSpescificBatteryTempertureLevel(int x) {
        return this.batteryStationLogic.getActiveBatteryTemperature(x);
    }

    /**
     * Return battery cycle count.
     *
     * @param x the number of the battery
     * @return the int of the cychlus level
     */
    public int getSpescificBatteryCyclecount(int x) {
        return (int) this.batteryStationLogic.getBatteryChargingCycle(x);
    }

    /**
     * Returns the predicted time to full charge in minutes.
     *
     * @param x the number of the battery
     * @return the int of the miuntes to full level
     */
    public int getSpescificBatteryMinToFull(int x) {
        return this.batteryStationLogic.getTimeToMaxChargingLevel(x);
    }
    
    
/**get the charging voltage of a spesifikk battery from the array list. 
 * 
 * @param x the number of the spesifikk battery in the list
 * @return the voltage of the spesifikk battery
 */
    public int getSpesificChargingVoltage(int x) {
       // return (int)this.batteryStationLogic.getSpesificChargingVoltage(x);
        
        return this.batteryStationLogic.getSpecificbatterychargeCurrent(x);
    }

/**get the limitSwitch of a spesifikk battery from the array list. 
 * This indicates if the battery is docked or not
 * 
 * @param x the number of the spesifikk battery in the list
 * @return the value of the limit switch of the spesifikk battery
 */
    public boolean getSpesificLimitSwitch(int x) {
        return batteryStationLogic.getBatteryLimitSwitchValue(x);
    }

  /**get the status of a spesifikk battery from the array list. 
 * 
 * @param x the number of the spesifikk battery in the list
 * @return the voltage of the spesifikk battery
 */
    public int getBatteriesStatus(int x) {
        return batteryStationLogic.getBatteriesStatus(x);
    }
    
    
      
    
    

    
    
    /**
     * get the timestamp as a string
     * @return timestamp in the "HH:mm:ss" format
     */
    public String getTimeStamp(){
        return events.getTimeStamp();
    }
    
}

    
    
    
    
  