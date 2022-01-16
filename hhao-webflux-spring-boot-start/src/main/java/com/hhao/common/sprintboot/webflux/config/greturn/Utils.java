
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

package com.hhao.common.sprintboot.webflux.config.greturn;/*
 * Copyright 2020-2021 WangSheng.
 *
 * Licensed under the GNU GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.hhao.common.springboot.annotations.ResponseAutoWrapper;
import com.hhao.common.springboot.response.ResultWrapper;
import com.hhao.common.springboot.response.ResultWrapperBuilder;
import com.hhao.common.springboot.response.UnResultWrapper;
import org.springframework.core.MethodParameter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Wang
 * @since 2022/1/15 14:43
 */
public class Utils {

    /**
     * 判断是否支持自动封装
     * @param returnType
     * @return
     */
    public static boolean supports(MethodParameter returnType) {
        //如果继承自UnResultWrapper,则不支持自动包装
        if (returnType.getMethod()!=null && returnType.getMethod().getReturnType()!=null && returnType.getMethod().getReturnType().isAssignableFrom(UnResultWrapper.class)){
            return false;
        }
        //判断是否有ResponseAutoWrapper的注解
        ResponseAutoWrapper responseAutoWrapper=returnType.getMethod().getAnnotation(ResponseAutoWrapper.class);
        if (responseAutoWrapper==null){
            responseAutoWrapper=returnType.getDeclaringClass().getAnnotation(ResponseAutoWrapper.class);
        }
        if (responseAutoWrapper==null || responseAutoWrapper.value()==false){
            return false;
        }
        return true;
    }

    public static Object wrapperResult(Object result){
        if (result instanceof Mono){
            //如果是Mono，将内容封装
            result=((Mono)result).map(data-> {
                if (!(data instanceof ResultWrapper)){
                    return ResultWrapperBuilder.ok(data);
                }
                return data;
            });
        }else if (result instanceof Flux){
            //如果是Flux，则取集合后封装
            result=((Flux)result).collectList().map(data->ResultWrapperBuilder.ok(data));
        }else if (!(result instanceof ResultWrapper)){
            //一般类型
            result=ResultWrapperBuilder.ok(result);
        }
        return result;
    }
}
