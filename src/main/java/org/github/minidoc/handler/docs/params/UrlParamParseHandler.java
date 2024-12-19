package org.github.minidoc.handler.docs.params;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.github.minidoc.core.constant.AnnotationConstant;
import org.github.minidoc.core.constant.MiniDocConstant;
import org.github.minidoc.core.constant.RootTypeConstant;
import org.github.minidoc.model.docs.ParamInfo;
import org.github.minidoc.util.CollectionUtils;
import org.github.minidoc.util.MethodUtils;
import org.github.minidoc.util.StrUtils;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@RequiredArgsConstructor
public class UrlParamParseHandler {

    /**
     * 解析接口文档的链接参数信息，主要是如下注解
     * PathVariable    ->  [请求URI为 /Demo2/{id1}/pets/{id2}]
     * MatrixVariable  ->  [请求URI为 /Demo2/66;color=red;year=2020/pets/77;color=blue;year=2019]
     *
     * @param rmi  接口映射信息
     * @param hm   接口方法信息
     * @param dpnd 方法名称解析器
     * @return 请求链接参数列表
     */
    public List<ParamInfo> analysis(RequestMappingInfo rmi, HandlerMethod hm, DefaultParameterNameDiscoverer dpnd) {
        if (rmi == null || hm == null || dpnd == null || hm.getMethod().getParameterCount() <= 0) {
            return CollectionUtils.newArrayList();
        }
        List<ParamInfo> paramInfoList = CollectionUtils.newArrayList();
        Method method = hm.getMethod();
        String[] parameterNames = dpnd.getParameterNames(method);
        AtomicInteger counter = new AtomicInteger();
        Arrays.stream(method.getParameters()).forEach(parameter -> { // 方法上的参数
            int index = counter.getAndIncrement();
            if (!MethodUtils.hasAnnotation(parameter, AnnotationConstant.ANN_URL_ARR)) {
                return;
            }
            String typeName = parameter.getType().getTypeName();
            if (RootTypeConstant.ignore(typeName)) {// 不需要显示
                return;
            }
            String paramName = parameterNames[index];
            MatrixVariable matrixVariable = parameter.getDeclaredAnnotation(MatrixVariable.class);
            // TODO 需要解析此类型的注解，方法上和参数内部field中
            PathVariable pathVariable = parameter.getDeclaredAnnotation(PathVariable.class);
            ParamInfo paramInfo = new ParamInfo();
            paramInfo.setFullType(parameter.getType().getTypeName());
            paramInfo.setSimpleType(parameter.getType().getSimpleName());
            paramInfo.setName(handlePathVariableName(pathVariable, paramName));
            paramInfo.setComment(MethodUtils.getComment(parameter));
            paramInfo.setMustFlag(pathVariable.required());
            paramInfoList.add(paramInfo);
        });
        return paramInfoList;
    }

    /**
     * 处理链接名称
     *
     * @param pathVariable 链接注解
     * @param paramName    原参数名称
     * @return 参数名称
     */
    private String handlePathVariableName(PathVariable pathVariable, String paramName) {
        if (StrUtils.isNotEmpty(pathVariable.value()) && StrUtils.isNotEmpty(pathVariable.name())) {
            log.error(MiniDocConstant.marker, "接口链接参数解析异常，存在多别名，【PathVariable】【name:{}】【value:{}】【Different @AliasFor】", pathVariable.name(), pathVariable.value());
        }
        if (StrUtils.isNotEmpty(pathVariable.name())) {
            paramName = pathVariable.name();
        }
        if (StrUtils.isNotEmpty(pathVariable.value())) {
            paramName = pathVariable.value();
        }
        return paramName;
    }

}
