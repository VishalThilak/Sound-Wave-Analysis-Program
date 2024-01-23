package cpen221.soundwaves;

import cpen221.soundwaves.soundutils.AudioFile;
import cpen221.soundwaves.soundutils.FilterType;

import java.util.Arrays;

public class ConcreteSoundWave implements SoundWave {
    double[] leftChannel;
    double[] rightChannel;

    //put precondition that right and left channel are equal

    /**
     * Create an instance of {@code SoundWave} with specified amplitude values for
     * the left and right channel (assuming stereo).
     *
     * @param leftChannel  an array for the left channel, requires size be same as rightChannel
     * @param rightChannel an array for the right channel, requires size be same as leftChannel
     */
    public ConcreteSoundWave(double[] leftChannel, double[] rightChannel) {
        this.leftChannel = leftChannel;
        this.rightChannel = rightChannel;

    }

    /**
     * @return left channel of sound wave
     */
    @Override
    public double[] getLeftChannel() {
        return leftChannel;
    }

    /**
     * @return right channel of sound wave
     */
    @Override
    public double[] getRightChannel() {
        return rightChannel;
    }

    /**
     * Calculate duration of sound wave
     *
     * @return duration of sound wave
     */
    @Override
    public double duration() {
        int length = rightChannel.length;
        return (double) length / SAMPLES_PER_SECOND;
    }

    /**
     * Adding left and right channel to the end of current sound
     * wave left and right channel
     *
     * @param lchannel an array to add to the end of the current sound wave's
     *                 left channel, requires size be same as rightChannel
     * @param rchannel an array to add to the end of the current sound
     *                 wave's right channel, requires size be same as leftChannel
     */
    @Override
    public void append(double[] lchannel, double[] rchannel) {
        double[] newlChannel = Arrays.copyOf(leftChannel, lchannel.length + rchannel.length + 1);
        double[] newrChannel = Arrays.copyOf(rightChannel, lchannel.length + rchannel.length + 1);
        for (int i = 0; i < lchannel.length; i++) {
            newlChannel[lchannel.length + i + 1] = lchannel[i];
            newrChannel[rchannel.length + i + 1] = rchannel[i];
        }
        leftChannel = newlChannel;
        rightChannel = newrChannel;
    }
//    public static double[] readmp3(String filename){
//        AudioFile af1 = new AudioFile("samples/" + filename);
//        double[] soundtrack = new double[0];
//        while (!af1.isEmpty()) {
//            double[] samples = af1.readNext();
//            soundtrack = Arrays.copyOf(soundtrack, soundtrack.length + samples.length);
//            for (int i = 0; i < samples.length; i++) {
//                soundtrack[soundtrack.length - samples.length + i] = samples[i];
//            }
//        }
//        return soundtrack;
//    }
    /**
     * Adding left and right channel to the end of current sound
     * wave left and right channel
     *
     * @param other the wave to append, requires the other waves left and
     *              right channels to have the same length
     *
     */
    @Override
    public void append(SoundWave other) {
        double[] newlChannel = Arrays.copyOf(leftChannel, other.getLeftChannel().length + leftChannel.length );
        double[] newrChannel = Arrays.copyOf(rightChannel, other.getRightChannel().length + rightChannel.length);
        for (int i = 0; i < other.getLeftChannel().length; i++) {
            newlChannel[leftChannel.length + i ] = other.getLeftChannel()[i];
            newrChannel[rightChannel.length + i] = other.getRightChannel()[i];
        }
        leftChannel = newlChannel;
        rightChannel = newrChannel;
    }

    /**
     * Superimposing two waves together by adding the leftChannels and rightChannels together
     * to create new sound wave with new left and right channels
     *
     * @param other the sound wave to add
     * @return superimposed sound wave
     */
    @Override
    public SoundWave add(SoundWave other) {
        double[] newArrayLeft = new double[Math.max(leftChannel.length, other.getLeftChannel().length)];
        double[] newArrayRight = new double[Math.max(rightChannel.length, other.getRightChannel().length)];
        double max = 1;
        for (int i = 0; i < newArrayLeft.length; i++) {
            if(i > leftChannel.length - 1) {
                newArrayLeft[i] = other.getLeftChannel()[i];
                newArrayRight[i] = other.getRightChannel()[i];
            }
            else if(i > other.getLeftChannel().length - 1) {
                newArrayLeft[i] = leftChannel[i];
                newArrayRight[i] = rightChannel[i];
            }
            else {
                newArrayLeft[i] = leftChannel[i] + other.getLeftChannel()[i];
                newArrayRight[i] = rightChannel[i] + other.getRightChannel()[i];
            }
            if (Math.abs(newArrayLeft[i]) > max) {
                max = Math.abs(newArrayLeft[i]);
            }
        }
        for (int i = 0; i < newArrayLeft.length; i++) {
            newArrayLeft[i] /= max;
            newArrayRight[i] /= max;
        }
        leftChannel = newArrayLeft;
        rightChannel = newArrayRight;
        return new ConcreteSoundWave(newArrayLeft, newArrayRight);
    }
    /**
     * Adds an echo onto sound wave, with a superimposition of the sound wave itself
     * with a phase difference and amplitude reduction.
     * s'(t) = s(t) + s(t - delta) * alpha
     *
     * @param delta > 0. delta, in seconds, is the time lag between this wave and
     *              the echo wave.
     * @param alpha > 0. alpha is the damping factor applied to the echo wave.
     *              modifies:
     */
    @Override
    public SoundWave addEcho(double delta, double alpha) {
        double[] newarrayleft = new double[leftChannel.length];
        double[] newarrayright = new double[rightChannel.length];
        int del = (int) Math.ceil(delta * SAMPLES_PER_SECOND);

        for (int i = 0; i < leftChannel.length; i++) {

            if (delta%(1.0/SAMPLES_PER_SECOND)==0 && i > del) {
                newarrayleft[i] = leftChannel[i] + alpha * leftChannel[i - (int)delta*SAMPLES_PER_SECOND];
                newarrayright[i] = rightChannel[i] + alpha * rightChannel[i - (int)delta*SAMPLES_PER_SECOND];
            } else if (i > del) {
                newarrayleft[i] = leftChannel[i] + alpha / 2 * (leftChannel[i - (int) Math.floor(SAMPLES_PER_SECOND * delta)] + leftChannel[i - (int) Math.ceil(SAMPLES_PER_SECOND * delta)]);
                newarrayright[i] = rightChannel[i] + alpha / 2 * (rightChannel[i - (int) Math.floor(SAMPLES_PER_SECOND * delta)] + rightChannel[i - (int) Math.ceil(SAMPLES_PER_SECOND * delta)]);
            } else {
                newarrayleft[i] = leftChannel[i];
                newarrayright[i] = rightChannel[i];
            }
        }
        leftChannel = newarrayleft;
        rightChannel = newarrayright;
        return new ConcreteSoundWave(newarrayleft, newarrayright); // change this, needs to modify existing one
    }

    /**
     * Scales amplitude of sound wave by scalingFactor
     * modifies: scales leftChannel by scaleFactor
     * when amplitude exceeds -1 or 1, then
     * amplitude takes on the respective peak it surpassed
     *
     * @param scalingFactor is a value > 0.
     */
    @Override
    public void scale(double scalingFactor) {

    }

    /**
     * Given two waves wave 1 and wave 2, determines if wave 2
     * occurs in wave 1, with possible amplitude scaling.
     * There must exist time instants a amd b, where a <= b
     * and beta (amplitude scaling factor) is greater than zero.
     * Also for wave 2 to be completely be contained in wave 1
     * the length of wave 2 = b - a + 1.
     *
     * @param other is the wave to search for in this wave.
     * @return true if wave 2 occurs in wave 1 and false otherwise
     */
    @Override
    public boolean contains(SoundWave other) {
        int a;
        int b;
        if(other.getLeftChannel().length > leftChannel.length){return false;}
        for(int i = 0; i < leftChannel.length; i++){
            a = i;
            b = i - 1;
            double beta = leftChannel[i] / other.getLeftChannel()[0];
            for(int j = 0; j < other.getLeftChannel().length; j++){
                if(beta * other.getLeftChannel()[j] == leftChannel[a]){
                    b++;
                } else{
                    break;
                }
            }
            if(other.getLeftChannel().length == b - a + 1){return true;}
        }
        return false;
    }
    /**
     * Compute the Γ (similarity) between two waves.
     * Uses getBeta function to find β (beta) values to use
     * and determines the optimal amplitude scaling factor
     * that should be used across all channels which maximizes the
     * similarities between the two SoundWaves.
     *
     * @param other sound wave
     * @return the Γ (similarity) between the two waves
     */
    @Override
    public double similarity(SoundWave other) {

        double[] other_left = other.getLeftChannel();
        double[] other_right = other.getRightChannel();
        int length = other_left.length;
        double beta_a = getBeta(length, other_left, leftChannel);
        double beta_b = getBeta(length, other_right, rightChannel);
        double sum_1=0;
        double sum_2=0;
        double sum_3=0;
        double sum_4=0;

        for(int i= 0; i<length; i++){
            sum_1 += Math.pow(leftChannel[i]-beta_a*other_left[i],2);
            sum_2 += Math.pow(other_left[i]-leftChannel[i]/beta_a,2);
            sum_3 += Math.pow(rightChannel[i]-beta_b*other_right[i],2);
            sum_4 += Math.pow(other_right[i]-rightChannel[i]/beta_b,2);
        }

        double sim_1 = (1.0/(1+sum_1)+1.0/(1+sum_2))/2.0;
        double sim_2 = (1.0/(1+sum_3)+1.0/(1+sum_4))/2.0;
        if(sim_1> sim_2){return sim_1;}
        return  sim_2;
    }

    /**
     * Find β(beta) value used as an amplitude scaling factor
     * that should be used across all channels.
     *
     * @param length of sound channel
     * @param other other waves sound channel
     * @param original original waves sound channel
     * @return β value
     */
    private double getBeta(int length, double[] other, double[] original) {
        double max=0;
        double other_max = 0;
        double beta;
        for(int i = 0; i< length; i++){
            if(Math.abs(other[i])>other_max){other_max=Math.abs(other[i]);}
            if(Math.abs(original[i])>max){max=Math.abs(original[i]);}
        }
        beta = max/other_max;
        return beta;
    }



    /**
     * Calculate the frequency with the highest amplitude of the sound wave
     * by finding the maximum value from the frequency domain
     *
     * @return the highest amplitude frequency of the sound wave
     */
    @Override
    public double highestAmplitudeFrequencyComponent() {
        Fourier_transform DFT1 = new Fourier_transform(leftChannel);
    return DFT1.highest_freq();
    }
    /**
     * Filtering out frequencies from the sound wave with given filter type and frequencies thresholds
     * @param type the type of filter to apply
     * @param frequencies the frequency thresholds that all frequencies between it will be zeroed-out
     * @return the filtered sound wave
     */
    @Override
    public SoundWave filter(FilterType type, Double... frequencies) {
//        // TODO: Implement this method correctly.
//         The provided code illustrates -- rather simply -- two ideas:
//         (1) the use of varargs, and
//         (2) one use of enums.
        Fourier_transform DFT1 = new Fourier_transform(leftChannel);
        if (frequencies.length > 2) {
            System.out.println("Error");
        } else {
            System.out.println(frequencies[0]);
        }

        switch (type) {
            case LOWPASS:
                DFT1.Lowfilter(frequencies);
            case BANDPASS:
                DFT1.Bandfilter(frequencies);
            case HIGHPASS:
                DFT1.Highfilter(frequencies);
            default:
        }
        double[] soundtrack = DFT1.IFT();
        return new ConcreteSoundWave(soundtrack,soundtrack);

    }
}
