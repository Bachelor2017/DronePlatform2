package droneplatform2;

import java.io.IOException;
import java.util.Arrays;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Creates the new SerialCom class. creates a new Semaphore wich is shared
 * between serial read and send to make sure on is running at the time
 *
 */
public class SerialComMega implements Runnable {

    private SerialPort serialPort;
    Semaphore semaphore = new Semaphore(1, true);
    private DataHandler dataHandler;
    private int increment1;
    private int increment2;
    private Thread t;
    private boolean hasReceived;
    private byte[] oldData = new byte[4];
    byte[] dataFromTeensynoToDH = new byte[13];

    /**
     *
     * @param comPort the serialcommunication port
     * @param dataHandler the datahandler
     */
    public SerialComMega(String comPort, DataHandler dataHandler, Semaphore semaphore) {
        serialPort = new SerialPort(comPort); //"/dev/ttyUSB0"
        connect();
        this.dataHandler = dataHandler;
        this.semaphore = semaphore;
        hasReceived = true;

    }

    /**
     * start a new thread containing the batterystationLogic
     */
    public void start() {
        t = new Thread(this, "BatteryStationLogic thread");
        t.start();
    }

    /**
     * Creats and starts the threads read and send.
     */
    public void connect() {
        try {
            if (!serialPort.isOpened()) {
                serialPort.openPort();
                getSerialPort().setParams(9600, 8, 1, 0);
                // reader = new Thread(new SerialReadArduino(this, semaPhore, serialPort, dataHandler));         
                // reader.start();
            }
        } catch (SerialPortException e) {
            System.out.println("No Port Found On: " + System.getProperty("os.name"));
        }
    }

    @Override
    public void run() {
        try {

            while (true) {
                if (hasReceived) {
                    byte[] dataToTeensy = new byte[7];
                    semaphore.acquire();
                    dataToTeensy = dataHandler.getDataToMega();
                    semaphore.release();
                    System.out.println("Read Arranged MEGA " + Arrays.toString(dataToTeensy));
                    increment1++;
                    serialPort.writeBytes(dataToTeensy);
                    hasReceived = false;
                }

                if (!hasReceived) {
                    byte[] data = serialPort.readBytes(1);
                    if (data[0] == 101) {
                        byte[] dataFromTeensynoToDH = serialPort.readBytes(13);
                         byte[] testTeensy = new byte[10];
                        // System.out.println("Read Arranged MEGA " + Arrays.toString(dataFromTeensynoToDH));
                        semaphore.acquire();
                        dataHandler.setDataFromMega(dataFromTeensynoToDH);
                        semaphore.release();
                     
                       
                        if (testTeensy != dataFromTeensynoToDH) {
                   //     System.out.println("Send Arranged MEGA" + Arrays.toString(dataFromTeensynoToDH));
                        }
                        testTeensy = dataFromTeensynoToDH;

                        hasReceived = true;

                    }
                }
            }

        } catch (SerialPortException ex) {
            System.out.println("SerialPortException i SerialRead");
        } catch (InterruptedException ex) {
            Logger.getLogger(SerialComMega.class.getName()).log(Level.SEVERE, null, ex);
        }
      
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
      
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public SerialPort getSerialPort() {
        return this.serialPort;
    }

    public String[] getPortList() {
        String[] portNames = SerialPortList.getPortNames();

        if (portNames.length == 0) {
            System.out.println("There are no serial-ports :( You can use an emulator, such ad VSPE, to create a virtual serial port.");
            System.out.println("Press Enter to exit...");
            try {
                System.in.read();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        for (int i = 0; i < portNames.length; i++) {
            System.out.println(portNames[i]);
        }
        return portNames;
    }

}
