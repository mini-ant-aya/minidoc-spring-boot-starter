package org.github.minidoc;

import lombok.RequiredArgsConstructor;
import org.github.minidoc.core.annotation.DocField;
import org.github.minidoc.handler.docs.MiniDocParseHandler;
import org.github.minidoc.handler.method.MiniDocMethodParseHandler;
import org.github.minidoc.model.docs.RestDocInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestCtrl {

    private final MiniDocMethodParseHandler miniDocMethodParseHandler;
    private final MiniDocParseHandler miniDocParseHandler;

    @PostConstruct
    public void init() {
//        List<RestMethodInfo> restMethodInfoList = miniDocMethodParseHandler.genMethodDocs();
//        restMethodInfoList.forEach(restfulInfo -> {
//            System.out.println("----------------------------------------------------------");
//            System.out.println(JsonUtils.toJson(restfulInfo));
//            System.out.println("----------------------------------------------------------");
//        });
        System.out.println("----------------------------------------------------------");
        List<RestDocInfo> restDocInfoList = miniDocParseHandler.genRestDocs();
        restDocInfoList.forEach(restfulInfo -> {
            System.out.println("----------------------------------------------------------");
            System.out.println(JsonUtils.toJson(restfulInfo));
            System.out.println("----------------------------------------------------------");
        });
    }

//    /**
//     * 生成验证码
//     */
//    @GetMapping(value = "/abc", name = "生成验证码")
//    public void abc11(@DocField("前端入参") @RequestBody List<CaptchaDto> dataResp) {
//    }

//    /**
//     * 生成验证码
//     */
//    @GetMapping(value = "/abc", name = "生成验证码")
//    public DataResp<List<CaptchaDto>> abc11(@DocField("前端对象") @RequestParam("aaa") @RequestBody String info) {
//        return new DataResp<>();
//    }

    /**
     * 生成验证码
     */
    @GetMapping(value = "/abc", name = "生成验证码")
    public void abc11(@DocField("前端对象") @RequestParam("aaa") @RequestBody TestDto<String, Integer, List<CaptchaDto>, CaptchaDto> info) {
    }

//    /**
//     * 生成验证码
//     */
//    @GetMapping(value = "/abc11/{abc}", name = "生成验证码")
//    public List<CaptchaDto> abc11(@DocField(comment = "名称") @PathVariable(value = "abc") @RequestParam String abc, @DocField(comment = "数据") @RequestBody List<CaptchaDto> list, @DocField(comment = "数据") @RequestBody String name) {
//        return new ArrayList<>();
//    }

//    /**
//     * 生成验证码
//     */
//    @GetMapping(value = "/abc/{abc}", name = "生成验证码")
//    public DataResp<Map<String, String>> abc(@DocField(comment = "名称") @PathVariable(value = "abc") String abc) {
//        return new DataResp<>();
//    }

//    /**
//     * 生成验证码
//     */
//    @GetMapping(value = "/gen/{abc}", name = "生成验证码")
//    public KeyValue<CaptchaTempDto, DataResp<CaptchaTempDto>> gencode(@PathVariable(value = "abc") String abc, @DocField(comment = "名称") @RequestAttribute(name = "name") String name, @DocField(comment = "邮箱") @RequestParam(value = "emails", required = false) String email, @DocField(comment = "唯一标识") String uuid, @ModelAttribute CaptchaDto dto) {
//        return new KeyValue<>();
//    }

}
