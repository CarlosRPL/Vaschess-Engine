package com.crud;
public class Queen extends Piece{
    public Queen (char color, int col, int lin){
        super(color,col,lin);
        type=Tipo.QUEEN;
        if (color=='w')
            image=getImage("/imagem/Wqueen");
        else
            image=getImage("/imagem/bqueen");
    }
    public boolean canMove(int targetcol,int targetlin){
        if (((targetcol==precol&&targetlin!=prelin)||(targetlin==prelin&&targetcol!=precol))) {
            
            if (quadradoValido(targetcol, targetlin)&&pieceAlemNaLinhaReta(targetcol, targetlin)==false)
                {return true;}    
        }
        if ((precol+prelin==targetcol+targetlin)||Math.abs(targetcol-precol)==Math.abs(targetlin-prelin)) {
            
            if (quadradoValido(targetcol, targetlin)&&pieceAlemDaDiagonal(targetcol, targetlin)==false
                &&targetcol!=precol&&targetlin!=prelin)
                {return true;}    
        }
        return false;}
}
