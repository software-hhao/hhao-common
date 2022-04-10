package com.hhao.common.support;

/**
 * The interface Tenant context.
 *
 * @author Wang
 * @since 1.0.0
 */
public interface TenantContext {
    /**
     * Gets tenant.
     *
     * @return the tenant
     */
    Tenant getTenant();

    /**
     * Sets tenant.
     *
     * @param tenant the tenant
     */
    void setTenant(Tenant tenant);

}
