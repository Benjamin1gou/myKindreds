package local.hal.st32.android.mykindreds;

import android.database.Cursor;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tester on 2016/11/10.
 *
 */

public class Replace {

    private ArrayList<String> requestId = new ArrayList<String>();

    private ArrayList<String> responseId = new ArrayList<String>();

    static String tableName;

    private static final String DEBUG_TAG = "replaseJson";

    public void setRequestId(String date) {
        requestId.add(date);
    }

    public void setResponseId(String date) {
        responseId.add(date);
    }

    public void setTableName(String name) {
        tableName = name;
    }

    public List<Map<String, String>> json(String result,String referenceValue) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        try {

            Map<String, String> map = new HashMap<String, String>();
            JSONObject rootJSON = new JSONObject(result);
            JSONArray arrayJson = rootJSON.getJSONArray(referenceValue);
            for (int i = 0; i < arrayJson.length(); i++) {
                JSONObject data = arrayJson.getJSONObject(i);
                map = new HashMap<String, String>();

                for (int x = 0; x < requestId.size(); x++) {
                    map.put(requestId.get(x), data.getString(requestId.get(x)));
                    Log.d("map", requestId.get(x) + ":" + data.getString(requestId.get(x)));
                }
                list.add(map);
            }
        } catch (JSONException ex) {
            Log.e(DEBUG_TAG, "JSON解析失敗", ex);
        }
        return list;
    }

    @Deprecated
    public Map<String, String> jsonOne(String result) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            JSONObject rootJSON = new JSONObject(result);
            JSONArray arrayJson = rootJSON.getJSONArray("date");
            JSONObject data = arrayJson.getJSONObject(0);
            for (int x = 0; x < requestId.size(); x++) {
                map.put(requestId.get(x), data.getString(requestId.get(x)));
            }

        } catch (JSONException ex) {
            Log.d(DEBUG_TAG, "JSON解析失敗", ex);
        }
        return map;
    }

    /**
     * １行分のJSONデータを解析するメソッド
     * @param result
     * @return １行分の解析されたデータ
     */
    public Map<String, String> oneColmunJson(String result){
        Map<String, String> map = new HashMap<String, String>();
        try{
            JSONObject rootJSON = new JSONObject(result);
            for (int x = 0; x < requestId.size(); x++) {
                map.put(requestId.get(x), rootJSON.getString(requestId.get(x)));
                Log.d("map", requestId.get(x) + ":" + rootJSON.getString(requestId.get(x)));
            }

        }catch (JSONException ex) {
            Log.e(DEBUG_TAG, "JSON解析失敗", ex);
        }

        return map;
    }

    /**
     * Cursor解析メソッド
     * @param cursor
     * @return
     */
    public ArrayList<String> analysisCursor(Cursor cursor){
        ArrayList<String> result = new ArrayList<String>();
        boolean flg = cursor.moveToFirst();
        while(flg) {
            for(int x = 0 ; x<requestId.size(); x++){
                result.add(cursor.getString(cursor.getColumnIndex(requestId.get(x))));
            }
            flg = cursor.moveToNext();
        }
        return result;
    }

    public ArrayList<Map<String,String>> analysisCursorList(Cursor cursor){
        ArrayList<Map<String,String>> result = new ArrayList<Map<String,String>>();
        boolean flg = cursor.moveToFirst();
        Map<String, String> map = new HashMap<String, String>();
        while(flg) {
            map = new HashMap<String, String>();
            for(int x = 0 ; x<requestId.size(); x++){
                map.put(requestId.get(x),cursor.getString(cursor.getColumnIndex(requestId.get(x))));
            }
            result.add(map);
            flg = cursor.moveToNext();
        }
        return result;
    }

    /**
     * 文字列抜き出しメソッド（両サイド）
     * @param voice
     * @param order
     * @return
     */
    public static String pullMission(String voice,int startValue, String order){
        int startNum = startValue;
        int endNum = voice.indexOf(order);
        Log.d("", ""+startNum);
        Log.d("", ""+endNum);
        String mission = voice.substring(startNum,endNum);
        Log.d("mission:", mission);
        return mission;
    }

    /**
     * 文字抜き出しメソッド（先頭）
     * @param voice
     * @param header
     * @return
     */
    public String startPull(String voice, String header){
        int startNum = header.length();
        String mission = voice.substring(startNum);
        return mission;
    }

    /**
     * 文字抜き出しメソッド（末尾）
     * @param voice
     * @param ender
     * @return
     */
    public String endPull(String voice, String ender){
        int endNum = ender.length();
        String mission = voice.substring(0,endNum);
        return mission;
    }
}
