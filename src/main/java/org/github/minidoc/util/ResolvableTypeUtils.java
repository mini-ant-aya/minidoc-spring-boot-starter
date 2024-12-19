package org.github.minidoc.util;

import org.springframework.core.ResolvableType;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ResolvableTypeUtils {

    /**
     * 获取指定类型的RawClass
     *
     * @param type 类型
     * @return RawClass
     */
    public static Class<?> getRawClass(Type type) {
        ResolvableType resolvableType = ResolvableType.forType(type);
        return resolvableType.getRawClass(); // 原始类型
    }

    /**
     * 获取泛型的第一个实际类型
     *
     * @param parameterizedType 泛型化参数
     * @return 泛型实际类型
     */
    public static Type getFirstParameterizedType(ParameterizedType parameterizedType) {
        Type[] actualTypeArguments = getParameterizedType(parameterizedType); // 返回实际泛型类型列表
        if (ArrayUtils.isNotEmpty(actualTypeArguments)) { // 没有
            return actualTypeArguments[0];
        }
        return null;
    }

    /**
     * 获取泛型的实际类型
     *
     * @param parameterizedType 泛型化参数
     * @return 泛型实际类型数组
     */
    public static Type[] getParameterizedType(ParameterizedType parameterizedType) {
        return parameterizedType.getActualTypeArguments(); // 返回实际泛型类型列表
    }

    /**
     * 获取泛型对应实际类型关系[只解析第一层的泛型]
     *
     * @param parameterizedType 泛型化参数
     * @return [有序Map] key:泛型类型、value:泛型实际类型
     */
    public static Map<String, Type> getParameterizedTypeRelationShipMap(ParameterizedType parameterizedType) {
        Map<String, Type> actualTypeMap = MapUtils.newLinkedHashMap();// 类内泛型名称对应实际类型

        final Type[] actualTypeArr = parameterizedType.getActualTypeArguments(); // 返回实际泛型类型列表
        if (ArrayUtils.isEmpty(actualTypeArr)) return actualTypeMap;

        Type rawType = parameterizedType.getRawType();
        ResolvableType resolvableType = ResolvableType.forType(rawType);
        final ResolvableType[] genericsArr = resolvableType.getGenerics(); // 获取泛型
        if (ArrayUtils.isEmpty(genericsArr)) return actualTypeMap;

        // 类内泛型存储集合 -> 获取类中定义的泛型名称
        final List<Type> genericsTypeList = Arrays.stream(genericsArr).map(ResolvableType::getType).collect(Collectors.toList());
        if (actualTypeArr.length != genericsTypeList.size()) return actualTypeMap;

        AtomicInteger counter = new AtomicInteger();
        genericsTypeList.stream().forEach(genericsType -> {
            Type realType = actualTypeArr[counter.getAndIncrement()]; // 泛型实际类型
            actualTypeMap.put(genericsType.getTypeName(), realType);
        });

        return actualTypeMap;
    }

}
