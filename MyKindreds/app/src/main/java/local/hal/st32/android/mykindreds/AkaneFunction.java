/**
 * Created by Tester on 2016/12/16.
 * 音声入力されたデータによって機能を分岐させるクラス
 *
 */
package local.hal.st32.android.mykindreds;



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

                break;
        }
    }




}
