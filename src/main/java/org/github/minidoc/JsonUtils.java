package org.github.minidoc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

@Slf4j
public class JsonUtils {

    private static final ObjectMapper objectMapper;

    static {
//        String DEFAULT_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
//        String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
//        String DEFAULT_TIME_PATTERN = "HH:mm:ss";
//        //日期序列化器
//        JavaTimeModule javaTimeModule = new JavaTimeModule();
//        //日期序列化
//        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_PATTERN)));
//        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN)));
//        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_PATTERN)));
//        //日期反序列化
//        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_PATTERN)));
//        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN)));
//        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_PATTERN)));
//        //注册序列化器
//        objectMapper.registerModule(javaTimeModule);

        objectMapper = new ObjectMapper();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        objectMapper.setDateFormat(dateFormat);
        // 该特性决定了当遇到未知属性（没有映射到属性，没有任何setter或者任何可以处理它的handler），是否应该抛出一个
        // JsonMappingException异常。这个特性一般式所有其他处理方法对未知属性处理都无效后才被尝试，属性保留未处理状态。
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 序列化时, 禁止自动缩进 (格式化)(默认false) 输出的 json (压缩输出)
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, false);
        // 序列化时, 将各种时间日期类型统一序列化为 timestamp 而不是其字符串表示
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    /**
     * 将java类型的对象转换为JSON格式的字符串
     *
     * @param object 参数
     */
    public static <T> String toJson(T object) {
        if (object == null) {
            return null;
        }
        try {
            return getObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.debug(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 将JSON格式的字符串转换为java类型的对象或者java数组类型的对象
     *
     * @param json
     * @param typeReference
     * @return
     */
    public static <T> T parseJson(String json, TypeReference<T> typeReference) {
        if (!StringUtils.hasText(json)) {
            return null;
        }
        try {
            return getObjectMapper().readValue(json, typeReference);
        } catch (IOException e) {
            log.debug(e.getMessage(), e);
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 将JSON格式的字符串转换为java类型的对象或者java数组类型的对象
     *
     * @param json
     * @param clz
     * @return
     */
    public static <T> T parseJson(String json, Class<T> clz) {
        if (!StringUtils.hasText(json)) {
            return null;
        }
        try {
            return getObjectMapper().readValue(json, clz);
        } catch (IOException e) {
            log.debug(e.getMessage(), e);
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 校验是否是json
     *
     * @param json
     * @return
     */
    public static boolean isJson(String json) {
        if (!StringUtils.hasText(json)) {
            return false;
        }
        try {
            getObjectMapper().readTree(json);
            return true;
        } catch (IOException e) {
            log.debug(e.getMessage(), e);
            log.error(e.getMessage());
        }
        return false;
    }

}
