package ca.cmpt276.finddamatch.views;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Chronometer;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.IntStream;

import ca.cmpt276.finddamatch.GameActivity;
import ca.cmpt276.finddamatch.R;
import ca.cmpt276.finddamatch.model.Card;
import ca.cmpt276.finddamatch.model.CardManager;
import ca.cmpt276.finddamatch.model.OptionsManager;

/**
 * Custom View class which manages the operations and the creation of a View with Custom Attributes and its
 * images which relate to the Draw Pile in the GameActivity. Helps output images to the screen via the CardManager.
 * Also manages touch operations and overloads the existing onTouch method to enable the user to play the game.
 */

public class DrawPileView extends View {
    private static final String TAG = "rectangle touched";
    private Paint mPaint;
    private Rect[] rectangle;
    private DiscardPileView discardView;
    private CardManager manager = CardManager.getInstance();
    private Card[] deck = CardManager.getDeck();
    private GameActivity gActivity;
    private DrawPileView drawView = this;
    private Chronometer timer;
    private int timerResult;
    private OptionsManager optionsManager = OptionsManager.getInstance();
    private int numOFCards = optionsManager.getNumOfCards();
    private int numOfImages = optionsManager.getNumOfImagesInCard();
    private int[] imagesOnCard = new int[numOfImages];
    private Bitmap[] img = new Bitmap[numOfImages];
    private boolean[] isWord = new boolean[numOfImages];
    private String[] wordsOnCard = new String[numOfImages];
    private Bitmap[] bit = new Bitmap[numOfImages];
    private boolean[] isRotate = new boolean[numOfImages];
    private boolean[] isSmall = new boolean[numOfImages];

    public void setActivity(GameActivity activity, Chronometer stopwatch) {
        gActivity = activity;
        timer = stopwatch;
    }

    public DrawPileView(Context context) {
        super(context);

        init();
    }

    public DrawPileView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public DrawPileView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    public void setDiscardView(DiscardPileView discardView) {
        this.discardView = discardView;
    }

    private void init() {
        //get default images from OptionsActivity
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(7.5f);
        mPaint.setStyle(Paint.Style.STROKE);

        rectangle = new Rect[numOfImages];

        for (int i = 0; i < numOfImages; i++) {
            rectangle[i] = new Rect();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /* reference for drawing the border:
         https://stackoverflow.com/questions/27119034/how-to-draw-borders-for-custom-views-in-android */

        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);

        if (numOfImages == 3) {
            rectangle[0].left = this.getWidth() / 4;
            rectangle[0].top = 0;
            rectangle[0].right = (this.getWidth() * 3 / 4);
            rectangle[0].bottom = this.getHeight() / 2;

            rectangle[1].left = 0;
            rectangle[1].top = this.getHeight() / 2;
            rectangle[1].right = this.getWidth() / 2;
            rectangle[1].bottom = this.getHeight();

            rectangle[2].left = this.getWidth() / 2;
            rectangle[2].top = this.getHeight() / 2;
            rectangle[2].right = this.getWidth();
            rectangle[2].bottom = this.getHeight();


            canvas.drawRect(rectangle[0], mPaint);
            canvas.drawRect(rectangle[1], mPaint);
            canvas.drawRect(rectangle[2], mPaint);
        }

        if (numOfImages == 4) {

            rectangle[0].left = 0;
            rectangle[0].top = 0;
            rectangle[0].right = this.getWidth() / 2;
            rectangle[0].bottom = this.getHeight() / 2;

            rectangle[1].left = this.getWidth() / 2;
            rectangle[1].top = 0;
            rectangle[1].right = this.getWidth();
            rectangle[1].bottom = this.getHeight() / 2;

            rectangle[2].left = 0;
            rectangle[2].top = this.getHeight() / 2;
            rectangle[2].right = this.getWidth() / 2;
            rectangle[2].bottom = this.getHeight();

            rectangle[3].left = this.getWidth() / 2;
            rectangle[3].top = this.getHeight() / 2;
            rectangle[3].right = this.getWidth();
            rectangle[3].bottom = this.getHeight();

            canvas.drawRect(rectangle[0], mPaint);
            canvas.drawRect(rectangle[1], mPaint);
            canvas.drawRect(rectangle[2], mPaint);
            canvas.drawRect(rectangle[3], mPaint);
        }

        if (numOfImages == 6) {

            rectangle[0].left = 0;
            rectangle[0].top = 0;
            rectangle[0].right = this.getWidth() / 3;
            rectangle[0].bottom = this.getHeight() / 2;

            rectangle[1].left = this.getWidth() / 3;
            rectangle[1].top = 0;
            rectangle[1].right = 2 * this.getWidth() / 3;
            rectangle[1].bottom = this.getHeight() / 2;

            rectangle[2].left = 2 * this.getWidth() / 3;
            rectangle[2].top = 0;
            rectangle[2].right = this.getWidth();
            rectangle[2].bottom = this.getHeight() / 2;

            rectangle[3].left = 0;
            rectangle[3].top = this.getHeight() / 2;
            rectangle[3].right = this.getWidth() / 3;
            rectangle[3].bottom = this.getHeight();

            rectangle[4].left = this.getWidth() / 3;
            rectangle[4].top = this.getHeight() / 2;
            rectangle[4].right = 2 * this.getWidth() / 3;
            rectangle[4].bottom = this.getHeight();

            rectangle[5].left = 2 * this.getWidth() / 3;
            rectangle[5].top = this.getHeight() / 2;
            rectangle[5].right = this.getWidth();
            rectangle[5].bottom = this.getHeight();

            canvas.drawRect(rectangle[0], mPaint);
            canvas.drawRect(rectangle[1], mPaint);
            canvas.drawRect(rectangle[2], mPaint);
            canvas.drawRect(rectangle[3], mPaint);
            canvas.drawRect(rectangle[4], mPaint);
            canvas.drawRect(rectangle[5], mPaint);
        }
        if (manager.getCurrentCard() + 1 == numOFCards) {
            for (int i = 0; i < Card.getNumOfImages(); i++) {
                img[i] = null;
            }

            if (img[0] != null) {
                for (int i = 0; i < Card.getNumOfImages(); i++) {
                    img[i].recycle();
                }
            }


            manager.setCurrentCard(0);
            timerResult = (int) (SystemClock.elapsedRealtime() - timer.getBase()); //set the timer result
            timer.stop();
            gActivity.openCongratulationsDialog();

            if (optionsManager.getIsExportSelected()) {
                exportToGallery();
            }
        }

        if (!optionsManager.isCustom()) {
            imagesOnCard = deck[manager.getCurrentCard()+ 1].getListOfImages().clone();
            if (optionsManager.isWord()) {
                isWord = deck[manager.getCurrentCard() +1].getIsWord().clone();
            }
            if(optionsManager.getDifficulty().equals("Medium")) {
                isRotate = deck[manager.getCurrentCard() +1].getIsRotate().clone();
            }
            wordsOnCard = deck[manager.getCurrentCard() +1] .getListOfWords().clone();
            for (int i = 0; i < numOfImages; i++) {
                if (optionsManager.getDifficulty().equals("Easy")) {
                    if (optionsManager.isWord()) {
                        if (isWord[i]) {
                            img[i] = drawText(wordsOnCard[i], 16.0f, 0xffffffff);
                        } else {
                            img[i] = decodeSampledBitmapFromResource(getResources(), imagesOnCard[i], rectangle[i].width(), rectangle[i].height());
                        }
                    } else {
                        img[i] = decodeSampledBitmapFromResource(getResources(), imagesOnCard[i], rectangle[i].width(), rectangle[i].height());
                    }
                    if (isRotate[i]) {
                        img[i] = rotateBitmap(img[i], 180);
                    }
                } else if (optionsManager.getDifficulty().equals("Medium")) {
                    if (optionsManager.isWord()) {
                        if (isWord[i]) {
                            img[i] = drawText(wordsOnCard[i], 16.0f, 0xffffffff);
                        } else {
                            img[i] = decodeSampledBitmapFromResource(getResources(), imagesOnCard[i], rectangle[i].width(), rectangle[i].height());
                        }
                    } else {
                        img[i] = decodeSampledBitmapFromResource(getResources(), imagesOnCard[i], rectangle[i].width(), rectangle[i].height());
                    }
                    if (isRotate[i]) {
                        img[i] = rotateBitmap(img[i], 180);
                    }
                } else if (optionsManager.getDifficulty().equals("Hard")) {
                    if (optionsManager.isWord()) {
                        if (isWord[i]) {
                            img[i] = drawText(wordsOnCard[i], 16.0f, 0xffffffff);
                        } else {
                            img[i] = decodeSampledBitmapFromResource(getResources(), imagesOnCard[i], rectangle[i].width(), rectangle[i].height());
                        }
                    } else {
                        img[i] = decodeSampledBitmapFromResource(getResources(), imagesOnCard[i], rectangle[i].width(), rectangle[i].height());
                    }
                    if (isRotate[i]) {
                        img[i] = rotateBitmap(img[i], 180);
                    }
                }
            }
        } else {
            bit = deck[manager.getCurrentCard() + 1].getListOfBitmapImages();
            if (numOfImages >= 0) System.arraycopy(bit, 0, img, 0, numOfImages);
            if (manager.getCurrentCard() + 1 <= CardManager.getNumOfCards()) {
                for (int i = 0; i < Card.getNumOfImages(); i++) {
                    if (img[i] != null) {
                        if (optionsManager.getDifficulty().equals("Medium")) {
                            isRotate = deck[manager.getCurrentCard() + 1].getIsRotate().clone();
                            if (isRotate[i]) {
                                img[i] = rotateBitmap(img[i], 180);
                            }
                            canvas.drawBitmap(img[i], null, rectangle[i], null);
                        } else if (optionsManager.getDifficulty().equals("Hard")) {

                            isRotate = deck[manager.getCurrentCard() + 1].getIsRotate().clone();
                            isSmall = deck[manager.getCurrentCard() + 1].getIsSmall().clone();

                            Rect newRect = new Rect();
                            newRect.left = rectangle[i].left + (rectangle[i].right - rectangle[i].left) / 4;
                            newRect.right = rectangle[i].right - (rectangle[i].right - rectangle[i].left) / 4;
                            newRect.top = rectangle[i].top - (rectangle[i].top - rectangle[i].bottom) / 4;
                            newRect.bottom = rectangle[i].bottom + (rectangle[i].top - rectangle[i].bottom) / 4;
                            if (isRotate[i]) {
                                img[i] = rotateBitmap(img[i], 180);
                            }
                            if (isSmall[i]) {
                                canvas.drawBitmap(img[i], null, newRect, null);
                            } else {
                                canvas.drawBitmap(img[i], null, rectangle[i], null);
                            }
                        } else {
                            canvas.drawBitmap(img[i], null, rectangle[i], null);
                        }
                    }
                }


            }
        }
        for (int i = 0; i < numOfImages; i++) {
            if (img[i] != null) {
                if (optionsManager.getDifficulty().equals("Medium")) {

                    isRotate = deck[manager.getCurrentCard() + 1].getIsRotate().clone();
                    canvas.drawBitmap(img[i], null, rectangle[i], null);
                } else if (optionsManager.getDifficulty().equals("Hard")) {

                    isRotate = deck[manager.getCurrentCard() +1].getIsRotate().clone();
                    isSmall = deck[manager.getCurrentCard() +1].getIsSmall().clone();

                    Rect newRect = new Rect();
                    newRect.left = rectangle[i].left + (rectangle[i].right - rectangle[i].left) / 4;
                    newRect.right = rectangle[i].right - (rectangle[i].right - rectangle[i].left) / 4;
                    newRect.top = rectangle[i].top - (rectangle[i].top - rectangle[i].bottom) / 4;
                    newRect.bottom = rectangle[i].bottom + (rectangle[i].top - rectangle[i].bottom) / 4;
                    if (isSmall[i]) {
                        canvas.drawBitmap(img[i], null, newRect, null);
                    } else {
                        canvas.drawBitmap(img[i], null, rectangle[i], null);
                    }
                } else {
                    canvas.drawBitmap(img[i], null, rectangle[i], null);
                }
            }

        }

        if(!optionsManager.isCustom()) {
            if (img[0] != null) {
                for (int i = 0; i < Card.getNumOfImages(); i++) {
                    img[i].recycle();
                    img[i] = null;
                }
            }
        }
    }

    // method used from // used from https://www.youtube.com/watch?v=fKSvtgEVZ1M
    private void exportToGallery() {
        discardView.setDrawingCacheEnabled(true);
        MediaStore.Images.Media.insertImage(getContext().getContentResolver(),
                discardView.getDrawingCache(),
                UUID.randomUUID().toString() + ".png",
                "drawing");
        discardView.destroyDrawingCache();
    }

    private Bitmap scaleBitmap(Bitmap bitmap, int wantedWidth, int wantedHeight) {
        Bitmap output = Bitmap.createBitmap(wantedWidth, wantedHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Matrix m = new Matrix();
        m.setScale((float) wantedWidth / bitmap.getWidth(), (float) wantedHeight / bitmap.getHeight());
        canvas.drawBitmap(bitmap, m, new Paint());

        return output;
    }

    public int getTimerResult() {
        if (timerResult != 0)
            return timerResult / 1000;
        else {
            return -1; //something is wrong method is not working as intended
        }
    }

    private Bitmap rotateBitmap(Bitmap source, float angle){
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    // Inspired by https://medium.com/mindorks/building-a-customview-tictactoe-eb439f506505
    // Also inspired by https://www.youtube.com/watch?v=cfwO0Yui43g
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean value = super.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {  //Action down registers that the screen is being touched thus it must return true

                return true;
            }
            case MotionEvent.ACTION_UP: { //This is fired when the user removes his finger from the screen
                float x = event.getX(); // get x coordinate of the touch
                float y = event.getY(); //get y coordinate of the touch
                if (numOfImages == 3) {

                    if (this.getWidth() / 4.0f < x && (this.getWidth() * 3.0f / 4.0f) > x) {  //left < x, right >x
                        if (0 < y && this.getHeight() / 2.0f > y) {  //top < y, bottom > y
                            //is touched
                            Log.d(TAG, "rec1");
                            if (!optionsManager.isCustom()) {
                                int[] imagesOnDiscardCard = deck[manager.getCurrentCard()].getListOfImages().clone();
                                boolean check = IntStream.of(imagesOnDiscardCard).anyMatch(m -> m == imagesOnCard[0]);
                                if (check) {
                                    GameActivity.playCorrectAnswerSound();
                                    manager.setNextCard();

                                    if (optionsManager.getIsExportSelected()) {
                                        exportToGallery();
                                    }

                                    discardView.invalidate();
                                    drawView.invalidate();
                                } else {
                                    GameActivity.playWrongAnswerSound();
                                }

                            } else {
                                Bitmap[] imagesOnDiscardCard = deck[manager.getCurrentCard()].getListOfBitmapImages();
                                boolean check = false;
                                for (int i = 0; i < numOfImages; i++) {
                                    if (BitmapEquals(bit[0], imagesOnDiscardCard[i])) {
                                        check = true;
                                        break;
                                    }
                                }
                                if (check) {
                                    GameActivity.playCorrectAnswerSound();
                                    manager.setNextCard();

                                    if (optionsManager.getIsExportSelected()) {
                                        exportToGallery();
                                    }

                                    discardView.invalidate();
                                    drawView.invalidate();
                                } else {
                                    GameActivity.playWrongAnswerSound();
                                }
                            }

                            return true;
                        }
                    }
                    if (this.getWidth() / 2.0f < x && this.getWidth() > x) { //left < x, right >x
                        //is touched
                        if (this.getHeight() / 2.0f < y && this.getHeight() > y) { //top < y, bottom > y
                            //is touched
                            Log.d(TAG, "rec2");
                            if (!optionsManager.isCustom()) {
                                int[] imagesOnDiscardCard = deck[manager.getCurrentCard()].getListOfImages().clone();
                                boolean check = IntStream.of(imagesOnDiscardCard).anyMatch(m -> m == imagesOnCard[2]);
                                if (check) {
                                    GameActivity.playCorrectAnswerSound();
                                    manager.setNextCard();

                                    if (optionsManager.getIsExportSelected()) {
                                        exportToGallery();
                                    }

                                    discardView.invalidate();
                                    drawView.invalidate();
                                } else {
                                    GameActivity.playWrongAnswerSound();
                                }

                            } else {
                                Bitmap[] imagesOnDiscardCard = deck[manager.getCurrentCard()].getListOfBitmapImages();
                                boolean check = false;
                                for (int i = 0; i < numOfImages; i++) {
                                    if (BitmapEquals(bit[2], imagesOnDiscardCard[i])) {
                                        check = true;
                                        break;
                                    }
                                }
                                if (check) {
                                    GameActivity.playCorrectAnswerSound();
                                    manager.setNextCard();

                                    if (optionsManager.getIsExportSelected()) {
                                        exportToGallery();
                                    }

                                    discardView.invalidate();
                                    drawView.invalidate();
                                } else {
                                    GameActivity.playWrongAnswerSound();
                                }
                            }


                            return true;

                        }
                    }
                    if (0 < x && this.getWidth() / 2.0f > x) { //left < x, right >x
                        //is touched
                        if (this.getHeight() / 2.0f < y && this.getHeight() > y) { //top < y, bottom > y
                            //is touched
                            Log.d(TAG, "rec3");
                            if (!optionsManager.isCustom()) {
                                int[] imagesOnDiscardCard = deck[manager.getCurrentCard()].getListOfImages().clone();
                                boolean check = IntStream.of(imagesOnDiscardCard).anyMatch(m -> m == imagesOnCard[1]);
                                if (check) { //play correct sound???
                                    GameActivity.playCorrectAnswerSound();
                                    manager.setNextCard();

                                    if (optionsManager.getIsExportSelected()) {
                                        exportToGallery();
                                    }

                                    discardView.invalidate();
                                    drawView.invalidate();
                                } else {
                                    GameActivity.playWrongAnswerSound();
                                }

                            } else {
                                Bitmap[] imagesOnDiscardCard = deck[manager.getCurrentCard()].getListOfBitmapImages();
                                boolean check = false;
                                for (int i = 0; i < numOfImages; i++) {
                                    if (BitmapEquals(bit[1], imagesOnDiscardCard[i])) {
                                        check = true;
                                        break;
                                    }
                                }
                                if (check) {
                                    GameActivity.playCorrectAnswerSound();
                                    manager.setNextCard();

                                    if (optionsManager.getIsExportSelected()) {
                                        exportToGallery();
                                    }

                                    discardView.invalidate();
                                    drawView.invalidate();
                                } else {
                                    GameActivity.playWrongAnswerSound();
                                }
                            }
                            return true;
                        }
                    }
                }
                if (numOfImages == 4) {
                    if (0f < x && this.getWidth() / 2.0f > x) {
                        if (0f < y && this.getHeight() / 2.0f > y) {
                            Log.d(TAG, "rec1");
                            if (!optionsManager.isCustom()) {
                                int[] imagesOnDiscardCard = deck[manager.getCurrentCard()].getListOfImages().clone();
                                boolean check = IntStream.of(imagesOnDiscardCard).anyMatch(m -> m == imagesOnCard[0]);
                                if (check) {
                                    GameActivity.playCorrectAnswerSound();
                                    manager.setNextCard();

                                    if (optionsManager.getIsExportSelected()) {
                                        exportToGallery();
                                    }

                                    discardView.invalidate();
                                    drawView.invalidate();
                                } else {
                                    GameActivity.playWrongAnswerSound();
                                }

                            } else {
                                Bitmap[] imagesOnDiscardCard = deck[manager.getCurrentCard()].getListOfBitmapImages();
                                boolean check = false;
                                for (int i = 0; i < numOfImages; i++) {
                                    if (BitmapEquals(bit[0], imagesOnDiscardCard[i])) {
                                        check = true;
                                        break;
                                    }
                                }
                                if (check) {
                                    GameActivity.playCorrectAnswerSound();
                                    manager.setNextCard();

                                    if (optionsManager.getIsExportSelected()) {
                                        exportToGallery();
                                    }

                                    discardView.invalidate();
                                    drawView.invalidate();
                                } else {
                                    GameActivity.playWrongAnswerSound();
                                }
                            }
                            return true;
                        }
                    }


                    if (this.getWidth() / 2.0f < x && this.getWidth() > x) {
                        if (0f < y && this.getHeight() / 2.0f > y) {
                            Log.d(TAG, "rec2");
                            if (!optionsManager.isCustom()) {
                                int[] imagesOnDiscardCard = deck[manager.getCurrentCard()].getListOfImages().clone();
                                boolean check = IntStream.of(imagesOnDiscardCard).anyMatch(m -> m == imagesOnCard[1]);
                                if (check) {
                                    GameActivity.playCorrectAnswerSound();
                                    manager.setNextCard();

                                    if (optionsManager.getIsExportSelected()) {
                                        exportToGallery();
                                    }

                                    discardView.invalidate();
                                    drawView.invalidate();
                                } else {
                                    GameActivity.playWrongAnswerSound();
                                }

                            } else {
                                Bitmap[] imagesOnDiscardCard = deck[manager.getCurrentCard()].getListOfBitmapImages();
                                boolean check = false;
                                for (int i = 0; i < numOfImages; i++) {
                                    if (BitmapEquals(bit[1], imagesOnDiscardCard[i])) {
                                        check = true;
                                        break;
                                    }
                                }
                                if (check) {
                                    GameActivity.playCorrectAnswerSound();
                                    manager.setNextCard();

                                    if (optionsManager.getIsExportSelected()) {
                                        exportToGallery();
                                    }

                                    discardView.invalidate();
                                    drawView.invalidate();
                                } else {
                                    GameActivity.playWrongAnswerSound();
                                }
                            }
                            return true;
                        }
                    }

                    if (0f < x && this.getWidth() / 2.0f > x) {
                        if (this.getHeight() / 2.0f < y && this.getHeight() > y) {
                            Log.d(TAG, "rec3");
                            if (!optionsManager.isCustom()) {
                                int[] imagesOnDiscardCard = deck[manager.getCurrentCard()].getListOfImages().clone();
                                boolean check = IntStream.of(imagesOnDiscardCard).anyMatch(m -> m == imagesOnCard[2]);
                                if (check) {
                                    GameActivity.playCorrectAnswerSound();
                                    manager.setNextCard();

                                    if (optionsManager.getIsExportSelected()) {
                                        exportToGallery();
                                    }

                                    discardView.invalidate();
                                    drawView.invalidate();
                                } else {
                                    GameActivity.playWrongAnswerSound();
                                }

                            } else {
                                Bitmap[] imagesOnDiscardCard = deck[manager.getCurrentCard()].getListOfBitmapImages();
                                boolean check = false;
                                for (int i = 0; i < numOfImages; i++) {
                                    if (BitmapEquals(bit[2], imagesOnDiscardCard[i])) {
                                        check = true;
                                        break;
                                    }
                                }
                                if (check) {
                                    GameActivity.playCorrectAnswerSound();
                                    manager.setNextCard();

                                    if (optionsManager.getIsExportSelected()) {
                                        exportToGallery();
                                    }

                                    discardView.invalidate();
                                    drawView.invalidate();
                                } else {
                                    GameActivity.playWrongAnswerSound();
                                }
                            }
                            return true;
                        }
                    }

                    if (this.getWidth() / 2.0f < x && this.getWidth() > x) {
                        if (this.getHeight() / 2.0f < y && this.getHeight() > y) {
                            Log.d(TAG, "rec4");
                            if (!optionsManager.isCustom()) {
                                int[] imagesOnDiscardCard = deck[manager.getCurrentCard()].getListOfImages().clone();
                                boolean check = IntStream.of(imagesOnDiscardCard).anyMatch(m -> m == imagesOnCard[3]);
                                if (check) {
                                    GameActivity.playCorrectAnswerSound();
                                    manager.setNextCard();

                                    if (optionsManager.getIsExportSelected()) {
                                        exportToGallery();
                                    }

                                    discardView.invalidate();
                                    drawView.invalidate();
                                } else {
                                    GameActivity.playWrongAnswerSound();
                                }

                            } else {
                                Bitmap[] imagesOnDiscardCard = deck[manager.getCurrentCard()].getListOfBitmapImages();
                                boolean check = false;
                                for (int i = 0; i < numOfImages; i++) {
                                    if (BitmapEquals(bit[3], imagesOnDiscardCard[i])) {
                                        check = true;
                                        break;
                                    }
                                }
                                if (check) {
                                    GameActivity.playCorrectAnswerSound();
                                    manager.setNextCard();

                                    if (optionsManager.getIsExportSelected()) {
                                        exportToGallery();
                                    }

                                    discardView.invalidate();
                                    drawView.invalidate();
                                } else {
                                    GameActivity.playWrongAnswerSound();
                                }
                            }
                            return true;
                        }
                    }

                }

                if (numOfImages == 6) {
                    if (0f < x && this.getWidth() / 3.0f > x) {
                        if (0 < y && this.getHeight() / 2.0f > y) {
                            Log.d(TAG, "rec1");
                            if (!optionsManager.isCustom()) {
                                int[] imagesOnDiscardCard = deck[manager.getCurrentCard()].getListOfImages().clone();
                                boolean check = IntStream.of(imagesOnDiscardCard).anyMatch(m -> m == imagesOnCard[0]);
                                if (check) {
                                    GameActivity.playCorrectAnswerSound();
                                    manager.setNextCard();

                                    if (optionsManager.getIsExportSelected()) {
                                        exportToGallery();
                                    }

                                    discardView.invalidate();
                                    drawView.invalidate();
                                } else {
                                    GameActivity.playWrongAnswerSound();
                                }

                            } else {
                                Bitmap[] imagesOnDiscardCard = deck[manager.getCurrentCard()].getListOfBitmapImages();
                                boolean check = false;
                                for (int i = 0; i < numOfImages; i++) {
                                    if (BitmapEquals(bit[0], imagesOnDiscardCard[i])) {
                                        check = true;
                                        break;
                                    }
                                }
                                if (check) {
                                    GameActivity.playCorrectAnswerSound();
                                    manager.setNextCard();

                                    if (optionsManager.getIsExportSelected()) {
                                        exportToGallery();
                                    }

                                    discardView.invalidate();
                                    drawView.invalidate();
                                } else {
                                    GameActivity.playWrongAnswerSound();
                                }
                            }
                            return true;
                        }
                    }

                    if (this.getWidth() / 3.0f < x && 2.0f * this.getWidth() / 3.0f > x) {
                        if (0 < y && this.getHeight() / 2.0f > y) {
                            Log.d(TAG, "rec2");
                            if (!optionsManager.isCustom()) {
                                int[] imagesOnDiscardCard = deck[manager.getCurrentCard()].getListOfImages().clone();
                                boolean check = IntStream.of(imagesOnDiscardCard).anyMatch(m -> m == imagesOnCard[1]);
                                if (check) {
                                    GameActivity.playCorrectAnswerSound();
                                    manager.setNextCard();

                                    if (optionsManager.getIsExportSelected()) {
                                        exportToGallery();
                                    }

                                    discardView.invalidate();
                                    drawView.invalidate();
                                } else {
                                    GameActivity.playWrongAnswerSound();
                                }

                            } else {
                                Bitmap[] imagesOnDiscardCard = deck[manager.getCurrentCard()].getListOfBitmapImages();
                                boolean check = false;
                                for (int i = 0; i < numOfImages; i++) {
                                    if (BitmapEquals(bit[1], imagesOnDiscardCard[i])) {
                                        check = true;
                                        break;
                                    }
                                }
                                if (check) {
                                    GameActivity.playCorrectAnswerSound();
                                    manager.setNextCard();

                                    if (optionsManager.getIsExportSelected()) {
                                        exportToGallery();
                                    }

                                    discardView.invalidate();
                                    drawView.invalidate();
                                } else {
                                    GameActivity.playWrongAnswerSound();
                                }
                            }
                            return true;
                        }
                    }

                    if (2.0f * this.getWidth() / 3.0f < x && this.getWidth() > x) {
                        if (0 < y && this.getHeight() / 2.0f > y) {
                            Log.d(TAG, "rec3");
                            if (!optionsManager.isCustom()) {
                                int[] imagesOnDiscardCard = deck[manager.getCurrentCard()].getListOfImages().clone();
                                boolean check = IntStream.of(imagesOnDiscardCard).anyMatch(m -> m == imagesOnCard[2]);
                                if (check) {
                                    GameActivity.playCorrectAnswerSound();
                                    manager.setNextCard();

                                    if (optionsManager.getIsExportSelected()) {
                                        exportToGallery();
                                    }

                                    discardView.invalidate();
                                    drawView.invalidate();
                                } else {
                                    GameActivity.playWrongAnswerSound();
                                }

                            } else {
                                Bitmap[] imagesOnDiscardCard = deck[manager.getCurrentCard()].getListOfBitmapImages();
                                boolean check = false;
                                for (int i = 0; i < numOfImages; i++) {
                                    if (BitmapEquals(bit[2], imagesOnDiscardCard[i])) {
                                        check = true;
                                        break;
                                    }
                                }
                                if (check) {
                                    GameActivity.playCorrectAnswerSound();
                                    manager.setNextCard();

                                    if (optionsManager.getIsExportSelected()) {
                                        exportToGallery();
                                    }

                                    discardView.invalidate();
                                    drawView.invalidate();
                                } else {
                                    GameActivity.playWrongAnswerSound();
                                }
                            }
                            return true;
                        }
                    }

                    if (0f < x && this.getWidth() / 3.0f > x) {
                        if (this.getHeight() / 2.0f < y && this.getHeight() > y) {
                            Log.d(TAG, "rec3");
                            if (!optionsManager.isCustom()) {
                                int[] imagesOnDiscardCard = deck[manager.getCurrentCard()].getListOfImages().clone();
                                boolean check = IntStream.of(imagesOnDiscardCard).anyMatch(m -> m == imagesOnCard[3]);
                                if (check) {
                                    GameActivity.playCorrectAnswerSound();
                                    manager.setNextCard();

                                    if (optionsManager.getIsExportSelected()) {
                                        exportToGallery();
                                    }

                                    discardView.invalidate();
                                    drawView.invalidate();
                                } else {
                                    GameActivity.playWrongAnswerSound();
                                }

                            } else {
                                Bitmap[] imagesOnDiscardCard = deck[manager.getCurrentCard()].getListOfBitmapImages();
                                boolean check = false;
                                for (int i = 0; i < numOfImages; i++) {
                                    if (BitmapEquals(bit[3], imagesOnDiscardCard[i])) {
                                        check = true;
                                        break;
                                    }
                                }
                                if (check) {
                                    GameActivity.playCorrectAnswerSound();
                                    manager.setNextCard();

                                    if (optionsManager.getIsExportSelected()) {
                                        exportToGallery();
                                    }

                                    discardView.invalidate();
                                    drawView.invalidate();
                                } else {
                                    GameActivity.playWrongAnswerSound();
                                }
                            }
                            return true;
                        }
                    }

                    if (this.getWidth() / 3.0f < x && 2.0f * this.getWidth() / 3.0f > x) {
                        if (this.getHeight() / 2.0f < y && this.getHeight() > y) {
                            Log.d(TAG, "rec3");
                            if (!optionsManager.isCustom()) {
                                int[] imagesOnDiscardCard = deck[manager.getCurrentCard()].getListOfImages().clone();
                                boolean check = IntStream.of(imagesOnDiscardCard).anyMatch(m -> m == imagesOnCard[4]);
                                if (check) {
                                    GameActivity.playCorrectAnswerSound();
                                    manager.setNextCard();

                                    if (optionsManager.getIsExportSelected()) {
                                        exportToGallery();
                                    }

                                    discardView.invalidate();
                                    drawView.invalidate();
                                } else {
                                    GameActivity.playWrongAnswerSound();
                                }

                            } else {
                                Bitmap[] imagesOnDiscardCard = deck[manager.getCurrentCard()].getListOfBitmapImages();
                                boolean check = false;
                                for (int i = 0; i < numOfImages; i++) {
                                    if (BitmapEquals(bit[4], imagesOnDiscardCard[i])) {
                                        check = true;
                                        break;
                                    }
                                }
                                if (check) {
                                    GameActivity.playCorrectAnswerSound();
                                    manager.setNextCard();

                                    if (optionsManager.getIsExportSelected()) {
                                        exportToGallery();
                                    }

                                    discardView.invalidate();
                                    drawView.invalidate();
                                } else {
                                    GameActivity.playWrongAnswerSound();
                                }
                            }
                            return true;
                        }
                    }

                    if (2.0f * this.getWidth() / 3.0f < x && this.getWidth() > x) {
                        if (this.getHeight() / 2.0f < y && this.getHeight() > y) {
                            Log.d(TAG, "rec3");
                            if (!optionsManager.isCustom()) {
                                int[] imagesOnDiscardCard = deck[manager.getCurrentCard()].getListOfImages().clone();
                                boolean check = IntStream.of(imagesOnDiscardCard).anyMatch(m -> m == imagesOnCard[5]);
                                if (check) {
                                    GameActivity.playCorrectAnswerSound();
                                    manager.setNextCard();

                                    if (optionsManager.getIsExportSelected()) {
                                        exportToGallery();
                                    }

                                    discardView.invalidate();
                                    drawView.invalidate();
                                } else {
                                    GameActivity.playWrongAnswerSound();
                                }

                            } else {
                                Bitmap[] imagesOnDiscardCard = deck[manager.getCurrentCard()].getListOfBitmapImages();
                                boolean check = false;
                                for (int i = 0; i < numOfImages; i++) {
                                    if (BitmapEquals(bit[5], imagesOnDiscardCard[i])) {
                                        check = true;
                                        break;
                                    }
                                }
                                if (check) {
                                    GameActivity.playCorrectAnswerSound();
                                    manager.setNextCard();

                                    if (optionsManager.getIsExportSelected()) {
                                        exportToGallery();
                                    }

                                    discardView.invalidate();
                                    drawView.invalidate();
                                } else {
                                    GameActivity.playWrongAnswerSound();
                                }
                            }
                            return true;
                        }
                    }
                }
            }
        }
        return value; //default actions will be taken if nothing is done
    }

    private boolean BitmapEquals(Bitmap bit, Bitmap bitmap) {
        ByteBuffer buffer1 = ByteBuffer.allocate(bit.getHeight() * bit.getRowBytes());
        bit.copyPixelsToBuffer(buffer1);

        ByteBuffer buffer2 = ByteBuffer.allocate(bitmap.getHeight() * bitmap.getRowBytes());
        bitmap.copyPixelsToBuffer(buffer2);

        return Arrays.equals(buffer1.array(), buffer2.array());
    }
    //https://developer.android.com/topic/performance/graphics/load-bitmap
    //calculateInSampleSize and decodeSampledBitmapFromResource came from this website
    //Used to fix a memory error, Images were too high of a resolution to store in memory
    //Had to scaled down to use it
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
                                            int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth,
                                                         int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }


    private static Bitmap drawText(String word, float textSize, int textColor) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(word) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(word, 0, baseline, paint);
        return image;
    }
}
