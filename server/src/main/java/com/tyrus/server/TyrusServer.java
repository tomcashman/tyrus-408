package com.tyrus.server;

import java.io.IOException;

import javax.websocket.DeploymentException;

import org.glassfish.tyrus.server.Server;

public class TyrusServer {

	public static void main(String [] args) {
		Server websocketServer = new Server("localhost",
                27000, "/ws", null, TyrusServerEndpoint.class);
		try {
			websocketServer.start();
		} catch (DeploymentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		websocketServer.stop();
	}
}
