package ch.clu.libgdxexamples.net;

import java.nio.ByteBuffer;

import com.badlogic.gdx.Gdx;
import com.codedisaster.steamworks.SteamException;
import com.codedisaster.steamworks.SteamID;
import com.codedisaster.steamworks.SteamNetworking;
import com.codedisaster.steamworks.SteamNetworking.P2PSend;

import ch.clu.libgdxexamples.screens.util.Screens;
import ch.clu.libgdxexamples.steam.SteamHelper;
import ch.clu.libgdxexamples.steam.data.LobbyData;
import ch.clu.libgdxexamples.steam.data.LobbyMember;
import ch.clu.libgdxexamples.util.ScreenManager;

public class NetworkUtil {
	static final String tag = NetworkUtil.class.getSimpleName();

	public static String hostOrderToString(int ip) {
		String hexString = Integer.toHexString(ip);
		String[] split = hexString.split("(?<=\\G..)");
		String result = "";
		for (int i = 0; i < split.length; i++) {
			result += Integer.decode("0x" + split[i]);
			if (i < split.length - 1) {
				result += ".";
			}
		}
		return result;
	}

	public static void sendToLobbyMembers(Messages msg) {
		SteamHelper sh = SteamHelper.get();
		SteamID steamIDLobby = sh.getSteamIDLobby();
		if (steamIDLobby == null) {
			Gdx.app.error(tag, String.format("steam lobby not set"));
			return;
		}
		LobbyData lobbyData = sh.gatherLobbyData(steamIDLobby);
		for (LobbyMember m : lobbyData.members) {
			try {
				msg.byteData.rewind();
				boolean sendP2PPacket = sh.getSN().sendP2PPacket(m.steamID, msg.data(), P2PSend.Reliable, 0);
				Gdx.app.log(tag,
						String.format("sent %s to member %s (%s): success:%s", msg, m.name, m.steamID, sendP2PPacket));
			} catch (SteamException e) {
				Gdx.app.log(tag, String.format("failed to send:%s ", msg), e);
			}
		}
	}

	public static void poll() {
		SteamNetworking net = SteamHelper.get().getSN();
		int packetSize = 0;

		packetSize = net.isP2PPacketAvailable(0);
		if (packetSize <= 0) {
			return;
		}
		Gdx.app.log(tag, String.format("packet available size:%s", packetSize));

		do {
			// read packet
			SteamID steamIDRemote = new SteamID();
			ByteBuffer bb = ByteBuffer.allocateDirect(packetSize);
			try {
				int read = net.readP2PPacket(steamIDRemote, bb, 0);

				String name = SteamHelper.get().getSF().getFriendPersonaName(steamIDRemote);
				Gdx.app.log(tag, String.format("read: %d bytes of data from %s (%s)", read, name, steamIDRemote));

				// determine packet type and process
				int typeData = bb.getInt();
				Messages messageType = Messages.fromData(typeData);

				switch (messageType) {
				case START_GAME:
					onStartGame();
					break;
				default:
					Gdx.app.log(tag, String.format("unknown message type: %s ", messageType));
					break;
				}
			} catch (SteamException e) {
				Gdx.app.log(tag, String.format("error during poll "), e);
			}

			packetSize = net.isP2PPacketAvailable(0);
		} while (packetSize > 0);
	}

	private static void onStartGame() {
		Gdx.app.log(tag, String.format("onStartGame: "));
		ScreenManager.getInstance().setScreen(Screens.GAME);
	}
}
