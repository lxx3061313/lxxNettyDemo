package com.lxx.mydemo.nettydemo.service.common.util;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lixiaoxiong
 * @version 2017-11-04
 */
public class SplitterUtil {
    private final static Splitter COMMA_SPLITTER = Splitter.on(",");

    public static List<String> splitByComma(String param) {
        if (Strings.isNullOrEmpty(param)) {
            return Collections.EMPTY_LIST;
        }
        Iterable<String> split = COMMA_SPLITTER.split(param);
        return Lists.newArrayList(split);
    }

    public static List<Integer> splitByCommaToIntList(String param) {
        List<String> list = splitByComma(param);
        return list.stream().map(Integer::valueOf).collect(Collectors.toList());
    }
}
