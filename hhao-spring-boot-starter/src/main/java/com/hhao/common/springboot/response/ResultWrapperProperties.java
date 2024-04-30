package com.hhao.common.springboot.response;

import com.hhao.common.CoreConstant;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "com.hhao.config.global-return.result-wrapper")
public class ResultWrapperProperties {
    private ResultStatus status;
    private ResultMessage msg;

    // Getter and Setter methods
    public ResultStatus getStatus() {
        return status;
    }

    public void setStatus(ResultStatus status) {
        this.status = status;
    }

    public ResultMessage getMsg() {
        return msg;
    }

    public void setMsg(ResultMessage msg) {
        this.msg = msg;
    }

    // Nested classes to match the YAML structure
    public static class ResultStatus {
        private int error= CoreConstant.DEFAULT_EXCEPTION_STATUS;
        private int succeed=CoreConstant.DEFAULT_SUCCEED_STATUS;

        // Getters and Setters
        public int getError() {
            return error;
        }

        public void setError(int error) {
            this.error = error;
        }

        public int getSucceed() {
            return succeed;
        }

        public void setSucceed(int succeed) {
            this.succeed = succeed;
        }
    }

    public static class ResultMessage {
        private String succeed=CoreConstant.DEFAULT_SUCCEED_MESSAGE;
        private String error=CoreConstant.DEFAULT_EXCEPTION_MESSAGE;

        // Getters and Setters
        public String getSucceed() {
            return succeed;
        }

        public void setSucceed(String succeed) {
            this.succeed = succeed;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }
}
