package org.github.minidoc.model.docs;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.github.minidoc.core.enums.ParamType;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
public class ParamInfo implements Serializable {

    private static final long serialVersionUID = -3497290534950884434L;

    /**
     * 类型所用类
     */
    private Class typeClazz;
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
    private String name;
    /**
     * 参数描述
     */
    private String comment;
    /**
     * 是否必填
     */
    private Boolean mustFlag = false;
    /**
     * 示例值
     */
    private String demoValue;
    /**
     * 类型[O:obj、L:list、M:map]
     */
    private ParamType paramType;
    /**
     * 是否显示当前节点：
     */
    private boolean showCurrent = true;
    /**
     * 是否虚拟节点(默认不是，虚拟节点不显示，fieldList，根据showField进行显示)
     */
    private boolean virtualFlag = false;
    /**
     * 是否显示field节点：
     */
    private boolean showField = true;
    /**
     * 属性信息
     */
    private List<ParamInfo> fieldList;

}
