package com.hhao.common.extension.test;

import com.hhao.common.extension.BizScenario;
import com.hhao.common.extension.model.ExtensionPoint;

/**
 * @author Wang
 * @since 1.0.0
 */
public interface SayExtension extends ExtensionPoint<Void,String> {
    void autoWired(BizScenario bizScenario, String str);
}
