/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.rag.content.aggregator;

import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.rag.content.Content;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

public class ReciprocalRankFuser {
    public static List<Content> fuse(Collection<List<Content>> listsOfContents) {
        return ReciprocalRankFuser.fuse(listsOfContents, 60);
    }

    public static List<Content> fuse(Collection<List<Content>> listsOfContents, int k) {
        ValidationUtils.ensureBetween(k, 1, Integer.MAX_VALUE, "k");
        LinkedHashMap<Content, Double> scores = new LinkedHashMap<Content, Double>();
        for (List<Content> singleListOfContent : listsOfContents) {
            for (int i = 0; i < singleListOfContent.size(); ++i) {
                Content content = singleListOfContent.get(i);
                double currentScore = scores.getOrDefault(content, 0.0);
                int rank = i + 1;
                double newScore = currentScore + 1.0 / (double)(k + rank);
                scores.put(content, newScore);
            }
        }
        ArrayList<Content> fused = new ArrayList<Content>(scores.keySet());
        fused.sort(Comparator.comparingDouble(scores::get).reversed());
        return fused;
    }
}

