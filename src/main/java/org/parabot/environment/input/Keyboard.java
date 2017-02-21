package org.parabot.environment.input;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Random;

import com.google.inject.Singleton;
import org.parabot.core.Context;
import org.parabot.core.Core;

/**
 * 
 * Virtual keyboard, dispatches key events to a component.
 * 
 * @author Everel, Matt, Dane
 *
 */
@Singleton
public class Keyboard implements KeyListener {
	private static HashMap<Character, Character> specialChars;
	private Component component;
	private long pressTime;

	public Keyboard(){
	}

	public void setComponent(Component component){
		this.component = component;
	}
	
	public static Keyboard getInstance() {
		return Core.getInjector().getInstance(Context.class).getKeyboard();
	}

	static {
		char[] spChars = { '~', '!', '@', '#', '%', '^', '&', '*', '(', ')',
				'_', '+', '{', '}', ':', '<', '>', '?', '"', '|' };
		char[] replace = { '`', '1', '2', '3', '5', '6', '7', '8', '9', '0',
				'-', '=', '[', ']', ';', ',', '.', '/', '\'', '\\' };
		specialChars = new HashMap<Character, Character>(spChars.length);
		for (int x = 0; x < spChars.length; ++x)
			specialChars.put(spChars[x], replace[x]);
	}

	private static long getRandom() {
		Random rand = new Random();
		return rand.nextInt(100) + 40;
	}

	public void sendKeys(String s) {

		pressTime = System.currentTimeMillis();
		for (char c : s.toCharArray())
			for (KeyEvent ke : createKeyClick(component, c)) {
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				sendKeyEvent(ke);
			}
		clickKey(10);
	}

	public void clickKey(char c) {

		pressTime = System.currentTimeMillis();
		for (KeyEvent ke : createKeyClick(component, c))
			sendKeyEvent(ke);
	}

	public void clickKey(int keyCode) {

		pressTime = System.currentTimeMillis();
		for (KeyEvent ke : createKeyClick(component, keyCode))
			sendKeyEvent(ke);
	}

	public void pressKey(int keyCode) {

		pressTime = System.currentTimeMillis();
		KeyEvent ke = createKeyPress(component, keyCode);
		sendKeyEvent(ke);
	}

	public void releaseKey(int keyCode) {

		pressTime = System.currentTimeMillis();
		KeyEvent ke = createKeyRelease(component, keyCode);
		sendKeyEvent(ke);
	}

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

			return new KeyEvent[] { pressed, typed, released };
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

			return new KeyEvent[] { shiftDown, pressed, typed, released,
					shiftUp };
		}
	}

	private KeyEvent[] createKeyClick(Component target, int keyCode) {
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
		KeyEvent released = new KeyEvent(target, KeyEvent.KEY_RELEASED,
				pressTime + getRandom(), 0, keyCode, KeyEvent.CHAR_UNDEFINED);

		return new KeyEvent[] { pressed, released };
	}

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

	private KeyEvent createKeyRelease(Component target, int keyCode) {
		@SuppressWarnings("unused")
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
		KeyEvent released = new KeyEvent(target, KeyEvent.KEY_RELEASED,
				pressTime + getRandom(), 0, keyCode, KeyEvent.CHAR_UNDEFINED);

		return released;
	}

	public void sendKeyEvent(KeyEvent e) {
		for (KeyListener kl : component.getKeyListeners()) {
			if(kl instanceof Keyboard) {
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

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

}
