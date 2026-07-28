/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.oracle.coherence.ai.DocumentChunk
 *  com.tangosol.util.Extractors
 *  com.tangosol.util.Filter
 *  com.tangosol.util.Filters
 *  com.tangosol.util.ValueExtractor
 *  dev.langchain4j.store.embedding.filter.Filter
 *  dev.langchain4j.store.embedding.filter.comparison.IsEqualTo
 *  dev.langchain4j.store.embedding.filter.comparison.IsGreaterThan
 *  dev.langchain4j.store.embedding.filter.comparison.IsGreaterThanOrEqualTo
 *  dev.langchain4j.store.embedding.filter.comparison.IsIn
 *  dev.langchain4j.store.embedding.filter.comparison.IsLessThan
 *  dev.langchain4j.store.embedding.filter.comparison.IsLessThanOrEqualTo
 *  dev.langchain4j.store.embedding.filter.comparison.IsNotEqualTo
 *  dev.langchain4j.store.embedding.filter.comparison.IsNotIn
 *  dev.langchain4j.store.embedding.filter.logical.And
 *  dev.langchain4j.store.embedding.filter.logical.Not
 *  dev.langchain4j.store.embedding.filter.logical.Or
 */
package dev.langchain4j.store.embedding.coherence;

import com.oracle.coherence.ai.DocumentChunk;
import com.tangosol.util.Extractors;
import com.tangosol.util.Filter;
import com.tangosol.util.Filters;
import com.tangosol.util.ValueExtractor;
import dev.langchain4j.store.embedding.filter.comparison.IsEqualTo;
import dev.langchain4j.store.embedding.filter.comparison.IsGreaterThan;
import dev.langchain4j.store.embedding.filter.comparison.IsGreaterThanOrEqualTo;
import dev.langchain4j.store.embedding.filter.comparison.IsIn;
import dev.langchain4j.store.embedding.filter.comparison.IsLessThan;
import dev.langchain4j.store.embedding.filter.comparison.IsLessThanOrEqualTo;
import dev.langchain4j.store.embedding.filter.comparison.IsNotEqualTo;
import dev.langchain4j.store.embedding.filter.comparison.IsNotIn;
import dev.langchain4j.store.embedding.filter.logical.And;
import dev.langchain4j.store.embedding.filter.logical.Not;
import dev.langchain4j.store.embedding.filter.logical.Or;
import java.util.Collection;
import java.util.HashSet;

class CoherenceMetadataFilterMapper {
    CoherenceMetadataFilterMapper() {
    }

    static Filter<DocumentChunk> map(dev.langchain4j.store.embedding.filter.Filter filter) {
        if (filter == null) {
            return null;
        }
        if (filter instanceof IsEqualTo) {
            IsEqualTo equalTo = (IsEqualTo)filter;
            return CoherenceMetadataFilterMapper.mapEqual(equalTo);
        }
        if (filter instanceof IsNotEqualTo) {
            IsNotEqualTo notEqualTo = (IsNotEqualTo)filter;
            return CoherenceMetadataFilterMapper.mapNotEqual(notEqualTo);
        }
        if (filter instanceof IsGreaterThan) {
            IsGreaterThan greaterThan = (IsGreaterThan)filter;
            return CoherenceMetadataFilterMapper.mapGreaterThan(greaterThan);
        }
        if (filter instanceof IsGreaterThanOrEqualTo) {
            IsGreaterThanOrEqualTo greaterThanOrEqualTo = (IsGreaterThanOrEqualTo)filter;
            return CoherenceMetadataFilterMapper.mapGreaterThanOrEqual(greaterThanOrEqualTo);
        }
        if (filter instanceof IsLessThan) {
            IsLessThan lessThan = (IsLessThan)filter;
            return CoherenceMetadataFilterMapper.mapLessThan(lessThan);
        }
        if (filter instanceof IsLessThanOrEqualTo) {
            IsLessThanOrEqualTo lessThanOrEqualTo = (IsLessThanOrEqualTo)filter;
            return CoherenceMetadataFilterMapper.mapLessThanOrEqual(lessThanOrEqualTo);
        }
        if (filter instanceof IsIn) {
            IsIn in = (IsIn)filter;
            return CoherenceMetadataFilterMapper.mapIn(in);
        }
        if (filter instanceof IsNotIn) {
            IsNotIn notIn = (IsNotIn)filter;
            return CoherenceMetadataFilterMapper.mapNotIn(notIn);
        }
        if (filter instanceof And) {
            And and = (And)filter;
            return CoherenceMetadataFilterMapper.mapAnd(and);
        }
        if (filter instanceof Not) {
            Not not = (Not)filter;
            return CoherenceMetadataFilterMapper.mapNot(not);
        }
        if (filter instanceof Or) {
            Or or = (Or)filter;
            return CoherenceMetadataFilterMapper.mapOr(or);
        }
        throw new UnsupportedOperationException("Unsupported filter type: " + filter.getClass().getName());
    }

    private static <V> ValueExtractor<DocumentChunk, V> extractor(String field) {
        return Extractors.chained((ValueExtractor[])new ValueExtractor[]{ValueExtractor.of(DocumentChunk::metadata), Extractors.extract((String)field)});
    }

    private static Filter<DocumentChunk> mapEqual(IsEqualTo isEqualTo) {
        return Filters.equal(CoherenceMetadataFilterMapper.extractor(isEqualTo.key()), (Object)isEqualTo.comparisonValue());
    }

    private static Filter<DocumentChunk> mapNotEqual(IsNotEqualTo isNotEqualTo) {
        return Filters.not((Filter)Filters.equal(CoherenceMetadataFilterMapper.extractor(isNotEqualTo.key()), (Object)isNotEqualTo.comparisonValue()));
    }

    private static Filter<DocumentChunk> mapGreaterThan(IsGreaterThan isGreaterThan) {
        ValueExtractor extractor = CoherenceMetadataFilterMapper.extractor(isGreaterThan.key());
        Comparable value = isGreaterThan.comparisonValue();
        return Filters.greater(extractor, (Comparable)value);
    }

    private static Filter<DocumentChunk> mapGreaterThanOrEqual(IsGreaterThanOrEqualTo isGreaterThanOrEqualTo) {
        ValueExtractor extractor = CoherenceMetadataFilterMapper.extractor(isGreaterThanOrEqualTo.key());
        Comparable value = isGreaterThanOrEqualTo.comparisonValue();
        return Filters.greaterEqual(extractor, (Comparable)value);
    }

    private static Filter<DocumentChunk> mapLessThan(IsLessThan isLessThan) {
        ValueExtractor extractor = CoherenceMetadataFilterMapper.extractor(isLessThan.key());
        Comparable value = isLessThan.comparisonValue();
        return Filters.less(extractor, (Comparable)value);
    }

    private static Filter<DocumentChunk> mapLessThanOrEqual(IsLessThanOrEqualTo isLessThanOrEqualTo) {
        ValueExtractor extractor = CoherenceMetadataFilterMapper.extractor(isLessThanOrEqualTo.key());
        Comparable value = isLessThanOrEqualTo.comparisonValue();
        return Filters.lessEqual(extractor, (Comparable)value);
    }

    public static Filter<DocumentChunk> mapIn(IsIn isIn) {
        ValueExtractor extractor = CoherenceMetadataFilterMapper.extractor(isIn.key());
        Collection values = isIn.comparisonValues();
        return Filters.in(extractor, new HashSet(values));
    }

    public static Filter<DocumentChunk> mapNotIn(IsNotIn isNotIn) {
        ValueExtractor extractor = CoherenceMetadataFilterMapper.extractor(isNotIn.key());
        Collection values = isNotIn.comparisonValues();
        return Filters.not((Filter)Filters.in(extractor, new HashSet(values)));
    }

    private static Filter<DocumentChunk> mapAnd(And and) {
        return Filters.all((Filter[])new Filter[]{CoherenceMetadataFilterMapper.map(and.left()), CoherenceMetadataFilterMapper.map(and.right())});
    }

    private static Filter<DocumentChunk> mapNot(Not not) {
        return Filters.not(CoherenceMetadataFilterMapper.map(not.expression()));
    }

    private static Filter<DocumentChunk> mapOr(Or or) {
        return Filters.any((Filter[])new Filter[]{CoherenceMetadataFilterMapper.map(or.left()), CoherenceMetadataFilterMapper.map(or.right())});
    }
}

