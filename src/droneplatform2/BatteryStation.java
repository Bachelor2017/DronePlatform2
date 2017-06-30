package droneplatform2;

import java.util.TimerTask;
import java.util.Timer;
/*

test 3


 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Batteristation for batteri charging
 *
 */
public class BatteryStation {

    private boolean dockingStatus;
    private int batteryLevel;
    private int batteryStatus;
    private int xValue;
    private int yValue;
    private int zValue;
    private byte[] stationLocation;
    private int secondsPassed;
    private Timer timer;
    private TimerTask tTask;
    private int batteryPossition;
    private int chargingTimeToMax;
    private float temperature;
    private int timeToMaxBattery;
    private int chargedPercentage;
    private float batteryCycles;
    private float voltageChargingLevel;
    private boolean limitSwitch;
    private int chargingLevelOnBatteri;

    /**
     * constructor of batteryStation class
     *
     * @param batteryPossition a integer describing number in line of battery.
     * not in use later. CAN DELETE
     */
    public BatteryStation(int batteryPossition) {
chargingLevelOnBatteri = 0;
        this.batteryPossition = batteryPossition;
        batteryStatus = 0;
        batteryLevel = 0;
        dockingStatus = true;
       // setDocked(dockingStatus);
        this.stationLocation = new byte[3];
        chargingTimeToMax = 100;   //3600 oringalt

    }

    

    /**
     * sett the temperature of the battery
     *
     * @param temp of battery
     */
    public void setTemperature(float temp) {
        temperature = temp;
    }

    /**
     * returning the battery temperature
     *
     * @return battery temperature int
     */
    public float getTemperature() {
        /*    if (temperature == 120.35)
        {
            temperature = 0;
        }*/
        return temperature;
    }

    /**
     * Setting the time to battery is fully charged
     *
     * @param time to battery is fully charged int
     */
    public void setTimeToMaxBattery(int time) {
        timeToMaxBattery = time;
    }

    /**
     * returns the time for the battery is fully charged
     *
     * @return timeToMaxBattery
     */
    public int returnTimeToMaxBattery() {
        return timeToMaxBattery;
    }

    /**
     * returns the battery charging percentage
     *
     * @param percentage of charged battery
     */
    public void setPercentageCharged(int percentage) {
        this.chargedPercentage = percentage;
    }

    /**
     * returns the percentage of the charged battery
     *
     * @return percetage of battery
     */
    public int getChargedPercentage() {
        /*  if (chargedPercentage==-1)
        {
            chargedPercentage=0;
        }*/
        return chargedPercentage;
    }

    /**
     * setting number of cychles the battery has been charged
     *
     * @param numberOfSycles the amount of times the battery has been charged
     */
    public void setBatteryCycles(float numberOfSycles) {
        this.batteryCycles = numberOfSycles;
    }

    /**
     * returning the amount of charging cstatuychles of the battery
     *
     * @return an int describing number of charging cychles
     */
    public float getNumberOfChargingCycles() {
        /* if(batteryCycles==-1)
        {
            batteryCycles=0;
        }*/
        return batteryCycles;
    }

    /**
     * get the batterystatus from the station
     *
     * @return
     */
    public int getBatteryStatus() {
        return batteryStatus;
    }

    /**
     * Setting the batterystatus to the station
     *
     * @param value a integer for the batterystatus
     */
    public void setBatteryStatus(int value) {
        batteryStatus = value;
    }

    /**
     * setting the voltage charging the battery
     *
     * @param voltageCharg the float value of the charger
     */
    public void setChargingVoltage(float voltageCharg) {
        voltageChargingLevel = voltageCharg;
    }

    /**
     * get the voltage charginbg the battery
     *
     * @return the charging voltage
     */
    public float getVoltageChargingLevel() {
        return voltageChargingLevel;

    }

    /**
     * setting the tru/false value of the limitSwitch
     *
     * @param value boolean value showing if the battery is in docking or not
     */
    public void setLimiSwitch(boolean value) {
       // this.limitSwitch = value;

    }

    /**
     * returning the value of the limitSwitch
     *
     * @return boolean value of limitSSwitch
     */
    public boolean returnLimitSwitch() {
        if(chargedPercentage!=0)
        {
            limitSwitch = true;
        }
        else
        {
            limitSwitch = false;
        }
        return this.limitSwitch;
    }
    
    
    
    public void setChargingLevelOnBatteries(int chargingLevel)
    {
        this.chargingLevelOnBatteri = chargingLevel;
    }
    
    
    public int getChargingLevelOnBatteries()
    {
        return  this.chargingLevelOnBatteri;
    }

    /////////////////////////////////////////////////////////////
    //////////////////Setting the location paramters of the batterystation. 
    //////////////////not sure if should be used////////////////////////////
    ////////ALT UNDER KAN TAS BORT. SKAL IKKE BRUKES/////////////////////////
    
    /**
     * Returning the X-Value of the charger
     *
     * @return xValue of the charger
     */
    public int getxValue() {
        return xValue;
    }

    /**
     * Setting the X-Value of the station
     *
     * @param xValue setting xValue of station
     */
    public void setxValue(int xValue) {
        this.xValue = xValue;
    }

    /**
     * Returning the Y-Value of the charger
     *
     * @return yValue of the charger
     */
    public int getyValue() {
        return yValue;
    }

    /**
     * Setting the Y-Value of the station
     *
     * @param yValue setting xValue of station
     */
    public void setyValue(int yValue) {
        this.yValue = yValue;
    }

    /**
     * Returning the Z-Value of the charger
     *
     * @return zValue of the charger
     */
    public int getzValue() {
        return zValue;
    }

    /**
     * Setting the Z-Value of the station
     *
     * @param zValue setting xValue of station
     */
    public void setzValue(int zValue) {
        this.zValue = zValue;
    }

    /**
     * returns the location in X,Y and Z direction of the station
     *
     * @return stationLocation
     */
    public byte[] getBatteryStationLocation() {
        stationLocation[0] = (byte) xValue;
        stationLocation[1] = (byte) yValue;
        stationLocation[2] = (byte) zValue;
        return stationLocation;
    }

    /**
     * Setting the location of the batteryStation
     *
     * @param newXValue, xvalue of the station
     * @param newYValue yvalue of the station
     * @param newZValue zvalue of the station
     */
    public void setBatteryStationLocation(int newXValue, int newYValue, int newZValue) {
        xValue = (byte) newXValue;
        yValue = (byte) newYValue;
        zValue = (byte) newZValue;
    }
}

    //////////////////////////////////////////////////////////////////////
    ////////////////////////USED FOR TESTING//////////////////////////////
    //////////////////////////////////////////////////////////////////////
    /**
     * Returns the number of seconds the batterystation has batteri on charging
     * returns 0 if batterDocking is false
     */
   /* public int getNumberOfSecondsCharged() {
        return secondsPassed;
    }*/

    /**
     * Sets the batterycharged value. makes sure the value uios not below 0 or
     * above 100
     *
     * @param value the value of batterycharging status
     */
   /* public void setBatteryLevel(int value) {
        if (value < 0) {
            batteryLevel = 0;
        } else if (value > 100) {
            batteryLevel = 100;
        } else {
            batteryLevel = value;

        }
    }*/

    /**
     * The value is calculated from the soconds in docking to seconds to full
     * carge ratio
     *
     * @return the value of the battery in persentage
     */
  /*  public int getBatteryLevel() {
        batteryLevel = (secondsPassed / chargingTimeToMax) * 100;
        return batteryLevel;
    }*/

    /**
     * Setting the docking status of the station If battery is docked, the timer
     * starts running. of the battery is out of docking, the timer pstops and
     * resets
     *
     * @param value boolean value , true if battery is in docking, and false if
     * station is empty
     */
 /*   public void setDockedtest(boolean value) {
        if (value == true) {
            tTask = new TimerTask() {
                public void run() {
                    secondsPassed++;
                    // System.out.println("Battery :" + batteryPossition + " ,Seconds Passed: " + secondsPassed);
                    //   setTemperature(temperature+secondsPassed);
                    // System.out.println("Temp:" +getTemperature());
                }
            };

            timer = new Timer();
            timer.scheduleAtFixedRate(tTask, 1000, 1000);
        } else {
            timer.cancel();
            timer.purge();
            secondsPassed = 0;
            chargedPercentage = 0;
        }
        dockingStatus = value;
    }*/
    
    /**
     * Check if there is battery docked to station
     *
     * @return boolean signal that staes if there is battery in station (true)
     * or not (false)
     *
     */
 /*   public boolean isDocked() {
        return dockingStatus;
    }
*/
    /**
     * Setting the docking status of the station
     *
     * @param value boolean value , true if battery is in docking, and false if
     * station is empty
     */
  /*  public void setDocked(boolean value) {

        dockingStatus = value;
    }

}
*/