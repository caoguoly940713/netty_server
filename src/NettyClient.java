import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NettyClient {

    private static String makeJson2(String s1, String s2) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "chat");
            jsonObject.put("from", "test");
            jsonObject.put("to", s1);
            jsonObject.put("message", s2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    private static String makeJson1(String s1, String s2) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "login");
            jsonObject.put("name", s1);
            jsonObject.put("pass", s2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static void main(String[] args) {

        ArrayList<ChannelHandler> handlers = new ArrayList<>();
        handlers.add(new StringDecoder());
        handlers.add(new StringEncoder());
//        handlers.add(new ChannelInboundHandlerAdapter(){
//            @Override
//            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//                super.channelRead(ctx, msg);
//            }
//        });

        ChannelFuture channelFuture = NettyHelper.makeConnect(new ArrayList<>());
//        String messge = makeJson2("cgl","hello");
//        channelFuture.channel().writeAndFlush(messge);
    }
}