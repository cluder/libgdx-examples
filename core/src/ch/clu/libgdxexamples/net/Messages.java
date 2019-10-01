package ch.clu.libgdxexamples.net;

import java.nio.ByteBuffer;

public enum Messages {
	START_GAME(0x01) //
	;

	int intData = 0;
	ByteBuffer byteData = ByteBuffer.allocateDirect(4);

	private Messages(int data) {
		byteData.putInt(data);
		byteData.rewind();
	}

	public ByteBuffer data() {
		return byteData;
	}

	static public Messages fromData(int data) {
		switch (data) {
		case 0x01:
			return START_GAME;
		default:
			break;
		}
		throw new UnsupportedOperationException();
	}

}
