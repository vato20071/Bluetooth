package com.example.vato.bluetoothcontrol;

/**
 * Created by vato on 12/10/16.
 */
public class Finger {

    public int id;
    public int value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Finger(int id, int value) {

        this.id = id;
        this.value = value;
    }
}
