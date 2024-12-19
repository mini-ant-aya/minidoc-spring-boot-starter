package org.github.minidoc.model.method;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
public class MethodParamInfo implements Serializable {

    private static final long serialVersionUID = -7081015725043764502L;

    /**
     * 参数类型[全量包名]
     */
    private String fullType;
    /**
     * 参数类型[只有类型名]
     */
    private String simpleType;
    /**
     * 参数名称
     */
    private String paramName;
    /**
     * 参数名称
     */
    private String argName;
    /**
     * 注解
     */
    private List<AnnotationInfo> annList;

}
