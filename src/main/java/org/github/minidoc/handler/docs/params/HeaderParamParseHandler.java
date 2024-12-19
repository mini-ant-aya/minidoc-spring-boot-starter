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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@RequiredArgsConstructor
public class HeaderParamParseHandler {

    /**
     * 解析接口文档的请求头参数信息
     *
     * @param rmi  接口映射信息
     * @param hm   接口方法信息
     * @param dpnd 方法名称解析器
     * @return 请求头参数列表
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
            if (!MethodUtils.hasAnnotation(parameter, AnnotationConstant.ANN_HEADER_ARR)) {
                return;
            }
            String typeName = parameter.getType().getTypeName();
            if (RootTypeConstant.ignore(typeName)) {// 不需要显示
                return;
            }
            String paramName = parameterNames[index];
            RequestHeader requestHeader = parameter.getDeclaredAnnotation(RequestHeader.class);
            ParamInfo paramInfo = new ParamInfo();
            paramInfo.setFullType(typeName);
            paramInfo.setSimpleType(parameter.getType().getSimpleName());
            paramInfo.setName(handleHeaderName(requestHeader, paramName));
            paramInfo.setComment(MethodUtils.getComment(parameter));
            paramInfo.setMustFlag(requestHeader.required());
            paramInfoList.add(paramInfo);
        });
        return paramInfoList;
    }

    /**
     * 处理请求头名称
     *
     * @param requestHeader 请求头注解
     * @param paramName     原参数名称
     * @return 参数名称
     */
    private String handleHeaderName(RequestHeader requestHeader, String paramName) {
        if (StrUtils.isNotEmpty(requestHeader.value()) && StrUtils.isNotEmpty(requestHeader.name())) {
            log.error(MiniDocConstant.marker, "请求头参数解析异常，存在多别名，【RequestHeader】【name:{}】【value:{}】【Different @AliasFor】", requestHeader.name(), requestHeader.value());
        }
        if (StrUtils.isNotEmpty(requestHeader.name())) {
            paramName = requestHeader.name();
        }
        if (StrUtils.isNotEmpty(requestHeader.value())) {
            paramName = requestHeader.value();
        }
        return paramName;
    }

}
