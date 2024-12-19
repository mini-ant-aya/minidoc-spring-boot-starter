package org.github.minidoc;

import lombok.Getter;
import lombok.Setter;
import org.github.minidoc.core.annotation.DocField;

import java.io.Serializable;

@Setter
@Getter
public class CaptchaTempDto implements Serializable {

    private static final long serialVersionUID = -3296207811340164610L;

    @DocField("临时参数")
    private String temp;

    @DocField("验证码对象")
    private CaptchaDto dto;

}
