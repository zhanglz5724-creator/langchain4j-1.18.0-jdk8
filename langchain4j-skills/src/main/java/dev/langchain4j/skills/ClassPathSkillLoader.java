/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.internal.Utils
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.skills;

import dev.langchain4j.Experimental;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.skills.DefaultFileSystemSkill;
import dev.langchain4j.skills.DefaultSkill;
import dev.langchain4j.skills.DefaultSkillResource;
import dev.langchain4j.skills.Skill;
import dev.langchain4j.skills.SkillLoaderCommon;
import dev.langchain4j.skills.SkillResource;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Experimental
public class ClassPathSkillLoader {
    private static final Logger log = LoggerFactory.getLogger(ClassPathSkillLoader.class);

    private ClassPathSkillLoader() {
    }

    public static List<Skill> loadSkills(String directoryOnClasspath) {
        return ClassPathSkillLoader.loadSkills(directoryOnClasspath, ClassPathSkillLoader.getDefaultClassLoader());
    }

    /*
     * Exception decompiling
     */
    public static List<Skill> loadSkills(String directoryOnClasspath, ClassLoader classLoader) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
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

    public static Skill loadSkill(String skillDirectoryOnClasspath) {
        return ClassPathSkillLoader.loadSkill(skillDirectoryOnClasspath, ClassPathSkillLoader.getDefaultClassLoader());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static Skill loadSkill(String skillDirectoryOnClasspath, ClassLoader classLoader) {
        ResolvedDirectory resolved = ClassPathSkillLoader.resolveClasspathDirectory(skillDirectoryOnClasspath, classLoader);
        try {
            Skill skill = ClassPathSkillLoader.loadSkillFromPath(resolved);
            return skill;
        }
        finally {
            ClassPathSkillLoader.closeJarFileSystem(resolved);
        }
    }

    private static Skill loadSkillFromPath(ResolvedDirectory skillDirectory) {
        Path skillFile = skillDirectory.path.resolve("SKILL.md");
        if (!Files.exists(skillFile, new LinkOption[0])) {
            throw new IllegalArgumentException("SKILL.md not found in " + skillDirectory);
        }
        try {
            String markdown = new String(Files.readAllBytes(skillFile), StandardCharsets.UTF_8);
            Map<String, List<String>> frontMatter = SkillLoaderCommon.parseFrontMatter(markdown);
            String content = SkillLoaderCommon.extractContent(markdown);
            String name = SkillLoaderCommon.getSingle(frontMatter, "name");
            String description = SkillLoaderCommon.getSingle(frontMatter, "description");
            List<DefaultSkillResource> resources = ClassPathSkillLoader.loadResources(skillDirectory.path);
            if (skillDirectory.jarFileSystem != null) {
                return ((DefaultSkill.Builder)((DefaultSkill.Builder)((DefaultSkill.Builder)((DefaultSkill.Builder)DefaultSkill.builder().name(name)).description(description)).content(content)).resources(resources)).build();
            }
            return ((DefaultFileSystemSkill.Builder)((DefaultFileSystemSkill.Builder)((DefaultFileSystemSkill.Builder)((DefaultFileSystemSkill.Builder)DefaultFileSystemSkill.builder().name(name)).description(description)).content(content)).resources(resources)).basePath(skillDirectory.path).build();
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to load skill from " + skillDirectory, e);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static List<DefaultSkillResource> loadResources(Path skillDirectory) {
        try (Stream<Path> files = Files.walk(skillDirectory, new FileVisitOption[0]);){
            List<DefaultSkillResource> list = files.filter(x$0 -> Files.isRegularFile(x$0, new LinkOption[0])).filter(path -> !path.getFileName().toString().equals("SKILL.md")).filter(path -> {
                String relativePath = StreamSupport.stream(skillDirectory.relativize((Path)path).spliterator(), false).map(Path::toString).collect(Collectors.joining("/"));
                return !relativePath.startsWith("scripts");
            }).map(path -> {
                try {
                    String content = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
                    if (Utils.isNullOrBlank((String)content)) {
                        return null;
                    }
                    String relativePath = StreamSupport.stream(skillDirectory.relativize((Path)path).spliterator(), false).map(Path::toString).collect(Collectors.joining("/"));
                    return SkillResource.builder().relativePath(relativePath).content(content).build();
                }
                catch (MalformedInputException e) {
                    log.warn("Skipping binary file that cannot be read as UTF-8 text: {}", path);
                    return null;
                }
                catch (IOException e) {
                    throw new RuntimeException("Failed to load skill resource from " + path, e);
                }
            }).filter(Objects::nonNull).collect(Collectors.toList());
            return list;
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to load skill resources from " + skillDirectory, e);
        }
    }

    private static ResolvedDirectory resolveClasspathDirectory(String pathOnClasspath, ClassLoader classLoader) {
        URL url = classLoader.getResource(pathOnClasspath);
        if (url == null) {
            throw new IllegalArgumentException("Classpath resource not found: " + pathOnClasspath);
        }
        try {
            URI uri = url.toURI();
            if ("jar".equals(uri.getScheme())) {
                return ClassPathSkillLoader.resolveJarPath(uri);
            }
            return new ResolvedDirectory(Paths.get(uri), null, false);
        }
        catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Failed to resolve classpath resource: " + pathOnClasspath, e);
        }
    }

    private static ResolvedDirectory resolveJarPath(URI jarUri) throws IOException {
        boolean created;
        FileSystem fs;
        String schemeSpecific = jarUri.getSchemeSpecificPart();
        int separator = schemeSpecific.indexOf("!/");
        if (separator == -1) {
            throw new IllegalArgumentException("Invalid JAR URI: " + jarUri);
        }
        String pathInJar = schemeSpecific.substring(separator + 1);
        try {
            fs = FileSystems.newFileSystem(jarUri, Collections.emptyMap());
            created = true;
        }
        catch (FileSystemAlreadyExistsException e) {
            fs = FileSystems.getFileSystem(jarUri);
            created = false;
        }
        return new ResolvedDirectory(fs.getPath(pathInJar, new String[0]), fs, created);
    }

    private static void closeJarFileSystem(ResolvedDirectory resolvedDirectory) {
        if (resolvedDirectory.shouldCloseFileSystemAfterLoading() && resolvedDirectory.jarFileSystem() != null) {
            try {
                resolvedDirectory.jarFileSystem().close();
            }
            catch (IOException e) {
                log.warn("Failed to close JAR filesystem", (Throwable)e);
            }
        }
    }

    private static ClassLoader getDefaultClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    private static /* synthetic */ Skill lambda$loadSkills$2(ResolvedDirectory resolved, Path skillDirectory) {
        return ClassPathSkillLoader.loadSkillFromPath(new ResolvedDirectory(skillDirectory, resolved.jarFileSystem, false));
    }

    private static /* synthetic */ boolean lambda$loadSkills$1(Path dir) {
        return Files.exists(dir.resolve("SKILL.md"), new LinkOption[0]);
    }

    private static /* synthetic */ boolean lambda$loadSkills$0(Path x$0) {
        return Files.isDirectory(x$0, new LinkOption[0]);
    }

    private static class ResolvedDirectory {
        final Path path;
        final FileSystem jarFileSystem;
        final boolean shouldCloseFileSystemAfterLoading;

        ResolvedDirectory(Path path, FileSystem jarFileSystem, boolean shouldCloseFileSystemAfterLoading) {
            this.path = path;
            this.jarFileSystem = jarFileSystem;
            this.shouldCloseFileSystemAfterLoading = shouldCloseFileSystemAfterLoading;
        }

        Path path() {
            return this.path;
        }

        FileSystem jarFileSystem() {
            return this.jarFileSystem;
        }

        boolean shouldCloseFileSystemAfterLoading() {
            return this.shouldCloseFileSystemAfterLoading;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof ResolvedDirectory)) {
                return false;
            }
            ResolvedDirectory that = (ResolvedDirectory)o;
            return this.shouldCloseFileSystemAfterLoading == that.shouldCloseFileSystemAfterLoading && Objects.equals(this.path, that.path) && Objects.equals(this.jarFileSystem, that.jarFileSystem);
        }

        public int hashCode() {
            return Objects.hash(this.path, this.jarFileSystem, this.shouldCloseFileSystemAfterLoading);
        }

        public String toString() {
            return "ResolvedDirectory[path=" + this.path + ", jarFileSystem=" + this.jarFileSystem + ", shouldCloseFileSystemAfterLoading=" + this.shouldCloseFileSystemAfterLoading + "]";
        }
    }
}

