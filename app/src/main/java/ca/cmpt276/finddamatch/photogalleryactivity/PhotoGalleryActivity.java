package ca.cmpt276.finddamatch.photogalleryactivity;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;

/**
 * Creates the PhotoGalleryFragment and is used to navigate to the PhotoGalleryFragment
 */
public class PhotoGalleryActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return PhotoGalleryFragment.newInstance();
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, PhotoGalleryActivity.class);
    }
}
