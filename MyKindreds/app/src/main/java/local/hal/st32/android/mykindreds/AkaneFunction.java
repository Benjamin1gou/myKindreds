/**
 * Created by Tester on 2016/12/16.
 * 音声入力されたデータによって機能を分岐させるクラス
 *
 */
package local.hal.st32.android.mykindreds;


import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.ArrayList;

public class AkaneFunction {

    public MainActivity context;

    public AkaneFunction(MainActivity context){
        this.context = context;
    }



    /**
     * 音声で機能を分岐させるメソッド
     * @param voice
     */
    public void methodSwitch(String voice){

        ArrayList<String> contents = new ArrayList<String>();
        contents = DataAccess.contentsAll(context);
        int count = 0;

        for (String content: contents ){
            if(content.indexOf(voice) >= 0){
                break;
            }else{
                count++;
            }
        }
        Log.d("count", ":"+count);

        switch (voice){
            case "天気":
                WeatherFunction weather = new WeatherFunction();
                weather.startWeather();
                break;

            case "テスト":
                VoiceTestFunction voiceTest = new VoiceTestFunction(context);
                voiceTest.start();
                break;

            default:
                MainActivity.tts.speak(Voice.voiceNone, TextToSpeech.QUEUE_ADD, null, Voice.voiceNone);
                break;
        }
    }




}
