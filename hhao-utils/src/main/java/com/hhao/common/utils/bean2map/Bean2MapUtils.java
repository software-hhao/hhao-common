/*
 * Copyright 2008-2024 wangsheng
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hhao.common.utils.bean2map;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bean与Map互转
 * 类型转换器视情况需要补充
 *
 * @author Wang
 * @since 1.0.0
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class Bean2MapUtils {
    /**
     * The constant initConverts.
     */
    public static List<Convert> initConverts=new ArrayList<>();
    static {
        initConverts.add(new String2NumberConvert());
    }

    /**
     * Find convert convert.
     *
     * @param converts the converts
     * @param source   the source
     * @param target   the target
     * @return the convert
     */
    public static Convert findConvert(List<Convert> converts,Class source,Class target){
        for(Convert convert:converts){
            if (convert.support(source,target)){
                return convert;
            }
        }
        return null;
    }

    /**
     * Map 2 bean t.
     *
     * @param <T>         the type parameter
     * @param targetClass the target class
     * @param map         the map
     * @return the t
     */
//    public static <T> T map2Bean(Class<T> targetClass, Map<String,Object> map) {
//        return map2Bean(targetClass,map,initConverts);
//    }

    /**
     * map转bean
     *
     * @return 实体类 t
     */
    @SuppressWarnings("unchecked")
//    public static <T> T map2Bean(Class<T> targetClass, Map<String,Object> map,List<Convert> converts) {
//        try {
//            if (converts==null){
//                converts=initConverts;
//            }
//            Object object=targetClass.getDeclaredConstructor().newInstance();
//            BeanInfo beanInfo= Introspector.getBeanInfo(targetClass);
//            PropertyDescriptor[] propertyDescriptors=beanInfo.getPropertyDescriptors();
//            if (propertyDescriptors!=null && propertyDescriptors.length>0) {
//                String propertyName;
//                Object propertyValue;
//                for (PropertyDescriptor pd:propertyDescriptors) {
//                    propertyName=pd.getName();
//                    //检查map中是否包含这个key
//                    if (map.containsKey(propertyName)) {
//                        propertyValue=map.get(propertyName);
//                        if (propertyValue!=null&&!"null".equals(propertyValue)&&!"NULL".equals(propertyValue)) {
//                            //相当于setXxx()set方法
//                            try {
//                                if (!pd.getPropertyType().equals(propertyValue.getClass())){
//                                    Convert convert=findConvert(converts,propertyValue.getClass(),pd.getPropertyType());
//                                    if (convert==null){
//                                        throw new IllegalArgumentException();
//                                    }
//                                    propertyValue=convert.convert(propertyValue,pd.getPropertyType());
//                                }
//                                pd.getWriteMethod().invoke(object, new Object[] {propertyValue});
//                            } catch (IllegalAccessException e) {
//                                e.printStackTrace();
//                            } catch (IllegalArgumentException e) {
//                                e.printStackTrace();
//                            } catch (InvocationTargetException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }
//                return (T) object;
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }

    /**
     * 把bean 转成map
     *
     * @param bean 被转成map的bean
     * @return map
     * @throws IntrospectionException    the introspection exception
     * @throws IllegalAccessException    the illegal access exception
     * @throws IllegalArgumentException  the illegal argument exception
     * @throws InvocationTargetException the invocation target exception
     */
    public static Map<String,Object> bean2Map(Object bean) throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class<?extends Object> type=bean.getClass();
        Map<String,Object> returnMap=new HashMap<String, Object>();
        BeanInfo beanInfo=Introspector.getBeanInfo(type);
        PropertyDescriptor[] propertyDescriptors=beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor propertyDescriptor=propertyDescriptors[i];
            String propertyName=propertyDescriptor.getName();
            if (!"class".equals(propertyName)) {
                Method getMethod=propertyDescriptor.getReadMethod();
                Object result=getMethod.invoke(bean, new Object[0]);
                if (result!=null) {
                    returnMap.put(propertyName, result);
                }else {
                    returnMap.put(propertyName, null);
                }
            }
        }
        return returnMap;
    }
}
