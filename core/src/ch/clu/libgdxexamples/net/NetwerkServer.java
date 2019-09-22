package ch.clu.libgdxexamples.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;

public class NetwerkServer implements Runnable {
	List<Socket> connectedClients = new ArrayList<Socket>();
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

				// handle existing clients
				handleClients();

//				Gdx.app.debug("NetworkServer", "waiting for connections ...");
				acceptClients(serverSocket);

				sleep(100);
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

	/**
	 * Listens for new connecting clients. <br>
	 * New Clients are added to the list of connected clients.
	 */
	private void acceptClients(ServerSocket serverSocket) {
		try {
			Socket clientSocket = serverSocket.accept(null);
			Gdx.app.log("NetworkServer", "accepted connection");

			if (clientSocket.isConnected()) {
				connectedClients.add(clientSocket);
				Gdx.app.debug("NetworkServer", "added client: " + connectedClients.size());
			}
		} catch (Exception e) {
			if (e.getCause() instanceof SocketTimeoutException) {
				// expected timeout
			} else {
				Gdx.app.log("NetworkServer", "error accepting clients:", e);
			}
		}
	}

	/**
	 * Iterate over connected clients and handle any incoming messages.
	 */
	private void handleClients() {
		for (Socket client : connectedClients) {
			handleClients(client);
		}
	}

	private void handleClients(Socket client) {
//		Gdx.app.debug("NetworkServer", "handling client:" + client.getRemoteAddress());

		try {
			InputStreamReader isr = new InputStreamReader(client.getInputStream());
			BufferedReader br = new BufferedReader(isr);

			if (br.ready()) {
				String line = br.readLine();
				Gdx.app.log("NetworkServer", "received line:" + line);
				broadcastChat(line, client);
			}
		} catch (Exception e) {
			Gdx.app.log("NetworkServer", "error during client handling:", e);
			if (e.getCause() instanceof SocketException) {
				// socked was closed
				client.dispose();
			}
		}

		// remove closed clients
		connectedClients = connectedClients.stream().filter(c -> c.isConnected()).collect(Collectors.toList());
	}

	private void broadcastChat(String line, Socket origin) {
		for (Socket s : connectedClients) {
			if (s == origin) {
				// skip sender
				continue;
			}

			Gdx.app.log("NetworkServer", "updating client:" + s.getRemoteAddress());
			PrintWriter writer = new PrintWriter(s.getOutputStream());
			writer.print(line);
			writer.println();
			writer.flush();
		}
	}

	private void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			running = false;
		}
	}

}
