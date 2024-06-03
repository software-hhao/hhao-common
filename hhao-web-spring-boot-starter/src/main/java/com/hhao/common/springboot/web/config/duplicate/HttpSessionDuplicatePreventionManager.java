package com.hhao.common.springboot.web.config.duplicate;

import com.hhao.common.springboot.duplicate.DuplicatePreventException;
import com.hhao.common.springboot.duplicate.DuplicatePreventInfo;
import com.hhao.common.springboot.duplicate.DuplicatePreventionManager;
import jakarta.servlet.http.HttpServletRequest;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

/**
 * 基于HttpSession实现
 */
public class HttpSessionDuplicatePreventionManager  implements DuplicatePreventionManager {
    private final String REQUEST_PARAMETER_MD5_KEY="request-parameter-hash";

    @Override
    public void storeDuplicatePreventInfo(MethodInvocation invocation, DuplicatePreventInfo duplicatePreventInfo) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        request.getSession(true).setAttribute(duplicatePreventInfo.getUniqueKey(), duplicatePreventInfo);
    }

    @Override
    public String getContentHashFromClient(MethodInvocation invocation) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Optional<String> md5FromParams = getMd5FromRequestParameters(request);
        if (md5FromParams.isPresent()) {
            return md5FromParams.get();
        }
        Optional<String> md5FromHeaders = getMd5FromRequestHeaders(request);
        if (md5FromHeaders.isPresent()) {
            return md5FromHeaders.get();
        }
        throw new DuplicatePreventException("Neither 'request-parameter-md5' parameter in request nor 'request-parameter-md5' header was found.");
    }

    private Optional<String> getMd5FromRequestParameters(HttpServletRequest request) {
        return Optional.ofNullable(request.getParameter(REQUEST_PARAMETER_MD5_KEY));
    }

    private Optional<String> getMd5FromRequestHeaders(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(REQUEST_PARAMETER_MD5_KEY));
    }

    @Override
    public boolean isDuplicate(MethodInvocation invocation, DuplicatePreventInfo duplicatePreventInfo) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        DuplicatePreventInfo historyInfo = (DuplicatePreventInfo) request.getSession(true).getAttribute(duplicatePreventInfo.getUniqueKey());
        if (historyInfo==null){
            return false;
        }
        // 毫秒
        long currentTime = System.currentTimeMillis();
        // 说明重复提交了
        if (currentTime - historyInfo.getTimeStamp() < duplicatePreventInfo.getExpirationTime()*1000) {
            return true;
        }
        // 更新时间
        duplicatePreventInfo.setTimeStamp(currentTime);
        return false;
    }
}
