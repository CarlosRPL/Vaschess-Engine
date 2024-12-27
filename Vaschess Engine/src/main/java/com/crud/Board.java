package com.crud;

import java.awt.Color;
import java.awt.Graphics2D;

public class Board {
    final int MAX_COL=8;
    final int MAX_LIN=8;
    public static final int SQUARE_SIZE=75;
    public static final int HALF_SQUARE_SIZE= SQUARE_SIZE/2;

    public void draw (Graphics2D g2){
        boolean xadra= true;
        for(int col=0;col<MAX_COL;col++){
            if (xadra) xadra=false;
            else xadra=true;
            for (int linha=0; linha<MAX_LIN;linha++) {
                if(xadra){
                    g2.setColor(new Color(75, 0, 130));
                    xadra=false;
                }else{
                    g2.setColor(new Color(230, 190, 255));
                    xadra=true;
                }
                g2.fillRect(col*SQUARE_SIZE, linha*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
    }
}
