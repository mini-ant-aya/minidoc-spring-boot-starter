package org.github.minidoc.model.method;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 注解方法对象
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class AnnotationMethodInfo implements Serializable {

    private static final long serialVersionUID = 2195227683428888621L;

    /**
     * 方法名称
     */
    private String methodName;
    /**
     * 参数名称
     */
    private String returnType;

}
