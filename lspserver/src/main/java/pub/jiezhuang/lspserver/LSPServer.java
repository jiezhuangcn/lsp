package pub.jiezhuang.lspserver;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import pub.jiezhuang.lsp.AskMsg;

import java.util.concurrent.TimeUnit;


/**
 *  
 */
public class LSPServer {
    private int port;
    private SocketChannel socketChannel;
    public LSPServer(int port) throws InterruptedException {
        this.port = port;
        bind();
    }

    private void bind() throws InterruptedException {
        EventLoopGroup boss=new NioEventLoopGroup();
        EventLoopGroup worker=new NioEventLoopGroup();
        
        try {
	        ServerBootstrap bootstrap=new ServerBootstrap();
	        bootstrap.group(boss,worker);
	        bootstrap.channel(NioServerSocketChannel.class); 
	        bootstrap.option(ChannelOption.SO_BACKLOG, 128);
	        bootstrap.option(ChannelOption.TCP_NODELAY, true);
//	        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
	        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
	            @Override
	            protected void initChannel(SocketChannel socketChannel) throws Exception {
	            	System.out.print( "Request from "+ socketChannel.remoteAddress() );
	                ChannelPipeline p = socketChannel.pipeline();
	                
	                p.addLast( new LSPMessageDecoder() );
	                p.addLast( new LSPServerHandler());
	                p.addLast( new LSPMessageEncoder());
	                
	                // �����õ�
//	                p.addLast( new LineBasedFrameDecoder(255));
//	                p.addLast( new StringDecoder());
//	                p.addLast(new DiscardServerHandler());

	            }
	        });
	        ChannelFuture f= bootstrap.bind(port).sync();
	        if(f.isSuccess()){
	            System.out.println("server start---------------");
	        }
	        
	        f.channel().closeFuture().sync();
	        
        }catch( Exception e ){
        	e.printStackTrace();
        }finally {
        	boss.shutdownGracefully();
        	worker.shutdownGracefully();
        }
    }
    
    public static void main(String []args) throws InterruptedException {
        LSPServer bootstrap=new LSPServer(9999);
        
        while (true){
            SocketChannel channel=(SocketChannel)NettyChannelMap.get("001");
            if(channel!=null){
                AskMsg askMsg=new AskMsg();
                channel.writeAndFlush(askMsg);
            }
            TimeUnit.SECONDS.sleep(10);
        }
    }
}
