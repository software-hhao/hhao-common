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

package com.hhao.common.springboot.safe.executor;

import com.hhao.common.springboot.safe.SafeHtml;
import com.hhao.common.springboot.safe.xss.XssPolicyHandler;
import com.hhao.common.springboot.safe.decode.DecodeHandler;
import com.hhao.common.utils.html.HtmlUtils;
import com.hhao.common.utils.xss.XssUtils;
import org.owasp.validator.html.Policy;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Xss执行器
 * 根据注解@SafeHtml进行过滤操作
 *
 * @author Wang
 * @since 1.0.0
 */
public class DefaultSafeHtmlExecutor implements SafeHtmlExecutor {
    /**
     * The constant DEFAULT_CHARACTER_ENCODING.
     */
    public static final String DEFAULT_CHARACTER_ENCODING = "UTF-8";

    private List<XssPolicyHandler> xssPolicyHandlers =new ArrayList<>(16);
    private List<DecodeHandler> decodeHandlers=new ArrayList<>(16);

    /**
     * Instantiates a new Default safe html executor.
     *
     * @param xssPolicyHandlers the xss policy handlers
     * @param decodeHandlers    the decode handlers
     */
    public DefaultSafeHtmlExecutor(List<XssPolicyHandler> xssPolicyHandlers, List<DecodeHandler> decodeHandlers){
        this.xssPolicyHandlers = xssPolicyHandlers;
        this.decodeHandlers=decodeHandlers;
    }

    private XssPolicyHandler findPolicyHandler(SafeHtml safeHtml){
        return xssPolicyHandlers.stream().filter(xssPolicyHandler -> {
            return xssPolicyHandler.support(safeHtml.xssPolicy());
        }).findFirst().orElseThrow();
    }

    private DecodeHandler findDecodeHandler(SafeHtml safeHtml){
        return decodeHandlers.stream().filter(decodeHandler -> {
            return decodeHandler.support(safeHtml.decode());
        }).findFirst().orElseThrow();
    }

    private String htmlClear(String html, SafeHtml safeHtml) {
        Policy policy=findPolicyHandler(safeHtml).getPolicy();
        return XssUtils.cleanHtml(html,policy);
    }

    private String htmlClear(String html,Policy policy) {
        return XssUtils.cleanHtml(html,policy);
    }

    private String htmlEscape(String html) {
        return HtmlUtils.htmlEscape(html,DEFAULT_CHARACTER_ENCODING);
    }

    private String htmlEscape(String html,String encoding) {
        return HtmlUtils.htmlEscape(html,encoding);
    }

    /**
     * 默认情况下，如果注解为null，则按htmlEscape方式过滤
     */
    @Override
    public String filter(@NonNull String html, SafeHtml safeHtml) {
        Assert.notNull(html, "html not null");
        if (safeHtml == null) {
            return htmlEscape(html);
        }
        //判断是否需要先解码
        DecodeHandler decodeHandler=null;
        if (StringUtils.hasLength(safeHtml.decode())){
            decodeHandler=this.findDecodeHandler(safeHtml);
        }
        if (decodeHandler!=null){
            html=decodeHandler.decode(html);
        }
        //xss处理
        if (SafeHtml.XssFilterModel.CLEAR.equals(safeHtml.xssFilterModel())) {
            return htmlClear(html, safeHtml);
        } else if (SafeHtml.XssFilterModel.ESCAPE.equals(safeHtml.xssFilterModel())) {
            return htmlEscape(html);
        }
        return html;
    }
}
