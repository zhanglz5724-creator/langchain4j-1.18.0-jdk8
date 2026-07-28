/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.agentic.supervisor;

public class ResponseScore {
    private double score1;
    private double score2;

    public double getScore1() {
        return this.score1;
    }

    public void setScore1(double score1) {
        this.score1 = score1;
    }

    public double getScore2() {
        return this.score2;
    }

    public void setScore2(double score2) {
        this.score2 = score2;
    }

    public String toString() {
        return "ResponseScore{score1=" + this.score1 + ", score2=" + this.score2 + '}';
    }
}

