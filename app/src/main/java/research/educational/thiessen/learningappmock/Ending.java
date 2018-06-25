package research.educational.thiessen.learningappmock;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;

import java.io.IOException;

public class Ending extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ending);

        MediaPlayer mediaPlayer  = MediaPlayer.create(this, R.raw.vielendank);
        mediaPlayer.start();

        try {
            Start.outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
