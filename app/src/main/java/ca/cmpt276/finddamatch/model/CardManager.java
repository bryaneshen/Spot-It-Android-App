package ca.cmpt276.finddamatch.model;

import android.content.SharedPreferences;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Random;

/**
 * Singleton class handles the deck array which is an array of Card objects. Stores the number of cards
 * in the deck as an integer (and initializes them). Stores the current card and the imageSet which is currently chosen
 * either by default or by the user in the OptionsActivity.
 */
public class CardManager {
    private static int numOfCards;
    private static Card[] deck;
    private static int currentCard;
    private static CardManager single_instance = null;
    private static int[] imageSet;
    private static int numOfImages;
    private static Bitmap[] bitmapSet;

    public static int getNumOfImages() {
        return numOfImages;
    }

    public static void setNumOfImages(int numOfImages) {
        CardManager.numOfImages = numOfImages;
    }

    private CardManager() {
        currentCard = 0;
        imageSet = new int[numOfCards];
        deck = new Card[numOfCards];
        for(int i = 0; i < numOfCards; i++){ //initialize deck indexes
            deck[i] = new Card();
        }
    }
    
    public static CardManager getInstance() {
        if (single_instance == null) {
            single_instance = new CardManager();
        }
        return single_instance;
    }

    public static int[] getImageSet() {
        return imageSet;
    }

    //maybe we should delete getDeck bad for encapsulation.
    public static Card[] getDeck() {
        return deck;
    }

    public static void setDeck(Card[] deck) {
        CardManager.deck = deck.clone();
    }
    // Fisher–Yates shuffle Algorithm
    //inspired by https://www.geeksforgeeks.org/shuffle-a-given-array-using-fisher-yates-shuffle-algorithm/
    public void shuffleDeck(int n){
        Random random = new Random();

        for(int i = n-1; i > 0; i--){
            int currentRandIndex = random.nextInt(i+1);
            Card tempValue = deck[i];
            deck[i] = deck[currentRandIndex];
            deck[currentRandIndex] = tempValue;

        }
    }

    public static void setNumOfCards(int numOfCards) {
        CardManager.numOfCards = numOfCards;
    }

    public static int getNumOfCards() {
        return numOfCards;
    }

    public static int getCurrentCard() {
        return currentCard;
    }

    public static void setNextCard(){
        CardManager.currentCard = currentCard+1;
    }

    public static void setCurrentCard(int currentCard) {
        CardManager.currentCard = currentCard;
    }

    public static void setImageSet(int[] imageSet) {
        CardManager.imageSet = imageSet.clone();
    }

    public static void setBitmapSet(Bitmap[] bitmapSet){
        CardManager.imageSet = null;
        CardManager.bitmapSet = bitmapSet;
    }
}
