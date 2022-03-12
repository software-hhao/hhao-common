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

package com.hhao.common.springboot.web.config.filter;

import com.hhao.common.exception.error.request.PayloadLengthException;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.web.util.WebUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.*;

/**
 * The type Caching after request wrapper.
 *
 * @author Wang
 * @since 1.0.0
 */
public class CachingAfterRequestWrapper  extends HttpServletRequestWrapper {
    private static final String FORM_CONTENT_TYPE = "application/x-www-form-urlencoded";


    private final ByteArrayOutputStream cachedContent;

    @Nullable
    private final Integer contentCacheLimit;

    @Nullable
    private ServletInputStream inputStream;

    @Nullable
    private BufferedReader reader;


    /**
     * Create a new ContentCachingRequestWrapper for the given servlet request.
     *
     * @param request the original servlet request
     */
    public CachingAfterRequestWrapper(HttpServletRequest request) {
        super(request);
        int contentLength = request.getContentLength();
        this.cachedContent = new ByteArrayOutputStream(contentLength >= 0 ? contentLength : 1024);
        this.contentCacheLimit = null;
    }

    /**
     * Create a new ContentCachingRequestWrapper for the given servlet request.
     *
     * @param request           the original servlet request
     * @param contentCacheLimit the maximum number of bytes to cache per request
     * @see #handleContentOverflow(int) #handleContentOverflow(int)
     * @since 4.3.6
     */
    public CachingAfterRequestWrapper(HttpServletRequest request, int contentCacheLimit) {
        super(request);
        int contentLength = request.getContentLength();
        contentLength=contentLength >= 0 ? contentLength : 0;
        contentLength=contentLength>contentCacheLimit?contentCacheLimit:contentLength;
        this.cachedContent = new ByteArrayOutputStream(contentLength);
        this.contentCacheLimit = contentCacheLimit;
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
            this.inputStream = new ContentCachingInputStream(getRequest().getInputStream());
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
        if (this.cachedContent.size() == 0 && isFormPost()) {
            writeRequestParametersToCachedContent();
        }
        return super.getParameter(name);
    }

    /**
     * Gets parameter map.
     *
     * @return the parameter map
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        if (this.cachedContent.size() == 0 && isFormPost()) {
            writeRequestParametersToCachedContent();
        }
        return super.getParameterMap();
    }

    /**
     * Gets parameter names.
     *
     * @return the parameter names
     */
    @Override
    public Enumeration<String> getParameterNames() {
        if (this.cachedContent.size() == 0 && isFormPost()) {
            writeRequestParametersToCachedContent();
        }
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
        if (this.cachedContent.size() == 0 && isFormPost()) {
            writeRequestParametersToCachedContent();
        }
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
        }
        catch (IOException ex) {
            throw new IllegalStateException("Failed to write request parameters to cached content", ex);
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
     * @since 4.3.6
     */
    protected void handleContentOverflow(int contentCacheLimit) {
        throw new PayloadLengthException(contentCacheLimit);
    }


    private class ContentCachingInputStream extends ServletInputStream {

        private final ServletInputStream is;

        private boolean overflow = false;

        /**
         * Instantiates a new Content caching input stream.
         *
         * @param is the is
         */
        public ContentCachingInputStream(ServletInputStream is) {
            this.is = is;
        }

        /**
         * Read int.
         *
         * @return the int
         * @throws IOException the io exception
         */
        @Override
        public int read() throws IOException {
            int ch = this.is.read();
            if (ch != -1 && !this.overflow) {
                if (contentCacheLimit != null && cachedContent.size() == contentCacheLimit) {
                    this.overflow = true;
                    handleContentOverflow(contentCacheLimit);
                }
                else {
                    cachedContent.write(ch);
                }
            }
            return ch;
        }

        /**
         * Read int.
         *
         * @param b the b
         * @return the int
         * @throws IOException the io exception
         */
        @Override
        public int read(byte[] b) throws IOException {
            int count = this.is.read(b);
            writeToCache(b, 0, count);
            return count;
        }

        private void writeToCache(final byte[] b, final int off, int count) {
            if (!this.overflow && count > 0) {
                if (contentCacheLimit != null &&
                        count + cachedContent.size() > contentCacheLimit) {
                    this.overflow = true;
                    cachedContent.write(b, off, contentCacheLimit - cachedContent.size());
                    handleContentOverflow(contentCacheLimit);
                    return;
                }
                cachedContent.write(b, off, count);
            }
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
        public int read(final byte[] b, final int off, final int len) throws IOException {
            int count = this.is.read(b, off, len);
            writeToCache(b, off, count);
            return count;
        }

        /**
         * Read line int.
         *
         * @param b   the b
         * @param off the off
         * @param len the len
         * @return the int
         * @throws IOException the io exception
         */
        @Override
        public int readLine(final byte[] b, final int off, final int len) throws IOException {
            int count = this.is.readLine(b, off, len);
            writeToCache(b, off, count);
            return count;
        }

        /**
         * Is finished boolean.
         *
         * @return the boolean
         */
        @Override
        public boolean isFinished() {
            return this.is.isFinished();
        }

        /**
         * Is ready boolean.
         *
         * @return the boolean
         */
        @Override
        public boolean isReady() {
            return this.is.isReady();
        }

        /**
         * Sets read listener.
         *
         * @param readListener the read listener
         */
        @Override
        public void setReadListener(ReadListener readListener) {
            this.is.setReadListener(readListener);
        }
    }

}
