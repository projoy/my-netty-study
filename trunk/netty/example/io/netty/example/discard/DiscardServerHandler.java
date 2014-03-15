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
package io.netty.example.discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles a server-side channel.
 */
public class DiscardServerHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = Logger.getLogger(
            DiscardServerHandler.class.getName());

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // discard
    	//当我使用telnet连接的时候，发现msg的类型是bytebuf类型的，
    	//是谁决定了它的类型，为什么我打一个字它就会响应，而不是我输入回车后响应,是客户端造成的，还是服务器问题
    	logger.info(((ByteBuf)msg).toString(Charset.defaultCharset()));
    	System.out.println(((ByteBuf)msg).toString(Charset.defaultCharset()));
    	StackTraceElement[] s=Thread.currentThread().getStackTrace();
    	for (int i = 0; i < s.length; i++) {
			StackTraceElement stackTraceElement = s[i];
			System.out.println(stackTraceElement.getClassName()+"#"+stackTraceElement.getMethodName()
					+"  "+stackTraceElement.getFileName());
		}
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
            Throwable cause) throws Exception {
        // Close the connection when an exception is raised.
        logger.log(
                Level.WARNING,
                "Unexpected exception from downstream.",
                cause);
        //一般遇到错误之后，我们应该记录错误日志，然后关闭连接。下面的方法调用估计就是关闭连接用到的。
        //如果不是客户机关闭连接造成的异常，而是服务端错误，如http500错误，我们可以在关闭之前给客户端发送一个500的code
        ctx.close();
    }
}
