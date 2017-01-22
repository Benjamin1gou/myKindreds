package local.hal.st32.android.mykindreds;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by Tester on 2017/01/17.
 * content:Todo機能の大元クラス
 */

public class TodoFunction {

    public static final String METHOD = "Todo";
    public static final String INSERT = "INSERT";
    public static final String DELETE = "DELETE";
    public static final String BROWSE = "BROWSE";
    public String METHOD_NAME = BROWSE;
    ListView _list;

    MainActivity main;

    public TodoFunction(MainActivity main){
        this.main = main;
        _list = (ListView)main.findViewById(R.id.todoList);
    }

    /**
     * todoファンクションを始める最初のメソッド
     * 音声情報から追加か削除選別し、DBと接続する
     * @param voice
     */
    public void todoStart(String voice){
        if(0 <= voice.lastIndexOf(INSERT)){
            METHOD_NAME = INSERT;
            todoInsert(voice);
        }else if(0 <= voice.lastIndexOf(DELETE)){
            todoDelete(voice);
            METHOD_NAME = DELETE;
        }else{
            todoBrowse();
        }
    }

    public void todoInsert(String voice){
        String mission = pullMission(voice, INSERT);
        serverAccess serv = new serverAccess();
        serv.execute(URL.Todo_URL+"?method="+METHOD+"&userId="+User.userData+"&type="+INSERT+"&mission="+mission);

    }

    public void todoDelete(String voice){
        String mission = pullMission(voice, DELETE);
        serverAccess serve = new serverAccess();
        serve.execute(URL.Todo_URL+"?method="+METHOD+"&userId="+User.userData+"&type="+DELETE+"&mission="+mission);
    }

    public void todoBrowse(){
        serverAccess serve = new serverAccess();
        Log.d("ipaddress",URL.Todo_URL+"?method="+METHOD+"&userId="+User.userData+"&type="+BROWSE);
        serve.execute(URL.Todo_URL+"?method="+METHOD+"&userId="+User.userData+"&type="+BROWSE);
    }


    /**
     * 音声データから登録するデータだけを抜き出すメソッド
     * @param voice
     * @param order
     * @return
     */
    public String pullMission(String voice, String order){
        int startNum = 2;
        int endNum = voice.indexOf(order)-1;
        String mission = voice.substring(startNum,endNum);
        Log.d("mission:", mission);
        return mission;
    }

    private class serverAccess extends Access{

        @Override
        public void onPostExecute(String result){
            Replace re = new Replace();
            List<Map<String, String>> list;
            String message;
            switch(METHOD_NAME){
                case INSERT:
                    re.setRequestId("message");
                    message = re.json(result,"data").get(0).get("message");
                    outputMessage(message);
                    break;
                case DELETE:
                    re.setRequestId("message");
                    message = re.json(result,"data").get(0).get("message");
                    outputMessage(message);
                    break;
                case BROWSE:
                    re.setRequestId("title");
                    list = re.json(result, "data");
                    outputList(list);
                    break;
            }
        }

    }

    public void outputList(List<Map<String,String>> listDate){
        String[] from = {"title"};
        int[] to = {android.R.id.text1};
        SimpleAdapter adapter = new SimpleAdapter(main, listDate, android.R.layout.simple_list_item_1, from, to);
        _list.setAdapter(adapter);
    }

    public void outputMessage(String message){
        MainActivity.speakText.setText(message);
        MainActivity.tts.speak(message, TextToSpeech.QUEUE_ADD, null, message);
    }


}
