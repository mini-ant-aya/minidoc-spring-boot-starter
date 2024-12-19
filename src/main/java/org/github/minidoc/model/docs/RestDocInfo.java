package org.github.minidoc.model.docs;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@ToString
@EqualsAndHashCode
public class RestDocInfo implements Serializable {

    private static final long serialVersionUID = 6888652226876129878L;

    /**
     * 接口地址
     */
    private String reqUrl;

    /**
     * 接口描述
     */
    private String comment;
    /**
     * 请求方法类型
     */
    private Set<String> reqMethodList;
    /**
     * 请求体类型
     */
    private Set<String> contentTypeList;
    /**
     * 是否有请求头[默认没有]
     */
    private Boolean hasHeaderParam = false;
    /**
     * 请求头参数
     */
    private List<ParamInfo> headerParamInfoList;
    /**
     * 是否有链接参数[默认没有]
     */
    private Boolean hasUrlParam = false;
    /**
     * url参数信息
     */
    private List<ParamInfo> urlParamInfoList;
    /**
     * 是否有Query参数值[默认没有]
     */
    private Boolean hasQueryParam = false;
    /**
     * Query参数信息
     */
    private List<ParamInfo> queryParamInfoList;
    /**
     * 是否有参数值[默认没有]
     */
    private Boolean hasFormParam = false;
    /**
     * 参数信息
     */
    private List<ParamInfo> formParamInfoList;
    /**
     * 是否有json参数值[默认没有]
     */
    private Boolean hasJsonParam = false;
    /**
     * json参数信息
     */
    private ParamInfo jsonParamInfo;
    /**
     * 是否有响应值[默认没有]
     */
    private Boolean hasResp = false;
    /**
     * 返回信息
     */
    private ParamInfo returnInfo;

}
