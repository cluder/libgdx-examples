package ch.clu.libgdxexamples.steam.data;

import com.codedisaster.steamworks.SteamID;

public class LobbyMember {
	public SteamID steamID;
	public String name;

	@Override
	public String toString() {
		return name != null ? name : steamID.toString();
	}
}
