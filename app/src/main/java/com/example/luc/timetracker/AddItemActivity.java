package com.example.luc.timetracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

public class AddItemActivity extends AppCompatActivity {

    private EditText itemEditText;
    private DataSource dataSource;

    private Spinner tagSpinner;
    private ImageButton addTagBtn;

    private SimpleCursorAdapter simpleCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        itemEditText = (EditText) findViewById(R.id.add_item);
        dataSource = new DataSource(this);

        tagSpinner = (Spinner) findViewById(R.id.add_item_tag_spinner);
        addTagBtn = (ImageButton) findViewById(R.id.add_item_add_tag_button);
        tagSpinner.setEmptyView(findViewById(R.id.add_item_tag_spinner_empty));

        String[] columns = new String[]
                {
                        MySQLiteHelper.COLUMN_TAG
                };
        int[] to = new int[]
                {
                        android.R.id.text1
                };
        simpleCursorAdapter = new SimpleCursorAdapter(
                this, android.R.layout.simple_spinner_dropdown_item,
                dataSource.getAllTagsCursor(),
                columns,
                to,
                0);
        tagSpinner.setAdapter(simpleCursorAdapter);


        addTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new
                        AlertDialog.Builder(AddItemActivity.this);
                builder.setTitle("Add tag");
                builder.setMessage("Add your tag here");
                final EditText editText = new EditText(AddItemActivity.this);
                builder.setView(editText);
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dataSource.createTag(editText.getText().toString());
                        simpleCursorAdapter.changeCursor(dataSource.getAllTagsCursor());
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_item_menu_save) {
            long itemId = dataSource.createItem(itemEditText.getText().toString(),
                    simpleCursorAdapter.getItemId(tagSpinner.getSelectedItemPosition()));

            Intent result = new Intent();
            result.putExtra(MainActivity.EXTRA_ITEM_ID, itemId);
            setResult(Activity.RESULT_OK, result);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
