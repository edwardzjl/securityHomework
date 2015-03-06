package com.edwardlol.autoanswertest;

import java.util.LinkedList;

/**
 * Created by edwardlol on 15/3/6.
 */
class Queue {
    private LinkedList<Character> list = new LinkedList<>();
    public void put(Character c) {
        list.addFirst(c);
    }
    public Character get() {
        return list.removeLast();
    }
    public boolean isEmpty() {
        return list.isEmpty();
    }
    public String output() {
        return list.toString();
    }
}