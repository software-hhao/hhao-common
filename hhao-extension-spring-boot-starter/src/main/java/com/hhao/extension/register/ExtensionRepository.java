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
import org.springframework.core.OrderComparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Wang
 * @since 1.0.0
 */
public class ExtensionRepository {
    private Map<ExtensionCoordinate, List<ExtensionPoint>> extensionRepo = new HashMap<>(32);

    public Map<ExtensionCoordinate, List<ExtensionPoint>> getExtensionRepo() {
        return extensionRepo;
    }

    public void setExtensionRepo(Map<ExtensionCoordinate, List<ExtensionPoint>> extensionRepo) {
        this.extensionRepo = extensionRepo;
    }

    public synchronized void putExtensionPoint(ExtensionCoordinate extensionCoordinate ,ExtensionPoint extensionPoint){
        List<ExtensionPoint> extensionPoints=extensionRepo.get(extensionCoordinate);
        if (extensionPoints==null){
            extensionPoints=new ArrayList<>();
            extensionRepo.put(extensionCoordinate,extensionPoints);
        }
        if (isDuplicate(extensionPoints,extensionPoint)){
            throw new IllegalArgumentException("ExtensionPoints is repeated " + extensionPoint.getClass());
        }
        extensionPoints.add(extensionPoint);
        //排序
        OrderComparator.sort(extensionPoints);
    }

    public List<ExtensionPoint> getExtensionPoints(ExtensionCoordinate extensionCoordinate){
        List<ExtensionPoint> extensionPoints=extensionRepo.get(extensionCoordinate);
        if (extensionPoints==null){
            throw new IllegalArgumentException("ExtensionPoints can not be found with " + extensionCoordinate);
        }
        return extensionPoints;
    }

    /**
     * 查重，同一个key下，只能有一个类型的bean
     *
     * @param extensionPoints
     * @param extensionPoint
     * @return
     */
    private boolean isDuplicate(List<ExtensionPoint> extensionPoints,ExtensionPoint extensionPoint){
        for(ExtensionPoint ext:extensionPoints){
            if (ext.getClass().equals(extensionPoint.getClass())){
                return true;
            }
        }
        return false;
    }
}
