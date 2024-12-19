package org.github.minidoc.core.constant;

import org.github.minidoc.core.enums.MethodType;
import org.github.minidoc.util.CollectionUtils;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public final class RequestMethodConstant {

    // 请求方法集合
    private static final Set<String> rootSet = new LinkedHashSet<>();

    static {
        rootSet.add(MethodType.GET.getHttpMethod());
        rootSet.add(MethodType.POST.getHttpMethod());
        rootSet.add(MethodType.PUT.getHttpMethod());
        rootSet.add(MethodType.PATCH.getHttpMethod());
        rootSet.add(MethodType.DELETE.getHttpMethod());
    }

    /**
     * 获取全部请求方法类型
     *
     * @param methodList 请求方法集合
     * @return 请求方法集合
     */
    public static Set<String> getMethodOrGetAll(Set<String> methodList) {
        if (CollectionUtils.isEmpty(methodList)) return rootSet;
        return rootSet.stream().filter(methodList::contains).collect(Collectors.toSet());
    }

}
