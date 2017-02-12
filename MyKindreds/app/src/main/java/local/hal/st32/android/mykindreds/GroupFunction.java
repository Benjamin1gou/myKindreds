package local.hal.st32.android.mykindreds;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

/**
 *グループでのタスク管理を行う機能
 * Created by Tester on 2017/02/02.
 */

public class GroupFunction {

    private static final String METHOD = "Group";

    private static final String GSEARCH = "GSEARCH";
    private static final String GADD = "GADD";
    private static final String GLIST = "GLIST";

    private static final String _search = "探す";
    private static final String _add = "追加";
    private String METHOD_NAME = "GLIST";

    private MainActivity main;
    ListView _list;
    List<Map<String, String>> list;


    public GroupFunction (MainActivity main){
        this.main = main;
        _list = (ListView)main.findViewById(R.id.todoList);
    }

    public void groupStart(String voice){
        if(0 <= voice.lastIndexOf(_add)){
            METHOD_NAME = GADD;
            groupInsert(voice);
        }else if(0 <= voice.lastIndexOf(_search)){
            groupSearch(voice);
            METHOD_NAME = GSEARCH;
        }else{
            groupList();
        }
    }

    /**
     * 新しくグループを作る際に始動
     * @param voice　登録グループ名
     */
    private void groupInsert (String voice){
        String groupName = Replace.pullMission(voice, 4,_add);
        Log.e("groupName", groupName);
        ServerAccess serv = new ServerAccess();
        Log.e("URL",URL.Todo_URL+"?method="+METHOD+"&userId="+User.userData+"&type="+GADD+"&groupName="+groupName);
        serv.execute(URL.Todo_URL+"?method="+METHOD+"&userId="+User.userData+"&type="+GADD+"&groupName="+groupName);
    }

    /**
     * 現在存在しているグループを検索する際に使用
     * @param voice　検索グループ名
     */
    private void groupSearch (String voice){
        String groupName = Replace.pullMission(voice, 4,_search);
        Log.e("groupName", groupName);
        ServerAccess serv = new ServerAccess();
        Log.e("URL",URL.Todo_URL+"?method="+METHOD+"&userId="+User.userData+"&type="+GSEARCH+"&groupName="+groupName);
        serv.execute(URL.Todo_URL+"?method="+METHOD+"&userId="+User.userData+"&type="+GSEARCH+"&groupName="+groupName);
    }

    /**
     * 所属しているグループを表示する際に始動
     */
    private void groupList (){
        ServerAccess serve = new ServerAccess();
        Log.d("URL",URL.Todo_URL+"?method="+METHOD+"&userId="+User.userData+"&type="+GLIST+"&groupName="+" ");
        serve.execute(URL.Todo_URL+"?method="+METHOD+"&userId="+User.userData+"&type="+GLIST+"&groupName="+" ");
    }


    private class ServerAccess extends Access{
        @Override
        public void onPostExecute(String result){
            Replace re = new Replace();
            String message;
            switch(METHOD_NAME){
                case GADD:
                    re.setRequestId("groupName");
                    message = re.json(result,"data").get(0).get("groupName");
                    outputMessage(message);
                    break;
                case GSEARCH:
                    re.setRequestId("groupName");
                    list = re.json(result, "data");
                    outputList(list);
                    break;
                case GLIST:
                    re.setRequestId("groupName");
                    list = re.json(result, "data");
                    outputList(list);
                    break;
            }
        }
    }

    public void outputList(List<Map<String,String>> listDate){
        String[] from = {"groupName"};
        int[] to = {android.R.id.text1};
        SimpleAdapter adapter = new SimpleAdapter(main, listDate, android.R.layout.simple_list_item_1, from, to);
        _list.setAdapter(adapter);
        //　todo　OnClickListenerはメソッド名により分岐する
        switch (METHOD_NAME){
            case GADD:

                break;

            case GLIST:

                break;
        }
//        _list.setOnItemClickListener(new ListViewOnClickListener());
    }

    public void outputMessage(String message){
        MainActivity.speakText.setText(message);
        MainActivity.tts.speak(message, TextToSpeech.QUEUE_ADD, null, message);
    }


}
