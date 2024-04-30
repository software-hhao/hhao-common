/*
 * Copyright 2008-2024 wangsheng
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hhao.common.springboot.web.config.filter;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

/**
 * response的包装过滤
 *
 * @author Wang
 * @since 1.0.0
 */
public class CachingResponseWrapper extends ContentCachingResponseWrapper {
    /**
     * Create a new ContentCachingResponseWrapper for the given servlet response.
     *
     * @param response the original servlet response
     */
    public CachingResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    /**
     * Flush buffer.
     *
     * @throws IOException the io exception
     */
    @Override
    public void flushBuffer() throws IOException {
        // do not flush the underlying response as the content has not been copied to it yet
    }


}
