
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

package com.hhao.common.sprintboot.webflux.config.safe;

import com.hhao.common.springboot.safe.SafeHtml;
import com.hhao.common.springboot.safe.SafeHtmlExecutor;
import com.hhao.common.springboot.safe.filter.DefaultSafeFilter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The type Default reactor safe filter.
 *
 * @author Wang
 * @since 2022 /1/14 16:22
 */
public class DefaultReactorSafeFilter extends DefaultSafeFilter {
    /**
     * Instantiates a new Safe filter.
     *
     * @param safeHtmlExecutor the safe html executor
     */
    public DefaultReactorSafeFilter(SafeHtmlExecutor safeHtmlExecutor) {
        super(safeHtmlExecutor);
    }

    @Override
    protected Object filterObject(Object obj, SafeHtml safeHtml) {
        if (obj instanceof Mono){
            return ((Mono)obj).map(object->{
               return super.filterObject(object,safeHtml);
            });
        }else if(obj instanceof Flux){
            return ((Flux)obj).map(object->{
                return super.filterObject(object,safeHtml);
            });
        }
        return super.filterObject(obj,safeHtml);
    }
}
