package com.crud;

public class King extends Piece{
    public King (char color, int col, int lin){
        super(color,col,lin);
      type= Tipo.KING;  
      if (color=='w')
            image=getImage("/imagem/WWKing");
        else
            image=getImage("/imagem/bbking");
    }
    public boolean canMove(int targetcol,int targetlin){
        if (((Math.abs(targetcol-precol)+Math.abs(targetlin-prelin))==1)||
        (Math.abs(targetcol-precol)*Math.abs(targetlin-prelin)==1)) {
            
            if (quadradoValido(targetcol, targetlin))
                {return true;}    
        }
        if (moveu==false){
            //rook pra direita
           if (targetcol==precol-2 && targetlin==prelin && pieceAlemNaLinhaReta(targetcol, targetlin)==false) {
              for(Piece piece: Gampanel.simpieces){
                  if(piece.col == precol-3&& piece.lin==prelin&&piece.moveu==false){
                      Gampanel.castlingP=piece;
                      return true;
                   }
               }
          }if ( targetcol==precol+2 && targetlin==prelin && pieceAlemNaLinhaReta(targetcol, targetlin)==false) {
             Piece p[]=new Piece[2];
                for (Piece piece : Gampanel.simpieces){
                    if (piece.col==precol+3&&piece.lin==targetlin) {
                        p[0]=piece;
                    }    
                    if (piece.col==precol+4&&piece.lin==targetlin) {
                        p[1]=piece;
                    }
                    if (p[0]==null&& p[1]!=null &&p[1].moveu==false) {
                        Gampanel.castlingP= p[1];
                        return true;
                    }
               }
            
          }
      }
        return false;}

}
