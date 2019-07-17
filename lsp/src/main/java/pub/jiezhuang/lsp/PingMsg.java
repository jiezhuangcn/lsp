package pub.jiezhuang.lsp;

/**
 *  
 * ����������Ϣ����
 */
public class PingMsg extends BaseMsg {
    public PingMsg() {
        super();
        setType(MsgType.PING);
    }
}
