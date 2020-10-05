package ca.cmpt276.finddamatch.model;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Card class which holds a final integer variable to determine the number of images on a card.
 * Holds an array of integers to store the locations of the images.
 */

public class Card {
    private static int numOfImages;
    private int[] listOfImages;
    private String[] listOfWords;
    private boolean[] isWord;
    private Bitmap[] listOfBitmapImages; //for when images do not come from the /res file
    private boolean[] isRotate;
    private boolean[] isSmall;

    public static int getNumOfImages() {
        return numOfImages;
    }

    public void setNumOfImages(int numOfImages) {
        Card.numOfImages = numOfImages;
    }

    public int[] getListOfImages() {
        return listOfImages;
    }

    public void setListOfImages(int[] listOfImages) {
        this.listOfImages = listOfImages.clone();
    }

    public String[] getListOfWords() {
        return listOfWords;
    }

    public void setListOfWords(String[] listOfWords) {
        this.listOfWords = listOfWords.clone();
    }

    public boolean[] getIsWord() {
        return isWord;
    }

    public void setIsWord(boolean[] isWord) {
        this.isWord = isWord;
    }

    public Bitmap[] getListOfBitmapImages() {
        return listOfBitmapImages;
    }

    public void setListOfBitmapImages(Bitmap[] listOfBitmapImages) {
        this.listOfBitmapImages = listOfBitmapImages.clone();
    }

    public boolean[] getIsRotate() {
        return isRotate;
    }

    public void setIsRotate(boolean[] isRotate) {
        this.isRotate = isRotate;
    }

    public boolean[] getIsSmall() {
        return isSmall;
    }

    public void setIsSmall(boolean[] isSmall) {
        this.isSmall = isSmall;
    }
}
