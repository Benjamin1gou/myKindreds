package local.hal.st32.android.mykindreds;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Tester on 2017/01/12.
 * content: DB接続を行うクラス
 */

public class DataAccess {

    public static ArrayList<String> contentsAll(Context context){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ArrayList<String> result = new ArrayList<String>();
        Cursor cursor = null;
        String sql = "SELECT name FROM contents";

        try{
            cursor = db.rawQuery(sql,null);
            Replace re = new Replace();
            re.setRequestId("name");
            result = re.analysisCursor(cursor);

        }catch(Exception ex){
            Log.e("ERROR", ex.toString());
        }finally{
            db.close();
        }
        return result;
    }

}
