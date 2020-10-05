package ca.cmpt276.finddamatch.model;

import java.util.HashMap;
import java.util.Map;

public class HighScoreTable {

    private static HighScoreTable single_instance = null;
    private static Map<Key, HighScoreManager> map = new HashMap<Key, HighScoreManager>();

    public static HighScoreTable getInstance() {
        if (single_instance == null) {
            single_instance = new HighScoreTable();
        }
        return single_instance;
    }

    public HighScoreManager getValue(int numOfImages, int numofCards){
        if(map.containsKey(new Key(numOfImages, numofCards))== false){
            HighScoreManager manager = new HighScoreManager();
            map.put(new Key(numOfImages, numofCards),manager);
            manager = map.get(new Key(numOfImages, numofCards));
            return manager;
        }
        else{
            HighScoreManager  manager= map.get(new Key(numOfImages, numofCards));
            return manager;
        }
    }

    public Map<Key, HighScoreManager> getMap() {
        return map;
    }

    public void setMap(Map<Key, HighScoreManager> map) {
        HighScoreTable.map = map;
    }
}
