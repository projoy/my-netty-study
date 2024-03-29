/*
 * Copyright 2013 The Netty Project
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
package io.netty.bootstrap;

import io.netty.channel.Channel;

/**
 * Factory that creates a new {@link Channel} on {@link Bootstrap#bind()}, {@link Bootstrap#connect()}, and
 * {@link ServerBootstrap#bind()}.
 * <p>在bootstrap的bind()和connect()方法上新建一个channel的工厂。应该是在这两个方法调用前吧，因为bind和connect需要的是现有连接。</p>
 */
public interface ChannelFactory<T extends Channel> {
    /**
     * Creates a new channel.
     */
    T newChannel();
}
