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

package com.hhao.extension.register;

import com.hhao.extension.model.ExtensionCoordinate;
import com.hhao.extension.model.ExtensionPoint;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Wang
 * @since 1.0.0
 */
public class ExtensionRepository {
    private Map<ExtensionCoordinate, ExtensionPoint> extensionRepo = new HashMap<>(32);

    public Map<ExtensionCoordinate, ExtensionPoint> getExtensionRepo() {
        return extensionRepo;
    }

    public void setExtensionRepo(Map<ExtensionCoordinate, ExtensionPoint> extensionRepo) {
        this.extensionRepo = extensionRepo;
    }


}
