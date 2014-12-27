package com.android.matt.defWeb;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.matt.defWeb.R;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AddDefinitionActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_definition);
    if (savedInstanceState == null) {
      getFragmentManager().beginTransaction()
          .add(R.id.container, new addDefFragment())
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

  /**
   * A placeholder fragment allowing user to add a definition to database
   */
  public static class addDefFragment extends Fragment {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String mCurrentPhotoPath;

    public addDefFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      /* Get add definition button and add on click listener */
      View rootView = inflater.inflate(R.layout.fragment_add_definition, container, false);
      Button addDefButton = (Button) rootView.findViewById(R.id.button_add_definition);
      Button captureImgButton = (Button) rootView.findViewById(R.id.button_capture_img);
      addDefButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          /* Get two fields to define definition  */
          EditText defName = (EditText) getActivity().findViewById(R.id.textview_definition_name);
          TextView defBody = (TextView) getActivity().findViewById(R.id.textview_img_loc);

          /* Finds if you have no image location string */
          if (defBody.toString() != "") {
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

        captureImgButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          /* Recommended method on Google Developer pages for sending intent to capture image */
          Intent takePicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

          String defName = ((EditText) getActivity().findViewById(R.id.textview_definition_name))
              .getText().toString();

          if (takePicIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
              photoFile = createImageFile(defName);
            } catch (IOException ex) {
              // Error occurred while creating the File
              Log.e("DefWeb", "Failed to create image file");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
              takePicIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                  Uri.fromFile(photoFile));
              startActivityForResult(takePicIntent, REQUEST_IMAGE_CAPTURE);
            }
          }
        }
      });

      return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
      /* Shows the thumbnail */
      if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
        Bitmap bmp = BitmapFactory.decodeFile(mCurrentPhotoPath);
        ImageView img = (ImageView) getActivity().findViewById(R.id.imageView_picture_thumbnail);
        img.setImageBitmap(bmp);
      }
    }

    /* Suggested method for creating an image file */
    private File createImageFile(String defName) throws IOException {
      /*
      Creates file of form:
      JPEG_(definition name)_(time of image capture)_.jpg
      * */
      String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
      String imageFileName = "JPEG_" + defName + "_" + timeStamp + "_";
      File storageDir = Environment.getExternalStoragePublicDirectory(
          Environment.DIRECTORY_PICTURES);
      File image = File.createTempFile(
          imageFileName,  /* prefix */
          ".jpg",         /* suffix */
          storageDir      /* directory */
      );

      /* Stores a simple path to file */
      mCurrentPhotoPath = image.getPath();

      TextView textview_img_loc = (TextView)getActivity().findViewById(R.id.textview_img_loc);
      textview_img_loc.setText(mCurrentPhotoPath);

      return image;
    }
  }
}
