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

package com.hhao.common.springboot.safe.xss;

import com.hhao.common.utils.StringUtils;
import com.hhao.common.utils.xss.XssUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 名称为default的规则执行器
 *
 * @author Wang
 * @since 1.0.0
 */
public class DefaultXssPolicyHandler implements XssPolicyHandler {
    private static final Log log = LogFactory.getLog(DefaultXssPolicyHandler.class);
    /**
     * The constant NAME.
     */
    public static final String NAME="default";
    /**
     * The constant POLICY_FILE_LOCATION.
     */
    public static final String POLICY_FILE_LOCATION="antisamy.xml";
    private List<String> policyUris= new ArrayList<>();
    private String policyUri="";
    private Policy policy=null;
    /**
     * Instantiates a new Default xss policy handler.
     */
    public DefaultXssPolicyHandler(){

    }

    /**
     * Instantiates a new Default xss policy handler.
     *
     * @param policyUri the policy uri
     */
    public DefaultXssPolicyHandler(String policyUri){
        policyUris.addAll(Arrays.asList(new String[]{
                "classpath:config/" + POLICY_FILE_LOCATION,
                "classpath:" + POLICY_FILE_LOCATION,
                ResourceUtils.FILE_URL_PREFIX + System.getProperty("user.dir") + File.separator + "config" + File.separator + POLICY_FILE_LOCATION
        }));
        policyUris.add(policyUri);
        //this.initPolicy();
    }

    /**
     * Gets policy uri.
     *
     * @return the policy uri
     */
    public String getPolicyUri() {
        return policyUri;
    }

    /**
     * Sets policy uri.
     *
     * @param policyUri the policy uri
     * @return the policy uri
     */
    public DefaultXssPolicyHandler setPolicyUri(String policyUri) {
        if (StringUtils.hasText(policyUri)) {
            this.policyUri = policyUri;
        }
        return this;
    }

    @Override
    public boolean support(String name) {
        return NAME.equals(name);
    }

    /**
     * Init policy default xss policy handler.
     *
     * @return the default xss policy handler
     */
    protected DefaultXssPolicyHandler initPolicy(){
        try {
            for(int i=policyUris.size()-1;i>=0;i--){
                policyUri=policyUris.get(i);
                try {
                    File file = ResourceUtils.getFile(policyUri);
                    if (file != null) {
                        policy = Policy.getInstance(file);
                    }
                    if (policy!=null) {
                        break;
                    }
                } catch (Exception e) {
                    log.warn(String.format("policy file not found.%s",e.getMessage()));
                }
            }
            //最外采用本身自有的
            if (policy==null) {
                policy = Policy.getInstance(XssUtils.class.getResourceAsStream(POLICY_FILE_LOCATION));
            }
        } catch (PolicyException e) {
            e.printStackTrace();
        }
        Assert.notNull(policy,"policy is not null");
        return this;
    }

    @Override
    public Policy getPolicy() {
        if (policy==null){
            initPolicy();
        }
        return policy;
    }
}
