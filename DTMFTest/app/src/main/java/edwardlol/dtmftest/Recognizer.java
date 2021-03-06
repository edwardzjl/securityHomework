package edwardlol.dtmftest;

import java.util.ArrayList;

/**
 * Created by edwardlol on 15/3/4.
 */
public class Recognizer {
    ArrayList<Character> history;
    char actualValue;

    public Recognizer() {
        history = new ArrayList<Character>();
        actualValue = ' ';
    }

    public char getRecognizedKey(char recognizedKey) {
        history.add(recognizedKey);

        if (history.size() <= 4) {
            return ' ';
        }

        history.remove(0);

        int count = 0;

        for (Character c : history) {
            if (c.equals(recognizedKey)) {
                count++;
            }
        }

        if (count >= 3) {
            actualValue = recognizedKey;
        }

        return actualValue;
    }
}
