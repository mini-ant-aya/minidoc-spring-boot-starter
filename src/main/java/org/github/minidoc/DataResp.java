package org.github.minidoc;

import lombok.Data;
import lombok.experimental.Accessors;
import org.github.minidoc.core.annotation.DocField;

import java.io.Serializable;

/**
 * 响应信息主体
 */
@Data
@Accessors(chain = true)
public class DataResp<T> implements Serializable {

    private static final long serialVersionUID = 5639138840062703168L;

    @DocField("成功标识")
    private boolean succFlag = Boolean.TRUE;

    /**
     * 响应编码
     */
    @DocField("状态码")
    private int code;
    /**
     * 提示消息
     */
    @DocField("提示消息")
    private String msg;
    /**
     * 响应数据
     */
    @DocField("明细数据")
    private T data;

}
