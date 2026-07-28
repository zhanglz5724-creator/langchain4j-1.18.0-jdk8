/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mongodb.client.model.Filters
 *  dev.langchain4j.store.embedding.filter.Filter
 *  dev.langchain4j.store.embedding.filter.comparison.ContainsString
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
 *  org.bson.conversions.Bson
 */
package dev.langchain4j.store.embedding.mongodb;

import com.mongodb.client.model.Filters;
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.filter.comparison.ContainsString;
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
import java.util.regex.Pattern;
import org.bson.conversions.Bson;

class MongoDbMetadataFilterMapper {
    MongoDbMetadataFilterMapper() {
    }

    public static Bson map(Filter filter) {
        if (filter instanceof IsEqualTo) {
            return MongoDbMetadataFilterMapper.mapEqual((IsEqualTo)filter);
        }
        if (filter instanceof IsNotEqualTo) {
            return MongoDbMetadataFilterMapper.mapNotEqual((IsNotEqualTo)filter);
        }
        if (filter instanceof IsGreaterThan) {
            return MongoDbMetadataFilterMapper.mapGreaterThan((IsGreaterThan)filter);
        }
        if (filter instanceof IsGreaterThanOrEqualTo) {
            return MongoDbMetadataFilterMapper.mapGreaterThanOrEqual((IsGreaterThanOrEqualTo)filter);
        }
        if (filter instanceof IsLessThan) {
            return MongoDbMetadataFilterMapper.mapLessThan((IsLessThan)filter);
        }
        if (filter instanceof IsLessThanOrEqualTo) {
            return MongoDbMetadataFilterMapper.mapLessThanOrEqual((IsLessThanOrEqualTo)filter);
        }
        if (filter instanceof IsIn) {
            return MongoDbMetadataFilterMapper.mapIn((IsIn)filter);
        }
        if (filter instanceof IsNotIn) {
            return MongoDbMetadataFilterMapper.mapNotIn((IsNotIn)filter);
        }
        if (filter instanceof And) {
            return MongoDbMetadataFilterMapper.mapAnd((And)filter);
        }
        if (filter instanceof Or) {
            return MongoDbMetadataFilterMapper.mapOr((Or)filter);
        }
        if (filter instanceof Not) {
            return MongoDbMetadataFilterMapper.mapNot((Not)filter);
        }
        if (filter instanceof ContainsString) {
            return MongoDbMetadataFilterMapper.mapContains((ContainsString)filter);
        }
        throw new UnsupportedOperationException("Unsupported filter type: " + filter.getClass().getName());
    }

    private static String getFieldName(String key) {
        return "metadata." + key;
    }

    private static Bson mapEqual(IsEqualTo filter) {
        return Filters.eq((String)MongoDbMetadataFilterMapper.getFieldName(filter.key()), (Object)filter.comparisonValue());
    }

    private static Bson mapNotEqual(IsNotEqualTo filter) {
        return Filters.ne((String)MongoDbMetadataFilterMapper.getFieldName(filter.key()), (Object)filter.comparisonValue());
    }

    private static Bson mapGreaterThan(IsGreaterThan filter) {
        return Filters.gt((String)MongoDbMetadataFilterMapper.getFieldName(filter.key()), (Object)filter.comparisonValue());
    }

    private static Bson mapGreaterThanOrEqual(IsGreaterThanOrEqualTo filter) {
        return Filters.gte((String)MongoDbMetadataFilterMapper.getFieldName(filter.key()), (Object)filter.comparisonValue());
    }

    private static Bson mapLessThan(IsLessThan filter) {
        return Filters.lt((String)MongoDbMetadataFilterMapper.getFieldName(filter.key()), (Object)filter.comparisonValue());
    }

    private static Bson mapLessThanOrEqual(IsLessThanOrEqualTo filter) {
        return Filters.lte((String)MongoDbMetadataFilterMapper.getFieldName(filter.key()), (Object)filter.comparisonValue());
    }

    private static Bson mapIn(IsIn filter) {
        return Filters.in((String)MongoDbMetadataFilterMapper.getFieldName(filter.key()), (Iterable)filter.comparisonValues());
    }

    private static Bson mapNotIn(IsNotIn filter) {
        return Filters.nin((String)MongoDbMetadataFilterMapper.getFieldName(filter.key()), (Iterable)filter.comparisonValues());
    }

    private static Bson mapAnd(And filter) {
        return Filters.and((Bson[])new Bson[]{MongoDbMetadataFilterMapper.map(filter.left()), MongoDbMetadataFilterMapper.map(filter.right())});
    }

    private static Bson mapOr(Or filter) {
        return Filters.or((Bson[])new Bson[]{MongoDbMetadataFilterMapper.map(filter.left()), MongoDbMetadataFilterMapper.map(filter.right())});
    }

    private static Bson mapNot(Not filter) {
        return Filters.not((Bson)MongoDbMetadataFilterMapper.map(filter.expression()));
    }

    private static Bson mapContains(ContainsString filter) {
        return Filters.regex((String)MongoDbMetadataFilterMapper.getFieldName(filter.key()), (String)Pattern.quote(filter.comparisonValue()));
    }
}

