package wpam.recognizer;

public class Tone {
    private int lowFrequency;
    private int highFrequency;
    private char key;

    public Tone(int lowFrequency, int highFrequency, char key) {
        this.lowFrequency = lowFrequency;
        this.highFrequency = highFrequency;
        this.key = key;
    }

    public char getKey() {
        return key;
    }

    public boolean match(int lowFrequency, int highFrequency) {
        return (matchFrequency(lowFrequency, this.lowFrequency) && matchFrequency(highFrequency, this.highFrequency));
    }

    private boolean matchFrequency(int frequency, int frequencyPattern) {
        int FREQUENCY_DELTA = 2;
        return ((frequency - frequencyPattern) * (frequency - frequencyPattern) < FREQUENCY_DELTA * FREQUENCY_DELTA);
    }

}
