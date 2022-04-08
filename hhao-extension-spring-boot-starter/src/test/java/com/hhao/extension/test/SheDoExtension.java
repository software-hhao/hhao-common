package com.hhao.extension.test;

import com.hhao.extension.annotation.Extension;

/**
 * @author Wang
 * @since 1.0.0
 */
@Extension(bizId = "test",useCase = "say")
public class SheDoExtension implements DoExtension{
    @Override
    public String execute(String context) {
        return "she do:" + context;
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
