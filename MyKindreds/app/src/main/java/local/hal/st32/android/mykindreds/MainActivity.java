package local.hal.st32.android.mykindreds;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

import static local.hal.st32.android.mykindreds.Voice.*;


public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    private TextToSpeech tts;
    private ImageView character;
    private TextView speakText;
    private static final int REQUEST_CODE = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tts = new TextToSpeech(this,this);
    }

    @Override
    protected void onDestroy(){
        tts.speak(voiceBay, TextToSpeech.QUEUE_ADD, null, voiceBay);
        while(tts.isSpeaking()){

        }

        if(null != tts){
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            character = (ImageView)findViewById(R.id.character);
            character.setImageResource(R.drawable.akane_smail);
            tts.speak(voiceHello, TextToSpeech.QUEUE_FLUSH, null, voiceHello);
        } else {
            System.out.println("Oops!");
        }
    }

    private void speech(){
        // 音声認識が使えるか確認する
        try {
            // 音声認識の　Intent インスタンス
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 100);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "喋ってや");
            // インテント発行
            startActivityForResult(intent, REQUEST_CODE);
        }
        catch (ActivityNotFoundException e) {
            speakText = (TextView)findViewById(R.id.textArea);
            speakText.setText("No Activity " );
        }
    }

    /**
     * キーコードを取得するメソッド
     * @param event
     * @return キーコード
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        // DOWNとUPが取得できるのでログの2重表示防止のためif
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            //キーコード取得
            int keyCode = event.getKeyCode();
            if(79 == keyCode){
                speech();
            }
        }

        return super.dispatchKeyEvent(event);
    }
}
