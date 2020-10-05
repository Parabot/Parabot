package org.parabot.environment.input;

import org.parabot.core.Context;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Random;

/**
 * Virtual keyboard, dispatches key events to a component.
 *
 * @author Everel, Matt, Dane
 */
public class Keyboard implements KeyListener {
    /**
     * {@code KeyEvent.VK_ENTER} is actually New Line, '\n'.
     * The code for the Enter button is 13. It has no associated {@link KeyEvent} constant.
     */
    public static final int ENTER_KEYCODE = 13;
    private static final HashMap<Character, Character> specialChars;

    static {
        char[] spChars = { '~', '!', '@', '#', '%', '^', '&', '*', '(', ')',
                '_', '+', '{', '}', ':', '<', '>', '?', '"', '|' };
        char[] replace = { '`', '1', '2', '3', '5', '6', '7', '8', '9', '0',
                '-', '=', '[', ']', ';', ',', '.', '/', '\'', '\\' };
        specialChars = new HashMap<Character, Character>(spChars.length);
        for (int x = 0; x < spChars.length; ++x) {
            specialChars.put(spChars[x], replace[x]);
        }
    }

    private final Component component;
    private long pressTime;

    public Keyboard(Component component) {
        this.component = component;
    }

    public static Keyboard getInstance() {
        return Context.getInstance().getKeyboard();
    }

    /**
     * Types the given String and afterwards presses Enter.
     *
     * @param s The String to type.
     */
    public void sendKeys(String s) {
        sendKeys(s, true);
    }

    /**
     * Types the given String and optionally presses Enter afterwards.
     *
     * @param s          The String to type.
     * @param enterAfter True if {@code KeyEvent.VK_ENTER} should be pressed afterwards. This is actually the '\n' character, for New Line. Useful for logging in.
     */
    public void sendKeys(String s, boolean enterAfter) {

        pressTime = System.currentTimeMillis();
        for (char c : s.toCharArray()) {
            for (KeyEvent ke : createKeyClick(component, c)) {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sendKeyEvent(ke);
            }
        }
        if (enterAfter) {
            clickKey(KeyEvent.VK_ENTER);
        }
    }

    /**
     * Creates and sends a single KeyEvent using the given Char.
     *
     * @param c The char to send.
     */
    public void clickKey(char c) {

        pressTime = System.currentTimeMillis();
        for (KeyEvent ke : createKeyClick(component, c)) {
            sendKeyEvent(ke);
        }
    }

    /**
     * Creates and sends a given KeyEvent using the given keyCode.
     * <p>Use constants where possible, from {@link KeyEvent}, such as {@code KeyEvent.VK_ENTER}
     *
     * @param keyCode The keycode to send.
     */
    public void clickKey(int keyCode) {

        pressTime = System.currentTimeMillis();
        for (KeyEvent ke : createKeyClick(component, keyCode)) {
            sendKeyEvent(ke);
        }
    }

    /**
     * Creates and sends a given PRESS KeyEvent using the given keyCode. Note, this does not send a Release Event
     * typically associated with a key click.
     * <p>Use constants where possible, from {@link KeyEvent}, such as {@code KeyEvent.VK_ENTER}
     *
     * @param keyCode
     */
    public void pressKey(int keyCode) {

        pressTime = System.currentTimeMillis();
        KeyEvent ke = createKeyPress(component, keyCode);
        sendKeyEvent(ke);
    }

    /**
     * Creates and sends a given RELEASE KeyEvent using the given keyCode. Note, this does not send a Press Event
     * typically associated with a key click.
     * <p>Use constants where possible, from {@link KeyEvent}, such as {@code KeyEvent.VK_ENTER}
     *
     * @param keyCode
     */
    public void releaseKey(int keyCode) {

        pressTime = System.currentTimeMillis();
        KeyEvent ke = createKeyRelease(component, keyCode);
        sendKeyEvent(ke);
    }

    /**
     * Actually triggers sending of a given KeyEvent in the instance of KeyListeners' {@code component} field.
     *
     * @param e
     */
    public void sendKeyEvent(KeyEvent e) {
        for (KeyListener kl : component.getKeyListeners()) {
            if (kl instanceof Keyboard) {
                continue;
            }
            if (!e.isConsumed()) {
                switch (e.getID()) {
                    case KeyEvent.KEY_PRESSED:
                        kl.keyPressed(e);
                        break;
                    case KeyEvent.KEY_RELEASED:
                        kl.keyReleased(e);
                        break;
                    case KeyEvent.KEY_TYPED:
                        kl.keyTyped(e);
                        break;
                }
            }
        }
    }

    /**
     * Allows the {@code KeyListener.keyPressed} event to be overridden.
     *
     * @param e
     */
    @Override
    public void keyPressed(KeyEvent e) {

    }

    /**
     * Allows the {@code KeyListener.keyReleased} event to be overridden.
     *
     * @param e
     */
    @Override
    public void keyReleased(KeyEvent e) {

    }

    /**
     * Allows the {@code KeyListener.keyTyped} event to be overridden.
     *
     * @param e
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * Generates a random number in the range of 40-140.
     *
     * @return The random number
     */
    private static long getRandom() {
        Random rand = new Random();
        return rand.nextInt(100) + 40;
    }

    /**
     * Creates KeyEvents to perform a Click of the given Char. This includes a Press, Typed and Release event
     * in addition to an initial shiftDown and ending shiftUp if the character is a Special Char such as {@code !"£$%^&*(}
     * <p>
     * {@see specialChars}
     *
     * @param target Component this event is linked to.
     * @param c      Char to send.
     *
     * @return KeyEvents for each action.
     */
    private KeyEvent[] createKeyClick(Component target, char c) {

        pressTime += 2 * getRandom();

        Character newChar = specialChars.get(c);
        int keyCode = Character.toUpperCase((newChar == null) ? c : newChar);

        if (Character.isLowerCase(c)
                || (!Character.isLetter(c) && (newChar == null))) {
            KeyEvent pressed = new KeyEvent(target, KeyEvent.KEY_PRESSED,
                    pressTime, 0, keyCode, c);
            KeyEvent typed = new KeyEvent(target, KeyEvent.KEY_TYPED,
                    pressTime, 0, 0, c);
            pressTime += getRandom();
            KeyEvent released = new KeyEvent(target, KeyEvent.KEY_RELEASED,
                    pressTime, 0, keyCode, c);

            return new KeyEvent[]{ pressed, typed, released };
        } else {
            KeyEvent shiftDown = new KeyEvent(target, KeyEvent.KEY_PRESSED,
                    pressTime, KeyEvent.SHIFT_MASK, KeyEvent.VK_SHIFT,
                    KeyEvent.CHAR_UNDEFINED);

            pressTime += getRandom();
            KeyEvent pressed = new KeyEvent(target, KeyEvent.KEY_PRESSED,
                    pressTime, KeyEvent.SHIFT_MASK, keyCode, c);
            KeyEvent typed = new KeyEvent(target, KeyEvent.KEY_TYPED,
                    pressTime, KeyEvent.SHIFT_MASK, 0, c);
            pressTime += getRandom();
            KeyEvent released = new KeyEvent(target, KeyEvent.KEY_RELEASED,
                    pressTime, KeyEvent.SHIFT_MASK, keyCode, c);
            pressTime += getRandom();
            KeyEvent shiftUp = new KeyEvent(target, KeyEvent.KEY_RELEASED,
                    pressTime, 0, KeyEvent.VK_SHIFT, KeyEvent.CHAR_UNDEFINED);

            return new KeyEvent[]{ shiftDown, pressed, typed, released,
                    shiftUp };
        }
    }

    /**
     * Creates KeyEvents for Press and Release of the given keyCode.
     *
     * @param target
     * @param keyCode
     *
     * @return An array containing Press and Release KeyEvents.
     */
    private KeyEvent[] createKeyClick(Component target, int keyCode) {

        return new KeyEvent[]{ createKeyPress(target, keyCode), createKeyRelease(target, keyCode) };
    }

    /**
     * Creates a Press type KeyEvent
     *
     * @param target
     * @param keyCode
     *
     * @return
     */
    private KeyEvent createKeyPress(Component target, int keyCode) {
        int modifier = 0;
        switch (keyCode) {
            case KeyEvent.VK_SHIFT:
                modifier = KeyEvent.SHIFT_MASK;
                break;
            case KeyEvent.VK_ALT:
                modifier = KeyEvent.ALT_MASK;
                break;
            case KeyEvent.VK_CONTROL:
                modifier = KeyEvent.CTRL_MASK;
                break;
        }
        KeyEvent pressed = new KeyEvent(target, KeyEvent.KEY_PRESSED,
                pressTime, modifier, keyCode, KeyEvent.CHAR_UNDEFINED);

        return pressed;
    }

    /**
     * Creates a Release type KeyEvent
     *
     * @param target
     * @param keyCode
     *
     * @return
     */
    private KeyEvent createKeyRelease(Component target, int keyCode) {
        KeyEvent released = new KeyEvent(target, KeyEvent.KEY_RELEASED,
                pressTime + getRandom(), 0, keyCode, KeyEvent.CHAR_UNDEFINED);

        return released;
    }

}
