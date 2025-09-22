package main;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import entity.Entity.Direction;

public class MouseMoveHandler implements MouseMotionListener {

    private int mouseX, mouseY;

    GamePanel gamePanel;

    public Point mouseLocation;

    public Cursor currentCursor;

    public MouseMoveHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.mouseX = e.getX();
        this.mouseY = e.getY();
        this.mouseLocation = new Point(e.getX(), e.getY());
        Direction direction = this.gamePanel.player.direction;
        switch (direction) {
            case Direction.UP -> {
                if (Constants.SCREEN_HEIGHT / 2 > e.getY()) {
                    this.currentCursor = this.gamePanel.targetCursor;
                } else {
                    this.currentCursor = this.gamePanel.targetCursorWide;
                }
            }
            case Direction.DOWN -> {
                if (Constants.SCREEN_HEIGHT / 2 < e.getY()) {
                    this.currentCursor = this.gamePanel.targetCursor;
                } else {
                    this.currentCursor = this.gamePanel.targetCursorWide;
                }
            }
            case Direction.LEFT -> {
                if (Constants.SCREEN_WIDTH / 2 > e.getX()) {
                    this.currentCursor = this.gamePanel.targetCursor;
                } else {
                    this.currentCursor = this.gamePanel.targetCursorWide;
                }
            }
            case Direction.RIGHT -> {
                if (Constants.SCREEN_WIDTH / 2 < e.getX()) {
                    this.currentCursor = this.gamePanel.targetCursor;
                } else {
                    this.currentCursor = this.gamePanel.targetCursorWide;
                }
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    public int getMouseX() {
        return this.mouseX;
    }

    public int getMouseY() {
        return this.mouseY;
    }
}
