package ch.cluder.libgdxexamples.net;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;

public class NetwerkServer implements Runnable {
	Socket clientSocket;
	Thread serverThread;
	boolean running;

	// open listening port
	public void start() {
		serverThread = new Thread(this, "Server Thread");
		serverThread.start();
		running = true;
	}

	public void stop() {
		running = false;
	}

	@Override
	public void run() {
		ServerSocketHints hints = new ServerSocketHints();
		hints.acceptTimeout = 1000;

		ServerSocket serverSocket = Gdx.net.newServerSocket(Protocol.TCP, 5555, hints);

		while (running) {
			try {

				SocketHints socketHints = new SocketHints();

				Gdx.app.log("NetworkServer", "waiting for connections ...");

				// blocks until connect
				clientSocket = serverSocket.accept(socketHints);

				Gdx.app.log("NetworkServer", "accepted connection");
				handleClient(clientSocket);
				sleep(100);

				if (clientSocket.isConnected()) {
					try (BufferedInputStream bufferedInputStream = new BufferedInputStream(
							clientSocket.getInputStream())) {

						Gdx.app.log("Server", "start read ...: ");
						BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
						while (true) {
							String line = br.readLine();
							Gdx.app.log("Server", line);
							sleep(100);
						}
					}
				}
			} catch (Exception e) {
				if (e.getCause() instanceof SocketTimeoutException) {
//					Gdx.app.log("NetworkServer", "timed out");
				} else {
					Gdx.app.log("NetworkServer", "accept:", e);
					serverThread.interrupt();
				}
			}
		}
		Gdx.app.log("NetworkServer", "stopping server ...:");

		serverSocket.dispose();
	}

	private void handleClient(Socket clientSocket) {

	}

	private void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			running = false;
		}
	}

}
