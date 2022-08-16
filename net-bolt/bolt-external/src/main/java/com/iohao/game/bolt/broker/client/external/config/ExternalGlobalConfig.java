/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2022 double joker （262610965@qq.com） . All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.iohao.game.bolt.broker.client.external.config;

import lombok.experimental.UtilityClass;

/**
 * @author 渔民小镇
 * @date 2022-03-22
 */
@UtilityClass
public class ExternalGlobalConfig {
    /** true 表示请求业务方法需要先登录 */
    @Deprecated
    public boolean verifyIdentity = true;
    /** 访问验证钩子接口 */
    public AccessAuthenticationHook accessAuthenticationHook = new DefaultAccessAuthenticationHook();
}