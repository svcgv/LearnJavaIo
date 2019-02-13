package com.charjay.rpc.registry;

import com.charjay.config.CustomConfig;
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
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcRegistry {
	private static final Logger LOGGER = LoggerFactory.getLogger(RpcRegistry.class);
	private int port;

	static {
		CustomConfig.load("config.properties");
	}

	public RpcRegistry(int port){
		this.port = port;
	}
	public RpcRegistry(){
		this.port = CustomConfig.getInt("rpc.port");
	}

	
	public void start(){
		
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try{
			
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					
					ChannelPipeline pipeline = ch.pipeline();
					
					//处理的拆包、粘包的解、编码器
					pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4,0,4));
					pipeline.addLast(new LengthFieldPrepender(4));

					//处理序列化的解、编码器（JDK默认的序列化）
					pipeline.addLast("encoder",new ObjectEncoder());
					pipeline.addLast("decoder",new ObjectDecoder(Integer.MAX_VALUE,ClassResolvers.cacheDisabled(null)));

					//自己的业务逻辑
					pipeline.addLast(new RegistryHandler());
					
				}
				
			})
			.option(ChannelOption.SO_BACKLOG, 128)
			.childOption(ChannelOption.SO_KEEPALIVE, true);
			
			ChannelFuture f = b.bind(this.port).sync();

			LOGGER.info("RPC Registry start listen at {}" , this.port);
			f.channel().closeFuture().sync();
			
		}catch(Exception e){
			LOGGER.error("",e);
		}finally{
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}

	}
	
	

	
	
}