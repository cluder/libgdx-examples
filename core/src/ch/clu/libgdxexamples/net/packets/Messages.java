package ch.clu.libgdxexamples.net.packets;

import java.nio.ByteBuffer;

public enum Messages {
	START_GAME((byte) 0x01) //
	;

	int intData = 0;
	ByteBuffer byteData = ByteBuffer.allocateDirect(4);

	private Messages(byte b) {
		byteData.put(b);

	}

	public ByteBuffer data() {
		return byteData;
	}

	public Messages fromData(byte b) {
		switch (b) {
		case 0x01:
			return START_GAME;
		default:
			break;
		}
		throw new UnsupportedOperationException();
	}

}
