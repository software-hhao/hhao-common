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
package com.hhao.common.utils;


import java.io.*;
import java.nio.charset.Charset;


/**
 * The type Stream utils.
 *
 * @author Wang
 * @since 1.0.0
 */
public  class StreamUtils {

    /**
     * The default buffer size used when copying bytes.
     */
    public static final int BUFFER_SIZE = 4096;

    private static final byte[] EMPTY_CONTENT = new byte[0];


    /**
     * Copy to byte array byte [ ].
     *
     * @param in the in
     * @return the byte [ ]
     * @throws IOException the io exception
     */
    public static byte[] copyToByteArray( InputStream in) throws IOException {
        if (in == null) {
            return new byte[0];
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream(BUFFER_SIZE);
        copy(in, out);
        return out.toByteArray();
    }

    /**
     * Copy to string string.
     *
     * @param in      the in
     * @param charset the charset
     * @return the string
     * @throws IOException the io exception
     */
    public static String copyToString( InputStream in, Charset charset) throws IOException {
        if (in == null) {
            return "";
        }

        StringBuilder out = new StringBuilder(BUFFER_SIZE);
        InputStreamReader reader = new InputStreamReader(in, charset);
        char[] buffer = new char[BUFFER_SIZE];
        int charsRead;
        while ((charsRead = reader.read(buffer)) != -1) {
            out.append(buffer, 0, charsRead);
        }
        return out.toString();
    }


    /**
     * Copy to string string.
     *
     * @param baos    the baos
     * @param charset the charset
     * @return the string
     */
    public static String copyToString(ByteArrayOutputStream baos, Charset charset) {
        Assert.notNull(baos, "No ByteArrayOutputStream specified");
        Assert.notNull(charset, "No Charset specified");
        try {
            // Can be replaced with toString(Charset) call in Java 10+
            return baos.toString(charset.name());
        }
        catch (UnsupportedEncodingException ex) {
            // Should never happen
            throw new IllegalArgumentException("Invalid charset name: " + charset, ex);
        }
    }

    /**
     * Copy.
     *
     * @param in  the in
     * @param out the out
     * @throws IOException the io exception
     */
    public static void copy(byte[] in, OutputStream out) throws IOException {
        Assert.notNull(in, "No input byte array specified");
        Assert.notNull(out, "No OutputStream specified");

        out.write(in);
        out.flush();
    }

    /**
     * Copy.
     *
     * @param in      the in
     * @param charset the charset
     * @param out     the out
     * @throws IOException the io exception
     */
    public static void copy(String in, Charset charset, OutputStream out) throws IOException {
        Assert.notNull(in, "No input String specified");
        Assert.notNull(charset, "No Charset specified");
        Assert.notNull(out, "No OutputStream specified");

        Writer writer = new OutputStreamWriter(out, charset);
        writer.write(in);
        writer.flush();
    }


    /**
     * Copy int.
     *
     * @param in  the in
     * @param out the out
     * @return the int
     * @throws IOException the io exception
     */
    public static int copy(InputStream in, OutputStream out) throws IOException {
        Assert.notNull(in, "No InputStream specified");
        Assert.notNull(out, "No OutputStream specified");

        int byteCount = 0;
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
            byteCount += bytesRead;
        }
        out.flush();
        return byteCount;
    }


    /**
     * Copy range long.
     *
     * @param in    the in
     * @param out   the out
     * @param start the start
     * @param end   the end
     * @return the long
     * @throws IOException the io exception
     */
    public static long copyRange(InputStream in, OutputStream out, long start, long end) throws IOException {
        Assert.notNull(in, "No InputStream specified");
        Assert.notNull(out, "No OutputStream specified");

        long skipped = in.skip(start);
        if (skipped < start) {
            throw new IOException("Skipped only " + skipped + " bytes out of " + start + " required");
        }

        long bytesToCopy = end - start + 1;
        byte[] buffer = new byte[(int) Math.min(StreamUtils.BUFFER_SIZE, bytesToCopy)];
        while (bytesToCopy > 0) {
            int bytesRead = in.read(buffer);
            if (bytesRead == -1) {
                break;
            }
            else if (bytesRead <= bytesToCopy) {
                out.write(buffer, 0, bytesRead);
                bytesToCopy -= bytesRead;
            }
            else {
                out.write(buffer, 0, (int) bytesToCopy);
                bytesToCopy = 0;
            }
        }
        return (end - start + 1 - bytesToCopy);
    }

    /**
     * Drain int.
     *
     * @param in the in
     * @return the int
     * @throws IOException the io exception
     */
    public static int drain(InputStream in) throws IOException {
        Assert.notNull(in, "No InputStream specified");
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = -1;
        int byteCount = 0;
        while ((bytesRead = in.read(buffer)) != -1) {
            byteCount += bytesRead;
        }
        return byteCount;
    }

    /**
     * Empty input input stream.
     *
     * @return the input stream
     */
    public static InputStream emptyInput() {
        return new ByteArrayInputStream(EMPTY_CONTENT);
    }


    /**
     * Non closing input stream.
     *
     * @param in the in
     * @return the input stream
     */
    public static InputStream nonClosing(InputStream in) {
        Assert.notNull(in, "No InputStream specified");
        return new NonClosingInputStream(in);
    }

    /**
     * Non closing output stream.
     *
     * @param out the out
     * @return the output stream
     */
    public static OutputStream nonClosing(OutputStream out) {
        Assert.notNull(out, "No OutputStream specified");
        return new NonClosingOutputStream(out);
    }


    private static class NonClosingInputStream extends FilterInputStream {

        /**
         * Instantiates a new Non closing input stream.
         *
         * @param in the in
         */
        public NonClosingInputStream(InputStream in) {
            super(in);
        }

        @Override
        public void close() throws IOException {
        }
    }


    private static class NonClosingOutputStream extends FilterOutputStream {

        /**
         * Instantiates a new Non closing output stream.
         *
         * @param out the out
         */
        public NonClosingOutputStream(OutputStream out) {
            super(out);
        }

        @Override
        public void write(byte[] b, int off, int let) throws IOException {
            // It is critical that we override this method for performance
            this.out.write(b, off, let);
        }

        @Override
        public void close() throws IOException {
        }
    }

}
