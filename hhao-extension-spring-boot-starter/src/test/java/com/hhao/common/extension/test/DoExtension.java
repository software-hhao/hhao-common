package com.hhao.common.extension.test;

import com.hhao.common.extension.BizScenario;
import com.hhao.common.extension.model.ExtensionPoint;
import com.hhao.common.extension.model.Return;

/**
 * @author Wang
 * @since 1.0.0
 */
public interface DoExtension extends ExtensionPoint<String,String> {
    Return<String> autoWired(BizScenario bizScenario, String str);
}
