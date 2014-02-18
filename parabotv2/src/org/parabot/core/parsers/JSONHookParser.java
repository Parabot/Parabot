package org.parabot.core.parsers;

import java.util.HashMap;

import org.parabot.core.asm.hooks.HookFile;
import org.parabot.core.asm.wrappers.Callback;
import org.parabot.core.asm.wrappers.Getter;
import org.parabot.core.asm.wrappers.Interface;
import org.parabot.core.asm.wrappers.Invoker;
import org.parabot.core.asm.wrappers.Setter;
import org.parabot.core.asm.wrappers.Super;

public class JSONHookParser extends HookParser {

	public JSONHookParser(HookFile hookFile) {
		super(hookFile);
	}

	@Override
	public Interface[] getInterfaces() {
		return null;
	}

	@Override
	public Super[] getSupers() {
		return null;
	}

	@Override
	public Getter[] getGetters() {
		return null;
	}

	@Override
	public Setter[] getSetters() {
		return null;
	}

	@Override
	public Invoker[] getInvokers() {
		return null;
	}

	@Override
	public Callback[] getCallbacks() {
		return null;
	}

	@Override
	public HashMap<String, String> getConstants() {
		return null;
	}

}
