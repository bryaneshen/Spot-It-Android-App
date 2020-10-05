package ca.cmpt276.finddamatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * First Activity of the Application. If nothing is done it will launch the Main Menu
 * However if the Skip Button is pressed it will skip the animations and just launch the Main Menu
 */
public class SplashScreenActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 5000; //Splash screen timer
    private Boolean fRun = true;
    private Thread thread;
    private ImageButton skipButton;
    private ImageView gameTitle, gameCreators;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.SplashScreenTheme);
        setContentView(R.layout.activity_splash_screen);

        skipButton = findViewById(R.id.skip_button);
        gameTitle = findViewById(R.id.game_title);
        gameCreators = findViewById(R.id.created_by);

        animateScreen();
        splashScreenTimer();
        skipButton();
    }

    /*
        The Skip Button of the User Interface
     */
    private void skipButton() {

        skipButton.setOnClickListener(v -> {
            if (thread != null && thread.isAlive()) {
                fRun = false;
            }
            launchMainMenu();
        });
    }

    /*
        Handles the animations on the screen
     */
    private void animateScreen() {
        Animation upperScreen = AnimationUtils.loadAnimation(this, R.anim.top_screen_animation);
        Animation lowerScreen = AnimationUtils.loadAnimation(this, R.anim.top_screen_animation);
        gameTitle.setAnimation(upperScreen);
        skipButton.setAnimation(lowerScreen);
        gameCreators.setAnimation(lowerScreen);
    }

    /*
        A method which creates a timer for the splash screen to end
        Also prevents the launch of the MainMenuActivity for the second time
        if bRun = true
     */
    //inspired by https://www.youtube.com/watch?v=O4q49TDsXAo and https://stackoverflow.com/questions/33780491/skipping-a-splash-screen
    private void splashScreenTimer() {
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(SPLASH_TIME_OUT);
                    if (fRun) {
                        launchMainMenu();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    /*
        Creates an intent which launches the Main Menu
     */
    private void launchMainMenu() {
        Intent intent = new Intent(SplashScreenActivity.this, MainMenuActivity.class);
        startActivity(intent);
        finish();
    }

}