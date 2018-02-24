package com.lxx.mydemo.nettydemo.service.common.util;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.sun.media.sound.SoftTuning;
import com.sun.org.apache.regexp.internal.RE;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.Iterator;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * @author lixiaoxiong
 * @version 2018-02-12
 */
@Service
public class BCD8421Operater {

    private static final Splitter SPLITTER_COLON = Splitter.on(":");
    /**
     * BCD字节数组===>String
     *
     * @param bytes
     * @return 十进制字符串
     */
    public String bcd2String(byte[] bytes) {
        StringBuilder temp = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            // 高四位
            temp.append((bytes[i] & 0xf0) >>> 4);
            // 低四位
            temp.append(bytes[i] & 0x0f);
        }
        return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp.toString().substring(1) : temp.toString();
    }

    public String bcd2HexString(byte[] bytes) {
        StringBuilder temp = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            // 高四位
            temp.append(Integer.toHexString((bytes[i] & 0xf0) >>> 4).toUpperCase());
            // 低四位
            temp.append(Integer.toHexString(bytes[i] & 0x0f).toUpperCase());
            temp.append(":");
        }

        String result = temp.toString();
        return result.substring(0, result.length() -1);
    }

    /**
     *
     * @param hex BB
     * @return
     */
    public byte byteFromHexString(String hex) {
        if (Strings.isNullOrEmpty(hex)) {
            return 0;
        }
        char c0 = hex.charAt(0);
        char c1 = hex.charAt(1);

        // 高4位
        byte h = TransUtil.charToByte(c0);
        byte l = TransUtil.charToByte(c1);

        return (byte) ((h<<4 & 0xf0) | (l & 0x0f));
    }

    /**
     * 字符串==>BCD字节数组
     *
     * @param str
     * @return BCD字节数组
     */
    public byte[] string2Bcd(String str) {
        // 奇数,前补零
        if ((str.length() & 0x1) == 1) {
            str = "0" + str;
        }

        byte ret[] = new byte[str.length() / 2];
        byte bs[] = str.getBytes();
        for (int i = 0; i < ret.length; i++) {

            byte high = ascII2Bcd(bs[2 * i]);
            byte low = ascII2Bcd(bs[2 * i + 1]);

            // TODO 只遮罩BCD低四位?
            ret[i] = (byte) ((high << 4) | low);
        }
        return ret;
    }

    private byte ascII2Bcd(byte asc) {
        if ((asc >= '0') && (asc <= '9')) {
            return (byte) (asc - '0');
        }
        else if ((asc >= 'A') && (asc <= 'F')) {
            return (byte) (asc - 'A' + 10);
        }
        else if ((asc >= 'a') && (asc <= 'f')) {
            return (byte) (asc - 'a' + 10);
        }
        else {
            return (byte) (asc - 48);
        }
    }

    public String parseBcdStringFromBytes(byte[] data, int startIndex, int lenth, String defaultVal) {
        try {
            byte[] tmp = new byte[lenth];
            System.arraycopy(data, startIndex, tmp, 0, lenth);
            return bcd2String(tmp);
        } catch (Exception e) {
            return defaultVal;
        }
    }

    public String parseBcdHexString(byte[] data, int startIndex, int lenth) {
        byte[] tmp = new byte[lenth];
        System.arraycopy(data, startIndex, tmp, 0, lenth);
        return bcd2HexString(tmp);
    }

    /**
     * 从十六进制解析成二进制
     * @param hexString BB:E0:0F:0E:00:4B
     * @return 6个字节
     */
    public byte[] parseBytesFromString(String hexString) {
        Iterable<String> split = SPLITTER_COLON.split(hexString);
        Iterator<String> iterator = split.iterator();
        byte[] result = new byte[6];
        int i = 0;
        while (iterator.hasNext()) {
            byte b = byteFromHexString(iterator.next());
            result[i] = b;
            ++i;
        }
        return result;
    }

    /**
     * 特殊情况 0001 1000 -》18, 0000 0010-》02
     * @param b
     * @return
     */
    public static int decIntFromBcd1(byte b) {
        int decade = (b & 0xF0) >> 4;
        int unit = b & 0x0F;
        return decade * 10 + unit;
    }

    public static void main(String[] args) {
        ByteBuf buf = Unpooled.buffer();
        buf.writeByte(0x00);
        buf.writeByte(0x01);
        buf.writeByte(0x02);
        buf.writeByte(0x03);
        buf.writeByte(0x04);
        buf.writeByte(0x05);
        byte [] test = new byte[buf.readableBytes()];
        buf.readBytes(test);
        buf.writeByte(0x06);
        buf.resetReaderIndex();
        buf.resetWriterIndex();
        System.out.println(buf);
    }
}
