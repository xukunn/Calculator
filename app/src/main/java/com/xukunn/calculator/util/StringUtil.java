package com.xukunn.calculator.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xukun on 2016/6/29.
 */
public class StringUtil {

    public static String getMaskPhone(String phone) {
        if (phone == null || phone.length() < 3) {
            return "";
        }
        return phone.substring(0, 3) + "******" + phone.substring(phone.length() - 2);
    }

    public static String getMaxLength(String str, int max) {
        if (str == null) {
            return "";
        }
        return str.length() > max ? str.substring(0, max) + "..." : str;
    }

    public static String replaceNull(String str) {
        return (TextUtils.isEmpty(str) || "null".equals(str)) ? "" : str;
    }

    /**
     * 如果不为null,给str加上空格
     *
     * @param str
     * @return
     */
    public static String replaceNullWithBlank(String str) {
        return (TextUtils.isEmpty(str) || "null".equals(str)) ? "" : str + " ";
    }

    /**
     * null就用后面的替换
     */
    public static String replaceNullWith(String str, String replace) {
        return (TextUtils.isEmpty(str) || "null".equals(str)) ? replace : str;
    }
    /**
     * null就用后面的替换
     */
    public static String replaceNullWith(Long str, String replace) {
        return  "null".equals(str) ? replace : str.toString();
    }

    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }

    public static boolean isCommonStr(String str) {
        String pattern = "^[A-Za-z0-9_\\-\u4e00-\u9fa5]+$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);
        return m.matches();
    }

    /**
     * 中英文,数字,下划线,减号,小数点,空格,@符号,空字符串""
     *
     * @param str
     * @return
     */
    public static boolean isUserName(String str) {
        String pattern = "^[A-Za-z0-9_@.\\-\\s\u4e00-\u9fa5]*$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);
        return m.matches();
    }

    /*由于Java是基于Unicode编码的，因此，一个汉字的长度为1，而不是2。
   * 但有时需要以字节单位获得字符串的长度。例如，“123abc长城”按字节长度计算是10，而按Unicode计算长度是8。
   * 为了获得10，需要从头扫描根据字符的Ascii来获得具体的长度。如果是标准的字符，Ascii的范围是0至255，如果是汉字或其他全角字符，Ascii会大于255。
   * 因此，可以编写如下的方法来获得以字节为单位的字符串长度。*/
    public static int getWordCount(String s) {
        int length = 0;
        for (int i = 0; i < s.length(); i++) {
            int ascii = Character.codePointAt(s, i);
            if (ascii >= 0 && ascii <= 255)
                length++;
            else
                length += 2;

        }
        return length;

    }

    /*基本原理是将字符串中所有的非标准字符（双字节字符）替换成两个标准字符（**，或其他的也可以）。这样就可以直接例用length方法获得字符串的字节长度了*/
    //要除2转字符所以加1
    public static int getWordCountRegex(String s) {
        if (s != null) {
            s = s.replaceAll("[^\\x00-\\xff]", "**");
            int length = s.length();
            return length;
        }
        return -1;
    }

    /**
     * 小数处理多余的0
     */
    public static String removeZero(String s) {
        if (TextUtils.isEmpty(s)) {
            return "";
        }
        if (s.indexOf(".") > 0) {
            //正则表达
            s = s.replaceAll("0+?$", "");//去掉后面无用的零
            s = s.replaceAll("[.]$", "");//如小数点后面全是零则去掉小数点
        }
        return s;
    }

    /**
     * 银行卡号每4位添加空格
     * @param str
     * @return
     */
    public static String addCardSpace(String str) {
        if (null == str || "".equals(str)) {
            return "";
        }
        StringBuilder sb = new StringBuilder(str.replace(" ", ""));

        int i = sb.length() / 4;
        int j = sb.length() % 4;

        for (int x = (j == 0 ? i - 1 : i); x > 0; x--) {
            sb = sb.insert(x * 4, " ");
        }
        return sb.toString();
    }

    /**
     * , 分隔的strs 是否包含str
     *
     * @param str
     * @param strs
     * @return
     */
    public static boolean contains(String str, String strs) {
        if (!TextUtils.isEmpty(strs)) {
            String[] split = strs.split(",");
            for (String s : split) {
                if (TextUtils.equals(s, str)) {
                    return true;
                }
            }
        }
        return false;
    }

}
