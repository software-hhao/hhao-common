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

import java.util.HashMap;
import java.util.Map;

/**
 * This is the object communicate with Client. The clients could be view layer or other RPC Consumers
 *
 * @author Wang
 * @since 1.0.0
 */
public abstract class ClientObject extends Dto {
    private static final long serialVersionUID = -3806763817323624297L;

    /**
     * 对象创建的时间戳。
     */
    private long timestamp = System.currentTimeMillis();

    /**
     * 用于存放附加信息的扩展字段，可以存储自定义的属性或元数据。
     */
    private Map<String, Object> attachments;

    /**
     * Gets attachments.
     *
     * @return the attachments
     */
    public Map<String, Object> getAttachments() {
        return attachments;
    }

    /**
     * Sets attachments.
     *
     * @param attachments the attachments
     */
    public void setAttachments(Map<String, Object> attachments) {
        this.attachments = attachments;
    }

    /**
     * Add attachment.
     *
     * @param key   the key
     * @param value the value
     */
    public void addAttachment(String key, Object value) {
        if (this.attachments == null) {
            synchronized (this) {
                if (this.attachments == null) {
                    this.attachments = new HashMap<>();
                }
            }
        }
        this.attachments.put(key, value);
    }

    /**
     * Gets attachment.
     *
     * @param key the key
     * @return the attachment
     */
    public Object getAttachment(String key) {
        return this.attachments != null ? this.attachments.get(key) : null;
    }

    /**
     * Gets timestamp.
     *
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Sets timestamp.
     *
     * @param timestamp the timestamp
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Remove attachment object.
     *
     * @param key the key
     * @return the object
     */
    public Object removeAttachment(String key) {
        return this.attachments != null ? this.attachments.remove(key) : null;
    }


    @Override
    public String toString() {
        return "DubboRemoteObject{" +
                "timestamp=" + timestamp +
                ", version='" + super.getVersion() + '\'' +
                ", attachments=" + attachments +
                '}';
    }
}
