package kr.saintdev.safetypin.models.datas.dbm;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by 5252b on 2018-06-10
 */

public class DBHelper extends SQLiteOpenHelper {
    private SQLiteDatabase readDB = null;
    private SQLiteDatabase writeDB = null;

    public DBHelper(Context context) {
        super(context, "project_safetypin", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLQuerys.PARENT_CHILDS);
        db.execSQL(SQLQuerys.CHILDS_TEACHER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void open() {
        // read 와 write 할 수 있는 db 객체를 가져옵니다.
        this.readDB = getReadableDatabase();
        this.writeDB = getWritableDatabase();
    }

    public Cursor sendReadableQuery(String query) {
        return this.readDB.rawQuery(query, null);
    }

    public void sendWriteableQuery(String query) {
        this.writeDB.execSQL(query);
    }

    public SQLiteDatabase getReadDB() {
        return readDB;
    }

    public SQLiteDatabase getWriteDB() {
        return writeDB;
    }

    public interface SQLQuerys {
        String PARENT_CHILDS = "CREATE TABLE `tb_parent_childs` (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "child_name TEXT NOT NULL," +
                "child_num INTEGER NOT NULL," +
                "child_code INTEGER NOT NULL" +
                ");";
        String CHILDS_TEACHER = "CREATE TABLE `tb_child_teacher` (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "teacher_class TEXT NOT NULL," +
                "teacher_info TEXT NOT NULL," +
                "teacher_name TEXT NOT NULL," +
                "teacher_email TEXT NOT NULL" +
                ");";
    }
}
