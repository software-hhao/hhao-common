/*
 * Copyright 2018-2021 WangSheng.
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

package com.hhao.common.mybatis.page;/*
 * Copyright 2020-2021 WangSheng.
 *
 * Licensed under the GNU GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.hhao.common.mybatis.page.executor.SingleQueryStaticPageExecutor;

/**
 * @author Wang
 * @since 2021/11/18 11:13
 */
public class PageInfoWithCount extends PageInfo{
    private String countMappedStatementId;

    public String getCountMappedStatementId() {
        return countMappedStatementId;
    }

    public void setCountMappedStatementId(String countMappedStatementId) {
        this.countMappedStatementId = countMappedStatementId;
    }

    public static class Builder extends PageInfo.Builder{
        public Builder(long pageNum,String countMappedStatementId) {
            super(PageInfoWithCount.class,pageNum);
            ((PageInfoWithCount)pageInfo).setCountMappedStatementId(countMappedStatementId);
        }

        public Builder withSingleQueryStaticPageExecutor(){
            pageInfo.setPageExecutor(new SingleQueryStaticPageExecutor());
            return this;
        }

        @Override
        public PageInfo build() {
            return super.build();
        }
    }
}
