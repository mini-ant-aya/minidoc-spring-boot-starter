package org.github.minidoc.core.enums;

/**
 * 解析类型
 */
public enum ParseType {

    PARAM("请求参数"), // param
    RETURN("返回参数"), // return
    ;

    ParseType(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return name;
    }

}
