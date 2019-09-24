package ch.clu.libgdxexamples.steam;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.Observable;

import com.badlogic.gdx.Gdx;
import com.codedisaster.steamworks.SteamAPI;
import com.codedisaster.steamworks.SteamAuth;
import com.codedisaster.steamworks.SteamException;
import com.codedisaster.steamworks.SteamFriends;
import com.codedisaster.steamworks.SteamFriends.PersonaChange;
import com.codedisaster.steamworks.SteamFriendsCallback;
import com.codedisaster.steamworks.SteamID;
import com.codedisaster.steamworks.SteamMatchmaking;
import com.codedisaster.steamworks.SteamMatchmaking.ChatEntryType;
import com.codedisaster.steamworks.SteamMatchmaking.ChatMemberStateChange;
import com.codedisaster.steamworks.SteamMatchmaking.ChatRoomEnterResponse;
import com.codedisaster.steamworks.SteamMatchmakingCallback;
import com.codedisaster.steamworks.SteamResult;
import com.codedisaster.steamworks.SteamServerListRequest;
import com.codedisaster.steamworks.SteamUserCallback;
import com.codedisaster.steamworks.SteamUtils;
import com.codedisaster.steamworks.SteamUtilsCallback;

public class SteamHelper extends Observable
		implements SteamUtilsCallback, SteamMatchmakingCallback, SteamUserCallback, SteamFriendsCallback {
	String tag = this.getClass().getSimpleName();

	// singleton instance
	private static SteamHelper instance = null;

	// lobby filter (only needed for the demo game id 480)
	public static final String LOBBY_KEY_MAGIC = "MAGIC_KEY";
	public static final String LOBBY_VALUE_MAGIC = "123456";
	public static final String LOBBY_KEY_NAME = "NAME";

	// steam interfaces
	private SteamMatchmaking smm;
	private SteamFriends steamFriends;
	private SteamUtils steamUtils;

	// steam members
	private SteamID steamIDLobby;

	SteamServerListRequest r = null;

	// simplified data structures
	ArrayList<LobbyData> lobbyList = new ArrayList<>();

	private SteamHelper() {
	}

	public static SteamHelper get() {
		if (instance == null) {
			instance = new SteamHelper();
		}
		return instance;
	}

	public void initSteam() {
		try {
			SteamAPI.loadLibraries();
			if (!SteamAPI.init()) {
				// Steamworks initialization error, e.g. Steam client not running
				System.out.println("Steam client not running");
				SteamAPI.printDebugInfo(System.err);
				return;
			}
		} catch (SteamException e) {
			// Error extracting or loading native libraries
			Gdx.app.log("init", e.getMessage(), e);
			System.out.println("exiting");
			Gdx.app.exit();
		}

		// used for overlay activated callback
		smm = new SteamMatchmaking(this);
		steamFriends = new SteamFriends(this);
		steamUtils = new SteamUtils(this);

	}

	public SteamFriends getSF() {
		return steamFriends;
	}

	public SteamUtils getSU() {
		return steamUtils;
	}

	public SteamMatchmaking getSmm() {
		return smm;
	}

	public ArrayList<LobbyData> getLobbyList() {
		return lobbyList;
	}

	/**
	 * @return current steam lobby ID, can be null.
	 */
	public SteamID getSteamIDLobby() {
		return steamIDLobby;
	}

	/**
	 * Leaves the current lobby.
	 */
	public void leaveLobby() {
		if (steamIDLobby != null) {
			smm.leaveLobby(steamIDLobby);
			steamIDLobby = null;
		}
	}

	@Override
	public void onSteamShutdown() {
	}

	// SteamUserCallback
	@Override
	public void onValidateAuthTicket(SteamID steamID, SteamAuth.AuthSessionResponse authSessionResponse,
			SteamID steamID1) {
		Gdx.app.log(tag, "onValidateAuthTicket");

	}

	@Override
	public void onMicroTxnAuthorization(int appID, long orderID, boolean authorized) {
		Gdx.app.log(tag, "onMicroTxnAuthorization");

	}

	@Override
	public void onEncryptedAppTicket(SteamResult result) {
		Gdx.app.log(tag, "onEncryptedAppTicket");

	}

	@Override
	public void onLobbyChatMessage(SteamID steamIDLobby, SteamID steamIDUser, ChatEntryType entryType, int chatID) {
		Gdx.app.log(tag, format("onLobbyChatMessage %s %s %s %s", steamIDLobby, steamIDUser, entryType, chatID));

	}

	@Override
	public void onLobbyGameCreated(SteamID steamIDLobby, SteamID steamIDGameServer, int ip, short port) {
		Gdx.app.log(tag, format("onLobbyGameCreated: %s %s % %s", steamIDLobby, steamIDGameServer, ip, port));

	}

	@Override
	public void onFavoritesListChanged(int ip, int queryPort, int connPort, int appID, int flags, boolean add,
			int accountID) {
		Gdx.app.log(tag, "onFavoritesListChanged");

	}

	@Override
	public void onLobbyInvite(SteamID steamIDUser, SteamID steamIDLobby, long gameID) {
		Gdx.app.log(tag, "onLobbyInvite");

	}

	@Override
	public void onLobbyEnter(SteamID steamIDLobby, int chatPermissions, boolean blocked,
			ChatRoomEnterResponse response) {
		Gdx.app.log(tag, format("onLobbyEnter"));

	}

	@Override
	public void onLobbyDataUpdate(SteamID steamIDLobby, SteamID steamIDMember, boolean success) {
		Gdx.app.log(tag,
				"onLobbyDataUpdate lobbyID:" + steamIDLobby + " member:" + steamIDMember + " success:" + success);

	}

	@Override
	public void onLobbyChatUpdate(SteamID steamIDLobby, SteamID steamIDUserChanged, SteamID steamIDMakingChange,
			ChatMemberStateChange stateChange) {
		Gdx.app.log(tag, format("onLobbyChatUpdate: %s", steamIDLobby));

	}

	@Override
	public void onLobbyMatchList(int lobbiesMatching) {
		Gdx.app.log(tag, "onLobbyMatchList:" + lobbiesMatching);

		lobbyList = new ArrayList<>();

		for (int i = 0; i < lobbiesMatching; i++) {
			// get lobby infos
			SteamID lobbyID = smm.getLobbyByIndex(i);
			int lobbyDataCount = smm.getLobbyDataCount(steamIDLobby);
			String lobbyName = smm.getLobbyData(lobbyID, LOBBY_KEY_NAME);
			int numLobbyMembers = smm.getNumLobbyMembers(steamIDLobby);

			Gdx.app.log(tag, format("  lobby ID/Name:%s/%s member#:%s data#:%s", lobbyID, lobbyName, numLobbyMembers,
					lobbyDataCount));

			// add to simple data list
			LobbyData data = new LobbyData();
			data.lobbyID = lobbyID;
			data.name = (lobbyName != null && !lobbyName.isEmpty()) ? lobbyName : lobbyID.toString();
			data.numMembers = numLobbyMembers;
			lobbyList.add(data);

			// notify interested observers
			setChanged();
			notifyObservers(lobbyList);
		}

	}

	@Override
	public void onLobbyKicked(SteamID steamIDLobby, SteamID steamIDAdmin, boolean kickedDueToDisconnect) {
		Gdx.app.log(tag, format("onLobbyKicked::%s kickedDueToDisconnect=%s", steamIDLobby, kickedDueToDisconnect));

	}

	/**
	 * Creates a lobby and sets a lobby key/value, which we use to filter later.
	 */
	@Override
	public void onLobbyCreated(SteamResult result, SteamID steamIDLobby) {
		Gdx.app.log(tag, format("onLobbyCreated:%s %s", steamIDLobby, result));

		smm.setLobbyData(steamIDLobby, LOBBY_KEY_MAGIC, LOBBY_VALUE_MAGIC);

		String lobbyName = SteamHelper.get().getSF().getPersonaName() + "'s Lobby";

		smm.setLobbyData(steamIDLobby, LOBBY_KEY_NAME, lobbyName);

		this.steamIDLobby = steamIDLobby;

	}

	@Override
	public void onFavoritesListAccountsUpdated(SteamResult result) {
		Gdx.app.log(tag, "onFavoritesListAccountsUpdated");

	}

	@Override
	public void onSetPersonaNameResponse(boolean success, boolean localSuccess, SteamResult result) {
		Gdx.app.log(tag, "onSetPersonaNameResponse");

	}

	@Override
	public void onPersonaStateChange(SteamID steamID, PersonaChange change) {
		Gdx.app.log(tag, "onPersonaStateChange");

	}

	@Override
	public void onGameOverlayActivated(boolean active) {
		Gdx.app.log(tag, "overlay active: " + active);

	}

	@Override
	public void onGameLobbyJoinRequested(SteamID steamIDLobby, SteamID steamIDFriend) {
		Gdx.app.log(tag, "onGameLobbyJoinRequested: " + steamIDLobby);

	}

	@Override
	public void onAvatarImageLoaded(SteamID steamID, int image, int width, int height) {
		Gdx.app.log(tag, "onAvatarImageLoaded: " + steamIDLobby);

	}

	@Override
	public void onFriendRichPresenceUpdate(SteamID steamIDFriend, int appID) {
		Gdx.app.log(tag, "onFriendRichPresenceUpdate: " + steamIDLobby);

	}

	@Override
	public void onGameRichPresenceJoinRequested(SteamID steamIDFriend, String connect) {
		Gdx.app.log(tag, "onGameRichPresenceJoinRequested: " + steamIDLobby);

	}

	@Override
	public void onGameServerChangeRequested(String server, String password) {
		Gdx.app.log(tag, "onGameServerChangeRequested: " + steamIDLobby);

	}

}
