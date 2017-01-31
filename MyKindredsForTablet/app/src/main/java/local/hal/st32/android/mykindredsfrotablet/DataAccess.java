package local.hal.st32.android.mykindredsfrotablet;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
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
            Log.e("contentsAll", ex.toString());
        }finally{
            db.close();
        }
        return result;
    }

    /**
     * web検索履歴保存メソッド
     * @param context
     * @param word
     */
    public static void historySave(Context context, String word){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        String sql = "INSERT INTO searched(word) VALUES(?);";
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.bindString(1, word);
        db.beginTransaction();
        try{
            stmt.executeInsert();
            db.setTransactionSuccessful();
        }catch (Exception ex){
            Log.e("historySave", ex.toString());
        }finally {
            db.endTransaction();
            db.close();
        }
    }

    /**
     * todo 履歴取得メソッド作成する
     */
    public static ArrayList<String> historyGet(Context context){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ArrayList<String> result = new ArrayList<String>();
        Cursor cursor = null;
        String sql = "SELECT word FROM searched";

        try{
            cursor = db.rawQuery(sql,null);
            Replace re = new Replace();
            re.setRequestId("word");
            result = re.analysisCursor(cursor);

        }catch(Exception ex){
            Log.e("historyGet", ex.toString());
        }finally{
            db.close();
        }
        return result;
    }


}
