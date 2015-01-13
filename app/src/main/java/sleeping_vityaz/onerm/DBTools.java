package sleeping_vityaz.onerm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sleeping_vityaz.onerm.records_util.RecordObject;

public class DBTools extends SQLiteOpenHelper {

    private static DBTools sInstance;

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database name
    private static final String DATABASE_NAME = "workout_log";

    // Table Names
    private static final String TABLE = "history_table";

    // Common column names
    private static final String KEY_ID = "col_id";
    private static final String WEIGHT = "col_weight_lifted";
    private static final String REPS = "col_reps";
    private static final String ONERM = "col_onerm";
    private static final String DATE_CREATED = "col_date_created";


    public DBTools(Context applicationContext) {
        super(applicationContext, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DBTools getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DBTools(context.getApplicationContext());
        }
        return sInstance;
    }


    @Override
    public void onCreate(SQLiteDatabase database) {

        database.execSQL(createTable(TABLE));

    }

    /* This will alter existing table without erasing any of the user's data
    *  Loops through each database version and  necessary columns*/
    @Override
    public void onUpgrade(final SQLiteDatabase database, final int oldVersion, final int newVersion) {

        int upgradeTo = oldVersion + 1;
        while (upgradeTo <= newVersion) {
            /*switch (upgradeTo)
            {
                case 2:
                    database.execSQL("ALTER TABLE " + DEADLIFT + " ADD COLUMN " + NEW_COLUMN_NAME + TYPE);
                    database.execSQL("ALTER TABLE " + BENCH + " ADD COLUMN " + NEW_COLUMN_NAME + TYPE);
                    database.execSQL("ALTER TABLE " + SQUAT + " ADD COLUMN " + NEW_COLUMN_NAME + TYPE);
                    database.execSQL("ALTER TABLE " + PRESS + " ADD COLUMN " + NEW_COLUMN_NAME + TYPE);
                    break;
            }*/
            upgradeTo++;
        }
    }

    /*
     * MAIN_E table: KEY_ID|WEEK|CYCLE|MAIN_EXERCISE|WEIGHT|DATE_CREATED|NOTES
     * ASSISTANCE table: KEY_ID|WEEK|CYCLE|MAIN_EXERCISE|A_EXERCISE|WEIGHT|DATE_CREATED
     */
    // Table Create Statement
    public String createTable(String TABLE) {

        String create = "CREATE TABLE "
                    + TABLE + " ( " + KEY_ID + " INTEGER PRIMARY KEY, " + WEIGHT
                    + " REAL, " + REPS + " INTEGER, " + ONERM + " REAL, " + DATE_CREATED + " DATETIME" + " )";

        return create;
    }

    /*
     * MAIN_E table: KEY_ID|WEEK|CYCLE|MAIN_EXERCISE|WEIGHT|DATE_CREATED|NOTES
     * ASSISTANCE table: KEY_ID|WEEK|CYCLE|MAIN_EXERCISE|A_EXERCISE|WEIGHT|DATE_CREATED
     */
    public void insertRecord(HashMap<String, String> queryValues) {

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(WEIGHT, queryValues.get(WEIGHT));
        values.put(REPS, queryValues.get(REPS));
        values.put(ONERM, queryValues.get(ONERM));
        values.put(DATE_CREATED, queryValues.get(DATE_CREATED));

        database.insert(TABLE, null, values);

        database.close();

    }

    public void deleteRecord(String id) {

        SQLiteDatabase database = this.getWritableDatabase();

        String deleteQuery = "DELETE FROM " + TABLE + " WHERE " + KEY_ID + " = '" + id + "'";

        database.execSQL(deleteQuery);

    }

    void deleteAllData(String TABLE) {
        SQLiteDatabase sdb = this.getWritableDatabase();
        sdb.delete(TABLE, null, null);
    }


    // this is NOT needed since getExerciseInfo takes care of this function.
    // getExerciseInfo can output 1 exercise info as well as more than 1 exercise info
    public List<RecordObject> getAllRecords() {

        List<RecordObject> recordArrayList = new ArrayList<RecordObject>();
        SQLiteDatabase database = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE + " ORDER BY " + DATE_CREATED + " DESC, " + ONERM + " DESC";

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                RecordObject recordObject = new RecordObject();

                recordObject.setRecordsID(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                recordObject.setWeight(cursor.getDouble(cursor.getColumnIndex(WEIGHT)));
                recordObject.setReps(cursor.getInt(cursor.getColumnIndex(REPS)));
                recordObject.setOneRM(cursor.getDouble(cursor.getColumnIndex(ONERM)));
                recordObject.setDate(cursor.getString(cursor.getColumnIndex(DATE_CREATED)));

                recordArrayList.add(recordObject);

            } while (cursor.moveToNext());
        }

        return recordArrayList;

    }

}
