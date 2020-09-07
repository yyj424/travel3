package ddwucom.mobile.travel;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class PlanDBHelper extends SQLiteOpenHelper {
    final static String DB_NAME = "plan.db";
    public final static String TABLE_NAME = "plan_table";
    public final static String COL_ID = "_id";
    public final static String COL_TITLE = "title";
    public final static String COL_DATE = "date";
    public final static String COL_PLACE = "place";
    public final static String COL_CONTENTS = "contents";

    public PlanDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
