/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projectbattleship;


public class Ship {
    private String name;
    private int size;
    private boolean h_pos;

    public Ship(String name, int size) {
        this.name = name;
        this.size = size;
        this.h_pos = true;
    }

    public String getName() { return name;}
    public int getSize() { return size; }
    public boolean isHorizontal() { return h_pos; }
    public void rotar() { h_pos = !h_pos; }
}

