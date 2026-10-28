
package app.hotel21.meetingminutesbook;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MeetingFormActivity extends Activity {

    private DatabaseHelper dbHelper;

    private EditText edtTitle;
    private EditText edtDate;
    private EditText edtDescription;
    private EditText edtDecisions;
    private EditText edtActions;

    private int meetingId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_form);

        dbHelper = new DatabaseHelper(this);

        TextView txtFormTitle = findViewById(R.id.txtFormTitle);

        edtTitle = findViewById(R.id.edtTitle);
        edtDate = findViewById(R.id.edtDate);
        edtDescription = findViewById(R.id.edtDescription);
        edtDecisions = findViewById(R.id.edtDecisions);
        edtActions = findViewById(R.id.edtActions);

        Button btnSave = findViewById(R.id.btnSave);

        meetingId = getIntent().getIntExtra("meeting_id", -1);

        if (meetingId != -1) {
            txtFormTitle.setText("Edit Meeting");
            btnSave.setText("Update Meeting");
            loadMeeting();
        }

        btnSave.setOnClickListener(v -> saveMeeting());
    }

    private void loadMeeting() {

        Cursor cursor = dbHelper.getMeetingById(meetingId);

        try {
            if (cursor.moveToFirst()) {

                edtTitle.setText(cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                DatabaseHelper.COL_TITLE)));

                edtDate.setText(cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                DatabaseHelper.COL_DATE)));

                edtDescription.setText(cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                DatabaseHelper.COL_DESCRIPTION)));

                edtDecisions.setText(cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                DatabaseHelper.COL_DECISIONS)));

                edtActions.setText(cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                DatabaseHelper.COL_ACTIONS)));
            }
        } finally {
            cursor.close();
        }
    }

    private void saveMeeting() {

        String title = edtTitle.getText().toString().trim();
        String date = edtDate.getText().toString().trim();

        String description =
                edtDescription.getText().toString().trim();

        String decisions =
                edtDecisions.getText().toString().trim();

        String actions =
                edtActions.getText().toString().trim();

        if (title.isEmpty()) {
            edtTitle.setError("Please enter a meeting title");
            edtTitle.requestFocus();
            return;
        }

        long result;

        if (meetingId == -1) {

            result = dbHelper.insertMeeting(
                    title, date, description, decisions, actions
            );

        } else {

            result = dbHelper.updateMeeting(
                    meetingId, title, date,
                    description, decisions, actions
            );
        }

        if (result > 0) {

            Toast.makeText(
                    this,
                    meetingId == -1
                            ? "Meeting saved successfully"
                            : "Meeting updated successfully",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

        } else {

            Toast.makeText(
                    this,
                    "Unable to save meeting",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}
