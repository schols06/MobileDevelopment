package com.example.luc.timetracker;

import android.app.assist.AssistContent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_ID = "extraItemId";

    private ListView listView;
    private DataSource dataSource;

    private SimpleCursorAdapter simpleCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.main_list);
        TextView emptyView = (TextView) findViewById(R.id.list_empty);
        listView.setEmptyView(emptyView);

        dataSource = new DataSource(this);

        //List<Item> items = dataSource.getAllItems();

        String[] columns = new String[]{
                MySQLiteHelper.COLUMN_ITEM_DESCRIPTION,
                MySQLiteHelper.COLUMN_TAG};

        int[] to = new int[]{
                R.id.list_item_1,
                R.id.list_item_2};
        simpleCursorAdapter = new SimpleCursorAdapter(
                this, R.layout.list_item,
                dataSource.getAllItemsCursor(),
                columns,
                to,
                0);

        // itemAdapter = new ArrayAdapter<Item>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(simpleCursorAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra(EXTRA_ITEM_ID,
                        simpleCursorAdapter.getItemId(position));
                startActivity(intent);
            }
        });
        registerForContextMenu(listView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select an action");
        menu.add(0, v.getId(), 0, "Delete item");
    }


    @Override
    public boolean onContextItemSelected(MenuItem menuItem) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)
                menuItem.getMenuInfo();
        if (menuItem.getTitle() == "Delete item") {
            Toast.makeText(getApplicationContext(), "Item deleted",
                    Toast.LENGTH_LONG).show();
            dataSource.deleteItem(simpleCursorAdapter .getItemId(info.position));
            simpleCursorAdapter.changeCursor(dataSource.getAllItemsCursor());

        } else {
            return false;
        }
        return true;
    }


    /*
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo itemInfo = (AdapterView.AdapterContextMenuInfo)
                item.getMenuInfo();
        if (item.getItemId() == R.id.context_menu_delete_item) {
            dataSource.deleteItem(simpleCursorAdapter.getItemId(itemInfo.position));
            simpleCursorAdapter.changeCursor(dataSource.getAllItemsCursor());
        }
        return super.onContextItemSelected(item);
    }
    */


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                simpleCursorAdapter.changeCursor(dataSource.getAllItemsCursor());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.main_menu_add) {

            startActivityForResult(new Intent(this, AddItemActivity.class), 1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
