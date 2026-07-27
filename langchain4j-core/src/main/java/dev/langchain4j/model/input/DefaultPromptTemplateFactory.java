/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.input;

import dev.langchain4j.Internal;
import dev.langchain4j.internal.Exceptions;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.spi.prompt.PromptTemplateFactory;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Internal
class DefaultPromptTemplateFactory
implements PromptTemplateFactory {
    DefaultPromptTemplateFactory() {
    }

    @Override
    public DefaultTemplate create(PromptTemplateFactory.Input input) {
        return new DefaultTemplate(input.getTemplate());
    }

    static class DefaultTemplate
    implements PromptTemplateFactory.Template {
        private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{\\{\\s*(.+?)\\s*\\}\\}");
        private final String template;
        private final Set<String> allVariables;

        public DefaultTemplate(String template) {
            this.template = ValidationUtils.ensureNotBlank(template, "template");
            this.allVariables = DefaultTemplate.extractVariables(template);
        }

        private static Set<String> extractVariables(String template) {
            HashSet<String> variables = new HashSet<String>();
            Matcher matcher = VARIABLE_PATTERN.matcher(template);
            while (matcher.find()) {
                variables.add(matcher.group(1).trim());
            }
            return variables;
        }

        @Override
        public String render(Map<String, Object> variables) {
            ValidationUtils.ensureNotNull(variables, "variables");
            if (this.allVariables.isEmpty()) {
                return this.template;
            }
            this.ensureAllVariablesProvided(variables);
            Matcher matcher = VARIABLE_PATTERN.matcher(this.template);
            StringBuffer result = new StringBuffer(this.template.length());
            while (matcher.find()) {
                String variable = matcher.group(1).trim();
                Object value = variables.get(variable);
                if (value == null || value.toString() == null) {
                    throw Exceptions.illegalArgument("Value for the variable '%s' is null", variable);
                }
                matcher.appendReplacement(result, Matcher.quoteReplacement(value.toString()));
            }
            matcher.appendTail(result);
            return result.toString();
        }

        private void ensureAllVariablesProvided(Map<String, Object> providedVariables) {
            for (String variable : this.allVariables) {
                if (providedVariables.containsKey(variable)) continue;
                throw Exceptions.illegalArgument("Value for the variable '%s' is missing", variable);
            }
        }
    }
}

