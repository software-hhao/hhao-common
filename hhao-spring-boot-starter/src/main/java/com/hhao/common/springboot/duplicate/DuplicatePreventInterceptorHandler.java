package com.hhao.common.springboot.duplicate;

import com.hhao.common.log.Logger;
import com.hhao.common.log.LoggerFactory;
import com.hhao.common.springboot.aop.InterceptorHandler;
import org.aopalliance.intercept.Invocation;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.Ordered;

public class DuplicatePreventInterceptorHandler implements InterceptorHandler {
    /**
     * The Logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(DuplicatePreventInterceptorHandler.class);
    /**
     * 拦截器id
     */
    public static final String ID="rejectDuplicateInterceptorHandler";
    private int order = Ordered.HIGHEST_PRECEDENCE;
    private DuplicatePreventionManager duplicatePreventionManager;

    public DuplicatePreventInterceptorHandler(DuplicatePreventionManager duplicatePreventionManager){
        this.duplicatePreventionManager=duplicatePreventionManager;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public boolean onBegin(Invocation invocation) {
        MethodInvocation methodInvocation=((MethodInvocation)invocation);
        // 获取注解
        DuplicatePrevent duplicatePrevent = methodInvocation.getMethod().getAnnotation(DuplicatePrevent.class);
        if (duplicatePrevent == null) {
            return true;
        }

        // 获取提交内容Hash
        String contentHash ="";
        if (!duplicatePrevent.openServerCheck()) {
            contentHash = duplicatePreventionManager.getContentHashFromClient(methodInvocation);
        }else{
            contentHash=duplicatePreventionManager.getContentHashFromServer(methodInvocation);
        }
        // 生成防重复信息
        DuplicatePreventInfo duplicatePreventInfo=new DuplicatePreventInfo(duplicatePrevent.uniqueKey(),contentHash, duplicatePrevent.expirationTime());

        // 验证是否重复提交
        if (duplicatePreventionManager.isDuplicate(methodInvocation,duplicatePreventInfo)){
            logger.info("Duplicate submission detected, rejecting request");
            // 保存防重信息
            duplicatePreventionManager.storeDuplicatePreventInfo(methodInvocation,duplicatePreventInfo);
            throw new DuplicatePreventException("Duplicate submission detected, rejecting request");
        }
        // 保存防重信息
        duplicatePreventionManager.storeDuplicatePreventInfo(methodInvocation,duplicatePreventInfo);
        return true;
    }

    @Override
    public boolean support(String id) {
        return ID.equals(id);
    }
}
