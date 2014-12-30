package com.android.matt.defWeb;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.matt.defWeb.R;

import org.w3c.dom.Text;

public class ViewImage extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_image);
    if (savedInstanceState == null) {
      getFragmentManager().beginTransaction()
          .add(R.id.container, new PlaceholderFragment())
          .commit();
    }
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_view_image, menu);
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
      View rootView = inflater.inflate(R.layout.fragment_view_image, container, false);
      final ImageView imageView = (ImageView)rootView.findViewById(R.id.imageView_full);
      final TextView imageDefName = (TextView)rootView.findViewById(R.id.textView_image_name);
      Boolean flag = true;

      ViewTreeObserver vto = imageView.getViewTreeObserver();
      vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
        public boolean onPreDraw() {
          /* If not bitmap is loaded */
          if (imageView.getDrawable() == null) {
            Intent callingIntent = getActivity().getIntent();

            String defName = callingIntent.getStringExtra("DefName");
            String imgLoc = callingIntent.getStringExtra("ImgLoc");

            ImageHelper imageHelper = new ImageHelper(getActivity());
            imageHelper.setPath(imgLoc);
            imageHelper.showScaledImage(imageView);

            imageDefName.setText(defName);

          }
          return true;
        }
      });

      return rootView;
    }
  }
}

//TODO: try https://stackoverflow.com/questions/16894215/how-pinch-zoom-image-in-image-zoom-android
