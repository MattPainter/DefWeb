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
    /* Sets layout and adds WebFragment*/
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
    /* Inflate the menu; this adds items to the action bar if it is present. */
    getMenuInflater().inflate(R.menu.web, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    /*
     Handles menu presses of refresh and clear
     Refresh - refresh the listview items
     Clear - deletes the database via explicit deletions not dropping
    */
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
   * A fragment containing listview //TODO: rename
   */
  public static class WebFragment extends Fragment {
    private DefDataSource dataSource;

    public WebFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View rootView = inflater.inflate(R.layout.fragment_web, container, false);
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

      /* Manages the searching of data -
        listens for submission of query then searches by it
        results sent to listview
      */
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

          /* Gets list of definitions and adapter to list view*/
          ListView listView = (ListView) getActivity().findViewById(R.id.main_listview);
          List<Definition> definitionList1 = dataSource.getAllDefinitions();
          List<String> stringList = new ArrayList<String>();
          ArrayAdapter<String> arrayAdapter1 = (ArrayAdapter<String>) listView.getAdapter();

          /* Searches for matches linearly */
          for (Definition definition : definitionList1) {
            if (definition.getDefName().toLowerCase().contains(query)) {
              stringList.add(definition.getDefName());
            }
          }

          /* Clean up */
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

  /* Shows activity to add definition */
  public void addDefinition(MenuItem item) {
    Intent addDefIntent = new Intent(this, AddDefinitionActivity.class);
    startActivity(addDefIntent);
  }

  /* Function connects to DB and sends all definitions to listview */
  private void refreshListView() {
    /* Gets adapter to listview */
    ListView listView = (ListView) findViewById(R.id.main_listview);
    ArrayAdapter<String> arrayAdapter = (ArrayAdapter<String>) listView.getAdapter();
    arrayAdapter.clear();

    /* Set up data source to access database*/
    DefDataSource dataSource = new DefDataSource(this);
    try {
      dataSource.open();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    /* Adds definitions to adapter */
    List<Definition> definitions = dataSource.getAllDefinitions();
    for (Definition d : definitions) {
      arrayAdapter.add(d.getDefName());
    }

    arrayAdapter.notifyDataSetChanged(); //necessary?
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

    dataSource.dropAndRemakeTable();

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
