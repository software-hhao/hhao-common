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

package com.hhao.common.springboot.swagger;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The type Swagger properties.
 *
 * @author Wang
 * @since 1.0.0
 */
@ConfigurationProperties("com.hhao.config.swagger.swagger-groups")
public class SwaggerProperties {
    /**
     * 组名称
     */
    private String groupName="default";

    /**
     * 联系人姓名
     */
    private  String contactName="";

    /**
     * 个人网站链接
     */
    private  String contactUrl="";

    /**
     * 个人邮箱
     */
    private  String contactEmail="";

    /**
     * api版本
     */
    private  String apiVersion="1.0";

    /**
     * api标题
     */
    private  String apiTitle="Api Documentation";

    /**
     * api描述
     */
    private  String apiDescriptionUrl="classpath:config/swagger-description";

    /**
     * 组织链接
     */
    private  String apiTermsOfServiceUrl="";

    /**
     * 许可
     */
    private  String apiLicense="";

    /**
     * 许可链接
     */
    private  String apiLicenseUrl="";

    /**
     * requestHandler选择器
     * 取值：any|none|basePackage:包路径
     * 示例：
     * any
     * none
     * basePackage:com.hhao.common.springboot.web.mvc.test.api
     */
    private String requestHandlerSelectors="";

    /**
     * 路径选择器
     * 取值：none|any|regex:路径|ant:路径
     */
    private String pathSelectors="";

    /**
     * servlet路径
     */
    private String pathMapping="/";

    /**
     * 是否使用默认的ResponseMessages
     */
    private Boolean useDefaultResponseMessages=true;

    /**
     * 如果启用，则有如下功能
     * <pre>{@code
     * http://example.org/findCustomersBy?name=Test解析为http://example.org/findCustomersBy{?name}
     * http://example.org/findCustomersBy?zip=76051解析为http://example.org/findCustomersBy{?zip}
     * }
     * </pre>
     */

    private Boolean enableUrlTemplating=true;

    /**
     * Gets group name.
     *
     * @return the group name
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * Sets group name.
     *
     * @param groupName the group name
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * Gets contact name.
     *
     * @return the contact name
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * Sets contact name.
     *
     * @param contactName the contact name
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * Gets contact url.
     *
     * @return the contact url
     */
    public String getContactUrl() {
        return contactUrl;
    }

    /**
     * Sets contact url.
     *
     * @param contactUrl the contact url
     */
    public void setContactUrl(String contactUrl) {
        this.contactUrl = contactUrl;
    }

    /**
     * Gets contact email.
     *
     * @return the contact email
     */
    public String getContactEmail() {
        return contactEmail;
    }

    /**
     * Sets contact email.
     *
     * @param contactEmail the contact email
     */
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    /**
     * Gets api version.
     *
     * @return the api version
     */
    public String getApiVersion() {
        return apiVersion;
    }

    /**
     * Sets api version.
     *
     * @param apiVersion the api version
     */
    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    /**
     * Gets api title.
     *
     * @return the api title
     */
    public String getApiTitle() {
        return apiTitle;
    }

    /**
     * Sets api title.
     *
     * @param apiTitle the api title
     */
    public void setApiTitle(String apiTitle) {
        this.apiTitle = apiTitle;
    }

    /**
     * Gets api description url.
     *
     * @return the api description url
     */
    public String getApiDescriptionUrl() {
        return apiDescriptionUrl;
    }

    /**
     * Sets api description url.
     *
     * @param apiDescriptionUrl the api description url
     */
    public void setApiDescriptionUrl(String apiDescriptionUrl) {
        this.apiDescriptionUrl = apiDescriptionUrl;
    }

    /**
     * Gets api terms of service url.
     *
     * @return the api terms of service url
     */
    public String getApiTermsOfServiceUrl() {
        return apiTermsOfServiceUrl;
    }

    /**
     * Sets api terms of service url.
     *
     * @param apiTermsOfServiceUrl the api terms of service url
     */
    public void setApiTermsOfServiceUrl(String apiTermsOfServiceUrl) {
        this.apiTermsOfServiceUrl = apiTermsOfServiceUrl;
    }

    /**
     * Gets api license.
     *
     * @return the api license
     */
    public String getApiLicense() {
        return apiLicense;
    }

    /**
     * Sets api license.
     *
     * @param apiLicense the api license
     */
    public void setApiLicense(String apiLicense) {
        this.apiLicense = apiLicense;
    }

    /**
     * Gets api license url.
     *
     * @return the api license url
     */
    public String getApiLicenseUrl() {
        return apiLicenseUrl;
    }

    /**
     * Sets api license url.
     *
     * @param apiLicenseUrl the api license url
     */
    public void setApiLicenseUrl(String apiLicenseUrl) {
        this.apiLicenseUrl = apiLicenseUrl;
    }

    /**
     * Gets request handler selectors.
     *
     * @return the request handler selectors
     */
    public String getRequestHandlerSelectors() {
        return requestHandlerSelectors;
    }

    /**
     * Sets request handler selectors.
     *
     * @param requestHandlerSelectors the request handler selectors
     */
    public void setRequestHandlerSelectors(String requestHandlerSelectors) {
        this.requestHandlerSelectors = requestHandlerSelectors;
    }

    /**
     * Gets path selectors.
     *
     * @return the path selectors
     */
    public String getPathSelectors() {
        return pathSelectors;
    }

    /**
     * Sets path selectors.
     *
     * @param pathSelectors the path selectors
     */
    public void setPathSelectors(String pathSelectors) {
        this.pathSelectors = pathSelectors;
    }

    /**
     * Gets path mapping.
     *
     * @return the path mapping
     */
    public String getPathMapping() {
        return pathMapping;
    }

    /**
     * Sets path mapping.
     *
     * @param pathMapping the path mapping
     */
    public void setPathMapping(String pathMapping) {
        this.pathMapping = pathMapping;
    }

    /**
     * Is use default response messages boolean.
     *
     * @return the boolean
     */
    public Boolean isUseDefaultResponseMessages() {
        return useDefaultResponseMessages;
    }

    /**
     * Sets use default response messages.
     *
     * @param useDefaultResponseMessages the use default response messages
     */
    public void setUseDefaultResponseMessages(Boolean useDefaultResponseMessages) {
        this.useDefaultResponseMessages = useDefaultResponseMessages;
    }

    /**
     * Is enable url templating boolean.
     *
     * @return the boolean
     */
    public Boolean isEnableUrlTemplating() {
        return enableUrlTemplating;
    }

    /**
     * Sets enable url templating.
     *
     * @param enableUrlTemplating the enable url templating
     */
    public void setEnableUrlTemplating(Boolean enableUrlTemplating) {
        this.enableUrlTemplating = enableUrlTemplating;
    }
}
