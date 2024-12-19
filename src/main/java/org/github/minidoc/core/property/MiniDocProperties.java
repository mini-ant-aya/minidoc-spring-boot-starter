package org.github.minidoc.core.property;

import org.github.minidoc.core.constant.MiniDocConstant;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;

@ConfigurationProperties(prefix = MiniDocConstant.MINIDOC_PROPERTY_PREFIX)
public class MiniDocProperties extends Properties {

    /**
     * 开关
     * 【true：开启、false：关闭】
     * 【默认：true】
     */
    private Boolean enable = true;

    public Boolean getEnable() {
        return enable;
    }

    public MiniDocProperties setEnable(Boolean enable) {
        this.enable = enable;
        return this;
    }

}
