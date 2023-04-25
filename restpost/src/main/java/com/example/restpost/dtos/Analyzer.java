package com.example.restpost.dtos;

import lombok.val;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

public class Analyzer {

    public boolean containsNull(Object obj) throws IllegalAccessException {
        if (obj == null) {
            return true;
        }
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            val value = field.get(obj);
            if (value == null) {
                return true;
            } else if (value.getClass().isArray()) {
                if (arrayCheck(value)) {return true;}
            } else if (value instanceof Collection<?>) {
                if (collectionCheck((Collection<?>) value)) {return true;}
            } else if (value.getClass().getPackage().getName().contains("com.example.restpost.dtos")) {
                if (containsNull(value)) { return true;}
            } else if (value instanceof Map<?,?>) {
                if (mapCheck((Map<?,?>)value)) {return true;}
            }
        }
        return false;
    }

    private boolean arrayCheck(Object value) throws IllegalAccessException {
        int length = java.lang.reflect.Array.getLength(value);
        if (length ==0) {return true;}
        for (int i = 0; i < length; i++) {
            Object arrayElement = java.lang.reflect.Array.get(value, i);
            if (containsNull(arrayElement)) { return true; }
        }
        return false;
    }

    private boolean collectionCheck(Collection<?> value) throws IllegalAccessException {
        if (value.size() == 0) { return true;}
        for (Object v : value) {
            if (containsNull(v)) {return true;}
        }
        return false;
    }

    private boolean mapCheck(Map<?,?> value) throws IllegalAccessException {
        if(value.size() ==0) {return true;}
        for (Object key : value.keySet()) {
            if (containsNull(value.get(key))) {return true;}
        }
        return false;
    }
}
