package com.suke.zhjg.common.autofull.handler;

import com.suke.zhjg.common.autofull.util.ClassTypeUtil;
import com.suke.zhjg.common.autofull.util.StringSQLUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * @author czx
 * @title: Handler
 * @projectName zhjg
 * @description: TODO
 * @date 2020/8/2711:31
 */
public interface Handler {

    String sql(String table, String queryField, String alias, String conditionField, String condition);

    String sql(String sql, String conditionField);

   void result(Annotation annotation, Field[] fields, Field field, Object obj, String sequence, int level);

    default Object findFieldValue(Field[] fields, String key, Object obj){
        for (Field field : fields){
            if(field.getName().equals(key)){
                try {
                    if(field.getType().getName().toLowerCase().equals("int")){
                        return String.valueOf((int)field.get(obj));
                    }else if(field.getType().getName().toLowerCase().equals("long")){
                        return String.valueOf((long)field.get(obj));
                    }else if(field.getType().getName().toLowerCase().equals("integer")){
                        return String.valueOf((long)field.get(obj));
                    }else {
                        return field.get(obj);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    default Map<Integer,Object> getParam(Field[] fields, Object obj, String sql){
        Map<Integer,Object> paramMap = new HashMap<>();
        Matcher matcher = StringSQLUtil.parse(sql);
        int index = 1;
        while(matcher.find()){
            String fieldKey = matcher.group(1);
            Object param = findFieldValue(fields,fieldKey,obj);
            paramMap.put(index,param);
            index ++;
        }
        return paramMap;
    }

    default  List<Object> getParamList(Field[] fields, Object obj, String sql){
        List<Object> paramList = new ArrayList<>();
        Matcher matcher = StringSQLUtil.parse(sql);
        while(matcher.find()){
            String fieldKey = matcher.group(1);
            Object param = findFieldValue(fields,fieldKey,obj);
            paramList.add(param);
        }
        return paramList;
    }

    default Class<?> getListClassType(Field field){
        Class<?>[] parameterizedType = ClassTypeUtil.getParameterizedListType(field);
        return parameterizedType[0];
    }

    default Class<?> getBeanClassType(Field field){
        Class<?> parameterizedType = field.getType();
        return parameterizedType;
    }

    default Class<?> getClassType(Field field){
        Class<?> parameterizedType = field.getType();
        return parameterizedType;
    }

}
