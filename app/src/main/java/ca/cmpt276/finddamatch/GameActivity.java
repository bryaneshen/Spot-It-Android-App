package ca.cmpt276.finddamatch;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Chronometer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;

import ca.cmpt276.finddamatch.model.Card;
import ca.cmpt276.finddamatch.model.CardManager;
import ca.cmpt276.finddamatch.model.HighScoreManager;
import ca.cmpt276.finddamatch.model.HighScoreTable;
import ca.cmpt276.finddamatch.model.Key;
import ca.cmpt276.finddamatch.model.OptionsManager;
import ca.cmpt276.finddamatch.views.DiscardPileView;
import ca.cmpt276.finddamatch.views.DrawPileView;

/**
 * Initializes the game and the CustomViews (DiscardPileView and DrawPileView) in order to enable
 * game-play. Hard codes the images on to the cards from the OptionsActivity. Shuffles the deck every time activity
 * is launched thus ensuring unique game-play. Also handles the saving of new HighScores achieved
 * within this activity.
 */
public class GameActivity extends AppCompatActivity implements CongratulationsMessageFragment.MessageFragmentListener {

    private DrawPileView drawView;
    private DiscardPileView discardView;
    private Chronometer chronometer;
    private String userName;
    private int currentGameLength;
    private CardManager cardManager = CardManager.getInstance();
    private OptionsManager optionsManager = OptionsManager.getInstance();
    private int[] imageSet = OptionsManager.getCurrImageSet();
    private Bitmap[] bitmapImageSet = OptionsManager.getCurrBitmapSet();
    private String[] wordSet = OptionsManager.getWordSet();
    private int numOfCards = (int) (Math.pow(OptionsManager.getNumOfImagesInCard() - 1, 2) + OptionsManager.getNumOfImagesInCard() - 1 + 1);
    private Card[] deck = new Card[numOfCards];
    private int actualNumOfCards = OptionsManager.getNumOfCards();
    private int numOfImages = OptionsManager.getNumOfImagesInCard();
    private HighScoreTable table = HighScoreTable.getInstance();
    private boolean isWord = OptionsManager.isWord();
    private MediaPlayer player;
    private static SoundPool soundPool;
    private static int correctAnswerSound, wrongAnswerSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //superCardInitializer();
        initalizeGame();
        //cardManager.shuffleDeck(numOfCards);
        super.onCreate(savedInstanceState);
        setTheme(R.style.GameActivityTheme);
        setContentView(R.layout.activity_game);

        discardView = findViewById(R.id.discardPileView2);
        drawView = findViewById(R.id.drawPileView2);

        drawView.setActivity(this, chronometer);

        chronometer = findViewById(R.id.stopwatch);

        drawView.setActivity(this, chronometer);

        startChronometer();

        drawView.setDiscardView(discardView);
        discardView.setDrawView(drawView);

        loadGameStartSound();
        loadGameSounds();


    }

    private void loadGameStartSound() {
        if (player == null) {
            player = MediaPlayer.create(this, R.raw.game_has_started);
        }
        player.start();
    }

    private void loadGameSounds() {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(6)
                .setAudioAttributes(audioAttributes)
                .build();

        correctAnswerSound = soundPool.load(this, R.raw.correct_answer, 1);
        wrongAnswerSound = soundPool.load(this, R.raw.wrong_answer, 1);
    }

    public static void playCorrectAnswerSound() {
        soundPool.play(correctAnswerSound, 1, 1, 0, 0, 1);
    }

    public static void playWrongAnswerSound() {
        soundPool.play(wrongAnswerSound, 1, 1, 0, 0, 1);
    }

    public void startChronometer() {

        chronometer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CardManager.setCurrentCard(0);
        player.release();
        player = null;

        soundPool.release();
        soundPool = null;
    }

    /*
        Fills in image arrays for every card according to the hardcoded combinations. I've checked with pen and paper and it should work.
        Then it uses every image array to initialize all the cards that need to be in the deck
        In the end it calls the cardManager.setDeck() method to set the array of cards as the current deck

        We should move this method to CardManager however I am unable to do this due to the nature of Options.getSavedImageSet(Context) requiring a context as a parameter.
     */

    void superCardInitializer() {

        int n = OptionsManager.getNumOfImagesInCard() - 1;

        int count = 0;

        for (int i = 0; i < numOfCards; i++) {
            deck[i] = new Card();
        }
        int numOfCards = 0;
        if (OptionsManager.isCustom()) {
            Bitmap[] bit = new Bitmap[n + 1];
            //For First card
            System.arraycopy(bitmapImageSet, 0, bit, 0, n + 1);
            deck[numOfCards].setListOfBitmapImages(bit);
            numOfCards++;
            //For n following cards
            for (int j = 0; j < n; j++) {
                bit[count] = bitmapImageSet[0];
                count++;
                for (int k = 0; k < n; k++) {
                    bit[count] = bitmapImageSet[n + 1 + n * j + k];
                    count++;
                }
                deck[numOfCards].setListOfBitmapImages(bit);
                numOfCards++;
                count = 0;
            }
            count = 0;
            //For N*N following cards
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    bit[count] = bitmapImageSet[i + 1];
                    count++;
                    for (int k = 0; k < n; k++) {
                        final int index = n + 1 + n * k + (i * k + j) % n;
                        bit[count] = bitmapImageSet[index];
                        count++;
                    }
                    deck[numOfCards].setListOfBitmapImages(bit);
                    numOfCards++;
                    count = 0;
                }
            }

        } else {
            int[] images = new int[n + 1];
            String[] words = new String[n + 1];
            //For First card
            for (int i = 0; i <= n; i++) {
                images[i] = imageSet[i];
                words[i] = wordSet[i];
            }
            deck[numOfCards].setListOfImages(images);
            deck[numOfCards].setListOfWords(words);

            numOfCards++;
            //For n following cards
            for (int j = 0; j < n; j++) {
                images[count] = imageSet[0];
                words[count] = wordSet[0];
                count++;
                for (int k = 0; k < n; k++) {
                    images[count] = imageSet[n + 1 + n * j + k];
                    words[count] = wordSet[n + 1 + n * j + k];
                    count++;
                }
                deck[numOfCards].setListOfImages(images);
                deck[numOfCards].setListOfWords(words);

                numOfCards++;
                count = 0;
            }
            count = 0;
            //For N*N following cards
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    images[count] = imageSet[i + 1];
                    words[count] = wordSet[i + 1];
                    count++;
                    for (int k = 0; k < n; k++) {
                        final int index = n + 1 + n * k + (i * k + j) % n;
                        images[count] = imageSet[index];
                        words[count] = wordSet[index];
                        count++;
                    }
                    deck[numOfCards].setListOfImages(images);
                    deck[numOfCards].setListOfWords(words);

                    numOfCards++;
                    count = 0;
                }
            }
            if (isWord) {
                for (int i = 0; i < numOfCards; i++) {
                    int maxWords = (int) ((Math.random() * (numOfImages - 1)) + 1);
                    boolean[] isWord = new boolean[numOfImages];
                    for (int j = 0; j < maxWords; j++) {
                        int randIndex = (int) ((Math.random() * (numOfImages)));
                        if (isWord[randIndex]) {
                            j--;
                            continue;
                        }
                        isWord[randIndex] = true;
                    }
                    deck[i].setIsWord(isWord);
                }
            }
        }
        if (OptionsManager.getDifficulty().equals("Medium")) {
            for (int i = 0; i < numOfCards; i++) {
                int maxRotations = (int) ((Math.random() * (numOfImages)) + 1);
                boolean[] isRotate = new boolean[numOfImages];
                for (int j = 0; j < maxRotations; j++) {
                    int randIndex = (int) ((Math.random() * (numOfImages)));
                    if (isRotate[randIndex]) {
                        j--;
                        continue;
                    }
                    isRotate[randIndex] = true;
                }
                deck[i].setIsRotate(isRotate);
            }
        }
        if (OptionsManager.getDifficulty().equals("Hard")) {
            for (int i = 0; i < numOfCards; i++) {
                int maxRotations = (int) ((Math.random() * (numOfImages)) + 1);
                boolean[] isRotate = new boolean[numOfImages];
                for (int j = 0; j < maxRotations; j++) {
                    int randIndex = (int) ((Math.random() * (numOfImages)));
                    if (isRotate[randIndex]) {
                        j--;
                        continue;
                    }
                    isRotate[randIndex] = true;
                }
                deck[i].setIsRotate(isRotate);
            }
            for (int i = 0; i < numOfCards; i++) {
                int maxSizeChange = (int) ((Math.random() * (numOfImages)) + 1);
                boolean[] isSmall = new boolean[numOfImages];
                for (int j = 0; j < maxSizeChange; j++) {
                    int randIndex = (int) ((Math.random() * (numOfImages)));
                    if (isSmall[randIndex]) {
                        j--;
                        continue;
                    }
                    isSmall[randIndex] = true;
                }
                deck[i].setIsSmall(isSmall);
            }
        }
        CardManager.setDeck(deck);
    }


    @Override
    protected void onStop() {
        super.onStop();
        //updateData(); //data is updated accordingly after activity is no longer visible.
        updateItems();
    }

    private void updateData() {
        String currentUserName = userName;

        @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

        HighScoreManager manager = table.getValue(numOfImages, actualNumOfCards);

        manager.AddToArray(currentUserName, date, currentGameLength, numOfImages, actualNumOfCards, OptionsManager.isWord()); // add to highScores

        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFERENCES", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.enableComplexMapKeySerialization().setPrettyPrinting().create();
        Type type = new TypeToken<Map<Key, HighScoreManager>>() {
        }.getType();
        String json = gson.toJson(table.getMap(), type);
        editor.putString("HighScore", json);
        editor.apply();
    }
    private void initalizeGame(){
        Thread thread = new Thread(){
            @Override
            public void run() {
                superCardInitializer();
                cardManager.shuffleDeck(numOfCards);
            }
        };
        thread.start();
    }

    private void updateItems() {
        Thread thread = new Thread(){
            @Override
            public void run() {
                updateData();
            }
        };
        thread.start();
    }

    public void openCongratulationsDialog() { // when game is over, launch congrats fragment
        CongratulationsMessageFragment congratulationsMessageFragment = new CongratulationsMessageFragment();
        congratulationsMessageFragment.show(getSupportFragmentManager(), "Dialog");
        player.release();
        player = MediaPlayer.create(this, R.raw.game_is_won);
        player.start();
    }

    @Override
    public void applyText(String username) {
        userName = username; // must use userName to save time for high score.
        currentGameLength = drawView.getTimerResult(); //get how long it took to finish the game
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, GameActivity.class);
    }
}