package local.hal.st32.android.mykindredsfrotablet;

import android.app.SearchManager;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by Tester on 2017/01/23.
 */

public class MemoFunction {

    private static final String METHOD = "Memo";
    public static final String INSERT = "INSERT";
    public static final String _ADD = "追加";
    public static final String BROWSE = "BROWSE";
    public String METHOD_NAME = BROWSE;

    private MainActivity main;
    ListView _list;
    List<Map<String, String>> list;

    public MemoFunction(MainActivity main){
        this.main = main;
        _list = (ListView)main.findViewById(R.id.todoList);
    }

    public void memoStart (String voice){
        if(0 <= voice.lastIndexOf(_ADD)){
            METHOD_NAME = INSERT;
            memoInsert(voice);
        }else{
            memoBrowse();
        }
    }

    /**
     * メモを追加する場合に動くメソッド
     * @param voice
     */
    public void memoInsert(String voice){
        String mission = Replace.pullMission(voice, _ADD);
        Log.e("mission", mission);
        serverAccess serv = new serverAccess();
        Log.e("ipaddress",URL.Todo_URL+"?method="+METHOD+"&userId="+User.userData+"&type="+INSERT+"&mission="+mission);
        serv.execute(URL.Todo_URL+"?method="+METHOD+"&userId="+User.userData+"&type="+INSERT+"&mission="+mission);
    }

    /**
     * メモを閲覧する際に動くメソッド
     */
    public void memoBrowse(){
        serverAccess serve = new serverAccess();
        Log.d("ipaddress",URL.Todo_URL+"?method="+METHOD+"&userId="+User.userData+"&type="+BROWSE+"&mission="+" ");
        serve.execute(URL.Todo_URL+"?method="+METHOD+"&userId="+User.userData+"&type="+BROWSE+"&mission="+" ");
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
