package com.example.matt.defweb;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

public class Web extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new WebFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.web, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class WebFragment extends Fragment {

        public WebFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_web, container, false);
            RelativeLayout main = (RelativeLayout)rootView.findViewById(R.id.RelativeLayoutMain);

            ListView listview = (ListView)rootView.findViewById(R.id.main_listview);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
            arrayAdapter.addAll(getResources().getStringArray(R.array.test_array));
            listview.setAdapter(arrayAdapter);

            //manages the search view - sends data to search activity test
            SearchView searchView = (SearchView)rootView.findViewById(R.id.searchView);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override //submits search via button now does nothing
                public boolean onQueryTextSubmit(String query) {
                    SearchView searchView1 = (SearchView)getActivity().findViewById(R.id.searchView);

                    return true;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    return false;
                }//interesting
            });



            return rootView;
        }
    }

    @Override
    public boolean onSearchRequested() {

        return true;
    }

}

/*  SPAWNS NEW SEARCH ACTIVITY WITH LIST VIEW
                    SearchView s = (SearchView)getActivity().findViewById(R.id.searchView);
                    Intent searchActivity = new Intent(getActivity(), searchActivity.class);
                    Toast.makeText(getActivity(), "TEST", Toast.LENGTH_SHORT).show();
                    searchActivity.putExtra(SearchManager.QUERY, s.getQuery().toString().toLowerCase());
                    startActivity(searchActivity);*/

/* PROGRAMATICALLY CREATES BUTTON
            //creates view button an d moves it to bottom
            NodeButton b = new NodeButton(getActivity());
            RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            relativeParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            b.setLayoutParams(relativeParams);
            //b.setBackgroundColor(getResources().getColor(android.R.color.holo_purple)); //this resets the background resource
            b.setBackgroundResource(R.drawable.oval_shape);
            b.setText("TEST");
            //searches on click
            b.setOnClickListener(new View.OnClickListener() {
                @Override //alt way of launching search activity
                public void onClick(View view) {
                    SearchView s = (SearchView)getActivity().findViewById(R.id.searchView);
                    Intent searchActivity = new Intent(getActivity(), searchActivity.class);

                    searchActivity.putExtra(SearchManager.QUERY, s.getQuery().toString().toLowerCase());
                    startActivity(searchActivity);
                }
            });
            //adds button to view
            main.addView(b, relativeParams);
*/

/** THIS WORKS
 *       public View onCreateView(LayoutInflater inflater, ViewGroup container,
 *               Bundle savedInstanceState) {
 *           View rootView = inflater.inflate(R.layout.fragment_web, container, false);
 *           RelativeLayout main = (RelativeLayout)rootView.findViewById(R.id.RelativeLayoutMain);
 *
 *           NodeButton b = new NodeButton(getActivity());
 *           b.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
 *                                                                   RelativeLayout.LayoutParams.WRAP_CONTENT));
 *           b.setText("Test Text");
 *           b.setNodeData("Here is some node Data");
 *           b.setOnClickListener(new View.OnClickListener() {
 *               @Override
 *               public void onClick(View view) {
 *
 *               }
 *           });
 *           main.addView(b);
 *
 *           return rootView;
 *       }
 *   }
 *
 */
