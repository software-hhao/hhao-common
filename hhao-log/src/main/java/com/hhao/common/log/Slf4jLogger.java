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

package com.hhao.common.log;

/**
 * @author Wang
 * @since 1.0.0
 */
public class Slf4jLogger implements Logger{
    private org.slf4j.Logger logger;

    public Slf4jLogger(org.slf4j.Logger logger){
        this.logger=logger;
    }

    @Override
    public String getName() {
        return logger.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public void trace(String msg) {
        logger.trace(msg);
    }

    @Override
    public void trace(String format, Object arg) {
        logger.trace(format,arg);
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        logger.trace(format,arg1,arg2);
    }

    @Override
    public void trace(String format, Object... arguments) {
        logger.trace(format,arguments);
    }

    @Override
    public void trace(String msg, Throwable t) {
        logger.trace(msg,t);
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public void debug(String msg) {
        logger.debug(msg);
    }

    @Override
    public void debug(String format, Object arg) {
        logger.debug(format,arg);
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        logger.debug(format,arg1,arg2);
    }

    @Override
    public void debug(String format, Object... arguments) {
        logger.debug(format,arguments);
    }

    @Override
    public void debug(String msg, Throwable t) {
        logger.debug(msg,t);
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public void info(String msg) {
        logger.info(msg);
    }

    @Override
    public void info(String format, Object arg) {
        logger.info(format,arg);
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        logger.info(format,arg1,arg2);
    }

    @Override
    public void info(String format, Object... arguments) {
        logger.info(format,arguments);
    }

    @Override
    public void info(String msg, Throwable t) {
        logger.info(msg,t);
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    @Override
    public void warn(String msg) {
        logger.warn(msg);
    }

    @Override
    public void warn(String format, Object arg) {
        logger.warn(format,arg);
    }

    @Override
    public void warn(String format, Object... arguments) {
        logger.warn(format,arguments);
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        logger.warn(format,arg1,arg2);
    }

    @Override
    public void warn(String msg, Throwable t) {
        logger.warn(msg,t);
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    @Override
    public void error(String msg) {
        logger.error(msg);
    }

    @Override
    public void error(String format, Object arg) {
        logger.error(format,arg);
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        logger.error(format,arg1,arg2);
    }

    @Override
    public void error(String format, Object... arguments) {
        logger.error(format,arguments);
    }

    @Override
    public void error(String msg, Throwable t) {
        logger.error(msg,t);
    }
}
