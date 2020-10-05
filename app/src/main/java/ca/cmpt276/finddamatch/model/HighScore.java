package ca.cmpt276.finddamatch.model;

/**
 * High score class to be able to get and set the name, date and time of the user's game attempt.
 */
public class HighScore {
    private String name;
    private String date;
    private int time;
    private int numOfCards;
    private int numOfImages;
    private boolean isWord;

    public HighScore() {
        name = "";
        date = "";
        time = 0;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public int getTime() {
        return time;
    }

    public void setName(String name) {
        if(this.name == null){
            this.name = "";
        }
        this.name = name;
    }

    public void setDate(String currentDate) {
        date = currentDate;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getNumOfCards() {
        return numOfCards;
    }

    public void setNumOfCards(int numOfCards) {
        this.numOfCards = numOfCards;
    }

    public int getNumOfImages() {
        return numOfImages;
    }

    public void setNumOfImages(int numOfImages) {
        this.numOfImages = numOfImages;
    }

    public boolean isWord() {
        return isWord;
    }

    public void setWord(boolean word) {
        isWord = word;
    }
}
