package org.parabot.environment.input;

import org.parabot.core.Context;
import org.parabot.environment.api.utils.Time;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * A virtual mouse, dispatches mouse events to a component
 *
 * @author Everel
 */
public class Mouse implements MouseListener, MouseMotionListener {
    private final Component component;
    private int x;
    private int y;

    public Mouse(Component component) {
        this.component = component;
    }

    public static Mouse getInstance() {
        return Context.getInstance().getMouse();
    }

    /**
     * Moves the mouse to the given point and clicks
     *
     * @param x
     * @param y
     * @param left
     */
    public void click(final int x, final int y, final boolean left) {

        moveMouse(x, y);
        Time.sleep(50, 200);
        pressMouse(x, y, left);
        Time.sleep(10, 100);
        releaseMouse(x, y, left);
        Time.sleep(10, 100);
        clickMouse(x, y, left);
    }

    public void pressMouse(int x, int y, boolean left) {
        MouseEvent me = new MouseEvent(component,
                MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, x,
                y, 1, false, left ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3);
        for (MouseListener l : component.getMouseListeners()) {
            if (!(l instanceof Mouse)) {
                l.mousePressed(me);
            }
        }
    }

    public void click(final Point p, final boolean left) {
        click(p.x, p.y, left);
    }

    public void click(final Point p) {
        click(p.x, p.y, true);
    }

    public void clickMouse(int x, int y, boolean left) {
        try {

            MouseEvent me = new MouseEvent(component,
                    MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, x,
                    y, 0, false, left ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3);
            for (MouseListener l : component.getMouseListeners()) {
                if (!(l instanceof Mouse)) {
                    l.mouseClicked(me);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void releaseMouse(int x, int y, boolean left) {
        try {

            MouseEvent me = new MouseEvent(component,
                    MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(), 0, x,
                    y, 0, false, left ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3);
            for (MouseListener l : component.getMouseListeners()) {
                if (!(l instanceof Mouse)) {
                    l.mouseReleased(me);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Moves the mouse cursor to the given location
     *
     * @param x
     * @param y
     */
    public void moveMouse(int x, int y) {
        try {
            MouseEvent me = new MouseEvent(component,
                    MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, x,
                    y, 0, false);
            for (MouseMotionListener l : component.getMouseMotionListeners()) {
                if (!(l instanceof Mouse)) {
                    l.mouseMoved(me);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Mouse cursor current location
     *
     * @return point
     */
    public Point getPoint() {
        return new Point(x, y);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

}
