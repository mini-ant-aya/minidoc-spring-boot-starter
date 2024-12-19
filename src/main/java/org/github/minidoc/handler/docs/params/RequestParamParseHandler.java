package org.github.minidoc.handler.docs.params;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.github.minidoc.component.ParamParserComponent;
import org.github.minidoc.core.constant.AnnotationConstant;
import org.github.minidoc.core.constant.MiniDocConstant;
import org.github.minidoc.core.constant.RootTypeConstant;
import org.github.minidoc.core.enums.ParseType;
import org.github.minidoc.model.docs.ParamInfo;
import org.github.minidoc.util.CollectionUtils;
import org.github.minidoc.util.MethodUtils;
import org.github.minidoc.util.StrUtils;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestParamParseHandler {

    private final ParamParserComponent paramParser;


    /**
     * 解析接口文档的Json格式请求参数信息
     *
     * @param rmi  接口映射信息
     * @param hm   接口方法信息
     * @param dpnd 方法名称解析器
     * @return 请求链接参数列表
     */
    public List<ParamInfo> analysisQueryParam(RequestMappingInfo rmi, HandlerMethod hm, DefaultParameterNameDiscoverer dpnd) {
        if (rmi == null || hm == null || dpnd == null || hm.getMethod().getParameterCount() <= 0) {
            return CollectionUtils.newArrayList();
        }
        List<ParamInfo> paramInfoList = CollectionUtils.newArrayList();

        List<Class<Annotation>> excludeList = CollectionUtils.newArrayList();
        excludeList.addAll(Arrays.asList(AnnotationConstant.ANN_HEADER_ARR));
        excludeList.addAll(Arrays.asList(AnnotationConstant.ANN_URL_ARR));
        excludeList.addAll(Arrays.asList(AnnotationConstant.ANN_FORM_PARAM_ARR));
        excludeList.addAll(Arrays.asList(AnnotationConstant.ANN_JSON_PARAM_ARR));
        Class<Annotation>[] notList = Arrays.stream(AnnotationConstant.ANN_ALL).filter(e -> !excludeList.contains(e)).toArray(Class[]::new);

        Arrays.stream(hm.getMethodParameters()).forEach(parameter -> { // 方法上的参数
            parameter.initParameterNameDiscovery(dpnd);
            if (!MethodUtils.hasAnnotation(parameter, notList)) { // 忽略掉不需要的注解类型
                return;
            }
            String paramName = parameter.getParameterName();
            String typeName = parameter.getParameterType().getName();
            if (RootTypeConstant.ignore(typeName)) {// 不需要显示
                return;
            }
            boolean isRootType = RootTypeConstant.isRootType(typeName);
            if (!isRootType) {
                log.error(MiniDocConstant.marker, "接口解析异常，@RequestParam注解一般用于基本类型或简单类型，但是发现类型不符合，当前类型为：{}", typeName);
                return;
            }

            String finalParamName = paramName;
            boolean mustFlag = false;
            RequestParam requestParam = parameter.getParameterAnnotation(RequestParam.class);
            if (requestParam != null) { // RequestParam注解
                finalParamName = handleQueryParamName(requestParam, paramName);
                mustFlag = requestParam.required();
            }
            ParamInfo paramInfo = new ParamInfo();
            paramInfo.setFullType(typeName);
            paramInfo.setSimpleType(parameter.getParameterType().getSimpleName());
            paramInfo.setName(finalParamName);
            paramInfo.setComment(MethodUtils.getComment(parameter)); // 获取参数描述
            paramInfo.setMustFlag(mustFlag);
            paramInfoList.add(paramInfo);
        });
        return paramInfoList;
    }


    /**
     * 处理参数名称
     *
     * @param requestParam 请求参数注解
     * @param paramName    原参数名称
     * @return 参数名称
     */
    private String handleQueryParamName(RequestParam requestParam, String paramName) {
        if (StrUtils.isNotEmpty(requestParam.value()) && StrUtils.isNotEmpty(requestParam.name())) {
            log.error(MiniDocConstant.marker, "接口Query Param参数解析异常，存在多别名，【RequestParam】【name:{}】【value:{}】【Different @AliasFor】", requestParam.name(), requestParam.value());
        }
        if (StrUtils.isNotEmpty(requestParam.name())) {
            paramName = requestParam.name();
        }
        if (StrUtils.isNotEmpty(requestParam.value())) {
            paramName = requestParam.value();
        }
        return paramName;
    }


    /**
     * 解析接口文档的Json格式请求参数信息
     *
     * @param rmi  接口映射信息
     * @param hm   接口方法信息
     * @param dpnd 方法名称解析器
     * @return 结果 key:方法全量名、value:参数列表
     */
    public List<ParamInfo> analysisFormParam(RequestMappingInfo rmi, HandlerMethod hm, DefaultParameterNameDiscoverer dpnd) {
        if (rmi == null || hm == null || dpnd == null || hm.getMethod().getParameterCount() <= 0) {
            return CollectionUtils.newArrayList();
        }
        // TODO
        return null;
    }


    /**
     * 解析接口文档的Json格式请求参数信息
     *
     * @param rmi  接口映射信息
     * @param hm   接口方法信息
     * @param dpnd 方法名称解析器
     * @return 参数列表
     */
    public ParamInfo analysisJsonParam(RequestMappingInfo rmi, HandlerMethod hm, DefaultParameterNameDiscoverer dpnd) {
        if (rmi == null || hm == null || dpnd == null || hm.getMethod().getParameterCount() <= 0) {
            return null;
        }
        MethodParameter[] parameters = hm.getMethodParameters();
        MethodParameter requiredParameter = Arrays.stream(parameters).filter(this::hasRequiredRequestBodyFilter).findFirst().orElse(null);
        if (requiredParameter != null) { // 有符合要求的注解参数
            requiredParameter.initParameterNameDiscovery(dpnd);
            return paramParser.parseMethodParamType(rmi, requiredParameter, ParseType.PARAM);
        }
        MethodParameter needParameter = Arrays.stream(parameters).filter(this::hasAnyRequestBodyFilter).findFirst().orElse(null);
        if (needParameter != null) { // 取RequestBody参数中，第一个
            needParameter.initParameterNameDiscovery(dpnd);
            return paramParser.parseMethodParamType(rmi, needParameter, ParseType.PARAM);
        }
        return null; // 没有符合要求的RequestBody注解参数
    }


    /**
     * 有任意RequestBody注解的参数
     *
     * @param parameter 参数对象
     * @return true:有,false:没有
     */
    private boolean hasAnyRequestBodyFilter(MethodParameter parameter) {
        RequestBody requestBody = parameter.getParameterAnnotation(RequestBody.class);
        return requestBody != null;
    }

    /**
     * 有合格RequestBody注解的参数
     *
     * @param parameter 参数对象
     * @return true:有,false:没有
     */
    private boolean hasRequiredRequestBodyFilter(MethodParameter parameter) {
        RequestBody requestBody = parameter.getParameterAnnotation(RequestBody.class);
        return requestBody != null && requestBody.required();
    }

}
