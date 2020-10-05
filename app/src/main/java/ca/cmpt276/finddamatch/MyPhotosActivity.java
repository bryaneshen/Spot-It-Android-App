package ca.cmpt276.finddamatch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ca.cmpt276.finddamatch.model.GalleryItem;


public class MyPhotosActivity extends AppCompatActivity {
    private RecyclerViewAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_photos);
        initRecyclerView();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.choose_my_photos) {
            launchMyPhotoGallery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void launchMyPhotoGallery() {
        if (ActivityCompat.checkSelfPermission(MyPhotosActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MyPhotosActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    100);
            return;
        }
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @SuppressLint("StaticFieldLeak")
    private class FetchItemsTask extends AsyncTask<Intent, Void, Void> {
        private boolean isAdded;

        @Override
        protected Void doInBackground(Intent... intents) {
            isAdded = getNewImages(intents[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.updateAdapter(getBitmapFiles());
            adapter.notifyDataSetChanged();
            if (!isAdded) {
                Toast.makeText(getApplicationContext(), "Maximum number of images you can have is 31", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private boolean getNewImages(Intent data) {
        ClipData clipData = data.getClipData(); //will be null if user only selects one image


        if (clipData != null) {
            if (clipData.getItemCount() + getBitmapFiles().size() > 31) {
                return false;
            } else {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri imageUri = clipData.getItemAt(i).getUri();
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(imageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        saveChosenImages(bitmap); //saves bitmap to android filesystem into a seperate folder
                        System.out.println("loop runs for multiple images");


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            if (getBitmapFiles().size() + 1 > 31) {
                return false;
            } else {
                Uri imageUri = data.getData();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    saveChosenImages(bitmap); //saves bitmap to android filesystem into a seperate folder
                    System.out.println("images are added");

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {
                new FetchItemsTask().execute(data);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_photos_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public static class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
        private static final String TAG = "RecyclerViewAdapter";
        private List<File> myPhotos;
        private Context mContext;

        public RecyclerViewAdapter(Context mContext, List<File> myPhotos) {
            System.out.println("constructor for adapter called");
            this.myPhotos = myPhotos;
            this.mContext = mContext;

        }

        public void updateAdapter(List<File> myPhotos) {
            this.myPhotos = myPhotos;
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
            System.out.println(myPhotos.size() + "shit");

            Glide.with(mContext).load(myPhotos.get(position)).placeholder(R.drawable.loading_gif).into(holder.image);

            holder.image.setOnClickListener(v -> Toast.makeText(mContext, R.string.delete_image_toast_message, Toast.LENGTH_SHORT).show());
            holder.image.setOnLongClickListener(v -> {

                removeItem(myPhotos.get(position), position);
                holder.image.refreshDrawableState();
                return true;
            });

        }

        private void removeItem(File s, int position) { //removes item from current Activity imageview
            System.out.println(myPhotos.get(position) + " deleted ");
            deleteData(myPhotos.get(position).getPath()); //deletes data in directory
            myPhotos.remove(s);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, myPhotos.size());

        }

        private void deleteData(String filePath) {
            File file = new File(filePath);
            boolean deleted = file.delete();
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
                System.out.println("I happen");
                image = itemView.findViewById(R.id.list_item_imageview);
                image.setAdjustViewBounds(true);
                image.setCropToPadding(false);
                image.setScaleType(ImageView.ScaleType.FIT_XY);
                parentLayout = itemView.findViewById(R.id.parent_layout);
            }
        }
    }

    public ArrayList<File> getBitmapFiles() {
        String fileDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/GalleryImages/";
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
        if (files != null) {
            Collections.addAll(myFilesList, files);
        }
        return myFilesList;
    }


    private void initRecyclerView() {
        recyclerView = findViewById(R.id.myphotosrecycler);
        RecyclerView.LayoutManager layoutManager;
        layoutManager = new GridLayoutManager(MyPhotosActivity.this, 3);
        adapter = new RecyclerViewAdapter(MyPhotosActivity.this, getBitmapFiles());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    //inspired by http://androidseekho.com/android-codes/save-bitmap-on-a-specified-path-in-android-programmatically/
    public void saveChosenImages(Bitmap bitmap) {
        File path;
        try {
            boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
            if (isSDPresent) {
                String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/GalleryImages";
                path = new File(dir);
                path.mkdirs();
            } else {
                path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/GalleryImages");

            }
            File imageFile = new File(path, System.currentTimeMillis() + ".jpg");
            FileOutputStream fileOutPutStream = new FileOutputStream(imageFile);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fileOutPutStream);
            fileOutPutStream.flush();
            fileOutPutStream.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }


    public static Intent createIntent(Context context) {
        return new Intent(context, MyPhotosActivity.class);
    }
}