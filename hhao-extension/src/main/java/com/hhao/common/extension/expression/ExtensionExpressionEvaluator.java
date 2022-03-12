
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

package com.hhao.common.extension.expression;

/**
 * @author Wang
 * @since 2022/3/10 21:27
 */
public abstract class ExtensionExpressionEvaluator {
//    private final SpelExpressionParser parser;
//
//    private final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
//
//
//    /**
//     * Create a new instance with the specified {@link SpelExpressionParser}.
//     */
//    protected ExtensionExpressionEvaluator(SpelExpressionParser parser) {
//        Assert.notNull(parser, "SpelExpressionParser must not be null");
//        this.parser = parser;
//    }
//
//    /**
//     * Create a new instance with a default {@link SpelExpressionParser}.
//     */
//    protected ExtensionExpressionEvaluator() {
//        this(new SpelExpressionParser());
//    }
//
//
//    /**
//     * Return the {@link SpelExpressionParser} to use.
//     */
//    protected SpelExpressionParser getParser() {
//        return this.parser;
//    }
//
//    /**
//     * Return a shared parameter name discoverer which caches data internally.
//     */
//    protected ParameterNameDiscoverer getParameterNameDiscoverer() {
//        return this.parameterNameDiscoverer;
//    }
//
//
//    /**
//     * Return the {@link Expression} for the specified SpEL value
//     * <p>{@link #parseExpression(String) Parse the expression} if it hasn't been already.
//     * @param cache the cache to use
//     * @param elementKey the element on which the expression is defined
//     * @param expression the expression to parse
//     */
//    protected Expression getExpression(Map<CachedExpressionEvaluator.ExpressionKey, Expression> cache,
//                                       AnnotatedElementKey elementKey, String expression) {
//
//        CachedExpressionEvaluator.ExpressionKey expressionKey = createKey(elementKey, expression);
//        Expression expr = cache.get(expressionKey);
//        if (expr == null) {
//            expr = parseExpression(expression);
//            cache.put(expressionKey, expr);
//        }
//        return expr;
//    }
//
//    /**
//     * Parse the specified {@code expression}.
//     * @param expression the expression to parse
//     * @since 5.3.13
//     */
//    protected Expression parseExpression(String expression) {
//        return getParser().parseExpression(expression);
//    }
//
//    private CachedExpressionEvaluator.ExpressionKey createKey(AnnotatedElementKey elementKey, String expression) {
//        return new CachedExpressionEvaluator.ExpressionKey(elementKey, expression);
//    }
//
//
//    /**
//     * An expression key.
//     */
//    protected static class ExpressionKey implements Comparable<CachedExpressionEvaluator.ExpressionKey> {
//
//        private final AnnotatedElementKey element;
//
//        private final String expression;
//
//        protected ExpressionKey(AnnotatedElementKey element, String expression) {
//            Assert.notNull(element, "AnnotatedElementKey must not be null");
//            Assert.notNull(expression, "Expression must not be null");
//            this.element = element;
//            this.expression = expression;
//        }
//
//        @Override
//        public boolean equals(@Nullable Object other) {
//            if (this == other) {
//                return true;
//            }
//            if (!(other instanceof CachedExpressionEvaluator.ExpressionKey)) {
//                return false;
//            }
//            CachedExpressionEvaluator.ExpressionKey otherKey = (CachedExpressionEvaluator.ExpressionKey) other;
//            return (this.element.equals(otherKey.element) &&
//                    ObjectUtils.nullSafeEquals(this.expression, otherKey.expression));
//        }
//
//        @Override
//        public int hashCode() {
//            return this.element.hashCode() * 29 + this.expression.hashCode();
//        }
//
//        @Override
//        public String toString() {
//            return this.element + " with expression \"" + this.expression + "\"";
//        }
//
//        @Override
//        public int compareTo(CachedExpressionEvaluator.ExpressionKey other) {
//            int result = this.element.toString().compareTo(other.element.toString());
//            if (result == 0) {
//                result = this.expression.compareTo(other.expression);
//            }
//            return result;
//        }
//    }
}
