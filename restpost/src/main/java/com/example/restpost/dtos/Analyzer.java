package com.example.restpost.dtos;

import lombok.val;

import java.lang.reflect.Field;
import java.util.Collection;

public class Analyzer {


    public boolean containsNull(Object obj) throws IllegalAccessException {
        if (obj == null) {
            return true;
        }

        Class<?> clazz = obj.getClass();


        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            val value = field.get(obj);
            if (value == null) {
                return true;
            } else if (value.getClass().isArray()) {
                int length = java.lang.reflect.Array.getLength(value);
                for (int i = 0; i < length; i++) {
                    Object arrayElement = java.lang.reflect.Array.get(value, i);
                    if (containsNull(arrayElement)) {
                        return true;
                    }
                }
            } else if (value instanceof Collection<?>) {
                if(((Collection<?>) value).size() == 0) {
                    return true;
                }

                for (Object v : ((Collection<?>) value)) {
                    {
                        try {
                            if (containsNull(v)) {
                                return true;
                            }
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }




            } else if (!field.getType().isPrimitive() && !field.getType().equals(String.class) &&
                    value.getClass().getPackage().getName().contains("com.example.restpost.dtos")) {

                if (containsNull(value)) {
                    return true;
                }
            }
        }

        return false;
    }



}
