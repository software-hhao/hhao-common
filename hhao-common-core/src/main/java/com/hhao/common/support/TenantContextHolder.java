package com.hhao.common.support;

import com.hhao.common.lang.Nullable;

/**
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

    public static void setTenantContext(@Nullable TenantContext tenantContext) {
        setTenantContext(tenantContext, false);
    }

    public static void setTenantContext(@Nullable TenantContext tenantContext, boolean inheritable) {
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

    @Nullable
    public static TenantContext getTenantContext() {
        TenantContext tenantContext = tenantContextHolder.get();
        if (tenantContext == null) {
            tenantContext = inheritableTenantContextHolder.get();
        }
        return tenantContext;
    }


    public static void setTenant(@Nullable Tenant tenant) {
        setTenant(tenant, false);
    }

    public static void setTenant(@Nullable Tenant tenant, boolean inheritable) {
        TenantContext tenantContext = getTenantContext();
        tenantContext.setTenant(tenant);
        setTenantContext(tenantContext, inheritable);
    }

    public static Tenant getTenant() {
        return getTenant(getTenantContext());
    }

    public static Tenant getTenant(@Nullable TenantContext tenantContext) {
        if (tenantContext != null) {
            Tenant tenant = tenantContext.getTenant();
            if (tenant != null) {
                return tenant;
            }
        }
        return null;
    }
}
