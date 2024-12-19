package org.github.minidoc.util;

import org.github.minidoc.core.annotation.DocField;
import org.github.minidoc.model.method.AnnotationInfo;
import org.github.minidoc.model.method.AnnotationMethodInfo;
import org.springframework.core.MethodParameter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

public class MethodUtils {

    // 注解忽略方法
    private static final Set<String> ignoreMethodSet = new LinkedHashSet<>();

    static {
        Arrays.stream(Object.class.getMethods()).forEach(method -> {
            ignoreMethodSet.add(method.toString());
        });
        Arrays.stream(Annotation.class.getMethods()).forEach(method -> {
            ignoreMethodSet.add(method.toString());
        });
    }

    /**
     * 不是要忽略的方法
     *
     * @param methodName 方法
     * @return true:不需要忽略，false:是要忽略的方法
     */
    public static boolean notIgnoreMethod(String methodName) {
        return !ignoreMethodSet.contains(methodName);
    }

    /**
     * 过滤公共且是get或is开头的方法
     *
     * @param method 方法
     * @return true:所需方法，false:不需要的方法
     */
    public static boolean filterGetAndIsMethod(Method method) {
        return Modifier.isPublic(method.getModifiers()) && MethodUtils.notIgnoreMethod(method.toString()) && (StrUtils.startWith(method.getName(), RequestUtils.METHOD_PRE_GET) || StrUtils.startWith(method.getName(), RequestUtils.METHOD_PRE_IS));
    }

    /**
     * 过滤属性的get或is开头的方法
     *
     * @param field     属性对象
     * @param methodMap 方法列表
     * @return true:所需方法，false:不需要的方法
     */
    public static boolean filterGetAndIsMethod(Field field, Map<String, List<Method>> methodMap) {
        // 获取所需方法[get、is]开头的方法
        return methodMap.containsKey(StrUtils.upperFirstAndAddPre(field.getName(), RequestUtils.METHOD_PRE_GET)) || methodMap.containsKey(StrUtils.upperFirstAndAddPre(field.getName(), RequestUtils.METHOD_PRE_IS));
    }

    /**
     * 解析方法的注解信息
     *
     * @param annArr 注解数组
     * @return 方法的注解列表
     */
    public static List<AnnotationInfo> parseAnnInfoList(Annotation[] annArr) {
        List<AnnotationInfo> annInfoList = CollectionUtils.newArrayList();
        if (ArrayUtils.isEmpty(annArr)) {
            return annInfoList;
        }
        Arrays.stream(annArr).forEach(annotation -> {
            AnnotationInfo annotationInfo = new AnnotationInfo();
            annotationInfo.setFullType(annotation.annotationType().getTypeName());
            annotationInfo.setSimpleType(annotation.annotationType().getSimpleName());
            List<AnnotationMethodInfo> methodList = CollectionUtils.newArrayList();
            Arrays.stream(annotation.annotationType().getMethods()).filter(method -> MethodUtils.notIgnoreMethod(method.toString())).forEach(method -> {
                AnnotationMethodInfo annotationMethodInfo = new AnnotationMethodInfo();
                annotationMethodInfo.setMethodName(method.getName());
                annotationMethodInfo.setReturnType(method.getReturnType().getTypeName());
                methodList.add(annotationMethodInfo);
            });
            annotationInfo.setMethodList(methodList);
            annInfoList.add(annotationInfo);
        });
        return annInfoList;
    }

    /**
     * 是否有类型注解,参数对象为空返回不存在
     *
     * @param parameter 参数对象
     * @param clazz     注解类
     * @return true:存在，false:不存在
     */
    public static boolean hasAnnotation(MethodParameter parameter, Class... clazz) {
        if (parameter == null || clazz == null) {
            return false;
        }
        for (Class clz : clazz) {
            if (parameter.getParameterAnnotation(clz) != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否有类型注解,参数对象为空返回不存在
     *
     * @param parameter 参数对象
     * @param clazz     注解类
     * @return true:存在，false:不存在
     */
    public static boolean hasAnnotation(Parameter parameter, Class... clazz) {
        if (parameter == null || clazz == null) {
            return false;
        }
        for (Class clz : clazz) {
            if (parameter.getDeclaredAnnotation(clz) != null) {
                return true;
            }
        }
        return false;
    }


    /**
     * 获取方法参数的描述信息
     *
     * @param parameter 参数对象
     * @return 描述信息
     */
    public static String getComment(Parameter parameter) {
        if (parameter == null) {
            return null;
        }
        DocField docField = parameter.getDeclaredAnnotation(DocField.class);
        if (docField != null) {
            return docField.value();
        }
        return null;
    }


    /**
     * 获取方法返回的描述信息
     *
     * @param parameter 参数对象
     * @return 描述信息
     */
    public static String getComment(MethodParameter parameter) {
        if (parameter == null) {
            return null;
        }
        DocField docField = parameter.getParameterAnnotation(DocField.class);
        if (docField != null) {
            return docField.value();
        }
        return null;
    }

    /**
     * 获取属性参数的属性名称
     *
     * @param field 属性对象
     * @return 属性名称
     */
    public static String getFieldName(Field field) {
        if (field == null) {
            return null;
        }
        return field.getName();
    }

    /**
     * 获取属性参数的描述信息
     *
     * @param field 属性对象
     * @return 描述信息
     */
    public static String getComment(Field field) {
        if (field == null) {
            return null;
        }
        DocField docField = field.getDeclaredAnnotation(DocField.class);
        if (docField != null) {
            return docField.value();
        }
        return null;
    }

    /**
     * 获取类的参数的描述信息
     *
     * @param clazz 属性对象
     * @return 描述信息
     */
    public static String getComment(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        DocField docField = clazz.getAnnotation(DocField.class);
        if (docField != null) {
            return docField.value();
        }
        return null;
    }

    /**
     * 获取指定类的get和is开头的方法
     *
     * @param needParseClass
     * @return 方法映射关系
     */
    public static Map<String, List<Method>> findGetAndIsMethod(Class needParseClass) {
        // 获取所需方法[get、is]开头的方法
        return Arrays.stream(needParseClass.getMethods()).filter(MethodUtils::filterGetAndIsMethod).collect(Collectors.groupingBy(Method::getName));
    }

}
