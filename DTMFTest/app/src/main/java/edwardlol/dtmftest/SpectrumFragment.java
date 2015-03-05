package edwardlol.dtmftest;

/**
 * Created by edwardlol on 15/3/4.
 */
//频谱段
public class SpectrumFragment {
    private int start;
    private int end;
    private Spectrum spectrum;

    public SpectrumFragment(int start, int end, Spectrum spectrum) {
        this.start = start;
        this.end = end;
        this.spectrum = spectrum;
    }

    public int getMax() {
        int max = 0;
        double maxValue = 0;

        for (int i = start; i <= end; ++i)
            if (maxValue < spectrum.get(i)) {
                maxValue = spectrum.get(i);
                max = i;
            }

        return max;
    }
}
