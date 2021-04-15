package de.fhb.fbi.msr.maus.uebung1.chatapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public abstract class AbstractMessageSender extends Activity {

    /**
     * logging
     */
    protected static final String logger = "AbstractMessageSender";

    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView();

        // Das Eingabefeld für eine neue Nachricht
        EditText newMessage = (EditText) findViewById(R.id.new_message);
        Log.i(logger,"Eingabefeld: " + newMessage);

        newMessage.setImeOptions(EditorInfo.IME_ACTION_DONE);

        // Falls eine neue Nachricht eingegeben wird und mittels "done" beendet
        // wird, wird die Activity Callee aufgerufen

        newMessage.setOnEditorActionListener(new OnEditorActionListener() {

            public boolean onEditorAction(TextView view, int arg1, KeyEvent arg2) {
                Log.d(logger, "got editor action " + arg1 + " on textview "
                        + view + ". KeyEvent is: " + arg2);
                if (arg1 == EditorInfo.IME_ACTION_DONE) {
                    Log.d(logger, "done was pressed!");

                    // Zugriff auf den eingegebenen Text
                    String message = view.getText().toString();

                    // aufruf des Callee
                    processMessage(message);

                    view.setText("");

                    return false;
                }
                return false;
            }

        });

    }

    /**
     * Unterklassen können die Ansicht auswählen. Diese muss lediglich zumindest
     * die beiden hier mittels findViewById() verwendeten Elemente bereitstellen
     */
    protected abstract void setContentView();

    /**
     * diese Methode aktualisiert die Ansicht des Nachrichtenaustauschs, d.h. neue
     * Nachrichten werden in neuer Zeile angehängt
     */
    protected void updateConversation(String message) {
        // erneuter Zugriff auf die conversation Ansicht via layout
        TextView conversation = (TextView) findViewById(R.id.conversation_transcript);
        conversation.append("\n" + message);
    }

    /**
     * Bsp. für Pattern "Abstract Method"
     *
     * Übernimmt Verarbeitung einer Nachricht, die im Eingabefeld der App eingetippt wurde
     *
     * @param message
     */
    protected abstract void processMessage(String message);

}
