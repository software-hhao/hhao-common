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

package com.hhao.common.springboot.exception;

import com.hhao.common.exception.DefaultErrorCodes;
import com.hhao.common.exception.error.request.RequestRuntimeException;
import com.hhao.common.springboot.AppContext;
import org.springframework.context.NoSuchMessageException;
import org.springframework.validation.BindingResult;

/**
 * 验证异常类
 *
 * @author Wang
 * @since 1.0.0
 */
public class ValidateRuntimeException extends RequestRuntimeException {
    /**
     * BindingResult
     */
    private BindingResult bindingResult;

    public ValidateRuntimeException(BindingResult bindingResult) {
        super(DefaultErrorCodes.ERROR_400);
        this.bindingResult = bindingResult;
    }

    /**
     * Instantiates a new Validate exception.
     *
     * @param bindingResult the binding result
     */
    public ValidateRuntimeException(BindingResult bindingResult, Throwable cause) {
        super(DefaultErrorCodes.ERROR_400, cause);
        this.bindingResult = bindingResult;
    }

    /**
     * Gets binding result.
     *
     * @return the binding result
     */
    public BindingResult getBindingResult() {
        return bindingResult;
    }

    /**
     * Sets binding result.
     *
     * @param bindingResult the binding result
     */
    public void setBindingResult(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }


    /**
     * Gets target.
     *
     * @return the target
     */
    public Object getTarget() {
        return this.bindingResult.getTarget();
    }

    /**
     * Gets target class name.
     *
     * @return the target class name
     */
    public String getTargetClassName() {
        return this.getTarget() == null ? "" : this.getTarget().getClass().getName();
    }

    /**
     * 从资源文件中获取key为errorCode，参数为arguments的信息
     **/
    private String getResourceMessage(String key, Object[] arguments) {
        if (key == null) {
            return null;
        }
        try {
            return AppContext.getInstance().getMessage(key, arguments, AppContext.getInstance().getLocale());
        } catch (NoSuchMessageException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getMessage() {
        return this.getErrorCode()
                .applyArgs(new Object[]{bindingResult.getErrorCount()})
                .getLocalMessage(AppContext.getInstance(), AppContext.getInstance().getLocale());
    }
}
