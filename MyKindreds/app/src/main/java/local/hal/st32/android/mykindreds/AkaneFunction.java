/**
 * Created by Tester on 2016/12/16.
 * 音声入力されたデータによって機能を分岐させるクラス
 *
 */
package local.hal.st32.android.mykindreds;


import android.content.Context;
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
            if(voice.indexOf(content) >= 0){
                break;
            }else{

                count++;
            }
        }
        Log.d("count", ":"+count);

        switch (count){
            /**
             * 天気メソッド
             */
            case 0:
                WeatherFunction weather = new WeatherFunction();
                weather.startWeather();
                break;

            /**
             * 検索
             */
            case 1:
                break;

            /**
             * Todo
             */
            case 2:
                TodoFunction todo = new TodoFunction(context);
                todo.todoStart(voice);
                break;

            /**
             * テスト
             */
            case 3:
                VoiceTestFunction voiceTest = new VoiceTestFunction(context);
                voiceTest.start();
                break;

            /**
             * バルス
             */
            case 4:
                BalsFunction musuka = new BalsFunction();
                musuka.bals();
                break;

            /**
             * 現在地取得
             */
            case 5:
                NowLocationGetFunction location = new NowLocationGetFunction(context);
                break;

            /**
             * メモ
             */
            case 6:
                break;


            default:
                MainActivity.icon.setImageResource(R.drawable.question);
                MainActivity.tts.speak(Voice.voiceNone, TextToSpeech.QUEUE_ADD, null, Voice.voiceNone);
                MainActivity.speakText.setText(voice);
                break;
        }
    }




}
