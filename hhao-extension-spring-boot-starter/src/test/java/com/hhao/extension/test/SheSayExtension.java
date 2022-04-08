package com.hhao.extension.test;

import com.hhao.extension.annotation.Extension;

/**
 * @author Wang
 * @since 1.0.0
 */
@Extension(bizId = "test",useCase = "say")
public class SheSayExtension implements SayExtension {


    @Override
    public Void execute(String context) {
        System.out.print("She say:" + context);
        return null;
    }

    @Override
    public void executeVoid(String context) {
        System.out.println("she ...:" + context);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
