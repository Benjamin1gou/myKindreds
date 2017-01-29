package local.hal.st32.android.mykindreds;

import android.speech.tts.TextToSpeech;
import android.widget.ListView;

/**
 * Created by Tester on 2017/01/17.
 */

public class BalsFunction {

    public void bals(){
        MainActivity.character.setImageResource(R.drawable.akane_bals);
        MainActivity.speakText.setText(Voice.voiceBals);
        MainActivity.tts.speak(Voice.voiceBals, TextToSpeech.QUEUE_ADD, null, Voice.voiceBals);
    }
}
