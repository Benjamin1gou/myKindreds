package local.hal.st32.android.mykindreds;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

import static local.hal.st32.android.mykindreds.MainActivity.character;

/**
 * Created by Tester on 2017/01/10.
 */

public class VoiceTestFunction extends AppCompatActivity {

    private static final int REQUEST_CODE = 1000;

    private MainActivity main;

    public VoiceTestFunction(MainActivity main){
        this.main = main;
    }

    public void start() {
        try {
            // 音声認識の　Intent インスタンス 設定の部分
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "テスト開始");

            // インテント発行
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            Log.e("", "No Activity");
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
                character.setImageResource(R.drawable.akane_speak);
                String str = candidates.get(0);
                TextView speakText = (TextView)main.findViewById(R.id.textArea);
                speakText.setText(str);
            }
        }
    }
}
