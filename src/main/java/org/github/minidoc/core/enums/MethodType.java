package org.github.minidoc.core.enums;

/**
 * 请求方式与注解映射
 */
public enum MethodType {

    GET("GET", "GetMapping"),
    PUT("PUT", "PutMapping"),
    POST("POST", "PostMapping"),
    DELETE("DELETE", "DeleteMapping"),
    PATCH("PATCH", "PatchMapping"),
    DEFAULT("DEFAULT", "RequestMapping");

    private String httpMethod;
    private String annotation;

    MethodType(String httpMethod, String annotation) {
        this.httpMethod = httpMethod;
        this.annotation = annotation;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getAnnotation() {
        return annotation;
    }

}
