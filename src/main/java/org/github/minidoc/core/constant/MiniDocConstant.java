package org.github.minidoc.core.constant;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * MiniDoc Constant
 */
public final class MiniDocConstant {

    /**
     * 扫描包名
     */
    public static final String PACKAGE_COMPONENT = "org.github.minidoc";
    /**
     * 配置文件属性前缀
     */
    public static final String MINIDOC_PROPERTY_PREFIX = "minidoc";

    /**
     * 日志根节点打印标签
     */
    public static final Marker marker = MarkerFactory.getMarker("minidoc");

}
