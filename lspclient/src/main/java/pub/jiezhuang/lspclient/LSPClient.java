package pub.jiezhuang.lspclient;

import io.netty.bootstrap.Bootstrap;  
import io.netty.buffer.Unpooled;  
import io.netty.channel.ChannelFuture;  
import io.netty.channel.ChannelInitializer;  
import io.netty.channel.ChannelOption;  
import io.netty.channel.EventLoopGroup;  
import io.netty.channel.nio.NioEventLoopGroup;  
import io.netty.channel.socket.SocketChannel;  
import io.netty.channel.socket.nio.NioSocketChannel;  
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;  
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;  
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;  
import io.netty.handler.codec.http.websocketx.WebSocketFrame;  
import io.netty.handler.codec.serialization.ClassResolvers;  
import io.netty.handler.codec.serialization.ObjectDecoder;  
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.IdleStateHandler;  
import io.netty.util.concurrent.DefaultEventExecutorGroup;  
import io.netty.util.concurrent.EventExecutorGroup;  
  
import java.io.BufferedReader;  
import java.io.IOException;  
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import pub.jiezhuang.lsp.AskMsg;  
import pub.jiezhuang.lsp.AskParams;  
import pub.jiezhuang.lsp.Constants;
import pub.jiezhuang.lspclient.LSPClient.onRepsonseListener;
import pub.jiezhuang.lsp.LoginMsg;  
  
/** 
 *  
 */  
public class LSPClient {  
    private int port;  
    private String host;  
    private Boolean mIsConnected = false;
    
    private SocketChannel mSocketChannel;  
    private static final EventExecutorGroup group = new DefaultEventExecutorGroup(20);  
    
    public interface onConnectListener{
    	public void onConnected( LSPClient client );
    }
    
    public interface onRepsonseListener{
    	public void onResponse( JSONObject data );
    }
    
    Map<String, onRepsonseListener> mRequestMap = new HashMap<String, onRepsonseListener>();
    
    private onConnectListener mOnConnectListener;
    
    public LSPClient( String host, int port ) {  
        this.port = port;  
        this.host = host;  
    }  
    
    
    public LSPClient( String host, int port, onConnectListener listener ) {  
        this.port = port;  
        this.host = host;  
        mOnConnectListener = listener;
    }
    
    
    public void connect() throws InterruptedException {  
    	
        EventLoopGroup eventLoopGroup=new NioEventLoopGroup();  
        
        try {
	        Bootstrap bootstrap=new Bootstrap();  
	        bootstrap.channel(NioSocketChannel.class);  
	        bootstrap.option(ChannelOption.SO_KEEPALIVE,true);  
	        bootstrap.group(eventLoopGroup);  
	        bootstrap.remoteAddress(host,port);  
	        bootstrap.handler(new ChannelInitializer<SocketChannel>() {  
	            @Override  
	            protected void initChannel(SocketChannel socketChannel) throws Exception {
	            	
	                socketChannel.pipeline()
	                	.addLast(new pub.jiezhuang.lspclient.LSPMessageEncoder() )
	                	.addLast( new LSPMessageDocoder() )
	                	.addLast( new StringDecoder() )
	                	.addLast(new LSPClientHandler(mRequestMap));
	            }  
	        });  
	        ChannelFuture future =bootstrap.connect(host,port).sync();
	        
	        if (future.isSuccess()) {  
	        	mRequestMap.clear();
	        	mSocketChannel = (SocketChannel)future.channel();  
	            
	        	mIsConnected = true;
	            if( mOnConnectListener != null ){
	            	mOnConnectListener.onConnected( LSPClient.this );
	            }	            
	            
	            future.channel().closeFuture().sync();	            
	        }
        }catch( Exception e){
        	e.printStackTrace();
        }
        finally {
        	eventLoopGroup.shutdownGracefully();
        	mIsConnected = false;
        }        
    }
    
    
    public void sendRequest( JSONObject data, onRepsonseListener listener ){
    	if( data == null ) {
			return;
		}
    	
    	JSONObject json = new JSONObject();
	    json.put( "type",  "reqest" );
	    json.put( "ref",  "12345678" );
	    json.put( "data", data.toString() );
	    mSocketChannel.writeAndFlush(json);  
	    
	    mRequestMap.put( "12345678", listener );
	    
    }
    
    public void close(){
    	mSocketChannel.close();
    }
 
}  