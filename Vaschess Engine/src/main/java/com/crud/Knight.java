package com.crud;
public class Knight extends Piece {
    public Knight(char color, int col, int lin){
        super(color,col,lin);
      type=Tipo.KNIGHT;  
      if (color=='w')
            image=getImage("/imagem/Wknight");
        else
            image=getImage("/imagem/bknight");
    }
    public boolean canMove(int targetcol, int targetlin){
        if (Math.abs(targetcol-precol)*Math.abs(targetlin-prelin)==2) {
            if (quadradoValido(targetcol, targetlin)) {
                return true;
            }
            
        }
        return false;
    }
}
