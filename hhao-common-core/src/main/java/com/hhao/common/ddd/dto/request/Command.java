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

package com.hhao.common.ddd.dto.request;

import com.hhao.common.ddd.dto.Dto;

import java.io.Serializable;

/**
 * Command request from Client.
 *
 * @author Wang
 * @since 2022 /2/22 21:39
 */
public abstract class Command<UserId extends Serializable> extends Dto {
    private static final long serialVersionUID = -1431554215120006400L;
    /**
     * 操作人
     */
    private UserId operatorId;

    /**
     * Gets operater id.
     *
     * @return the operater id
     */
    public UserId getOperatorId() {
        return operatorId;
    }

    /**
     * Sets operater id.
     *
     * @param operatorId the operater id
     */
    public void setOperatorId(UserId operatorId) {
        this.operatorId = operatorId;
    }
}
