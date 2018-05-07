package com.example.washer.client.washerclient;

/**
 * Created by Victory on 2014-12-27.
 */
public class Contact {

    int idx;
    String name;

    public Contact(){

    }
    public Contact(int idx, String name){
        this.idx = idx;
        this.name = name;
    }

    public int getIdx(){
        return this.idx;
    }

    public void setIdx(int idx){
        this.idx = idx;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }
}