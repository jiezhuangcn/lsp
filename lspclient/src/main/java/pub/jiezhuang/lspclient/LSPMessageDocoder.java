package pub.jiezhuang.lspclient;

import java.util.List;

import org.json.JSONObject;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class LSPMessageDocoder extends ByteToMessageDecoder {

	private static int HEAD_LENGTH = 8;
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.readableBytes() < HEAD_LENGTH ) {
            return;
        }
		
        int header = in.readInt();
        if( header != 0xFFFE ){
        	return;
        }
        
        in.markReaderIndex(); //���Ǳ��һ�µ�ǰ��readIndex��λ��
        int dataLength = in.readInt();       // ��ȡ���͹�������Ϣ�ĳ��ȡ�ByteBuf ��readInt()������������readIndex����4
        if (dataLength < 0) { // ���Ƕ�������Ϣ�峤��Ϊ0�����ǲ�Ӧ�ó��ֵ���������������������ر����ӡ�
            ctx.close();
        }

        if (in.readableBytes() < dataLength) { //��������Ϣ�峤�����С�����Ǵ��͹�������Ϣ���ȣ���resetReaderIndex. ������markReaderIndexʹ�õġ���readIndex���õ�mark�ĵط�
            in.resetReaderIndex();
            return;
        }

        byte[] body = new byte[dataLength];
        in.readBytes(body);  //
        
        String jsonStr = new String(body);
       
//        out.add(jsonStr);
        JSONObject json = new JSONObject(jsonStr);
        out.add(json);  
	}

}
