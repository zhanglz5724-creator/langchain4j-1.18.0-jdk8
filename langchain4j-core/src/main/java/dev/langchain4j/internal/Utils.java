/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NonNull
 *  org.jspecify.annotations.Nullable
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.internal;

import dev.langchain4j.Internal;
import dev.langchain4j.internal.Exceptions;
import dev.langchain4j.internal.JacocoIgnoreCoverageGenerated;
import dev.langchain4j.internal.ValidationUtils;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Internal
public class Utils {
    private static final Logger log = LoggerFactory.getLogger(Utils.class);

    private Utils() {
    }

    @SafeVarargs
    public static <T> @NonNull T firstNotNull(@NonNull String name, T ... values) {
        ValidationUtils.ensureNotEmpty(values, name + " values");
        for (T value : values) {
            if (value == null) continue;
            return value;
        }
        throw Exceptions.illegalArgument("At least one of the given '%s' values must be not null", name);
    }

    public static <T> T getOrDefault(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    public static <T> List<T> getOrDefault(List<T> list, List<T> defaultList) {
        return Utils.isNullOrEmpty(list) ? defaultList : list;
    }

    public static <K, V> Map<K, V> getOrDefault(Map<K, V> map, Map<K, V> defaultMap) {
        return Utils.isNullOrEmpty(map) ? defaultMap : map;
    }

    public static <T> T getOrDefault(@Nullable T value, Supplier<T> defaultValueSupplier) {
        return value != null ? value : defaultValueSupplier.get();
    }

    public static boolean isNullOrBlank(String string) {
        return string == null || string.trim().isEmpty();
    }

    public static boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    public static boolean isNotNullOrBlank(String string) {
        return !Utils.isNullOrBlank(string);
    }

    public static boolean isNotNullOrEmpty(String string) {
        return !Utils.isNullOrEmpty(string);
    }

    public static boolean areNotNullOrBlank(String ... strings) {
        if (strings == null || strings.length == 0) {
            return false;
        }
        for (String string : strings) {
            if (!Utils.isNullOrBlank(string)) continue;
            return false;
        }
        return true;
    }

    public static boolean isNullOrEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotNullOrEmpty(Collection<?> collection) {
        return !Utils.isNullOrEmpty(collection);
    }

    public static boolean isNullOrEmpty(Iterable<?> iterable) {
        return iterable == null || !iterable.iterator().hasNext();
    }

    public static boolean isNullOrEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isNullOrEmpty(@Nullable Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static String repeat(String string, int times) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; ++i) {
            sb.append(string);
        }
        return sb.toString();
    }

    public static String randomUUID() {
        return UUID.randomUUID().toString();
    }

    @JacocoIgnoreCoverageGenerated
    private static MessageDigest getSha256Instance() {
        try {
            return MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String generateUUIDFrom(String input) {
        byte[] hashBytes = Utils.getSha256Instance().digest(input.getBytes(StandardCharsets.UTF_8));
        String hexFormat = Utils.bytesToHex(hashBytes);
        return UUID.nameUUIDFromBytes(hexFormat.getBytes(StandardCharsets.UTF_8)).toString();
    }

    public static String ensureTrailingForwardSlash(String url) {
        return url.endsWith("/") ? url : url + "/";
    }

    public static String quoted(Object object) {
        if (object == null) {
            return "null";
        }
        return "\"" + object + "\"";
    }

    public static String firstChars(String string, int numberOfChars) {
        if (string == null) {
            return null;
        }
        return string.length() > numberOfChars ? string.substring(0, numberOfChars) : string;
    }

    /*
     * Exception decompiling
     */
    public static byte[] readBytes(String url) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 4 blocks at once
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:412)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:487)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public static <T> Set<T> copyIfNotNull(Set<T> set) {
        if (set == null) {
            return null;
        }
        return Collections.unmodifiableSet(set);
    }

    public static <T> Set<T> copy(Set<T> set) {
        if (set == null) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(set);
    }

    public static <T> List<T> copyIfNotNull(List<T> list) {
        if (list == null) {
            return null;
        }
        return Collections.unmodifiableList(list);
    }

    public static <T> List<T> copy(List<? extends T> list) {
        if (list == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(list);
    }

    public static <T> List<T> mutableCopy(List<T> list) {
        if (list == null) {
            return new ArrayList();
        }
        return new ArrayList<T>(list);
    }

    public static <T> List<T> copy(Collection<? extends T> collection) {
        if (collection == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(new ArrayList<T>(collection));
    }

    public static <K, V> Map<K, V> copyIfNotNull(Map<K, V> map) {
        if (map == null) {
            return null;
        }
        return Collections.unmodifiableMap(map);
    }

    public static <K, V> Map<K, V> copy(Map<K, V> map) {
        if (map == null) {
            return Collections.emptyMap();
        }
        return Collections.unmodifiableMap(map);
    }

    public static <K, V> Map<K, V> mutableCopy(Map<K, V> map) {
        if (map == null) {
            return new HashMap();
        }
        return new HashMap<K, V>(map);
    }

    public static Map<String, String> toStringValueMap(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        HashMap<String, String> stringValueMap = new HashMap<String, String>();
        for (String key : map.keySet()) {
            Object value = map.get(key);
            String stringValue = Objects.toString(value, null);
            stringValueMap.put(key, stringValue);
        }
        return stringValueMap;
    }

    public static Optional<Method> getAnnotatedMethod(Method method, Class<? extends Annotation> annotation) {
        if (method.isAnnotationPresent(annotation)) {
            return Optional.of(method);
        }
        if (Proxy.isProxyClass(method.getDeclaringClass())) {
            for (Class<?> iface : method.getDeclaringClass().getInterfaces()) {
                try {
                    Method interfaceMethod = iface.getMethod(method.getName(), method.getParameterTypes());
                    if (interfaceMethod.isAnnotationPresent(annotation)) {
                        return Optional.of(interfaceMethod);
                    }
                }
                catch (NoSuchMethodException noSuchMethodException) {
                    // empty catch block
                }
            }
        }
        return Optional.empty();
    }

    public static List<Method> allConcreteMethods(Class<?> clazz) {
        return Utils.allMethods(clazz, true);
    }

    public static List<Method> allMethods(Class<?> clazz) {
        return Utils.allMethods(clazz, false);
    }

    private static List<Method> allMethods(Class<?> clazz, boolean concreteOnly) {
        ArrayList<Method> allMethods = new ArrayList<Method>();
        HashSet<MethodSignature> seen = new HashSet<MethodSignature>();
        for (Class<?> current = clazz; current != null && current != Object.class; current = current.getSuperclass()) {
            Utils.collectConcreteMethods(current, seen, allMethods);
        }
        Utils.collectInterfaceMethods(clazz, seen, allMethods, new HashSet(), concreteOnly);
        return Collections.unmodifiableList(new ArrayList<Method>(allMethods));
    }

    private static void collectConcreteMethods(Class<?> clazz, Set<MethodSignature> seen, List<Method> result) {
        for (Method method : clazz.getDeclaredMethods()) {
            MethodSignature sig;
            if (method.isBridge() || method.isSynthetic() || !seen.add(sig = new MethodSignature(method.getName(), Arrays.asList(method.getParameterTypes())))) continue;
            result.add(method);
        }
    }

    private static void collectInterfaceMethods(Class<?> clazz, Set<MethodSignature> seen, List<Method> result, Set<Class<?>> visited, boolean concreteOnly) {
        if (clazz == null) {
            return;
        }
        for (Class<?> iface : clazz.getInterfaces()) {
            if (!visited.add(iface)) continue;
            for (Method method : iface.getDeclaredMethods()) {
                MethodSignature sig;
                if (method.isBridge() || method.isSynthetic() || concreteOnly && Modifier.isAbstract(method.getModifiers()) || !seen.add(sig = new MethodSignature(method.getName(), Arrays.asList(method.getParameterTypes())))) continue;
                result.add(method);
            }
            Utils.collectInterfaceMethods(iface, seen, result, visited, concreteOnly);
        }
        Utils.collectInterfaceMethods(clazz.getSuperclass(), seen, result, visited, concreteOnly);
    }

    public static String warnIfNullOrBlank(String value, String fieldName, Class<?> clazz) {
        if (Utils.isNullOrBlank(value)) {
            log.warn("{}: '{}' is null or blank", (Object)clazz.getSimpleName(), (Object)fieldName);
        }
        return value;
    }

    public static String toBase64(String s) {
        if (s == null) {
            return null;
        }
        return Base64.getEncoder().encodeToString(s.getBytes(StandardCharsets.UTF_8));
    }

    public static <T> List<T> merge(List<T> ... lists) {
        if (lists.length < 2) {
            throw new IllegalArgumentException("lists must have at least 2 elements");
        }
        if (lists.length == 2) {
            if (lists[0] == null || lists[0].isEmpty()) {
                return lists[1];
            }
            if (lists[1] == null || lists[1].isEmpty()) {
                return lists[0];
            }
        }
        ArrayList<T> result = new ArrayList<T>();
        for (List<T> list : lists) {
            result.addAll(list);
        }
        return result;
    }

    public static <K, V> Map<K, V> merge(Map<K, V> ... maps) {
        if (maps.length < 2) {
            throw new IllegalArgumentException("maps must have at least 2 elements");
        }
        if (maps.length == 2) {
            if (maps[0] == null || maps[0].isEmpty()) {
                return maps[1];
            }
            if (maps[1] == null || maps[1].isEmpty()) {
                return maps[0];
            }
        }
        HashMap<K, V> result = new HashMap<K, V>();
        for (Map<K, V> map : maps) {
            for (Map.Entry<K, V> e : map.entrySet()) {
                if (result.putIfAbsent(e.getKey(), e.getValue()) == null) continue;
                throw new IllegalArgumentException("Duplicate key: " + e.getKey());
            }
        }
        return result;
    }

    public static String randomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; ++i) {
            result.append(characters.charAt(random.nextInt(characters.length())));
        }
        return result.toString();
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private static final class MethodSignature {
        private final String name;
        private final List<Class<?>> params;

        MethodSignature(String name, List<Class<?>> params) {
            this.name = name;
            this.params = params;
        }

        public String name() {
            return this.name;
        }

        public List<Class<?>> params() {
            return this.params;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof MethodSignature)) {
                return false;
            }
            MethodSignature that = (MethodSignature)o;
            return Objects.equals(this.name, that.name) && Objects.equals(this.params, that.params);
        }

        public int hashCode() {
            return Objects.hash(this.name, this.params);
        }
    }
}

