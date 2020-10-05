package ca.cmpt276.finddamatch.views;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import ca.cmpt276.finddamatch.model.Card;
import ca.cmpt276.finddamatch.model.CardManager;
import ca.cmpt276.finddamatch.model.OptionsManager;

/**
 * Custom View class which manages the operations and the creation of a View with Custom Attributes and its
 * images which relate to the Discard Pile in the GameActivity. Helps output images to the screen via the CardManager.
 */
public class DiscardPileView extends View {

    private Paint mPaint = new Paint();
    private Rect[] rectangle = new Rect[Card.getNumOfImages()];
    private DrawPileView drawView;
    private CardManager manager = CardManager.getInstance();
    private OptionsManager optionsManager = OptionsManager.getInstance();
    private int numOFCards = optionsManager.getNumOfCards();
    private int numOfImages = optionsManager.getNumOfImagesInCard();
    private int[] imagesOnCard = new int[numOfImages];
    private Bitmap[] img = new Bitmap[numOfImages];
    private Card[] deck = CardManager.getDeck();
    private boolean[] isWord = new boolean[numOfImages];
    private String[] wordsOnCard = new String[numOfImages];
    private boolean[] isRotate = new boolean[numOfImages];
    private boolean[] isSmall = new boolean[numOfImages];

    public DiscardPileView(Context context) {
        super(context);

        init(null);
    }

    public DiscardPileView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public DiscardPileView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    private void init(@Nullable AttributeSet set) {
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

    public void setDrawView(DrawPileView drawView) {
        this.drawView = drawView;

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

            rectangle[2].left = this.getWidth() / 2;
            rectangle[2].top = this.getHeight() / 2;
            rectangle[2].right = this.getWidth();
            rectangle[2].bottom = this.getHeight();

            rectangle[1].left = 0;
            rectangle[1].top = this.getHeight() / 2;
            rectangle[1].right = this.getWidth() / 2;
            rectangle[1].bottom = this.getHeight();

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
        System.out.println(imagesOnCard.length);
        if (!optionsManager.isCustom()) {
            imagesOnCard = deck[manager.getCurrentCard()].getListOfImages().clone();
            if (optionsManager.isWord()) {
                isWord = deck[manager.getCurrentCard()].getIsWord().clone();
            }
            if(optionsManager.getDifficulty().equals("Medium")) {
                isRotate = deck[manager.getCurrentCard()].getIsRotate().clone();
            }
            wordsOnCard = deck[manager.getCurrentCard()].getListOfWords().clone();
            for (int i = 0; i < numOfImages; i++) {
                if(optionsManager.getDifficulty().equals("Easy")){
                    if (optionsManager.isWord()) {
                        if (isWord[i]) {
                            img[i] = drawText(wordsOnCard[i], 16.0f, 0xffffffff);
                        }
                        else {
                            img[i] =  decodeSampledBitmapFromResource(getResources(), imagesOnCard[i], rectangle[i].width(), rectangle[i].height());
                        }
                    }
                    else {
                        img[i] =  decodeSampledBitmapFromResource(getResources(), imagesOnCard[i], rectangle[i].width(), rectangle[i].height());
                    }
                    if(isRotate[i]){
                        img[i] = rotateBitmap(img[i], 180);
                    }
                }
                else if(optionsManager.getDifficulty().equals("Medium")){
                    if (optionsManager.isWord()) {
                        if (isWord[i]) {
                            img[i] = drawText(wordsOnCard[i], 16.0f, 0xffffffff);
                        }
                        else {
                            img[i] =  decodeSampledBitmapFromResource(getResources(), imagesOnCard[i], rectangle[i].width(), rectangle[i].height());
                        }
                    }
                    else {
                        img[i] =  decodeSampledBitmapFromResource(getResources(), imagesOnCard[i], rectangle[i].width(), rectangle[i].height());
                    }
                    if(isRotate[i]){
                        img[i] = rotateBitmap(img[i], 180);
                    }
                }
                else if(optionsManager.getDifficulty().equals("Hard")){
                    if (optionsManager.isWord()) {
                        if (isWord[i]) {
                            img[i] = drawText(wordsOnCard[i], 16.0f, 0xffffffff);
                        }
                        else {
                            img[i] =  decodeSampledBitmapFromResource(getResources(), imagesOnCard[i], rectangle[i].width(), rectangle[i].height());
                        }
                    }
                    else {
                        img[i] =  decodeSampledBitmapFromResource(getResources(), imagesOnCard[i], rectangle[i].width(), rectangle[i].height());
                    }
                    if(isRotate[i]){
                        img[i] = rotateBitmap(img[i], 180);
                    }
                }
            }

        } else {
            Bitmap[] bit = deck[manager.getCurrentCard()].getListOfBitmapImages();
            if (optionsManager.getDifficulty().equals("Medium") ||optionsManager.getDifficulty().equals("Hard")  ) {
                isRotate = deck[manager.getCurrentCard()].getIsRotate().clone();
            }
            for (int i = 0; i < numOfImages; i++) {
                img[i] = bit[i];
                if(isRotate[i]){
                    img[i] = rotateBitmap(img[i], 180);
                }
            }
        }
        for (int i = 0; i < numOfImages; i++) {
            if (img[i] != null) {
                if (optionsManager.getDifficulty().equals("Medium")) {

                    isRotate = deck[manager.getCurrentCard()].getIsRotate().clone();
                    canvas.drawBitmap(img[i], null, rectangle[i], null);
                } else if (optionsManager.getDifficulty().equals("Hard")) {

                    isRotate = deck[manager.getCurrentCard()].getIsRotate().clone();
                    isSmall = deck[manager.getCurrentCard()].getIsSmall().clone();

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

    private Bitmap rotateBitmap(Bitmap source, float angle){
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
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
