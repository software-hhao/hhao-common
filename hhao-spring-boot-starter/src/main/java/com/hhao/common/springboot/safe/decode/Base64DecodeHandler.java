/*
 * Copyright 2018-2021 WangSheng.
 *
 * Licensed under the GNU GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hhao.common.springboot.safe.decode;

import com.hhao.common.springboot.exception.entity.request.DecodeException;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 基于Base64的解码器
 *
 * @author Wang
 * @since 1.0.0
 */
public class Base64DecodeHandler implements DecodeHandler {
    /**
     * The constant NAME.
     */
    public static final String NAME="base64";

    @Override
    public boolean support(String name) {
        return NAME.equals(name);
    }

    @Override
    public String decode(@NonNull String content) {
        byte [] raw= new byte[0];
        try {
            Assert.notNull(content,"content not null");
            raw = Base64.getDecoder().decode(content.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new DecodeException(e);
        }
        return new String(raw);
    }
}
