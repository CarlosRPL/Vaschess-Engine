package com.crud;   
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

    public class Piece implements Cloneable {
    public Tipo type;
    public BufferedImage image;
    public int x,y;
    public int col,lin, precol,prelin;
    public char color;
    public Piece targetPiece;
    public boolean moveu=false, duascasas=false;

    public Piece(char color, int col, int lin){
        this.color=color;
        this.col=col;
        this.lin=lin;   
        x = getX(col);
        y = getY(lin);
        precol = col;
        prelin=lin;
    }
public BufferedImage getImage(String imagePath) {
    try {
        String fullPath =  imagePath + ".png";
        System.out.println("Tentando carregar a imagem: " + fullPath);
        this.image = ImageIO.read(getClass().getResourceAsStream(fullPath));

        if (this.image == null) {
            throw new NullPointerException("Imagem não encontrada: " + fullPath);
        }
    } catch (IOException e) {
        System.out.println("Erro ao carregar a imagem: " + imagePath);
        e.printStackTrace();
    } catch (NullPointerException e) {
        System.out.println("Imagem não encontrada no caminho especificado: " + imagePath + ".png");
    }
    return this.image;
}
    public int getIndex(){
        for(int index=0;index<Gampanel.simpieces.size();index++){
            if (Gampanel.simpieces.get(index)== this)
                 {return index;}
            
        }
        return -1;
    }
    public int getX (int col){
        return col*Board.SQUARE_SIZE;
    }
    public int getY (int lin){
        return lin*Board.SQUARE_SIZE;
    }
    public int getCol(int x){
        return(x+Board.HALF_SQUARE_SIZE)/Board.SQUARE_SIZE;
    }
    public int getLin(int y){
        return (y+Board.HALF_SQUARE_SIZE)/Board.SQUARE_SIZE;
    }
    public void updatePosition(){
        if (type==Tipo.PAWN) {
           if (Math.abs(lin-prelin)==2){duascasas=true;}
        }
        Gampanel.updateMatrix(this);
        x=getX(col);
        y=getY(lin);
        precol=getCol(x);
        prelin=getLin(y);
        moveu=true;

    }
    public void resetPosition(){
        col=precol;
        lin=prelin;
        x=getX(col);
        y=getY(lin);
    }
    public boolean canMove(int targetcol,int targetlin){
        return false;
    }
    
    public boolean pieceAlemDaDiagonal(int targetcol, int targetlin) {
        int linIncrement, colIncrement;
        
        // 🔼 Movimentos para cima
        if (targetlin < prelin) {
            // 🔼⬅️ Para cima e esquerda
            linIncrement = -1;
            colIncrement = -1;
            for (int c = precol - 1; c > targetcol; c--) {
                int lin = prelin + linIncrement * Math.abs(c - precol);
                if (isPieceAt(c, lin)) return true;
            }
            
            // 🔼➡️ Para cima e direita
            linIncrement = -1;
            colIncrement = +1;
            for (int c = precol + 1; c < targetcol; c++) {
                int lin = prelin + linIncrement * Math.abs(c - precol);
                if (isPieceAt(c, lin)) return true;
            }
        }
    
        // 🔽 Movimentos para baixo
        if (targetlin > prelin) {
            // 🔽⬅️ Para baixo e esquerda
            linIncrement = +1;
            colIncrement = -1;
            for (int c = precol - 1; c > targetcol; c--) {
                int lin = prelin + linIncrement * Math.abs(c - precol);
                if (isPieceAt(c, lin)) return true;
            }
            
            // 🔽➡️ Para baixo e direita
            linIncrement = +1;
            colIncrement = +1;
            for (int c = precol + 1; c < targetcol; c++) {
                int lin = prelin + linIncrement * Math.abs(c - precol);
                if (isPieceAt(c, lin)) return true;
            }
        }
        
        return false;
    }
    
    private boolean isPieceAt(int col, int lin) {
        for (Piece piece : Gampanel.simpieces) {
            if (piece.col == col && piece.lin == lin) {
                targetPiece = piece;
                return true;
            }
        }
        return false;
    }
    public boolean pieceAlemNaLinhaReta(int targetcol, int targetlin){
        // indo pra esquerda
        for(int c=precol-1;c>targetcol;c--){
            for(Piece piece :Gampanel.simpieces){
                if(piece.col==c&&piece.lin==targetlin){
                    targetPiece=piece;
                    return true;
                }
            }
        }
        //indo pra direita
        for(int c=precol+1;c<targetcol;c++){
            for(Piece piece :Gampanel.simpieces){
                if(piece.col==c&&piece.lin==targetlin){
                    targetPiece=piece;
                    return true;
                }
            }
        }
        //indo pra baixo
        for(int c=prelin-1;c>targetlin;c--){
            for(Piece piece :Gampanel.simpieces){
                if(piece.lin==c&&piece.col==targetcol){
                    targetPiece=piece;
                    return true;
                }
            }
        }
        //indo pra cima
        for(int c=prelin+1;c<targetlin;c++){
            for(Piece piece :Gampanel.simpieces){
                if(piece.lin==c&&piece.col==targetcol){
                    targetPiece=piece;
                    return true;
                }
            }
        }
        return false;
    }
    public boolean quadradoValido(int targetcol, int targetlin){
        targetPiece=pieceColideswPiece(targetcol, targetlin);
        if(targetPiece==null){
            return true;
        }else{
            if (targetPiece.color!=this.color) {
                return true;
            }else targetPiece = null;
        }
    return false; 
   }
    public Piece pieceColideswPiece(int targetcol, int targetlin){
        for(Piece piece: Gampanel.simpieces){
            if (piece.col== targetcol&&piece.lin==targetlin&&piece!=this){
                return piece;
            }
            
        }
        return null;
    }
    public void draw(Graphics2D g2){
        g2.drawImage(image, x, y, Board.SQUARE_SIZE,Board.SQUARE_SIZE, null);
        
    }
      @Override
    public Piece clone() {
        try {
            return (Piece) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Isso nunca deve acontecer, já que implementamos Cloneable
        }
    }
}

