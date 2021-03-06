package com.suke.zhjg.common.autofull.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suke.zhjg.common.autofull.config.ApplicationContextRegister;
import com.suke.zhjg.common.autofull.config.AutoConfig;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * @author czx
 * @title: AutoFullHandler
 * @projectName zhjg
 * @description: TODO 自动填充属性
 * @date 2020/8/2014:21
 */
@Slf4j
@UtilityClass
public class AutoFullHandler {

    private Handler handler;

    public <T> IPage<T> full(IPage<T> iPage){
        if(CollUtil.isNotEmpty(iPage.getRecords())){
            iPage.getRecords().forEach(obj-> BeanUtil.copyProperties( obj,handler(obj,null,1)));
        }
        return iPage;
    }

    public <T> IPage<T> full(IPage<T> iPage,String sequence){
        if(CollUtil.isNotEmpty(iPage.getRecords())){
            iPage.getRecords().forEach(obj-> BeanUtil.copyProperties( obj,handler(obj,sequence,1)));
        }
        return iPage;
    }

    public <T> IPage<T> full(IPage<T> iPage,String sequence,int level){
        if(CollUtil.isNotEmpty(iPage.getRecords())){
            iPage.getRecords().forEach(obj-> BeanUtil.copyProperties( obj,handler(obj,sequence,level)));
        }
        return iPage;
    }

    public <T> List<T> full(List<T> list){
        if(CollUtil.isNotEmpty(list)){
            list.forEach(obj-> BeanUtil.copyProperties( obj,handler(obj,null,1)));
        }
        return list;
    }

    public <T> List<T> full(List<T> list,String sequence){
        if(CollUtil.isNotEmpty(list)){
            list.forEach(obj-> BeanUtil.copyProperties( obj,handler(obj,sequence,1)));
        }
        return list;
    }

    public <T> List<T> full(List<T> list,String sequence,int level){
        if(CollUtil.isNotEmpty(list)){
            list.forEach(obj-> BeanUtil.copyProperties( obj,handler(obj,sequence,level)));
        }
        return list;
    }

    public <T> T full(T entity){
        if(ObjectUtil.isNotNull(entity)){
            BeanUtil.copyProperties( entity,handler(entity,null,1));
        }
        return entity;
    }

    public <T> T full(T entity,String sequence){
        if(ObjectUtil.isNotNull(entity)){
            BeanUtil.copyProperties( entity,handler(entity,sequence,1));
        }
        return entity;
    }

    public <T> T full(T entity,String sequence,int level){
        if(ObjectUtil.isNotNull(entity)){
            BeanUtil.copyProperties( entity,handler(entity,sequence,level));
        }
        return entity;
    }


    protected Object handler(Object obj,String sequence,int level){
        Field[] fields = obj.getClass().getDeclaredFields();
        if(fields != null) {
            for (Field field : fields) {
                Annotation[] annotations = field.getDeclaredAnnotations();
                for(Annotation annotation : annotations){
                    AutoConfig autoConfig = ApplicationContextRegister.getApplicationContext().getBean(AutoConfig.class);
                    if(ObjectUtil.isNotNull(autoConfig)){
                        handler = (Handler) autoConfig.findBean(annotation);
                    }
                    if(handler != null){
                        handler.result(annotation,fields,field,obj,sequence,level);
                    }
                }
            }
        }
       return obj;
    }
}
