package org.github.minidoc.handler.method;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.github.minidoc.model.method.MethodReturnInfo;
import org.github.minidoc.util.MapUtils;
import org.github.minidoc.util.RequestUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class MethodReturnParseHandler {

    /**
     * 解析接口文档返回数据
     *
     * @param handlerMethodMap 方法信息Map
     * @return 结果 key:方法全量名、value:返回信息对象
     */
    public Map<String, MethodReturnInfo> analysis(Map<RequestMappingInfo, HandlerMethod> handlerMethodMap) {
        Map<String, MethodReturnInfo> paramInfoMap = MapUtils.newHashMap();
        if (MapUtils.isEmpty(handlerMethodMap)) {
            return paramInfoMap;
        }
        handlerMethodMap.forEach((rmi, hm) -> {
            // 方法全名[类全路径.方法名]
            String methodFullName = RequestUtils.genMethodFullName(hm);
            paramInfoMap.put(methodFullName, null);
            if (!hm.isVoid()) {
                MethodReturnInfo methodReturnInfo = new MethodReturnInfo();
                MethodParameter methodParameter = hm.getReturnType();
                methodReturnInfo.setFullType(methodParameter.getParameterType().getTypeName());
                methodReturnInfo.setSimpleType(methodParameter.getParameterType().getSimpleName());
                paramInfoMap.put(methodFullName, methodReturnInfo);
            }
        });
        return paramInfoMap;
    }

}
