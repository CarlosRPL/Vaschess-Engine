package com.crud;
import java.util.*;

public class Bot{
    public char color='b';
     


  private List<Move> getAllValidMoves(){
       List<Move> moves= new ArrayList<>();
        for (Piece piece : Gampanel.pieces)  
            if(piece.color==this.color)
              for (int col=0; col<8;col++)
                  for(int lin=0; lin<8;lin++)
                      if (piece.canMove(col,lin))
                          moves.add(new Move(piece.type, col, lin, piece.precol, piece.prelin));
     return moves;
    }
  public Move getTrueMove(){
    return new Move(Tipo.PAWN,4,3,4,1);
  }
}
