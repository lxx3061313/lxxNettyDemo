package com.lxx.mydemo.nettydemo.service.common.util;

import java.nio.charset.CharacterCodingException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author lixiaoxiong
 * @version 2018-02-11
 */
public class ByteUtil {
    public static byte[] hexString2Bytes(String hex) {

        if ((hex == null) || (hex.equals(""))){
            return null;
        }
        else if (hex.length()%2 != 0){
            return null;
        }
        else{
            hex = hex.toUpperCase();
            int len = hex.length()/2;
            byte[] b = new byte[len];
            char[] hc = hex.toCharArray();
            for (int i=0; i<len; i++){
                int p=2*i;
                b[i] = (byte) (charToByte(hc[p]) << 4 | charToByte(hc[p+1]));
            }
            return b;
        }
    }

    public static String bytes2HexString(byte[] b) {
        String r = "";

        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            r += hex.toUpperCase() + " ";
        }

        return r;
    }

    /**
     * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序
     * @param ary
     * @param offset
     * @return
     */
    public static int bytesToInt(byte[] ary, int offset) {
        int value;
        value = (int) ((ary[offset]&0xFF)
                | ((ary[offset+1]<<8) & 0xFF00)
                | ((ary[offset+2]<<16)& 0xFF0000)
                | ((ary[offset+3]<<24) & 0xFF000000));
        return value;
    }

    public static int bytesToShort(byte[] ary, int offset) {
        short value;
        value = (short) ((ary[offset]&0xFF)
                | ((ary[offset+1]<<8) & 0xFF00));
        return value;
    }

    public static byte [] shortToBytes(short value) {
        byte[] byte_src = new byte[2];
        byte_src[1] = (byte) ((value & 0xFF00)>>8);
        byte_src[0] = (byte) ((value & 0x00FF));
        return byte_src;
    }

    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(低位在前，高位在后)的顺序。
     * @param value
     *            要转换的int值
     * @return byte数组
     */
    public static byte[] intToBytes(int value)
    {
        byte[] byte_src = new byte[4];
        byte_src[3] = (byte) ((value & 0xFF000000)>>24);
        byte_src[2] = (byte) ((value & 0x00FF0000)>>16);
        byte_src[1] = (byte) ((value & 0x0000FF00)>>8);
        byte_src[0] = (byte) ((value & 0x000000FF));
        return byte_src;
    }

    public static byte[] stringToBytes(String str, int count) {
        char[] chars = str.toCharArray();
        byte[] result = new byte[count];
        int index = 0;
        for (char c : chars) {
            if (index >= count) {
                break;
            }
            result[index] = (byte) c;
            index++;
        }
        return result;
    }

    public static int byteToInt(byte b) {
        return b & 0xFF;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static byte[] Encrypt(byte[] sSrc, byte[] sKey){
        try{
            SecretKeySpec skeySpec = new SecretKeySpec(sKey, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(sSrc);
            return encrypted;
        }catch(Exception ex){
            return null;
        }
    }


    private static boolean isMatch(String regex, String orginal){
        if (orginal == null || orginal.trim().equals("")) {
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher isNum = pattern.matcher(orginal);
        return isNum.matches();
    }

    public static boolean isDecimal(String orginal){
        return isMatch("{0,1}\\d+\\.\\d*|{0,1}\\d*\\.\\d+", orginal);
    }

    public static boolean isRealNumber(String orginal){
        return isWholeNumber(orginal) || isDecimal(orginal);
    }
    public static boolean isWholeNumber(String orginal) {
        return isMatch("[+-]{0,1}0", orginal) || isPositiveInteger(orginal) || isNegativeInteger(orginal);
    }

    public static boolean isPositiveDecimal(String orginal){
        return isMatch("\\+{0,1}[0]\\.[1-9]*|\\+{0,1}[1-9]\\d*\\.\\d*", orginal);
    }

    public static boolean isPositiveInteger(String orginal) {
        return isMatch("^\\+{0,1}[1-9]\\d*", orginal);
    }

    public static boolean isNegativeInteger(String orginal) {
        return isMatch("^-[1-9]\\d*", orginal);
    }
    public static void main(String[] args) throws CharacterCodingException {
    }
}
