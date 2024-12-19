package org.github.minidoc.core.enums;

import org.springframework.http.MediaType;

/**
 * 接口请求参数类型
 */
public enum ContentType {

    PATH_PARAMS("Path_Params"), // PathVariable
    QUERY_PARAMS("Query_Params"), // RequestParam
    FORM_URLENCODED(MediaType.APPLICATION_FORM_URLENCODED_VALUE), // form-urlencoded
    FORM_DATA(MediaType.MULTIPART_FORM_DATA_VALUE), // form-data
    JSON(MediaType.APPLICATION_JSON_VALUE); // RequestBody

    private String paramType;

    ContentType(String paramType) {
        this.paramType = paramType;
    }

    public String getParamType() {
        return paramType;
    }

}
