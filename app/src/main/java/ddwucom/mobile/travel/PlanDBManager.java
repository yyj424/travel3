package ddwucom.mobile.travel;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class PlanDBManager {/*
    PlanDBHelper planDBHelper = null;
    Cursor cursor = null;

    public PlanDBManager(Context context) {
        planDBHelper = new PlanDBHelper(context);
    }

    public ArrayList<MyPlan> getAllDiary() {
        ArrayList diaryList = new ArrayList();
        SQLiteDatabase db = planDBHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + planDBHelper.TABLE_NAME, null);

        while(cursor.moveToNext()) {
            long id = cursor.getInt(cursor.getColumnIndex(planDBHelper.COL_ID));
            String title = cursor.getString(cursor.getColumnIndex(planDBHelper.COL_TITLE));
            String date = cursor.getString(cursor.getColumnIndex(planDBHelper.COL_DATE));
            String place = cursor.getString(cursor.getColumnIndex(planDBHelper.COL_PLACE));
            String contents = cursor.getString(cursor.getColumnIndex(planDBHelper.COL_CONTENTS));

            diaryList.add ( new MyPlan (id, title, date, place, contents) );
        }

        cursor.close();
        diaryDBHelper.close();
        return diaryList;
    }

    public boolean addNewDiary(MyDiary newDiary) {
        SQLiteDatabase db = diaryDBHelper.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(diaryDBHelper.COL_IMAGE, newDiary.getImage());
        value.put(diaryDBHelper.COL_TITLE, newDiary.getTitle());
        value.put(diaryDBHelper.COL_DATE, newDiary.getDate());
        value.put(diaryDBHelper.COL_PLACE, newDiary.getPlace());
        value.put(diaryDBHelper.COL_WEATHER, newDiary.getWeather());
        value.put(diaryDBHelper.COL_CONTENTS, newDiary.getContents());

        long count = db.insert(diaryDBHelper.TABLE_NAME, null, value);
        diaryDBHelper.close();
        if (count > 0) return true;
        return false;
    }

    public boolean modifyDiary(MyDiary diary) {
        SQLiteDatabase sqLiteDatabase = diaryDBHelper.getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put(diaryDBHelper.COL_TITLE, diary.getTitle());
        row.put(diaryDBHelper.COL_DATE, diary.getDate());
        row.put(diaryDBHelper.COL_PLACE, diary.getPlace());
        row.put(diaryDBHelper.COL_IMAGE, diary.getImage());
        row.put(diaryDBHelper.COL_WEATHER, diary.getWeather());
        row.put(diaryDBHelper.COL_CONTENTS, diary.getContents());
        String whereClause = diaryDBHelper.COL_ID + "=?";
        String[] whereArgs = new String[] { String.valueOf(diary.get_id()) };
        int result = sqLiteDatabase.update(diaryDBHelper.TABLE_NAME, row, whereClause, whereArgs);
        diaryDBHelper.close();
        if (result > 0) return true;
        return false;
    }

    public boolean removeFood(long id) {
        SQLiteDatabase sqLiteDatabase = diaryDBHelper.getWritableDatabase();
        String whereClause = diaryDBHelper.COL_ID + "=?";
        String[] whereArgs = new String[] { String.valueOf(id) };
        int result = sqLiteDatabase.delete(diaryDBHelper.TABLE_NAME, whereClause,whereArgs);
        diaryDBHelper.close();
        if (result > 0) return true;
        return false;
    }

    public  ArrayList<MyDiary> getDiaryByTitle(String title) {
        ArrayList<MyDiary> diaryList = new ArrayList<MyDiary>();
        SQLiteDatabase db = diaryDBHelper.getWritableDatabase();
        String[] columns = {"image", "title", "date", "place", "weather", "contents"};
        String selection = "title=?";
        String[] selectArgs = new String[]{title};
        Cursor cursor = db.query(diaryDBHelper.TABLE_NAME, columns, selection, selectArgs, null, null, null, null);

        while(cursor.moveToNext()) {
            int image = cursor.getInt(cursor.getColumnIndex(diaryDBHelper.COL_IMAGE));
            String date = cursor.getString(cursor.getColumnIndex(diaryDBHelper.COL_DATE));
            String place = cursor.getString(cursor.getColumnIndex(diaryDBHelper.COL_PLACE));
            String weather = cursor.getString(cursor.getColumnIndex(diaryDBHelper.COL_WEATHER));
            String contents = cursor.getString(cursor.getColumnIndex(diaryDBHelper.COL_CONTENTS));
            diaryList.add ( new MyDiary (image, title, date, place, weather, contents) );
        }
        cursor.close();
        diaryDBHelper.close();
        return diaryList;
    }

    public void close() {
        if (diaryDBHelper != null) diaryDBHelper.close();
        if (cursor != null) cursor.close();
    };*/
}
