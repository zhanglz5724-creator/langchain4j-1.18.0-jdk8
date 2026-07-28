/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.guardrail.GuardrailResult
 *  dev.langchain4j.guardrail.GuardrailResult$Failure
 *  dev.langchain4j.guardrail.GuardrailResult$Result
 *  dev.langchain4j.internal.ValidationUtils
 *  org.assertj.core.api.AbstractObjectAssert
 *  org.assertj.core.api.Assertions
 *  org.assertj.core.api.InstanceOfAssertFactories
 *  org.assertj.core.api.ListAssert
 *  org.assertj.core.api.ObjectAssert
 */
package dev.langchain4j.test.guardrail;

import dev.langchain4j.guardrail.GuardrailResult;
import dev.langchain4j.internal.ValidationUtils;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.assertj.core.api.ListAssert;
import org.assertj.core.api.ObjectAssert;

public abstract class GuardrailResultAssert<A extends GuardrailResultAssert<A, R, F>, R extends GuardrailResult<R>, F extends GuardrailResult.Failure>
extends AbstractObjectAssert<A, R> {
    private final Class<F> failureClass;

    protected GuardrailResultAssert(R r, Class<A> resultType, Class<F> failureClass) {
        super(r, resultType);
        this.failureClass = failureClass;
    }

    public A hasResult(GuardrailResult.Result result) {
        this.isNotNull();
        if (!Objects.equals(((GuardrailResult)this.actual).result(), result)) {
            throw this.failureWithActualExpected(((GuardrailResult)this.actual).result(), result, "Expected result to be <%s> but was <%s>", new Object[]{result, ((GuardrailResult)this.actual).result()});
        }
        return (A)((Object)this);
    }

    public A hasSuccessfulText(String successfulText) {
        this.isSuccessful();
        if (!Objects.equals(((GuardrailResult)this.actual).successfulText(), successfulText)) {
            throw this.failureWithActualExpected(((GuardrailResult)this.actual).successfulText(), successfulText, "Expected successful text to be <%s> but was <%s>", new Object[]{successfulText, ((GuardrailResult)this.actual).successfulText()});
        }
        return (A)((Object)this);
    }

    public A isSuccessful() {
        this.isNotNull();
        if (!((GuardrailResult)this.actual).isSuccess()) {
            throw this.failure("Expected result to be successful but was <%s>", new Object[]{((GuardrailResult)this.actual).result()});
        }
        return (A)((Object)this);
    }

    public A hasFailures() {
        this.isNotNull();
        this.withFailures().isNotEmpty();
        return (A)((Object)this);
    }

    public A hasSingleFailureWithMessage(String expectedFailureMessage) {
        this.isNotNull();
        ((ObjectAssert)this.withFailures().singleElement()).extracting("message").isEqualTo((Object)expectedFailureMessage);
        return (A)((Object)this);
    }

    public A assertSingleFailureSatisfies(Consumer<F> requirements) {
        this.isNotNull();
        ValidationUtils.ensureNotNull(requirements, (String)"requirements");
        ((ObjectAssert)this.withFailures().singleElement()).satisfies(new Consumer[]{requirements});
        return (A)((Object)this);
    }

    public ListAssert<F> withFailures() {
        return (ListAssert)((ListAssert)Assertions.assertThat((List)((GuardrailResult)this.actual).failures()).isNotNull()).asInstanceOf(InstanceOfAssertFactories.list(this.failureClass));
    }
}

