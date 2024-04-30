
/*
 * Copyright 2018-2022 WangSheng.
 *
 * Licensed under the GNU GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hhao.common.springboot.response;

import com.hhao.common.jackson.JacksonUtilFactory;
import com.hhao.common.jackson.view.Views;
import com.hhao.common.log.Logger;
import com.hhao.common.log.LoggerFactory;
import com.hhao.common.springboot.jackson.SpringJacksonKeyType;

import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * 工具类，将json字符串转换成ResultWrapper
 *
 * @author Wang
 * @since 1.0.0
 */
public class ResultWrapperUtil {
    /**
     * The constant logger.
     */
    protected static final Logger logger = LoggerFactory.getLogger(ResultWrapperUtil.class);

    /**
     * Json to result wrapper result wrapper.
     *
     * @param <T>    the type parameter
     * @param target the target
     * @param json   the json
     * @return the result wrapper
     */
    public static <T> ResultWrapper<T> jsonToResultWrapper(Class<T> target,String json){
        //对结果进行转换
        return (ResultWrapper) JacksonUtilFactory.getJsonUtil(SpringJacksonKeyType.SPRING_RETURN).string2Pojo(json,ResultWrapper.class,target, Views.Default.class);
    }

    /**
     * Json to result wrapper result wrapper.
     *
     * @param targetType the target type
     * @param json       the json
     * @return the result wrapper
     */
    public static ResultWrapper jsonToResultWrapper(Type targetType, String json){
        Class retyped= HashMap.class;
        try {
            retyped=Class.forName(targetType.getTypeName());
        } catch (ClassNotFoundException e) {
            logger.debug(e.getMessage());
        }
        return jsonToResultWrapper(retyped,json);
    }
}
