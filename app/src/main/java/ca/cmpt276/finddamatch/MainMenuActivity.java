package ca.cmpt276.finddamatch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import ca.cmpt276.finddamatch.model.Card;
import ca.cmpt276.finddamatch.model.CardManager;
import ca.cmpt276.finddamatch.model.HighScore;
import ca.cmpt276.finddamatch.model.HighScoreManager;
import ca.cmpt276.finddamatch.model.HighScoreTable;
import ca.cmpt276.finddamatch.model.Key;
import ca.cmpt276.finddamatch.model.OptionsManager;
import ca.cmpt276.finddamatch.photogalleryactivity.PhotoGalleryFragment;
import ca.cmpt276.finddamatch.photogalleryactivity.QueryPreferences;

/**
 * Main Menu of the application. User is able to navigate to all other parts of this app through
 * this activity.
 */
public class MainMenuActivity extends AppCompatActivity {
    private CardManager cardManager = CardManager.getInstance();
    private OptionsManager optionManager = OptionsManager.getInstance();
    private HighScoreTable table = HighScoreTable.getInstance();
    private Thread thread;
    private static final int[] firstImageSet = {R.drawable.set_one_image1, R.drawable.set_one_image2, R.drawable.set_one_image3,
            R.drawable.set_one_image4, R.drawable.set_one_image5,
            R.drawable.set_one_image6, R.drawable.set_one_image7, R.drawable.set_one_image8, R.drawable.set_one_image9, R.drawable.set_one_image10,
            R.drawable.set_one_image11, R.drawable.set_one_image12, R.drawable.set_one_image13, R.drawable.set_one_image14, R.drawable.set_one_image15,
            R.drawable.set_one_image16, R.drawable.set_one_image17, R.drawable.set_one_image18, R.drawable.set_one_image19, R.drawable.set_one_image20,
            R.drawable.set_one_image21, R.drawable.set_one_image22, R.drawable.set_one_image23, R.drawable.set_one_image24, R.drawable.set_one_image25,
            R.drawable.set_one_image26, R.drawable.set_one_image27, R.drawable.set_one_image28, R.drawable.set_one_image29, R.drawable.set_one_image30,
            R.drawable.set_one_image31};

    private final int[] secondImageSet = {R.drawable.set_two_image1, R.drawable.set_two_image2,
            R.drawable.set_two_image3, R.drawable.set_two_image4, R.drawable.set_two_image5,
            R.drawable.set_two_image6, R.drawable.set_two_image7, R.drawable.set_two_image8,
            R.drawable.set_two_image9, R.drawable.set_two_image10, R.drawable.set_two_image11,
            R.drawable.set_two_image12, R.drawable.set_two_image13, R.drawable.set_two_image14,
            R.drawable.set_two_image15, R.drawable.set_two_image16, R.drawable.set_two_image17,
            R.drawable.set_two_image18, R.drawable.set_two_image19, R.drawable.set_two_image20,
            R.drawable.set_two_image21, R.drawable.set_two_image22, R.drawable.set_two_image23,
            R.drawable.set_two_image24, R.drawable.set_two_image25, R.drawable.set_two_image26,
            R.drawable.set_two_image27, R.drawable.set_two_image28, R.drawable.set_two_image29,
            R.drawable.set_two_image30, R.drawable.set_two_image31};

    private final String[] firstWordSet = {"Sick", "Captain America", "Chocolate", "Covid-19", "Sneeze", "Cupcake",
            "Muffin", "Futuristic Guy", "Nurse", "Doctor", "Emergency Kit", "Cake",
            "Donut", "Fever", "John Wick", "Groot", "Wash yo hands", "Girl with Red Hair",
            "Coffee", "Magic Wishing Ball", "Magneto", "Naruto", "Pill", "Wolverine",
            "Temperature", "Girl with Silver Hair", "Stethoscope", "Cough", "Runny Nose",
            "Crab", "Hat"};

    private final String[] secondWordSet = {"Bee", "Bird", "Book", "Bridge", "Butterfly", "Cat", "Wrench", "Boy", "Cow",
            "Dinosaur", "Dog", "Fish", "Giraffe", "Girl", "Earth", "Hippo", "Jellyfish", "Lamp",
            "Bracelet", "Money", "Octopus", "Orange", "Penguin", "Pig", "Shark", "Spider", "Star", "Tree",
            "Triforce", "Turtle", "Ant"};

    private static int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.MainActivityTheme);
        setContentView(R.layout.activity_main);

        updateItems();
        startGame();
        optionsMenu();
        helpMenu();
        highScoresMenu();
        setupStoragePermission();


    }

    private void updateItems() {
        thread = new Thread() {
            @Override
            public void run() {
                loadSharedPreferences();
                System.out.println("background thread started");
            }

        };
        thread.start();

    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    private void loadSharedPreferences() {
        if (flag == 0) {

            SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFERENCES", MODE_PRIVATE);
            SharedPreferences imagePreferences = getSharedPreferences("ImagesPerCardPreferences", MODE_PRIVATE);
            SharedPreferences numOfCardsPreferences = getSharedPreferences("NumCardsPerDeckPreferences", MODE_PRIVATE);
            SharedPreferences difficultyPreferences = getSharedPreferences("DifficultyPreferences", MODE_PRIVATE);
            String isBitmap = sharedPreferences.getString("BitmapImageSet", null);
            if (!sharedPreferences.contains("ImageSet") && isBitmap == null) {
                CardManager.setImageSet(firstImageSet);
                OptionsManager.setCurrImageSet(firstImageSet);
                OptionsManager.setWordSet(firstWordSet);
                OptionsManager.setIsWord(false);
            } else {
                int n = sharedPreferences.getInt("CurrentWordSet", 1);
                if (n == 1) {
                    CardManager.setImageSet(firstImageSet);
                    OptionsManager.setCurrImageSet(firstImageSet);
                    OptionsManager.setWordSet(firstWordSet);
                    OptionsManager.setIsWord(false);
                    OptionsManager.setCustom(false);
                } else if (n == 2) {
                    CardManager.setImageSet(secondImageSet);
                    OptionsManager.setCurrImageSet(secondImageSet);
                    OptionsManager.setWordSet(secondWordSet);
                    OptionsManager.setIsWord(false);
                    OptionsManager.setCustom(false);
                } else if (n == 3) {
                    CardManager.setImageSet(firstImageSet);
                    OptionsManager.setCurrImageSet(firstImageSet);
                    OptionsManager.setWordSet(firstWordSet);
                    OptionsManager.setIsWord(true);
                    OptionsManager.setCustom(false);
                } else if (n == 4) {
                    CardManager.setImageSet(secondImageSet);
                    OptionsManager.setCurrImageSet(secondImageSet);
                    OptionsManager.setWordSet(secondWordSet);
                    OptionsManager.setIsWord(true);
                    OptionsManager.setCustom(false);
                } else if (n == 5) {

                    System.out.println("Find da match imageset");
                    OptionsManager.setIsWord(false);
                    OptionsManager.setCustom(true);
                } else if (n == 6) {
                    System.out.println("Gallery Images set");
                    OptionsManager.setIsWord(false);
                    OptionsManager.setCustom(true);
                }
            }
            if (sharedPreferences.contains("HighScore")) {
                Type type = new TypeToken<Map<Key, HighScoreManager>>() {
                }.getType();
                String jsonString = sharedPreferences.getString("HighScore", "");
                table.setMap(new Gson().fromJson(jsonString, type));
            }
            OptionsManager.setNumOfImagesInCard(imagePreferences.getInt("Num images per card", 3));
            OptionsManager.setNumOfCards(numOfCardsPreferences.getInt("Num cards in deck", 5));
            CardManager.setNumOfCards(numOfCardsPreferences.getInt("Num cards in deck", 5));
            CardManager.setNumOfImages(imagePreferences.getInt("Num images per card", 3));
            OptionsManager.setDifficulty(difficultyPreferences.getString("Difficulty", "Easy"));

            flag = 1;
        }


    }

    private Bitmap[] getBitmapImages(String fileDirectory) {
        Bitmap[] customImages = null;
        File folder = new File(fileDirectory);

        if (folder.exists()) {
            File[] files = folder.listFiles((dir1, name) -> (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png")));

            if (files != null) {

                if (files.length > 31) {
                    customImages = new Bitmap[31]; //in order to keep the app fast if there are too many images + puts less strain on to the thread, discuss with others on size of array
                    for (int i = 0; i < 31; i++) {
                        String imageLocation = fileDirectory + files[i].getName();
                        customImages[i] = BitmapFactory.decodeFile(imageLocation);
                        System.out.println(imageLocation + "this is MainActivity");
                    }
                } else {
                    customImages = new Bitmap[files.length];
                    for (int i = 0; i < files.length; i++) {
                        String imageLocation = fileDirectory + files[i].getName();
                        customImages[i] = BitmapFactory.decodeFile(imageLocation);
                        System.out.println(imageLocation + "this is MainActivity");
                    }
                }
            }
        }
        return customImages;
    }


    private void highScoresMenu() {
        ImageButton highScoresBtn = findViewById(R.id.high_scores_button);
        highScoresBtn.setOnClickListener(v -> {
            if (!thread.isAlive()) {
            Intent intent = HighScoresActivity.createIntent(MainMenuActivity.this);
            startActivity(intent);
            }
        });
    }

    private void helpMenu() {
        ImageButton helpBtn = findViewById(R.id.help_button);
        helpBtn.setOnClickListener(v -> {

            Intent intent = HelpMenuActivity.createIntent(MainMenuActivity.this);
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 112) {
            System.out.println("on activity result called");
            updateItems();
        } else {
            System.out.println("came back from somewhere else");
        }
    }

    private void optionsMenu() {
        Card card = new Card();
        card.setNumOfImages(OptionsActivity.getNumImagesOnCard(this)); // set how many images on a card here

        CardManager.setNumOfCards(OptionsActivity.getNumCardsInDeck(this));

        OptionsManager options = OptionsManager.getInstance();
        OptionsManager.setNumOfImagesTotal(OptionsManager.getNumOfImagesInCard());
        OptionsManager.setIsExportSelected(OptionsManager.getIsExportSelected());

        ImageButton optionsBtn = findViewById(R.id.options_button);
        optionsBtn.setOnClickListener(v -> {
            Intent intent = OptionsActivity.createIntent(MainMenuActivity.this);
            if (!thread.isAlive()) {
                startActivityForResult(intent, 112);
            }
        });
    }

    private void startGame() {
        ImageButton startGameBtn = findViewById(R.id.start_game_button);
        startGameBtn.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFERENCES", MODE_PRIVATE);
            int n = sharedPreferences.getInt("CurrentWordSet", 1);
            if (n == 5) {

                System.out.println("I happen");
                cardManager.setBitmapSet(getBitmapImages(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/FindDaMatch/"));
                optionManager.setCurrBitmapSet(getBitmapImages(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/FindDaMatch/"));
                optionManager.setIsWord(false);
                optionManager.setCustom(true);
            } else if (n == 6) {

                cardManager.setBitmapSet(getBitmapImages(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/GalleryImages/"));
                optionManager.setCurrBitmapSet(getBitmapImages(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/GalleryImages/"));
                optionManager.setIsWord(false);
                optionManager.setCustom(true);
            }
            Intent intent = GameActivity.createIntent(MainMenuActivity.this);
            if (!thread.isAlive()) {

                startActivity(intent);
            }
        });
    }

    /**
     * method to ask for permission to user the storage for saving the photos
     * tutorial from: https://www.youtube.com/watch?v=cBS_qL3BUnM
     */
    private void setupStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

}