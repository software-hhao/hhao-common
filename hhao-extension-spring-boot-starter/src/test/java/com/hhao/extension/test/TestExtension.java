package com.hhao.extension.test;

import com.hhao.common.extension.BizScenario;
import com.hhao.extension.executor.ExtensionExecutor;
import com.hhao.extension.model.ExtensionPoint;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Wang
 * @since 1.0.0
 */
@SpringBootTest(classes = TestExtensionApplication.class,webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TestExtension {
    @Resource
    private ExtensionExecutor executor;

    @Test
    public void testExtension() {
        SayExtension a1 = new MySayExtension();
        SayExtension a2 = new MySayExtension();


        String str1 = executor.execute(DoExtension.class, BizScenario.valueOf("test", "say"), "wang");
        System.out.println(str1);

        List<String> results1 = executor.multiExecute(DoExtension.class, BizScenario.valueOf("test", "say"), "wang");
        System.out.println(results1);

        executor.executeVoid(SayExtension.class, BizScenario.valueOf("test", "say"), "wang");
        executor.multiExecuteVoid(SayExtension.class, BizScenario.valueOf("test", "say"), "wang");

        String str2 = executor.callback(DoExtension.class, BizScenario.valueOf("test", "say"), ext -> {
            return ext.execute("call back");
        });
        System.out.println(str2);

        List<String> results2 = executor.multiCallback(DoExtension.class, BizScenario.valueOf("test", "say"), exps -> {
            List<String> combinationResult = new ArrayList<>(exps.size());
            String result = null;
            for (ExtensionPoint exp : exps) {
                try {
                    result = (String) exp.execute("good");
                    combinationResult.add(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return combinationResult;
        });

        System.out.println(results2);

        executor.callbackVoid(SayExtension.class, BizScenario.valueOf("test", "say"), ext -> {
            ext.execute("call back");
        });

        executor.multiCallbackVoid(SayExtension.class, BizScenario.valueOf("test", "say"), exps -> {
            for (ExtensionPoint exp : exps) {
                exp.executeVoid("multiCallbackVoid");
            }
        });
    }
}
