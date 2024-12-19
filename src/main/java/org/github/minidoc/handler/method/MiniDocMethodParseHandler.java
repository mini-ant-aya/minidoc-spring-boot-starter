package org.github.minidoc.handler.method;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.github.minidoc.core.constant.MiniDocConstant;
import org.github.minidoc.core.constant.RequestMethodConstant;
import org.github.minidoc.model.method.AnnotationInfo;
import org.github.minidoc.model.method.MethodParamInfo;
import org.github.minidoc.model.method.MethodReturnInfo;
import org.github.minidoc.model.method.RestMethodInfo;
import org.github.minidoc.core.property.MiniDocProperties;
import org.github.minidoc.util.CollectionUtils;
import org.github.minidoc.util.MethodUtils;
import org.github.minidoc.util.RequestUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class MiniDocMethodParseHandler {

    private final MiniDocProperties properties;
    private final MethodParamParseHandler methodParamParseHandler;
    private final MethodReturnParseHandler methodReturnParseHandler;
    private final RequestMappingHandlerMapping handlerMapping;


    /**
     * 生成Controller方法
     *
     * @return 接口文档列表
     */
    public List<RestMethodInfo> genMethodDocs() {
        if (!properties.getEnable()) {
            log.info(MiniDocConstant.marker, "接口文档解析操作取消，原因是未开启解析操作，如想开启，请检查相关配置");
            return CollectionUtils.newArrayList();
        }
        // 请求接口集合
        List<RestMethodInfo> resultList = CollectionUtils.newArrayList();

        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = handlerMapping.getHandlerMethods();
        // 方法参数解析
        Map<String, List<MethodParamInfo>> methodParamInfoMap = methodParamParseHandler.analysis(handlerMethodMap);
        // 方法返回解析
        Map<String, MethodReturnInfo> methodReturnInfoMap = methodReturnParseHandler.analysis(handlerMethodMap);

        handlerMethodMap.forEach((rmi, hm) -> {
            // 方法全名[类全路径.方法名]
            String methodFullName = RequestUtils.genMethodFullName(hm);

            // 接口类型校验
            boolean isPageRequest = RequestUtils.isPageRequest(hm);
            if (isPageRequest) {
                log.debug(MiniDocConstant.marker, "[{}]{}为页面，不进行接口数据解析.", methodFullName, rmi.getPatternValues());
                return;
            }
            boolean isJsonRequest = RequestUtils.isJsonRequest(hm);
            if (!isJsonRequest) {
                log.debug(MiniDocConstant.marker, "[{}]{}为非JSON格式接口，不进行接口数据解析.", methodFullName, rmi.getPatternValues());
                return;
            }

            // 获取方法注解
            List<AnnotationInfo> methodAnnList = MethodUtils.parseAnnInfoList(hm.getMethod().getAnnotations());
            // 方法参数获取
            List<MethodParamInfo> methodParamInfoList = methodParamInfoMap.getOrDefault(methodFullName, CollectionUtils.newArrayList());
            // 方法返回获取
            MethodReturnInfo methodReturnInfo = methodReturnInfoMap.getOrDefault(methodFullName, null);
            // 请求方法集合
            final Set<String> methodList = RequestMethodConstant.getMethodOrGetAll(rmi.getMethodsCondition().getMethods().stream().map(RequestMethod::toString).collect(Collectors.toSet()));

            rmi.getPatternValues().forEach(reqUrl -> { // 请求接口遍历
                RestMethodInfo restMethodInfo = new RestMethodInfo();
                restMethodInfo.setCtrlName(hm.getBeanType().getTypeName());
                restMethodInfo.setMethodName(hm.getMethod().getName());
                restMethodInfo.setMethodAnnList(methodAnnList);
                restMethodInfo.setMethodParamList(methodParamInfoList);
                restMethodInfo.setMethodReturnInfo(methodReturnInfo);
                restMethodInfo.setReqUrl(reqUrl);
                restMethodInfo.setReqMethodList(methodList);
                restMethodInfo.setComment(rmi.getName());
                resultList.add(restMethodInfo);
            });
        });
        return resultList;
    }

}
