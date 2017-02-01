package local.hal.st32.android.mykindredsfrotablet;

import android.app.SearchManager;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * web履歴表示用クラス
 * Created by Tester on 2017/01/30.
 */

public class WebHistoryFunction {

    public static final String METHOD = "WebHistoryFunction";
    ListView _list;

    private MainActivity main;
    private ArrayList<Map<String,String>> result;

    public WebHistoryFunction(MainActivity main){
        this.main = main;
    }

    public void historyStart(){
        _list = (ListView)main.findViewById(R.id.historyList);
        result = DataAccess.historyGet(main);
        outputList(result);

    }

    public void outputList(List<Map<String,String>> listDate){
        String[] from = {"word"};
        int[] to = {android.R.id.text1};
        SimpleAdapter adapter = new SimpleAdapter(main, listDate, android.R.layout.simple_list_item_1, from, to);
        _list.setAdapter(adapter);
        _list.setOnItemClickListener(new WebHistoryFunction.ListViewOnClickListener());
    }

    private class ListViewOnClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Map<String, String> intentData = result.get(position);
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, intentData.get("word"));
            main.startActivity(intent);
        }
    }
}
