package pub.jiezhuang.lspserver;


import java.util.Date;

import org.json.JSONObject;

import pub.jiezhuang.lsp.AskMsg;
import pub.jiezhuang.lsp.BaseMsg;
import pub.jiezhuang.lsp.LoginMsg;
import pub.jiezhuang.lsp.MsgType;
import pub.jiezhuang.lsp.PingMsg;
import pub.jiezhuang.lsp.ReplyBody;
import pub.jiezhuang.lsp.ReplyClientBody;
import pub.jiezhuang.lsp.ReplyMsg;
import pub.jiezhuang.lsp.ReplyServerBody;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

/**
 *  
 */
public class LSPServerHandler extends SimpleChannelInboundHandler<JSONObject> {
  
	 private int UNCONNECT_NUM_S = 0;
	
	 @Override
	    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//	        if (evt instanceof IdleStateEvent) {
//	        	if(UNCONNECT_NUM_S >= 4) {
//	        		System.err.println("client connect status is disconnect.");
//	        		ctx.close();
//	        		//�˴������������ﵽ4��֮�󣬹رմ����Ӻ�����������صļ�¼��Ϣ
//	        		return;
//	        	}
//	        	
//	            IdleStateEvent e = (IdleStateEvent) evt;
//	            switch (e.state()) {
//	                case WRITER_IDLE:
//	                	System.out.println("send ping to client---date=" + new Date());
//	                    PingMsg pingMsg=new PingMsg();
//	                    ctx.writeAndFlush(pingMsg);
//	                    UNCONNECT_NUM_S++;
//	                    System.err.println("writer_idle over. and UNCONNECT_NUM_S=" + UNCONNECT_NUM_S);
//	                    break;
//	                case READER_IDLE:  
//	                	System.err.println("reader_idle over.");
//	                	UNCONNECT_NUM_S++;
//	                	//��ȡ�������Ϣ��ʱʱ��ֱ�ӶϿ������ӣ������µ�¼���󣬽���ͨ��
//	                case ALL_IDLE:
//	                	System.err.println("all_idle over.");
//	                	UNCONNECT_NUM_S++;
//	                	//��ȡ�������Ϣ��ʱʱ��ֱ�ӶϿ������ӣ������µ�¼���󣬽���ͨ��
//	                default:
//	                    break;
//	            }
//	        }
	    }
	
	
	@Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.err.println("in channelInactive.");
		NettyChannelMap.remove((SocketChannel)ctx.channel());
    }
	
	
//    protected void messageReceived(ChannelHandlerContext channelHandlerContext, BaseMsg baseMsg) throws Exception {
//
//        if(MsgType.LOGIN.equals(baseMsg.getType())){
//            LoginMsg loginMsg=(LoginMsg)baseMsg;
//            if("lin".equals(loginMsg.getUserName())&&"alan".equals(loginMsg.getPassword())){
//                //��¼�ɹ�,��channel�浽����˵�map��
//                NettyChannelMap.add(loginMsg.getClientId(),(SocketChannel)channelHandlerContext.channel());
//                System.out.println("client"+loginMsg.getClientId()+" ��¼�ɹ�");
//            }
//        }else{
//            if(NettyChannelMap.get(baseMsg.getClientId())==null){
//                    //˵��δ��¼���������Ӷ��ˣ���������ͻ��˷����¼�����ÿͻ������µ�¼
//                    LoginMsg loginMsg=new LoginMsg();
//                    channelHandlerContext.channel().writeAndFlush(loginMsg);
//            }
//        }
//        switch (baseMsg.getType()){
//            case PING:{
//                  PingMsg pingMsg=(PingMsg)baseMsg;
//                  ReplyMsg replyPing=new ReplyMsg();
//                  ReplyServerBody body = new ReplyServerBody("send server msg.");
//                  replyPing.setBody(body);
//                  NettyChannelMap.get(pingMsg.getClientId()).writeAndFlush(replyPing);
//            	  System.err.println("ping over.");
//            }break;
//            case ASK:{
//                //�յ��ͻ��˵�����
//                AskMsg askMsg=(AskMsg)baseMsg;
//                if("authToken".equals(askMsg.getParams().getAuth())){
//                    ReplyServerBody replyBody=new ReplyServerBody("receive client askmsg:" + askMsg.getParams().getContent());
//                    ReplyMsg replyMsg=new ReplyMsg();
//                    replyMsg.setBody(replyBody);
//                    NettyChannelMap.get(askMsg.getClientId()).writeAndFlush(replyMsg);
//                }
//            }break;
//            case REPLY:{
//                //�յ��ͻ��˻ظ�
//                ReplyMsg replyMsg=(ReplyMsg)baseMsg;
//                ReplyClientBody clientBody=(ReplyClientBody)replyMsg.getBody();
//                UNCONNECT_NUM_S = 0;
//                System.out.println("UNCONNECT_NUM_S=" + UNCONNECT_NUM_S +",receive client replymsg: "+clientBody.getClientInfo());
//            }break;
//            default:break;
//        }
//        ReferenceCountUtil.release(baseMsg);
//    }
    
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    	
    	System.err.println("in here has an error.");
    	NettyChannelMap.remove((SocketChannel)ctx.channel());
    	super.exceptionCaught(ctx, cause);
    	System.err.println("channel is exception over. (SocketChannel)ctx.channel()=" + (SocketChannel)ctx.channel());
    }


//	protected void channelRead0(ChannelHandlerContext channelHandlerContext, BaseMsg baseMsg) throws Exception {
//		 if(MsgType.LOGIN.equals(baseMsg.getType())){
//	            LoginMsg loginMsg=(LoginMsg)baseMsg;
//	            if("lin".equals(loginMsg.getUserName())&&"alan".equals(loginMsg.getPassword())){
//	                //��¼�ɹ�,��channel�浽����˵�map��
//	                NettyChannelMap.add(loginMsg.getClientId(),(SocketChannel)channelHandlerContext.channel());
//	                System.out.println("client"+loginMsg.getClientId()+" ��¼�ɹ�");
//	            }
//	        }else{
//	            if(NettyChannelMap.get(baseMsg.getClientId())==null){
//	                    //˵��δ��¼���������Ӷ��ˣ���������ͻ��˷����¼�����ÿͻ������µ�¼
//	                    LoginMsg loginMsg=new LoginMsg();
//	                    channelHandlerContext.channel().writeAndFlush(loginMsg);
//	            }
//	        }
//	        switch (baseMsg.getType()){
//	            case PING:{
//	                  PingMsg pingMsg=(PingMsg)baseMsg;
//	                  ReplyMsg replyPing=new ReplyMsg();
//	                  ReplyServerBody body = new ReplyServerBody("send server msg.");
//	                  replyPing.setBody(body);
//	                  NettyChannelMap.get(pingMsg.getClientId()).writeAndFlush(replyPing);
//	            	  System.err.println("ping over.");
//	            }break;
//	            case ASK:{
//	                //�յ��ͻ��˵�����
//	                AskMsg askMsg=(AskMsg)baseMsg;
//	                if("authToken".equals(askMsg.getParams().getAuth())){
//	                    ReplyServerBody replyBody=new ReplyServerBody("receive client askmsg:" + askMsg.getParams().getContent());
//	                    ReplyMsg replyMsg=new ReplyMsg();
//	                    replyMsg.setBody(replyBody);
//	                    NettyChannelMap.get(askMsg.getClientId()).writeAndFlush(replyMsg);
//	                }
//	            }break;
//	            case REPLY:{
//	                //�յ��ͻ��˻ظ�
//	                ReplyMsg replyMsg=(ReplyMsg)baseMsg;
//	                ReplyClientBody clientBody=(ReplyClientBody)replyMsg.getBody();
//	                UNCONNECT_NUM_S = 0;
//	                System.out.println("UNCONNECT_NUM_S=" + UNCONNECT_NUM_S +",receive client replymsg: "+clientBody.getClientInfo());
//	            }break;
//	            default:break;
//	        }
//	        ReferenceCountUtil.release(baseMsg);
//		
//	}


	@Override
	protected void channelRead0(ChannelHandlerContext ctx, JSONObject jsonObject) throws Exception {
		// TODO Auto-generated method stub
		
		System.out.println( ctx.channel().remoteAddress() + " : " + jsonObject.toString() );
		ReferenceCountUtil.release(jsonObject);
		
//		final ByteBuf time = ctx.alloc().buffer(4); // (2)
//        time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));

//        ctx.writeAndFlush(time); // (3)   
		
		String ref = jsonObject.getString("ref");
		
		responseOK( ctx, ref );				
	}
	
	public void responseOK( ChannelHandlerContext ctx, String ref ){
    	 
    	JSONObject json = new JSONObject();
	    json.put( "result",  "ok" );
	    sendRequest( ctx, ref, json );
	    
    }
	
	
	public void sendRequest( ChannelHandlerContext ctx, String ref, JSONObject data ){
    	if( data == null )
    		return;
    	
    	JSONObject json = new JSONObject();
	    json.put( "type",  "response" );
	    json.put( "ref", ref );
	    json.put( "data", data.toString() );
	    
	    ctx.channel().writeAndFlush(json);
//	    ctx.writeAndFlush(json);  
    }
    
}
