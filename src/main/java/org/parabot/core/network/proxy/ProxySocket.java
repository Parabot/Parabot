package org.parabot.core.network.proxy;

import org.parabot.core.Core;
import org.parabot.core.ui.utils.UILog;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class ProxySocket extends Socket {

    private static final List<ProxySocket> connections = new ArrayList<ProxySocket>();
    public static boolean auth = false;
    private static ProxyType proxyType = ProxyType.NONE;
    private static int proxyPort = 0;
    private static String username = null, password = null;
    private static InetAddress proxyInetAddress = null;
    private InetAddress addr;
    private int port;
    private InetSocketAddress cachedAddr;

    public ProxySocket(InetAddress addr, int port) throws IOException {
        super(addr, port);
    }

    public ProxySocket() {
        super();
    }

    public ProxySocket(String host, int port) throws IOException {
        super(host, port);
    }

    public static int closeConnections() {
        int value = 0;
        for (ProxySocket socket : connections) {
            try {
                connections.remove(socket);
                if (socket.isClosed()) {
                    continue;
                }
                socket.close();
                value++;
            } catch (Exception e) {
                Core.verbose("Error closing proxy connection: " + e.getMessage());
            }
        }
        return value;
    }

    public static void setProxy(ProxyType type, String host, int port) {
        try {
            proxyInetAddress = InetAddress.getByName(host);
            proxyPort = port;
            proxyType = type;
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static InetAddress getProxyAddress() {
        return proxyInetAddress;
    }

    public static int getProxyPort() {
        return proxyPort;
    }

    public static ProxyType getProxyType() {
        return proxyType;
    }

    public static void setType(ProxyType pt) {
        proxyType = pt;
    }

    public static int getConnectionCount() {
        return connections.size();
    }

    public static void setLogin(String user, char[] pass) {
        setLogin(user, new String(pass));
    }

    public static void setLogin(String user, String pass) {
        username = user;
        password = pass;
    }

    @Override
    public void connect(SocketAddress addr) throws IOException {
        connections.add(this);
        if (addr instanceof InetSocketAddress) {
            InetSocketAddress isa = (InetSocketAddress) addr;
            this.addr = InetAddress.getByName(isa.getHostString());
            this.port = isa.getPort();
        }
        if (proxyType != ProxyType.NONE) {
            try {
                super.connect(cachedAddr = new InetSocketAddress(
                        proxyInetAddress, proxyPort));
                initProxy();
            } catch (Exception e) {
                UILog.log("Proxy Error", e.getMessage(),
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            super.connect(addr);
        }
    }

    @Override
    public int getPort() {
        if (super.getInetAddress().equals(proxyInetAddress)) {
            return port;
        }
        return super.getPort();
    }

    @Override
    public InetAddress getInetAddress() {
        if (super.getInetAddress().equals(proxyInetAddress)) {
            return addr;
        }
        return super.getInetAddress();
    }

    @Override
    public SocketAddress getRemoteSocketAddress() {
        if (super.getInetAddress().equals(proxyInetAddress)) {
            return cachedAddr;
        }
        return super.getRemoteSocketAddress();
    }

    @Override
    public SocketChannel getChannel() {
        if (super.getInetAddress().equals(proxyInetAddress)) {
            return null;
        }
        return super.getChannel();
    }

    @Override
    public void close() throws IOException {
        connections.remove(this);
        super.close();
    }

    private void initProxy() throws IOException {
        System.out.println("Proxying:" + addr + ":" + port + " Over:"
                + proxyInetAddress + ":" + proxyPort + " Type:" + proxyType);
        switch (proxyType) {
            case HTTP:
                http_connect();
                break;
            case SOCKS4:
                socks4_connect();
                break;
            case SOCKS5:
                socks5_connect();
                break;
            default:
                throw new IOException("Unsupported proxy type:" + proxyType);
        }
    }

    private void http_connect() throws IOException {
        InputStream in = getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        OutputStream out = getOutputStream();
        out.write(("CONNECT " + addr.getHostAddress() + ":" + port + "\r\n")
                .getBytes());
        // out.write("Connection:keep-alive\r\n".getBytes());
        out.write("\r\n".getBytes());
        String str;
        while ((str = br.readLine()) != null) {
            if (str.length() == 0) {
                break;
            }
            if (!str.startsWith("HTTP")) {
                continue;
            }
            int code = Integer.parseInt(str.substring(9, 12));
            switch (code) {
                case 404:
                    throw new IOException(
                            "Proxy seems to think we're connecting to a webpage...");
                case 403:
                    throw new IOException(
                            "Proxy doesn't support connecting to port: " + port
                                    + "! Try a different proxy.");
            }
            if (code / 100 != 2) {
                throw new IOException(
                        "Unable to connect to proxy server! HTTP Error code:"
                                + code);
            }
        }
    }

    private void socks4_connect() throws IOException {
        DataOutputStream out = new DataOutputStream(getOutputStream());
        DataInputStream in = new DataInputStream(getInputStream());

        out.write(0x04);
        out.write(0x01); // connection type (TCP stream)
        out.writeShort(port);
        byte[] b = addr.getAddress();
        if (b.length != 4) {
            throw new IOException("Unsupported IP type for socksv4!");
        }
        out.write(b);
        out.write(0); // the userID stuff, 0 means end of string (null
        // terminated)
        out.flush();

        if (in.read() != 0x00) // null byte
        {
            throw new IOException("Proxy server dun goofed");
        }
        if (in.read() != 0x5a) {
            throw new IOException(
                    "Proxy server was unable to connect to server!");
        }

        in.readShort(); // ignored
        in.readFully(b); // ignored
    }

    private void socks5_connect() throws IOException {
        DataOutputStream out = new DataOutputStream(getOutputStream());
        DataInputStream in = new DataInputStream(getInputStream());
        out.write(0x05); // the version
        out.write(auth ? 2 : 1); // number of authentication methods (no auth
        // for now)
        out.write(0); // the authentication (none)
        if (auth) {
            out.write(2);
        }
        out.flush();
        if (in.read() != 0x05) // remote proxy version
        {
            throw new IOException("Proxy server is not supported!");
        }
        switch (in.read()) { // auth method
            case 0:
                break; // no auth
            case 2:
                // username and pass stuff
                out.write(0x01); // user/pass version #
                out.write(username.length());
                out.write(username.getBytes());
                out.write(password.length());
                out.write(password.getBytes());
                out.flush();
                in.read(); // skip the version
                if (in.read() == 0) // Successful login, continue
                {
                    break;
                }
            default:
                throw new IOException("Proxy server declined request!");
        }

        // now to write the actual request
        out.write(0x05); // again the socks version
        out.write(0x01); // the connection type (0x01 = TCP Connection)
        out.write(0x00); // the reserve byte, un-used
        byte[] b = addr.getAddress();
        out.write(b.length == 4 ? 0x01 : 0x04); // if ipv4 or ipv6 (0x03 =
        // domain name, but that's
        // unsupported as of yet)
        out.write(b);
        out.writeShort(port);
        out.flush();

        // now to read the server's reply
        if (in.read() != 0x05) // socks version (again)
        {
            throw new IOException("Proxy server dun goofed");
        }
        int reply = in.read();
        if (reply == 0x08) {
            throw new IOException("Bad address sent to proxy server");
        }
        if (reply != 0x00) {
            throw new IOException("Unable to connect to server!");
        }
        in.read(); // reserve byte
        int addrType = in.read();
        b = new byte[4];
        switch (addrType) {
            case 0x01:
                b = new byte[4];
                break;
            case 0x04:
                b = new byte[16];
                break;
            default:
                throw new IOException("Bad address type from proxy server!");
        }
        in.readFully(b);
        in.readShort(); // the returned port #, ignored
    }

}