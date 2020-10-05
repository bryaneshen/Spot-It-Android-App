package ca.cmpt276.finddamatch.photogalleryactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;

import ca.cmpt276.finddamatch.R;
import ca.cmpt276.finddamatch.model.GalleryItem;

/**
 * Displays images downloaded by the user in PhotoGalleryActivity. Enables the user to delete and view
 * the images that they had chosen.
 */
public class DownloadedPhotosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_downloaded_photos);
        super.onCreate(savedInstanceState);

        initRecyclerView();
        deleteImageToastMessage();
    }

    private void deleteImageToastMessage() {
        Toast.makeText(this, R.string.delete_image_toast_message, Toast.LENGTH_SHORT).show();
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, DownloadedPhotosActivity.class);
    }


    public static class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
        private static final String TAG = "RecyclerViewAdapter";
        private ArrayList<File> myPhotos;
        Context mContext;

        public RecyclerViewAdapter(Context mContext, ArrayList<File> galleryItems) {
            this.myPhotos = galleryItems;
            this.mContext = mContext;

        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
            return new ViewHolder(view);
        }

        //https://stackoverflow.com/questions/45413292/recyclerview-does-not-update-after-removing-an-item
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Log.d(TAG, "onBindViewHolder: called");

            Glide.with(mContext).load(myPhotos.get(position)).placeholder(R.drawable.loading_gif).into(holder.image);

            holder.image.setOnClickListener(v -> Toast.makeText(mContext, R.string.delete_image_toast_message, Toast.LENGTH_SHORT).show());
            holder.image.setOnLongClickListener(v -> {

                removeItem(myPhotos.get(position), position);
                holder.image.refreshDrawableState();
                return true;
            });


        }

        private void removeItem(File s, int position) { //removes item from current Activity imageview
            deleteData(myPhotos.get(position).getPath()); //deletes data in directory
            myPhotos.remove(s);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, myPhotos.size());

        }

        @Override
        public int getItemCount() {
            return myPhotos.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            private GridLayout parentLayout;
            private ImageView image;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.list_item_imageview);
                image.setAdjustViewBounds(true);
                image.setCropToPadding(false);
                image.setScaleType(ImageView.ScaleType.FIT_XY);
                parentLayout = itemView.findViewById(R.id.parent_layout);
            }
        }
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler);
        RecyclerView.LayoutManager layoutManager;
        layoutManager = new GridLayoutManager(DownloadedPhotosActivity.this, 3);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(DownloadedPhotosActivity.this, getBitmapFiles());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public ArrayList<File> getBitmapFiles() {
        String fileDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/FindDaMatch/";
        File folder = new File(fileDirectory);
        File[] files = null;
        if (folder.exists()) {
            files = folder.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png"));
                }
            });

        }
        ArrayList<File> myFilesList = new ArrayList<>();
        if(files!= null) {
            Collections.addAll(myFilesList, files);
        }
        return myFilesList;
    }

    private static void deleteData(String filePath) {
        File file = new File(filePath);
        boolean deleted = file.delete();
    }


}