package com.crud;

public class Move {
    public Tipo type;
    public int score;
    public int col, lin, prelin, precol;

    public Move(Tipo type, int col, int lin, int precol, int prelin) {
        this.type = type;
        this.col = col;
        this.lin = lin;
        this.prelin = prelin;
        this.precol = precol;
    }
}

