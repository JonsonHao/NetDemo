package com.linjunhao.arouter_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: linjunhao
 * @e-mail: linjunhao@xmiles.cn
 * @date: 2022/3/15
 * @desc:
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface ARouter {
}
