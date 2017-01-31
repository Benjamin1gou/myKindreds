package local.hal.st32.android.mykindreds;

import java.util.ArrayList;

/**
 * web履歴表示用クラス
 * Created by Tester on 2017/01/30.
 */

public class WebHistoryFunction {

    public static final String METHOD = "WebHistoryFunction";

    private MainActivity main;
    private ArrayList<String> result;

    public WebHistoryFunction(MainActivity main){
        this.main = main;
    }

    public void historyStart(){
        result = DataAccess.historyGet(main);

    }
}
