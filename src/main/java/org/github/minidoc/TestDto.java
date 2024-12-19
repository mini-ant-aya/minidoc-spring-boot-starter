package org.github.minidoc;

import lombok.Getter;
import lombok.Setter;
import org.github.minidoc.core.annotation.DocField;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
public class TestDto<A, B, C, D> implements Serializable {

    private static final long serialVersionUID = -1280714410234786197L;

    @DocField("aa对象")
    private A aa;
    @DocField("bb对象")
    private B bb;
    @DocField("cc对象")
    private C cc;
    @DocField("dd列表")
    private List<D> ddList;
    @DocField("的是")
    private TestDto<B, A, C, D> testDto;

}
