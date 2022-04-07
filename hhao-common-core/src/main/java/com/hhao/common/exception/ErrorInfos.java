
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

package com.hhao.common.exception;
/**
 * @author Wang
 * @since 2022/3/10 22:35
 */

/**
 * 自定义异常信息从此类继承
 * 每个模块可定义一个自定义异常信息类
 *
 * @author Wang
 * @since 1.0.0
 */
public class ErrorInfos {
    /**
     * 通用异常类型
     */
    public static final ErrorInfo ERROR_400 = ErrorInfoBuilder.build("400", "error.code.400");
    /**
     * The constant ERROR_401.
     */
    public static final ErrorInfo ERROR_401 = ErrorInfoBuilder.build("401", "error.code.401");
    /**
     * The constant ERROR_403.
     */
    public static final ErrorInfo ERROR_403 = ErrorInfoBuilder.build("403", "error.code.403");
    /**
     * The constant ERROR_404.
     */
    public static final ErrorInfo ERROR_404 = ErrorInfoBuilder.build("404", "error.code.404");
    /**
     * The constant ERROR_405.
     */
    public static final ErrorInfo ERROR_405 = ErrorInfoBuilder.build("405", "error.code.405");
    /**
     * The constant ERROR_406.
     */
    public static final ErrorInfo ERROR_406 = ErrorInfoBuilder.build("406", "error.code.406");
    /**
     * The constant ERROR_409.
     */
    public static final ErrorInfo ERROR_409 = ErrorInfoBuilder.build("409", "error.code.409");
    /**
     * The constant ERROR_410.
     */
    public static final ErrorInfo ERROR_410 = ErrorInfoBuilder.build("410", "error.code.410");
    /**
     * The constant ERROR_415.
     */
    public static final ErrorInfo ERROR_415 = ErrorInfoBuilder.build("415", "error.code.415");
    /**
     * The constant ERROR_422.
     */
    public static final ErrorInfo ERROR_422 = ErrorInfoBuilder.build("422", "error.code.422");
    /**
     * The constant ERROR_429.
     */
    public static final ErrorInfo ERROR_429 = ErrorInfoBuilder.build("429", "error.code.429");
    /**
     * The constant ERROR_40X.
     */
    public static final ErrorInfo ERROR_40X = ErrorInfoBuilder.build("40X", "error.code.40X");
    /**
     * The constant ERROR_4XX.
     */
    public static final ErrorInfo ERROR_4XX = ErrorInfoBuilder.build("4XX", "error.code.4XX");

    /**
     * The constant ERROR_500.
     */
    public static final ErrorInfo ERROR_500 = ErrorInfoBuilder.build("500", "error.code.500");
    /**
     * The constant ERROR_501.
     */
    public static final ErrorInfo ERROR_501 = ErrorInfoBuilder.build("501", "error.code.501");
    /**
     * The constant ERROR_502.
     */
    public static final ErrorInfo ERROR_502 = ErrorInfoBuilder.build("502", "error.code.502");
    /**
     * The constant ERROR_503.
     */
    public static final ErrorInfo ERROR_503 = ErrorInfoBuilder.build("503", "error.code.503");
    /**
     * The constant ERROR_504.
     */
    public static final ErrorInfo ERROR_504 = ErrorInfoBuilder.build("504", "error.code.504");
    /**
     * The constant ERROR_505.
     */
    public static final ErrorInfo ERROR_505 = ErrorInfoBuilder.build("505", "error.code.505");
    /**
     * The constant ERROR_5XX.
     */
    public static final ErrorInfo ERROR_5XX = ErrorInfoBuilder.build("5XX", "error.code.5XX");

    /**
     * The constant ERROR_998.
     */
    public static final ErrorInfo ERROR_998 = ErrorInfoBuilder.build("998", "error.code.998");
    /**
     * The constant ERROR_999.
     */
    public static final ErrorInfo ERROR_999 = ErrorInfoBuilder.build("999", "error.code.999");

    /**
     * The constant ERROR_400_DATETIME.
     */
    public static final ErrorInfo ERROR_400_DATETIME = ErrorInfoBuilder.build("400.1", "error.code.400.1");
    /**
     * The constant ERROR_400_PAYLOAD_LENGTH.
     */
    public static final ErrorInfo ERROR_400_PAYLOAD_LENGTH = ErrorInfoBuilder.build("400.2", "error.code.400.2");
    /**
     * The constant ERROR_400_DECODE.
     */
    public static final ErrorInfo ERROR_400_DECODE = ErrorInfoBuilder.build("400.3", "error.code.400.3");

    public static final ErrorInfo ERROR_400_MONEY = ErrorInfoBuilder.build("400.4", "error.code.400.4");

    /**
     * The constant ERROR_500_IO.
     */
    public static final ErrorInfo ERROR_500_IO = ErrorInfoBuilder.build("500.1", "error.code.500.1");
}
