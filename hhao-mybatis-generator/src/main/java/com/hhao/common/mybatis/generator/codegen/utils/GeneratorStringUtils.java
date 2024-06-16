/*
 * Copyright (c) 2024 wangsheng
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hhao.common.mybatis.generator.codegen.utils;

/**
 * The type Hao generator string utils.
 *
 * @author wang sheng
 * @since 2024 /6/6 下午3:01
 */
public class GeneratorStringUtils {

    /**
     * 将字符串的首字母转换为小写。
     *
     * @param str 原始字符串
     * @return 首字母小写的字符串，如果原始字符串为空则返回原字符串
     */
    public static String lowercaseFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }

    /**
     * 将字符串的首字母转换为大写。
     *
     * @param str 原始字符串
     * @return 首字母大写的字符串 ，如果原始字符串为空则返回原字符串
     */
    public static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    /**
     * 将逗号之后的内容去除，用括号括起来
     *
     * @param input the input
     * @return string
     */
    public static String replaceAfterCommaWithParentheses(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        int commaIndex = input.indexOf(',');
        if (commaIndex == -1) {
            return input;
        }
        return input.substring(0, commaIndex) + ")";
    }

    /**
     * 获取字符串中最后一个'.'之后的子串。
     *
     * @param fullClassName 完整的类名字符串
     * @return 最后一个 '.'之后的子串，如果没有找到'.'则返回原字符串
     */
    public static String getLastSegmentAfterDot(String fullClassName) {
        if (fullClassName == null || fullClassName.isEmpty()) {
            return fullClassName;
        }

        int lastDotIndex = fullClassName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return fullClassName; // 如果没有找到'.'则返回整个字符串
        }

        return fullClassName.substring(lastDotIndex + 1);
    }
}
