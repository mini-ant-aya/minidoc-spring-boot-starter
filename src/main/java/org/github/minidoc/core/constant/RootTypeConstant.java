package org.github.minidoc.core.constant;

import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.*;

public final class RootTypeConstant {

    // 基本属性、Map不需要再次深入解析
    private static final Set<String> rootSet = new LinkedHashSet<>();

    static {
        // 基本类型
        rootSet.add(byte.class.getName());
        rootSet.add(short.class.getName());
        rootSet.add(int.class.getName());
        rootSet.add(char.class.getName());
        rootSet.add(boolean.class.getName());
        rootSet.add(float.class.getName());
        rootSet.add(double.class.getName());
        rootSet.add(long.class.getName());
        // 基本类型[包装]
        rootSet.add(Byte.class.getName());
        rootSet.add(Short.class.getName());
        rootSet.add(Integer.class.getName());
        rootSet.add(Character.class.getName());
        rootSet.add(Boolean.class.getName());
        rootSet.add(Float.class.getName());
        rootSet.add(Double.class.getName());
        rootSet.add(Long.class.getName());
        // 字符串
        rootSet.add(String.class.getName());
        // servlet
        rootSet.add(HttpServletRequest.class.getName());
        rootSet.add(HttpServletResponse.class.getName());
        // 时间
        rootSet.add(LocalTime.class.getName());
        rootSet.add(LocalDateTime.class.getName());
        rootSet.add(LocalDate.class.getName());
        rootSet.add(java.util.Date.class.getName());
        rootSet.add(java.sql.Date.class.getName());
        // math
        rootSet.add(BigInteger.class.getName());
        rootSet.add(BigDecimal.class.getName());
        // file
        rootSet.add(MultipartFile.class.getName());
    }


    /**
     * 是否包含无需解析类
     * 1、基本类型
     * 2、数值类型
     * 3、字符串类型
     * 4、时间类型
     * 5、上传文件类型
     *
     * @param type 类型
     * @return true:是，false:不是
     */
    public static boolean isRootType(String type) {
        return rootSet.contains(type);
    }

    // List、Set
    private static final Set<String> listSet = new LinkedHashSet<>();

    static {
        // Collection类型
        listSet.add(Collection.class.getName());
        // list类型
        listSet.add(List.class.getName());
        listSet.add(ArrayList.class.getName());
        listSet.add(LinkedList.class.getName());
        listSet.add(CopyOnWriteArrayList.class.getName());
        // set类型
        listSet.add(Set.class.getName());
        listSet.add(HashSet.class.getName());
        listSet.add(TreeSet.class.getName());
        listSet.add(LinkedHashSet.class.getName());
        listSet.add(SortedSet.class.getName());
        listSet.add(ConcurrentSkipListSet.class.getName());
        listSet.add(CopyOnWriteArraySet.class.getName());
    }

    /**
     * 是否是List类型
     *
     * @param type 类型名称
     * @return true:是，false:不是
     */
    public static boolean isListType(String type) {
        return listSet.contains(type);
    }

    // Map
    private static final Set<String> mapSet = new LinkedHashSet<>();

    static {
        // Map类型
        mapSet.add(Map.class.getName());
        mapSet.add(HashMap.class.getName());
        mapSet.add(LinkedHashMap.class.getName());
        mapSet.add(ConcurrentHashMap.class.getName());
        mapSet.add(ConcurrentMap.class.getName());
        mapSet.add(ConcurrentNavigableMap.class.getName());
        mapSet.add(ConcurrentSkipListMap.class.getName());
    }

    /**
     * 是否是Map类型
     *
     * @param type 类型名称
     * @return true:是，false:不是
     */
    public static boolean isMapType(String type) {
        return mapSet.contains(type);
    }

    private static final List<String> ignoreList = new ArrayList<>();

    static {
        ignoreList.add(HttpServletRequest.class.getName());
        ignoreList.add(HttpServletResponse.class.getName());
        ignoreList.add(BindResult.class.getName());
        ignoreList.add(Void.class.getName());
        ignoreList.add(void.class.getName());
    }

    /**
     * 是否忽略类型
     *
     * @param type 类型
     * @return true:包含，false:不包含
     */
    public static boolean ignore(String type) {
        return ignoreList.contains(type);
    }

}
