/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
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
package dev.langchain4j.rag.content.retriever.azure.search;

import dev.langchain4j.rag.content.retriever.azure.search.AzureAiSearchFilterMapper;
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
import java.util.Collection;
import java.util.stream.Collectors;

public class DefaultAzureAiSearchFilterMapper
implements AzureAiSearchFilterMapper {
    @Override
    public String map(Filter filter) {
        if (filter == null) {
            return "";
        }
        if (this.isLogicalOperator(filter)) {
            return this.mapLogicalOperator(filter);
        }
        return this.mapComparisonFilter(filter);
    }

    private String mapLogicalOperator(Filter operator) {
        if (operator instanceof And) {
            return String.format(this.getLogicalFormat(operator), this.map(((And)operator).left()), this.map(((And)operator).right()));
        }
        if (operator instanceof Or) {
            return String.format(this.getLogicalFormat(operator), this.map(((Or)operator).left()), this.map(((Or)operator).right()));
        }
        if (operator instanceof Not) {
            return String.format(this.getLogicalFormat(operator), this.map(((Not)operator).expression()));
        }
        throw new UnsupportedOperationException("Unsupported filter type: " + operator.getClass().getName());
    }

    private boolean isLogicalOperator(Filter filter) {
        return filter instanceof And || filter instanceof Or || filter instanceof Not;
    }

    private String mapComparisonFilter(Filter filter) {
        if (filter instanceof IsEqualTo) {
            return this.mapIsEqualTo((IsEqualTo)filter);
        }
        if (filter instanceof IsNotEqualTo) {
            return this.mapIsNotEqualTo((IsNotEqualTo)filter);
        }
        if (filter instanceof IsGreaterThan) {
            return this.mapIsGreaterThan((IsGreaterThan)filter);
        }
        if (filter instanceof IsGreaterThanOrEqualTo) {
            return this.mapIsGreaterThanOrEqualTo((IsGreaterThanOrEqualTo)filter);
        }
        if (filter instanceof IsLessThan) {
            return this.mapIsLessThan((IsLessThan)filter);
        }
        if (filter instanceof IsLessThanOrEqualTo) {
            return this.mapIsLessThanOrEqualTo((IsLessThanOrEqualTo)filter);
        }
        if (filter instanceof IsIn) {
            return this.mapIsIn((IsIn)filter);
        }
        if (filter instanceof IsNotIn) {
            return this.mapIsNotIn((IsNotIn)filter);
        }
        throw new UnsupportedOperationException("Unsupported filter type: " + filter.getClass().getName());
    }

    private String getLogicalFormat(Filter filter) {
        if (filter instanceof And) {
            return "(%s and %s)";
        }
        if (filter instanceof Or) {
            return "(%s or %s)";
        }
        if (filter instanceof Not) {
            return "(not %s)";
        }
        throw new UnsupportedOperationException("Unsupported filter type: " + filter.getClass().getName());
    }

    private String getComparisonFormat(Filter filter) {
        if (filter instanceof IsEqualTo) {
            return "k/value eq '%s'";
        }
        if (filter instanceof IsGreaterThan) {
            return "k/value gt '%s'";
        }
        if (filter instanceof IsGreaterThanOrEqualTo) {
            return "k/value ge '%s'";
        }
        if (filter instanceof IsLessThan) {
            return "k/value lt '%s'";
        }
        if (filter instanceof IsLessThanOrEqualTo) {
            return "k/value le '%s'";
        }
        if (filter instanceof IsIn) {
            return "search.in(k/value, ('%s'))";
        }
        throw new UnsupportedOperationException("Unsupported filter type: " + filter.getClass().getName());
    }

    private String mapIsNotIn(IsNotIn filter) {
        return this.map(Filter.not((Filter)new IsIn(filter.key(), filter.comparisonValues())));
    }

    private String mapIsIn(IsIn filter) {
        return this.formatComparisonFilter(filter.key(), this.mapSearchInValues(filter.comparisonValues()), this.getComparisonFormat((Filter)filter));
    }

    private String mapIsLessThanOrEqualTo(IsLessThanOrEqualTo filter) {
        return this.formatComparisonFilter(filter.key(), filter.comparisonValue().toString(), this.getComparisonFormat((Filter)filter));
    }

    private String mapIsLessThan(IsLessThan filter) {
        return this.formatComparisonFilter(filter.key(), filter.comparisonValue().toString(), this.getComparisonFormat((Filter)filter));
    }

    private String mapIsGreaterThanOrEqualTo(IsGreaterThanOrEqualTo filter) {
        return this.formatComparisonFilter(filter.key(), filter.comparisonValue().toString(), this.getComparisonFormat((Filter)filter));
    }

    private String mapIsGreaterThan(IsGreaterThan filter) {
        return this.formatComparisonFilter(filter.key(), filter.comparisonValue().toString(), this.getComparisonFormat((Filter)filter));
    }

    private String mapIsEqualTo(IsEqualTo filter) {
        return this.formatComparisonFilter(filter.key(), filter.comparisonValue().toString(), this.getComparisonFormat((Filter)filter));
    }

    private String mapIsNotEqualTo(IsNotEqualTo filter) {
        return this.map(Filter.not((Filter)new IsEqualTo(filter.key(), filter.comparisonValue())));
    }

    private String mapSearchInValues(Collection<?> comparisonValues) {
        return comparisonValues.stream().map(Object::toString).sorted().collect(Collectors.joining(", "));
    }

    private String formatComparisonFilter(String key, String value, String format) {
        return String.format("metadata/attributes/any(k: k/key eq '%s' and " + format + ")", key, value);
    }
}

