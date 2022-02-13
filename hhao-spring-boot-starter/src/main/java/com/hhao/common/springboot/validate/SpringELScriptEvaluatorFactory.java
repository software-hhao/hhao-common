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

package com.hhao.common.springboot.validate;

import org.hibernate.validator.spi.scripting.AbstractCachingScriptEvaluatorFactory;
import org.hibernate.validator.spi.scripting.ScriptEvaluationException;
import org.hibernate.validator.spi.scripting.ScriptEvaluator;
import org.springframework.expression.*;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;

/**
 * The type Spring el script evaluator factory.
 *
 * @author Wang
 * @since 1.00
 */
public class SpringELScriptEvaluatorFactory extends AbstractCachingScriptEvaluatorFactory {

    /**
     * Create new script evaluator script evaluator.
     *
     * @param languageName the language name
     * @return the script evaluator
     */
    @Override
    public ScriptEvaluator createNewScriptEvaluator(String languageName) {
        if (!"spring".equalsIgnoreCase(languageName)) {
            throw new IllegalStateException("Only Spring EL is supported");
        }
        return new SpringELScriptEvaluator();
    }

    private static class SpringELScriptEvaluator implements ScriptEvaluator {
        private final ExpressionParser expressionParser = new SpelExpressionParser();

        /**
         * Evaluate object.
         *
         * @param script   the script
         * @param bindings the bindings
         * @return the object
         * @throws ScriptEvaluationException the script evaluation exception
         */
        @Override
        public Object evaluate(String script, Map<String, Object> bindings) throws ScriptEvaluationException {
            try {
                Expression expression = expressionParser.parseExpression(script);
                EvaluationContext context = new StandardEvaluationContext(bindings.values().iterator().next());
                for (Map.Entry<String, Object> binding : bindings.entrySet()) {
                    context.setVariable(binding.getKey(), binding.getValue());
                }
                return expression.getValue(context);
            } catch (ParseException | EvaluationException e) {
                throw new ScriptEvaluationException("Unable to evaluate SpEL script", e);
            }
        }
    }
}
