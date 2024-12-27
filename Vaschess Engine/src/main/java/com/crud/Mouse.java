package com.crud;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
public class Mouse extends MouseAdapter{
        public int x, y;
        public boolean precionado;
        @Override
        public void mousePressed(MouseEvent e){
                precionado=true;
        }
        @Override
        public void mouseReleased(MouseEvent e){
            precionado=false;
        }
        @Override
        public void mouseDragged(MouseEvent e){
            x=e.getX();
            y=e.getY();
        }
        @Override
        public void mouseMoved(MouseEvent e){
            x=e.getX();
            y=e.getY();
        }
}