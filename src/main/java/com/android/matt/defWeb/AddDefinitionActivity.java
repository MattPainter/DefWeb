package com.android.matt.defWeb;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;

public class AddDefinitionActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_definition);
    if (savedInstanceState == null) {
      getFragmentManager().beginTransaction()
          .add(R.id.container, new addDefFragment(), "fragment_add_def")
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
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      addDefFragment fragment = (addDefFragment) getFragmentManager().findFragmentByTag("fragment_add_def");
      fragment.onActivityResult(requestCode, resultCode, data);
  }

  /**
   * A placeholder fragment allowing user to add a definition to database
   */
  public static class addDefFragment extends Fragment {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageHelper imageHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      /* Get buttons and create image helper */
      View rootView = inflater.inflate(R.layout.fragment_add_definition, container, false);
      Button addDefButton = (Button) rootView.findViewById(R.id.button_add_definition);
      Button captureImgButton = (Button) rootView.findViewById(R.id.button_capture_img);
      imageHelper = new ImageHelper(getActivity());

      //TODO: stop addition of image to db without first adding def body or pic

      /* Add on click listener to add button */
      addDefButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          /* Get two fields to define definition  */
          EditText defName = (EditText) getActivity().findViewById(R.id.textview_definition_name);
          TextView defBody = (TextView) getActivity().findViewById(R.id.textview_img_loc);

          /* Finds if you have no image location string */
          if (!defBody.toString().equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Add Definition?")
                .setMessage("Are you sure you want to add this definition?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int which) {
                    EditText defName = (EditText) getActivity().findViewById(R.id.textview_definition_name);
                    TextView defBody = (TextView) getActivity().findViewById(R.id.textview_img_loc);

                    DefDataSource defDataSource = new DefDataSource(getActivity());
                    try {
                      defDataSource.open();
                    } catch (SQLException e) {
                      e.printStackTrace();
                    }

                    defDataSource.createDefinition(defName.getText().toString(), defBody.getText().toString());
                    defDataSource.close();

                    /* Return to previous activity */
                    getActivity().finish();
                  }
                })
                .setNegativeButton("No", null)
                .show();
          } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Add Definition?")
                .setMessage("Add definition without picture?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                    EditText defName = (EditText) getActivity().findViewById(R.id.textview_definition_name);
                    TextView defBody = (TextView) getActivity().findViewById(R.id.textview_img_loc);

                    DefDataSource defDataSource = new DefDataSource(getActivity());
                    try {
                      defDataSource.open();
                    } catch (SQLException e) {
                      e.printStackTrace();
                    }

                    defDataSource.createDefinition(defName.getText().toString(), defBody.getText().toString());
                    defDataSource.close();

                    /* Return to previous activity */
                    getActivity().finish();
                  }
                })
                .setNegativeButton("No", null)
                .show();
          }

          //TODO: delete picture
        }
      }
      );

      /* Add on click listener to capture image button */
        captureImgButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          imageHelper.sendTakePicIntent();
        }
      });

      return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
      /* Gets image view to store thumbnail and asks image helper to put the image there */
      ImageView imageViewThumbnail = (ImageView)getActivity().findViewById(R.id.imageView_picture_thumbnail);
      if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
        imageHelper.showThumbnail(imageViewThumbnail);
      }
    }
  }
}
