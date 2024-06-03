package com.hhao.common.springboot.duplicate;

import com.hhao.common.jackson.JacksonUtil;
import com.hhao.common.jackson.JacksonUtilFactory;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;

/**
 * 接口用于管理重复提交防护逻辑
 * 支持多种存储机制实现，如内存、Redis等
 */
public interface DuplicatePreventionManager {

    /**
     * 保存防重记录，用于后续重复检查
     */
    void storeDuplicatePreventInfo(MethodInvocation invocation, DuplicatePreventInfo duplicatePreventInfo);

    /**
     * 从客户端获取提交内容的哈希值
     * @param invocation
     * @return
     */
    String getContentHashFromClient(MethodInvocation invocation);

    /**
     * 从服务器端获取提交内容的哈希值
     * @param invocation
     * @return
     */
    default String getContentHashFromServer(MethodInvocation invocation){
        try {
            JacksonUtil jacksonUtil= JacksonUtilFactory.getJsonUtil();
            Object[] args = invocation.getArguments();
            StringBuilder paramsJsonValues = new StringBuilder();
            for (Object arg : args) {
                paramsJsonValues.append(jacksonUtil.obj2String(arg));
            }
            return DigestUtils.md5DigestAsHex(paramsJsonValues.toString().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new DuplicatePreventException("UnsupportedEncoding UTF-8",e);
        }
    }

    /**
     * 检验提交是否重复
     */
    boolean isDuplicate(MethodInvocation invocation, DuplicatePreventInfo duplicatePreventInfo);
}
