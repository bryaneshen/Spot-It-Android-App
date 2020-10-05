package ca.cmpt276.finddamatch.model;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class OptionsManager {
    private static int numOfImagesInCard;
    private static int numOfCards;
    private static int numOfImagesTotal;
    private static int[] currImageSet;
    private static Bitmap[] currBitmapSet;
    private static ArrayList<Bitmap> myPhotosBitmapsList;
    private static boolean isWord;
    private static String[] wordSet;
    private static boolean isCustom;
    private static boolean isExportSelected;
    private static String difficulty;


    public static ArrayList<Bitmap> getMyPhotosBitmapsList() {
        return myPhotosBitmapsList;
    }


    public static void setWord(boolean word) {
        isWord = word;
    }

    public static boolean isCustom() {
        return isCustom;
    }

    public static void setCustom(boolean custom) {
        isCustom = custom;
    }

    private static OptionsManager single_instance = null;

    private OptionsManager() {
        numOfImagesInCard = 0;
        numOfCards = 0;
        numOfImagesTotal = 0;
        myPhotosBitmapsList = new ArrayList<>();
    }

    public static OptionsManager getInstance() {
        if (single_instance == null) {
            single_instance = new OptionsManager();
        }
        return single_instance;
    }

    public static int getNumOfImagesInCard() {
        return numOfImagesInCard;
    }

    public static int getNumOfCards() {
        return numOfCards;
    }

    public static void setNumOfImagesInCard(int numOfImagesInCard) {
        OptionsManager.numOfImagesInCard = numOfImagesInCard;
    }

    public static void setNumOfCards(int numOfCards) {
        OptionsManager.numOfCards = numOfCards;
    }

    public static void setNumOfImagesTotal(int numOfImagesInCard) {
        if (numOfImagesInCard == 3) {
            numOfImagesTotal = 7;
        } else if (numOfImagesInCard == 4) {
            numOfImagesTotal = 13;
        } else if (numOfImagesInCard == 6) {
            numOfImagesTotal = 31;
        }
    }

    public static int[] getCurrImageSet() { //add currentImageSet == null to see if Bitmap choice was made in CustomView
        return currImageSet;
    }

    public static Bitmap[] getCurrBitmapSet() {
        return currBitmapSet;
    }

    public static void setCurrBitmapSet(Bitmap[] currBitmapSet) {
        OptionsManager.currBitmapSet = currBitmapSet;
    }

    public static void setCurrImageSet(int[] imageSet) {
        currImageSet = imageSet;
    }

    public static boolean isWord() {
        return isWord;
    }

    public static void setIsWord(boolean word) {
        isWord = word;
    }

    public static String[] getWordSet() {
        return wordSet;
    }

    public static void setWordSet(String[] wordSet) {
        OptionsManager.wordSet = wordSet;
    }

    public static boolean getIsExportSelected() {
        return isExportSelected;
    }

    public static void setIsExportSelected(boolean exportSelected) {
        isExportSelected = exportSelected;
    }

    public static String getDifficulty() {
        return difficulty;
    }

    public static void setDifficulty(String difficulty) {
        OptionsManager.difficulty = difficulty;
    }
}
