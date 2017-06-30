/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package droneplatform2;

//import java.nio.ByteBuffer;
import java.util.TimerTask;

/**
 * runs on timertask. checs the functions each run
 *
 * @author Jørgen
 */
public class GUIController extends TimerTask {

    private DataHandler datahandler;

    public GUIController() {
    }

    /**
     *
     */
    @Override
    public void run() {
        //System.out.println("Request data");
        //this.datahandler.incrementRequestCode();
    }

    /**
     * setting the datahandler to the timertask
     *
     * @param datahandler
     */
    public void setDatahandler(DataHandler datahandler) {
        this.datahandler = datahandler;
    }

    /**
     * checs value if true, and ends the boolean value to datahandler
     *
     * @param status The status of the motor (true/false)
     * @param direction The direction/funcion of the engine (int:0=idle, 1 =
     * rev, 2=forward)
     */
    public void setLiftStatus(boolean value, int direction) {
       this.datahandler.motorStatus(1, value, direction);

    }
    
    
    public void setMotorNumberToIdle()
    {
        this.datahandler.setIdleMotorNumber();
                
    }
    
    /*
    public void setArmCalibrated(boolean value)
    {
        this.datahandler.setArmCalibrated(value);
    }
    */
    
    public void setCalibrationStatus(boolean value)
    {
        this.datahandler.setCalibrationStatus(value);
    }

    
    public void setDataToDataHandler(int byteNumber, int value)
    {
        this.datahandler.setSpesificDataFromGUI(byteNumber,value);
    }
    
    public void resetAfterDroneAttached()
    {
        this.datahandler.resetAfterDroneAttached();
    }
    
    
    
    /**
     * checs value if true, and ends the boolean value to datahandler
     *
     * @param status The status of the motor (true/false)
     * @param direction The direction/funcion of the engine (int:0=idle, 1 =
     * rev, 2=forward)
     */
    public void setConveyorStatus(boolean value, int direction) {
       this.datahandler.motorStatus(4, value, direction);

    }

    /**
     * Setting the running mode of platform. true = auto , false = manual
     *
     * @param value boolean value , true =auto , false = manual
     */
    public void setPlatformMode(boolean value) {

        this.datahandler.setPlatformMode(value);      //auto mode

    }
    


    /**
     * setting the speed value in persentage as an integer 0-100
     *
     * @param value the speed value 0-100 (percentage)
     */
    public void setSpeedValue(int value) {
        this.datahandler.setSpeedValue(value);
    }

    /**
     * Setting the status of the robotArm
     *
     * @param status The status of the motor (true/false)
     * @param direction The direction/funcion of the engine (int:0=idle, 1 =
     * rev, 2=forward)
     */
    public void setArmStatus(boolean value, int direction) {
       this.datahandler.motorStatus(3, value, direction);
    }

    /**
     * Setting the status of the X-Axis engine
     *
     * @param status The status of the motor (true/false)
     * @param direction The direction/funcion of the engine (int:0=idle, 1 =
     * rev, 2=forward)
     */
    public void setXAxisStatus(boolean value, int direction) {
        this.datahandler.motorStatus(2, value, direction);
    }
   
    
    
  
}
