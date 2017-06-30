package droneplatform2;

import java.util.Arrays;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
////////////PROTOCOLLS///////////////
////Data from GU to be sendt to MEGA (stepper engine controller) -dataToMega
//byte[0] - 101    (flagbyte)
//byte[1] - 0-1    (auto/manual mode)
//byte[2] - 1-10   (motor number)
//byte[3] - 0-2    (Motor direction)    0 = idle, 1 = rev , 2 = forward
//byte[4] - 0-1    (start calibration ,1 = true     
//byte[5] - neste batteri klart til bytte
//byte[6] - drone on platform 1 = drone sent signal 
//byte[7] - fixed the error with drone attached on battery under change
//
//
//
/////Data from arduino controller to GUI   - dataFromArduino//  Battery information
//
///byte[0] - 0-15   (BatteryNumber)
//Repeat x16 batteries  total of byte[176]
//byte[1] - 0-100% (Relative charge)
//byte[2] - Voltage
//byte[3] - Voltage descimal
//byte[4] - Minutes remaining to full
//byte[5] - Temperature
//byte[6] - Temperature descimal
//byte[7] - cycle count
//byte[8] - cycle count Descimal
//byte[9] - Battery Status


//Data from Mega controller to GUI   - dataFromMega//   Stepper controller information
//
//byte[0] motor 1 (lift)   position
//byte[1] motor 1 (lift)   position
//byte[2] motor 2 (slider) position
//byte[3] motor 2 (slider) position
//byte[4] motor 3 (arm)    position
//byte[5] motor 3 (arm)    position
//byte[6] Case number    
//byte[7] Fault number/Motor guard
//byte[8] calibrationStatus   0= idle, 1 = calibrating slider, 2 = calibrating lift, 3 = calibrating arm, 4 = calibration done
//byte[9] drone located     0 = no drone on platform, 1 = drone located on platform
//byte[10] number of batteries changed
//byte[11] battery is detached from station
//byte[12] resett GUI batteryinformation

public class DataHandler {

    private byte[] dataFromArduino;     //The byteArray retrieved from Arduino 
    private byte[] dataToMega;        //The byteArray to be sendt to Teensy 
    private byte[] dataFromMega;      //The byteArray retrieved from Teensy 
    private byte[] chargeCurrent;       //The charge current for each battery
    /// for testing
    private java.util.Timer timer;
    private TimerTask tTask;
    public int secondsPassed;
    public int eventStatus = 0;    //bare til test
    public boolean platformMode = false;
    public boolean droneOnPlatform = false;
    /// testing slutt
   

    public DataHandler() {

        dataFromArduino = new byte[176];
        dataToMega = new byte[8];
        dataFromMega = new byte[13];
        dataToMega[0] = 101;
        dataToMega[1] = 1; //setting to manual from start
        dataFromMega[5] = 1;  //????????????????????????????????????????????????????
        chargeCurrent = new byte[8];
        
        for(int i = 0; i < 8; i++){
            // init
            chargeCurrent[i] = (byte) 150;
        }
       

    }

    /**
     * get the data retrieved from the arduino controller
     *
     * @return retuns a byte[] retrieved from the mikrocontroller
     */
    public byte[] getDataFromArduino() {
        return dataFromArduino;
    }

    /**
     * setting the data from the arduinoController
     *
     * @param newData the data updating the dataFromArduino byte[]
     */
    public void setDataFromArduino(byte[] newData) {
        this.dataFromArduino = newData;
    }

    /**
     * get the data retrieved from the teensy controller
     *
     * @return retuns a byte[] retrieved from the mikrocontroller
     */
    public byte[] getDataFromMega() {     //byte[1] er case
   
        return dataFromMega;
    }

    /**
     * setting the data from the teensyController
     *
     * @param newData the data updating the dataFromMega byte[]
     */
    public void setDataFromMega(byte[] serialData) {
        this.dataFromMega = serialData;

    }
    
    
    public void resetAfterDroneAttached()
    {
         this.dataToMega[7] = (byte) 99;
    }
    

   
    /**
     * if the drone signal is received, the byte is sett to 1. 
     * @param value the value of the signal from drone. 1= drone located, 0 = no drone on platform
     */
    public void droneOnPlatform(int value)
    {
        this.dataToMega[6] = (byte)value;

    }

    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////FROM GUI////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
    /**
     * Setting the value of the spesific stepper engine
     *
     * @param motorNumber the number of the motor (1-5)
     * @param status The status of the motor (true/false)
     * @param direction The direction/funcion of the engine (int:0=idle, 1 =
     * rev, 2=forward)
     */
    public void motorStatus(int motorNumber, boolean status, int direction) {
        int modeNumber = dataToMega[1];
      //  if (modeNumber == 1) {

            //the number of the motor to be activated
            dataToMega[2] = (byte) motorNumber;    //Setting engine number to the bytearray
            dataToMega[3] = (byte) direction;    //setting the direction to byte 3 in  the bytearray
      //  }
    }

    public void setIdleMotorNumber() {
        dataToMega[2] = 0;
    }

 
    public void setCalibrationStatus(boolean value) {
        if (value == true) {
            dataToMega[4] = 106;
        } else {
            dataToMega[4] = 0;
        }
        System.out.println(Arrays.toString(dataToMega));
    }

    public void setSpesificDataFromGUI(int byteNumber, int value) {
        dataToMega[byteNumber] = (byte) value;
     //   System.out.println("data to Teensy controller: " + Arrays.toString(dataToMega));
     //   System.out.println("data From Teensy controller: " + Arrays.toString(dataFromMega));
     
    }

    /**
     * Setting the running mode of platform. true = auto , false = manual
     *
     * @param value boolean value , true =auto , false = manual
     */
    public void setPlatformMode(boolean value) {
        platformMode = value;
        getPlatformMode();
        if (value) {
            dataToMega[1] = 99;      //auto mode

        } else if (!value) {

            dataToMega[1] = 1;      //Manuel mode
        }
      //  System.out.println(Arrays.toString(dataToMega));
  //  System.out.println(platformMode);
   
    }

    public boolean getPlatformMode() {

        return platformMode;

    }

    /**
     * Setting the speed value of the engine
     *
     * @param speed 0-100, (percentage)
     */
    public void setSpeedValue(int speed) {
        //  dataToMega[4] = (byte) speed;
        //    System.out.println(Arrays.toString(dataToMega));
    }

    /**
     * get the data to be sendt to the teensy controller
     *
     * @return retuns a byte[] to be sendt to the teensy controller
     */
    public byte[] getDataToMega() {

        return dataToMega;
    }

    /**
     * setting the data to the TeensyController
     *
     * @param newData the data updating the dataToMega byte[]
     */
    public void setDataToTeensy(byte[] newData) {
        this.dataToMega = newData;
    }

    public void setNextBatteryNumberToChange(int batteryNumber) {
        this.dataToMega[5] = (byte) batteryNumber;

    }
    
  

}
