package com.lxx.mydemo.nettydemo.service.common.util.pojo.node;

/**
 * miao.yang susing@gmail.com 2013年9月4日
 */

import java.util.ArrayList;
import java.util.List;

public class PathFormat {

    private final char separator;
    private final char quotechar;
    private final char escape;

    public static final char DEFAULT_SEPARATOR = '.';
    public static final char DEFAULT_QUOTE_CHARACTER = '"';
    public static final char DEFAULT_ESCAPE_CHARACTER = '\\';

    public static final char NULL_CHARACTER = '\0';

    public PathFormat() {
        this(DEFAULT_SEPARATOR, DEFAULT_QUOTE_CHARACTER, DEFAULT_ESCAPE_CHARACTER);
    }

    public PathFormat(char separator, char quotechar, char escape) {

        if (separator == NULL_CHARACTER) {
            throw new UnsupportedOperationException("The separator character must be defined!");
        }

        this.separator = separator;
        this.quotechar = quotechar;
        this.escape = escape;
    }

    public List<String> parseLine(String sline) {

        char[] arr = sline.toCharArray();

        boolean inQuotes = false;

        List<String> tokensOnThisLine = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();

        for (int i = 0, n = arr.length; i < n; i++) {
            char c = arr[i];
            if (c == escape) {
                i++;
                if (!inQuotes || n <= i)
                    throw new IllegalArgumentException("can't parse : " + sline);
                char next = arr[i];

                switch (next) {
                case 'b':
                    sb.append('\b');
                    break;
                case 'f':
                    sb.append('\f');
                    break;
                case 'n':
                    sb.append('\n');
                    break;
                case 'r':
                    sb.append('\r');
                    break;
                case 't':
                    sb.append('\t');
                    break;
                case '/':
                    sb.append('/');
                    break;
                default:
                    if (next == quotechar || next == escape)
                        sb.append(next);
                    else {
                        sb.append(escape).append(next);
                    }
                }

            } else if (c == quotechar) {
                inQuotes = !inQuotes;
            } else if (c == separator && !inQuotes) {
                tokensOnThisLine.add(sb.toString());
                sb.setLength(0); // start work on next token
            } else {
                sb.append(c);
            }
        }

        if (sb != null) {
            tokensOnThisLine.add(sb.toString());
        }
        return tokensOnThisLine;

    }

    private static boolean containsSpec(char[] arr) {
        for (char c : arr) {
            if (!Character.isJavaIdentifierPart(c))
                return false;
        }
        return true;
    }

    public static String convert(String text) {
        char[] arr = text.toCharArray();
        if (containsSpec(arr))
            return text;
        StringBuilder sb = new StringBuilder(text.length() + 10).append('"');
        for (char ch : arr) {
            switch (ch) {
            case '"':
                sb.append("\\\"");
                break;
            case '\\':
                sb.append("\\\\");
                break;
            case '\b':
                sb.append("\\b");
                break;
            case '\f':
                sb.append("\\f");
                break;
            case '\n':
                sb.append("\\n");
                break;
            case '\r':
                sb.append("\\r");
                break;
            case '\t':
                sb.append("\\t");
                break;
            case '/':
                sb.append("\\/");
                break;
            default:
                sb.append(ch);
            }
        }
        return sb.append('"').toString();
    }
}
