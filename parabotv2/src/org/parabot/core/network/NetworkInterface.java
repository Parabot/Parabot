package org.parabot.core.network;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.NoSuchElementException;

public class NetworkInterface {
	public static byte[] mac = new byte[] { 11, 11, 11, 11, 11, 11 };
	private static byte[] realMac;
	private static NetworkInterface cached;

	static {
			try {
				mac = getRealHardwareAddress();
			} catch (SocketException ignored) {
			}
	}

	public byte[] getHardwareAddress() {
		return mac;
	}

	public static Enumeration<NetworkInterface> getNetworkInterfaces()
			throws SocketException {
		final ArrayList<NetworkInterface> netifs = new ArrayList<>();

		return new Enumeration<NetworkInterface>() {
			private int i = 0;
			public NetworkInterface nextElement() {
				if (i < netifs.size()) {
					NetworkInterface netif = netifs.get(i++);
					return netif;
				} else {
					throw new NoSuchElementException();
				}
			}

			public boolean hasMoreElements() {
				return (i < netifs.size());
			}
		};
	}

	public static byte[] getRealHardwareAddress() throws SocketException{
		if (realMac != null)
			return realMac;
		try {
			return realMac = java.net.NetworkInterface.getByInetAddress(
					InetAddress.getLocalHost()).getHardwareAddress();
		} catch (Exception ignored) {
		}
		return mac;
	}

	public static NetworkInterface getByInetAddress(InetAddress addr) {
		if (cached == null)
			cached = new NetworkInterface();
		return cached;
	}
}
