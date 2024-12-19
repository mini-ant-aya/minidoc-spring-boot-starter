package org.github.minidoc.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Map工具类
 */
public final class MapUtils {

    private MapUtils() {
    }

    /**
     * Map是否为空
     *
     * @param map 集合
     * @return 若集合为null, 或集合大小为0, 则返回true, 否则返回false
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.size() == 0;
    }


    /**
     * Map是否不为空
     *
     * @param map 集合
     * @return 若集合不为null, 而且集合大小不为0, 则返回true, 否则返回false
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap<>();
    }

    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap() {
        LinkedHashMap<K, V> linkedHashMap = new LinkedHashMap<>();
        return linkedHashMap;
    }

}
