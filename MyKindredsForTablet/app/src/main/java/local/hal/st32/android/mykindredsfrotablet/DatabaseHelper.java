package local.hal.st32.android.mykindredsfrotablet;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;

/**
 * Created by Tester on 2017/01/12.
 * content:初期インストール用DBクラス
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    /**
     * データベースファイル名の定数フィールド
     */
    private static final String DATABASE_NAME = "akane.db";

    /**
     * バージョン情報の定数フィールド
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * コンストラクタ
     */
    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE contents (");
        sb.append("_id INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb.append("name TEXT NOT NULL");
        sb.append(");");

        String sql = sb.toString();

        db.execSQL(sql);

        sb = new StringBuffer();
        sb.append("CREATE TABLE searched(");
        sb.append("_id INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb.append("word TEXT NOT NULL");
        sb.append(");");
        sql = sb.toString();
        db.execSQL(sql);

        ArrayList<String> content = new ArrayList<String>();
        content.add("INSERT INTO contents(name) VALUES('天気');");
        content.add("INSERT INTO contents(name) VALUES('検索');");
        content.add("INSERT INTO contents(name) VALUES('直近');");
        content.add("INSERT INTO contents(name) VALUES('テスト');");
        content.add("INSERT INTO contents(name) VALUES('バルス');");
        content.add("INSERT INTO contents(name) VALUES('現在地');");
        content.add("INSERT INTO contents(name) VALUES('メモ');");
        content.add("INSERT INTO contents(name) VALUES('履歴');");

        SQLiteStatement stmt;
        for (String date: content){
            stmt = db.compileStatement(date);
            stmt.executeInsert();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}
