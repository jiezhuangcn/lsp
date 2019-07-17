package pub.jiezhuang.lspclient;

import java.io.IOException;

import org.json.JSONObject;

import io.netty.channel.socket.SocketChannel;

public class Main {
	 public static void main(String[]args) throws InterruptedException, IOException {  
  
	    final LSPClient mLSPClient = new LSPClient(  "127.0.0.1", 9999, new LSPClient.onConnectListener() {
			
			@Override
			public void onConnected( LSPClient client ){
				System.out.println("connected to server");
				JSONObject json1 = new JSONObject();
				json1.put( "name", "hello" );
				client.sendRequest(json1, new LSPClient.onRepsonseListener() {
					
					@Override
					public void onResponse(JSONObject data) {
						// TODO Auto-generated method stub
						System.out.println(data.toString());
					}
				} );
				
	    	    
				//client.close();
			}
		} ); 
	       
	    mLSPClient.connect();
	 }
 
  
}
