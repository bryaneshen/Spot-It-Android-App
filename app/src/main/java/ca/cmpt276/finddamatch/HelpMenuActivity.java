package ca.cmpt276.finddamatch;

import androidx.appcompat.app.AppCompatActivity;

import android.app.VoiceInteractor;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Objects;

/**
 * Helps new players in how to play the game,
 * Also provides sources for images that were used.
 */
public class HelpMenuActivity extends AppCompatActivity {

    private String allBackgrounds = "<a href= 'https://www.walpaperlist.com/2020/01/landscape-minimalist-wallpaper-1920x1080.html'> " +
            "Source for all of the Background images </a>";
    int backGroundInfo_TextView = R.id.source_1;

    private String allButtonsAndImageViews = "<a href= 'https://flamingtext.com/'> " +
            "Source for all of the ImageButtons and ImageViews </a>";
    int allButtonAndImageViews_TextView = R.id.source_2;

    private String allCardImages = "<a href= 'https://icons8.com/'> " +
            "Source for all of of the Images displayed on the Cards </a>";
    int allCardImages_TextView = R.id.source_3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.HelpActivityTheme);
        setContentView(R.layout.activity_help);
        setHyperLinks(allBackgrounds, backGroundInfo_TextView);
        setHyperLinks(allButtonsAndImageViews, allButtonAndImageViews_TextView);
        setHyperLinks(allCardImages, allCardImages_TextView);
        setupBackButton();
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, HelpMenuActivity.class);
    }

    // back button for toolbar, from: https://www.youtube.com/watch?v=JkVdP-e9BCo
    private void setupBackButton() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    //Inspired from https://stackoverflow.com/questions/9290651/make-a-hyperlink-textview-in-android
    private void setHyperLinks(String link, int resourceID) {
        TextView textView = findViewById(resourceID);
        textView.setClickable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(Html.fromHtml(link, Html.FROM_HTML_MODE_COMPACT));
    }
}