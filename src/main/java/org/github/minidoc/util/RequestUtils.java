package org.github.minidoc.util;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

public final class RequestUtils {

    private RequestUtils() {
    }

    public static final String METHOD_PRE_GET = "get";
    public static final String METHOD_PRE_IS = "is";

    /**
     * 组装方法全名
     * <p>
     * 方法全名[类全路径.方法名]
     *
     * @param hm 请求方法
     * @return 示例：org.github.pt.mvc.ctrl.system.SysDeptCtrl.list
     */
    public static String genMethodFullName(HandlerMethod hm) {
        return String.format("%s.%s", hm.getBeanType().getTypeName(), hm.getMethod().getName());
    }

    /**
     * 判断是否是page请求
     *
     * @param hm 请求方法
     * @return true:是json、false:不是json
     */
    public static boolean isPageRequest(HandlerMethod hm) {
        return hm.getBeanType().isAnnotationPresent(Controller.class) && (hm.getMethod().getReturnType().equals(String.class) || hm.getMethod().getReturnType().equals(ModelAndView.class));
    }

    /**
     * 判断是否是json请求
     *
     * @param hm 请求方法
     * @return true:是json、false:不是json
     */
    public static boolean isJsonRequest(HandlerMethod hm) {
        return hm.getBeanType().isAnnotationPresent(RestController.class) || (hm.getBeanType().isAnnotationPresent(Controller.class) && hm.getMethod().isAnnotationPresent(ResponseBody.class));
    }

}
