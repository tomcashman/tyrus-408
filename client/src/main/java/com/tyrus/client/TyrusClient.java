package com.tyrus.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.DeploymentException;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

import org.glassfish.tyrus.client.ClientManager;
import org.glassfish.tyrus.client.ClientProperties;
import org.glassfish.tyrus.client.ThreadPoolConfig;
import org.glassfish.tyrus.container.grizzly.client.GrizzlyClientProperties;
import org.glassfish.tyrus.server.Server;

public class TyrusClient {

	public static void main(String [] args) {
		Server websocketServer = new Server("localhost",
                27001, "/ws", null, TyrusEndpoint.class);
		try {
			websocketServer.start();
		} catch (DeploymentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ClientEndpointConfig configuration = ClientEndpointConfig.Builder.create().build();
		ClientManager clientManager = ClientManager.createClient();
		clientManager.getProperties().put(ClientProperties.SHARED_CONTAINER, true);
		
		int grizzlyWorkerThreads = Runtime.getRuntime().availableProcessors();
		int grizzlySelectorThreads = grizzlyWorkerThreads > 4 ? grizzlyWorkerThreads / 2 : 1;
		
		clientManager.getProperties().put(GrizzlyClientProperties.SELECTOR_THREAD_POOL_CONFIG, ThreadPoolConfig
				.defaultConfig().setCorePoolSize(grizzlySelectorThreads).setMaxPoolSize(grizzlySelectorThreads));
		clientManager.getProperties().put(GrizzlyClientProperties.WORKER_THREAD_POOL_CONFIG, ThreadPoolConfig
				.defaultConfig().setCorePoolSize(grizzlyWorkerThreads).setMaxPoolSize(grizzlyWorkerThreads));
		
		ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(12);
		Session session;
		try {
			for(int i = 0; i < 8; i++) {
				session = clientManager.connectToServer(new Endpoint() {
					@Override
					public void onOpen(Session session, EndpointConfig config) {
					}
				}, configuration, new URI("ws://localhost:27000/ws/test"));
				session.addMessageHandler(new MessageHandler.Whole<String>() {
					@Override
					public void onMessage(String message) {
					}
				});
				for(int j = 0; j < 50; j++) {
					scheduledExecutorService.scheduleAtFixedRate(new TyrusMessageSender(session), 0L, 1L, TimeUnit.MILLISECONDS);
				}
			}
		} catch (DeploymentException | IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		try {
			System.in.read();
			scheduledExecutorService.shutdown();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
