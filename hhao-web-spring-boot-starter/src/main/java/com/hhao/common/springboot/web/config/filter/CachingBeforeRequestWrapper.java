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

import com.hhao.common.exception.error.request.PayloadLengthException;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.WebUtils;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * 缓存request
 * 如果采用spring自已的ContentCachingRequestWrapper，有局限性，只能在doFilter调用链之后才能访问getContentAsByteArray，但是性能会好些
 * 本类采用了doFilter调用链前缓存，这样可以在采用本包装类后就可以调用getContentAsByteArray，但是会占用多一份的内存
 *
 * @author Wang
 * @since 1.0.0
 */
public class CachingBeforeRequestWrapper extends HttpServletRequestWrapper {
    private static final String FORM_CONTENT_TYPE = "application/x-www-form-urlencoded";
    /**
     * 缓存器
     */
    private final ByteArrayOutputStream cachedContent;
    /**
     * 缓存器长度限制
     */
    @NotNull
    private final Integer contentCacheLimit;

    /**
     * 封装后的输入流
     */

    private ServletInputStream inputStream;

    /**
     * 封装后的Reader
     */

    private BufferedReader reader;

    /**
     * Instantiates a new Caching before request wrapper.
     *
     * @param request           the request
     * @param contentCacheLimit the content cache limit
     */
    public CachingBeforeRequestWrapper(HttpServletRequest request, int contentCacheLimit) {
        super(request);
        int contentLength = request.getContentLength();
        //如果内容长度大于限制长度，抛出错误
        if (contentLength > contentCacheLimit) {
            handleContentOverflow(contentCacheLimit);
        }
        this.cachedContent = new ByteArrayOutputStream(contentLength < 0 ? 0 : contentLength);
        this.contentCacheLimit = contentCacheLimit;

        //缓存操作
        if (isFormPost()) {
            //表单方式内容的缓存
            writeRequestParametersToCachedContent();
        } else {
            //其它方式的内容缓存
            //注意：如果是文件上传、下载可能要做一些限制
            writePayloadToCachedContent();
        }
    }

    /**
     * Gets input stream.
     *
     * @return the input stream
     * @throws IOException the io exception
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (this.inputStream == null) {
            this.inputStream = new ContentCachingInputStream(this.getContentAsByteArray());
        }
        return this.inputStream;
    }

    /**
     * Gets character encoding.
     *
     * @return the character encoding
     */
    @Override
    public String getCharacterEncoding() {
        String enc = super.getCharacterEncoding();
        return (enc != null ? enc : WebUtils.DEFAULT_CHARACTER_ENCODING);
    }

    /**
     * Gets reader.
     *
     * @return the reader
     * @throws IOException the io exception
     */
    @Override
    public BufferedReader getReader() throws IOException {
        if (this.reader == null) {
            this.reader = new BufferedReader(new InputStreamReader(getInputStream(), getCharacterEncoding()));
        }
        return this.reader;
    }

    /**
     * Gets parameter.
     *
     * @param name the name
     * @return the parameter
     */
    @Override
    public String getParameter(String name) {
        return super.getParameter(name);
    }

    /**
     * Gets parameter map.
     *
     * @return the parameter map
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        return super.getParameterMap();
    }

    /**
     * Gets parameter names.
     *
     * @return the parameter names
     */
    @Override
    public Enumeration<String> getParameterNames() {
        return super.getParameterNames();
    }

    /**
     * Get parameter values string [ ].
     *
     * @param name the name
     * @return the string [ ]
     */
    @Override
    public String[] getParameterValues(String name) {
        return super.getParameterValues(name);
    }


    private boolean isFormPost() {
        String contentType = getContentType();
        return (contentType != null && contentType.contains(FORM_CONTENT_TYPE) &&
                HttpMethod.POST.matches(getMethod()));
    }

    private void writeRequestParametersToCachedContent() {
        try {
            if (this.cachedContent.size() == 0) {
                String requestEncoding = getCharacterEncoding();
                Map<String, String[]> form = super.getParameterMap();
                for (Iterator<String> nameIterator = form.keySet().iterator(); nameIterator.hasNext(); ) {
                    String name = nameIterator.next();
                    List<String> values = Arrays.asList(form.get(name));
                    for (Iterator<String> valueIterator = values.iterator(); valueIterator.hasNext(); ) {
                        String value = valueIterator.next();
                        this.cachedContent.write(URLEncoder.encode(name, requestEncoding).getBytes());
                        if (value != null) {
                            this.cachedContent.write('=');
                            this.cachedContent.write(URLEncoder.encode(value, requestEncoding).getBytes());
                            if (valueIterator.hasNext()) {
                                this.cachedContent.write('&');
                            }
                        }
                    }
                    if (nameIterator.hasNext()) {
                        this.cachedContent.write('&');
                    }
                }
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to write request parameters to cached content", ex);
        }
    }

    private void writePayloadToCachedContent() {
        try {
            ServletInputStream is = this.getRequest().getInputStream();
            int ch = -1;
            boolean overflow = false;

            while ((ch = is.read()) != -1 && !overflow) {
                if (contentCacheLimit != null && cachedContent.size() == contentCacheLimit) {
                    overflow = true;
                    break;
                } else {
                    cachedContent.write(ch);
                }
            }
            if (overflow) {
                handleContentOverflow(contentCacheLimit);
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to write request payload to cached content", ex);
        }
    }


    /**
     * Return the cached request content as a byte array.
     * <p>The returned array will never be larger than the content cache limit.
     * <p><strong>Note:</strong> The byte array returned from this method
     * reflects the amount of content that has has been read at the time when it
     * is called. If the application does not read the content, this method
     * returns an empty array.
     *
     * @return the byte [ ]
     */
    public byte[] getContentAsByteArray() {
        return this.cachedContent.toByteArray();
    }

    /**
     * Template method for handling a content overflow: specifically, a request
     * body being read that exceeds the specified content cache limit.
     * <p>The default implementation is empty. Subclasses may override this to
     * throw a payload-too-large exception or the like.
     *
     * @param contentCacheLimit the maximum number of bytes to cache per request which has just been exceeded
     */
    protected void handleContentOverflow(int contentCacheLimit) {
        throw new PayloadLengthException(contentCacheLimit);
    }


    private static class ContentCachingInputStream extends ServletInputStream {
        private final ByteArrayInputStream is;

        /**
         * Instantiates a new Content caching input stream.
         *
         * @param payload the payload
         */
        public ContentCachingInputStream(byte[] payload) {
            this.is = new ByteArrayInputStream(payload);
        }

        /**
         * Read int.
         *
         * @return the int
         * @throws IOException the io exception
         */
        @Override
        public int read() throws IOException {
            return this.is.read();
        }

        /**
         * Read int.
         *
         * @param b the b
         * @return the int
         * @throws IOException the io exception
         */
        @Override
        public int read(@NotNull byte[] b) throws IOException {
            int count = this.is.read(b);
            return count;
        }

        /**
         * Read int.
         *
         * @param b   the b
         * @param off the off
         * @param len the len
         * @return the int
         * @throws IOException the io exception
         */
        @Override
        public int read(@NotNull final byte[] b, final int off, final int len) throws IOException {
            int count = this.is.read(b, off, len);
            return count;
        }

        /**
         * Is finished boolean.
         *
         * @return the boolean
         */
        @Override
        public boolean isFinished() {
            return false;
        }

        /**
         * Is ready boolean.
         *
         * @return the boolean
         */
        @Override
        public boolean isReady() {
            return true;
        }

        /**
         * Sets read listener.
         *
         * @param readListener the read listener
         */
        @Override
        public void setReadListener(ReadListener readListener) {

        }
    }
}
