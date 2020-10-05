package ca.cmpt276.finddamatch.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ca.cmpt276.finddamatch.R;

/**
 * Singleton class that manages all the high scores for HighScoresActivity. Can generate default
 * top five high scores; user populates the high scores by playing the game and beating the times.
 */
public class HighScoreManager {
    private HighScore[] highScore = new HighScore[5];
    private int amountOfHighScores;
    private ArrayList<String> listOfHighScores  = new ArrayList<>();
    HighScoreManager() {
        for(int i = 0; i < 5; i++){
            highScore[i] = new HighScore();
        }
        SetDefaultHighScores();
    }
    private void CompareToArray(String name, String date, int time, int numOfCards, int numOfImages, boolean isWord){
        if(amountOfHighScores == 5){
            if(time > highScore[amountOfHighScores-1].getTime()){
                return;
            }
        }
        int i = amountOfHighScores-1;
        while(i > 0 &&  time < highScore[i-1].getTime()){
            highScore[i].setTime(highScore[i-1].getTime());
            highScore[i].setName(highScore[i-1].getName());
            highScore[i].setDate(highScore[i-1].getDate());
            highScore[i].setNumOfCards(highScore[i-1].getNumOfCards());
            highScore[i].setNumOfImages(highScore[i-1].getNumOfImages());
            highScore[i].setWord(highScore[i-1].isWord());
            i--;
        }
        highScore[i].setTime(time);
        highScore[i].setName(name);
        highScore[i].setDate(date);
        highScore[i].setNumOfCards(numOfCards);
        highScore[i].setNumOfImages(numOfImages);
        highScore[i].setWord(isWord);
    }

    public void AddToArray(String name, String date, int time, int numOfCards, int numOfImages, boolean isWord){

        if(name != null) {
            if (this.amountOfHighScores == 5) {
                CompareToArray(name, date, time, numOfCards, numOfImages, isWord);
            } else if (amountOfHighScores < 5) {
                highScore[amountOfHighScores].setName(name);
                highScore[amountOfHighScores].setDate(date);
                highScore[amountOfHighScores].setTime(time);
                highScore[amountOfHighScores].setNumOfCards(numOfCards);
                highScore[amountOfHighScores].setNumOfImages(numOfImages);
                highScore[amountOfHighScores].setWord(isWord);
                this.amountOfHighScores++;
            }
        }
    }

    public ArrayList<String> GenerateStringArrayList(Context currentContext){
        listOfHighScores.clear();
        int time;
        String date;
        String name;
        for(int i = 0; i < amountOfHighScores; i++){

            time = highScore[i].getTime();
            date = highScore[i].getDate();
            name = highScore[i].getName();
            if(name != null) { // if username is not null add it to the list
                listOfHighScores.add(currentContext.getString(R.string.highscore_list_element, time, name, date));
            }
        }

        return listOfHighScores;
    }

    public ArrayList<String> ClearStringArrayList(){
        listOfHighScores.clear();

        for(int i = 0; i < amountOfHighScores; i++){
            highScore[i].setTime(0);
            highScore[i].setDate("");
            highScore[i].setName("");
        }
        amountOfHighScores = 0;
        return listOfHighScores;
    }

    public void SetDefaultHighScores(){

        if(amountOfHighScores == 0){
            int default_FirstHighScoreTime = 70;
            String default_FirstUserName = "User1";
            String default_FirstDate = "12-01-2020";
            this.AddToArray(default_FirstUserName, default_FirstDate, default_FirstHighScoreTime, 3, 7, false);

            int default_SecondHighScoreTime = 80;
            String default_SecondUserName = "User2";
            String default_SecondDate = "16-01-2020";
            this.AddToArray(default_SecondUserName, default_SecondDate, default_SecondHighScoreTime,  3, 7, false);

            int default_ThirdHighScoreTime = 90;
            String default_ThirdUserName = "User3";
            String default_ThirdDate = "19-06-2020";
            this.AddToArray(default_ThirdUserName, default_ThirdDate, default_ThirdHighScoreTime,  3, 7, false);


            int default_FourthHighScoreTime = 100;
            String default_FourthUserName = "User4";
            String default_FourthDate = "09-07-2020";
            this.AddToArray(default_FourthUserName, default_FourthDate, default_FourthHighScoreTime,  3, 7, false);


            int default_FifthHighScoreTime = 105;
            String default_FifthUserName = "User5";
            String default_FifthDate = "29-07-2020";
            this.AddToArray(default_FifthUserName, default_FifthDate, default_FifthHighScoreTime,  3, 7, false);
        }
    }
}
