package com.example.libor.lt1_danniel_isiah_libor;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by libor on 1/31/2018.
 */

public class dogInfo implements Serializable {
    public ArrayList<String> getDogGender()
    {
        return dogGender;
    }
    public void setGender (ArrayList<String> dogGender)
    {
        this.dogGender = dogGender;
    }
    private ArrayList<String> dogGender;

    public ArrayList<String> getDogBreed()
    {
        return dogBreed;
    }
    public void setBreed (ArrayList<String> dogBreed)
    {
        this.dogBreed = dogBreed;
    }
    private ArrayList<String> dogBreed;
}
