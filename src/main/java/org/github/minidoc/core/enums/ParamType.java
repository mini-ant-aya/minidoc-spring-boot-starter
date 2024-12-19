package org.github.minidoc.core.enums;

import org.github.minidoc.core.constant.RootTypeConstant;

/**
 * 参数类型
 */
public enum ParamType {

    L, // List类型
    M, // Map 类型
    O, // Object[基本类型]
    ;

    public static ParamType getType(String type) {
        if (RootTypeConstant.isListType(type)) {
            return ParamType.L;
        } else if (RootTypeConstant.isMapType(type)) {
            return ParamType.M;
        } else {
            return ParamType.O;
        }
    }

}
