
/*
 * Copyright 2018-2022 WangSheng.
 *
 * Licensed under the GNU GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hhao.common.extension.test;

import com.hhao.common.extension.annotation.ExtensionPointAutowired;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Wang
 * @since 2022/3/11 20:28
 */
@SpringBootTest(classes = JunitAppliction.class)
public class ExtensionPointAutowiredTest {


    private Say say1;


    @ExtensionPointAutowired
    public void setSay1(Say say1) {
        this.say1 = say1;
    }

    public void aa(Say say1){
        System.out.println(say1.say("hello"));
    }

    @Test
    public void testExecuteAllMatch() {
        aa(say1);
       //System.out.println(say1.say("hello"));
    }
}
