package pub.jiezhuang.lsp;


import java.io.Serializable;

/**
 * 
 * ����ʵ������,serialVersionUID һ��Ҫ��
 */

public abstract class BaseMsg  implements Serializable {
    private static final long serialVersionUID = 1L;
    private MsgType type;
    //����Ψһ�����߻����channel���û���
    private String clientId;

    //��ʼ���ͻ���id
    public BaseMsg() {
        this.clientId = Constants.getClientId();
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public MsgType getType() {
        return type;
    }

    public void setType(MsgType type) {
        this.type = type;
    }
}
