package com.hhao.extension.test;

import com.hhao.common.extension.BizScenario;
import com.hhao.extension.executor.ExtensionExecutor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

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
        SayExtension a1=new MySayExtension();
        SayExtension a2=new MySayExtension();


        String str=executor.execute(SayExtension.class, BizScenario.valueOf("test","say","mysay"),"wang");
        System.out.println(str);
    }
}
