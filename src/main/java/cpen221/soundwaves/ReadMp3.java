package cpen221.soundwaves;

import cpen221.soundwaves.soundutils.AudioFile;

import java.util.Arrays;

public class ReadMp3 {
    String filename;
    /**
     * read the mp3 file and return a double array of the soundtrack
     * @param filename the name of the mp3 file
     * @return a double array of the soundtrack
     */
    public static double[] readmp3(String filename){
        AudioFile af1 = new AudioFile("samples/" + filename);
        double[] soundtrack = new double[0];
        while (!af1.isEmpty()) {
            double[] samples = af1.readNext();
            soundtrack = Arrays.copyOf(soundtrack, soundtrack.length + samples.length);
            for (int i = 0; i < samples.length; i++) {
                soundtrack[soundtrack.length - samples.length + i] = samples[i];
            }
        }
        return soundtrack;
    }
}
