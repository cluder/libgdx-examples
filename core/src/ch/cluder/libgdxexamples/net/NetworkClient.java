package ch.cluder.libgdxexamples.net;

import java.io.PrintWriter;

import com.badlogic.gdx.net.Socket;

public class NetworkClient {
	Socket clientSocket;

	public NetworkClient(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	public void sendChat(String text) {
		PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
		writer.print(text);
		writer.println();
		writer.flush();
	}

}
