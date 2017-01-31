package local.hal.st32.android.mykindredsfrotablet;

/**
 * アプリが起動または再開された際に動くクラス
 * Created by Tester on 2017/01/31.
 */

public class StartFunction {

    public static void start(MainActivity main){
        /**
         * todo メモ表示
         */


        /**
         * todo 検索履歴表示
         */


        /**
         * todo 天気表示
         */
        WeatherFunction weather = new WeatherFunction();
        weather.startWeather();


        /**
         * todo todo,グループ表示
         */
    }



}
