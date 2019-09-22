package ch.clu.libgdxexamples.util;

import com.badlogic.gdx.Gdx;
import com.codedisaster.steamworks.SteamAPI;
import com.codedisaster.steamworks.SteamAuth;
import com.codedisaster.steamworks.SteamException;
import com.codedisaster.steamworks.SteamFriends;
import com.codedisaster.steamworks.SteamFriends.PersonaChange;
import com.codedisaster.steamworks.SteamFriendsCallback;
import com.codedisaster.steamworks.SteamID;
import com.codedisaster.steamworks.SteamMatchmaking.ChatEntryType;
import com.codedisaster.steamworks.SteamMatchmaking.ChatMemberStateChange;
import com.codedisaster.steamworks.SteamMatchmaking.ChatRoomEnterResponse;
import com.codedisaster.steamworks.SteamMatchmakingCallback;
import com.codedisaster.steamworks.SteamResult;
import com.codedisaster.steamworks.SteamServerListRequest;
import com.codedisaster.steamworks.SteamUserCallback;
import com.codedisaster.steamworks.SteamUtilsCallback;

public class SteamHelper
		implements SteamUtilsCallback, SteamMatchmakingCallback, SteamUserCallback, SteamFriendsCallback {
	String tag = "SteamHelper";

	public static SteamHelper instance = null;
	SteamFriends steamFriends;
	SteamServerListRequest r = null;

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
		steamFriends = new SteamFriends(this);
	}

	public SteamFriends getSteamFriends() {
		return steamFriends;
	}

	@Override
	public void onSteamShutdown() {

	}

	// SteamUserCallback
	@Override
	public void onValidateAuthTicket(SteamID steamID, SteamAuth.AuthSessionResponse authSessionResponse,
			SteamID steamID1) {

	}

	@Override
	public void onMicroTxnAuthorization(int appID, long orderID, boolean authorized) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEncryptedAppTicket(SteamResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFavoritesListChanged(int ip, int queryPort, int connPort, int appID, int flags, boolean add,
			int accountID) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLobbyInvite(SteamID steamIDUser, SteamID steamIDLobby, long gameID) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLobbyEnter(SteamID steamIDLobby, int chatPermissions, boolean blocked,
			ChatRoomEnterResponse response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLobbyDataUpdate(SteamID steamIDLobby, SteamID steamIDMember, boolean success) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLobbyChatUpdate(SteamID steamIDLobby, SteamID steamIDUserChanged, SteamID steamIDMakingChange,
			ChatMemberStateChange stateChange) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLobbyChatMessage(SteamID steamIDLobby, SteamID steamIDUser, ChatEntryType entryType, int chatID) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLobbyGameCreated(SteamID steamIDLobby, SteamID steamIDGameServer, int ip, short port) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLobbyMatchList(int lobbiesMatching) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLobbyKicked(SteamID steamIDLobby, SteamID steamIDAdmin, boolean kickedDueToDisconnect) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLobbyCreated(SteamResult result, SteamID steamIDLobby) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFavoritesListAccountsUpdated(SteamResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSetPersonaNameResponse(boolean success, boolean localSuccess, SteamResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPersonaStateChange(SteamID steamID, PersonaChange change) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGameOverlayActivated(boolean active) {
		Gdx.app.log(tag, "overlay active: " + active);

	}

	@Override
	public void onGameLobbyJoinRequested(SteamID steamIDLobby, SteamID steamIDFriend) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAvatarImageLoaded(SteamID steamID, int image, int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFriendRichPresenceUpdate(SteamID steamIDFriend, int appID) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGameRichPresenceJoinRequested(SteamID steamIDFriend, String connect) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGameServerChangeRequested(String server, String password) {
		// TODO Auto-generated method stub

	}

}
