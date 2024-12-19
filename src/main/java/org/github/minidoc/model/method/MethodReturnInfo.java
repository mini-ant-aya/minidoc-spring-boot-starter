package org.github.minidoc.model.method;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
@EqualsAndHashCode
public class MethodReturnInfo implements Serializable {

    private static final long serialVersionUID = -8177697746128503394L;

    /**
     * 参数类型[全量包名]
     */
    private String fullType;
    /**
     * 参数类型[只有类型名]
     */
    private String simpleType;

}
