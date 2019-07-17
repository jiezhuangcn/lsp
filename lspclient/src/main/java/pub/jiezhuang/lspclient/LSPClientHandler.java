package pub.jiezhuang.lspclient;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;  
import io.netty.handler.timeout.IdleStateEvent;  
import io.netty.util.ReferenceCountUtil;  
  
import java.util.Date;
import java.util.Map;

import org.json.JSONObject;

import pub.jiezhuang.lspclient.LSPClient.onRepsonseListener;
  
 
  
/** 
 *   
 */  
public class LSPClientHandler extends ChannelInboundHandlerAdapter {  
  
    private int UNCONNECT_NUM = 0;  
    
    Map<String, onRepsonseListener> mRequestMap;
    
    
    public LSPClientHandler( Map<String, onRepsonseListener> mRequestMap ){
    	this.mRequestMap = mRequestMap;
    }
      
    @Override  
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {  
//        if (evt instanceof IdleStateEvent) {  
//            if(UNCONNECT_NUM >= 4) {  
//                System.err.println("connect status is disconnect.");  
//                ctx.close();  
//                //�˴������������ﵽ4��֮�󣬹رմ����Ӻ󣬲������������һ�ε�¼����  
//                return;  
//            }  
//              
//            IdleStateEvent e = (IdleStateEvent) evt;  
//            switch (e.state()) {  
//                case WRITER_IDLE:  
//                    System.out.println("send ping to server---date=" + new Date());  
//                    PingMsg pingMsg=new PingMsg();  
//                    ctx.writeAndFlush(pingMsg);  
//                    UNCONNECT_NUM++;  
//                    System.err.println("writer_idle over. and UNCONNECT_NUM=" + UNCONNECT_NUM);  
//                    break;  
//                case READER_IDLE:    
//                    System.err.println("reader_idle over.");  
//                    UNCONNECT_NUM++;  
//                    //��ȡ�������Ϣ��ʱʱ��ֱ�ӶϿ������ӣ������µ�¼���󣬽���ͨ��  
//                case ALL_IDLE:  
//                    System.err.println("all_idle over.");  
//                    UNCONNECT_NUM++;  
//                    //��ȡ�������Ϣ��ʱʱ��ֱ�ӶϿ������ӣ������µ�¼���󣬽���ͨ��  
//                default:  
//                    break;  
//            }  
//        }  
    }  
      

      
    @Override  
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {  
          
        System.err.println("in client exceptionCaught.");  
        super.exceptionCaught(ctx, cause);  
        //�����쳣ʱ�����Է��ͻ��߼�¼�����־��Ϣ��֮��ֱ�ӶϿ������ӣ������µ�¼���󣬽���ͨ��  
  
    }
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
    	
    	JSONObject m = (JSONObject) msg;
    	    
    	   String ref = m.getString( "ref");
    	   
    	   onRepsonseListener listener =  mRequestMap.get( ref );
    	   if( listener != null ){
    		   listener.onResponse(m);
    	   }
    	   
    	    
//        ByteBuf m = (ByteBuf) msg; // (1)
//        try {
//            long currentTimeMillis = (m.readUnsignedInt() - 2208988800L) * 1000L;
//            System.out.println(new Date(currentTimeMillis));
//            ctx.close();
//        } finally {
//            m.release();
//        }
    }

//	@Override
//	protected void channelRead0(ChannelHandlerContext ctx, JSONObject jsonObject) throws Exception {
//
//		System.out.println( ctx.channel().remoteAddress() + " : " + jsonObject.toString() );
//		ReferenceCountUtil.release(jsonObject);
//	}
}  