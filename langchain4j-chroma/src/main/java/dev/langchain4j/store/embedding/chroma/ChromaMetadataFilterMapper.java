/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.Internal
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
package dev.langchain4j.store.embedding.chroma;

import dev.langchain4j.Internal;
import dev.langchain4j.store.embedding.filter.Filter;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

@Internal
class ChromaMetadataFilterMapper {
    ChromaMetadataFilterMapper() {
    }

    static Map<String, Object> map(Filter filter) {
        if (filter == null) {
            return null;
        }
        if (filter instanceof IsEqualTo) {
            return ChromaMetadataFilterMapper.mapEqual((IsEqualTo)filter);
        }
        if (filter instanceof IsNotEqualTo) {
            return ChromaMetadataFilterMapper.mapNotEqual((IsNotEqualTo)filter);
        }
        if (filter instanceof IsGreaterThan) {
            return ChromaMetadataFilterMapper.mapIsGreaterThan((IsGreaterThan)filter);
        }
        if (filter instanceof IsGreaterThanOrEqualTo) {
            return ChromaMetadataFilterMapper.mapIsGreaterThanOrEqualTo((IsGreaterThanOrEqualTo)filter);
        }
        if (filter instanceof IsLessThan) {
            return ChromaMetadataFilterMapper.mapIsLessThan((IsLessThan)filter);
        }
        if (filter instanceof IsLessThanOrEqualTo) {
            return ChromaMetadataFilterMapper.mapIsLessThanOrEqualTo((IsLessThanOrEqualTo)filter);
        }
        if (filter instanceof IsIn) {
            return ChromaMetadataFilterMapper.mapIsIn((IsIn)filter);
        }
        if (filter instanceof IsNotIn) {
            return ChromaMetadataFilterMapper.mapIsNotIn((IsNotIn)filter);
        }
        if (filter instanceof And) {
            return ChromaMetadataFilterMapper.mapAnd((And)filter);
        }
        if (filter instanceof Or) {
            return ChromaMetadataFilterMapper.mapOr((Or)filter);
        }
        if (filter instanceof Not) {
            return ChromaMetadataFilterMapper.mapNot((Not)filter);
        }
        throw new UnsupportedOperationException("Unsupported filter type: " + filter.getClass().getName());
    }

    private static Map<String, Object> mapEqual(IsEqualTo filter) {
        return Collections.singletonMap(filter.key(), filter.comparisonValue());
    }

    private static Map<String, Object> mapNotEqual(IsNotEqualTo filter) {
        return Collections.singletonMap(filter.key(), Collections.singletonMap("$ne", filter.comparisonValue()));
    }

    private static Map<String, Object> mapIsGreaterThan(IsGreaterThan filter) {
        return Collections.singletonMap(filter.key(), Collections.singletonMap("$gt", filter.comparisonValue()));
    }

    private static Map<String, Object> mapIsGreaterThanOrEqualTo(IsGreaterThanOrEqualTo filter) {
        return Collections.singletonMap(filter.key(), Collections.singletonMap("$gte", filter.comparisonValue()));
    }

    private static Map<String, Object> mapIsLessThan(IsLessThan filter) {
        return Collections.singletonMap(filter.key(), Collections.singletonMap("$lt", filter.comparisonValue()));
    }

    private static Map<String, Object> mapIsLessThanOrEqualTo(IsLessThanOrEqualTo filter) {
        return Collections.singletonMap(filter.key(), Collections.singletonMap("$lte", filter.comparisonValue()));
    }

    private static Map<String, Object> mapIsIn(IsIn filter) {
        return Collections.singletonMap(filter.key(), Collections.singletonMap("$in", filter.comparisonValues()));
    }

    private static Map<String, Object> mapIsNotIn(IsNotIn filter) {
        return Collections.singletonMap(filter.key(), Collections.singletonMap("$nin", filter.comparisonValues()));
    }

    private static Map<String, Object> mapAnd(And and) {
        return Collections.singletonMap("$and", Arrays.asList(ChromaMetadataFilterMapper.map(and.left()), ChromaMetadataFilterMapper.map(and.right())));
    }

    private static Map<String, Object> mapOr(Or or) {
        return Collections.singletonMap("$or", Arrays.asList(ChromaMetadataFilterMapper.map(or.left()), ChromaMetadataFilterMapper.map(or.right())));
    }

    private static Map<String, Object> mapNot(Not not) {
        Filter expression = not.expression();
        if (expression instanceof IsEqualTo) {
            expression = new IsNotEqualTo(((IsEqualTo)expression).key(), ((IsEqualTo)expression).comparisonValue());
        } else if (expression instanceof IsNotEqualTo) {
            expression = new IsEqualTo(((IsNotEqualTo)expression).key(), ((IsNotEqualTo)expression).comparisonValue());
        } else if (expression instanceof IsGreaterThan) {
            expression = new IsLessThanOrEqualTo(((IsGreaterThan)expression).key(), ((IsGreaterThan)expression).comparisonValue());
        } else if (expression instanceof IsGreaterThanOrEqualTo) {
            expression = new IsLessThan(((IsGreaterThanOrEqualTo)expression).key(), ((IsGreaterThanOrEqualTo)expression).comparisonValue());
        } else if (expression instanceof IsLessThan) {
            expression = new IsGreaterThanOrEqualTo(((IsLessThan)expression).key(), ((IsLessThan)expression).comparisonValue());
        } else if (expression instanceof IsLessThanOrEqualTo) {
            expression = new IsGreaterThan(((IsLessThanOrEqualTo)expression).key(), ((IsLessThanOrEqualTo)expression).comparisonValue());
        } else if (expression instanceof IsIn) {
            expression = new IsNotIn(((IsIn)expression).key(), ((IsIn)expression).comparisonValues());
        } else if (expression instanceof IsNotIn) {
            expression = new IsIn(((IsNotIn)expression).key(), ((IsNotIn)expression).comparisonValues());
        } else if (expression instanceof And) {
            expression = new Or(Filter.not((Filter)((And)expression).left()), Filter.not((Filter)((And)expression).right()));
        } else if (expression instanceof Or) {
            expression = new And(Filter.not((Filter)((Or)expression).left()), Filter.not((Filter)((Or)expression).right()));
        } else {
            throw new UnsupportedOperationException("Unsupported filter type: " + expression.getClass().getName());
        }
        return ChromaMetadataFilterMapper.map(expression);
    }
}

