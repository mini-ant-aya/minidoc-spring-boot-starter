package org.github.minidoc.util;

/**
 * 数组工具类
 */
public final class ArrayUtils {

    private ArrayUtils() {
    }

    /**
     * 判断数组是否为空
     *
     * @param array 数组
     * @return 布尔值
     */
    public static boolean isNotEmpty(Object[] array) {
        return !isEmpty(array);
    }

    /**
     * 判断数组是否非空
     *
     * @param array 数组
     * @return 返回布尔值
     */
    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 数组是否为空
     *
     * @param array 数组
     * @return 若数组为null, 或长度为0, 则返回true, 否则返回false
     */
    public static boolean isEmpty(boolean[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 数组是否为空
     *
     * @param array 数组
     * @return 若数组为null, 或长度为0, 则返回true, 否则返回false
     */
    public static boolean isEmpty(byte[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 数组是否为空
     *
     * @param array 数组
     * @return 若数组为null, 或长度为0, 则返回true, 否则返回false
     */
    public static boolean isEmpty(char[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 数组是否为空
     *
     * @param array 数组
     * @return 若数组为null, 或长度为0, 则返回true, 否则返回false
     */
    public static boolean isEmpty(double[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 数组是否为空
     *
     * @param array 数组
     * @return 若数组为null, 或长度为0, 则返回true, 否则返回false
     */
    public static boolean isEmpty(float[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 数组是否为空
     *
     * @param array 数组
     * @return 若数组为null, 或长度为0, 则返回true, 否则返回false
     */
    public static boolean isEmpty(int[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 数组是否为空
     *
     * @param array 数组
     * @return 若数组为null, 或长度为0, 则返回true, 否则返回false
     */
    public static boolean isEmpty(long[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 数组是否为空
     *
     * @param array 数组
     * @return 若数组为null, 或长度为0, 则返回true, 否则返回false
     */
    public static boolean isEmpty(short[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 数组是否为不空
     *
     * @param array 数组
     * @return 若数组不为null, 而且长度不为0, 则返回true, 否则返回false
     */
    public static boolean isNotEmpty(boolean[] array) {
        return !isEmpty(array);
    }

    /**
     * 数组是否为不空
     *
     * @param array 数组
     * @return 若数组不为null, 而且长度不为0, 则返回true, 否则返回false
     */
    public static boolean isNotEmpty(byte[] array) {
        return !isEmpty(array);
    }

    /**
     * 数组是否为不空
     *
     * @param array 数组
     * @return 若数组不为null, 而且长度不为0, 则返回true, 否则返回false
     */
    public static boolean isNotEmpty(char[] array) {
        return !isEmpty(array);
    }

    /**
     * 数组是否为不空
     *
     * @param array 数组
     * @return 若数组不为null, 而且长度不为0, 则返回true, 否则返回false
     */
    public static boolean isNotEmpty(double[] array) {
        return !isEmpty(array);
    }

    /**
     * 数组是否为不空
     *
     * @param array 数组
     * @return 若数组不为null, 而且长度不为0, 则返回true, 否则返回false
     */
    public static boolean isNotEmpty(float[] array) {
        return !isEmpty(array);
    }

    /**
     * 数组是否为不空
     *
     * @param array 数组
     * @return 若数组不为null, 而且长度不为0, 则返回true, 否则返回false
     */
    public static boolean isNotEmpty(int[] array) {
        return !isEmpty(array);
    }

    /**
     * 数组是否为不空
     *
     * @param array 数组
     * @return 若数组不为null, 而且长度不为0, 则返回true, 否则返回false
     */
    public static boolean isNotEmpty(long[] array) {
        return !isEmpty(array);
    }

    /**
     * 数组是否为不空
     *
     * @param array 数组
     * @return 若数组不为null, 而且长度不为0, 则返回true, 否则返回false
     */
    public static boolean isNotEmpty(short[] array) {
        return !isEmpty(array);
    }

}
