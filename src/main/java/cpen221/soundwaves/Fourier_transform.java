package cpen221.soundwaves;

public class Fourier_transform {

    public double[] imag_values;
    public double[] real_values;
    private int N;
    /**
     * create an instance of a sound wave and compute the fourier transform
     * modifies: change the imag_values and real_values arrays to the fourier transform of the sound wave
     * @param soundtrack the double array representing the sound wave
     */
    public Fourier_transform(double[] soundtrack) {
        this.N = soundtrack.length;
        double[] imag_values = new double[N];
        double[] real_values = new double[N];

        for (int freq = 0; freq < N; freq += 1) {
            double y_distance = 0;
            double x_distance = 0;
            for (int i = 0; i < N; i+= 1) {
                double angle = -2 * Math.PI * freq * i / N;
                y_distance += soundtrack[i] * Math.sin(angle);
                x_distance += soundtrack[i] * Math.cos(angle);
            }

            imag_values [freq] =  y_distance;
            real_values [freq] =  x_distance;
        }

        this.imag_values = imag_values;
        this.real_values = real_values;
    }

    /**
     * Compute the inverse fourier transform of the sound wave
     *
     * @return the double array representing the inversely transferred sound wave
     */
    public double[] IFT() {
        double[] original_soundtrack = new double[N];
        double max = 0;
        for (int i = 0; i < N; i++){
            double sum = 0;
            for (int freq = 0; freq < N; freq += 1) {
                double angle = 2 * Math.PI * freq * i / N;
                sum += real_values[freq] * Math.cos(angle) - imag_values[freq] * Math.sin(angle);
            }
            original_soundtrack[i] = sum / N;

        }



        return original_soundtrack;
    }
    /**
     * Calculate the highest amplitude frequency of the sound wave by finding the maximum value from the frequency domain
     *
     * @return the highest amplitude frequency of the sound wave
     */
    public double highest_freq(){

        double max = 0;
        double freq = 0;
        for (int i = 0; i < 4 * imag_values.length / 5; i++) {
            if (Math.abs(imag_values[i]) > max) {
                max = Math.abs(imag_values[i]);
                freq = i;
            }
        }

        return  freq / ((double) N /SoundWave.SAMPLES_PER_SECOND);
    }
    /**
     * A filter that zero-out all frequencies below the given frequency
     * modifies: the imag_values and real_values arrays
     * @param freq the frequency threshold that all frequencies below it will be zeroed-out
     */
    public void Lowfilter (Double[] freq){
        for (int i = 0; i <= (int) Math.floor(freq[0]); i++) {
            imag_values[i] = 0;
            real_values[i] = 0;
        }
    }
    /**
     * A filter that zero-out all frequencies above the given frequency
     * modifies: the imag_values and real_values arrays
     * @param freq the frequency threshold that all frequencies below it will be zeroed-out
     */
    public void Highfilter (Double[] freq){
        for (int i = imag_values.length-1; i > (int) Math.floor(freq[0]); i--) {
            imag_values[i] = 0;
            real_values[i] = 0;
        }
    }
    /**
     * A filter that zero-out all frequencies between the given frequencies
     * modifies: the imag_values and real_values arrays
     * @param freq the frequency thresholds that all frequencies between it will be zeroed-out
     */
    public void Bandfilter (Double[] freq){
        for (int i = (int) Math.floor(freq[0]); i <= (int) Math.floor(freq[1]); i++) {
            imag_values[i] = 0;
            real_values[i] = 0;
        }
    }

}