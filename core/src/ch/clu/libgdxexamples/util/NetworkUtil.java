package ch.clu.libgdxexamples.util;

import com.badlogic.gdx.Gdx;
import com.codedisaster.steamworks.SteamException;
import com.codedisaster.steamworks.SteamID;
import com.codedisaster.steamworks.SteamNetworking.P2PSend;

import ch.clu.libgdxexamples.net.packets.Messages;
import ch.clu.libgdxexamples.steam.SteamHelper;
import ch.clu.libgdxexamples.steam.data.LobbyData;
import ch.clu.libgdxexamples.steam.data.LobbyMember;

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
				boolean sendP2PPacket = sh.getSN().sendP2PPacket(m.steamID, msg.data(), P2PSend.Reliable, 0);
				Gdx.app.log(tag, String.format("sent %s to members:%s", msg, sendP2PPacket));
			} catch (SteamException e) {
				Gdx.app.log(tag, String.format("failed to send:%s ", msg), e);
			}
		}
	}
}
