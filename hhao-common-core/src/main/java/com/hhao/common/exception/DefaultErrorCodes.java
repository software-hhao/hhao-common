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

package com.hhao.common.exception;


/**
 * 自定义异常信息从此类继承
 * 每个模块可定义一个自定义异常信息类
 *
 * @author Wang
 * @since 1.0.0
 */
public class DefaultErrorCodes {
    /**
     * 通用异常类型
     */
    public static final ErrorCode ERROR_400 = new ErrorCode("400","${error.code.400}");

    /**
     * The constant ERROR_401.
     */
    public static final ErrorCode ERROR_401 =new ErrorCode("401","${error.code.401}");
    public static final ErrorCode ERROR_401_1 = new ErrorCode("401","${error.code.401.1}");
    public static final ErrorCode ERROR_401_2 = new ErrorCode("401","${error.code.401.2}");
    public static final ErrorCode ERROR_401_3 = new ErrorCode("401","${error.code.401.3}");

    /**
     * The constant ERROR_403.
     */
    public static final ErrorCode ERROR_403 = new ErrorCode("403","${error.code.403}");
    /**
     * The constant ERROR_404.
     */
    public static final ErrorCode ERROR_404 = new ErrorCode("404","${error.code.404}");
    /**
     * The constant ERROR_405.
     */
    public static final ErrorCode ERROR_405 = new ErrorCode("405","${error.code.405}");
    /**
     * The constant ERROR_406.
     */
    public static final ErrorCode ERROR_406 = new ErrorCode("406","${error.code.406}");
    /**
     * The constant ERROR_409.
     */
    public static final ErrorCode ERROR_409 = new ErrorCode("409","${error.code.409}");
    /**
     * The constant ERROR_410.
     */
    public static final ErrorCode ERROR_410 = new ErrorCode("410","${error.code.410}");
    /**
     * The constant ERROR_415.
     */
    public static final ErrorCode ERROR_415 = new ErrorCode("415","${error.code.415}");
    /**
     * The constant ERROR_422.
     */
    public static final ErrorCode ERROR_422 = new ErrorCode("422","${error.code.422}");
    /**
     * The constant ERROR_429.
     */
    public static final ErrorCode ERROR_429 = new ErrorCode("429","${error.code.429}");
    /**
     * The constant ERROR_40X.
     */
    public static final ErrorCode ERROR_40X = new ErrorCode("40X","${error.code.40X}");
    /**
     * The constant ERROR_4XX.
     */
    public static final ErrorCode ERROR_4XX = new ErrorCode("4XX","${error.code.4XX}");

    /**
     * The constant ERROR_500.
     */
    public static final ErrorCode ERROR_500 = new ErrorCode("500","${error.code.500}");

    /**
     * The constant ERROR_501.
     */
    public static final ErrorCode ERROR_501 = new ErrorCode("501","${error.code.501}");
    /**
     * The constant ERROR_502.
     */
    public static final ErrorCode ERROR_502 = new ErrorCode("502","${error.code.502}");
    /**
     * The constant ERROR_503.
     */
    public static final ErrorCode ERROR_503 = new ErrorCode("503","${error.code.503}");
    /**
     * The constant ERROR_504.
     */
    public static final ErrorCode ERROR_504 = new ErrorCode("504","${error.code.504}");
    /**
     * The constant ERROR_505.
     */
    public static final ErrorCode ERROR_505 = new ErrorCode("505","${error.code.505}");
    /**
     * The constant ERROR_5XX.
     */
    public static final ErrorCode ERROR_5XX = new ErrorCode("5XX","${error.code.5XX}");

    public static final ErrorCode ERROR_997 = new ErrorCode("997","${error.code.997}");
    /**
     * The constant ERROR_998.
     */
    public static final ErrorCode ERROR_998 = new ErrorCode("998","${error.code.998}");
    /**
     * The constant ERROR_999.
     */
    public static final ErrorCode ERROR_999 = new ErrorCode("999","${error.code.999}");

    /**
     * The constant ERROR_400_DATETIME.
     */
    public static final ErrorCode ERROR_400_DATA_CONVERT = new ErrorCode("400.1","${error.code.400.1}");
    /**
     * The constant ERROR_400_PAYLOAD_LENGTH.
     */
    public static final ErrorCode ERROR_400_PAYLOAD_LENGTH = new ErrorCode("400.2","${error.code.400.2}");
    /**
     * The constant ERROR_400_DECODE.
     */
    public static final ErrorCode ERROR_400_DECODE = new ErrorCode("400.3","${error.code.400.3}");

    public static final ErrorCode ERROR_400_MONEY =new ErrorCode("400.4","${error.code.400.4}");

    /**
     * The constant ERROR_500_IO.
     */
    public static final ErrorCode ERROR_500_IO =new ErrorCode("500.1","${error.code.500.1}");
    public static final ErrorCode ERROR_500_CONNECT =new ErrorCode("500.2","${error.code.500.2}");
}
