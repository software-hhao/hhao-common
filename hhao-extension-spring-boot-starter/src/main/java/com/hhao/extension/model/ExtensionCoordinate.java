/*
 * Copyright 2018-2022 WangSheng.
 *
 * Licensed under the GNU GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        https://www.gnu.org/licenses/gpl-3.0.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.hhao.extension.model;

import com.hhao.common.extension.BizScenario;

/**
 * 扩展点坐标
 * 扩展点接口的类名+bizScenarioUniqueIdentity来识别
 *
 * @author Wang
 * @since 1.0.0
 */
public class ExtensionCoordinate {
    private String extensionPointName;
    private String bizScenarioUniqueIdentity;
    private Class extensionPointClass;
    private BizScenario bizScenario;

    /**
     * Gets extension point class.
     *
     * @return the extension point class
     */
    public Class getExtensionPointClass() {
        return extensionPointClass;
    }

    /**
     * Gets biz scenario.
     *
     * @return the biz scenario
     */
    public BizScenario getBizScenario() {
        return bizScenario;
    }

    /**
     * Value of extension coordinate.
     *
     * @param extPtClass  the ext pt class
     * @param bizScenario the biz scenario
     * @return the extension coordinate
     */
    public static ExtensionCoordinate valueOf(Class extPtClass, BizScenario bizScenario) {
        return new ExtensionCoordinate(extPtClass, bizScenario);
    }

    /**
     * Instantiates a new Extension coordinate.
     *
     * @param extPtClass  the ext pt class
     * @param bizScenario the biz scenario
     */
    public ExtensionCoordinate(Class extPtClass, BizScenario bizScenario) {
        this.extensionPointClass = extPtClass;
        this.extensionPointName = extPtClass.getName();
        this.bizScenario = bizScenario;
        this.bizScenarioUniqueIdentity = bizScenario.getUniqueIdentity();
    }

    /**
     * Instantiates a new Extension coordinate.
     *
     * @param extensionPoint the extension point
     * @param bizScenario    the biz scenario
     */
    public ExtensionCoordinate(String extensionPoint, String bizScenario) {
        this.extensionPointName = extensionPoint;
        this.bizScenarioUniqueIdentity = bizScenario;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bizScenarioUniqueIdentity == null) ? 0 : bizScenarioUniqueIdentity.hashCode());
        result = prime * result + ((extensionPointName == null) ? 0 : extensionPointName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ExtensionCoordinate other = (ExtensionCoordinate) obj;
        if (bizScenarioUniqueIdentity == null) {
            if (other.bizScenarioUniqueIdentity != null) {
                return false;
            }
        } else if (!bizScenarioUniqueIdentity.equals(other.bizScenarioUniqueIdentity)) {
            return false;
        }
        if (extensionPointName == null) {
            if (other.extensionPointName != null) {
                return false;
            }
        } else if (!extensionPointName.equals(other.extensionPointName)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ExtensionCoordinate [extensionPointName=" + extensionPointName + ", bizScenarioUniqueIdentity=" + bizScenarioUniqueIdentity + "]";
    }
}
