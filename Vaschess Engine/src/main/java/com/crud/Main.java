package com.crud;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame janela= new JFrame("Xadrez Vascante");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setResizable(false);
        Gampanel vasco= new Gampanel();
        janela.getContentPane().add(vasco);
        janela.pack();
        janela.setLocationRelativeTo(null);
        janela.setVisible(true);
        vasco.launchGame();
    }
}
