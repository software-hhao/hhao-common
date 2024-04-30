/*
 * Copyright 2008-2024 wangsheng
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hhao.common.extension.test;

import com.hhao.common.extension.BizScenario;
import com.hhao.common.extension.annotation.ExtensionPointAutowired;
import com.hhao.common.extension.model.CombinedReturn;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author Wang
 * @since 1.0.0
 */
@SpringBootTest(classes = TestExtensionApplication.class)
public class TestAutoWired {

    @ExtensionPointAutowired(model = ExtensionPointAutowired.Model.SIMPLE)
    private SayExtension sayExtension;

    @ExtensionPointAutowired(model = ExtensionPointAutowired.Model.MULTI)
    private DoExtension doExtension;

    @Test
    public void Test(){
        sayExtension.autoWired(BizScenario.valueOf("test", "say"),"good");
    }

    @Test
    public void TestWithReturn(){
        CombinedReturn<String> multiValues=(CombinedReturn)doExtension.autoWired(BizScenario.valueOf("test", "say"),"good");
        List<String> results=multiValues.getCombinationResult();

        System.out.println(results);
    }
}
