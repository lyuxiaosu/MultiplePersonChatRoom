package gwu.cs.network.common;

public abstract class ChatRoomMessage {
	public abstract int getMessageType();
	public abstract byte[] serilize();
}
