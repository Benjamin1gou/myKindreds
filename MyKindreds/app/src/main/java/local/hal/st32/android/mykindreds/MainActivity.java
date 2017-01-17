package local.hal.st32.android.mykindreds;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static local.hal.st32.android.mykindreds.Voice.*;


public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    public static TextToSpeech tts;
    public static ImageView character;
    public static TextView speakText;
    public static ImageView icon;
    private static final int REQUEST_CODE = 1000;
    private AkaneFunction functions = new AkaneFunction(MainActivity.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        speakText = (TextView)findViewById(R.id.textArea);
        icon = (ImageView)findViewById(R.id.imageView2);
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
            // 音声認識の　Intent インスタンス 設定の部分
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "喋ってや");

            // インテント発行
            startActivityForResult(intent, REQUEST_CODE);
        }
        catch (ActivityNotFoundException e) {
            Log.e("","No Activity");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            // 認識結果を ArrayList で取得
            ArrayList<String> candidates = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            if(candidates.size() > 0) {
                // 認識結果候補で一番有力なものを表示
                String str = candidates.get(0);
                functions.methodSwitch(str);
            }
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
            if(126 == keyCode || 127 == keyCode || 79 == keyCode){
                speech();
            }
        }

        return super.dispatchKeyEvent(event);
    }

    public void voiceOn(View view){
        speech();
    }

    public void testOn(View view){
        functions.methodSwitch("天気");
    }
}
