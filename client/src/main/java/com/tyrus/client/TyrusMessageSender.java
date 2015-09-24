package com.tyrus.client;

import java.util.concurrent.atomic.AtomicLong;

import javax.websocket.Session;

public class TyrusMessageSender implements Runnable {
	private final Session session;
	private final AtomicLong counter;
	
	public TyrusMessageSender(Session session) {
		this.session = session;
		this.counter = new AtomicLong(Long.MIN_VALUE + 1);
	}

	@Override
	public void run() {
		session.getAsyncRemote().sendText("Hello " + counter.getAndIncrement());
	}

}
