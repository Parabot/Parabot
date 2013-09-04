package org.parabot.environment;

public enum OperatingSystem {

	WINDOWS, LINUX, MAC, OTHER;

	public static final OperatingSystem getOS() {
		String str = System.getProperty("os.name").toLowerCase();
		if (str.indexOf("win") > -1)
			return OperatingSystem.WINDOWS;
		if (str.indexOf("mac") > -1)
			return OperatingSystem.MAC;
		if (str.indexOf("nix") > -1 || str.indexOf("nux") > -1)
			return OperatingSystem.LINUX;
		return OperatingSystem.OTHER;
	}

}
