package cpen221.soundwaves;

import ca.ubc.ece.cpen221.mp1.utils.MP3Player;
import cpen221.soundwaves.soundutils.Audio;
import cpen221.soundwaves.soundutils.AudioFile;
import cpen221.soundwaves.soundutils.SoundWaveChart;

import java.util.Arrays;

/**
 * <p>Simple <code>Main</code> class to demonstrate how
 * to play and visualize sound waves.</p>
 *
 * <p>
 *     There are four examples:
 *     <ul>
 *         <li>{@link #examplePlayMP3file()};</li>
 *         <li>{@link #examplePlayWAVfile()};</li>
 *         <li>{@link #examplePlayAndDraw()};</li>
 *         <li>{@link #exampleSineWaveWithChart()}.</li>
 *     </ul>
 * </p>
 */
public class Main {

    public static void main(String[] args) {
     //examplePlayMP3file();
   //   examplePlayWAVfile();
        //exampleSineWaveWithChart();
        examplePlayAndDraw();
    }

    private static void examplePlayMP3file() {
        AudioFile af1 = new AudioFile("samples/force.mp3");

        while (!af1.isEmpty()) {
            double[] samples = af1.readNext();
            MP3Player.playWave(samples, samples);
        }
    }

    private static void examplePlayWAVfile() {
        AudioFile af1 = new AudioFile("samples/SnareDrum.wav");

        while (!af1.isEmpty()) {
            double[] samples = af1.readNext();
            Audio.play(samples);
        }
    }

    private static void examplePlayAndDraw() {
        AudioFile af1 = new AudioFile("samples/pearlharbor.mp3");
        SoundWaveChart chart = new SoundWaveChart();
        while (!af1.isEmpty()) {
            double[] samples = af1.readNext();
            chart.updateDrawing(samples);
            Audio.play(samples);
        }
    }

    // To try this method, one should implement SinusoidalWave first
    private static void exampleSineWaveWithChart() {
//        ConcreteSoundWave sw = SinusoidalWave.getInstance(100, 0, 2, 0.1);
        //SoundWaveChart chart = new SoundWaveChart();
//        sw.add(SinusoidalWave.getInstance(200, 0, 2, 0.1));
//        sw.add(TriangleWave.getInstance(300, 0, 2, 0.1));
//        double[] allSamples = sw.getLeftChannel();
//        double[] Fourier = new Furiour_transform(allSamples).ydis_per_freq;
//        Furiour_transform DFT1 = new Furiour_transform(allSamples);
//
//        double[] Fourier1 = DFT1.IFT();
//

        // Let us play & update the chart on a per-second basis

       // AudioFile af1 = new AudioFile("samples/SnareDrum.wav");

         //   double[] samples = af1.readNext();
        //Furiour_transform DFT1 = new Furiour_transform(samples);
       // DFT1.Highfilter(new Double[]{3000.0});
       // double[] Fourier1 = DFT1.IFT();
        //    chart.updateDrawing(Fourier1);
        //    Audio.play(Fourier1);

        ConcreteSoundWave sw_1 = SinusoidalWave.getInstance(4, 0, 1, 1);
        ConcreteSoundWave sw = SinusoidalWave.getInstance(4, 0, 1, 1);

        SoundWaveChart chart = new SoundWaveChart();
        double[] allSamples = sw.getLeftChannel();

        // Let us play & update the chart on a per-second basis
        int duration = allSamples.length / (SoundWave.SAMPLES_PER_SECOND);
        for (int t = 0; t < duration; t++) {
            double[] samples = Arrays.copyOfRange(allSamples, t * SoundWave.SAMPLES_PER_SECOND, (t + 1) * SoundWave.SAMPLES_PER_SECOND);
            chart.updateDrawing(samples);
            Audio.play(samples);
        }




    }

}
