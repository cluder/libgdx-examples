package ch.clu.libgdxexamples.steam.data;

import java.util.ArrayList;
import java.util.List;

import com.codedisaster.steamworks.SteamID;

public class LobbyData {
	public SteamID lobbyID;
	public String name;
	public int numMembers;
	public int lobbyDataCount;
	public List<LobbyMember> members = new ArrayList<>();
}
