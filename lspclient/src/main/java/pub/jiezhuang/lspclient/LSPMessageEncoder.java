package pub.jiezhuang.lspclient;

import org.json.JSONObject;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class LSPMessageEncoder extends MessageToByteEncoder<JSONObject>{

	
	@Override
	protected void encode(ChannelHandlerContext ctx, JSONObject msg, ByteBuf out) throws Exception {
		String jsonStr = msg.toString();
		byte[] body = jsonStr.getBytes();
		
        int length = body.length;
        
        out.writeInt( 0xFFFE );
        out.writeInt( length );
        out.writeBytes( body );
	}
}
