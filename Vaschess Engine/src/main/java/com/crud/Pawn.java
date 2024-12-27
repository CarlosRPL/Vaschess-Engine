package com.crud;
public class Pawn extends Piece {
    public int movevalue;
    public Pawn(char color, int col, int lin){
        super(color,col,lin);
        type= Tipo.PAWN;
        if (color=='w')
            image=getImage("/imagem/Wpawn");
        else
            image=getImage("/imagem/Bpawn");

    }
    public boolean canMove(int targetcol, int targetlin){
        movevalue = (this.color=='w')? -1 : 1;
        
        targetPiece=pieceColideswPiece(targetcol, targetlin);
        if (targetlin==prelin+movevalue&&targetcol==precol&&targetPiece==null) {
            return true;
        }
        if (targetlin==prelin+movevalue*2&&targetcol==precol&&targetPiece==null&&
        pieceAlemNaLinhaReta(targetcol, targetlin)==false&& moveu==false) {
            return true;
        }
        if (Math.abs(targetcol-precol)==1&& targetlin==prelin+movevalue&& targetPiece!=null&&
        targetPiece.color!=this.color) {
            return true;
        }
        if (Math.abs(targetcol-precol)==1&& targetlin==prelin+movevalue){
           for (Piece piece:Gampanel.simpieces){
              if (piece.col == targetcol && piece.lin==prelin&&piece.duascasas==true){
                  targetPiece=piece;
                  return true;
        }
      }
    }
        return false;
    }
}
