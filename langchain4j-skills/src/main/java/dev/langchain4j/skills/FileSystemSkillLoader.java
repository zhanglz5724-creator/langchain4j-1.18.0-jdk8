/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Experimental
 *  dev.langchain4j.internal.Exceptions
 *  dev.langchain4j.internal.Utils
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.skills;

import dev.langchain4j.Experimental;
import dev.langchain4j.internal.Exceptions;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.skills.DefaultFileSystemSkill;
import dev.langchain4j.skills.DefaultSkillResource;
import dev.langchain4j.skills.FileSystemSkill;
import dev.langchain4j.skills.SkillLoaderCommon;
import dev.langchain4j.skills.SkillResource;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Experimental
public class FileSystemSkillLoader {
    private static final Logger log = LoggerFactory.getLogger(FileSystemSkillLoader.class);

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static List<FileSystemSkill> loadSkills(Path directory) {
        try (Stream<Path> entries = Files.list(directory);){
            List<FileSystemSkill> list = entries.filter(x$0 -> Files.isDirectory(x$0, new LinkOption[0])).filter(dir -> Files.exists(dir.resolve("SKILL.md"), new LinkOption[0])).map(FileSystemSkillLoader::loadSkill).collect(Collectors.toList());
            return list;
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to load skills from " + directory, e);
        }
    }

    public static FileSystemSkill loadSkill(Path skillDirectory) {
        Path skillFile = skillDirectory.resolve("SKILL.md");
        if (!Files.exists(skillFile, new LinkOption[0])) {
            throw new IllegalArgumentException("SKILL.md not found in " + skillDirectory);
        }
        String markdown = (String)Exceptions.unchecked(() -> new String(Files.readAllBytes(skillFile), StandardCharsets.UTF_8));
        Map<String, List<String>> frontMatter = SkillLoaderCommon.parseFrontMatter(markdown);
        String content = SkillLoaderCommon.extractContent(markdown);
        String name = SkillLoaderCommon.getSingle(frontMatter, "name");
        String description = SkillLoaderCommon.getSingle(frontMatter, "description");
        List<DefaultSkillResource> resources = FileSystemSkillLoader.loadResources(skillDirectory);
        return ((DefaultFileSystemSkill.Builder)((DefaultFileSystemSkill.Builder)((DefaultFileSystemSkill.Builder)((DefaultFileSystemSkill.Builder)DefaultFileSystemSkill.builder().name(name)).description(description)).content(content)).resources(resources)).basePath(skillDirectory).build();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static List<DefaultSkillResource> loadResources(Path skillDirectory) {
        try (Stream<Path> files = Files.walk(skillDirectory, new FileVisitOption[0]);){
            List<DefaultSkillResource> list = files.filter(x$0 -> Files.isRegularFile(x$0, new LinkOption[0])).filter(path -> !path.getFileName().toString().equals("SKILL.md")).filter(path -> !skillDirectory.relativize((Path)path).startsWith("scripts")).map(path -> {
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
                catch (Exception e) {
                    throw new RuntimeException("Failed to load skill resource from " + path, e);
                }
            }).filter(Objects::nonNull).collect(Collectors.toList());
            return list;
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to load skill resources from " + skillDirectory, e);
        }
    }
}

