package ca.cmpt276.finddamatch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import ca.cmpt276.finddamatch.model.Card;
import ca.cmpt276.finddamatch.model.CardManager;
import ca.cmpt276.finddamatch.model.OptionsManager;
import ca.cmpt276.finddamatch.photogalleryactivity.PhotoGalleryActivity;


/**
 * Class for the options activity UI.
 * User is able to select from two sets of images to play with.
 **/
public class OptionsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final int[] firstImageSet = {R.drawable.set_one_image1, R.drawable.set_one_image2,
            R.drawable.set_one_image3, R.drawable.set_one_image4, R.drawable.set_one_image5,
            R.drawable.set_one_image6, R.drawable.set_one_image7, R.drawable.set_one_image8,
            R.drawable.set_one_image9, R.drawable.set_one_image10, R.drawable.set_one_image11,
            R.drawable.set_one_image12, R.drawable.set_one_image13, R.drawable.set_one_image14,
            R.drawable.set_one_image15, R.drawable.set_one_image16, R.drawable.set_one_image17,
            R.drawable.set_one_image18, R.drawable.set_one_image19, R.drawable.set_one_image20,
            R.drawable.set_one_image21, R.drawable.set_one_image22, R.drawable.set_one_image23,
            R.drawable.set_one_image24, R.drawable.set_one_image25, R.drawable.set_one_image26,
            R.drawable.set_one_image27, R.drawable.set_one_image28, R.drawable.set_one_image29,
            R.drawable.set_one_image30, R.drawable.set_one_image31};

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

    private OptionsManager options = OptionsManager.getInstance();

    private CardManager manager = CardManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.OptionsActivityTheme);
        setContentView(R.layout.activity_options);

        setupImageSetSpinner();
        setupNumOfImagesSpinner();
        setupNumofCardsSpinner();
        setupExportImagesSpinner();
        setupDifficultySpinner();
        photoGallery();
        setupBackButton();
        startGalleryImages();
    }

    private void setupExportImagesSpinner() {
        // TODO: add a toast message here telling the user where/when the images will be exported.
        Spinner spinner = findViewById(R.id.exportImagesSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.exportImagesOptions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        if (OptionsManager.getIsExportSelected()) {
            spinner.setSelection(0);
        } else {
            spinner.setSelection(1);
        }
    }

    private void setupDifficultySpinner() {
        Spinner spinner = findViewById(R.id.difficultyspinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Modes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        switch (OptionsManager.getDifficulty()) {
            case "Easy":
                spinner.setSelection(0);
                break;
            case "Medium":
                spinner.setSelection(1);
                break;
            case "Hard":
                spinner.setSelection(2);
                break;
        }
    }


    private void setupNumofCardsSpinner() {
        Spinner spinner = findViewById(R.id.numOfCardsSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.NumOfCards, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        if (OptionsManager.getNumOfCards() == 5) {
            spinner.setSelection(0);
        } else if (OptionsManager.getNumOfCards() == 10) {
            spinner.setSelection(1);
        } else if (OptionsManager.getNumOfCards() == 15) {
            spinner.setSelection(2);
        } else if (OptionsManager.getNumOfCards() == 20) {
            spinner.setSelection(3);
        } else if (OptionsManager.getNumOfCards() == 7 || OptionsManager.getNumOfCards() == 13 || OptionsManager.getNumOfCards() == 31) {
            spinner.setSelection(4);
        }
    }

    private void setupNumOfImagesSpinner() {
        Spinner spinner = findViewById(R.id.numOfImagesSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.NumOfImages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        if (OptionsManager.getNumOfImagesInCard() == 3) {
            spinner.setSelection(0);
        }
        if (OptionsManager.getNumOfImagesInCard() == 4) {
            spinner.setSelection(1);
        }
        if (OptionsManager.getNumOfImagesInCard() == 6) {
            spinner.setSelection(2);
        }
    }

    private void setupImageSetSpinner() {
        Spinner spinner = findViewById(R.id.ImageSetSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.ImageSets, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFERENCES", MODE_PRIVATE);
        int n = sharedPreferences.getInt("CurrentWordSet", 1);
        if (n == 1) {
            spinner.setSelection(0);
        }
        if (n == 2) {
            spinner.setSelection(1);
        }
        if (n == 3) {
            spinner.setSelection(2);
        }
        if (n == 4) {
            spinner.setSelection(3);
        }
        if (n == 5) {
            spinner.setSelection(4);
        }
        if (n == 6) {
            spinner.setSelection(5);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        getNumOfBitmapImages(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/FindDaMatch/");
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFERENCES", MODE_PRIVATE);
        int n = sharedPreferences.getInt("CurrentWordSet", 1);
        if (n == 5) {
            String isBitmap = sharedPreferences.getString("BitmapImageSet", null);
            String isGalleryImageBitmap = sharedPreferences.getString("GalleryImageSet", null);

            if(isBitmap != null) {
                if (isBitmap.equals("BitmapImageSet")) {
                    System.out.println("I happen");
                    OptionsManager.setIsWord(false);
                    OptionsManager.setCustom(true);
                }
            }
            if (isGalleryImageBitmap != null) {
                if (isGalleryImageBitmap.equals("GalleryImageSet")) {

                    System.out.println("I happen GalleryImageSet");
                    OptionsManager.setIsWord(false);
                    OptionsManager.setCustom(true);
                }
            }
        }
    }

    // back button for toolbar, from: https://www.youtube.com/watch?v=JkVdP-e9BCo
    private void setupBackButton() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, OptionsActivity.class);
    }

    private boolean compareNumImagesToNumDownloadedImages() {
        int num = getNumOfBitmapImages(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/FindDaMatch/");
        return (OptionsManager.getNumOfImagesInCard() != 3 || num >= 7) &&
                (OptionsManager.getNumOfImagesInCard() != 4 || num >= 13) &&
                (OptionsManager.getNumOfImagesInCard() != 6 || num >= 31);
    }

    private boolean compareNumImagesToNumGalleryImages() {
        int num = getNumOfBitmapImages(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/GalleryImages/");
        return (OptionsManager.getNumOfImagesInCard() != 3 || num >= 7) &&
                (OptionsManager.getNumOfImagesInCard() != 4 || num >= 13) &&
                (OptionsManager.getNumOfImagesInCard() != 6 || num >= 31);
    }

    // storing and getting integer array using JSON: idea from
    // https://www.youtube.com/watch?v=f-kcvxYZrB4
    private void saveImageSet(int[] imageSet, int i) {
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFERENCES", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(imageSet);
        editor.putInt("CurrentSet", i);
        editor.putString("ImageSet", json);
        editor.apply();
        OptionsManager.setCurrImageSet(imageSet);
    }

    private int getNumOfBitmapImages(String fileDirectory) {
        File folder = new File(fileDirectory);
        int numOfFiles = 0;
        if (folder.exists()) {
            File[] files = folder.listFiles((dir1, name) -> (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png")));

            if (files != null) {
                for (File file : files) {
                    numOfFiles++;
                }
            }
        }
        return numOfFiles;
    }

    private void setAsBitmapSet(int i) {

        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFERENCES", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("CurrentSet", i);
        editor.putBoolean("BitmapSet", true);
        editor.apply();

    }

    private void saveNumOfImagesPerCard(int numOfImagesOnCard) {
        SharedPreferences sharedPreferences = getSharedPreferences("ImagesPerCardPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("Num images per card", numOfImagesOnCard);
        editor.apply();
        OptionsManager.setNumOfImagesInCard(numOfImagesOnCard);
    }

    public static int getNumImagesOnCard(Context context) {
        Card card = new Card();
        SharedPreferences sharedPreferences = context.getSharedPreferences("ImagesPerCardPreferences", MODE_PRIVATE);
        int defaultValue = 3;
        int numImagesOnCard = sharedPreferences.getInt("Num images per card", defaultValue);
        card.setNumOfImages(numImagesOnCard); // save number of images in card to the card class
        return numImagesOnCard;
    }

    private void saveNumCardsInDeck(int numCardsInDeck) {
        SharedPreferences sharedPreferences = getSharedPreferences("NumCardsPerDeckPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("Num cards in deck", numCardsInDeck);
        editor.apply();
        OptionsManager.setNumOfCards(numCardsInDeck);
    }

    public static int getNumCardsInDeck(Context context) {
        CardManager manager = CardManager.getInstance();
        SharedPreferences sharedPreferences = context.getSharedPreferences("NumCardsPerDeckPreferences", MODE_PRIVATE);
        int defaultValue = 5;
        int numCardsInDeck = sharedPreferences.getInt("Num cards in deck", defaultValue);
        CardManager.setNumOfCards(numCardsInDeck); // save number of cards in deck to the card manager class
        return numCardsInDeck;
    }

    private void saveWordSet(String[] wordSet, int i) {
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFERENCES", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(wordSet);
        editor.putInt("CurrentWordSet", i);
        editor.putString("WordSet", json);
        editor.apply();
        OptionsManager.setWordSet(wordSet);
    }

    /**
     * Since this activity is launching Flickr, app will crash if we select to have 4 or 6 images
     * per card because we have not configured those number of images on one card.
     */
    private void photoGallery() {
        ImageButton customImagesButton = findViewById(R.id.custom_images_button);
        customImagesButton.setOnClickListener(v -> {
            Intent intent = PhotoGalleryActivity.createIntent(OptionsActivity.this);
            startActivity(intent);
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Spinner numOfCardsSpinner = findViewById(R.id.numOfCardsSpinner);
        Spinner numOfImagesSpinner = findViewById(R.id.numOfImagesSpinner);
        Spinner imageSetSpinner = findViewById(R.id.ImageSetSpinner);
        Spinner exportSpinner = findViewById(R.id.exportImagesSpinner);
        Spinner difficultySpinner = findViewById(R.id.difficultyspinner);

        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);

        SharedPreferences sharedPref = getSharedPreferences("SHARED_PREFERENCES", MODE_PRIVATE);
        int n = sharedPref.getInt("CurrentWordSet", 1);

        switch (parent.getId()) {
            case R.id.numOfCardsSpinner:
                if (position == 0) {
                    if (OptionsManager.isCustom()) {
                        if (n == 5) {
                            if (!compareNumImagesToNumDownloadedImages()) {
                                saveImageSet(firstImageSet, 1);
                                saveWordSet(firstWordSet, 1);
                                OptionsManager.setIsWord(false);
                                OptionsManager.setCustom(false);

                                SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFERENCES", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("CurrentWordSet", 1);
                                editor.putBoolean("IsWord", false);
                                editor.apply();
                                imageSetSpinner.setSelection(0);
                                Toast.makeText(this, "Not Enough Images Downloaded Setting Image set to Set One", Toast.LENGTH_LONG).show();
                            }
                        } else if (n == 6) {
                            if (!compareNumImagesToNumGalleryImages()) {
                                saveImageSet(firstImageSet, 1);
                                saveWordSet(firstWordSet, 1);
                                OptionsManager.setIsWord(false);
                                OptionsManager.setCustom(false);

                                SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFERENCES", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("CurrentWordSet", 1);
                                editor.putBoolean("IsWord", false);
                                editor.apply();
                                imageSetSpinner.setSelection(0);
                                Toast.makeText(this, "Not Enough Images Gallery Setting Image set to Set One", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    saveNumCardsInDeck(5);
                    numOfCardsSpinner.setSelection(0);
                }
                if (position == 1) {
                    if (OptionsManager.isCustom()) {
                        if (n == 5) {
                            if (!compareNumImagesToNumDownloadedImages()) {
                                saveImageSet(firstImageSet, 1);
                                saveWordSet(firstWordSet, 1);
                                OptionsManager.setIsWord(false);
                                OptionsManager.setCustom(false);

                                SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFERENCES", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("CurrentWordSet", 1);
                                editor.putBoolean("IsWord", false);
                                editor.apply();
                                imageSetSpinner.setSelection(0);
                                Toast.makeText(this, "Not Enough Images Downloaded Setting Image set to Set One", Toast.LENGTH_LONG).show();
                            }
                        } else if (n == 6) {
                            if (!compareNumImagesToNumGalleryImages()) {
                                saveImageSet(firstImageSet, 1);
                                saveWordSet(firstWordSet, 1);
                                OptionsManager.setIsWord(false);
                                OptionsManager.setCustom(false);

                                SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFERENCES", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("CurrentWordSet", 1);
                                editor.putBoolean("IsWord", false);
                                editor.apply();
                                imageSetSpinner.setSelection(0);
                                Toast.makeText(this, "Not Enough Images Gallery Setting Image set to Set One", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    if (OptionsManager.getNumOfImagesInCard() == 3) {
                        saveNumCardsInDeck(5);
                        numOfCardsSpinner.setSelection(0);
                        Toast.makeText(this, "Number of Images not supported for number of Cards setting number of cards to 5", Toast.LENGTH_LONG).show();
                    } else {
                        saveNumCardsInDeck(10);
                        numOfCardsSpinner.setSelection(1);
                    }
                }
                if (position == 2) {
                    if (OptionsManager.isCustom()) {
                        if (n == 5) {
                            if (!compareNumImagesToNumDownloadedImages()) {
                                saveImageSet(firstImageSet, 1);
                                saveWordSet(firstWordSet, 1);
                                OptionsManager.setIsWord(false);
                                OptionsManager.setCustom(false);

                                SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFERENCES", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("CurrentWordSet", 1);
                                editor.putBoolean("IsWord", false);
                                editor.apply();
                                imageSetSpinner.setSelection(0);
                                Toast.makeText(this, "Not Enough Images Downloaded Setting Image set to Set One", Toast.LENGTH_LONG).show();
                            }
                        } else if (n == 6) {
                            if (!compareNumImagesToNumGalleryImages()) {
                                saveImageSet(firstImageSet, 1);
                                saveWordSet(firstWordSet, 1);
                                OptionsManager.setIsWord(false);
                                OptionsManager.setCustom(false);

                                SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFERENCES", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("CurrentWordSet", 1);
                                editor.putBoolean("IsWord", false);
                                editor.apply();
                                imageSetSpinner.setSelection(0);
                                Toast.makeText(this, "Not Enough Images Gallery Setting Image set to Set One", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    if (OptionsManager.getNumOfImagesInCard() == 3 || OptionsManager.getNumOfImagesInCard() == 4) {
                        saveNumCardsInDeck(5);
                        numOfCardsSpinner.setSelection(0);
                        Toast.makeText(this, "Number of Cards not supported for number of Images setting number of cards to 5", Toast.LENGTH_LONG).show();
                    } else {
                        saveNumCardsInDeck(15);
                        numOfCardsSpinner.setSelection(2);
                    }
                }
                if (position == 3) {
                    if (OptionsManager.isCustom()) {
                        if (n == 5) {
                            if (!compareNumImagesToNumDownloadedImages()) {
                                saveImageSet(firstImageSet, 1);
                                saveWordSet(firstWordSet, 1);
                                OptionsManager.setIsWord(false);
                                OptionsManager.setCustom(false);

                                SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFERENCES", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("CurrentWordSet", 1);
                                editor.putBoolean("IsWord", false);
                                editor.apply();
                                imageSetSpinner.setSelection(0);
                                Toast.makeText(this, "Not Enough Images Downloaded Setting Image set to Set One", Toast.LENGTH_LONG).show();
                            }
                        } else if (n == 6) {
                            if (!compareNumImagesToNumGalleryImages()) {
                                saveImageSet(firstImageSet, 1);
                                saveWordSet(firstWordSet, 1);
                                OptionsManager.setIsWord(false);
                                OptionsManager.setCustom(false);

                                SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFERENCES", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("CurrentWordSet", 1);
                                editor.putBoolean("IsWord", false);
                                editor.apply();
                                imageSetSpinner.setSelection(0);
                                Toast.makeText(this, "Not Enough Images Gallery Setting Image set to Set One", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    if (OptionsManager.getNumOfImagesInCard() == 3 || OptionsManager.getNumOfImagesInCard() == 4) {
                        saveNumCardsInDeck(5);
                        numOfCardsSpinner.setSelection(0);
                        Toast.makeText(this, "Number of Cards not supported for number of Images setting number of cards to 5", Toast.LENGTH_LONG).show();
                    } else {
                        saveNumCardsInDeck(20);
                        numOfCardsSpinner.setSelection(3);
                    }
                }
                if (position == 4) {
                    if (OptionsManager.isCustom()) {
                        if (n == 5) {
                            if (!compareNumImagesToNumDownloadedImages()) {
                                saveImageSet(firstImageSet, 1);
                                saveWordSet(firstWordSet, 1);
                                OptionsManager.setIsWord(false);
                                OptionsManager.setCustom(false);

                                SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFERENCES", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("CurrentWordSet", 1);
                                editor.putBoolean("IsWord", false);
                                editor.apply();
                                imageSetSpinner.setSelection(0);
                                Toast.makeText(this, "Not Enough Images Downloaded Setting Image set to Set One", Toast.LENGTH_LONG).show();
                            }
                        } else if (n == 6) {
                            if (!compareNumImagesToNumGalleryImages()) {
                                saveImageSet(firstImageSet, 1);
                                saveWordSet(firstWordSet, 1);
                                OptionsManager.setIsWord(false);
                                OptionsManager.setCustom(false);

                                SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFERENCES", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("CurrentWordSet", 1);
                                editor.putBoolean("IsWord", false);
                                editor.apply();
                                imageSetSpinner.setSelection(0);
                                Toast.makeText(this, "Not Enough Images Gallery Setting Image set to Set One", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    if (OptionsManager.getNumOfImagesInCard() == 3) {
                        saveNumCardsInDeck(7);
                    }
                    if (OptionsManager.getNumOfImagesInCard() == 4) {
                        saveNumCardsInDeck(13);
                    }
                    if (OptionsManager.getNumOfImagesInCard() == 6) {
                        saveNumCardsInDeck(31);
                    }
                    numOfCardsSpinner.setSelection(4);
                }
                break;
            case R.id.ImageSetSpinner:
                if (position == 0) {
                    saveImageSet(firstImageSet, 1);
                    saveWordSet(firstWordSet, 1);
                    OptionsManager.setIsWord(false);
                    OptionsManager.setCustom(false);

                    SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFERENCES", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("CurrentWordSet", 1);
                    editor.putBoolean("IsWord", false);
                    editor.apply();
                    imageSetSpinner.setSelection(0);
                }
                if (position == 1) {
                    saveImageSet(secondImageSet, 2);
                    saveWordSet(secondWordSet, 2);
                    OptionsManager.setIsWord(false);
                    OptionsManager.setCustom(false);

                    SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFERENCES", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("CurrentWordSet", 2);
                    editor.putBoolean("IsWord", false);
                    editor.apply();
                    imageSetSpinner.setSelection(1);
                }
                if (position == 2) {
                    saveImageSet(firstImageSet, 3);
                    saveWordSet(firstWordSet, 3);
                    OptionsManager.setIsWord(true);
                    OptionsManager.setCustom(false);

                    SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFERENCES", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("CurrentWordSet", 3);
                    editor.putBoolean("IsWord", true);
                    editor.apply();

                    imageSetSpinner.setSelection(2);
                }
                if (position == 3) {
                    saveImageSet(secondImageSet, 4);
                    saveWordSet(secondWordSet, 4);
                    OptionsManager.setIsWord(true);
                    OptionsManager.setCustom(false);

                    SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFERENCES", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("IsWord", true);
                    editor.putInt("CurrentWordSet", 4);
                    editor.apply();
                    imageSetSpinner.setSelection(3);
                }
                if (position == 4) {
                    if (!compareNumImagesToNumDownloadedImages()) {
                        saveImageSet(firstImageSet, 1);
                        saveWordSet(firstWordSet, 1);
                        OptionsManager.setIsWord(false);
                        OptionsManager.setCustom(false);

                        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFERENCES", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("CurrentWordSet", 1);
                        editor.putBoolean("IsWord", false);
                        editor.apply();
                        imageSetSpinner.setSelection(0);
                        Toast.makeText(this, "Not Enough Images Downloaded Setting Image set to Set One", Toast.LENGTH_LONG).show();
                    } else {
                        setAsBitmapSet(5);

                        // this might cause issues in the future because i have to save an image set
                        saveImageSet(firstImageSet, 5);
                        OptionsManager.setIsWord(false);
                        OptionsManager.setCustom(true);

                        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFERENCES", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("BitmapImageSet", "BitmapImageSet");
                        editor.putString("GalleryImageSet", "null");
                        editor.putInt("CurrentWordSet", 5);
                        editor.putBoolean("IsWord", false);
                        editor.apply();
                        imageSetSpinner.setSelection(4);
                    }

                }
                if (position == 5) { //Gallery Image set
                    if (!compareNumImagesToNumGalleryImages()) {
                        saveImageSet(firstImageSet, 1);
                        saveWordSet(firstWordSet, 1);
                        OptionsManager.setIsWord(false);
                        OptionsManager.setCustom(false);

                        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFERENCES", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("CurrentWordSet", 1);
                        editor.putBoolean("IsWord", false);
                        editor.apply();
                        imageSetSpinner.setSelection(0);
                        Toast.makeText(this, "Not Enough Images Selected from Gallery Setting Image set to Set One", Toast.LENGTH_LONG).show();
                    } else {
                        setAsBitmapSet(6);
                        // this might cause issues in the future because i have to save an image set
                        saveImageSet(firstImageSet, 6);
                        OptionsManager.setIsWord(false);
                        OptionsManager.setCustom(true);

                        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFERENCES", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("GalleryImageSet", "GalleryImageSet");
                        editor.putString("BitmapImageSet", "null");
                        editor.putInt("CurrentWordSet", 6);
                        editor.putBoolean("IsWord", false);
                        editor.apply();
                        imageSetSpinner.setSelection(5);
                    }

                }
                break;
            case R.id.numOfImagesSpinner:
                if (position == 0) {
                    if (OptionsManager.isCustom()) {
                        saveNumOfImagesPerCard(3);
                        if (n == 5) {
                            if (!compareNumImagesToNumDownloadedImages()) {
                                saveImageSet(firstImageSet, 1);
                                saveWordSet(firstWordSet, 1);
                                OptionsManager.setIsWord(false);
                                OptionsManager.setCustom(false);

                                SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFERENCES", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("CurrentWordSet", 1);
                                editor.putBoolean("IsWord", false);
                                editor.apply();
                                imageSetSpinner.setSelection(0);
                                Toast.makeText(this, "Not Enough Images Downloaded Setting Image set to Set One", Toast.LENGTH_LONG).show();
                            }

                        }
                        if (n == 6) {
                            if (!compareNumImagesToNumGalleryImages()) {
                                saveImageSet(firstImageSet, 1);
                                saveWordSet(firstWordSet, 1);
                                OptionsManager.setIsWord(false);
                                OptionsManager.setCustom(false);

                                SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFERENCES", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("CurrentWordSet", 1);
                                editor.putBoolean("IsWord", false);
                                editor.apply();
                                imageSetSpinner.setSelection(0);
                                Toast.makeText(this, "Not Enough Images Gallery Setting Image set to Set One", Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                    if (OptionsManager.getNumOfCards() == 13 || OptionsManager.getNumOfCards() == 31) {
                        saveNumOfImagesPerCard(3);
                        saveNumCardsInDeck(7);
                        numOfCardsSpinner.setSelection(4);
                        numOfImagesSpinner.setSelection(0);
                    }
                    if (OptionsManager.getNumOfCards() > 7) {
                        saveNumOfImagesPerCard(3);
                        saveNumCardsInDeck(5);
                        numOfCardsSpinner.setSelection(0);
                        numOfImagesSpinner.setSelection(0);
                        Toast.makeText(this, "Set to Number of Cards to 5 to support 3 images", Toast.LENGTH_LONG).show();
                    } else {
                        saveNumOfImagesPerCard(3);
                        numOfImagesSpinner.setSelection(0);
                    }
                }
                if (position == 1) {
                    if (OptionsManager.isCustom()) {
                        saveNumOfImagesPerCard(4);
                        if (n == 5) {
                            if (!compareNumImagesToNumDownloadedImages()) {
                                saveImageSet(firstImageSet, 1);
                                saveWordSet(firstWordSet, 1);
                                OptionsManager.setIsWord(false);
                                OptionsManager.setCustom(false);

                                SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFERENCES", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("CurrentWordSet", 1);
                                editor.putBoolean("IsWord", false);
                                editor.apply();
                                imageSetSpinner.setSelection(0);
                                Toast.makeText(this, "Not Enough Images Downloaded Setting Image set to Set One", Toast.LENGTH_LONG).show();
                            }

                        }
                        if (n == 6) {
                            if (!compareNumImagesToNumGalleryImages()) {
                                saveImageSet(firstImageSet, 1);
                                saveWordSet(firstWordSet, 1);
                                OptionsManager.setIsWord(false);
                                OptionsManager.setCustom(false);

                                SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFERENCES", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("CurrentWordSet", 1);
                                editor.putBoolean("IsWord", false);
                                editor.apply();
                                imageSetSpinner.setSelection(0);
                                Toast.makeText(this, "Not Enough Images Gallery Setting Image set to Set One", Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                    if (OptionsManager.getNumOfCards() == 31 || OptionsManager.getNumOfCards() == 7) {
                        saveNumOfImagesPerCard(4);
                        saveNumCardsInDeck(13);
                        numOfCardsSpinner.setSelection(4);
                        numOfImagesSpinner.setSelection(1);
                    }
                    if (OptionsManager.getNumOfCards() > 13) {
                        saveNumOfImagesPerCard(4);
                        saveNumCardsInDeck(5);
                        numOfCardsSpinner.setSelection(0);
                        numOfImagesSpinner.setSelection(1);
                        Toast.makeText(this, "Set to Number of Cards to 5 to support 4 images", Toast.LENGTH_LONG).show();
                    } else {
                        saveNumOfImagesPerCard(4);
                        numOfImagesSpinner.setSelection(1);
                    }
                }
                if (position == 2) {
                    if (OptionsManager.isCustom()) {
                        saveNumOfImagesPerCard(6);
                        if (n == 5) {
                            if (!compareNumImagesToNumDownloadedImages()) {
                                saveImageSet(firstImageSet, 1);
                                saveWordSet(firstWordSet, 1);
                                OptionsManager.setIsWord(false);
                                OptionsManager.setCustom(false);

                                SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFERENCES", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("CurrentWordSet", 1);
                                editor.putBoolean("IsWord", false);
                                editor.apply();
                                imageSetSpinner.setSelection(0);
                                Toast.makeText(this, "Not Enough Images Downloaded Setting Image set to Set One", Toast.LENGTH_LONG).show();
                            }

                        }
                        if (n == 6) {
                            if (!compareNumImagesToNumGalleryImages()) {
                                saveImageSet(firstImageSet, 1);
                                saveWordSet(firstWordSet, 1);
                                OptionsManager.setIsWord(false);
                                OptionsManager.setCustom(false);

                                SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFERENCES", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("CurrentWordSet", 1);
                                editor.putBoolean("IsWord", false);
                                editor.apply();
                                imageSetSpinner.setSelection(0);
                                Toast.makeText(this, "Not Enough Images Gallery Setting Image set to Set One", Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                    saveNumOfImagesPerCard(6);
                    if (OptionsManager.getNumOfCards() == 7 || OptionsManager.getNumOfCards() == 13) {
                        saveNumCardsInDeck(31);
                    }
                    numOfImagesSpinner.setSelection(2);
                }
                break;

            case R.id.exportImagesSpinner:
                if (position == 0) {
                    Toast.makeText(this, getString(R.string.options_export_toast), Toast.LENGTH_SHORT).show();
                    OptionsManager.setIsExportSelected(true);
                } else {
                    OptionsManager.setIsExportSelected(false);
                }
                break;

            case R.id.difficultyspinner:
                if (position == 0) {
                    OptionsManager.setDifficulty("Easy");
                    SharedPreferences difficultyPreferences = getSharedPreferences("DifficultyPreferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = difficultyPreferences.edit();
                    editor.putString("Difficulty", "Easy");
                    editor.apply();
                    difficultySpinner.setSelection(0);
                } else if (position == 1) {
                    OptionsManager.setDifficulty("Medium");
                    SharedPreferences difficultyPreferences = getSharedPreferences("DifficultyPreferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = difficultyPreferences.edit();
                    editor.putString("Difficulty", "Medium");
                    editor.apply();
                    difficultySpinner.setSelection(1);
                } else if (position == 2) {
                    OptionsManager.setDifficulty("Hard");
                    SharedPreferences difficultyPreferences = getSharedPreferences("DifficultyPreferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = difficultyPreferences.edit();
                    editor.putString("Difficulty", "Hard");
                    editor.apply();
                    difficultySpinner.setSelection(2);
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
                        System.out.println("more than 31 images only choosing 31");
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

    private void startGalleryImages() {
        ImageButton startGameBtn = findViewById(R.id.gallery_images_button);
        startGameBtn.setOnClickListener(v -> {
            Intent intent = MyPhotosActivity.createIntent(OptionsActivity.this);
            startActivity(intent);
        });
    }

}
