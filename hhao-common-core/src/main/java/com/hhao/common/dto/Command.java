
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

package com.hhao.common.dto;

/**
 * Command request from Client.
 *
 * @author Wang
 * @since 2022 /2/22 21:39
 */
public abstract class Command extends Dto {
    private static final long serialVersionUID = 1L;
    /**
     * 操作人
     */
    private Long operaterId;

    /**
     * Gets operater id.
     *
     * @return the operater id
     */
    public Long getOperaterId() {
        return operaterId;
    }

    /**
     * Sets operater id.
     *
     * @param operaterId the operater id
     */
    public void setOperaterId(Long operaterId) {
        this.operaterId = operaterId;
    }
}
