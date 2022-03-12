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

package com.hhao.common.springboot.web.config.websocket.ws.server;

import com.hhao.common.entity.AbstractUser;
import com.hhao.common.exception.error.request.AuthorizeException;
import com.hhao.common.utils.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用token验证
 * token可在header,cookie,url参数中传递
 * 可以重写checkToken方法，验证token，返回user
 * 与DefaultHandshakeInterceptor一起使用，在握手前验证用户信息
 *
 * @author Wang
 * @since 1.0.0
 */
public class HandshakeAuthorizationWithToken implements HandshakeAuthorization {
    /**
     * The constant TOKEN_NAME.
     */
//token参数的名称
    protected static final String TOKEN_NAME="token";
    /**
     * The constant COOKIE_NAME.
     */
//cookie转存到HttpHeaders中的名称
    protected static final String COOKIE_NAME="cookie";

    /**
     * 从HttpHeaders分解出cookie
     *
     * @param headers the headers
     * @return map
     */
    protected Map<String,String> getCookie(HttpHeaders headers){
        Map<String,String> cookieMap=new HashMap<>();
        List<String> content=headers.get(COOKIE_NAME);
        if (content!=null){
            content.forEach(msg->{
                String [] infos= msg.split("=");
                if (infos!=null && infos.length==2){
                    cookieMap.put(StringUtils.trimAllWhitespace(infos[0]),StringUtils.trimAllWhitespace(infos[1]));
                }
            });
        }
        return cookieMap;
    }

    /**
     * 从ServerHttpRequest中获取URL的参数
     *
     * @param request   the request
     * @param paramName the param name
     * @return string
     */
    protected String getParameter(ServerHttpRequest request,String paramName){
        if (request instanceof ServletServerHttpRequest){
            return ((ServletServerHttpRequest) request).getServletRequest().getParameter(paramName);
        }
        return null;
    }

    /**
     * 获取token，获取顺序如下：
     * 1、header
     * 2、url的param
     * 3、cookie
     *
     * @param request the request
     * @return string
     */
    protected String getToken(ServerHttpRequest request){
        String token="";
        //先从request header取
        List<String> values=request.getHeaders().get(TOKEN_NAME);
        if (values!=null && values.size()==1){
            token=values.get(0);
        }
        //再从URL的param中取
        if (!StringUtils.hasLength(token)){
            token=this.getParameter(request,TOKEN_NAME);
        }
        //最后从cookie中取
        if (!StringUtils.hasLength(token)){
            token=this.getCookie(request.getHeaders()).get(TOKEN_NAME);
        }
        return token;
    }

    @Override
    public boolean handshake(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        String token=this.getToken(request);
        if (StringUtils.hasLength(token)){
            try {
                AbstractUser user=authorize(token);
                attributes.put("token", token);
                if (user!=null) {
                    attributes.put("user", user);
                }
                return true;
            } catch (AuthorizeException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 子类需要重写这个方法，验证token正确性
     * 如果验证失败，则抛出AuthorizeException
     * 如果存在用户，则返回Principal，否则返回null
     */
    @Override
    public AbstractUser authorize(String token) throws AuthorizeException {
        if (!StringUtils.hasLength(token)){
            throw new AuthorizeException();
        }
        AbstractUser user=new AbstractUser() {
            @Override
            public String getName() {
                return token;
            }
        };
        return user;
    }
}
