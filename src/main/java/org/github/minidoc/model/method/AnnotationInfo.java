package org.github.minidoc.model.method;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 注解对象
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class AnnotationInfo implements Serializable {

    private static final long serialVersionUID = 3255792495239352009L;

    /**
     * 参数类型[全量包名]
     */
    private String fullType;
    /**
     * 参数类型[只有类型名]
     */
    private String simpleType;
    /**
     * 方法列表
     */
    private List<AnnotationMethodInfo> methodList;

}
