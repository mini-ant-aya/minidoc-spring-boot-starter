package org.github.minidoc.model.method;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 接口方法信息
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class RestMethodInfo implements Serializable {

    private static final long serialVersionUID = 8704524907211336271L;


    // ------------------------------------- 接口类、方法信息

    /**
     * 接口地址
     */
    private String reqUrl;
    /**
     * 请求方法集合
     */
    private Set<String> reqMethodList;
    /**
     * 接口描述
     */
    private String comment;
    /**
     * 接口类名称
     */
    private String ctrlName;
    /**
     * 接口方法
     */
    private String methodName;
    /**
     * 方法注解列表
     */
    private List<AnnotationInfo> methodAnnList;
    /**
     * 方法参数注解集合
     */
    private List<MethodParamInfo> methodParamList;
    /**
     * 方法返回类型
     */
    private MethodReturnInfo methodReturnInfo;

    // ------------------------------------- 接口类、方法信息

}
