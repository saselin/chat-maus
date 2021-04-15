package de.fhb.fbi.msr.maus.uebung1.chatapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import android.util.Log;

public class PushClient {

    protected static String logger = "PushClient";

    public static final String TERMINATE = "TERMINATE";

    private static final int PORT = 1234; // server details
    private static final String HOST = "10.0.2.2";// "localhost";

    private Socket sock;
    private PrintWriter out; // output to the server

    private IOHandler mIOHandler;

    public PushClient(IOHandler aIOHandler) {
        this.mIOHandler = aIOHandler;
    }

    public void connect() {
        Log.i(logger, "starting client...");

        createSocket();

        try {

            Log.i(logger, "obtaining input stream reader from socket");
            BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            new Thread(new PushReceiver(this, in, mIOHandler)).start();

            Log.i(logger, "got input stream reader: " + in + ". Creating writer.");
            out = new PrintWriter(sock.getOutputStream(), true);
            // try to send a message to the server
            out.println("test message from client");
            // set the print writer on the iohandler
            Thread.sleep(5000);

            mIOHandler.setOutputWriter(out);
            Log.i(logger, "using print writer: " + out);
            mIOHandler.sendOutput("test output from device");
        } catch (Exception t) {
            Log.e(logger, "got exception on connect: " + t, t);
        }
    }

    public void disconnect() {
        Log.i(logger, "disconnecting...");
        out.println(TERMINATE);
        try {
            sock.close();
        } catch (IOException ioe) {
            Log.e(logger, "could not close socket on reconnect attempt. Got: " + ioe, ioe);
        }
    }

    private void reconnect() {
        Log.i(logger, "restarting client...");

        connect();
    }

    private void createSocket() {
        // connect(this.display);
        Runnable socketCreator = new Runnable()
        {

            @Override
            public void run() {
                try {
                    sock = new Socket(HOST, PORT);
                    sock.setSoTimeout(10000000);
                } catch (Exception e) {
                    Log.e(logger, "got exception creating socket: " + e);
                }
                Log.i(logger, "socket created");
            }

        };
        Thread thread = new Thread(socketCreator);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Log.e(logger, "got exception creating socket: " + e);
        }
    }


    private static class PushReceiver implements Runnable {

        private PushClient client;
        private BufferedReader input;
        private IOHandler ioHandler;

        public PushReceiver(PushClient client, BufferedReader input, IOHandler aIOHandler) {
            this.client = client;
            this.input = input;
            this.ioHandler = aIOHandler;
        }

        public void run() {

            Log.i(logger, "starting receiver thread " + super.toString());

            String line;

            try {
                while ((line = input.readLine()) != null) {
                    ioHandler.displayInput(line);
                    Log.i(logger, "wait for input...");
                }
            } catch (IOException t) {
                Log.w(logger, "got exception on run", t);
                client.disconnect();
                client.reconnect();
            }

            Log.i(logger, "receiver thread " + super.toString() + " is done.");
        }

    }

    public interface IOHandler {

        /**
         * Call-back after a message is received from some external message
         * source
         *
         * @param input
         *            the received message
         */
        public void displayInput(String input);

        /**
         * Call this method to send a message to the externel message sink
         *
         * @param output
         *            the message to be sent to the information sink
         */
        public void sendOutput(String output);

        /**
         * sets the writer after a connection to some message sink has been
         * established
         *
         * @param writer
         */
        public void setOutputWriter(PrintWriter writer);

    }

}
