package com.charjay.catalina.netty.server;

import org.apache.log4j.Logger;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
  
public class MyTomcat {
	
	private static Logger LOG = Logger.getLogger(MyTomcat.class);
	
    public void start(int port) throws Exception {  
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();  
        try {  
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        public void initChannel(SocketChannel ch) throws Exception {
                            //无锁化串行编程
                            //服务端发送的是httpResponse，所以要使用HttpResponseEncoder进行编码  
                            ch.pipeline().addLast(new HttpResponseEncoder());  
                            //服务端接收到的是httpRequest，所以要使用HttpRequestDecoder进行解码  
                            ch.pipeline().addLast(new HttpRequestDecoder()); 
                            //最后处理自己的逻辑
                            ch.pipeline().addLast(new MyTomcatHandler());


                        }  
                     })
                    .option(ChannelOption.SO_BACKLOG, 128)//针对主线程的最大数量
                    .childOption(ChannelOption.SO_KEEPALIVE, true); //子线程配置
            
            //绑定服务端口
            ChannelFuture future = b.bind(port).sync();//主线程阻塞，不阻塞就挂了
            
            LOG.info("HTTP服务已启动，监听端口:" + port);
             
            //开始接收客户
            future.channel().closeFuture().sync();
            
        } finally {  
            workerGroup.shutdownGracefully();  
            bossGroup.shutdownGracefully();  
        }  
    }

}  
