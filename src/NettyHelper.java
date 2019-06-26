import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.List;

public class NettyHelper {

    public static ChannelFuture makeConnect(final List<ChannelHandler> channelHandlers) {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        for (ChannelHandler channelHandler : channelHandlers) {
                            ch.pipeline().addLast(channelHandler);
                        }
                    }
                });

        return bootstrap.connect(Config.ADDRESS, Config.PORT);
    }
}