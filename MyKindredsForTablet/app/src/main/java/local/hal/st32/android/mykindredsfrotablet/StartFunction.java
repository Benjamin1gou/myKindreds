package local.hal.st32.android.mykindredsfrotablet;

import android.app.SearchManager;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

/**
 * アプリが起動または再開された際に動くクラス
 * Created by Tester on 2017/01/31.
 */

public class StartFunction {

    ListView todo;
    ListView memo;
    ListView history;
    ListView group;
    ListView schedule;
    List<Map<String, String>> oneColumnList;
    MainActivity main;


    public StartFunction(MainActivity main){
        this.main = main;
    }


    public  void start(){
        /**
         * todo メモ表示
         */


        /**
         * todo 検索履歴表示
         */
        WebHistoryFunction history = new WebHistoryFunction(main);
        history.historyStart();


        WeatherFunction weather = new WeatherFunction();
        weather.startWeather("START");


        /**
         * todo todo,グループ表示
         */
        todo = (ListView)main.findViewById(R.id.todoList);
        memo = (ListView)main.findViewById(R.id.memoList);
        group =(ListView)main.findViewById(R.id.GroupList);
        schedule = (ListView)main.findViewById(R.id.scheduleList);
        Calendar calendar = new GregorianCalendar();
        String dateTime = "";
        dateTime = calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.DAY_OF_MONTH);
        ServerAccess access = new ServerAccess();
        //todo ここ引数入れる
        access.execute(URL.Todo_URL+"?method=Start&userId="+User.userData+"&date="+dateTime);
//        access.execute(URL.Todo_URL+"?method=Todo&userId="+User.userData+"&type=BROWSE&mission="+" ");

    }

    private class ServerAccess extends Access {
        @Override
        public void onPostExecute(String result){
            Replace re = new Replace();
            re.setRequestId("title");
            oneColumnList = re.json(result, "todo");
            outputTodoList(oneColumnList);
            oneColumnList = re.json(result, "memo");
            outputMemoList(oneColumnList);
            oneColumnList = re.json(result, "schedule");
            outputScheduleList(oneColumnList);
        }
    }

    public void outputTodoList(List<Map<String,String>> listDate) {
        String[] from = {"title"};
        int[] to = {android.R.id.text1};
        SimpleAdapter adapter = new SimpleAdapter(main, listDate, android.R.layout.simple_list_item_1, from, to);
        todo.setAdapter(adapter);
//        todo.setOnItemClickListener(new TodoFunction.ListViewOnClickListener());
    }


    public void outputMemoList(List<Map<String,String>> listDate) {
        String[] from = {"title"};
        int[] to = {android.R.id.text1};
        SimpleAdapter adapter = new SimpleAdapter(main, listDate, android.R.layout.simple_list_item_1, from, to);
        memo.setAdapter(adapter);
//        todo.setOnItemClickListener(new TodoFunction.ListViewOnClickListener());
    }

    public void outputScheduleList(List<Map<String, String>> listDate){
        String[] from = {"title"};
        int[] to = {android.R.id.text1};
        SimpleAdapter adapter = new SimpleAdapter(main, listDate, android.R.layout.simple_list_item_1, from, to);
        schedule.setAdapter(adapter);
    }



}
