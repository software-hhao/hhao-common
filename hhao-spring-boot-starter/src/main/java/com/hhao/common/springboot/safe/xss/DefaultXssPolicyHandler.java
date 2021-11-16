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

package com.hhao.common.springboot.safe.xss;

import com.hhao.common.utils.xss.XssUtils;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * 名称为default的规则执行器
 *
 * @author Wang
 * @since 1.0.0
 */
public class DefaultXssPolicyHandler implements XssPolicyHandler {
    /**
     * The constant NAME.
     */
    public static final String NAME="default";
    /**
     * The constant POLICY_FILE_LOCATION.
     */
    public static final String POLICY_FILE_LOCATION="antisamy.xml";
    private String policyUri="classpath:config/" + POLICY_FILE_LOCATION;

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
        this.policyUri=policyUri;
        this.initPolicy();
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
        this.policyUri = policyUri;
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
            if (policyUri!=null) {
                try {
                    File file = ResourceUtils.getFile(policyUri);
                    if (file != null) {
                        policy = Policy.getInstance(file);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
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
