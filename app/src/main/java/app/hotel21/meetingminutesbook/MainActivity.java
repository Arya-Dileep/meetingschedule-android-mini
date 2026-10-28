
package app.hotel21.meetingminutesbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private DatabaseHelper dbHelper;
    private ListView listMeetings;
    private TextView txtEmpty;
    private ArrayList<Meeting> meetings;
    private ArrayAdapter<Meeting> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        listMeetings = findViewById(R.id.listMeetings);
        txtEmpty = findViewById(R.id.txtEmpty);
        Button btnAdd = findViewById(R.id.btnAddMeeting);

        meetings = new ArrayList<>();

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                meetings
        );

        listMeetings.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    MeetingFormActivity.class
            );
            startActivity(intent);
        });

        listMeetings.setOnItemClickListener(
                (parent, view, position, id) -> {

                    Meeting meeting = meetings.get(position);

                    Intent intent = new Intent(
                            MainActivity.this,
                            MeetingFormActivity.class
                    );

                    intent.putExtra("meeting_id", meeting.id);
                    startActivity(intent);
                });

        listMeetings.setOnItemLongClickListener(
                (parent, view, position, id) -> {

                    Meeting meeting = meetings.get(position);

                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Delete Meeting")
                            .setMessage("Delete \"" + meeting.title + "\"?")
                            .setPositiveButton("Delete", (dialog, which) -> {

                                int result =
                                        dbHelper.deleteMeeting(meeting.id);

                                if (result > 0) {
                                    Toast.makeText(
                                            MainActivity.this,
                                            "Meeting deleted successfully",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }

                                loadMeetings();
                            })
                            .setNegativeButton("Cancel", null)
                            .show();

                    return true;
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMeetings();
    }

    private void loadMeetings() {

        meetings.clear();

        Cursor cursor = dbHelper.getAllMeetings();

        try {
            while (cursor.moveToNext()) {

                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow(
                                DatabaseHelper.COL_ID));

                String title = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                DatabaseHelper.COL_TITLE));

                String date = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                DatabaseHelper.COL_DATE));

                meetings.add(new Meeting(id, title, date));
            }
        } finally {
            cursor.close();
        }

        adapter.notifyDataSetChanged();

        if (meetings.isEmpty()) {
            txtEmpty.setVisibility(View.VISIBLE);
            listMeetings.setVisibility(View.GONE);
        } else {
            txtEmpty.setVisibility(View.GONE);
            listMeetings.setVisibility(View.VISIBLE);
        }
    }

    static class Meeting {

        int id;
        String title;
        String date;

        Meeting(int id, String title, String date) {
            this.id = id;
            this.title = title;
            this.date = date;
        }

        @Override
        public String toString() {
            if (date == null || date.trim().isEmpty()) {
                return title;
            }

            return title + "\nDate: " + date;
        }
    }
}
