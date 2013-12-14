package org.matt123337.spoofer;

import java.net.InetAddress;

public class NetworkInterface {
	
	private byte[] mac = new byte[]{11,11,11,11,11,11};
	
	private static NetworkInterface cached;
	
	public byte[] getHardwareAddress(){
		System.out.println("[NI_HOOK] Mac Address Spoofed!");
		return mac;
	}
	
	public static NetworkInterface getByInetAddress(InetAddress addr){
		if(cached == null)
			cached = new NetworkInterface();
		return cached;
	}

}
