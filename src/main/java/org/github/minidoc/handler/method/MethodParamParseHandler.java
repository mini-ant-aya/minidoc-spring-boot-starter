package org.github.minidoc.handler.method;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.github.minidoc.model.method.MethodParamInfo;
import org.github.minidoc.util.CollectionUtils;
import org.github.minidoc.util.MapUtils;
import org.github.minidoc.util.MethodUtils;
import org.github.minidoc.util.RequestUtils;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@RequiredArgsConstructor
public class MethodParamParseHandler {

    /**
     * 解析方法的参数信息
     *
     * @param handlerMethodMap 方法信息Map
     * @return 结果 key:方法全量名、value:参数列表
     */
    public Map<String, List<MethodParamInfo>> analysis(Map<RequestMappingInfo, HandlerMethod> handlerMethodMap) {
        Map<String, List<MethodParamInfo>> paramInfoMap = MapUtils.newHashMap();
        if (MapUtils.isEmpty(handlerMethodMap)) {
            return paramInfoMap;
        }
        DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
        handlerMethodMap.forEach((rmi, hm) -> {
            // 方法全名[类全路径.方法名]
            String methodFullName = RequestUtils.genMethodFullName(hm);
            Method method = hm.getMethod();
            List<MethodParamInfo> methodParamInfoList = CollectionUtils.newArrayList();
            String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
            AtomicInteger counter = new AtomicInteger();
            Arrays.stream(method.getParameters()).forEach(parameter -> {
                int index = counter.getAndIncrement();
                MethodParamInfo methodParamInfo = new MethodParamInfo();
                methodParamInfo.setFullType(parameter.getType().getTypeName());
                methodParamInfo.setSimpleType(parameter.getType().getSimpleName());
                methodParamInfo.setParamName(parameterNames[index]);
                methodParamInfo.setArgName(parameter.getName());
                methodParamInfo.setAnnList(MethodUtils.parseAnnInfoList(parameter.getDeclaredAnnotations()));
                methodParamInfoList.add(methodParamInfo);
            });
            paramInfoMap.put(methodFullName, methodParamInfoList);
        });
        return paramInfoMap;
    }

}
