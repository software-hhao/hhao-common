package com.hhao.extension.test;

import com.hhao.extension.annotation.Extension;

/**
 * @author Wang
 * @since 1.0.0
 */
@Extension(bizId = "test",useCase = "say",scenario = "shesay")
public class SheSayExtension implements SayExtension {
    @Override
    public String exec(String context) {
        return "She:" + context;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
