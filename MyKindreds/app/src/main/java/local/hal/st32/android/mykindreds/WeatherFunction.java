/**
 * Created by Tester on 2017/01/01.
 * インターネット接続を行い、天気情報を出力するFunction
 */
package local.hal.st32.android.mykindreds;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class WeatherFunction {

    public void startWeather(){
        Weather weather = new Weather();
        //ここ修復まち　引数一つのためくっつけるように変更
        weather.execute(URL.WEATHER_URL, "270000");
    }

    private class Weather extends Access{
        private static final String DEBUG_TAG = "Weather";

        @Override
        public void onPostExecute(String result){
            String title = "";
            String text = "";
            String dateLabel = "";
            String telop = "";
            try{
                JSONObject rootJSON = new JSONObject(result);
                title = rootJSON.getString("title");
                JSONObject descriptionJSON = rootJSON.getJSONObject("description");
                text = descriptionJSON.getString("text");
                JSONArray forecasts = rootJSON.getJSONArray("forecasts");
                JSONObject forecastNow = forecasts.getJSONObject(0);
                dateLabel = forecastNow.getString("dateLabel");
                telop  = forecastNow.getString("telop");
            }catch (JSONException ex){
                Log.e(DEBUG_TAG, "JSON解析失敗", ex);
            }

            String msg = "今日の天気は" + telop + "\n" + text;

            weatherOutPut(telop,msg);

        }

    }

    public void weatherOutPut(String telop, String msg){

    }

    public  void weatherSwitch(String telop){

    }
}
