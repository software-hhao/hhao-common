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

package com.hhao.common.springboot.response;

/**
 * The type Result wrapper builder.
 *
 * @author Wang
 * @since 1.0.0
 */
public class ResultWrapperBuilder {
    private static ResultWrapperProperties resultWrapperProperties;

    /**
     * Instantiates a new Result wrapper builder.
     *
     * @param properties the properties
     */
    public ResultWrapperBuilder(ResultWrapperProperties properties){
        resultWrapperProperties=properties;
    }

    /**
     * Ok result wrapper.
     *
     * @param <T>     the type parameter
     * @param data    the data
     * @param status  the status
     * @param message the message
     * @return the result wrapper
     */
    public static <T> ResultWrapper<T> ok(T data, String status, String message) {
        if (data instanceof ResultWrapper) {
            return (ResultWrapper) data;
        }
        return new ResultWrapper<T>(data,status,message);
    }

    /**
     * Ok result wrapper.
     *
     * @param <T>  the type parameter
     * @param data the data
     * @return the result wrapper
     */
    public static <T> ResultWrapper<T> ok(T data) {
        return ok(data, resultWrapperProperties.statusSucceed,resultWrapperProperties.msgSucceed);
    }

    /**
     * Error result wrapper.
     *
     * @param <T>     the type parameter
     * @param data    the data
     * @param status  the status
     * @param message the message
     * @return the result wrapper
     */
    public static <T> ResultWrapper<T> error(T data, String status, String message) {
        if (data instanceof ResultWrapper) {
            return (ResultWrapper) data;
        }
        return new ResultWrapper<T>(data,status,message);
    }

    /**
     * Error result wrapper.
     *
     * @param <T>  the type parameter
     * @param data the data
     * @return the result wrapper
     */
    public static <T> ResultWrapper<T> error(T data) {
        return error(data,resultWrapperProperties.statusError,resultWrapperProperties.msgError);
    }

    /**
     * Error result wrapper.
     *
     * @param <T>       the type parameter
     * @param data      the data
     * @param status    the status
     * @param throwable the throwable
     * @return the result wrapper
     */
    public static <T> ResultWrapper<T> error(T data,String status,Throwable throwable) {
        return error(data,status,throwable.getMessage());
    }

    /**
     * Error result wrapper.
     *
     * @param <T>       the type parameter
     * @param data      the data
     * @param throwable the throwable
     * @return the result wrapper
     */
    public static <T> ResultWrapper<T> error(T data,Throwable throwable) {
        return error(data,resultWrapperProperties.statusError,throwable.getMessage());
    }
}
