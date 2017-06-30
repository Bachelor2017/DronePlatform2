package droneplatform2;

import droneplatform2.DataHandler;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;

/**
 *
 * @author
 */
public class UDPReceive extends Thread {

    Semaphore semaphore = new Semaphore(1, true);
    private DataHandler dh;
    private int consumerID;
    private int numberOfProducerThreads;
    private boolean stop;
    private boolean available;
    private long sleepTime;

    private final int PORT;
    private static final int PARAMS = 10;
    private DatagramSocket socket;
    private DatagramPacket datagram;

    public UDPReceive(int PORT, DataHandler dh, Semaphore semapore) throws SocketException {

        this.sleepTime = 1;
        stop = false;
        this.dh = dh;
        this.semaphore = semaphore;
        this.PORT = PORT;
        byte[] buf = new byte[PARAMS];
        datagram = new DatagramPacket(buf, buf.length);
        socket = new DatagramSocket(this.PORT);

    }

    public void run() {

        while (!stop) {
            System.out.println("i run");
            byte[] b = new byte[10];    // size of byte to be received
            try {
                b = receiveParam();

            } catch (IOException ex) {
                Logger.getLogger(UDPReceive.class.getName()).log(Level.SEVERE, null, ex);
            }
          //  System.out.println(Arrays.toString(b));
          //  System.out.println("prosess");
            processData(b);

        }

    }

    public byte[] receiveParam() throws IOException {
        socket.receive(datagram);
        byte[] datagramData = datagram.getData();
        return datagramData;
    }

    private void processData(byte[] b) {
        System.out.println(b);
        int value = 0;
        if (b[0] == 1) {
            value = 1;
        } else if (b[0] != 1) {
            value = 0;
        }
        try {
            semaphore.acquire();
            dh.droneOnPlatform(value);
            semaphore.release();
        } catch (InterruptedException ex) {
            Logger.getLogger(UDPReceive.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
