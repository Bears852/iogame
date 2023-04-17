/*
 * # iohao.com . 渔民小镇
 * Copyright (C) 2021 - 2023 double joker （262610965@qq.com） . All Rights Reserved.
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
package com.iohao.game.action.skeleton.core.flow.parser;

import com.iohao.game.action.skeleton.core.ActionCommand;
import com.iohao.game.action.skeleton.core.DataCodecKit;
import com.iohao.game.action.skeleton.protocol.wrapper.ByteValueList;
import com.iohao.game.common.kit.CollKit;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 默认解析器
 *
 * @author 渔民小镇
 * @date 2022-06-26
 */

class DefaultMethodParser implements MethodParser {
    @Override
    public Class<?> getActualClazz(ActionCommand.MethodParamResultInfo methodParamResultInfo) {
        return methodParamResultInfo.getActualTypeArgumentClazz();
    }

    @Override
    public Object parseParam(byte[] data, ActionCommand.ParamInfo paramInfo) {
        Class<?> actualTypeArgumentClazz = paramInfo.getActualTypeArgumentClazz();

        if (paramInfo.isList()) {
            if (Objects.isNull(data)) {
                return Collections.emptyList();
            }

            ByteValueList byteValueList = DataCodecKit.decode(data, ByteValueList.class);

            if (CollKit.isEmpty(byteValueList.values)) {
                return Collections.emptyList();
            }

            return byteValueList.values.stream()
                    .map(bytes -> DataCodecKit.decode(bytes, actualTypeArgumentClazz))
                    .toList();
        }

        if (Objects.isNull(data)) {
            // 如果配置了 action 参数类型的 Supplier，则通过 Supplier 来创建对象
            var o = MethodParsers.me().newObject(actualTypeArgumentClazz);
            if (Objects.nonNull(o)) {
                return o;
            }
        }

        return DataCodecKit.decode(data, actualTypeArgumentClazz);
    }

    @Override
    public Object parseResult(ActionCommand.ActionMethodReturnInfo actionMethodReturnInfo, Object methodResult) {

        if (actionMethodReturnInfo.isList()) {

            List<Object> list = (List<Object>) methodResult;

            ByteValueList byteValueList = new ByteValueList();
            byteValueList.values = list.stream()
                    .map(DataCodecKit::encode)
                    .collect(Collectors.toList());

            return byteValueList;
        }

        return methodResult;
    }

    @Override
    public boolean isCustomMethodParser() {
        return false;
    }

    private DefaultMethodParser() {

    }

    public static DefaultMethodParser me() {
        return Holder.ME;
    }


    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final DefaultMethodParser ME = new DefaultMethodParser();
    }
}
