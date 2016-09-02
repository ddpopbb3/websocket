package com.chen.websocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/testwebsocket")
public class TestWebSocket {
	private CopyOnWriteArraySet<TestWebSocket> map = new CopyOnWriteArraySet<TestWebSocket>();
	private Session session;

	@OnOpen
	public void OnOpen(Session session) {
		this.session = session;
		map.add(this);
		System.out.println("连接成功");
	}

	@OnClose
	public void OnClose() {
		map.remove(this);
		System.out.println("断开连接");
	}

	@OnError
	public void OnError(Session session, Throwable error) {
		System.out.println("发生错误");
		error.printStackTrace();
	}

	@OnMessage
	public void OnMessage(String message, Session session) throws IOException {
		System.out.println("客户端传来的消息：" + message);
		/*
		 * for(TestWebSocket item: map){ try { //item.SendMessage(message);
		 * String string = new String(""); Scanner scanner = new
		 * Scanner(System.in); string = scanner.nextLine();
		 * item.SendMessage(string);
		 * 
		 * } catch (Exception e) { e.printStackTrace(); continue; }
		 * 
		 * }
		 */

		for (TestWebSocket item : map) {
			try {

				String line;

				BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));

				line = sin.readLine();
				
				while (!line.equals("bye")) {

					System.out.println("Server:" + line);
					
					line = sin.readLine();
					
					item.SendMessage(line);

				}

				

			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

		}

	}

	public void SendMessage(String message) throws IOException {
		this.session.getBasicRemote().sendText(message);
	}

}
