package com.android.matt.defWeb;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import com.android.matt.defWeb.R;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    if (id == R.id.action_refresh) {
      refreshListView();
      return true; //correct to do this?
    } else if (id == R.id.action_clear_database) {
      clearDatabase();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  /**
   * A placeholder fragment containing a simple view.
   */
  public static class WebFragment extends Fragment {
    private DefDataSource dataSource;

    public WebFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View rootView = inflater.inflate(R.layout.fragment_web, container, false);
      //RelativeLayout main = (RelativeLayout)rootView.findViewById(R.id.RelativeLayoutMain);
      /* Define data source to connect to DB later*/
      dataSource = new DefDataSource(getActivity());

      /* Open data base connection */
      try {
        dataSource.open();
      } catch (SQLException e) {
        e.printStackTrace();
      }

      /* Get listview adapter */
      ListView listview = (ListView) rootView.findViewById(R.id.main_listview);
      ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);

      /* Get definitions from database and add them to adapter */
      List<Definition> definitionList = dataSource.getAllDefinitions();
      for (Definition definition : definitionList) {
        arrayAdapter.add(definition.getDefName());
      }

      listview.setAdapter(arrayAdapter);

      //manages the search view - sends data to search activity test
      SearchView searchView = (SearchView) rootView.findViewById(R.id.searchView);
      searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        @Override //submits search via button now searches listview //TODO: refresh DB and then search listview
        public boolean onQueryTextSubmit(String query) {
          dataSource = new DefDataSource(getActivity());

          try {
            dataSource.open();
          } catch (SQLException e) {
            e.printStackTrace();
          }

          //SearchView searchView1 = (SearchView)getActivity().findViewById(R.id.searchView);
          ListView listView = (ListView) getActivity().findViewById(R.id.main_listview);
          List<Definition> definitionList1 = dataSource.getAllDefinitions();
          List<String> stringList = new ArrayList<String>();
          ArrayAdapter<String> arrayAdapter1 = (ArrayAdapter<String>) listView.getAdapter();

          for (Definition definition : definitionList1) {
            if (definition.getDefName().toLowerCase().contains(query)) {
              stringList.add(definition.getDefName());
            }
          }

          arrayAdapter1.clear();
          arrayAdapter1.addAll(stringList);
          dataSource.close();
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

  public void addDefinition(MenuItem item) {
    Intent addDefIntent = new Intent(this, AddDefinitionActivity.class);
    startActivity(addDefIntent);
  }

/*
  private List<String> performSearch(String query) {
    List<String> stringList = new ArrayList<String>();

    return null;
  }*/

  /* Function connects to DB and sends all definitions to listview */
  private void refreshListView() {
    ListView listView = (ListView) findViewById(R.id.main_listview);
    ArrayAdapter<String> arrayAdapter = (ArrayAdapter<String>) listView.getAdapter();
    arrayAdapter.clear();

    DefDataSource dataSource = new DefDataSource(this);

    try {
      dataSource.open();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    List<Definition> definitions = dataSource.getAllDefinitions();
    for (Definition d : definitions) {
      arrayAdapter.add(d.getDefName());
    }

    arrayAdapter.notifyDataSetChanged();

    dataSource.close();
  }

  /* Function deletes all definitions in database*/
  private void clearDatabase() {
    DefDataSource dataSource = new DefDataSource(this);

    try {
      dataSource.open();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    List<Definition> definitions = dataSource.getAllDefinitions();
    for (Definition d : definitions) {
      dataSource.deleteDefinition(d);
    }

    dataSource.close();

    refreshListView();
  }

  @Override
  public boolean onSearchRequested() {

    return true;
  }

}

//TODO: Create class to handle storing of images - probably need images for Latex stuff rather than render latex on fly
//TODO: Rename package

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
