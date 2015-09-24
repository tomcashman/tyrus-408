package com.tyrus.server;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/test")
public class TyrusServerEndpoint {
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Session opened");
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Session closed");
    }

    @OnError
    public void onError(Throwable error) {
    	error.printStackTrace();
    }

    @OnMessage
    public void onMessage(String jsonMessage, Session session) throws Exception {
    	
    }
}
