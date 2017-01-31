package local.hal.st32.android.mykindredsfrotablet;

import android.app.SearchManager;
import android.content.Intent;

/**
 * web検索用ファンクション
 * Created by Tester on 2017/01/30.
 */

public class WebSearchFunction {
    public static final String METHOD = "web検索";

    private MainActivity main;

    public WebSearchFunction(MainActivity main){
     this.main = main;
    }

    public void startWeb(String voice){
        Replace re = new Replace();
        String searchWord = re.startPull(voice,"検索");
        DataAccess.historySave(main, searchWord);
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, searchWord);
        main.startActivity(intent);
    }




}
