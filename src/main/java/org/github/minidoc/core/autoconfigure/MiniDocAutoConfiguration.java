package org.github.minidoc.core.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.github.minidoc.core.constant.MiniDocConstant;
import org.github.minidoc.core.property.MiniDocProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Set;

@Slf4j
@Lazy(false)
@Configuration
@ConfigurationPropertiesScan
@ComponentScan({MiniDocConstant.PACKAGE_COMPONENT})
@EnableConfigurationProperties({MiniDocProperties.class})
public class MiniDocAutoConfiguration {

    private final MiniDocProperties properties;

    public MiniDocAutoConfiguration(MiniDocProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    public void init() {
        if (log.isTraceEnabled()) {
            log.trace(MiniDocConstant.marker, "========================================");
            log.trace(MiniDocConstant.marker, "[核心组件]配置文件加载完毕");
            log.trace(MiniDocConstant.marker, "开始打印配置信息");
            Set<Map.Entry<Object, Object>> entrySet = properties.entrySet();
            for (Map.Entry<Object, Object> entry : entrySet) {
                log.trace(MiniDocConstant.marker, "[{}]  =>  [{}]", entry.getKey(), entry.getValue());
            }
            log.trace(MiniDocConstant.marker, "结束打印配置信息");
            log.trace(MiniDocConstant.marker, "========================================");
        }
    }

}
