package org.github.minidoc;

import java.util.Date;

public class PrimitiveTest {
    public static void main(String[] args) {
        Class<?> numberClazz = Long.class;
        Class<?> timeClazz = Date.class;
        Class<?> sqlTimeClazz = java.sql.Date.class;
        System.out.println(Number.class.isAssignableFrom(numberClazz));
        System.out.println(Date.class.isAssignableFrom(timeClazz));
        System.out.println(Date.class.isAssignableFrom(sqlTimeClazz));
    }
}