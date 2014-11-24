package com.android.matt.defWeb;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.matt.defWeb.R;

import java.sql.SQLException;


public class AddDefinitionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_definition);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_definition, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_add_definition, container, false);
            Button addDefButton = (Button)rootView.findViewById(R.id.button_add_definition);
            addDefButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText defName = (EditText)getActivity().findViewById(R.id.textview_definition_name);
                    EditText defBody = (EditText)getActivity().findViewById(R.id.textview_definition_body);

                    DefDataSource defDataSource = new DefDataSource(getActivity());
                    try {
                        defDataSource.open();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    defDataSource.createDefinition(defName.getText().toString(), defBody.getText().toString());
                    defDataSource.close();

                    getActivity().finish();

                }
            });
            return rootView;
        }
    }
}
