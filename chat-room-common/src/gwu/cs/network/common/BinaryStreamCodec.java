package gwu.cs.network.common;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class BinaryStreamCodec {
	byte[] toBytes(ChatRoomMessage msg) {
		return msg.serilize();
	}
	ChatRoomMessage fromBytes(byte[] stream) {
		return null;
	}
}
