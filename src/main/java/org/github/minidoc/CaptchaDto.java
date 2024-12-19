package org.github.minidoc;

import lombok.Getter;
import lombok.Setter;
import org.github.minidoc.core.annotation.DocField;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@DocField("验证码对象")
public class CaptchaDto implements Serializable {

    @DocField("验证码开启标识[true:开启,false:关闭]")
    private Boolean captchaEnabled;
    @DocField("验证码唯一标识")
    private String uuid;
    @DocField("验证码图片信息")
    private String img;
    @DocField("资源列表")
    private List<CaptchaTempDto> resList;

}
