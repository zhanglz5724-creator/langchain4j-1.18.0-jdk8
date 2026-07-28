/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.classinstance.ClassInstanceLoader
 *  dev.langchain4j.guardrail.Guardrail
 *  dev.langchain4j.guardrail.GuardrailRequest
 *  dev.langchain4j.guardrail.GuardrailResult
 *  dev.langchain4j.guardrail.InputGuardrail
 *  dev.langchain4j.guardrail.InputGuardrailExecutor
 *  dev.langchain4j.guardrail.InputGuardrailExecutor$InputGuardrailExecutorBuilder
 *  dev.langchain4j.guardrail.OutputGuardrail
 *  dev.langchain4j.guardrail.OutputGuardrailExecutor
 *  dev.langchain4j.guardrail.OutputGuardrailExecutor$OutputGuardrailExecutorBuilder
 *  dev.langchain4j.guardrail.config.GuardrailsConfig
 *  dev.langchain4j.guardrail.config.InputGuardrailsConfig
 *  dev.langchain4j.guardrail.config.OutputGuardrailsConfig
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.spi.classloading.ClassMetadataProviderFactory
 */
package dev.langchain4j.service.guardrail;

import dev.langchain4j.classinstance.ClassInstanceLoader;
import dev.langchain4j.classloading.ClassMetadataProvider;
import dev.langchain4j.guardrail.Guardrail;
import dev.langchain4j.guardrail.GuardrailRequest;
import dev.langchain4j.guardrail.GuardrailResult;
import dev.langchain4j.guardrail.InputGuardrail;
import dev.langchain4j.guardrail.InputGuardrailExecutor;
import dev.langchain4j.guardrail.OutputGuardrail;
import dev.langchain4j.guardrail.OutputGuardrailExecutor;
import dev.langchain4j.guardrail.config.GuardrailsConfig;
import dev.langchain4j.guardrail.config.InputGuardrailsConfig;
import dev.langchain4j.guardrail.config.OutputGuardrailsConfig;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.service.guardrail.DefaultGuardrailService;
import dev.langchain4j.service.guardrail.GuardrailService;
import dev.langchain4j.service.guardrail.InputGuardrails;
import dev.langchain4j.service.guardrail.OutputGuardrails;
import dev.langchain4j.spi.classloading.ClassMetadataProviderFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class GuardrailServiceBuilder
implements GuardrailService.Builder {
    private final Supplier<InputGuardrailExecutor> defaultInputGuardrailSupplier = () -> ((InputGuardrailExecutor.InputGuardrailExecutorBuilder)((InputGuardrailExecutor.InputGuardrailExecutorBuilder)InputGuardrailExecutor.builder().config((GuardrailsConfig)this.inputGuardrailsConfig)).guardrails(GuardrailServiceBuilder.getNonAnnotationBasedClassLevelGuardrails(this.inputGuardrails, this.inputGuardrailClasses))).build();
    private final Supplier<OutputGuardrailExecutor> defaultOutputGuardrailSupplier = () -> ((OutputGuardrailExecutor.OutputGuardrailExecutorBuilder)((OutputGuardrailExecutor.OutputGuardrailExecutorBuilder)OutputGuardrailExecutor.builder().config((GuardrailsConfig)this.outputGuardrailsConfig)).guardrails(GuardrailServiceBuilder.getNonAnnotationBasedClassLevelGuardrails(this.outputGuardrails, this.outputGuardrailClasses))).build();
    private final Class<?> aiServiceClass;
    private InputGuardrailsConfig inputGuardrailsConfig;
    private OutputGuardrailsConfig outputGuardrailsConfig;
    private List<Class<? extends InputGuardrail>> inputGuardrailClasses = new ArrayList<Class<? extends InputGuardrail>>();
    private List<Class<? extends OutputGuardrail>> outputGuardrailClasses = new ArrayList<Class<? extends OutputGuardrail>>();
    private List<InputGuardrail> inputGuardrails = new ArrayList<InputGuardrail>();
    private List<OutputGuardrail> outputGuardrails = new ArrayList<OutputGuardrail>();

    GuardrailServiceBuilder(Class<?> aiServiceClass) {
        this.aiServiceClass = (Class)ValidationUtils.ensureNotNull(aiServiceClass, (String)"aiServiceClass");
    }

    @Override
    public GuardrailService.Builder inputGuardrailsConfig(InputGuardrailsConfig config) {
        this.inputGuardrailsConfig = (InputGuardrailsConfig)ValidationUtils.ensureNotNull((Object)config, (String)"config");
        return this;
    }

    @Override
    public GuardrailService.Builder outputGuardrailsConfig(OutputGuardrailsConfig config) {
        this.outputGuardrailsConfig = (OutputGuardrailsConfig)ValidationUtils.ensureNotNull((Object)config, (String)"config");
        return this;
    }

    @Override
    public <I extends InputGuardrail> GuardrailService.Builder inputGuardrailClasses(List<Class<? extends I>> guardrailClasses) {
        this.inputGuardrailClasses.clear();
        if (guardrailClasses != null) {
            this.inputGuardrailClasses.addAll(guardrailClasses);
        }
        return this;
    }

    @Override
    public <O extends OutputGuardrail> GuardrailService.Builder outputGuardrailClasses(List<Class<? extends O>> guardrailClasses) {
        this.outputGuardrailClasses.clear();
        if (guardrailClasses != null) {
            this.outputGuardrailClasses.addAll(guardrailClasses);
        }
        return this;
    }

    @Override
    public <I extends InputGuardrail> GuardrailService.Builder inputGuardrails(List<I> guardrails) {
        this.inputGuardrails.clear();
        if (guardrails != null) {
            this.inputGuardrails.addAll(guardrails);
        }
        return this;
    }

    @Override
    public <O extends OutputGuardrail> GuardrailService.Builder outputGuardrails(List<O> guardrails) {
        this.outputGuardrails.clear();
        if (guardrails != null) {
            this.outputGuardrails.addAll(guardrails);
        }
        return this;
    }

    @Override
    public DefaultGuardrailService build() {
        ClassMetadataProviderFactory factory;
        HashMap<Object, InputGuardrailExecutor> inputGuardrailsByMethod = new HashMap<Object, InputGuardrailExecutor>();
        HashMap<Object, OutputGuardrailExecutor> outputGuardrailsByMethod = new HashMap<Object, OutputGuardrailExecutor>();
        ClassMetadataProviderFactory typedFactory = factory = ClassMetadataProvider.getClassMetadataProviderFactory();
        typedFactory.getNonStaticMethodsOnClass(this.aiServiceClass).forEach(method -> {
            InputGuardrailExecutor inputGuardrailsForMethod = this.computeInputGuardrailsForAiServiceMethod(method, typedFactory);
            OutputGuardrailExecutor outputGuardrailsForMethod = this.computeOutputGuardrailsForAiServiceMethod(method, typedFactory);
            if (!inputGuardrailsForMethod.guardrails().isEmpty()) {
                inputGuardrailsByMethod.put(method, inputGuardrailsForMethod);
            }
            if (!outputGuardrailsForMethod.guardrails().isEmpty()) {
                outputGuardrailsByMethod.put(method, outputGuardrailsForMethod);
            }
        });
        return new DefaultGuardrailService(this.aiServiceClass, inputGuardrailsByMethod, outputGuardrailsByMethod);
    }

    private static <P extends GuardrailRequest, R extends GuardrailResult<R>, G extends Guardrail<P, R>> List<G> getNonAnnotationBasedClassLevelGuardrails(List<G> guardrails, List<Class<? extends G>> guardrailClasses) {
        ValidationUtils.ensureNotNull(guardrails, (String)"guardrails");
        ValidationUtils.ensureNotNull(guardrailClasses, (String)"guardrailClasses");
        Stream guardrailsSetByBuilderAtClassLevel = guardrails.stream();
        Stream<Guardrail> guardrailsSetByBuilderAtClassLevelByClassName = guardrailClasses.stream().map(GuardrailServiceBuilder::getGuardrailClassInstance);
        return Stream.concat(guardrailsSetByBuilderAtClassLevel, guardrailsSetByBuilderAtClassLevelByClassName).collect(Collectors.toCollection(ArrayList::new));
    }

    private static <P extends GuardrailRequest, R extends GuardrailResult<R>, G extends Guardrail<P, R>> G getGuardrailClassInstance(Class<G> guardrailClass) {
        ValidationUtils.ensureNotNull(guardrailClass, (String)"guardrailClass");
        return (G)((Guardrail)ClassInstanceLoader.getClassInstance(guardrailClass));
    }

    private static <I extends InputGuardrail> List<I> getGuardrails(InputGuardrails inputGuardrails) {
        return Stream.of(inputGuardrails.value()).map(guardrailClass -> (InputGuardrail)GuardrailServiceBuilder.getGuardrailClassInstance(guardrailClass)).collect(Collectors.toList());
    }

    private static <O extends OutputGuardrail> List<O> getGuardrails(OutputGuardrails outputGuardrails) {
        return Stream.of(outputGuardrails.value()).map(guardrailClass -> (OutputGuardrail)GuardrailServiceBuilder.getGuardrailClassInstance(guardrailClass)).collect(Collectors.toList());
    }

    private static InputGuardrailsConfig computeConfig(InputGuardrails annotation) {
        return (InputGuardrailsConfig)InputGuardrailsConfig.builder().build();
    }

    private static OutputGuardrailsConfig computeConfig(OutputGuardrails annotation) {
        return (OutputGuardrailsConfig)OutputGuardrailsConfig.builder().maxRetries(annotation.maxRetries()).build();
    }

    private InputGuardrailExecutor computeInputGuardrails(InputGuardrails annotation) {
        return ((InputGuardrailExecutor.InputGuardrailExecutorBuilder)((InputGuardrailExecutor.InputGuardrailExecutorBuilder)InputGuardrailExecutor.builder().config((GuardrailsConfig)(this.hasInputGuardrailConfigSetOnBuilder() ? this.inputGuardrailsConfig : GuardrailServiceBuilder.computeConfig(annotation)))).guardrails(this.hasInputGuardrailsSetOnBuilder() ? GuardrailServiceBuilder.getNonAnnotationBasedClassLevelGuardrails(this.inputGuardrails, this.inputGuardrailClasses) : GuardrailServiceBuilder.getGuardrails(annotation))).build();
    }

    private OutputGuardrailExecutor computeOutputGuardrails(OutputGuardrails annotation) {
        return ((OutputGuardrailExecutor.OutputGuardrailExecutorBuilder)((OutputGuardrailExecutor.OutputGuardrailExecutorBuilder)OutputGuardrailExecutor.builder().config((GuardrailsConfig)(this.hasOutputGuardrailConfigSetOnBuilder() ? this.outputGuardrailsConfig : GuardrailServiceBuilder.computeConfig(annotation)))).guardrails(this.hasOutputGuardrailsSetOnBuilder() ? GuardrailServiceBuilder.getNonAnnotationBasedClassLevelGuardrails(this.outputGuardrails, this.outputGuardrailClasses) : GuardrailServiceBuilder.getGuardrails(annotation))).build();
    }

    private <MethodKey> InputGuardrailExecutor computeInputGuardrailsForAiServiceMethod(MethodKey method, ClassMetadataProviderFactory<MethodKey> factory) {
        if (this.inputGuardrailsAndConfigSetOnBuilder()) {
            return this.defaultInputGuardrailSupplier.get();
        }
        return factory.getAnnotation(method, InputGuardrails.class).map(this::computeInputGuardrails).orElseGet(() -> factory.getAnnotation(this.aiServiceClass, InputGuardrails.class).map(this::computeInputGuardrails).orElseGet(this.defaultInputGuardrailSupplier::get));
    }

    private <MethodKey> OutputGuardrailExecutor computeOutputGuardrailsForAiServiceMethod(MethodKey method, ClassMetadataProviderFactory<MethodKey> factory) {
        if (this.outputGuardrailsAndConfigSetOnBuilder()) {
            return this.defaultOutputGuardrailSupplier.get();
        }
        return factory.getAnnotation(method, OutputGuardrails.class).map(this::computeOutputGuardrails).orElseGet(() -> factory.getAnnotation(this.aiServiceClass, OutputGuardrails.class).map(this::computeOutputGuardrails).orElseGet(this.defaultOutputGuardrailSupplier::get));
    }

    private boolean hasInputGuardrailsSetOnBuilder() {
        return !this.inputGuardrails.isEmpty() || !this.inputGuardrailClasses.isEmpty();
    }

    private boolean hasInputGuardrailConfigSetOnBuilder() {
        return this.inputGuardrailsConfig != null;
    }

    private boolean hasOutputGuardrailsSetOnBuilder() {
        return !this.outputGuardrails.isEmpty() || !this.outputGuardrailClasses.isEmpty();
    }

    private boolean hasOutputGuardrailConfigSetOnBuilder() {
        return this.outputGuardrailsConfig != null;
    }

    private boolean inputGuardrailsAndConfigSetOnBuilder() {
        return this.hasInputGuardrailsSetOnBuilder() && this.hasInputGuardrailConfigSetOnBuilder();
    }

    private boolean outputGuardrailsAndConfigSetOnBuilder() {
        return this.hasOutputGuardrailsSetOnBuilder() && this.hasOutputGuardrailConfigSetOnBuilder();
    }
}

