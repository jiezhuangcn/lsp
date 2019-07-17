package pub.jiezhuang.lsp;

public class LSPMessage {
	public static final int TYPE_REQUEST = 0;
	public static final int TYPE_RESPONE = 1;
	public static final int TYPE_NOFIFY = 2;
	
	private int type;
	private int checksum;

}
