package com.jinglitong.springshop.md5;

import java.io.*;

/**
 * @ClassName CodecSupport
 * @Description TODO
 * @Author zili.zong
 * @Date 2019/1/15 16:08
 * @Version 1.0
 **/
public abstract class CodecSupport {
    public static final String PREFERRED_ENCODING = "UTF-8";

    public CodecSupport() {
    }

    public static byte[] toBytes(char[] chars) throws Exception{
        return toBytes(new String(chars), "UTF-8");
    }

    public static byte[] toBytes(char[] chars, String encoding) throws Exception {
        return toBytes(new String(chars), encoding);
    }

    public static byte[] toBytes(String source) throws Exception{
        return toBytes(source, "UTF-8");
    }

    public static byte[] toBytes(String source, String encoding) throws Exception {
        try {
            return source.getBytes(encoding);
        } catch (UnsupportedEncodingException var4) {
            String msg = "Unable to convert source [" + source + "] to byte array using " + "encoding '" + encoding + "'";
            throw new Exception(msg, var4);
        }
    }

    public static String toString(byte[] bytes) throws Exception{
        return toString(bytes, "UTF-8");
    }

    public static String toString(byte[] bytes, String encoding) throws Exception {
        try {
            return new String(bytes, encoding);
        } catch (UnsupportedEncodingException var4) {
            String msg = "Unable to convert byte array to String with encoding '" + encoding + "'.";
            throw new RuntimeException(msg, var4);
        }
    }

    public static char[] toChars(byte[] bytes) throws Exception{
        return toChars(bytes, "UTF-8");
    }

    public static char[] toChars(byte[] bytes, String encoding) throws Exception {
        return toString(bytes, encoding).toCharArray();
    }

    protected boolean isByteSource(Object o) {
        return o instanceof byte[] || o instanceof char[] || o instanceof String || o instanceof ByteSource || o instanceof File || o instanceof InputStream;
    }

    protected byte[] toBytes(Object o) {
        if (o == null) {
            String msg = "Argument for byte conversion cannot be null.";
            throw new IllegalArgumentException(msg);
        } else if (o instanceof byte[]) {
            return (byte[])((byte[])o);
        } else if (o instanceof ByteSource) {
            return ((ByteSource)o).getBytes();
        } else if (o instanceof char[]) {
            try {
                return toBytes((char[])((char[])o));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        } else if (o instanceof String) {
            try {
                return toBytes((String)o);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        } else if (o instanceof File) {
            return this.toBytes((File)o);
        } else {
            return o instanceof InputStream ? this.toBytes((InputStream)o) : this.objectToBytes(o);
        }
    }

    protected String toString(Object o) throws Exception{
        if (o == null) {
            String msg = "Argument for String conversion cannot be null.";
            throw new IllegalArgumentException(msg);
        } else if (o instanceof byte[]) {
            return toString((byte[])((byte[])o));
        } else if (o instanceof char[]) {
            return new String((char[])((char[])o));
        } else {
            return o instanceof String ? (String)o : this.objectToString(o);
        }
    }

    protected byte[] toBytes(File file) {
        if (file == null) {
            throw new IllegalArgumentException("File argument cannot be null.");
        } else {
            try {
                return this.toBytes((InputStream)(new FileInputStream(file)));
            } catch (FileNotFoundException var4) {
                String msg = "Unable to acquire InputStream for file [" + file + "]";
                throw new RuntimeException(msg, var4);
            }
        }
    }

    protected byte[] toBytes(InputStream in) {
        if (in == null) {
            throw new IllegalArgumentException("InputStream argument cannot be null.");
        } else {
            boolean BUFFER_SIZE = true;
            ByteArrayOutputStream out = new ByteArrayOutputStream(512);
            byte[] buffer = new byte[512];

            byte[] var6;
            try {
                int bytesRead;
                while((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }

                var6 = out.toByteArray();
            } catch (IOException var18) {
                throw new RuntimeException(var18);
            } finally {
                try {
                    in.close();
                } catch (IOException var17) {
                    ;
                }

                try {
                    out.close();
                } catch (IOException var16) {
                    ;
                }

            }

            return var6;
        }
    }

    protected byte[] objectToBytes(Object o) {
        String msg = "The " + this.getClass().getName() + " implementation only supports conversion to " + "byte[] if the source is of type byte[], char[], String, " + ByteSource.class.getName() + " File or InputStream.  The instance provided as a method " + "argument is of type [" + o.getClass().getName() + "].  If you would like to convert " + "this argument type to a byte[], you can 1) convert the argument to one of the supported types " + "yourself and then use that as the method argument or 2) subclass " + this.getClass().getName() + "and override the objectToBytes(Object o) method.";
        throw new RuntimeException(msg);
    }

    protected String objectToString(Object o) {
        return o.toString();
    }
}
