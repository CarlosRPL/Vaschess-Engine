package com.crud;
import java.util.Iterator;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.JPanel;

public class Gampanel extends JPanel implements Runnable {
    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;
    final int FPS = 60;
    public char currentCollor = 'w';
    Thread gameThread;
    Board board = new Board();
    Mouse mouse = new Mouse();
    
    // As peças
    public static ArrayList<Piece> pieces = new ArrayList<>();
    public static ArrayList<Piece> simpieces = new ArrayList<>();
    ArrayList<Piece> promoPieces = new ArrayList<>();
    Piece activeP;
    boolean movimentovalido = false, promotion = false;
    public static Piece castlingP;
    public Piece wking = new King('w', 3, 7), bking = new King('b', 3, 0);
    
    public String winner = "";
    public boolean gameEnded = false;

    public Gampanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.black);
        // Adicionando o movimento do mouse na tela
        addMouseMotionListener(mouse);
        addMouseListener(mouse);
        setPieces();
        copyPieces(pieces, simpieces);
    }

    public void launchGame() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void trocarOsTurnos() {
        if (currentCollor == 'w') {
            currentCollor = 'b';
            for (Piece piece : pieces) {
                if (piece.color == 'b') piece.duascasas = false;
            }
        } else {
            currentCollor = 'w';
            for (Piece piece : pieces) {
                if (piece.color == 'b') piece.duascasas = false;
            }
        }
        activeP = null;
    }

    public void checkCastling() {
        if (castlingP != null) {
            if (castlingP.col == 0) {
                castlingP.col += 2;
            }
            if (castlingP.col == 7) {
                castlingP.col -= 3;
            }
            castlingP.x = castlingP.getX(castlingP.col);
        }
    }

    public boolean canPromote() {
        if (activeP.type == Tipo.PAWN) {
            if ((currentCollor == 'w' && activeP.lin == 0) || (currentCollor == 'b' && activeP.lin == 7)) {
                promoPieces.clear();
                promoPieces.add(new Rook(currentCollor, 2, 4));
                promoPieces.add(new Knight(currentCollor, 3, 4));
                promoPieces.add(new Queen(currentCollor, 4, 4));
                promoPieces.add(new Bishop(currentCollor, 5, 4));
                return true;
            }
        }
        return false;
    }

    public boolean isIllegal() {
        if (currentCollor == 'w') {
            for (Piece piece : simpieces) {
                if (piece != wking && piece.color != wking.color && piece.canMove(wking.col, wking.lin))
                    return true;
            }
        }
        if (currentCollor == 'b') {
            for (Piece piece : simpieces) {
                if (piece != bking && piece.color != bking.color && piece.canMove(bking.col, bking.lin)) {
                    return true;
                }
            }
        }
        return false;
    }
private synchronized boolean hasValidMoves(boolean checkIfKingIsSafe) {
    synchronized (simpieces) {
        for (Piece piece : new ArrayList<>(simpieces)) {
            if (piece.color == currentCollor) {
                for (int col = 0; col < 8; col++) {
                    for (int lin = 0; lin < 8; lin++) {
                        if (piece.canMove(col, lin)) {
                            // Salva o estado original
                            int originalCol = piece.col;
                            int originalLin = piece.lin;
                            Piece originalTarget = piece.targetPiece;

                            // Simula o movimento
                            piece.col = col;
                            piece.lin = lin;
                            if (originalTarget != null) simpieces.remove(originalTarget);

                            // Verifica se o movimento é seguro
                            boolean isSafe = !checkIfKingIsSafe || !isIllegal();

                            // Restaura o estado original
                            piece.col = originalCol;
                            piece.lin = originalLin;
                            if (originalTarget != null) simpieces.add(originalTarget);

                            if (isSafe) return true;
                        }
                    }
                }
            }
        }
    }
    return false;
}



    public boolean isCheckmate() {
        return isIllegal() && !hasValidMoves(true);
    }

    public boolean isStalemate() {
        return !isIllegal() && !hasValidMoves(true);
    }

    public void setPieces() {
        wking = null; bking = null; 
        wking = new King('w', 3, 7); bking = new King('b', 3, 0);
        for (int i = 0; i < 8; i++) {
            pieces.add(new Pawn('w', i, 6));
        }
        pieces.add(new Rook('w', 0, 7));
        pieces.add(new Rook('w', 7, 7));
        pieces.add(new Knight('w', 1, 7));
        pieces.add(new Knight('w', 6, 7));
        pieces.add(new Bishop('w', 2, 7));
        pieces.add(new Bishop('w', 5, 7));
        pieces.add(new Queen('w', 4, 7));
        pieces.add(wking);

        // As pretas agora
        for (int i = 0; i < 8; i++) {
            pieces.add(new Pawn('b', i, 1));
        }
        pieces.add(new Rook('b', 0, 0));
        pieces.add(new Rook('b', 7, 0));
        pieces.add(new Knight('b', 1, 0));
        pieces.add(new Knight('b', 6, 0));
        pieces.add(new Bishop('b', 2, 0));
        pieces.add(new Bishop('b', 5, 0));
        pieces.add(new Queen('b', 4, 0));
        pieces.add(bking);
    }

    private void copyPieces(ArrayList<Piece> source, ArrayList<Piece> target) {
        target.clear();
        for (int i = 0; i < source.size(); i++) {
            target.add(source.get(i));
        }
    }

    @Override
    public void run() {
        // Game loop
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    public synchronized void update() {
        if (promotion) {
            promoting();
        } else {
            if (isGameOver()) resetLoop(); // Evita atualizações se o jogo terminou

            if (mouse.precionado) {
                if (activeP == null) {
          synchronized(simpieces) {for (Piece piece : simpieces) {
                        if (piece.color == currentCollor &&
                            piece.col == mouse.x / Board.SQUARE_SIZE &&
                            piece.lin == mouse.y / Board.SQUARE_SIZE) {
                            activeP = piece;
                        }
                    }}
                } else simulate();
            }
            if (!mouse.precionado) {
                if (activeP != null) {
                    if (movimentovalido) {
                        copyPieces(simpieces, pieces);
                        activeP.updatePosition();
                        if (castlingP != null) castlingP.updatePosition();
                        if (canPromote()) {
                            promotion = true;
                        } else trocarOsTurnos();
                    } else {
                        copyPieces(pieces, simpieces);
                        activeP.resetPosition();
                    }
                    if (!promotion) activeP = null;
                }
            }
        }
    }

    private void simulate() {
        copyPieces(pieces, simpieces);

        if (castlingP != null) {
            castlingP.col = castlingP.precol;
            castlingP.x = castlingP.getX(castlingP.col);
            castlingP = null;
        }

        movimentovalido = false;
        activeP.x = mouse.x - Board.HALF_SQUARE_SIZE;
        activeP.y = mouse.y - Board.HALF_SQUARE_SIZE;
        activeP.col = activeP.getCol(activeP.x);
        activeP.lin = activeP.getLin(activeP.y);

        if (activeP.canMove(activeP.col, activeP.lin)) {
            if (activeP.targetPiece != null) {
                simpieces.remove(activeP.targetPiece.getIndex());
            }
            checkCastling();
            if (isIllegal() == false) {
                movimentovalido = true;
            }
        }
    }

    public void promoting() {
        if (mouse.precionado) {
            for (Piece piece : promoPieces) {
                if (piece.col == mouse.x / Board.SQUARE_SIZE && piece.lin == mouse.y / Board.SQUARE_SIZE) {
                    switch (piece.type) {
                        case ROOK:
                            simpieces.add(new Rook(currentCollor, activeP.col, activeP.lin));
                            break;
                        case KNIGHT:
                            simpieces.add(new Knight(currentCollor, activeP.col, activeP.lin));
                            break;
                        case QUEEN:
                            simpieces.add(new Queen(currentCollor, activeP.col, activeP.lin));
                            break;
                        case BISHOP:
                            simpieces.add(new Bishop(currentCollor, activeP.col, activeP.lin));
                            break;
                        default:
                            break;
                    }
                }
            }
            simpieces.remove(activeP.getIndex());
            copyPieces(simpieces, pieces);
            activeP = null;
            promotion = false;
            trocarOsTurnos();
        }
    }

    private boolean isGameOver() {
        if (isCheckmate()) {
            winner = (currentCollor == 'w') ? "Preto vence" : "Branco vence";
            gameEnded = true;
            return true;
        }

        if (isStalemate() || hasInsufficientMaterial()) {
            winner = "Empate";
            gameEnded = true;
            return true;
        }

        return false;
    }

    private boolean hasInsufficientMaterial() {
        // Checa se há peças suficientes para dar xeque-mate
        int whiteMaterial = 0, blackMaterial = 0;

        for (Piece piece : pieces) {
            if (piece.type != Tipo.KING) {
                if (piece.color == 'w') whiteMaterial++;
                if (piece.color == 'b') blackMaterial++;
            }
        }

        // Se ambos não têm material suficiente, retorna verdadeiro
        return (whiteMaterial == 0 || whiteMaterial == 1 && hasSingleMinorPiece('w')) &&
                (blackMaterial == 0 || blackMaterial == 1 && hasSingleMinorPiece('b'));
    }

    private boolean hasSingleMinorPiece(char color) {
        for (Piece piece : pieces) {
            if (piece.color == color &&
                (piece.type == Tipo.KNIGHT || piece.type == Tipo.BISHOP)) return true;
        }
        return false;
    }

    @Override
    public synchronized void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        board.draw(g2);

       synchronized(simpieces){ for (Piece p : simpieces) {
            p.draw(g2);
        }}

        if (activeP != null) {
            Composite originalComposite = g2.getComposite();
            if (movimentovalido) {
                g2.setColor(Color.CYAN);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                g2.fillRect(activeP.col * Board.SQUARE_SIZE, activeP.lin * Board.SQUARE_SIZE, Board.SQUARE_SIZE, Board.SQUARE_SIZE);
                g2.setComposite(originalComposite);
            }
            activeP.draw(g2);
        }

        if (promotion) {
            for (Piece piece : promoPieces) {
                g2.drawImage(piece.image, piece.getX(piece.col), piece.getY(piece.lin), Board.SQUARE_SIZE, Board.SQUARE_SIZE, null);
            }
        }

        // Mostra o texto de vitória/empate e a instrução de reinício
        if (gameEnded) {
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 20));
            g2.drawString(winner, WIDTH / 2 - 50, HEIGHT / 2 - 20);
            g2.setFont(new Font("Arial", Font.PLAIN, 16));
            g2.drawString("Clique para reiniciar o jogo", WIDTH / 2 - 80, HEIGHT / 2 + 10);
        }
    }

private void resetLoop(){
   if (mouse.precionado) {
      
    resetGame();
    }
  }
    private void resetGame() {
        pieces.clear();
        simpieces.clear();
        setPieces();
        copyPieces(pieces, simpieces);
        currentCollor = 'w';
        gameEnded = false;
        winner = "";
        activeP = null;
        castlingP = null;
        promotion = false;
    }
}

