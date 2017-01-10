/**
 * Created by Tester on 2017/01/01.
 * インターネット接続を行い、天気情報を出力するFunction
 */
package local.hal.st32.android.mykindreds;


import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static local.hal.st32.android.mykindreds.MainActivity.tts;


public class WeatherFunction {

    private ImageView weatherIcon;

    public void startWeather(){
        Weather weather = new Weather();
        weatherIcon = MainActivity.icon;
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

    /**
     * 取得した天気情報を取得するメソッド
     * @param telop
     * @param msg
     */
    public void weatherOutPut(String telop, String msg){
        MainActivity.character.setImageResource(R.drawable.akane_speak);
        msg = weatherSwitch(telop);
        tts.speak(msg, TextToSpeech.QUEUE_ADD, null, msg);
    }

    /**
     * 天気によって出力する天気のアイコンを分岐させるメソッド
     * @param telop
     */
    public String weatherSwitch(String telop){
        String msg = "";
        switch (telop){
            case "晴れ":
                weatherIcon.setImageResource(R.drawable.tenki_sun);
                break;
            case "曇り":
                weatherIcon.setImageResource(R.drawable.tenki_kumo);
                break;
            case "雨":
                weatherIcon.setImageResource(R.drawable.tenki_ame);
                break;
            case "雪":
                weatherIcon.setImageResource(R.drawable.tenki_yuki);
                break;
            case "晴のち曇":
                weatherIcon.setImageResource(R.drawable.tenki_kumo);
                break;
            case "晴のち雨":
                weatherIcon.setImageResource(R.drawable.tenki_ame);
                break;
            case "曇時々晴":
                weatherIcon.setImageResource(R.drawable.tenki_sun);
                break;
            case "曇時々雨":
                weatherIcon.setImageResource(R.drawable.tenki_ame);
                break;
            case "曇のち雨":
                weatherIcon.setImageResource(R.drawable.tenki_ame);
                break;
            case "雨時々曇":
                weatherIcon.setImageResource(R.drawable.tenki_kumo);
                break;
            case "暴風雨":
                weatherIcon.setImageResource(R.drawable.tenki_bohu);
                break;
            default:
                weatherIcon.setImageResource(R.drawable.question);
                msg = "知らないパターンです。新しく登録お願いしますマスター" + telop;
                break;
        }
        return msg;
    }
}
