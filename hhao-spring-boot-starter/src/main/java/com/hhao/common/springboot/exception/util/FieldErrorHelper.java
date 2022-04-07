/*
 * Copyright 2018-2021 WangSheng.
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

package com.hhao.common.springboot.exception.util;

import com.hhao.common.jackson.JacksonUtilFactory;
import com.hhao.common.springboot.AppContext;
import org.springframework.context.NoSuchMessageException;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用于FieldError的一些处理
 *
 * @author Wang
 * @since 1.0.0
 */
public class FieldErrorHelper {

    /**
     * 获取FiledError的错误信息
     *
     * @param fieldError the field error
     * @return message
     */
    public static String getMessage(FieldError fieldError) {
        String message ="";
        if (fieldError.getCodes()!=null) {
            for (String code : fieldError.getCodes()) {
                try {
                    message = AppContext.getInstance().getMessage(code, fieldError.getArguments(), AppContext.getInstance().getLocale());
                    break;
                } catch (NoSuchMessageException e) {
                    continue;
                }
            }
        }
        if (!StringUtils.hasLength(message)){
            message = fieldError.getDefaultMessage();
        }
        return message == null ? "" : message;
    }

    /**
     * 转化成Map形式
     * key:field
     * value:message
     *
     * @param fieldErrors the field errors
     * @return field errors as map
     */
    public static Map<String, String> getFieldErrorsAsMap(List<FieldError> fieldErrors) {
        Map<String, String> fieldsErrorMap = new HashMap<String, String>(fieldErrors.size());
        fieldErrors.forEach(fieldError -> {
            String message = FieldErrorHelper.getMessage(fieldError);
            if (fieldsErrorMap.containsKey(fieldError.getField())) {
                message = fieldsErrorMap.get(fieldError.getField()) + ";" + message;
            }
            fieldsErrorMap.put(fieldError.getField(), message);
        });
        return fieldsErrorMap;
    }

    /**
     * 转化成json
     *
     * @param fieldErrors the field errors
     * @return field errors as json
     */
    public static String getFieldErrorsAsJson(List<FieldError> fieldErrors) {
        Map<String, String> fieldsErrorMap = getFieldErrorsAsMap(fieldErrors);
        try {
            return JacksonUtilFactory.getJsonUtil().map2String(fieldsErrorMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 转化成字符串
     *
     * @param fieldErrors the field errors
     * @return field errors as string
     */
    public String getFieldErrorsAsString(List<FieldError> fieldErrors) {
        Map<String, String> fieldsErrorMap = getFieldErrorsAsMap(fieldErrors);
        StringBuffer sub = new StringBuffer();
        fieldsErrorMap.forEach((fieldName, message) -> {
            sub.append(message + ";");
        });
        return sub.toString();
    }
}
