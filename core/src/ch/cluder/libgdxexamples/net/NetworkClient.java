package ch.cluder.libgdxexamples.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.SocketException;

import com.badlogic.gdx.Gdx;
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

	public String getServerResponse() {
		try {
			InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream());
			BufferedReader br = new BufferedReader(isr);

			if (br.ready()) {
				String line = br.readLine();
				Gdx.app.log("NetworkClient", "received line:" + line);
				return line;
			}
		} catch (Exception e) {
			Gdx.app.log("NetworkServer", "error during client handling:", e);
			if (e.getCause() instanceof SocketException) {
				// socked was closed
			}
		}
		return null;
	}

}
