/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.commonmark.ext.front.matter.YamlFrontMatterExtension
 *  org.commonmark.ext.front.matter.YamlFrontMatterVisitor
 *  org.commonmark.node.Node
 *  org.commonmark.node.Visitor
 *  org.commonmark.parser.Parser
 */
package dev.langchain4j.skills;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.commonmark.ext.front.matter.YamlFrontMatterExtension;
import org.commonmark.ext.front.matter.YamlFrontMatterVisitor;
import org.commonmark.node.Node;
import org.commonmark.node.Visitor;
import org.commonmark.parser.Parser;

class SkillLoaderCommon {
    static final Parser PARSER = Parser.builder().extensions(Arrays.asList(YamlFrontMatterExtension.create())).build();

    private SkillLoaderCommon() {
    }

    static Map<String, List<String>> parseFrontMatter(String markdown) {
        Node document = PARSER.parse(markdown);
        YamlFrontMatterVisitor visitor = new YamlFrontMatterVisitor();
        document.accept((Visitor)visitor);
        return visitor.getData();
    }

    static String extractContent(String markdown) {
        int secondDelimiter;
        if (markdown.startsWith("---") && (secondDelimiter = markdown.indexOf("\n---", 3)) != -1) {
            return markdown.substring(secondDelimiter + 4).trim();
        }
        return markdown;
    }

    static String getSingle(Map<String, List<String>> map, String key) {
        return map.getOrDefault(key, Arrays.asList(new String[0])).stream().findFirst().orElse(null);
    }
}

