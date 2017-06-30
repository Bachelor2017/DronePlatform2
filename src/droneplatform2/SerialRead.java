package droneplatform2;

import java.util.Arrays;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import java.util.concurrent.Semaphore;

/**
 *
 * her kan det skrives inn test
 */
public class SerialRead implements Runnable {

    SerialCom serialCom;
    Semaphore semaphore;
    SerialPort serialPort;
    //public byte[] dataFromTeensy = new byte[10];
    DataHandler dataHandler;
    int increment;

    /**
     * Setting ut the serialCommunication reading thread.
     *
     * @param serialCom the serialcommunicator class
     * @param s The semaphore added to the communication
     * @param sp the serial communication port
     * @param dh the datahandler class
     */
    public SerialRead(SerialCom serialCom, Semaphore s, SerialPort sp) {
        this.semaphore = s;
        this.serialPort = sp;
        this.serialCom = serialCom;
        //this.dataHandler = dh;

    }

    /**
     * aquires semaphore, reads data and stors it to bytearray data..
     */
    @Override
    public void run() {
        try{
            while (true) {
                System.out.println("Serialread1");
                //byte[] flagByte = serialPort.readBytes(1);
                //if (flagByte[0] == 128) {
                    System.out.println("Serialread2 ");
                    byte[] data = serialPort.readBytes(25);
                     System.out.println("Serialsend:" + Arrays.toString(data));
                    semaphore.acquire();
                    serialCom.setDataFromTeensy(data);
                    semaphore.release();
                //}
            }
        } catch (SerialPortException ex) {
            System.out.println("SerialPortException i SerialRead");
        } catch (InterruptedException e) {
            System.out.println("InterruptedException i SerialRead");
        }
    }

}
