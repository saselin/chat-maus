package de.fhb.fbi.msr.maus.uebung1.chatapp;

import android.os.Bundle;

import java.io.PrintWriter;

public class MainActivity extends AbstractMessageSender{

    private  PushClient.IOHandler ioHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ioHandler = new PushClient.IOHandler() {

            private  PrintWriter mPrintWriter;

            @Override
            public void displayInput(String input) {
                updateConversation(input);
            }

            @Override
            public void sendOutput(String output) {
                mPrintWriter.println(output);
            }

            @Override
            public void setOutputWriter(PrintWriter writer) {
                mPrintWriter = writer;
            }
        };
        new PushClient(ioHandler).connect();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.conversation);
    }

    @Override
    protected void processMessage(String message) {
        ioHandler.sendOutput(message);
        super.updateConversation(message);
    }
}