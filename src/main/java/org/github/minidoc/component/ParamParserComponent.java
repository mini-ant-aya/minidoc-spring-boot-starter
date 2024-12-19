package org.github.minidoc.component;

import lombok.extern.slf4j.Slf4j;
import org.github.minidoc.core.constant.MiniDocConstant;
import org.github.minidoc.core.constant.RootTypeConstant;
import org.github.minidoc.core.enums.ParamType;
import org.github.minidoc.core.enums.ParseType;
import org.github.minidoc.core.exception.MiniDocException;
import org.github.minidoc.model.docs.ParamInfo;
import org.github.minidoc.util.*;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ParamParserComponent {

    private Map<String, ParamInfo> cacheMap; // 类型存在Map key:typeName,value:存在类型的对象，解析过就put，防止重复解析
    private Map<String, Integer> traceMap; // 痕迹Map，key:【typeName[<->]fieldTypeName[<->]fieldName】，value:【1】 ，解析过这个属性就设置一次，防止死循环导致的堆栈溢出

    /**
     * 解析参数类型信息【第一级，解析接口方法参数对象】
     *
     * @param rmi       接口映射信息
     * @param parameter 参数对象信息
     * @param parseType 参数解析类型
     * @return 返回信息集合
     */
    public ParamInfo parseMethodParamType(RequestMappingInfo rmi, MethodParameter parameter, ParseType parseType) {
        if (rmi == null || parameter == null || parseType == null) return null; // 必要参数校验
        String typeName = parameter.getParameterType().getName(); // 参数原始类型
        String paramName = parameter.getParameterName(); // 参数名称
        Set<String> urls = rmi.getPatternValues();
        try {
            log.debug(MiniDocConstant.marker, "【{}】开始解析【接口：{}】【[{}]:[{}]】的数据", parseType, urls, typeName, paramName);
            this.cacheMap = MapUtils.newHashMap();
            this.traceMap = MapUtils.newHashMap();
            // 获取带泛型的参数类型【带泛型，没有泛型返回原始类型】
            Type genericParameterType = parameter.getGenericParameterType();
            ParamInfo paramInfo = parseTypeInfo(genericParameterType, MapUtils.newHashMap());
            paramInfo.setName(parameter.getParameterName());
            paramInfo.setComment(MethodUtils.getComment(parameter));
            return paramInfo;
        } finally {
            this.traceMap = null;
            this.cacheMap = null;
            log.debug(MiniDocConstant.marker, "【{}】结束解析【接口：{}】【[{}]:[{}]】的数据", parseType, urls, typeName, paramName);
        }
    }

    /**
     * 解析的类型信息
     *
     * @param genericsType          类型信息
     * @param parentTypeRelationMap 父级映射关系
     * @return 参数对象信息
     */
    private ParamInfo parseTypeInfo(Type genericsType, Map<String, Type> parentTypeRelationMap) {
        // 1、根类型判断
        if (RootTypeConstant.isRootType(genericsType.getTypeName())) {
            ParamInfo paramInfo = assembleRootParamInfo(genericsType);
            return paramInfo;
        }
        // 2、是否参数化类型
        if (genericsType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericsType;
            Type rawType = parameterizedType.getRawType();
            String typeName = rawType.getTypeName(); // 类型名称
            if (RootTypeConstant.isListType(typeName)) { // list类型
                Map<String, Type> currentTypeRelationMap = ResolvableTypeUtils.getParameterizedTypeRelationShipMap(parameterizedType); // 关系类型Map
                ParamInfo paramInfo = assembleRootParamInfo(genericsType);// list类型数据组装

                ResolvableType resolvableType = ResolvableType.forType(genericsType);
                ResolvableType listGenericsResolvableType = resolvableType.getGenerics()[0]; // 泛型名称
                Type listRealGenericsType = parameterizedType.getActualTypeArguments()[0]; // list的泛型的实际类型，取第一个类型(可能依然是非真实类型)
                // 对泛型实际类型进行判断，是否依然是泛型

                Type currentRealGenericsType = currentTypeRelationMap.get(listGenericsResolvableType.getType().getTypeName());
                System.out.println(listRealGenericsType.getTypeName().equals(currentRealGenericsType.getTypeName()));
                ResolvableType currentResolvableType = ResolvableType.forType(currentRealGenericsType);
                Class<?> rawClass = currentResolvableType.getRawClass();
                if (rawClass == null) { // 从父级映射关系中查找
                    Type realGenericsTypeFromParent = parentTypeRelationMap.get(currentRealGenericsType.getTypeName());
                }


                Type t = getRealType(rawType, currentTypeRelationMap, parentTypeRelationMap);

                ParamInfo listGenericsParamInfo = parseClassType(ResolvableType.forType(listRealGenericsType), currentTypeRelationMap);
                listGenericsParamInfo.setVirtualFlag(true); // list泛型是虚拟节点
                listGenericsParamInfo.setShowCurrent(false); // 虚拟节点不显示，下级节点显示
                paramInfo.setFieldList(CollectionUtils.newArrayList(listGenericsParamInfo));
                return paramInfo;
            }
            if (RootTypeConstant.isMapType(typeName)) { // map类型
                ParamInfo paramInfo = assembleRootParamInfo(genericsType);
                return paramInfo;
            }
            if (RootTypeConstant.isRootType(typeName)) {// 根类型
                ParamInfo paramInfo = assembleRootParamInfo(genericsType);
                return paramInfo;
            }
            // 不是List、Map、也不是根类型
            Map<String, Type> typeRelationMap = ResolvableTypeUtils.getParameterizedTypeRelationShipMap(parameterizedType); // 2.1、关系类型Map
            ParamInfo paramInfo = parseClassType(ResolvableType.forType(genericsType), typeRelationMap); // 3、获取泛型的数据列表 TODO
            return paramInfo;
        }
        // 3、没有泛型类型，也不是根类型
        ParamInfo paramInfo = parseClassType(ResolvableType.forType(genericsType), MapUtils.newHashMap());
        return paramInfo;
    }

    /**
     * 解析类类型处理(非最终类型、非List类型、非Map类型)【第二级，解析参数对象的属性，以及将泛型映射到类中对应的Field】
     *
     * @param needParseType   要解析的参数类型
     * @param typeRelationMap 泛型类型关系Map
     * @return 参数对象信息
     */
    private ParamInfo parseClassType(ResolvableType needParseType, final Map<String, Type> typeRelationMap) {
        // 必要参数校验
        if (needParseType == null || typeRelationMap == null) throw new MiniDocException("解析类型时，有必要参数为null，请注意检查");
        final Class<?> rawClass = needParseType.getRawClass();
        if (rawClass == null) throw new MiniDocException("解析类型时，有必要参数为null，请注意检查");
        final String typeName = rawClass.getName();
        if (cacheMap.containsKey(typeName)) { // 解析过，直接返回
            return cacheMap.get(typeName);
        }
        ParamInfo paramInfo = assembleRootParamInfo(rawClass); // 组装数据
        Field[] fieldArr = rawClass.getDeclaredFields(); // 获取类的属性
        if (ArrayUtils.isEmpty(fieldArr)) { // 没有成员属性，直接返回空
            cacheTypeInfo(typeName, paramInfo);
            return paramInfo;
        }
        // 有属性的解析方式，保留有get和is开头的属性
        final Map<String, List<Method>> methodMap = MethodUtils.findGetAndIsMethod(rawClass);
        Field[] finalFieldArr = Arrays.stream(fieldArr).filter(field -> MethodUtils.filterGetAndIsMethod(field, methodMap)).toArray(Field[]::new); // 最终返回参数列表
        if (ArrayUtils.isNotEmpty(finalFieldArr)) {
            final List<ParamInfo> paramInfoList = Arrays.stream(finalFieldArr).filter(Objects::nonNull).map(field -> {
                if (field.getName().equals("ddList")) {
                    System.out.println(field.getGenericType().getTypeName());
                }
                Type genericType = field.getGenericType();
                if (typeRelationMap.containsKey(genericType.getTypeName())) {
                    genericType = typeRelationMap.get(genericType.getTypeName());
                }
                String cacheKey = genFieldTraceCacheKey(typeName, field.getModifiers(), genericType.getTypeName(), field.getName());
                if (cacheMap.containsKey(cacheKey)) { // 有缓存
                    ParamInfo fieldParamInfo = cacheMap.get(cacheKey);
                    fieldParamInfo.setName(field.getName());
                    fieldParamInfo.setComment(MethodUtils.getComment(field));
                    return fieldParamInfo;
                }
                // 没有缓存，继续解析
                String traceCacheKey = genFieldTraceCacheKey(typeName, field.getGenericType().getTypeName(), field.getName());
                if (traceMap.containsKey(traceCacheKey)) {
                    ParamInfo fieldParamInfo = assembleRootParamInfo(field.getGenericType()); // 组装数据
                    fieldParamInfo.setName(field.getName());
                    fieldParamInfo.setComment(MethodUtils.getComment(field));
                    cacheTypeInfo(cacheKey, fieldParamInfo);
                    return fieldParamInfo;
                }
                // 当前Field没有解析过
                cacheFieldTrace(traceCacheKey); // 计数**重要**防止死循环
                ParamInfo fieldParamInfo = parseTypeInfo(genericType, typeRelationMap);
                fieldParamInfo.setName(field.getName());
                fieldParamInfo.setComment(MethodUtils.getComment(field));
                cacheTypeInfo(cacheKey, fieldParamInfo);
                return fieldParamInfo;
            }).collect(Collectors.toList());
            paramInfo.setFieldList(paramInfoList);
        }
        cacheTypeInfo(typeName, paramInfo);
        return paramInfo;
    }


    /**
     * 解析并生成参数信息[根类型\list\map]
     *
     * @param type 参数类型
     * @return 参数信息
     */
    private ParamInfo assembleRootParamInfo(Type type) {
        if (type == null) {
            throw new MiniDocException("要生成参数对象的类型为空，请注意检查");
        }
        ResolvableType resolvableType = ResolvableType.forType(type);
        Class<?> rawClass = resolvableType.getRawClass();
        ParamInfo paramInfo = new ParamInfo();
        paramInfo.setTypeClazz(rawClass);
        paramInfo.setFullType(rawClass.getTypeName());
        paramInfo.setSimpleType(rawClass.getSimpleName());
        paramInfo.setParamType(ParamType.getType(rawClass.getName()));
        paramInfo.setComment(MethodUtils.getComment(rawClass));
        return paramInfo;
    }

    /**
     * 缓存类型书籍
     *
     * @param type      类型全名
     * @param paramInfo 属性列表
     */
    private void cacheTypeInfo(String type, ParamInfo paramInfo) {
        if (!cacheMap.containsKey(type)) { // 不存在此类型，进行缓存
            this.cacheMap.put(type, paramInfo);
        }
    }

    /**
     * 缓存Field解析痕迹
     *
     * @param type Field缓存key
     */
    private void cacheFieldTrace(String type) {
        if (!traceMap.containsKey(type)) { // 不存在，进行缓存
            this.traceMap.put(type, 1);
        }
    }

    /**
     * 生成Field痕迹缓存key
     *
     * @param classTypeName        类型名
     * @param mod                  方法级别
     * @param fieldGenericTypeName field泛型类型名
     * @param fieldName            field名称
     * @return 缓存key
     */
    private String genFieldTraceCacheKey(String classTypeName, int mod, String fieldGenericTypeName, String fieldName) {
        // org.github.minidoc.model.TestDto private boolean flag
        return String.format("%s %s %s %s", classTypeName, Modifier.toString(mod), fieldGenericTypeName, classTypeName, fieldName);
    }

    /**
     * 生成Field痕迹缓存key
     *
     * @param classTypeName        类型名
     * @param fieldGenericTypeName field泛型类型名
     * @param fieldName            field名称
     * @return 缓存key
     */
    private String genFieldTraceCacheKey(String classTypeName, String fieldGenericTypeName, String fieldName) {
        return String.format("%s[<->]%s[<->]%s", classTypeName, fieldGenericTypeName, fieldName);
    }

    /**
     * 获取真实类型
     *
     * @param rawType
     * @param currentTypeRelationMap
     * @param parentTypeRelationMap
     * @return
     */
    private Type getRealType(final Type rawType, final Map<String, Type> currentTypeRelationMap, final Map<String, Type> parentTypeRelationMap) {
        // 对泛型实际类型进行判断，是否依然是泛型
        ResolvableType resolvableType = ResolvableType.forType(rawType);
        ResolvableType listGenericsResolvableType = resolvableType.getGenerics()[0];
        Type currentRealGenericsType = currentTypeRelationMap.get(listGenericsResolvableType.getType().getTypeName());
        ResolvableType currentResolvableType = ResolvableType.forType(currentRealGenericsType);
        Class<?> rawClass = currentResolvableType.getRawClass();
        if (rawClass == null) { // 从父级映射关系中查找
            Type realGenericsTypeFromParent = parentTypeRelationMap.get(currentRealGenericsType.getTypeName());
            return realGenericsTypeFromParent;
        }
        return currentRealGenericsType;
    }

}
