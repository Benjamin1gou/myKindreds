package local.hal.st32.android.mykindreds;

import android.util.Log;
import android.widget.ListView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

/**
 * 予定管理用ファンクション
 * Created by Tester on 2017/02/08.
 */

public class ScheduleFunction {

    private static final String METHOD = "Schedule";
    private static final String FUNCTION_NAME = "予定";
    private MainActivity main;
    ListView _list;
    List<Map<String, String>> list;

    private static final String TODAY = "今日";
    private static final String TOMORROW = "明日";
    private static final String YESTERDAY = "昨日";
    private static final String AFTERTOMORROW = "明後日";

    private static final String _add = "追加";
    private static final String _browse = "閲覧";

    private static final String ADD = "ADD";
    private static final String BROWSE = "BROWSE";

    private String METHOD_NAME = "BROWSE";
    private String DATENAME = "";
    private int DATEINT = 0;




    public ScheduleFunction(MainActivity main){
        this.main = main;
        _list = (ListView)main.findViewById(R.id.todoList);
    }

    public void startSchedule(String voice){
        if(0 <= voice.lastIndexOf(_add)){
            ScheduleInsert(voice);
        }else{
            ScheduleBrowse(voice);
        }
    }


    public void ScheduleInsert(String voice){
        String date = metamorDate(voice);
        String schedule = Replace.pullMission(voice, DATEINT+3, _add);
        METHOD_NAME = ADD;
        ServerAccess serv = new ServerAccess();
        serv.execute(URL.Todo_URL+"?method="+METHOD+"&userId="+User.userData+"&type="+ADD+"&date="+date+"&schedule="+schedule);
    }

    public void ScheduleBrowse(String voice){
        String date = metamorDate(voice);
    }




    /**
     * 日本語を日付に変更するメソッド
     * @param voice
     * @return 音声情報頭の文字列を日付に変えたもの 例:2016/10/20とか
     */
    private String metamorDate(String voice){
        Calendar calendar = new GregorianCalendar();
        String dateTime = "";
        if(0 <= voice.indexOf(TODAY)){
            DATENAME = TODAY;
            dateTime = calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.DAY_OF_MONTH);
            Log.d("今日",dateTime);
            DATEINT = 2;
        }else if(0 <= voice.indexOf(TOMORROW)){
            DATENAME = TOMORROW;
            dateTime = calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH)+2) + "/" + calendar.get(Calendar.DAY_OF_MONTH);
            Log.d("明日",dateTime);
            DATEINT = 2;
        }else if(0 <= voice.indexOf(YESTERDAY)){
            DATENAME = YESTERDAY;
            dateTime = calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH)-1) + "/" + calendar.get(Calendar.DAY_OF_MONTH);
            Log.d("明後日",dateTime);
            DATEINT = 3;
        }else if(0 <= voice.indexOf(AFTERTOMORROW)){
            DATENAME = AFTERTOMORROW;
            dateTime = calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH)+3) + "/" + calendar.get(Calendar.DAY_OF_MONTH);
            Log.d("昨日",dateTime);
            DATEINT = 2;
        }
        return dateTime;
    }

    private class ServerAccess extends Access{
        @Override
        public void onPostExecute(String result){
            Replace re = new Replace();
            String message;
            switch(METHOD_NAME){
                case ADD:
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








}
