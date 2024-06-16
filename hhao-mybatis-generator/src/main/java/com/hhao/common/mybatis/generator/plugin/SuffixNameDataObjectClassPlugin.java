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

package com.hhao.common.mybatis.generator.plugin;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.List;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

/**
 * 生成DataObject后加后缀名的插件
 *
 * @author Wang
 * @since 2024 /1/30 16:08
 */
public class SuffixNameDataObjectClassPlugin extends PluginAdapter {
    private String suffixName="Do";

    @Override
    public boolean validate(List<String> warnings) {
        String suffixName = properties.getProperty("suffixName");
        boolean valid = StringUtility.stringHasValue(suffixName);
        if (!valid) {
            warnings.add(getString("ValidationError.18",
                    "SuffixNameDataObjectClassPlugin",
                    "suffixName"));
        }else{
            this.suffixName=suffixName;
        }
        return valid;
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        String oldType = introspectedTable.getBaseRecordType();
        if (!oldType.endsWith(suffixName)){
            oldType=oldType + suffixName;
        }
        introspectedTable.setBaseRecordType(oldType);
    }
}