package org.github.minidoc;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.github.minidoc.core.annotation.DocField;

import java.io.Serializable;
import java.util.Map;

/**
 * 表格分页数据对象
 */
@Setter
@Getter
@ToString
public class MapResp<T> implements Serializable {
    private static final long serialVersionUID = 767886673436605034L;

    /**
     * 是否成功[Y：成功、N：失败]
     */
    @DocField("成功标识")
    private boolean succFlag = Boolean.TRUE;

    /**
     * 总记录数
     */
//    @JsonSerialize(using = ToStringSerializer.class)
    @DocField("总记录数")
    private int total = 0;

    /**
     * 列表数据
     */
    @DocField("列表数据")
    private Map<String, T> rows;

    /**
     * 消息状态码
     */
    @DocField("状态码")
    private int code;

    /**
     * 消息内容
     */
    @DocField("提示消息")
    private String msg;

}
