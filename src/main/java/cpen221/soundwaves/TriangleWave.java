package cpen221.soundwaves;

public class TriangleWave extends ConcreteSoundWave {
    /**
     * A private constructor
     *
     * @param channel the time series of amplitude values, is not null
     */
    private TriangleWave(double[] channel) {
        super(channel, channel);
    }

    /**
     * Obtain a new {@code TriangleWave} instance.
     *
     * @param freq      the frequency of the wave, > 0
     * @param phase     the phase of the wave, >= 0
     * @param amplitude the amplitude of the wave, is in (0, 1]
     * @param duration  the duration of the wave, >= 0
     * @return a {@code TriangleWave} instance with the specified parameters
     */
    public static TriangleWave getInstance(double freq, double phase, double amplitude,
                                           double duration) {
        int time = (int) Math.floor(duration * SAMPLES_PER_SECOND);
        double interval = duration / time; //interval
        double[] channel = new double[time];
        for (int i = 0; i < time; i++) {
            channel[i] = 2/Math.PI * amplitude * Math.asin(Math.sin(2 * Math.PI * freq * (interval * i - phase)));
        }
        return new TriangleWave(channel);

    }

}
