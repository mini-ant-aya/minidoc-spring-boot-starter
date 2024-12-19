package org.github.minidoc.util;

import java.util.*;


/**
 * 集合工具类
 */
public final class CollectionUtils {

    private CollectionUtils() {
    }

    /**
     * 集合是否为空
     *
     * @param collection 集合
     * @return 若集合为null, 或集合大小为0, 则返回true, 否则返回false
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.size() == 0;
    }

    /**
     * 集合是否不为空
     *
     * @param collection 集合
     * @return 若集合不为null, 而且集合大小不为0, 则返回true, 否则返回false
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * Set合并，如果合并的集合中没有值，则添加默认值
     *
     * @param set01           集合01
     * @param set02           集合02
     * @param defaultValueArr 默认值
     * @return 合并后的集合
     */
    public static <T> Set<T> merge(Set<T> set01, Set<T> set02, T... defaultValueArr) {
        Set<T> resultSet = new HashSet<>(set01);
        resultSet.addAll(set02);
        if (isNotEmpty(resultSet)) {
            return resultSet;
        }
        if (defaultValueArr != null && defaultValueArr.length > 0) {
            resultSet.addAll(Arrays.asList(defaultValueArr));
        }
        return resultSet;
    }

    public static <T> ArrayList<T> newArrayList() {
        return new ArrayList<>();
    }


    public static <T> ArrayList newArrayList(T... ele) {
        ArrayList arrayList = new ArrayList<>();
        if (ArrayUtils.isNotEmpty(ele)) {
            for (T t : ele) {
                arrayList.add(t);
            }
        }
        return arrayList;
    }

    public static <T> HashSet<T> newHashSet() {
        return new HashSet<>();
    }

}
