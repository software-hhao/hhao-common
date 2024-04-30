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

package com.hhao.common.security.tenant;

/**
 * 用于多租户上下文管理
 *
 * @author Wang
 * @since 1.0.0
 */
public class TenantContextHolder {
    private static final ThreadLocal<TenantContext> tenantContextHolder =
            new ThreadLocal<>();

    private static final ThreadLocal<TenantContext> inheritableTenantContextHolder =
            new InheritableThreadLocal<>();

    public static void resetTenantContext() {
        tenantContextHolder.remove();
        inheritableTenantContextHolder.remove();
    }

    public static void setTenantContext(TenantContext tenantContext) {
        setTenantContext(tenantContext, false);
    }

    public static void setTenantContext(TenantContext tenantContext, boolean inheritable) {
        if (tenantContext == null) {
            resetTenantContext();
        }
        else {
            if (inheritable) {
                inheritableTenantContextHolder.set(tenantContext);
                tenantContextHolder.remove();
            }
            else {
                tenantContextHolder.set(tenantContext);
                inheritableTenantContextHolder.remove();
            }
        }
    }
    
    public static TenantContext getTenantContext() {
        TenantContext tenantContext = tenantContextHolder.get();
        if (tenantContext == null) {
            tenantContext = inheritableTenantContextHolder.get();
        }
        return tenantContext;
    }


    public static void setTenant( Tenant tenant) {
        setTenant(tenant, false);
    }

    public static void setTenant( Tenant tenant, boolean inheritable) {
        TenantContext tenantContext = getTenantContext();
        tenantContext.setTenant(tenant);
        setTenantContext(tenantContext, inheritable);
    }

    public static Tenant getTenant() {
        return getTenant(getTenantContext());
    }

    public static Tenant getTenant( TenantContext tenantContext) {
        if (tenantContext != null) {
            Tenant tenant = tenantContext.getTenant();
            if (tenant != null) {
                return tenant;
            }
        }
        return null;
    }
}
