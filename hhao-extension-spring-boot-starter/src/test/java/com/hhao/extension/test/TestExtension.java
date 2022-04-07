package com.hhao.extension.test;

import com.hhao.common.extension.BizScenario;
import com.hhao.extension.executor.ExtensionExecutor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
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
        SayExtension a1=new MySayExtension();
        SayExtension a2=new MySayExtension();


        List<String> results=executor.multiExecute(DoExtension.class, BizScenario.valueOf("test","say"),"wang");
        System.out.println(results);
    }
}
