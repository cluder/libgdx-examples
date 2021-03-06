package ch.clu.libgdxexamples.steam;

import static java.lang.String.format;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Observable;

import com.badlogic.gdx.Gdx;
import com.codedisaster.steamworks.SteamAPI;
import com.codedisaster.steamworks.SteamAuth;
import com.codedisaster.steamworks.SteamException;
import com.codedisaster.steamworks.SteamFriends;
import com.codedisaster.steamworks.SteamFriends.PersonaChange;
import com.codedisaster.steamworks.SteamFriends.PersonaState;
import com.codedisaster.steamworks.SteamFriendsCallback;
import com.codedisaster.steamworks.SteamID;
import com.codedisaster.steamworks.SteamMatchmaking;
import com.codedisaster.steamworks.SteamMatchmaking.ChatEntry;
import com.codedisaster.steamworks.SteamMatchmaking.ChatEntryType;
import com.codedisaster.steamworks.SteamMatchmaking.ChatMemberStateChange;
import com.codedisaster.steamworks.SteamMatchmaking.ChatRoomEnterResponse;
import com.codedisaster.steamworks.SteamMatchmakingCallback;
import com.codedisaster.steamworks.SteamMatchmakingKeyValuePair;
import com.codedisaster.steamworks.SteamNetworking;
import com.codedisaster.steamworks.SteamNetworking.P2PSessionError;
import com.codedisaster.steamworks.SteamNetworkingCallback;
import com.codedisaster.steamworks.SteamResult;
import com.codedisaster.steamworks.SteamServerListRequest;
import com.codedisaster.steamworks.SteamUserCallback;
import com.codedisaster.steamworks.SteamUtils;
import com.codedisaster.steamworks.SteamUtilsCallback;

import ch.clu.libgdxexamples.steam.data.LobbyChatMessage;
import ch.clu.libgdxexamples.steam.data.LobbyChatUpdate;
import ch.clu.libgdxexamples.steam.data.LobbyData;
import ch.clu.libgdxexamples.steam.data.LobbyDataList;
import ch.clu.libgdxexamples.steam.data.LobbyMember;

public class SteamHelper extends Observable
		implements SteamUtilsCallback, SteamMatchmakingCallback, SteamUserCallback, SteamFriendsCallback,
		// SteamGameServerCallback,
		SteamNetworkingCallback {
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
	SteamNetworking steamNetworking;
//	private SteamGameServer steamGameServer;
//	private SteamGameServerNetworking steamGameServerNetworking;
	// steam members
	private SteamID currentLobbyId;

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
		short steamPort = 27500;

		try {
			SteamAPI.loadLibraries();
			if (!SteamAPI.init()) {
				// Steamworks initialization error, e.g. Steam client not running
				System.out.println("Steam client not running");
				SteamAPI.printDebugInfo(System.err);
				return;
			}
//			SteamGameServerAPI.loadLibraries();
//			boolean serverInit = SteamGameServerAPI.init(0, (short) steamPort, (short) (steamPort + 1),
//					(short) (steamPort + 2), ServerMode.NoAuthentication, "1.0");
//			if (!serverInit) {
//				System.out.println("Could not init steam server");
//				SteamAPI.printDebugInfo(System.err);
//				return;
//			}

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
		steamNetworking = new SteamNetworking(this);
//		steamGameServer = new SteamGameServer(this);
//		steamGameServerNetworking = new SteamGameServerNetworking(this);

	}

	public void shutdown() {
		SteamAPI.shutdown();
//		SteamGameServerAPI.shutdown();
	}

	public SteamFriends getSF() {
		return steamFriends;
	}

	public SteamUtils getSU() {
		return steamUtils;
	}

//	public SteamGameServer getSGS() {
//		return steamGameServer;
//	}

	public SteamMatchmaking getSmm() {
		return smm;
	}

	public SteamNetworking getSN() {
		return steamNetworking;
	}

	/**
	 * @return current steam lobby ID, can be null.
	 */
	public SteamID getSteamIDLobby() {
		return currentLobbyId;
	}

	/**
	 * Leaves the current lobby.
	 */
	public void leaveLobby() {
		if (currentLobbyId != null) {
			smm.leaveLobby(currentLobbyId);
			currentLobbyId = null;
		}
	}

	@Override
	public void onSteamShutdown() {
		Gdx.app.log(tag, "onSteamShutdown");
	}

	// =========== Steam USer start ============
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

	// =========== Steam Matchmaking start ============

	@Override
	public void onLobbyChatMessage(SteamID steamIDLobby, SteamID steamIDUser, ChatEntryType entryType, int chatID) {
		ChatEntry chatEntry = new ChatEntry();
		ByteBuffer bb = ByteBuffer.allocateDirect(100);
		try {
			int lobbyChatEntry = smm.getLobbyChatEntry(steamIDLobby, chatID, chatEntry, bb);

		} catch (SteamException e) {
			Gdx.app.log(tag, "error during chat message handling", e);
		}

		CharBuffer decode = StandardCharsets.UTF_8.decode(bb);
		String string = decode.toString();

		String username = steamFriends.getFriendPersonaName(steamIDUser);
		Gdx.app.log(tag, format("onLobbyChatMessage %s %s %s %s %s %s", steamIDLobby, steamIDUser, entryType.name(),
				chatID, username, string));

		setChanged();
		notifyObservers(new LobbyChatMessage(username, string));
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
		currentLobbyId = steamIDLobby;

		LobbyData lobbyData = gatherLobbyData(steamIDLobby);
		Gdx.app.log(tag, format("onLobbyEnter: %s members:%s", response, Arrays.toString(lobbyData.members.toArray())));

		// notify observers about lobby join
		setChanged();
		notifyObservers(lobbyData);
	}

	@Override
	public void onLobbyDataUpdate(SteamID steamIDLobby, SteamID steamIDMember, boolean success) {
		int lobbyDataCount = smm.getLobbyDataCount(steamIDLobby);
		String memberName = steamFriends.getFriendPersonaName(steamIDMember);

		Gdx.app.log(tag, format("onLobbyDataUpdate lobbyID:%s member: %s (%s) success:%s count:%s", //
				steamIDLobby, steamIDMember, memberName, success, lobbyDataCount));

		SteamMatchmakingKeyValuePair keyValuePair = new SteamMatchmakingKeyValuePair();
		for (int i = 0; i < lobbyDataCount; i++) {
			boolean data = smm.getLobbyDataByIndex(steamIDLobby, i, keyValuePair);
			Gdx.app.log(tag, format("data [%s] key:%s value:%s", i, keyValuePair.getKey(), keyValuePair.getValue()));
		}

	}

	@Override
	public void onLobbyChatUpdate(SteamID steamIDLobby, SteamID steamIDUserChanged, SteamID steamIDMakingChange,
			ChatMemberStateChange stateChange) {
		String steamIDUserChangedName = steamFriends.getFriendPersonaName(steamIDUserChanged);
		String userMakingChangeName = steamFriends.getFriendPersonaName(steamIDMakingChange);

		Gdx.app.log(tag, format("onLobbyChatUpdate: %s %s steamIDUserChangedName=%s, userMakingChangeName=%s ",
				steamIDLobby, stateChange, steamIDUserChangedName, userMakingChangeName));

		LobbyData lobbyData = gatherLobbyData(steamIDLobby);
		setChanged();
		notifyObservers(lobbyData);

		LobbyChatUpdate chatUpdate = new LobbyChatUpdate(steamIDUserChangedName, stateChange.toString());
		setChanged();
		notifyObservers(chatUpdate);

	}

	@Override
	public void onLobbyMatchList(int lobbiesMatching) {
		Gdx.app.log(tag, "onLobbyMatchList:" + lobbiesMatching);

		LobbyDataList lobbyDataList = new LobbyDataList();

		for (int i = 0; i < lobbiesMatching; i++) {
			// get lobby infos
			SteamID lobbyID = smm.getLobbyByIndex(i);
			LobbyData data = gatherLobbyData(lobbyID);

			lobbyDataList.lobbyList.add(data);
			Gdx.app.log(tag, format("  lobby ID/Name:%s/%s member#:%s", data.lobbyID, data.name, data.numMembers));
		}
		// notify interested observers
		setChanged();
		notifyObservers(lobbyDataList);
	}

	public LobbyData gatherLobbyData(SteamID lobbyID) {
		LobbyData data = new LobbyData();
		int lobbyDataCount = smm.getLobbyDataCount(lobbyID);
		String lobbyName = smm.getLobbyData(lobbyID, LOBBY_KEY_NAME);
		int numLobbyMembers = smm.getNumLobbyMembers(lobbyID);

		for (int i = 0; i < numLobbyMembers; i++) {
			SteamID lobbyMember = smm.getLobbyMemberByIndex(lobbyID, i);
			String friendPersonaName = steamFriends.getFriendPersonaName(lobbyMember);

			LobbyMember member = new LobbyMember();
			member.steamID = lobbyMember;
			member.name = friendPersonaName;
			data.members.add(member);
		}

		data.lobbyID = lobbyID;
		data.name = (lobbyName != null && !lobbyName.isEmpty()) ? lobbyName : lobbyID.toString();
		data.numMembers = numLobbyMembers;
		data.lobbyDataCount = lobbyDataCount;
		return data;
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
		this.currentLobbyId = steamIDLobby;
		Gdx.app.log(tag, format("onLobbyCreated:%s %s", steamIDLobby, result));

		String lobbyName = SteamHelper.get().getSF().getPersonaName() + "'s Lobby";

		smm.setLobbyData(steamIDLobby, LOBBY_KEY_MAGIC, LOBBY_VALUE_MAGIC);
		smm.setLobbyData(steamIDLobby, LOBBY_KEY_NAME, lobbyName);

		LobbyData lobbyData = gatherLobbyData(steamIDLobby);
		setChanged();
		notifyObservers(lobbyData);
	}

	@Override
	public void onFavoritesListAccountsUpdated(SteamResult result) {
		Gdx.app.log(tag, "onFavoritesListAccountsUpdated");
	}

	@Override
	public void onGameServerChangeRequested(String server, String password) {
		Gdx.app.log(tag, "onGameServerChangeRequested: " + server);

	}

	// =========== Steam Friends start ============
	@Override
	public void onSetPersonaNameResponse(boolean success, boolean localSuccess, SteamResult result) {
		Gdx.app.log(tag, "onSetPersonaNameResponse");
	}

	@Override
	public void onPersonaStateChange(SteamID steamID, PersonaChange change) {
		Gdx.app.log(tag, format("onPersonaStateChange: %s", change.name()));

		PersonaState personaState = steamFriends.getPersonaState();
		String name = steamFriends.getFriendPersonaName(steamID);
		Gdx.app.log(tag, format("personaState: %s %s", name, personaState));
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
		Gdx.app.log(tag, "onAvatarImageLoaded: ");

	}

	@Override
	public void onFriendRichPresenceUpdate(SteamID steamIDFriend, int appID) {
		Gdx.app.log(tag, "onFriendRichPresenceUpdate: ");

	}

	@Override
	public void onGameRichPresenceJoinRequested(SteamID steamIDFriend, String connect) {
		Gdx.app.log(tag, "onGameRichPresenceJoinRequested: ");

	}

	// =========== Steam Game Server start ============
//	@Override
//	public void onValidateAuthTicketResponse(SteamID steamID, AuthSessionResponse authSessionResponse,
//			SteamID ownerSteamID) {
//		Gdx.app.log(tag, format("onValidateAuthTicketResponse: "));
//
//	}
//
//	@Override
//	public void onSteamServersConnected() {
//		Gdx.app.log(tag, format("onSteamServersConnected: "));
//
//	}
//
//	@Override
//	public void onSteamServerConnectFailure(SteamResult result, boolean stillRetrying) {
//		Gdx.app.log(tag, format("onSteamServerConnectFailure: %s %s ", result, stillRetrying));
//	}
//
//	@Override
//	public void onSteamServersDisconnected(SteamResult result) {
//		Gdx.app.log(tag, format("onSteamServersDisconnected: %s", result));
//	}
//
//	@Override
//	public void onClientApprove(SteamID steamID, SteamID ownerSteamID) {
//		Gdx.app.log(tag, format("onClientApprove: %s ", steamID, ownerSteamID));
//	}
//
//	@Override
//	public void onClientDeny(SteamID steamID, DenyReason denyReason, String optionalText) {
//		Gdx.app.log(tag, format("onClientDeny: %s", steamID, denyReason, optionalText));
//	}
//
//	@Override
//	public void onClientKick(SteamID steamID, DenyReason denyReason) {
//		Gdx.app.log(tag, format("onClientKick: %s", steamID, denyReason));
//	}
//
//	@Override
//	public void onClientGroupStatus(SteamID steamID, SteamID steamIDGroup, boolean isMember, boolean isOfficer) {
//		Gdx.app.log(tag, format("onClientGroupStatus: %s %s %s %s", steamID, steamIDGroup, isMember, isOfficer));
//	}
//
//	@Override
//	public void onAssociateWithClanResult(SteamResult result) {
//		Gdx.app.log(tag, format("onAssociateWithClanResult: %s", result));
//	}
//
//	@Override
//	public void onComputeNewPlayerCompatibilityResult(SteamResult result, int playersThatDontLikeCandidate,
//			int playersThatCandidateDoesntLike, int clanPlayersThatDontLikeCandidate, SteamID steamIDCandidate) {
//
//		Gdx.app.log(tag,
//				format("onComputeNewPlayerCompatibilityResult: %s %s %s %s %s", result, playersThatDontLikeCandidate,
//						playersThatCandidateDoesntLike, clanPlayersThatDontLikeCandidate, steamIDCandidate));
//	}

	// =========== Steam Networking start ============
	@Override
	public void onP2PSessionConnectFail(SteamID steamIDRemote, P2PSessionError sessionError) {
		String friendPersonaName = SteamHelper.get().getSF().getFriendPersonaName(steamIDRemote);
		Gdx.app.log(tag, format("onP2PSessionConnectFail: %s (%s) %s", friendPersonaName, steamIDRemote, sessionError));

	}

	/**
	 * User tries to send us data. We accept, if we are in the same lobby,
	 */
	@Override
	public void onP2PSessionRequest(SteamID steamIDRemote) {
		Gdx.app.log(tag, format("onP2PSessionRequest: %s", steamIDRemote));

		LobbyData lobbyData = gatherLobbyData(currentLobbyId);

		for (LobbyMember m : lobbyData.members) {
			if (m.steamID.equals(steamIDRemote)) {
				// found user in our lobby - accept
				boolean accept = steamNetworking.acceptP2PSessionWithUser(steamIDRemote);
				Gdx.app.log(tag,
						String.format("accepting session with:%s  %s", m.name, accept));
			}
		}
	}

	// =========== Steam Networking stop ============

}
