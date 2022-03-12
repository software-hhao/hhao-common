
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

import com.hhao.common.extension.annotation.Extension;
import org.springframework.stereotype.Component;

/**
 * @author Wang
 * @since 2022/3/11 20:24
 */
@Component
public class MySay implements Say{

    @Extension(condition = "asdfasd")
    @Override
    public String say(String name) {
        return "my say:" + name;
    }
}
