package ca.cmpt276.finddamatch.model;

import androidx.annotation.Nullable;

public class Key {

    private final int numOfImages;
    private final int numOfCards;

    public Key(int numOfImages, int numofCards) {
        this.numOfImages = numOfImages;
        this.numOfCards = numofCards;
    }

    @Override
    public int hashCode() {
        int result = numOfImages;
        result = 31 * result + numOfCards;
        return result;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj){
            return true;
        }
        if(!(obj instanceof  Key)) return false;
        Key key = (Key) obj;
        return numOfImages == key.numOfImages && numOfCards == key.numOfCards;
    }
}
