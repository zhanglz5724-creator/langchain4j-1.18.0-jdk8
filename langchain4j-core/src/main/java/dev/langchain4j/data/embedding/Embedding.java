/*
 * Decompiled with CFR 0.152.
 */
package dev.langchain4j.data.embedding;

import dev.langchain4j.internal.ValidationUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Embedding {
    private final float[] vector;

    public Embedding(float[] vector) {
        this.vector = ValidationUtils.ensureNotNull(vector, "vector");
    }

    public float[] vector() {
        return this.vector;
    }

    public List<Float> vectorAsList() {
        ArrayList<Float> list = new ArrayList<Float>(this.vector.length);
        for (float f : this.vector) {
            list.add(Float.valueOf(f));
        }
        return list;
    }

    public double[] vectorAsDoubleArray() {
        double[] doubles = new double[this.vector.length];
        for (int i = 0; i < this.vector.length; ++i) {
            doubles[i] = this.vector[i];
        }
        return doubles;
    }

    public void normalize() {
        double norm = 0.0;
        for (float f : this.vector) {
            norm += (double)(f * f);
        }
        if (Math.abs(norm = Math.sqrt(norm)) < 1.0E-10) {
            return;
        }
        int i = 0;
        while (i < this.vector.length) {
            int n = i++;
            this.vector[n] = this.vector[n] / (float)norm;
        }
    }

    public int dimension() {
        return this.vector.length;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Embedding that = (Embedding)o;
        return Arrays.equals(this.vector, that.vector);
    }

    public int hashCode() {
        return Arrays.hashCode(this.vector);
    }

    public String toString() {
        return "Embedding { vector = " + Arrays.toString(this.vector) + " }";
    }

    public static Embedding from(float[] vector) {
        return new Embedding(vector);
    }

    public static Embedding from(List<Float> vector) {
        float[] array = new float[vector.size()];
        for (int i = 0; i < vector.size(); ++i) {
            array[i] = vector.get(i).floatValue();
        }
        return new Embedding(array);
    }
}

