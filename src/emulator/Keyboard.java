package emulator;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Arrays;

public class Keyboard {
    private final boolean[] keyStates;
    public boolean isKeyPressed(int keyNo){
        return keyStates[keyNo];
    }

    Keyboard(Pane pane){
        keyStates = new boolean[16];

        ArrayList<KeyCode> keyCodes = generateKeyList();
        addKeyboardListeners(keyCodes, pane);
    }

    private ArrayList<KeyCode> generateKeyList(){
        return new ArrayList<>(Arrays.asList(KeyCode.X, KeyCode.DIGIT1, KeyCode.DIGIT2, KeyCode.DIGIT3, KeyCode.Q, KeyCode.W, KeyCode.E, KeyCode.A, KeyCode.S, KeyCode.D, KeyCode.Z, KeyCode.C, KeyCode.DIGIT4, KeyCode.R, KeyCode.F, KeyCode.V));
    }

    private void addKeyboardListeners(ArrayList<KeyCode> keys, Pane pane){
        pane.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            int keyNo = keys.indexOf(event.getCode());
            if(keyNo != -1){
                keyStates[keyNo] = true;
            }
            event.consume();
        });

        pane.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            int keyNo = keys.indexOf(event.getCode());
            if(keyNo != -1){
                keyStates[keyNo] = false;
            }
            event.consume();
        });
    }
}
