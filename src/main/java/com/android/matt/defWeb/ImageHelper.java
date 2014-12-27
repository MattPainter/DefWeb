package com.android.matt.defWeb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Matt on 27/12/2014.
 */
public class ImageHelper {
  static final int REQUEST_IMAGE_CAPTURE = 1;
  String mCurrentPhotoPath;
  Context context;

  public ImageHelper(Context context) {
    this.context = context;
  }

  public void sendTakePicIntent() {
    Intent takePicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    Activity activity = (Activity)this.context;

    String defName = ((EditText) activity.findViewById(R.id.textview_definition_name))
        .getText().toString();

    if (takePicIntent.resolveActivity(activity.getPackageManager()) != null) {
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
        activity.startActivityForResult(takePicIntent, REQUEST_IMAGE_CAPTURE);
      }
    }
  }

  /* Suggested method for creating an image file */
  private File createImageFile(String defName) throws IOException {
    Activity activity = (Activity)this.context;

      /*
      Creates file of form:
      JPEG_(definition name)_(time of image capture)_.jpg
      */
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

    TextView textview_img_loc = (TextView)activity.findViewById(R.id.textview_img_loc);
    textview_img_loc.setText(mCurrentPhotoPath);

    return image;
  }

  public void showThumbnail(ImageView imgView) {
      /* Shows the thumbnail */
      Bitmap bmp = BitmapFactory.decodeFile(mCurrentPhotoPath);
      imgView.setImageBitmap(bmp);
  }
}
