package cpen221.soundwaves;

public class SquareWave extends ConcreteSoundWave {

    /**
     * A private constructor
     *
     * @param channel the time series of amplitude values, is not null
     */
    private SquareWave(double[] channel) {
        super(channel, channel);
    }

    /**
     * Obtain a new {@code SquareWave} instance.
     *
     * @param freq      the frequency of the wave, > 0
     * @param phase     the phase of the wave, >= 0
     * @param amplitude the amplitude of the wave, is in (0, 1]
     * @param duration  the duration of the wave, >= 0
     * @return a {@code SquareWave} instance with the specified parameters
     */
    public static SquareWave getInstance(double freq, double phase, double amplitude, double duration) {
        int time = (int) Math.floor(duration * SAMPLES_PER_SECOND);
        double[] channel = new double[time];
        for (int i = 0; i < time; i++) {
            double test = amplitude * Math.sin(2 * Math.PI * freq * (1.0 / SAMPLES_PER_SECOND * i - phase));
            if (test >= 0) {
                channel[i] = 1*amplitude;
            } else {
                channel[i] = -1*amplitude;
            }
        }
        return new SquareWave(channel);
    }

}
