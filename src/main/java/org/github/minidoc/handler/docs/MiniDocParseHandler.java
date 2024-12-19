package org.github.minidoc.handler.docs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.github.minidoc.core.constant.MiniDocConstant;
import org.github.minidoc.core.constant.RequestMethodConstant;
import org.github.minidoc.core.enums.ContentType;
import org.github.minidoc.core.property.MiniDocProperties;
import org.github.minidoc.handler.docs.params.HeaderParamParseHandler;
import org.github.minidoc.handler.docs.params.RequestParamParseHandler;
import org.github.minidoc.handler.docs.params.UrlParamParseHandler;
import org.github.minidoc.handler.docs.returns.ReturnInfoParseHandler;
import org.github.minidoc.model.docs.ParamInfo;
import org.github.minidoc.model.docs.RestDocInfo;
import org.github.minidoc.util.CollectionUtils;
import org.github.minidoc.util.RequestUtils;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.http.MediaType;
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
public class MiniDocParseHandler {

    private final MiniDocProperties properties;
    private final HeaderParamParseHandler headerParamParseHandler;
    private final UrlParamParseHandler urlParamParseHandler;
    private final RequestParamParseHandler requestParamParseHandler;
    private final ReturnInfoParseHandler returnInfoParseHandler;
    private final RequestMappingHandlerMapping handlerMapping;

    /**
     * 生成Controller接口文档
     *
     * @return 接口文档列表
     */
    public List<RestDocInfo> genRestDocs() {
        if (!properties.getEnable()) {
            log.info(MiniDocConstant.marker, "接口文档解析操作取消，原因是未开启解析操作，如想开启，请检查相关配置");
            return CollectionUtils.newArrayList();
        }
        // 请求接口集合
        List<RestDocInfo> resultList = CollectionUtils.newArrayList();
        // 获取所有的Mapping
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = handlerMapping.getHandlerMethods();
        DefaultParameterNameDiscoverer dpnd = new DefaultParameterNameDiscoverer();
        handlerMethodMap.entrySet().stream().filter(t -> RequestUtils.isJsonRequest(t.getValue())).forEach(entry -> { // 只解析json的接口
            RequestMappingInfo rmi = entry.getKey();
            HandlerMethod hm = entry.getValue();
            // 请求方法集合
            final Set<String> methodList = RequestMethodConstant.getMethodOrGetAll(rmi.getMethodsCondition().getMethods().stream().map(RequestMethod::toString).collect(Collectors.toSet()));

            // 请求体类型
            final Set<String> contentTypeList = CollectionUtils.merge(
                    rmi.getProducesCondition().getProducibleMediaTypes().stream().map(MediaType::toString).collect(Collectors.toSet()),
                    rmi.getConsumesCondition().getConsumableMediaTypes().stream().map(MediaType::toString).collect(Collectors.toSet()),
                    ContentType.FORM_DATA.getParamType(), ContentType.JSON.getParamType()
            );

            // 请求头参数获取
            List<ParamInfo> headerParamInfoList = headerParamParseHandler.analysis(rmi, hm, dpnd);
            // 链接参数获取
            List<ParamInfo> urlParamInfoList = urlParamParseHandler.analysis(rmi, hm, dpnd);
            // Query参数获取
            List<ParamInfo> queryParamInfoList = requestParamParseHandler.analysisQueryParam(rmi, hm, dpnd);
            // 表单参数获取
            List<ParamInfo> formParamInfoList = requestParamParseHandler.analysisFormParam(rmi, hm, dpnd);
            // 请求参数获取
            ParamInfo jsonParamInfo = requestParamParseHandler.analysisJsonParam(rmi, hm, dpnd);
            // 响应对象获取
            ParamInfo returnInfo = returnInfoParseHandler.analysis(rmi, hm);

            rmi.getPatternValues().forEach(reqUrl -> { // 请求接口遍历
                RestDocInfo restDocInfo = new RestDocInfo();
                restDocInfo.setReqUrl(reqUrl);
                restDocInfo.setComment(rmi.getName());
                restDocInfo.setReqMethodList(methodList);
                restDocInfo.setContentTypeList(contentTypeList);
                restDocInfo.setHasHeaderParam(CollectionUtils.isNotEmpty(headerParamInfoList));
                restDocInfo.setHeaderParamInfoList(headerParamInfoList);
                restDocInfo.setHasUrlParam(CollectionUtils.isNotEmpty(urlParamInfoList));
                restDocInfo.setUrlParamInfoList(urlParamInfoList);
                restDocInfo.setHasQueryParam(CollectionUtils.isNotEmpty(queryParamInfoList));
                restDocInfo.setQueryParamInfoList(queryParamInfoList);
                restDocInfo.setHasFormParam(CollectionUtils.isNotEmpty(formParamInfoList));
                restDocInfo.setFormParamInfoList(formParamInfoList);
                restDocInfo.setHasJsonParam(jsonParamInfo != null ? true : false);
                restDocInfo.setJsonParamInfo(jsonParamInfo);
                restDocInfo.setHasResp(!hm.isVoid());
                restDocInfo.setReturnInfo(returnInfo);
                resultList.add(restDocInfo);
            });
        });
        return resultList;
    }

}
