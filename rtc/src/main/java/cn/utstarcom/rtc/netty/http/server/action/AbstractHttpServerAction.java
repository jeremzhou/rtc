package cn.utstarcom.rtc.netty.http.server.action;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import cn.utstarcom.rtc.netty.util.MediaType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.CharsetUtil;

/**
 * @author UTSC0167
 * @date 2017年9月5日
 *
 */
public abstract class AbstractHttpServerAction implements IHttpServerAction {

    /**
     * get the request content
     * 
     * @param request
     *            FullHttpRequest
     * 
     * @return the request content
     */
    protected String getContent(FullHttpRequest request) {

        return request.content().toString(CharsetUtil.UTF_8);
    }

    /**
     * build response object and send to client
     * 
     * @param ctx
     *            ChannelHandlerContext
     * @param status
     *            HttpResponseStatus
     */
    protected final void doResponse(FullHttpRequest request, ChannelHandlerContext ctx,
            HttpResponseStatus status) {

        // Build the response object.
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status);

        // send response
        sendResponse(request, ctx, response);

    }

    protected final void doResponse(FullHttpRequest request, ChannelHandlerContext ctx,
            FullHttpResponse response) {

        if (response.headers().get(HttpHeaderNames.CONTENT_LENGTH) == null)
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,
                    response.content().readableBytes());

        // send response
        sendResponse(request, ctx, response);

    }

    /**
     * build response object and send to client
     * 
     * @param ctx
     *            ChannelHandlerContext
     * 
     * @param responseContent
     *            the response content
     */
    protected final void doResponse(FullHttpRequest request, ChannelHandlerContext ctx,
            String responseContent) {

        this.doResponse(request, ctx, responseContent, MediaType.TEXT_PLAIN_VALUE);
    }

    /**
     * build response string object and send to client
     * 
     * @param ctx
     *            ChannelHandlerContext
     * 
     * @param responseContent
     *            the response string content
     * 
     * @param contentType
     *            http response content type
     */
    protected final void doResponse(FullHttpRequest request, ChannelHandlerContext ctx,
            String responseContent, String contentType) {

        // convert content
        ByteBuf byteBuf = Unpooled.copiedBuffer(responseContent, CharsetUtil.UTF_8);
        handleResponse(request, ctx, byteBuf, contentType, false);
    }

    /**
     * build response byte[] object and send to client
     * 
     * @param ctx
     *            ChannelHandlerContext
     * 
     * @param responseContent
     *            the byte[] response content
     * 
     * @param contentType
     *            http response content type
     * @param enableGzip
     *            whether http gzip is enabled
     */
    protected final void doResponse(FullHttpRequest request, ChannelHandlerContext ctx,
            byte[] responseContent, String contentType, boolean enableGzip) {

        // convert content
        ByteBuf byteBuf = Unpooled.copiedBuffer(responseContent);
        // ByteBuf byteBuf = Unpooled.directBuffer(responseContent.length);
        // byteBuf.writeBytes(responseContent);

        handleResponse(request, ctx, byteBuf, contentType, enableGzip);
    }

    /**
     * 根据http URI生成key-value parameter
     * 
     * @param uri
     *            the http URI
     * @return Returns the decoded key-value parameter pairs of the URI.
     */
    protected Map<String, List<String>> generateParams(String uri) {

        return new QueryStringDecoder(uri).parameters();
    }

    /**
     * get the parameter value
     * 
     * @param params
     *            the key-value parameter
     * @param key
     *            the key
     * @return the key value. if the don't exists, return null
     */
    protected Optional<String> getParamValues(Map<String, List<String>> params, String key) {

        Optional<String> value = Optional.empty();
        List<String> nameValues = params.get(key);
        if (nameValues != null && nameValues.size() > 0)
            value = Optional.of(nameValues.get(0));

        return value;
    }

    private final void handleResponse(FullHttpRequest request, ChannelHandlerContext ctx,
            ByteBuf byteBuf, String contentType, boolean enableGzip) {

        // Build the response object.
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK, byteBuf);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, contentType);
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());
        if (enableGzip)
            response.headers().set(HttpHeaderNames.CONTENT_ENCODING, "gzip");

        // send response
        sendResponse(request, ctx, response);
    }

    private final void sendResponse(FullHttpRequest request, ChannelHandlerContext ctx,
            FullHttpResponse response) {

        if (HttpUtil.isKeepAlive(request)) {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            ctx.writeAndFlush(response);
        } else {
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }
    }
}
