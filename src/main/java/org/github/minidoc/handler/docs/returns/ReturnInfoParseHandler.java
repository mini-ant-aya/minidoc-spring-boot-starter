package org.github.minidoc.handler.docs.returns;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.github.minidoc.component.ParamParserComponent;
import org.github.minidoc.core.constant.RootTypeConstant;
import org.github.minidoc.core.enums.ParseType;
import org.github.minidoc.core.exception.MiniDocException;
import org.github.minidoc.model.docs.ParamInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

@Slf4j
@Component("DocsReturnParseHandler")
@RequiredArgsConstructor
public class ReturnInfoParseHandler {

    private final ParamParserComponent paramParser;

    /**
     * 解析接口文档返回数据
     *
     * @param rmi 接口信息
     * @param hm  接口方法信息
     * @return 返回对象数据
     */
    public ParamInfo analysis(RequestMappingInfo rmi, HandlerMethod hm) {
        if (rmi == null) throw new MiniDocException("解析接口返回值，RequestMappingInfo为空，请及时检查代码");
        if (hm == null) throw new MiniDocException("解析接口返回值，HandlerMethod为空，请及时检查代码");
        String typeName = hm.getReturnType().getParameterType().getTypeName();
        if (hm.isVoid() || RootTypeConstant.ignore(typeName)) { // 无返回值
            return null;
        }
        return paramParser.parseMethodParamType(rmi, hm.getReturnType(), ParseType.RETURN);
    }


}
