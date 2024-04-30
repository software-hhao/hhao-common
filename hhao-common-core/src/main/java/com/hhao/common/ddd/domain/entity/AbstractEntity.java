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

package com.hhao.common.ddd.domain.entity;

import com.hhao.common.utils.DateTimeUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 实体
 *
 * @author Wang
 * @since 1.0.0
 */
public abstract class AbstractEntity <ID extends Serializable,UserId extends Serializable> implements Serializable {
    private static final long serialVersionUID = -4782293741120638801L;

    private ID id;
    /**
     * The Creator.
     */
    protected UserId creator;
    /**
     * The Modifier.
     */
    protected UserId modifier;
    /**
     * The Create time.
     */
    protected LocalDateTime createAt;
    /**
     * The Modifier time.
     */
    protected LocalDateTime updatedAt;

    /**
     * 无参数的构造函数，用于ORM框架（如Hibernate）实例化实体
     */
    protected AbstractEntity() {

    }

    protected AbstractEntity(ID id) {
        this.id = id;
    }

    public ID getId() {
        return id;
    }

    protected void setId(ID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractEntity<?,?> entity = (AbstractEntity<?,?>) o;
        return  Objects.equals(id, entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Entity{" +
                "id=" + id
        + '}';
    }

    public UserId getCreator() {
        return creator;
    }

    public void setCreator(UserId creator) {
        this.creator = creator;
    }

    public UserId getModifier() {
        return modifier;
    }

    public void setModifier(UserId modifier) {
        this.modifier = modifier;
    }

    public LocalDateTime getCreateAt() {
        if (this.createAt==null){
            return DateTimeUtils.nowLocalDateTimeUTC();
        }
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdatedAt() {
        if (this.updatedAt==null){
            return DateTimeUtils.nowLocalDateTimeUTC();
        }
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * 验证该实体的有效性
     *
     * @return
     */
    public abstract boolean isValid();
}
