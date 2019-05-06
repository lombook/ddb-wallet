package com.jinglitong.springshop.md5;


import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
/**
 * @ClassName SimpleByteSource
 * @Description TODO
 * @Author zili.zong
 * @Date 2019/1/15 16:33
 * @Version 1.0
 **/
public class SimpleByteSource implements ByteSource {
    private final byte[] bytes;
    private String cachedHex;
    private String cachedBase64;

    public SimpleByteSource(byte[] bytes) {
        this.bytes = bytes;
    }

    public SimpleByteSource(char[] chars) throws Exception {
        this.bytes = CodecSupport.toBytes(chars);
    }

    public SimpleByteSource(String string) throws Exception {
        this.bytes = CodecSupport.toBytes(string);
    }

    public SimpleByteSource(ByteSource source) {
        this.bytes = source.getBytes();
    }

    public SimpleByteSource(File file) {
        this.bytes = (new SimpleByteSource.BytesHelper()).getBytes(file);
    }

    public SimpleByteSource(InputStream stream) {
        this.bytes = (new SimpleByteSource.BytesHelper()).getBytes(stream);
    }

    public static boolean isCompatible(Object o) {
        return o instanceof byte[] || o instanceof char[] || o instanceof String || o instanceof ByteSource || o instanceof File || o instanceof InputStream;
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    public boolean isEmpty() {
        return this.bytes == null || this.bytes.length == 0;
    }

    public String toHex() {
        if (this.cachedHex == null) {
            this.cachedHex = Hex.encodeToString(this.getBytes());
        }

        return this.cachedHex;
    }

    public String toBase64() {
        if (this.cachedBase64 == null) {
            try {
                this.cachedBase64 = Base64.encodeToString(this.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return this.cachedBase64;
    }

    public String toString() {
        return this.toBase64();
    }

    public int hashCode() {
        return this.bytes != null && this.bytes.length != 0 ? Arrays.hashCode(this.bytes) : 0;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof ByteSource) {
            ByteSource bs = (ByteSource)o;
            return Arrays.equals(this.getBytes(), bs.getBytes());
        } else {
            return false;
        }
    }

    private static final class BytesHelper extends CodecSupport {
        private BytesHelper() {
        }

        public byte[] getBytes(File file) {
            return this.toBytes(file);
        }

        public byte[] getBytes(InputStream stream) {
            return this.toBytes(stream);
        }
    }
}

