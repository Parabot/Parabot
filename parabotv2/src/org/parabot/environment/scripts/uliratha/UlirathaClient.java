package org.parabot.environment.scripts.uliratha;

import naga.ExceptionObserver;
import naga.NIOService;
import naga.NIOSocket;
import naga.SocketObserver;
import naga.packetreader.RegularPacketReader;
import naga.packetwriter.RegularPacketWriter;
import org.parabot.core.ui.Logger;

import java.io.*;

/**
 * @author JKetelaar
 */

public class UlirathaClient extends Thread {

    private String host;
    private int port;
    private NIOSocket socket;
    private boolean connected;
    private int scriptID;
    private String api;
    private boolean valid;

    public UlirathaClient(String host, int port, int scriptID, String api) {
        this.host = host;
        this.port = port;
        this.scriptID = scriptID;
        this.api = api;
    }

    @Override
    public void run() {
        connect();
    }

    private void connect() {
        try {
            NIOService service = new NIOService();
            service.setExceptionObserver(new ExceptionObserver() {
                @Override
                public void notifyExceptionThrown(Throwable throwable) {
                    throwable.printStackTrace();
                    if (valid) {
                        reconnect();
                        connected = false;
                    }
                }
            });
            socket = service.openSocket(host, port);
            socket.setPacketReader(new RegularPacketReader(4, true));
            socket.setPacketWriter(new RegularPacketWriter(4, true));
            socket.listen(new SocketObserver() {
                public void connectionOpened(NIOSocket nioSocket) {
                    try {
                        sendObjects(nioSocket, new Object[]{76, scriptID, api});
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                public void packetReceived(NIOSocket socket, byte[] packet) {
                    try {
                        DataInputStream stream = new DataInputStream(new ByteArrayInputStream(packet));
                        int packetID = stream.readInt();

                        switch (packetID){
                            case 75:
                                valid = stream.readBoolean();
                                if (valid) {
                                    Logger.addMessage("We're connected with the Uliratha server!");
                                    connected = true;
                                }else{
                                    socket.close();
                                }
                                break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void packetSent(NIOSocket nioSocket, Object o) {

                }

                public void connectionBroken(NIOSocket nioSocket, Exception exception) {
                    if (valid) {
                        Logger.addMessage("We lost connection with the Uliratha server, reconnecting...");
                        reconnect();
                        connected = false;
                    }else{
                        Logger.addMessage("We're disconnected from the Uliratha server");
                    }
                }
            });
            while (true) {
                service.selectBlocking();
            }
        } catch (IOException e) {
            if (valid) {
                reconnect();
                connected = false;
            }
        }
    }

    private void reconnect() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        connect();
    }

    public boolean isConnected() {
        return connected;
    }

    public void disconnect(){
        valid = false;
        socket.close();
    }

    private void sendObjects(NIOSocket socket, Object[] objects) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(stream);
        for (Object o : objects) {
            if (o instanceof String) {
                dataStream.writeUTF((String) o);
            } else if (o instanceof Integer) {
                dataStream.writeInt((Integer) o);
            } else if (o instanceof byte[]) {
                dataStream.write((byte[]) o);
            } else if (o instanceof Long) {
                dataStream.writeLong((Long) o);
            } else if (o instanceof Boolean) {
                dataStream.writeBoolean((Boolean) o);
            }
        }
        dataStream.flush();
        final byte[] content = stream.toByteArray();
        dataStream.close();
        socket.write(content);
    }

    public void sendMessage(String message){
        try {
            sendObjects(socket, new Object[]{83, message});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}