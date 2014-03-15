/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.example.telnet;

import java.nio.charset.Charset;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * Creates a newly configured {@link ChannelPipeline} for a new channel.
 */
public class TelnetServerInitializer extends ChannelInitializer<SocketChannel> {
    private static final StringDecoder DECODER = new StringDecoder(Charset.forName("GBK"));
    private static final StringEncoder ENCODER = new StringEncoder(Charset.forName("GBK"));
    private static final TelnetServerHandler SERVERHANDLER = new TelnetServerHandler();

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //framedecode的作用是按协议正确的组织字节流，比如telnet协议是基于一行文本的
        //DelimiterBasedFrameDecoder就可以根据换行符切割字节流，把两个换行符之间的字节流
        //组装成一个消息，交给下一个handler
        // Add the text line codec combination first,
        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(
                8192, Delimiters.lineDelimiter()));
        // the encoder and decoder are static as these are sharable
        //字节流被分割组装好之后，StringDecoder可以把字节流转化为字符串
        pipeline.addLast("decoder", DECODER);
        //我一直无法理解encoder的作用，现实的情况是，如果没有encoder，我使用tenlet命令行时
        //我在客户端的输入无法现实，而加上encoder，输入就正常显示，不知道encoder到底如何起作用的
        // and then business logic.
        pipeline.addLast("encoder", ENCODER);
        pipeline.addLast("handler", SERVERHANDLER);
        //经过测试发现，如果点到encoder和handler的顺序，则客户端显示不正常
        //而实际的调用顺序却是客户端发信息过来只会触发framer，decoder，hanlder，
        //只有handler返回信息给客户端才会触发encoder。既然如何，为什么顺序encoder在handler之前呢
       
    }
}
