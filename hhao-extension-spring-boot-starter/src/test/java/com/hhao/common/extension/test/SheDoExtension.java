package com.hhao.common.extension.test;

import com.hhao.common.extension.BizScenario;
import com.hhao.common.extension.annotation.Extension;
import com.hhao.common.extension.model.Return;
import com.hhao.common.extension.model.SimpleReturn;

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

    @Override
    public Return<String> autoWired(BizScenario bizScenario, String str) {
        return SimpleReturn.of(execute(str));
    }
}
