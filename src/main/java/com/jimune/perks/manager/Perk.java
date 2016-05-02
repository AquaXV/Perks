package com.jimune.perks.manager;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Perk {

    String name();
    String permission();
    int cooldown() default 0;
    String[] alias() default "";
    boolean allowpvp() default true;
    boolean crossWorld() default true;

}
