package droneplatform2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 *
 */
public class BatteryStationLogic implements Runnable {

    private ArrayList<BatteryStation> batteries;
    private BatteryStation battery;
    private byte[] batteriStationLocation;
    private final Semaphore semaphore;
    private int batteryStationNumberPossition;
    private int batteryStationYPossition;
    private byte[] dataFromArduino;
    private byte[] dataFromMega;
    private DataHandler dh;
    private Thread t;
    private byte[] chargeCurrent;
    private java.util.Timer timer;
    private TimerTask tTask;
    public int secondsPassed;
    boolean limitSwitch;
    int resetBateryStatus = 0;
    private int zeroPercentageMessureAmount = 0;
       int nextBatteryNumber = 0;

    /**
     * create the batterystationlogic, and fill inn batteries to atrraylist
     */
    public BatteryStationLogic(DataHandler dh, Semaphore semaphore) {
        this.semaphore = semaphore;
        chargeCurrent = new byte[16];
        batteries = new ArrayList<>();
        batteryStationNumberPossition = 0;
        this.dh = dh;
        fillList();
        limitSwitch = false;

    }

    /**
     * start a new thread containing the batterystationLogic
     */
    public void start() {
        t = new Thread(this, "BatteryStationLogic thread");
        t.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                semaphore.acquire();
                dataFromArduino = dh.getDataFromArduino();
                dataFromMega = dh.getDataFromMega();
                dh.setNextBatteryNumberToChange(getNextBatteryToChange());
                updateBatteryInformation(dataFromArduino);
                setAllbatterychargeCurrent();
                if (dataFromMega[12] == 99) {
                    resetGUIData();
                }
                /*if (dataFromMega[11] == 99) {
                    batteryIsDetachedResetGUI();
                }*/

                semaphore.release();

                //    System.out.println( Arrays.toString(chargeCurrent));
            } catch (InterruptedException ex) {
                Logger.getLogger(BatteryStationLogic.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * fill the batterystationArrayList with 16 batterystations
     */
    private void fillList() {
        for (int i = 0; i < 16; i++) {
            battery = new BatteryStation(i);
            batteries.add(battery);
        }
      }

    /**
     * get the complete list of all batterys
     *
     * @return an arraylist of batteryStation
     */
    public ArrayList getArrayListBatteries() {
        return batteries;
    }

    /**
     * gets the data read from the arduino and adds the information to the
     * spesific battery Adds the temperature, batterycychle,timetomax and
     * percentage information
     */
    public void updateBatteryInformation(byte[] incomingDataFromArduino) {

        int i = 1;

        for (int x = 0; x < 16; x++) {

            if (incomingDataFromArduino[i] != 0) {
                batteries.get(x).setPercentageCharged(incomingDataFromArduino[i]);
            } else if ((incomingDataFromArduino[i] == 0) && (batteries.get(x).getChargedPercentage() == 100)) {
                batteries.get(x).setPercentageCharged(100);
            } 
           else if ((incomingDataFromArduino[i] == 0) && (batteries.get(x).getChargedPercentage() >= 10)) {
                batteries.get(x).setPercentageCharged(incomingDataFromArduino[i]);
            } else if ((incomingDataFromArduino[i] == 0) && (batteries.get(x).getChargedPercentage() <= 10)) {
                batteries.get(x).setPercentageCharged(0);
            } else if ((incomingDataFromArduino[i] == 0)) {
                zeroPercentageMessureAmount++;
            } else if ((incomingDataFromArduino[i] == 0) && (zeroPercentageMessureAmount >= 16)) {
                zeroPercentageMessureAmount = 0;
                batteries.get(x).setPercentageCharged(0);
            }
            ////
            i = i + 1;

            //Setting the charging voltage
            float voltage = incomingDataFromArduino[i];
            i = i + 1;
            float voltageDesc = incomingDataFromArduino[i];
            i = i + 1;
            batteries.get(x).setChargingVoltage(voltage + (voltageDesc / 100));

            //Setting minutest to fully charged
            if ((incomingDataFromArduino[i] == 0) || (incomingDataFromArduino[i] == 120)) {
                batteries.get(x).setTimeToMaxBattery(0);
            } else {

                batteries.get(x).setTimeToMaxBattery(incomingDataFromArduino[i]);
            }
//setTimeToMaxChargingLevel(x, dataFromArduino[i]);
            i++;

            //Setting Temperatur on station
            float temperature = incomingDataFromArduino[i]; // whole number temperature
            i = i + 1;                                      // increment the byte placement
            float tempDesc = incomingDataFromArduino[i];    // temperatur descimal
            // find the spesific battery, sett the temperature with combined whole number and the descimal
            if (temperature != 120) {
                batteries.get(x).setTemperature((temperature + (tempDesc / 100)));
            } else {
               // batteries.get(x).setTemperature(0);
            }

//setActiveBatteryTemperature(x, dataFromArduino[i]);
            i = i + 1;

            //setting cycle count on batteries
            float cyclecount = incomingDataFromArduino[i];
            i = i + 1;
            float cycleDesc = incomingDataFromArduino[i];
           
            if(cyclecount!=1.0)
            {
            batteries.get(x).setBatteryCycles((cyclecount + (cycleDesc / 100)) * 100);
            }
            i = i + 1;
            //setting staus on batteries
            batteries.get(x).setBatteryStatus(incomingDataFromArduino[i]);
            i = i + 1;

            /*   if (incomingDataFromArduino[i] == 1) {
                limitSwitch = true;
            } else {
                limitSwitch = false;
            }
             batteries.get(x).setLimiSwitch(limitSwitch);
             */
            if (incomingDataFromArduino[i] == 0) {
               }
            if (incomingDataFromArduino[i] != 0) {
                batteries.get(x).setChargingLevelOnBatteries(incomingDataFromArduino[i]);
            }
            i = i + 1;
            i = i + 1;
        }
        //resetBateryStatus++;

    }

    public void setAllbatterychargeCurrent() {
        chargeCurrent[0] = (byte) 101; //FlagByte
        if (batteries.size() != 0) {
            for (int x = 0; x < 8; x++) {
                chargeCurrent[x + 1] = (byte) batteries.get(x).getChargingLevelOnBatteries();
            }
        }
    }

    public byte[] getAllbatterychargeCurrent() {

        return chargeCurrent;

    }

    public int getSpecificbatterychargeCurrent(int x) {
        int batteryCurrent = batteries.get(x).getChargingLevelOnBatteries();

        return batteryCurrent;
    }

    /**
     * find the next battery in line to be used
     *
     * @return returns the number of the location to the next battery
     */
    public int getActiveBatteryPlacement() {
        batteryStationNumberPossition++;
        if (batteryStationNumberPossition > 15) {
            batteryStationNumberPossition = 0;
        }
        return batteryStationNumberPossition;
    }

    /**
     * Search for the next battery to change. The loop is checking all battery
     * charing levels, and choose the one with the most persentage
     *
     * @return the battery number
     */
    public int getNextBatteryToChange() {
     
        int lastBatteryPercentage = 0;

        for (int x = 0; x < 16; x++) {
            if (getBatteryChargingPercentage(x) > lastBatteryPercentage) {
                lastBatteryPercentage = getBatteryChargingPercentage(x);
                nextBatteryNumber = x;
            }

        }
        nextBatteryNumber = nextBatteryNumber + 1;

        // System.out.println("nextBatteryNumber: " + nextBatteryNumber);
        //  System.out.println("nextBatteryPercentage: " + lastBatteryPercentage);
        //  batteries.get(nextBatteryNumber).setPercentageCharged(0);
        if ((dataFromArduino[10] == 0) && (lastBatteryPercentage <= 60)) {
            nextBatteryNumber = 99;
        }

        return nextBatteryNumber;
    }

    /**
     * get the temperature of active battery
     *
     * @param x the battery in x posistion
     * @return the temperature of battery x
     */
    public float getActiveBatteryTemperature(int x) {
        return batteries.get(x).getTemperature();

    }

    /**
     * sets the temperature of the battery in station
     *
     * @param x the battery in position x
     * @param temperature the temperature of the battery
     */
    public void setActiveBatteryTemperature(int x, float temperature) {
        batteries.get(x).setTemperature(temperature);
    }

    /**
     * returns the time to max charging level of the battery
     *
     * @param x the battery in possition x
     * @return the timetomax charging level of battery in possition x
     */
    public int getTimeToMaxChargingLevel(int x) {
        return batteries.get(x).returnTimeToMaxBattery();

    }

    /**
     * sett the time to max charging level of the battery
     *
     * @param x the battery in possition x
     * @param timeToMax the time to max charging level of the battery
     */
    public void setTimeToMaxChargingLevel(int x, int timeToMax) {
        batteries.get(x).setTimeToMaxBattery(timeToMax);
    }

    /**
     * setting the chargingpercentage of the spesific battery
     *
     * @param x the battery number on station
     * @param percentage the percentage of the battery
     */
    public void setBatteryChargingPercentage(int x, int percentage) {
        batteries.get(x).setPercentageCharged(percentage);
    }

    /**
     * get the charging percentage of the battery
     *
     * @param x the battery numbert
     * @return the battery charging presentage
     */
    public int getBatteryChargingPercentage(int x) {
        return batteries.get(x).getChargedPercentage();
    }

    /**
     * gets the charging cychlus amount
     *
     * @param x the battery number
     * @return the cyclus amount in int
     */
    public float getBatteryChargingCycle(int x) {
        return this.batteries.get(x).getNumberOfChargingCycles();
    }

    /**
     * setting the battery charging cychle
     *
     * @param x the number of the battery
     */
    public void setBatteryChargingCycle(int x, float cycles) {
        this.batteries.get(x).setBatteryCycles(cycles);
    }

    /**
     * setting the chargingVoltage value of the batteryStation
     *
     * @param x the number of the batterystation location
     * @param voltage the chargingVoltage level of the station
     */
    public void setSpesificChargingVoltage(int x, float voltage) {
        this.batteries.get(x).setChargingVoltage(voltage);
    }

    /**
     * getting the voltage charing level at the battery
     *
     * @param x the number of the batterystation location
     * @return the charging level at the batteryStation
     */
    public float getSpesificChargingVoltage(int x) {
        return this.batteries.get(x).getVoltageChargingLevel();
    }

    /**
     * getting the battery status
     *
     * @param x the number of the batterystation location
     * @return the value of the status integer
     */
    public int getBatteriesStatus(int x) {
        return batteries.get(x).getBatteryStatus();
    }

    /**
     * setting the status of the battery
     *
     * @param x integer of the batterystation location
     * @param value the integer of the status
     */
    public void setBatteriesStatus(int x, int value) {
        batteries.get(x).setBatteryStatus(value);
    }

    /**
     * getting the limitSwitch boolean value
     *
     * @param x integer of the batterystation location
     * @return the boolean value of the limitSwitch
     */
    public boolean getBatteryLimitSwitchValue(int x) {
        return batteries.get(x).returnLimitSwitch();
    }

    /**
     * set the limitSwitch boolean value
     *
     * @param x integer of the batterystation location
     * @param value the boolean value of the limitswitch
     */
    public void setLimitSwitcxhValue(int x, boolean value) {
        batteries.get(x).setLimiSwitch(value);
    }

    public void resetGUIData() {

        for (int x = 0; x < 16; x++) {
            batteries.get(x).setPercentageCharged(1);
            batteries.get(x).setChargingVoltage(1);
            batteries.get(x).setTimeToMaxBattery(1);
            batteries.get(x).setTemperature(1);
            batteries.get(x).setBatteryCycles(1);
            batteries.get(x).setBatteryStatus(1);
            batteries.get(x).setChargingLevelOnBatteries(1);

        }
        //resetBateryStatus++;

    }
/*
    public void batteryIsDetachedResetGUI() {
        batteries.get(nextBatteryNumber).setPercentageCharged(0);
        batteries.get(nextBatteryNumber).setChargingVoltage(0);
        batteries.get(nextBatteryNumber).setTimeToMaxBattery(0);
        batteries.get(nextBatteryNumber).setTemperature(0);
        batteries.get(nextBatteryNumber).setBatteryCycles(0);
        batteries.get(nextBatteryNumber).setBatteryStatus(0);
        batteries.get(nextBatteryNumber).setChargingLevelOnBatteries(0);

    }
    */
}

//////////////////////////////////////SE OM KAN TAS BORT
/**
 * setting spesific battery to docking
 *
 * @param x
 * @param value
 */
/*  public void setBatteriesDocking(int x, boolean value) {
        batteries.get(x).setDocked(value);
    }*/
/**
 * checking if battery is in docking station
 *
 * @param x number of the batteryStation
 * @return the boolean value if the battery is docked at station
 */
/*   public boolean isBatteriesDocked(int x) {
        return batteries.get(x).isDocked();
    }
 */
/**
 * gives the XYZ location of the batterystation
 *
 * @param x X is the number of the batterystation in the list
 * @return a byte[] where first byte is X-value, second is Y-Value and last i
 * z-Value
 */
/*    public byte[] getActiveBatteryXYZLocation(int x) {
        this.batteriStationLocation = new byte[3];
        batteriStationLocation = batteries.get(x).getBatteryStationLocation();
        return batteriStationLocation;
    }*/
/**
 * get the charging level of the active battery
 *
 * @param x the number of the batterystation
 * @return the int of the charging level in percentage
 */
/*   public int getActiveBatteryChargingLevel(int x) {
        return batteries.get(x).getBatteryLevel();

    }*/
/**
 * setting the spesific battery to charging station
 *
 * @param x the number of the batterystation
 */
/*  public void settBatteryToChargeInStation(int x) {
        batteries.get(x).setDocked(true);
    }*/
/**
 * setting the spesific battery to charging station
 *
 * @param x the number of the batterystation
 */
/* public void releaseBatteryFromChargeInStation(int x) {
        batteries.get(x).setDocked(false);
    }*/
