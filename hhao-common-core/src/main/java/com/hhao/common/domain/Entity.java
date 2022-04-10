/*
 * Copyright 2020-2021 WangSheng.
 *
 * Licensed under the GNU GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hhao.common.domain;

import java.time.Instant;

/**
 * 实体
 *
 * @author Wang
 * @since 1.0.0
 */
public abstract class Entity {
    /**
     * The Id.
     */
    protected Long id;
    /**
     * The Creator.
     */
    protected Long creator;
    /**
     * The Modifier.
     */
    protected Long modifier;
    /**
     * The Create time.
     */
    protected Instant createTime;
    /**
     * The Modifier time.
     */
    protected Instant modifierTime;

    /**
     * Gets id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets creator.
     *
     * @return the creator
     */
    public Long getCreator() {
        return creator;
    }

    /**
     * Sets creator.
     *
     * @param creator the creator
     */
    public void setCreator(Long creator) {
        this.creator = creator;
    }

    /**
     * Gets modifier.
     *
     * @return the modifier
     */
    public Long getModifier() {
        return modifier;
    }

    /**
     * Sets modifier.
     *
     * @param modifier the modifier
     */
    public void setModifier(Long modifier) {
        this.modifier = modifier;
    }

    /**
     * Gets create time.
     *
     * @return the create time
     */
    public Instant getCreateTime() {
        return createTime;
    }

    /**
     * Sets create time.
     *
     * @param createTime the create time
     */
    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    /**
     * Gets modifier time.
     *
     * @return the modifier time
     */
    public Instant getModifierTime() {
        return modifierTime;
    }

    /**
     * Sets modifier time.
     *
     * @param modifierTime the modifier time
     */
    public void setModifierTime(Instant modifierTime) {
        this.modifierTime = modifierTime;
    }
}
