/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonAutoDetect$Visibility
 *  com.fasterxml.jackson.annotation.JsonTypeInfo
 *  com.fasterxml.jackson.annotation.JsonTypeInfo$As
 *  com.fasterxml.jackson.annotation.JsonTypeInfo$Id
 *  com.fasterxml.jackson.annotation.PropertyAccessor
 *  com.fasterxml.jackson.core.JsonGenerator
 *  com.fasterxml.jackson.core.JsonParser
 *  com.fasterxml.jackson.core.JsonProcessingException
 *  com.fasterxml.jackson.databind.AnnotationIntrospector
 *  com.fasterxml.jackson.databind.DeserializationContext
 *  com.fasterxml.jackson.databind.DeserializationFeature
 *  com.fasterxml.jackson.databind.JavaType
 *  com.fasterxml.jackson.databind.JsonDeserializer
 *  com.fasterxml.jackson.databind.JsonNode
 *  com.fasterxml.jackson.databind.JsonSerializer
 *  com.fasterxml.jackson.databind.MapperFeature
 *  com.fasterxml.jackson.databind.Module
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.fasterxml.jackson.databind.SerializationFeature
 *  com.fasterxml.jackson.databind.SerializerProvider
 *  com.fasterxml.jackson.databind.cfg.MapperConfig
 *  com.fasterxml.jackson.databind.introspect.Annotated
 *  com.fasterxml.jackson.databind.introspect.AnnotatedClass
 *  com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair
 *  com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector
 *  com.fasterxml.jackson.databind.json.JsonMapper
 *  com.fasterxml.jackson.databind.json.JsonMapper$Builder
 *  com.fasterxml.jackson.databind.jsontype.NamedType
 *  com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder
 *  com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder
 *  com.fasterxml.jackson.databind.module.SimpleModule
 *  com.fasterxml.jackson.databind.ser.std.StdSerializer
 */
package dev.langchain4j.internal;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import dev.langchain4j.Internal;
import dev.langchain4j.internal.Json;
import dev.langchain4j.internal.PolymorphicTypes;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Internal
class JacksonJsonCodec
implements Json.JsonCodec {
    private final ObjectMapper objectMapper;

    private static ObjectMapper createObjectMapper() {
        SimpleModule module = new SimpleModule("langchain4j-module");
        module.addSerializer(LocalDate.class, (JsonSerializer)new StdSerializer<LocalDate>(LocalDate.class){

            public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeString(value.format(DateTimeFormatter.ISO_LOCAL_DATE));
            }
        });
        module.addDeserializer(LocalDate.class, (JsonDeserializer)new JsonDeserializer<LocalDate>(){

            public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                JsonNode node = (JsonNode)p.getCodec().readTree(p);
                if (node.isObject()) {
                    int year = node.get("year").asInt();
                    int month = node.get("month").asInt();
                    int day = node.get("day").asInt();
                    return LocalDate.of(year, month, day);
                }
                return LocalDate.parse(node.asText(), DateTimeFormatter.ISO_LOCAL_DATE);
            }
        });
        module.addSerializer(LocalTime.class, (JsonSerializer)new StdSerializer<LocalTime>(LocalTime.class){

            public void serialize(LocalTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeString(value.format(DateTimeFormatter.ISO_LOCAL_TIME));
            }
        });
        module.addDeserializer(LocalTime.class, (JsonDeserializer)new JsonDeserializer<LocalTime>(){

            public LocalTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                JsonNode node = (JsonNode)p.getCodec().readTree(p);
                if (node.isObject()) {
                    int hour = node.get("hour").asInt();
                    int minute = node.get("minute").asInt();
                    int second = Optional.ofNullable(node.get("second")).map(JsonNode::asInt).orElse(0);
                    int nano = Optional.ofNullable(node.get("nano")).map(JsonNode::asInt).orElse(0);
                    return LocalTime.of(hour, minute, second, nano);
                }
                return LocalTime.parse(node.asText(), DateTimeFormatter.ISO_LOCAL_TIME);
            }
        });
        module.addSerializer(LocalDateTime.class, (JsonSerializer)new StdSerializer<LocalDateTime>(LocalDateTime.class){

            public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeString(value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            }
        });
        module.addDeserializer(LocalDateTime.class, (JsonDeserializer)new JsonDeserializer<LocalDateTime>(){

            public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                JsonNode node = (JsonNode)p.getCodec().readTree(p);
                if (node.isObject()) {
                    JsonNode date = node.get("date");
                    int year = date.get("year").asInt();
                    int month = date.get("month").asInt();
                    int day = date.get("day").asInt();
                    JsonNode time = node.get("time");
                    int hour = time.get("hour").asInt();
                    int minute = time.get("minute").asInt();
                    int second = Optional.ofNullable(time.get("second")).map(JsonNode::asInt).orElse(0);
                    int nano = Optional.ofNullable(time.get("nano")).map(JsonNode::asInt).orElse(0);
                    return LocalDateTime.of(year, month, day, hour, minute, second, nano);
                }
                return LocalDateTime.parse(node.asText(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            }
        });
        ObjectMapper mapper = ((JsonMapper)((JsonMapper.Builder)((JsonMapper.Builder)((JsonMapper.Builder)((JsonMapper.Builder)JsonMapper.builder().visibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)).disable(new SerializationFeature[]{SerializationFeature.INDENT_OUTPUT})).enable(new DeserializationFeature[]{DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES})).enable(new MapperFeature[]{MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS})).build()).findAndRegisterModules().registerModule((Module)module);
        mapper.setAnnotationIntrospector(AnnotationIntrospectorPair.pair((AnnotationIntrospector)new SealedTypePolymorphicIntrospector(), (AnnotationIntrospector)mapper.getDeserializationConfig().getAnnotationIntrospector()));
        return mapper;
    }

    public JacksonJsonCodec(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public JacksonJsonCodec() {
        this(JacksonJsonCodec.createObjectMapper());
    }

    @Override
    public String toJson(Object o) {
        try {
            return this.objectMapper.writeValueAsString(o);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T fromJson(String json, Class<T> type) {
        try {
            return (T)this.objectMapper.readValue(json, type);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T fromJson(String json, Type type) {
        try {
            return (T)this.objectMapper.readValue(json, this.objectMapper.constructType(type));
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public ObjectMapper getObjectMapper() {
        return this.objectMapper;
    }

    private static final class SealedTypePolymorphicIntrospector
    extends NopAnnotationIntrospector {
        private SealedTypePolymorphicIntrospector() {
        }

        public TypeResolverBuilder<?> findTypeResolver(MapperConfig<?> config, AnnotatedClass ac, JavaType baseType) {
            Class raw = ac.getRawType();
            if (!SealedTypePolymorphicIntrospector.shouldHandle(raw)) {
                return null;
            }
            StdTypeResolverBuilder builder = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.NAME, null).inclusion(JsonTypeInfo.As.PROPERTY).typeProperty(PolymorphicTypes.discriminatorPropertyName(raw)).typeIdVisibility(false);
            return builder;
        }

        public List<NamedType> findSubtypes(Annotated a) {
            Class raw = a.getRawType();
            if (!SealedTypePolymorphicIntrospector.shouldHandle(raw)) {
                return null;
            }
            return PolymorphicTypes.findConcreteSubtypes(raw).stream().map(sub -> new NamedType(sub, PolymorphicTypes.discriminatorValue(raw, sub))).collect(Collectors.toList());
        }

        private static boolean shouldHandle(Class<?> raw) {
            return raw.getAnnotation(JsonTypeInfo.class) == null && PolymorphicTypes.isPolymorphic(raw);
        }
    }
}

