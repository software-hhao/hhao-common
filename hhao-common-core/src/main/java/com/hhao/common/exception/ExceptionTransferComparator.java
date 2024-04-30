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

package com.hhao.common.exception;

import java.util.Comparator;

/**
 * @author Wang
 * @since 1.0.0
 */
public class ExceptionTransferComparator implements Comparator<ExceptionTransfer> {
    public static final ExceptionTransferComparator INSTANCE = new ExceptionTransferComparator();

    @Override
    public int compare(ExceptionTransfer o1, ExceptionTransfer o2) {
        if (o1 == null && o2 == null) {
            return 0;
        }

        if (o1 == null && o2 != null) {
            return 1;
        } else if (o1 != null && o2 == null) {
            return -1;
        }
        int i1 = o1.getOrder();
        int i2 = o2.getOrder();
        return Integer.compare(i1, i2);
    }
}
