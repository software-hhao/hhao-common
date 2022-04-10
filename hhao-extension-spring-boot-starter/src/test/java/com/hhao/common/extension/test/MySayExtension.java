package com.hhao.common.extension.test;

import com.hhao.common.extension.BizScenario;
import com.hhao.common.extension.annotation.Extension;

/**
 * @author Wang
 * @since 1.0.0
 */
@Extension(bizId = "test",useCase = "say")
public class MySayExtension implements SayExtension {

    @Override
    public Void execute(String context) {
        System.out.println("my say:" + context);
        return null;
    }

    @Override
    public void executeVoid(String context) {
        System.out.println("my ...:" + context);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public void autoWired(BizScenario bizScenario, String str) {
        execute(str);
    }
}
