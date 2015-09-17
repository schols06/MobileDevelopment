package com.example.luc.timetracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    private DataSource dataSource;
    private TextView textView;
    private Item item;

    private TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        dataSource = new DataSource(this);
        long assignmentId = getIntent().getLongExtra(MainActivity.EXTRA_ITEM_ID, -1);
        item = dataSource.getItem(assignmentId);
        textView = (TextView) findViewById(R.id.details_textview);
        textView.setText(item.getDescription());

        description = (TextView)findViewById(R.id.details_title);
        description.setText(item.getTag().toString());
    }
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */
}
