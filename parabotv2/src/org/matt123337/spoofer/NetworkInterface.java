package org.matt123337.spoofer;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.Enumeration;

import org.parabot.core.ui.components.LogArea;

public class NetworkInterface {

	public static byte[] mac = new byte[] { 11, 11, 11, 11, 11, 11 };

	private static byte[] realMac;

	private static NetworkInterface cached;

	static {
		try {
			mac = getRealHardwareAddress();
		} catch (Exception ignored) {
		}
	}

	public byte[] getHardwareAddress() {
		return mac;
	}

	public static byte[] getRealHardwareAddress() throws SocketException {
		if (realMac != null)
			return realMac;
		Enumeration<java.net.NetworkInterface> nis = java.net.NetworkInterface
				.getNetworkInterfaces();
		while (nis.hasMoreElements()) {
			try {
				byte[] b = nis.nextElement().getHardwareAddress();
				if (b.length == 0)
					continue;
				return realMac = b;
			} catch (Exception e) {
			}
		}
		return mac;
	}

	public static NetworkInterface getByInetAddress(InetAddress addr) {
		if (cached == null)
			cached = new NetworkInterface();
		return cached;
	}

}
