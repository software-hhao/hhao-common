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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * The type Result wrapper properties.
 *
 * @author Wang
 * @since 1.0.0
 */
public class ResultWrapperProperties {
    /**
     * The Status succeed.
     */
    @Value("${com.hhao.config.result.status.succeed:200}")
    public   String statusSucceed = "200";

    /**
     * The Status error.
     */
    @Value("${com.hhao.config.result.status.error:500}")
    public   String statusError = "500";

    /**
     * The Msg succeed.
     */
    @Value("${com.hhao.config.result.msg.succeed:succeed}")
    public   String msgSucceed ="succeed";

    /**
     * The Msg error.
     */
    @Value("${com.hhao.config.result.msg.error:error}")
    public   String msgError ="error";

    /**
     * Gets status succeed.
     *
     * @return the status succeed
     */
    public String getStatusSucceed() {
        return statusSucceed;
    }

    /**
     * Sets status succeed.
     *
     * @param statusSucceed the status succeed
     */
    public void setStatusSucceed(String statusSucceed) {
        this.statusSucceed = statusSucceed;
    }

    /**
     * Gets status error.
     *
     * @return the status error
     */
    public String getStatusError() {
        return statusError;
    }

    /**
     * Sets status error.
     *
     * @param statusError the status error
     */
    public void setStatusError(String statusError) {
        this.statusError = statusError;
    }

    /**
     * Gets msg succeed.
     *
     * @return the msg succeed
     */
    public String getMsgSucceed() {
        return msgSucceed;
    }


    /**
     * Sets msg succeed.
     *
     * @param msgSucceed the msg succeed
     */
    public void setMsgSucceed(String msgSucceed) {
        this.msgSucceed = msgSucceed;
    }

    /**
     * Gets msg error.
     *
     * @return the msg error
     */
    public String getMsgError() {
        return msgError;
    }

    /**
     * Sets msg error.
     *
     * @param msgError the msg error
     */
    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }
}
