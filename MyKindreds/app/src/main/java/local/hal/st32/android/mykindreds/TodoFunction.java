package local.hal.st32.android.mykindreds;

import android.app.SearchManager;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
    public static final String _ADD = "追加";
    public static final String _DEL = "削除";
    public String METHOD_NAME = BROWSE;
    ListView _list;
    List<Map<String, String>> list;

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
        if(0 <= voice.lastIndexOf(_ADD)){
            METHOD_NAME = INSERT;
            todoInsert(voice);
        }else if(0 <= voice.lastIndexOf(_DEL)){
            todoDelete(voice);
            METHOD_NAME = DELETE;
        }else{
            todoBrowse();
        }
    }

    public void todoInsert(String voice){
        String mission = pullMission(voice, _ADD);

        Log.e("mission", mission);
        serverAccess serv = new serverAccess();
        Log.e("ipaddress",URL.Todo_URL+"?method="+METHOD+"&userId="+User.userData+"&type="+INSERT+"&mission="+mission);
        serv.execute(URL.Todo_URL+"?method="+METHOD+"&userId="+User.userData+"&type="+INSERT+"&mission="+mission);
    }


    public void todoDelete(String voice){
        String mission = pullMission(voice, _DEL);
        serverAccess serve = new serverAccess();
        Log.d("ipaddress",URL.Todo_URL+"?method="+METHOD+"&userId="+User.userData+"&type="+DELETE+"&mission="+mission);
        serve.execute(URL.Todo_URL+"?method="+METHOD+"&userId="+User.userData+"&type="+DELETE+"&mission="+mission);
    }

    public void todoBrowse(){
        serverAccess serve = new serverAccess();
        Log.d("ipaddress",URL.Todo_URL+"?method="+METHOD+"&userId="+User.userData+"&type="+BROWSE+"&mission="+" ");
        serve.execute(URL.Todo_URL+"?method="+METHOD+"&userId="+User.userData+"&type="+BROWSE+"&mission="+" ");
    }


    /**
     * 音声データから登録するデータだけを抜き出すメソッド
     * @param voice
     * @param order
     * @return
     */
    public String pullMission(String voice, String order){
        int startNum = 2;
        int endNum = voice.indexOf(order);
        Log.d("", ""+startNum);
        Log.d("", ""+endNum);
        String mission = voice.substring(startNum,endNum);
        Log.d("mission:", mission);
        return mission;
    }

    private class serverAccess extends Access{

        @Override
        public void onPostExecute(String result){
            Replace re = new Replace();
            String message;
            switch(METHOD_NAME){
                case INSERT:
                    re.setRequestId("title");
                    message = re.json(result,"data").get(0).get("title");
                    outputMessage(message);
                    break;
                case DELETE:
                    re.setRequestId("title");
                    message = re.json(result,"data").get(0).get("title");
                    outputMessage(message);
                    break;
                case BROWSE:
                    re.setRequestId("title");
                    list = re.json(result, "data");
                    outputList(list);
                    break;
            }
            MainActivity.tts.speak(Voice.voiceTodo, TextToSpeech.QUEUE_ADD, null, Voice.voiceTodo);
        }

    }

    public void outputList(List<Map<String,String>> listDate){
        String[] from = {"title"};
        int[] to = {android.R.id.text1};
        SimpleAdapter adapter = new SimpleAdapter(main, listDate, android.R.layout.simple_list_item_1, from, to);
        _list.setAdapter(adapter);
        _list.setOnItemClickListener(new ListViewOnClickListener());
    }

    public void outputMessage(String message){
        MainActivity.speakText.setText(message);
        MainActivity.tts.speak(message, TextToSpeech.QUEUE_ADD, null, message);
    }

    private class ListViewOnClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Map<String, String> intentData = list.get(position);
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, intentData.get("title"));
            main.startActivity(intent);
        }
    }


}
