package local.hal.st32.android.mykindreds;

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
    private static final int DATABASE_VERSION = 12;

    /**
     * コンストラクタ
     */
    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    static final String CONTENTS_SQL = "CREATE TABLE contents ( _id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL);";
    static final String HISTORY_SQL = "CREATE TABLE search_history (_id INTEGER PRIMARY KEY AUTOINCREMENT,word TEXT NOT NULL);";

    @Override
    public void onCreate(SQLiteDatabase db){

        db.execSQL(CONTENTS_SQL);
        db.execSQL(HISTORY_SQL);

        ArrayList<String> content = new ArrayList<String>();
        content.add("INSERT INTO contents(name) VALUES('天気');");
        content.add("INSERT INTO contents(name) VALUES('検索');");
        content.add("INSERT INTO contents(name) VALUES('直近');");
        content.add("INSERT INTO contents(name) VALUES('テスト');");
        content.add("INSERT INTO contents(name) VALUES('バルス');");
        content.add("INSERT INTO contents(name) VALUES('現在地');");
        content.add("INSERT INTO contents(name) VALUES('メモ');");
        content.add("INSERT INTO contents(name) VALUES('履歴');");
        content.add("INSERT INTO contents(name) VALUES('グループ');");
        content.add("INSERT INTO contents(name) VALUES('予定');");

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
