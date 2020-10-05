package ca.cmpt276.finddamatch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import ca.cmpt276.finddamatch.model.HighScore;
import ca.cmpt276.finddamatch.model.HighScoreManager;
import ca.cmpt276.finddamatch.model.HighScoreTable;
import ca.cmpt276.finddamatch.model.Key;

/**
 * Outputs the users different high score combinations and saves them to a JSON file in order to be viewed in later sessions
 * of launching the activity and application.
 */
public class HighScoresActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    static private ArrayList<String> arrayList = new ArrayList<>();
    private ListView list;
    private HighScoreTable table = HighScoreTable.getInstance();
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.HighScoresActivityTheme);
        setContentView(R.layout.activity_high_scores);

        setupSpinner();
        setupBackButton();
        list = findViewById(R.id.highScoresList_threeImagesSevenCards);
        HighScoreManager manager = table.getValue(3, 5);
        displayHighScores(manager);
        setupEraseHighScores();
    }

    private void setupSpinner() {
        spinner = findViewById((R.id.spinner2));
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.GameModes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter((adapter));
        spinner.setOnItemSelectedListener(this);
    }

    private void setupEraseHighScores() {
        ImageButton eraseHighScores = findViewById(R.id.ResetScores);

        eraseHighScores.setOnClickListener(v -> {
            eraseSelectedHighScores();
        });
    }

    private void eraseSelectedHighScores() {
        int position = spinner.getSelectedItemPosition();
        HighScoreManager manager;
        switch (position) {
            case (0):
                manager = table.getValue(3, 5);
                manager.ClearStringArrayList();
                manager.SetDefaultHighScores();
                displayHighScores(manager);
                saveHighScores();
                break;
            case (1):
                manager = table.getValue(4, 5);
                manager.ClearStringArrayList();
                manager.SetDefaultHighScores();
                displayHighScores(manager);
                saveHighScores();
                break;
            case (2):
                manager = table.getValue(6, 5);
                manager.ClearStringArrayList();
                manager.SetDefaultHighScores();
                displayHighScores(manager);
                saveHighScores();
                break;
            case (3):
                manager = table.getValue(4, 10);
                manager.ClearStringArrayList();
                manager.SetDefaultHighScores();
                displayHighScores(manager);
                saveHighScores();
                break;
            case (4):
                manager = table.getValue(6, 10);
                manager.ClearStringArrayList();
                manager.SetDefaultHighScores();
                displayHighScores(manager);
                saveHighScores();
                break;
            case (5):
                manager = table.getValue(6, 15);
                manager.ClearStringArrayList();
                manager.SetDefaultHighScores();
                displayHighScores(manager);
                saveHighScores();
                break;
            case (6):
                manager = table.getValue(6, 20);
                manager.ClearStringArrayList();
                manager.SetDefaultHighScores();
                displayHighScores(manager);
                saveHighScores();
                break;
            case (7):
                manager = table.getValue(3, 7);
                manager.ClearStringArrayList();
                manager.SetDefaultHighScores();
                displayHighScores(manager);
                saveHighScores();
                break;
            case (8):
                manager = table.getValue(4, 13);
                manager.ClearStringArrayList();
                manager.SetDefaultHighScores();
                displayHighScores(manager);
                saveHighScores();
                break;
            case (9):
                manager = table.getValue(6, 31);
                manager.ClearStringArrayList();
                manager.SetDefaultHighScores();
                displayHighScores(manager);
                saveHighScores();
                break;
        }
    }

    private void saveHighScores() {
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

    // back button for toolbar, from: https://www.youtube.com/watch?v=JkVdP-e9BCo
    private void setupBackButton() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, HighScoresActivity.class);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
        ((TextView) parent.getChildAt(0)).setTextSize(20);
        HighScoreManager manager;
        switch (position) {
            case (0):
                manager = table.getValue(3, 5);
                displayHighScores(manager);
                break;
            case (1):
                manager = table.getValue(4, 5);
                displayHighScores(manager);
                break;
            case (2):
                manager = table.getValue(6, 5);
                displayHighScores(manager);
                break;
            case (3):
                manager = table.getValue(4, 10);
                displayHighScores(manager);
                break;
            case (4):
                manager = table.getValue(6, 10);
                displayHighScores(manager);
                break;
            case (5):
                manager = table.getValue(6, 15);
                displayHighScores(manager);
                break;
            case (6):
                manager = table.getValue(6, 20);
                displayHighScores(manager);
                break;
            case (7):
                manager = table.getValue(3, 7);
                displayHighScores(manager);
                break;
            case (8):
                manager = table.getValue(4, 13);
                displayHighScores(manager);
                break;
            case (9):
                manager = table.getValue(6, 31);
                displayHighScores(manager);
                break;
        }
    }

    //populate the array adapter so that we can view the highscores in the ListView
    private void displayHighScores(HighScoreManager manager) {
        arrayList.clear();
        arrayList = manager.GenerateStringArrayList(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                HighScoresActivity.this, //Context for activity
                R.layout.listview_textlayout, //Layout to use (Create)
                arrayList); // Items to be displayed
        list.setAdapter(adapter);
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}