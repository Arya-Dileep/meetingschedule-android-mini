
package app.hotel21.meetingminutesbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MeetingMinutes.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "meetings";
    public static final String COL_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_DATE = "meeting_date";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_DECISIONS = "decisions";
    public static final String COL_ACTIONS = "action_items";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " TEXT NOT NULL, " +
                COL_DATE + " TEXT, " +
                COL_DESCRIPTION + " TEXT, " +
                COL_DECISIONS + " TEXT, " +
                COL_ACTIONS + " TEXT)";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Insert a new meeting
    public long insertMeeting(String title, String date,
                              String description, String decisions,
                              String actions) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_TITLE, title);
        values.put(COL_DATE, date);
        values.put(COL_DESCRIPTION, description);
        values.put(COL_DECISIONS, decisions);
        values.put(COL_ACTIONS, actions);

        return db.insert(TABLE_NAME, null, values);
    }

    // Retrieve all meetings
    public Cursor getAllMeetings() {
        SQLiteDatabase db = getReadableDatabase();

        return db.query(TABLE_NAME, null, null, null,
                null, null, COL_ID + " DESC");
    }

    // Retrieve one meeting by ID
    public Cursor getMeetingById(int id) {
        SQLiteDatabase db = getReadableDatabase();

        return db.query(TABLE_NAME, null,
                COL_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null);
    }

    // Update an existing meeting
    public int updateMeeting(int id, String title, String date,
                             String description, String decisions,
                             String actions) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_TITLE, title);
        values.put(COL_DATE, date);
        values.put(COL_DESCRIPTION, description);
        values.put(COL_DECISIONS, decisions);
        values.put(COL_ACTIONS, actions);

        return db.update(TABLE_NAME, values,
                COL_ID + "=?",
                new String[]{String.valueOf(id)});
    }

    // Delete a meeting
    public int deleteMeeting(int id) {
        SQLiteDatabase db = getWritableDatabase();

        return db.delete(TABLE_NAME, COL_ID + "=?",
                new String[]{String.valueOf(id)});
    }
}
