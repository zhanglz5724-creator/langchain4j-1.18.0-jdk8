/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.model.input;

import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.input.DefaultPromptTemplateFactory;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.spi.ServiceHelper;
import dev.langchain4j.spi.prompt.PromptTemplateFactory;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PromptTemplate {
    private static final PromptTemplateFactory FACTORY = PromptTemplate.factory();
    static final String CURRENT_DATE = "current_date";
    static final String CURRENT_TIME = "current_time";
    static final String CURRENT_DATE_TIME = "current_date_time";
    private final String templateString;
    private final PromptTemplateFactory.Template template;
    private final Clock clock;

    private static PromptTemplateFactory factory() {
        Iterator<PromptTemplateFactory> iterator = ServiceHelper.loadFactories(PromptTemplateFactory.class).iterator();
        if (iterator.hasNext()) {
            PromptTemplateFactory factory = iterator.next();
            return factory;
        }
        return new DefaultPromptTemplateFactory();
    }

    public PromptTemplate(String template) {
        this(template, (String)null);
    }

    public PromptTemplate(String template, String name) {
        this(template, name, Clock.systemDefaultZone());
    }

    public PromptTemplate(String template, Clock clock) {
        this(template, null, clock);
    }

    public PromptTemplate(final String template, final String name, Clock clock) {
        this.templateString = ValidationUtils.ensureNotBlank(template, "template");
        this.template = name == null ? FACTORY.create(() -> template) : FACTORY.create(new PromptTemplateFactory.Input(){

            @Override
            public String getTemplate() {
                return template;
            }

            @Override
            public String getName() {
                return name;
            }
        });
        this.clock = ValidationUtils.ensureNotNull(clock, "clock");
    }

    public String template() {
        return this.templateString;
    }

    public Prompt apply(Object value) {
        return this.apply(Collections.singletonMap("it", value));
    }

    public Prompt apply(Map<String, Object> variables) {
        ValidationUtils.ensureNotNull(variables, "variables");
        return Prompt.from(this.template.render(this.injectDateTimeVariables(variables)));
    }

    private Map<String, Object> injectDateTimeVariables(Map<String, Object> variables) {
        HashMap<String, Object> variablesCopy = new HashMap<String, Object>(variables);
        variablesCopy.put(CURRENT_DATE, LocalDate.now(this.clock));
        variablesCopy.put(CURRENT_TIME, LocalTime.now(this.clock));
        variablesCopy.put(CURRENT_DATE_TIME, LocalDateTime.now(this.clock));
        return variablesCopy;
    }

    public static PromptTemplate from(String template) {
        return PromptTemplate.from(template, null, null);
    }

    public static PromptTemplate from(String template, String name) {
        return PromptTemplate.from(template, name, null);
    }

    public static PromptTemplate from(String template, Clock clock) {
        return PromptTemplate.from(template, null, clock);
    }

    public static PromptTemplate from(String template, String name, Clock clock) {
        return new PromptTemplate(template, name, clock != null ? clock : Clock.systemDefaultZone());
    }
}

