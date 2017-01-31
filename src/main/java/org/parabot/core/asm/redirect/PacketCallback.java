package org.parabot.core.asm.redirect;

public class PacketCallback {

    public static void onPacket(String methodName, int value) {
        System.out.println(methodName + "(" + value + ")");
    }

}
