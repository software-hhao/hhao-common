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

package com.hhao.common.metadata;

import com.hhao.common.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 版本信息格式如下：
 * groupId:artifactId:_majorVersion._minorVersion._patchLevel-_snapshotInfo
 *
 * @author Wang
 * @since 1.0.0
 */
public class VersionMetadata implements Metadata<Version> {
    protected final Logger logger = LoggerFactory.getLogger(VersionMetadata.class);
    private final String NAME = "VERSION";
    private String value="::0.0.0-";
    private Version version = Version.unknownVersion();

    @Override
    public boolean support(String name) {
        if (name == null) {
            return false;
        }
        if (NAME.equals(name)) {
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public Version getMetadata() {
        return version;
    }

    @Override
    public Version update(String value) {
        try {
            this.value=value;
            this.version=Version.of(value);
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
        return this.version;
    }
}

