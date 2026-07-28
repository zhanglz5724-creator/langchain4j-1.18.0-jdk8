/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 */
package dev.langchain4j.service.output;

import dev.langchain4j.Internal;
import dev.langchain4j.service.output.BigDecimalOutputParser;
import dev.langchain4j.service.output.BigIntegerOutputParser;
import dev.langchain4j.service.output.BooleanOutputParser;
import dev.langchain4j.service.output.ByteOutputParser;
import dev.langchain4j.service.output.DateOutputParser;
import dev.langchain4j.service.output.DoubleOutputParser;
import dev.langchain4j.service.output.EnumListOutputParser;
import dev.langchain4j.service.output.EnumOutputParser;
import dev.langchain4j.service.output.EnumSetOutputParser;
import dev.langchain4j.service.output.FloatOutputParser;
import dev.langchain4j.service.output.IntegerOutputParser;
import dev.langchain4j.service.output.LocalDateOutputParser;
import dev.langchain4j.service.output.LocalDateTimeOutputParser;
import dev.langchain4j.service.output.LocalTimeOutputParser;
import dev.langchain4j.service.output.LongOutputParser;
import dev.langchain4j.service.output.OutputParser;
import dev.langchain4j.service.output.OutputParserFactory;
import dev.langchain4j.service.output.PojoListOutputParser;
import dev.langchain4j.service.output.PojoOutputParser;
import dev.langchain4j.service.output.PojoSetOutputParser;
import dev.langchain4j.service.output.ShortOutputParser;
import dev.langchain4j.service.output.StringListOutputParser;
import dev.langchain4j.service.output.StringSetOutputParser;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Internal
class DefaultOutputParserFactory
implements OutputParserFactory {
    private static final Map<Class<?>, OutputParser<?>> OUTPUT_PARSERS = new HashMap();

    DefaultOutputParserFactory() {
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public OutputParser<?> get(Class<?> rawClass, Class<?> typeArgumentClass) {
        if (rawClass.isEnum()) {
            return new EnumOutputParser(rawClass);
        }
        if (rawClass.equals(List.class)) {
            if (typeArgumentClass.isEnum()) {
                return new EnumListOutputParser(typeArgumentClass);
            }
            if (typeArgumentClass.equals(String.class)) {
                return new StringListOutputParser();
            }
            return new PojoListOutputParser(typeArgumentClass);
        }
        if (rawClass.equals(Set.class)) {
            if (typeArgumentClass.isEnum()) {
                return new EnumSetOutputParser(typeArgumentClass);
            }
            if (typeArgumentClass.equals(String.class)) {
                return new StringSetOutputParser();
            }
            return new PojoSetOutputParser(typeArgumentClass);
        }
        OutputParser<?> outputParser = OUTPUT_PARSERS.get(rawClass);
        if (outputParser != null) {
            return outputParser;
        }
        return new PojoOutputParser(rawClass);
    }

    static {
        OUTPUT_PARSERS.put(Boolean.TYPE, new BooleanOutputParser());
        OUTPUT_PARSERS.put(Boolean.class, new BooleanOutputParser());
        OUTPUT_PARSERS.put(Byte.TYPE, new ByteOutputParser());
        OUTPUT_PARSERS.put(Byte.class, new ByteOutputParser());
        OUTPUT_PARSERS.put(Short.TYPE, new ShortOutputParser());
        OUTPUT_PARSERS.put(Short.class, new ShortOutputParser());
        OUTPUT_PARSERS.put(Integer.TYPE, new IntegerOutputParser());
        OUTPUT_PARSERS.put(Integer.class, new IntegerOutputParser());
        OUTPUT_PARSERS.put(Long.TYPE, new LongOutputParser());
        OUTPUT_PARSERS.put(Long.class, new LongOutputParser());
        OUTPUT_PARSERS.put(BigInteger.class, new BigIntegerOutputParser());
        OUTPUT_PARSERS.put(Float.TYPE, new FloatOutputParser());
        OUTPUT_PARSERS.put(Float.class, new FloatOutputParser());
        OUTPUT_PARSERS.put(Double.TYPE, new DoubleOutputParser());
        OUTPUT_PARSERS.put(Double.class, new DoubleOutputParser());
        OUTPUT_PARSERS.put(BigDecimal.class, new BigDecimalOutputParser());
        OUTPUT_PARSERS.put(Date.class, new DateOutputParser());
        OUTPUT_PARSERS.put(LocalDate.class, new LocalDateOutputParser());
        OUTPUT_PARSERS.put(LocalTime.class, new LocalTimeOutputParser());
        OUTPUT_PARSERS.put(LocalDateTime.class, new LocalDateTimeOutputParser());
    }
}

