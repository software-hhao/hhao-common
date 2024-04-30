package com.hhao.common.extension.model;

/**
 * The type Combined return builder.
 *
 * @author Wang
 * @since 1.0.0
 */
public abstract class CombinedReturnBuilder {
    /**
     * Builder combined return builder.
     *
     * @return the combined return builder
     */
    public abstract CombinedReturnBuilder builder();

    /**
     * Gets combined return instance.
     *
     * @return the combined return instance
     */
    public abstract CombinedReturn combinedReturnInstance();

    public abstract CombinedReturn combinedReturnInstance(int size);

    /**
     * The type Default combined return builder.
     */
    public static class DefaultCombinedReturnBuilder extends CombinedReturnBuilder{

        @Override
        public CombinedReturnBuilder builder() {
            return new DefaultCombinedReturnBuilder();
        }

        @Override
        public CombinedReturn combinedReturnInstance() {
            return CombinedReturn.build();
        }

        @Override
        public CombinedReturn combinedReturnInstance(int size) {
            return CombinedReturn.build(size);
        }
    }
}
