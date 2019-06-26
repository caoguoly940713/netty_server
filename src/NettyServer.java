import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * server提供三种服务：
 * 1.登录服务，将用户信息存进数据库
 * 2.聊天服务，将用户发送的消息分发出去
 * 3.报表服务，把在线用户的名称发送给客户端
 */
public class NettyServer {

    private Map<String, Channel> clientChannel;

    private void startServer(ChannelHandler handler) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        NioEventLoopGroup boos = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        serverBootstrap
                .group(boos, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(handler)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .bind(8000);
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        NettyServer server = new NettyServer();
        server.clientChannel = new HashMap<>();
        DBHelper.connect();
        DBHelper.createTable();

        server.startServer(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ch.pipeline().addLast(new StringDecoder());
                ch.pipeline().addLast(new StringEncoder());
                ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

                        System.out.println("客户端" + ctx.channel().remoteAddress() + ":" + msg);

                        JSONObject object = new JSONObject(msg.toString());
                        String type = object.optString("type");

                        //处理报表请求
                        if ("request".equals(type)) {
                            Object[] array = server.clientChannel.keySet().toArray();

                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("type", "response");
                            jsonObject.put("message", array);

                            ctx.writeAndFlush(jsonObject.toString());
                        }
                        //处理登录请求
                        if ("login".equals(type)) {
                            String name = object.optString("name");
                            String pass = object.optString("pass");

                            DBHelper.addUser(name, pass);
                            server.clientChannel.put(name, ctx.channel());
                            ctx.writeAndFlush("登陆成功");
                        }
                        //处理聊天请求
                        if ("chat".equals(type)) {
                            String from = object.optString("from");
                            String to = object.optString("to");
                            String message = object.optString("message");
                            server.clientChannel.put(from, ctx.channel());

                            if (from.equals(to)) {
                                //如果发送人和接收人一样，直接发送
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("type", "chat");
                                jsonObject.put("from", from);
                                jsonObject.put("message", message);

                                ctx.writeAndFlush(jsonObject.toString());
                            } else {
                                //如果发送人和接收人不一样，则需要搜索channel然后发送
                                Channel toChannel = server.clientChannel.get(to);
                                if (toChannel != null) {
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("type", "chat");
                                    jsonObject.put("message", message);
                                    jsonObject.put("from", from);
                                    toChannel.writeAndFlush(jsonObject.toString());
                                    System.out.println("toChannel != null");
                                }
                            }
                        }
                    }
                });
            }
        });
    }
}