package com.crud;
public class Rook extends Piece{
    public Rook(char color, int col, int lin){
        super(color,col,lin);
        type = Tipo.ROOK;
        if (color=='w')
            image=getImage("/imagem/WRook");
        else
            image=getImage("/imagem/brook");
    }
    public boolean canMove(int targetcol, int targetlin){
            if (((targetcol==precol&&targetlin!=prelin)||(targetlin==prelin&&targetcol!=precol))) {
                if ((quadradoValido(targetcol, targetlin)&&pieceAlemNaLinhaReta(targetcol, targetlin)==false)) {
                    return true;
                }
            }
        return false;
    }
}
