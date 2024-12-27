package com.crud;
public class Bishop extends Piece {
    public Bishop (char color, int col, int lin){
        super(color,col,lin);
        type=Tipo.BISHOP;
        if (color=='w')
            image=getImage("/imagem/Wbispo");
        else
            image=getImage("/imagem/bbispo");
    }
    public boolean canMove(int targetcol,int targetlin){
        if ((precol+prelin==targetcol+targetlin)||Math.abs(targetcol-precol)==Math.abs(targetlin-prelin)) {
            
            if (quadradoValido(targetcol, targetlin)&&pieceAlemDaDiagonal(targetcol, targetlin)==false
      &&targetcol!=precol&&targetlin!=prelin)
                {return true;}    
        }
        return false;}
  
}
