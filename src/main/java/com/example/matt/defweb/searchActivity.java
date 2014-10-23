package com.example.matt.defweb;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;


public class searchActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //adds valid data to listview
        Intent intent = getIntent();

            String query = intent.getStringExtra(SearchManager.QUERY);
            ArrayAdapter<String> arrayAdapter = doSearch(query);
            getListView().setAdapter(arrayAdapter);

       /* ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.test_array));
        */
    }

    private ArrayAdapter<String> doSearch(String query) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        for (String s : getResources().getStringArray(R.array.test_array)) {
            if (s.toLowerCase().contains(query.trim())) {
                arrayAdapter.add(s);
            }
        }
        //arrayAdapter.add("test");
        return arrayAdapter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
