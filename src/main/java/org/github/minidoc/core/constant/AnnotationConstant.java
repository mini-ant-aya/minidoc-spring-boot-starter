package org.github.minidoc.core.constant;

import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;

/**
 * 注解常量
 */
public final class AnnotationConstant {

    public static final Class<Annotation>[] ANN_ALL = new Class[]{
            ControllerAdvice.class,
            CookieValue.class,
            CrossOrigin.class,
            DeleteMapping.class,
            ExceptionHandler.class,
            GetMapping.class,
            InitBinder.class,
            Mapping.class,
            MatrixVariable.class,
            ModelAttribute.class,
            PatchMapping.class,
            PathVariable.class,
            PostMapping.class,
            PutMapping.class,
            RequestAttribute.class,
            RequestBody.class,
            RequestHeader.class,
            RequestMapping.class,
            RequestParam.class,
            RequestPart.class,
            ResponseBody.class,
            ResponseStatus.class,
            RestController.class,
            RestControllerAdvice.class,
            SessionAttribute.class,
            SessionAttributes.class
    };

    /**
     * RequestHeader
     * 功能：绑定请求头数据；
     */
    public static final Class<Annotation>[] ANN_HEADER_ARR = new Class[]{RequestHeader.class};
    /**
     * 链接参数注解
     * PathVariable
     * 功能：绑定URI模板变量值
     * MatrixVariable
     * 功能：用于接收URL的path中的矩阵参数
     * 语法格式：XXX/XXX/path;name=value;name=value
     * 开启功能：
     * （1）如果是xml配置的RequestMappingHandlerMap ping，那么需要设置removeSemicolonContent属性为false
     * （2）如果是注解的方式，直接设置的enableMatrixVariables=“true“ 就可以了
     */
    public static final Class<Annotation>[] ANN_URL_ARR = new Class[]{PathVariable.class, MatrixVariable.class};
    /**
     * 链接后拼接参数 ？ 后的参数或者form中的
     * RequestParam
     * 功能：绑定单个请求参数值；
     * RequestPart
     * 功能：绑定“multipart/data”数据，除了能绑定@RequestParam能做到的请求参数外，还能绑定上传的文件等。
     */
    public static final Class<Annotation>[] ANN_QUERY_PARAM_ARR = new Class[]{RequestParam.class, RequestPart.class};
    /**
     * ModelAttribute
     * 1：绑定请求参数到命令对象：放在功能处理方法的入参上时，用于将多个请求参数绑定到一个命令对象，从而简化绑定流程，而且自动暴露为模型数据用于视图页面展示时使用；
     * 2：暴露表单引用对象为模型数据：放在处理器的一般方法（非功能处理方法）上时，是为表单准备要展示的表单引用对象，如注册时需要选择的所在城市等，而且在执行功能处理方法（@RequestMapping注解的方法）之前，自动添加到模型对象中，用于视图页面展示时使用；
     * 3：暴露@RequestMapping方法返回值为模型数据：放在功能处理方法的返回值上时，是暴露功能处理方法的返回值为模型数据，用于视图页面展示时使用。
     */
    public static final Class<Annotation>[] ANN_FORM_PARAM_ARR = new Class[]{ModelAttribute.class};
    /**
     * json中的数据
     * RequestBody
     * 功能：绑定请求的内容区数据并能进行自动类型转换等。
     */
    public static final Class<Annotation>[] ANN_JSON_PARAM_ARR = new Class[]{RequestBody.class};

}
