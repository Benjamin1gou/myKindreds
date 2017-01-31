/**
 * Created by Tester on 2017/01/01.
 * インターネット接続を行い、天気情報を出力するFunction
 */
package local.hal.st32.android.mykindredsfrotablet;


import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.text.TextUtils.TruncateAt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class WeatherFunction {

    private ImageView weatherIcon;

    public void startWeather(){
        Weather weather = new Weather();
        weatherIcon = MainActivity.weatherIcon;
        //ここ修復まち　引数一つのためくっつけるように変更
        weather.execute(URL.WEATHER_URL+"?city=270000");
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

            String msg = Voice.voiceWeather+"今日の天気は" + telop + "\n" + text;
            weatherOutPut(telop,msg);
        }

    }

    /**
     * 取得した天気情報を表示するメソッド
     * @param telop
     * @param msg
     */
    public void weatherOutPut(String telop, String msg){
//        MainActivity.character.setImageResource(R.drawable.akane_speak);
        String speak = msg;
        speak = weatherSwitch(telop, speak);

        MainActivity.speakText.setFocusableInTouchMode(true);
        MainActivity.speakText.setEllipsize(TruncateAt.MARQUEE);
        MainActivity.speakText.setText(speak);
//        MainActivity.tts.speak(speak, TextToSpeech.QUEUE_ADD, null, speak);
    }

    /**
     * 天気によって出力する天気のアイコンを分岐させるメソッド
     * @param telop
     */
    public String weatherSwitch(String telop, String speak){
        String msg = speak;
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
            case "曇のち晴":
                weatherIcon.setImageResource(R.drawable.tenki_sun);
                break;
            case "雨時々曇":
                weatherIcon.setImageResource(R.drawable.tenki_kumo);
                break;
            case "暴風雨":
                weatherIcon.setImageResource(R.drawable.tenki_bohu);
                break;
            case "晴時々曇":
                weatherIcon.setImageResource(R.drawable.tenki_kumo);
                break;
            case "曇時々雪":
                weatherIcon.setImageResource(R.drawable.tenki_yuki);
                break;
            default:
                weatherIcon.setImageResource(R.drawable.question);
                msg = "知らないパターンです。新しく登録お願いしますマスター\n" + telop;
                break;
        }
        return msg;
    }
}
