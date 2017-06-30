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

//MULIG DENNE IKKE BRUKES: SLETT OM DEN IKKE BLIR TATT I BRUK
public class SerialComTeensyServo implements Runnable {

    private SerialPort serialPort;
    Semaphore semaphore = new Semaphore(1, true);
    // private Thread reader; // reads from arduino
    // private Thread sender;  // writes to arduino
    // public byte[] dataFromArduino = new byte[10];
    // public byte[] dataToArduino = new byte[2];
    private DataHandler dataHandler;
    private int increment1;
    private int increment2;
    private Thread t;
    private boolean hasReceived;

    /**
     *
     * @param comPort the serialcommunication port
     * @param dataHandler the datahandler
     */
    public SerialComTeensyServo(String comPort, DataHandler dataHandler, Semaphore semaphore) {
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
                    System.out.println("send");
                    byte[] dataToTeensy = new byte[5];
                    semaphore.acquire();
                    dataToTeensy = dataHandler.getDataToMega();
                    semaphore.release();
                    increment1++;
                    System.out.println("Serialsend:" + Arrays.toString(dataToTeensy));

                    serialPort.writeBytes(dataToTeensy);
                    hasReceived = false;
                }
if(!hasReceived)
{
                System.out.println("read");
                byte[] data = serialPort.readBytes(1);
                if (data[0] == -128) {

                    byte[] dataFromTeensynoToDH = serialPort.readBytes(10);
                    increment2++;
                    semaphore.acquire();
                    dataHandler.setDataFromArduino(dataFromTeensynoToDH);
                    semaphore.release();
                    System.out.println("Read Arranged " + Arrays.toString(dataFromTeensynoToDH));
                    hasReceived = true;
                }
}

            }

        } catch (SerialPortException ex) {
            System.out.println("SerialPortException i SerialRead");
        } catch (InterruptedException ex) {
            Logger.getLogger(SerialComTeensyServo.class.getName()).log(Level.SEVERE, null, ex);
        }
   
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
