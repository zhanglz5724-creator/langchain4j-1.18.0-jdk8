/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
 *  dev.langchain4j.internal.ValidationUtils
 */
package dev.langchain4j.data.document.splitter;

import dev.langchain4j.Internal;
import dev.langchain4j.internal.ValidationUtils;
import java.util.function.Function;

@Internal
class SegmentBuilder {
    private final int maxSegmentSize;
    private final Function<String, Integer> sizeFunction;
    private final String joinSeparator;
    private final int joinSeparatorSize;
    private StringBuilder segment = new StringBuilder();
    private int segmentSize = 0;

    public SegmentBuilder(int maxSegmentSize, Function<String, Integer> sizeFunction, String joinSeparator) {
        this.maxSegmentSize = ValidationUtils.ensureGreaterThanZero((Integer)maxSegmentSize, (String)"maxSegmentSize");
        this.sizeFunction = (Function)ValidationUtils.ensureNotNull(sizeFunction, (String)"sizeFunction");
        this.joinSeparator = (String)ValidationUtils.ensureNotNull((Object)joinSeparator, (String)"joinSeparator");
        this.joinSeparatorSize = this.sizeOf(joinSeparator);
    }

    public int getSize() {
        return this.segmentSize;
    }

    public boolean hasSpaceFor(String text) {
        int totalSize = this.sizeOf(text);
        if (this.isNotEmpty()) {
            totalSize += this.segmentSize + this.joinSeparatorSize;
        }
        return totalSize <= this.maxSegmentSize;
    }

    public boolean hasSpaceFor(int size) {
        int totalSize = size;
        if (this.isNotEmpty()) {
            totalSize += this.segmentSize + this.joinSeparatorSize;
        }
        return totalSize <= this.maxSegmentSize;
    }

    public int sizeOf(String text) {
        return this.sizeFunction.apply(text);
    }

    public void append(String text) {
        if (this.isNotEmpty()) {
            this.segment.append(this.joinSeparator);
        }
        this.segment.append(text);
        this.segmentSize = this.sizeOf(this.segment.toString());
    }

    public void prepend(String text) {
        if (this.isNotEmpty()) {
            this.segment.insert(0, this.joinSeparator).insert(0, text);
        } else {
            this.segment.replace(0, this.segment.length(), text);
        }
        this.segmentSize = this.sizeOf(this.segment.toString());
    }

    public boolean isNotEmpty() {
        return this.segment.length() > 0;
    }

    public String toString() {
        return this.segment.toString().trim();
    }

    public void reset() {
        this.segment.setLength(0);
        this.segmentSize = 0;
    }
}

