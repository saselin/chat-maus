package de.fhb.fbi.msr.maus.uebung1.chatapp;

import java.io.PrintWriter;

import android.util.Log;

public class PushClientTest {

    public static void main(String[] args) {
        PushClient client = new PushClient(new PushClient.IOHandler(){

            private PrintWriter printer;

            @Override
            public void displayInput(String input) {
                Log.i(PushClient.logger, input);
            }

            @Override
            public void sendOutput(String output) {
                Log.i(PushClient.logger, "sending output...");
                printer.write(output);
            }

            public void setOutputWriter(PrintWriter writer) {
                printer = writer;
            }

        });
        client.connect();
    }

}
