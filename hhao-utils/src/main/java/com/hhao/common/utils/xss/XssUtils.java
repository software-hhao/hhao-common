/*
 * Copyright 2018-2021 WangSheng.
 *
 * Licensed under the GNU GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hhao.common.utils.xss;

import org.owasp.validator.html.*;

import java.util.function.Consumer;

/**
 * Xss处理工具类
 *
 * @author Wang
 * @since 1.0.0
 */
public class XssUtils {
    /**
     * The constant POLICY_FILE_LOCATION.
     */
    final static String POLICY_FILE_LOCATION="antisamy.xml";
    /**
     * The Default policy.
     */
    static Policy defaultPolicy;
    /**
     * The Anti samy.
     */
    static AntiSamy antiSamy;

    static{
        try {
            defaultPolicy=Policy.getInstance(XssUtils.class.getResourceAsStream(POLICY_FILE_LOCATION));
            antiSamy = new AntiSamy(defaultPolicy);
        } catch (PolicyException e) {
            e.printStackTrace();
        }
    }

    /**
     * Clean html string.
     *
     * @param html the html
     * @return the string
     */
    public static String cleanHtml(String html){
        return cleanHtml(html,(str->{}));
    }

    /**
     * Clean html string.
     *
     * @param html        the html
     * @param msgConsumer the msg consumer
     * @return the string
     */
    public static String cleanHtml(String html, Consumer<String> msgConsumer){
        try {
            CleanResults cr=antiSamy.scan(html);
            if (msgConsumer!=null) {
                cr.getErrorMessages().stream().forEach(str -> {
                    msgConsumer.accept(str);
                });
            }
            return cr.getCleanHTML();
        } catch (PolicyException e) {
            e.printStackTrace();
        } catch (ScanException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Clean html string.
     *
     * @param html   the html
     * @param policy the policy
     * @return the string
     */
    public static String cleanHtml(String html, Policy policy){
        return cleanHtml(html,policy,null);
    }

    /**
     * Clean html string.
     *
     * @param html        the html
     * @param policy      the policy
     * @param msgConsumer the msg consumer
     * @return the string
     */
    public static String cleanHtml(String html, Policy policy, Consumer<String> msgConsumer){
        try {
            CleanResults cr=antiSamy.scan(html,policy,AntiSamy.SAX);
            if (msgConsumer!=null) {
                cr.getErrorMessages().stream().forEach(str -> {
                    msgConsumer.accept(str);
                });
            }
            return cr.getCleanHTML();
        } catch (PolicyException e) {
            e.printStackTrace();
        } catch (ScanException e) {
            e.printStackTrace();
        }
        return "";
    }
}
