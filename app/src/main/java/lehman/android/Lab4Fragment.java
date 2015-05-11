package lehman.android;

import android.app.Fragment;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.util.Log;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class Lab4Fragment extends Fragment {

	private final String TAG = getClass().getSimpleName();
	private final boolean D = Log.isLoggable(TAG, Log.DEBUG);

    public class TodoActivity extends ListActivity {
        private ListAdapter taskAdapter;
        private TaskDbHelper taskHelper;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_tasklist);
            updateTodoList();
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.lab4fragment, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.add:
                    AlertDialog.Builder todoTaskBuilder = new AlertDialog.Builder(this);
                    todoTaskBuilder.setTitle("Add Todo Task Item");
                    todoTaskBuilder.setMessage("describe the Todo task...");
                    final EditText todoET = new EditText(this);
                    todoTaskBuilder.setView(todoET);
                    todoTaskBuilder.setPositiveButton("Add Task", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String todoTaskInput = todoET.getText().toString();
                            taskHelper = new TaskDbHelper(TodoActivity.this);
                            SQLiteDatabase sqLiteDatabase = taskHelper.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            values.clear();

                            //write the Todo task input into database table
                            values.put(TaskDbHelper.COL1_TASK, todoTaskInput);
                            sqLiteDatabase.insertWithOnConflict(TaskDbHelper.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);

                            //update the Todo task list UI
                            updateTodoList();
                        }
                    });

                    todoTaskBuilder.setNegativeButton("Cancel", null);

                    todoTaskBuilder.create().show();
                    return true;

                default:
                    return false;
            }
        }

        //update the todo task list UI
        private void updateTodoList() {
            taskHelper = new TaskDbHelper(TodoActivity.this);
            SQLiteDatabase sqLiteDatabase = taskHelper.getReadableDatabase();

            //cursor to read todo task list from database
            Cursor cursor = sqLiteDatabase.query(taskHelper.TABLE_NAME,
                    new String[]{taskHelper._ID, taskHelper.COL1_TASK},
                    null, null, null, null, null);

            //binds the todo task list with the UI
            taskAdapter = new SimpleCursorAdapter(
                    this,
                    R.layout.task,
                    cursor,
                    new String[]{taskHelper.COL1_TASK},
                    new int[]{R.id.taskTextView},
                    0
            );

            this.setListAdapter(taskAdapter);
        }

        //closing the todo task item
        public void onDoneButtonClick(View view) {
            View v = (View) view.getParent();
            TextView todoTV = (TextView) v.findViewById(R.id.taskTextView);
            String todoTaskItem = todoTV.getText().toString();

            String deleteTodoItemSql = "DELETE FROM " + taskHelper.TABLE_NAME +
                    " WHERE " + taskHelper.COL1_TASK + " = '" + todoTaskItem + "'";

            taskHelper = new TaskDbHelper(TodoActivity.this);
            SQLiteDatabase sqlDB = taskHelper.getWritableDatabase();
            sqlDB.execSQL(deleteTodoItemSql);
            updateTodoList();
        }
    }
}
