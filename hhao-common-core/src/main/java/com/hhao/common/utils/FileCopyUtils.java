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
import java.nio.file.Files;

/**
 * The type File copy utils.
 *
 * @author Wang
 * @since 1.0.0
 */
public  class FileCopyUtils {

    /**
     * The constant BUFFER_SIZE.
     */
    public static final int BUFFER_SIZE = StreamUtils.BUFFER_SIZE;


    //---------------------------------------------------------------------
    // Copy methods for java.io.File
    //---------------------------------------------------------------------


    /**
     * Copy int.
     *
     * @param in  the in
     * @param out the out
     * @return the int
     * @throws IOException the io exception
     */
    public static int copy(File in, File out) throws IOException {
        Assert.notNull(in, "No input File specified");
        Assert.notNull(out, "No output File specified");
        return copy(Files.newInputStream(in.toPath()), Files.newOutputStream(out.toPath()));
    }

    /**
     * Copy.
     *
     * @param in  the in
     * @param out the out
     * @throws IOException the io exception
     */
    public static void copy(byte[] in, File out) throws IOException {
        Assert.notNull(in, "No input byte array specified");
        Assert.notNull(out, "No output File specified");
        copy(new ByteArrayInputStream(in), Files.newOutputStream(out.toPath()));
    }

    /**
     * Copy to byte array byte [ ].
     *
     * @param in the in
     * @return the byte [ ]
     * @throws IOException the io exception
     */
    public static byte[] copyToByteArray(File in) throws IOException {
        Assert.notNull(in, "No input File specified");
        return copyToByteArray(Files.newInputStream(in.toPath()));
    }


    //---------------------------------------------------------------------
    // Copy methods for java.io.InputStream / java.io.OutputStream
    //---------------------------------------------------------------------


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

        try {
            return StreamUtils.copy(in, out);
        }
        finally {
            close(in);
            close(out);
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

        try {
            out.write(in);
        }
        finally {
            close(out);
        }
    }

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


    //---------------------------------------------------------------------
    // Copy methods for java.io.Reader / java.io.Writer
    //---------------------------------------------------------------------

    /**
     * Copy int.
     *
     * @param in  the in
     * @param out the out
     * @return the int
     * @throws IOException the io exception
     */
    public static int copy(Reader in, Writer out) throws IOException {
        Assert.notNull(in, "No Reader specified");
        Assert.notNull(out, "No Writer specified");

        try {
            int charCount = 0;
            char[] buffer = new char[BUFFER_SIZE];
            int charsRead;
            while ((charsRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, charsRead);
                charCount += charsRead;
            }
            out.flush();
            return charCount;
        }
        finally {
            close(in);
            close(out);
        }
    }

    /**
     * Copy.
     *
     * @param in  the in
     * @param out the out
     * @throws IOException the io exception
     */
    public static void copy(String in, Writer out) throws IOException {
        Assert.notNull(in, "No input String specified");
        Assert.notNull(out, "No Writer specified");

        try {
            out.write(in);
        }
        finally {
            close(out);
        }
    }

    /**
     * Copy to string string.
     *
     * @param in the in
     * @return the string
     * @throws IOException the io exception
     */
    public static String copyToString( Reader in) throws IOException {
        if (in == null) {
            return "";
        }

        StringWriter out = new StringWriter(BUFFER_SIZE);
        copy(in, out);
        return out.toString();
    }

    private static void close(Closeable closeable) {
        try {
            closeable.close();
        }
        catch (IOException ex) {
            // ignore
        }
    }

}
