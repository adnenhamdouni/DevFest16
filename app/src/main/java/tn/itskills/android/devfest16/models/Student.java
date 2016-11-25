package tn.itskills.android.devfest16.models;

import java.io.Serializable;

/**
 * Created by adnenhamdouni on 25/11/2016.
 */

public class Student implements Serializable{

    public String name;

    public Student() {
    }

    public Student(String name) {
        this.name = name;
    }
}
