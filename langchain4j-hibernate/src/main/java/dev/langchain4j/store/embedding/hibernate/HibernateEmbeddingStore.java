/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.JsonProcessingException
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  dev.langchain4j.data.document.Metadata
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.internal.ValidationUtils
 *  dev.langchain4j.model.embedding.EmbeddingModel
 *  dev.langchain4j.store.embedding.EmbeddingMatch
 *  dev.langchain4j.store.embedding.EmbeddingSearchRequest
 *  dev.langchain4j.store.embedding.EmbeddingSearchResult
 *  dev.langchain4j.store.embedding.EmbeddingStore
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
 *  jakarta.persistence.criteria.CriteriaBuilder
 *  jakarta.persistence.criteria.CriteriaDelete
 *  jakarta.persistence.criteria.CriteriaQuery
 *  jakarta.persistence.criteria.Expression
 *  jakarta.persistence.criteria.Order
 *  jakarta.persistence.criteria.Predicate
 *  jakarta.persistence.criteria.Selection
 *  jakarta.persistence.metamodel.EntityType
 *  jakarta.persistence.metamodel.ManagedType
 *  jakarta.persistence.metamodel.SingularAttribute
 *  jakarta.persistence.metamodel.Type
 *  org.hibernate.SessionFactory
 *  org.hibernate.StatelessSession
 *  org.hibernate.boot.ResourceStreamLocator
 *  org.hibernate.boot.registry.BootstrapServiceRegistryBuilder
 *  org.hibernate.boot.registry.classloading.internal.ClassLoaderServiceImpl
 *  org.hibernate.boot.registry.classloading.spi.ClassLoaderService
 *  org.hibernate.boot.spi.AdditionalMappingContributions
 *  org.hibernate.boot.spi.AdditionalMappingContributor
 *  org.hibernate.boot.spi.InFlightMetadataCollector
 *  org.hibernate.boot.spi.MetadataBuildingContext
 *  org.hibernate.cfg.Configuration
 *  org.hibernate.engine.jdbc.spi.JdbcServices
 *  org.hibernate.engine.spi.SessionFactoryImplementor
 *  org.hibernate.engine.spi.SharedSessionContractImplementor
 *  org.hibernate.generator.BeforeExecutionGenerator
 *  org.hibernate.generator.EventType
 *  org.hibernate.generator.Generator
 *  org.hibernate.internal.util.ReaderInputStream
 *  org.hibernate.mapping.Column
 *  org.hibernate.metamodel.mapping.AttributeMapping
 *  org.hibernate.metamodel.mapping.ModelPart
 *  org.hibernate.metamodel.spi.EntityInstantiator
 *  org.hibernate.persister.entity.EntityPersister
 *  org.hibernate.query.MutationQuery
 *  org.hibernate.query.SelectionQuery
 *  org.hibernate.query.criteria.HibernateCriteriaBuilder
 *  org.hibernate.query.criteria.JpaConflictClause
 *  org.hibernate.query.criteria.JpaConflictUpdateAction
 *  org.hibernate.query.criteria.JpaCriteriaDelete
 *  org.hibernate.query.criteria.JpaCriteriaInsertValues
 *  org.hibernate.query.criteria.JpaCriteriaQuery
 *  org.hibernate.query.criteria.JpaJsonValueExpression
 *  org.hibernate.query.criteria.JpaParameterExpression
 *  org.hibernate.query.criteria.JpaPath
 *  org.hibernate.query.criteria.JpaPredicate
 *  org.hibernate.query.criteria.JpaRoot
 *  org.hibernate.query.restriction.Restriction
 *  org.hibernate.relational.SchemaManager
 *  org.hibernate.tool.schema.Action
 *  org.hibernate.tool.schema.SourceType
 *  org.hibernate.type.descriptor.java.JavaType
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.store.embedding.hibernate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
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
import dev.langchain4j.store.embedding.hibernate.DatabaseKind;
import dev.langchain4j.store.embedding.hibernate.DistanceFunction;
import dev.langchain4j.store.embedding.hibernate.EmbeddedText;
import dev.langchain4j.store.embedding.hibernate.Embedding;
import dev.langchain4j.store.embedding.hibernate.EmbeddingEntity;
import dev.langchain4j.store.embedding.hibernate.EmbeddingVector;
import dev.langchain4j.store.embedding.hibernate.MetadataAttribute;
import dev.langchain4j.store.embedding.hibernate.UnmappedMetadata;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Selection;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.ManagedType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.Type;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.boot.ResourceStreamLocator;
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;
import org.hibernate.boot.registry.classloading.internal.ClassLoaderServiceImpl;
import org.hibernate.boot.registry.classloading.spi.ClassLoaderService;
import org.hibernate.boot.spi.AdditionalMappingContributions;
import org.hibernate.boot.spi.AdditionalMappingContributor;
import org.hibernate.boot.spi.InFlightMetadataCollector;
import org.hibernate.boot.spi.MetadataBuildingContext;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.jdbc.spi.JdbcServices;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;
import org.hibernate.generator.Generator;
import org.hibernate.internal.util.ReaderInputStream;
import org.hibernate.mapping.Column;
import org.hibernate.metamodel.mapping.AttributeMapping;
import org.hibernate.metamodel.mapping.ModelPart;
import org.hibernate.metamodel.spi.EntityInstantiator;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.SelectionQuery;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaConflictClause;
import org.hibernate.query.criteria.JpaConflictUpdateAction;
import org.hibernate.query.criteria.JpaCriteriaDelete;
import org.hibernate.query.criteria.JpaCriteriaInsertValues;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.hibernate.query.criteria.JpaJsonValueExpression;
import org.hibernate.query.criteria.JpaParameterExpression;
import org.hibernate.query.criteria.JpaPath;
import org.hibernate.query.criteria.JpaPredicate;
import org.hibernate.query.criteria.JpaRoot;
import org.hibernate.query.restriction.Restriction;
import org.hibernate.relational.SchemaManager;
import org.hibernate.tool.schema.Action;
import org.hibernate.tool.schema.SourceType;
import org.hibernate.type.descriptor.java.JavaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateEmbeddingStore<E>
implements EmbeddingStore<TextSegment> {
    private static final Logger log = LoggerFactory.getLogger(HibernateEmbeddingStore.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final boolean IS_HIBERNATE_ORM_7_1;
    protected final boolean isDynamic;
    protected final SessionFactory sessionFactory;
    protected final DatabaseKind databaseKind;
    protected final Class<E> entityClass;
    protected final EntityPersister entityPersister;
    protected final JavaType<Object> idType;
    protected final Generator idGenerator;
    protected final boolean allowUuidGeneration;
    protected final AttributeMapping idAttributeMapping;
    protected final AttributeMapping embeddingAttributeMapping;
    protected final AttributeMapping embeddedTextAttributeMapping;
    protected final AttributeMapping unmappedMetadataAttributeMapping;
    protected final Type<Map<?, ?>> unmappedMetadataAttributeMapType;
    protected final Map<String, AttributeMapping> metadataAttributeMappings;
    protected final DistanceFunction distanceFunction;
    private final JpaCriteriaDelete<?> deleteByIds;
    private final JpaCriteriaInsertValues<?> insertValues;

    protected HibernateEmbeddingStore(boolean isDynamic, SessionFactory sessionFactory, DatabaseKind databaseKind, Class<E> entityClass, String embeddingAttributeName, String embeddedTextAttributeName, String unmappedMetadataAttributeName, String[] metadataAttributePaths, DistanceFunction distanceFunction) {
        this.isDynamic = isDynamic;
        this.sessionFactory = (SessionFactory)ValidationUtils.ensureNotNull((Object)sessionFactory, (String)"sessionFactory");
        this.databaseKind = (DatabaseKind)ValidationUtils.ensureNotNull((Object)databaseKind, (String)"databaseKind");
        this.entityClass = (Class)ValidationUtils.ensureNotNull(entityClass, (String)"entityClass");
        this.distanceFunction = (DistanceFunction)((Object)ValidationUtils.ensureNotNull((Object)((Object)distanceFunction), (String)"distanceFunction"));
        this.entityPersister = ((SessionFactoryImplementor)sessionFactory.unwrap(SessionFactoryImplementor.class)).getRuntimeMetamodels().getMappingMetamodel().getEntityDescriptor(entityClass);
        this.idType = this.entityPersister.getIdentifierMapping().getJavaType();
        this.allowUuidGeneration = this.entityPersister.getIdentifierMapping().getJavaType().getJavaTypeClass() == String.class || this.entityPersister.getIdentifierMapping().getJavaType().getJavaTypeClass() == UUID.class;
        this.idGenerator = this.entityPersister.getGenerator();
        this.idAttributeMapping = (AttributeMapping)this.entityPersister.getIdentifierMapping();
        this.embeddingAttributeMapping = this.entityPersister.findAttributeMapping(ValidationUtils.ensureNotEmpty((String)embeddingAttributeName, (String)"embeddingAttributeName"));
        this.embeddedTextAttributeMapping = embeddedTextAttributeName == null ? null : this.entityPersister.findAttributeMapping(embeddedTextAttributeName);
        this.unmappedMetadataAttributeMapping = this.entityPersister.findAttributeMapping(ValidationUtils.ensureNotEmpty((String)unmappedMetadataAttributeName, (String)"unmappedMetadataAttributeName"));
        if (this.embeddingAttributeMapping == null) {
            throw new IllegalArgumentException("Couldn't find embedding with attribute name: " + embeddingAttributeName);
        }
        if (this.embeddedTextAttributeMapping == null && embeddedTextAttributeName != null) {
            throw new IllegalArgumentException("Couldn't find embedded text with attribute name: " + embeddedTextAttributeName);
        }
        if (this.unmappedMetadataAttributeMapping == null) {
            throw new IllegalArgumentException("Couldn't find unmapped metadata with attribute name: " + unmappedMetadataAttributeName);
        }
        Type unmappedMetadataAttributeType = sessionFactory.getMetamodel().entity(entityClass).getSingularAttribute(unmappedMetadataAttributeName).getType();
        if (unmappedMetadataAttributeType.getJavaType() == String.class) {
            this.unmappedMetadataAttributeMapType = null;
        } else {
            if (unmappedMetadataAttributeType.getJavaType() != Map.class) {
                throw new IllegalArgumentException("Unmapped metadata attribute '" + unmappedMetadataAttributeName + "' must be of type Map or String, but found: " + unmappedMetadataAttributeType.getJavaType().getTypeName());
            }
            this.unmappedMetadataAttributeMapType = unmappedMetadataAttributeType;
        }
        if (metadataAttributePaths == null || metadataAttributePaths.length == 0) {
            this.metadataAttributeMappings = Collections.emptyMap();
        } else {
            LinkedHashMap<String, AttributeMapping> metadataAttributeMappings = new LinkedHashMap<String, AttributeMapping>(metadataAttributePaths.length);
            for (String metadataAttributePath : metadataAttributePaths) {
                ModelPart modelPart = this.entityPersister.findByPath(metadataAttributePath);
                if (!(modelPart instanceof AttributeMapping)) {
                    throw new IllegalArgumentException("Couldn't find metadata attribute with path: " + metadataAttributePath);
                }
                AttributeMapping attributeMapping = (AttributeMapping)modelPart;
                metadataAttributeMappings.put(metadataAttributePath, attributeMapping);
            }
            this.metadataAttributeMappings = metadataAttributeMappings;
        }
        HibernateCriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        JpaCriteriaDelete delete = criteriaBuilder.createCriteriaDelete(entityClass);
        JpaParameterExpression idListParameter = criteriaBuilder.listParameter(this.idType.getJavaTypeClass(), this.idAttributeMapping.getAttributeName());
        JpaRoot root = delete.getTarget();
        delete.where((Expression)criteriaBuilder.in((Expression)root.get(this.idAttributeMapping.getAttributeName())).value((Expression)idListParameter));
        this.deleteByIds = delete;
        JpaCriteriaInsertValues criteriaInsertValues = criteriaBuilder.createCriteriaInsertValues(entityClass);
        JpaRoot target = criteriaInsertValues.getTarget();
        JpaParameterExpression idParameter = criteriaBuilder.parameter(this.idType.getJavaTypeClass(), this.idAttributeMapping.getAttributeName());
        JpaParameterExpression embeddingParameter = criteriaBuilder.parameter(float[].class, embeddingAttributeName);
        JpaParameterExpression unmappedMetadataParameter = this.unmappedMetadataAttributeMapType != null ? criteriaBuilder.parameter(Map.class, unmappedMetadataAttributeName) : criteriaBuilder.parameter(String.class, unmappedMetadataAttributeName);
        ArrayList<Object> paths = new ArrayList<Object>();
        ArrayList<JpaParameterExpression> values = new ArrayList<JpaParameterExpression>();
        JpaConflictClause onConflict = criteriaInsertValues.onConflict().conflictOnConstraintAttributes(new String[]{this.idAttributeMapping.getAttributeName()});
        JpaRoot excludedRoot = onConflict.getExcludedRoot();
        JpaConflictUpdateAction updateAction = onConflict.onConflictDoUpdate();
        paths.add(target.get(this.idAttributeMapping.getAttributeName()));
        values.add(idParameter);
        paths.add(target.get(embeddingAttributeName));
        values.add(embeddingParameter);
        updateAction.set(embeddingAttributeName, (Object)excludedRoot.get(embeddingAttributeName));
        if (embeddedTextAttributeName != null) {
            JpaParameterExpression embeddedTextParameter = criteriaBuilder.parameter(String.class, embeddedTextAttributeName);
            paths.add(target.get(embeddedTextAttributeName));
            values.add(embeddedTextParameter);
            updateAction.set(embeddedTextAttributeName, (Object)excludedRoot.get(embeddedTextAttributeName));
        }
        paths.add(target.get(unmappedMetadataAttributeName));
        values.add(unmappedMetadataParameter);
        updateAction.set(unmappedMetadataAttributeName, (Object)excludedRoot.get(unmappedMetadataAttributeName));
        for (String attributePath : this.metadataAttributeMappings.keySet()) {
            JpaPath path = this.get(target, attributePath);
            paths.add(path);
            values.add(criteriaBuilder.parameter(path.getJavaType(), attributePath));
            updateAction.set(path, this.get(excludedRoot, attributePath));
        }
        criteriaInsertValues.setInsertionTargetPaths(paths);
        criteriaInsertValues.values(Collections.singletonList(criteriaBuilder.values(values)));
        this.insertValues = criteriaInsertValues;
    }

    public HibernateEmbeddingStore() {
        this.isDynamic = false;
        this.sessionFactory = null;
        this.databaseKind = null;
        this.entityClass = null;
        this.entityPersister = null;
        this.idType = null;
        this.idGenerator = null;
        this.allowUuidGeneration = false;
        this.idAttributeMapping = null;
        this.embeddingAttributeMapping = null;
        this.embeddedTextAttributeMapping = null;
        this.unmappedMetadataAttributeMapping = null;
        this.unmappedMetadataAttributeMapType = null;
        this.metadataAttributeMappings = null;
        this.distanceFunction = null;
        this.deleteByIds = null;
        this.insertValues = null;
    }

    public static <E> Builder<E> builder(Class<E> entityClass) {
        return new Builder<E>(entityClass);
    }

    public static DynamicBuilder dynamicBuilder() {
        return new DynamicBuilder();
    }

    public static DynamicDatasourceBuilder dynamicDatasourceBuilder() {
        return new DynamicDatasourceBuilder();
    }

    public void close() {
        if (this.isDynamic) {
            this.sessionFactory.close();
        }
    }

    public String add(dev.langchain4j.data.embedding.Embedding embedding) {
        List<String> ids = this.addAll(Collections.singletonList(embedding), null);
        return ids.get(0);
    }

    public void add(String id, dev.langchain4j.data.embedding.Embedding embedding) {
        this.addInternal(id, embedding, null);
    }

    public String add(dev.langchain4j.data.embedding.Embedding embedding, TextSegment textSegment) {
        List<String> ids = this.addAll(Collections.singletonList(embedding), textSegment == null ? null : Collections.singletonList(textSegment));
        return ids.get(0);
    }

    public List<String> addAll(List<dev.langchain4j.data.embedding.Embedding> embeddings) {
        return this.addAll(embeddings, null);
    }

    public void removeAll(Collection<String> ids) {
        ValidationUtils.ensureNotEmpty(ids, (String)"ids");
        this.sessionFactory.inTransaction(session -> session.createMutationQuery(this.deleteByIds).setParameter(this.idAttributeMapping.getAttributeName(), ids.stream().map(arg_0 -> this.idType.fromString(arg_0)).collect(Collectors.toList())).executeUpdate());
    }

    public void removeAll(Filter filter) {
        ValidationUtils.ensureNotNull((Object)filter, (String)"filter");
        HibernateCriteriaBuilder criteriaBuilder = this.sessionFactory.getCriteriaBuilder();
        JpaCriteriaDelete delete = criteriaBuilder.createCriteriaDelete(this.entityClass);
        delete.where((Expression)this.createPredicateFromFilter(delete.getTarget(), filter, criteriaBuilder));
        this.sessionFactory.inTransaction(session -> session.createMutationQuery((CriteriaDelete)delete).executeUpdate());
    }

    public void removeAll() {
        if (this.isDynamic) {
            this.sessionFactory.getSchemaManager().truncate();
        } else if (HibernateEmbeddingStore.isIsHibernateOrm71()) {
            this.sessionFactory.inStatelessTransaction(session -> session.createMutationQuery("delete from " + this.entityPersister.getEntityName()).executeUpdate());
        } else {
            try {
                this.sessionFactory.getSchemaManager().truncateTable(this.entityPersister.getIdentifierTableMapping().getTableName());
            }
            catch (UnsupportedOperationException ex) {
                this.sessionFactory.inStatelessTransaction(session -> session.createMutationQuery("delete from " + this.entityPersister.getEntityName()).executeUpdate());
            }
        }
    }

    private static boolean isIsHibernateOrm71() {
        return IS_HIBERNATE_ORM_7_1;
    }

    public List<E> query(dev.langchain4j.data.embedding.Embedding embedding, Restriction<E> restriction) {
        return this.query(embedding, null, restriction, null);
    }

    public List<E> query(dev.langchain4j.data.embedding.Embedding embedding, double minScore, Restriction<E> restriction) {
        return this.query(embedding, (Double)minScore, restriction, null);
    }

    public List<E> query(dev.langchain4j.data.embedding.Embedding embedding, double minScore, Restriction<E> restriction, int maxResults) {
        return this.query(embedding, (Double)minScore, restriction, (Integer)maxResults);
    }

    private List<E> query(dev.langchain4j.data.embedding.Embedding embedding, Double minScore, Restriction<E> restriction, Integer maxResults) {
        ValidationUtils.ensureNotNull(restriction, (String)"restriction");
        JpaCriteriaQuery<E> query = this.createBaseQuery(this.entityClass, minScore != null, (arg_0, arg_1) -> restriction.toPredicate(arg_0, arg_1));
        return (List)this.sessionFactory.fromStatelessSession(session -> {
            SelectionQuery selectionQuery = session.createSelectionQuery((CriteriaQuery)query);
            selectionQuery.setParameter(this.embeddingAttributeMapping.getAttributeName(), (Object)embedding.vector());
            if (minScore != null) {
                selectionQuery.setParameter("minScore", (Object)minScore);
            }
            if (maxResults != null) {
                selectionQuery.setMaxResults(maxResults.intValue());
            }
            return selectionQuery.getResultList();
        });
    }

    public EmbeddingSearchResult<TextSegment> search(dev.langchain4j.data.embedding.Embedding embedding, Restriction<E> restriction) {
        return this.search(embedding, null, restriction, null);
    }

    public EmbeddingSearchResult<TextSegment> search(dev.langchain4j.data.embedding.Embedding embedding, double minScore, Restriction<E> restriction) {
        return this.search(embedding, (Double)minScore, restriction, null);
    }

    public EmbeddingSearchResult<TextSegment> search(dev.langchain4j.data.embedding.Embedding embedding, double minScore, Restriction<E> restriction, int maxResults) {
        return this.search(embedding, (Double)minScore, restriction, (Integer)maxResults);
    }

    private EmbeddingSearchResult<TextSegment> search(dev.langchain4j.data.embedding.Embedding embedding, Double minScore, Restriction<E> restriction, Integer maxResults) {
        ValidationUtils.ensureNotNull(restriction, (String)"restriction");
        JpaCriteriaQuery<Object[]> query = this.createBaseQuery(Object[].class, minScore != null, (arg_0, arg_1) -> restriction.toPredicate(arg_0, arg_1));
        this.applyEmbeddingSearchResultSelections(query);
        return (EmbeddingSearchResult)this.sessionFactory.fromStatelessSession(session -> {
            SelectionQuery selectionQuery = session.createSelectionQuery((CriteriaQuery)query);
            selectionQuery.setParameter(this.embeddingAttributeMapping.getAttributeName(), (Object)embedding.vector());
            if (minScore != null) {
                selectionQuery.setParameter("minScore", (Object)minScore);
            }
            if (maxResults != null) {
                selectionQuery.setMaxResults(maxResults.intValue());
            }
            return this.transformToSearchResult(selectionQuery.getResultList());
        });
    }

    public EmbeddingSearchResult<TextSegment> search(EmbeddingSearchRequest request) {
        dev.langchain4j.data.embedding.Embedding referenceEmbedding = request.queryEmbedding();
        int maxResults = request.maxResults();
        double minScore = request.minScore();
        Filter filter = request.filter();
        JpaCriteriaQuery<Object[]> query = this.createBaseQuery(Object[].class, true, (root, cb) -> filter == null ? null : this.createPredicateFromFilter((JpaRoot)root, filter, (HibernateCriteriaBuilder)cb));
        this.applyEmbeddingSearchResultSelections(query);
        return (EmbeddingSearchResult)this.sessionFactory.fromStatelessSession(session -> {
            SelectionQuery selectionQuery = session.createSelectionQuery((CriteriaQuery)query);
            selectionQuery.setParameter(this.embeddingAttributeMapping.getAttributeName(), (Object)referenceEmbedding.vector());
            selectionQuery.setParameter("minScore", (Object)minScore);
            selectionQuery.setMaxResults(maxResults);
            return this.transformToSearchResult(selectionQuery.getResultList());
        });
    }

    private EmbeddingSearchResult<TextSegment> transformToSearchResult(List<Object[]> tuples) {
        ArrayList<EmbeddingMatch> result = new ArrayList<EmbeddingMatch>(tuples.size());
        for (Object[] tuple : tuples) {
            Double score = (Double)tuple[0];
            Object embeddingId = tuple[1];
            dev.langchain4j.data.embedding.Embedding embedding = new dev.langchain4j.data.embedding.Embedding((float[])tuple[2]);
            String text = this.embeddedTextAttributeMapping == null ? null : (String)tuple[4];
            TextSegment segment = null;
            if (Utils.isNotNullOrBlank((String)text)) {
                Metadata metadata;
                Object textMetadata = tuple[3];
                if (textMetadata instanceof String) {
                    String metadataJson = (String)textMetadata;
                    try {
                        metadata = new Metadata((Map)OBJECT_MAPPER.readValue((String)Utils.getOrDefault((Object)metadataJson, (Object)"{}"), Map.class));
                    }
                    catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                } else if (textMetadata instanceof Map) {
                    Map metadataMap = (Map)textMetadata;
                    metadata = new Metadata(metadataMap);
                } else if (textMetadata == null) {
                    metadata = new Metadata();
                } else {
                    throw new IllegalArgumentException("Text metadata must be of type String or Map but got: " + textMetadata.getClass());
                }
                int i = 0;
                for (Map.Entry<String, AttributeMapping> metadataAttribute : this.metadataAttributeMappings.entrySet()) {
                    String metadataAttributePath = metadataAttribute.getKey();
                    JavaType metadataAttributeJavaType = metadataAttribute.getValue().getJavaType();
                    Object metadataValue = tuple[5 + i];
                    if (metadataValue != null) {
                        if (metadataValue instanceof String) {
                            String string = (String)metadataValue;
                            metadata.put(metadataAttributePath, string);
                        } else if (metadataValue instanceof UUID) {
                            UUID uuid = (UUID)metadataValue;
                            metadata.put(metadataAttributePath, uuid);
                        } else if (metadataValue instanceof Integer) {
                            Integer integerValue = (Integer)metadataValue;
                            metadata.put(metadataAttributePath, integerValue.intValue());
                        } else if (metadataValue instanceof Long) {
                            Long longValue = (Long)metadataValue;
                            metadata.put(metadataAttributePath, longValue.longValue());
                        } else if (metadataValue instanceof Float) {
                            Float floatValue = (Float)metadataValue;
                            metadata.put(metadataAttributePath, floatValue.floatValue());
                        } else if (metadataValue instanceof Double) {
                            Double doubleValue = (Double)metadataValue;
                            metadata.put(metadataAttributePath, doubleValue.doubleValue());
                        } else {
                            metadata.put(metadataAttributePath, metadataAttributeJavaType.toString(metadataValue));
                        }
                    }
                    ++i;
                }
                segment = TextSegment.from((String)text, (Metadata)metadata);
            }
            result.add(new EmbeddingMatch(score, this.idType.toString(embeddingId), embedding, segment));
        }
        return new EmbeddingSearchResult(result);
    }

    private <T> JpaCriteriaQuery<T> createBaseQuery(Class<T> resultClass, boolean minScoreFilter, BiFunction<JpaRoot<E>, HibernateCriteriaBuilder, Predicate> additionalPredicateBuilder) {
        Predicate additonalPredicate;
        HibernateCriteriaBuilder criteriaBuilder = this.sessionFactory.getCriteriaBuilder();
        JpaCriteriaQuery query = criteriaBuilder.createQuery(resultClass);
        JpaRoot root = query.from(this.entityClass);
        JpaPath embeddingPath = root.get(this.embeddingAttributeMapping.getAttributeName());
        JpaParameterExpression embeddingParameter = criteriaBuilder.parameter(HibernateEmbeddingStore.class, this.embeddingAttributeMapping.getAttributeName());
        Expression<Double> distance = this.distance(this.distanceFunction, (Expression<float[]>)embeddingPath, (Expression<float[]>)embeddingParameter, (CriteriaBuilder)criteriaBuilder);
        Predicate predicate = minScoreFilter ? this.distanceFilter(this.distanceFunction, distance, (Expression<Double>)criteriaBuilder.parameter(Double.class, "minScore"), (CriteriaBuilder)criteriaBuilder) : null;
        Predicate predicate2 = additonalPredicate = additionalPredicateBuilder == null ? null : additionalPredicateBuilder.apply(root, criteriaBuilder);
        query.where((Expression)(additonalPredicate == null ? predicate : (predicate == null ? additonalPredicate : criteriaBuilder.and((Expression)predicate, (Expression)additonalPredicate))));
        query.orderBy(new Order[]{criteriaBuilder.asc(distance)});
        return query;
    }

    private void applyEmbeddingSearchResultSelections(JpaCriteriaQuery<Object[]> query) {
        HibernateCriteriaBuilder criteriaBuilder = this.sessionFactory.getCriteriaBuilder();
        JpaRoot root = (JpaRoot)query.getRoots().iterator().next();
        Expression distance = ((Order)query.getOrderList().get(0)).getExpression();
        int metadataOffset = this.embeddedTextAttributeMapping == null ? 4 : 5;
        Selection[] selections = new Selection[metadataOffset + this.metadataAttributeMappings.size()];
        selections[0] = this.score(this.distanceFunction, (Expression<Double>)distance, (CriteriaBuilder)criteriaBuilder);
        selections[1] = root.get(this.idAttributeMapping.getAttributeName());
        selections[2] = root.get(this.embeddingAttributeMapping.getAttributeName());
        selections[3] = root.get(this.unmappedMetadataAttributeMapping.getAttributeName());
        if (this.embeddedTextAttributeMapping != null) {
            selections[4] = root.get(this.embeddedTextAttributeMapping.getAttributeName());
        }
        int index = metadataOffset;
        for (String attributePath : this.metadataAttributeMappings.keySet()) {
            selections[index++] = this.get(root, attributePath);
        }
        query.select((Selection)criteriaBuilder.array(selections));
    }

    private Expression<Double> distance(DistanceFunction distanceFunction, Expression<float[]> lhs, Expression<float[]> rhs, CriteriaBuilder criteriaBuilder) {
        String functionName = switch (distanceFunction) {
            default -> throw new IncompatibleClassChangeError();
            case DistanceFunction.COSINE -> "cosine_distance";
            case DistanceFunction.EUCLIDEAN -> "euclidean_distance";
            case DistanceFunction.EUCLIDEAN_SQUARED -> "euclidean_square_distance";
            case DistanceFunction.MANHATTAN -> "taxicab_distance";
            case DistanceFunction.INNER_PRODUCT -> "inner_product";
            case DistanceFunction.NEGATIVE_INNER_PRODUCT -> "negative_inner_product";
            case DistanceFunction.HAMMING -> "hamming_distance";
            case DistanceFunction.JACCARD -> "jaccard_distance";
        };
        return criteriaBuilder.function(functionName, Double.class, new Expression[]{lhs, rhs});
    }

    protected Expression<Double> score(DistanceFunction distanceFunction, Expression<Double> distance, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.quot(criteriaBuilder.diff(criteriaBuilder.literal((Object)2.0), distance), criteriaBuilder.literal((Object)2.0)).as(Double.class);
    }

    protected Predicate distanceFilter(DistanceFunction distanceFunction, Expression<Double> distance, Expression<Double> minScore, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.le(distance, criteriaBuilder.function("round", Double.class, new Expression[]{criteriaBuilder.diff(criteriaBuilder.literal((Object)2.0), criteriaBuilder.prod(criteriaBuilder.literal((Object)2.0), minScore)), criteriaBuilder.literal((Object)8)}));
    }

    private <X> Predicate createPredicateFromFilter(JpaRoot<X> root, Filter filter, HibernateCriteriaBuilder criteriaBuilder) {
        if (filter instanceof ContainsString) {
            ContainsString containsString = (ContainsString)filter;
            return this.mapContains(root, criteriaBuilder, containsString);
        }
        if (filter instanceof IsEqualTo) {
            IsEqualTo isEqualTo = (IsEqualTo)filter;
            return this.mapEqual(root, criteriaBuilder, isEqualTo);
        }
        if (filter instanceof IsNotEqualTo) {
            IsNotEqualTo isNotEqualTo = (IsNotEqualTo)filter;
            return this.mapNotEqual(root, criteriaBuilder, isNotEqualTo);
        }
        if (filter instanceof IsGreaterThan) {
            IsGreaterThan isGreaterThan = (IsGreaterThan)filter;
            return this.mapGreaterThan(root, criteriaBuilder, isGreaterThan);
        }
        if (filter instanceof IsGreaterThanOrEqualTo) {
            IsGreaterThanOrEqualTo isGreaterThanOrEqualTo = (IsGreaterThanOrEqualTo)filter;
            return this.mapGreaterThanOrEqual(root, criteriaBuilder, isGreaterThanOrEqualTo);
        }
        if (filter instanceof IsLessThan) {
            IsLessThan isLessThan = (IsLessThan)filter;
            return this.mapLessThan(root, criteriaBuilder, isLessThan);
        }
        if (filter instanceof IsLessThanOrEqualTo) {
            IsLessThanOrEqualTo isLessThanOrEqualTo = (IsLessThanOrEqualTo)filter;
            return this.mapLessThanOrEqual(root, criteriaBuilder, isLessThanOrEqualTo);
        }
        if (filter instanceof IsIn) {
            IsIn isIn = (IsIn)filter;
            return this.mapIn(root, criteriaBuilder, isIn);
        }
        if (filter instanceof IsNotIn) {
            IsNotIn isNotIn = (IsNotIn)filter;
            return this.mapNotIn(root, criteriaBuilder, isNotIn);
        }
        if (filter instanceof And) {
            And and = (And)filter;
            return this.mapAnd(root, criteriaBuilder, and);
        }
        if (filter instanceof Not) {
            Not not = (Not)filter;
            return this.mapNot(root, criteriaBuilder, not);
        }
        if (filter instanceof Or) {
            Or or = (Or)filter;
            return this.mapOr(root, criteriaBuilder, or);
        }
        throw new UnsupportedOperationException("Unsupported filter type: " + filter.getClass().getName());
    }

    private <X> JpaPath<X> get(JpaRoot<?> root, String path) {
        if (path.indexOf(46) == -1) {
            return root.get(path);
        }
        JpaPath p = root;
        StringTokenizer tokenizer = new StringTokenizer(path, ".");
        while (tokenizer.hasMoreTokens()) {
            p = p.get(tokenizer.nextToken());
        }
        return p;
    }

    private Object toDomainValue(JavaType<?> attributeJavaType, Object value) {
        Object object;
        if (attributeJavaType.getJavaTypeClass() == String.class) {
            return value.toString();
        }
        if (value instanceof String) {
            String s = (String)value;
            object = attributeJavaType.fromString((CharSequence)s);
        } else {
            object = value;
        }
        return object;
    }

    private JpaPredicate mapContains(JpaRoot<?> root, HibernateCriteriaBuilder criteriaBuilder, ContainsString containsString) {
        AttributeMapping attributeMapping = this.metadataAttributeMappings.get(containsString.key());
        JpaJsonValueExpression expression = attributeMapping != null ? this.get(root, containsString.key()) : criteriaBuilder.jsonValue((Expression)root.get(this.unmappedMetadataAttributeMapping.getAttributeName()), (Expression)criteriaBuilder.literal((Object)("$." + containsString.key())));
        return criteriaBuilder.and((Expression)expression.isNotNull(), (Expression)criteriaBuilder.like((Expression)expression, "%" + containsString.comparisonValue().replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_") + "%", '\\'));
    }

    private JpaPredicate mapEqual(JpaRoot<?> root, HibernateCriteriaBuilder criteriaBuilder, IsEqualTo isEqualTo) {
        AttributeMapping attributeMapping = this.metadataAttributeMappings.get(isEqualTo.key());
        if (attributeMapping != null) {
            JpaPath valueExpression = this.get(root, isEqualTo.key());
            Object comparisonValue = isEqualTo.comparisonValue();
            Object domainValue = this.toDomainValue(attributeMapping.getJavaType(), comparisonValue);
            return criteriaBuilder.and((Expression)valueExpression.isNotNull(), (Expression)criteriaBuilder.equal(valueExpression, domainValue));
        }
        JpaJsonValueExpression valueExpression = criteriaBuilder.jsonValue((Expression)root.get(this.unmappedMetadataAttributeMapping.getAttributeName()), (Expression)criteriaBuilder.literal((Object)("$." + isEqualTo.key())), isEqualTo.comparisonValue().getClass());
        return criteriaBuilder.and((Expression)valueExpression.isNotNull(), (Expression)criteriaBuilder.equal((Expression)valueExpression, isEqualTo.comparisonValue()));
    }

    private JpaPredicate mapNotEqual(JpaRoot<?> root, HibernateCriteriaBuilder criteriaBuilder, IsNotEqualTo isNotEqualTo) {
        AttributeMapping attributeMapping = this.metadataAttributeMappings.get(isNotEqualTo.key());
        if (attributeMapping != null) {
            JpaPath valueExpression = this.get(root, isNotEqualTo.key());
            Object comparisonValue = isNotEqualTo.comparisonValue();
            Object domainValue = this.toDomainValue(attributeMapping.getJavaType(), comparisonValue);
            return criteriaBuilder.or((Expression)valueExpression.isNull(), (Expression)criteriaBuilder.notEqual(valueExpression, domainValue));
        }
        JpaJsonValueExpression valueExpression = criteriaBuilder.jsonValue((Expression)root.get(this.unmappedMetadataAttributeMapping.getAttributeName()), (Expression)criteriaBuilder.literal((Object)("$." + isNotEqualTo.key())), isNotEqualTo.comparisonValue().getClass());
        return criteriaBuilder.or((Expression)valueExpression.isNull(), (Expression)criteriaBuilder.notEqual((Expression)valueExpression, isNotEqualTo.comparisonValue()));
    }

    private JpaPredicate mapIn(JpaRoot<?> root, HibernateCriteriaBuilder criteriaBuilder, IsIn isIn) {
        AttributeMapping attributeMapping = this.metadataAttributeMappings.get(isIn.key());
        if (attributeMapping != null) {
            Collection domainValue = isIn.comparisonValues().stream().map(value -> this.toDomainValue(attributeMapping.getJavaType(), value)).collect(Collectors.toList());
            return criteriaBuilder.in(this.get(root, isIn.key()), domainValue);
        }
        return criteriaBuilder.in((Expression)criteriaBuilder.jsonValue((Expression)root.get(this.unmappedMetadataAttributeMapping.getAttributeName()), (Expression)criteriaBuilder.literal((Object)("$." + isIn.key()))), (Collection)isIn.comparisonValues().stream().map(Object::toString).collect(Collectors.toList()));
    }

    private JpaPredicate mapNotIn(JpaRoot<?> root, HibernateCriteriaBuilder criteriaBuilder, IsNotIn isNotIn) {
        AttributeMapping attributeMapping = this.metadataAttributeMappings.get(isNotIn.key());
        if (attributeMapping != null) {
            JpaPath valueExpression = this.get(root, isNotIn.key());
            Collection domainValue = isNotIn.comparisonValues().stream().map(value -> this.toDomainValue(attributeMapping.getJavaType(), value)).collect(Collectors.toList());
            return criteriaBuilder.or((Expression)valueExpression.isNull(), (Expression)criteriaBuilder.in(valueExpression, domainValue).not());
        }
        JpaJsonValueExpression valueExpression = criteriaBuilder.jsonValue((Expression)root.get(this.unmappedMetadataAttributeMapping.getAttributeName()), (Expression)criteriaBuilder.literal((Object)("$." + isNotIn.key())));
        return criteriaBuilder.or((Expression)valueExpression.isNull(), (Expression)criteriaBuilder.in((Expression)valueExpression, (Collection)isNotIn.comparisonValues().stream().map(Object::toString).collect(Collectors.toList())).not());
    }

    private JpaPredicate mapNot(JpaRoot<?> root, HibernateCriteriaBuilder criteriaBuilder, Not not) {
        return criteriaBuilder.not((Expression)this.createPredicateFromFilter(root, not.expression(), criteriaBuilder));
    }

    private JpaPredicate mapAnd(JpaRoot<?> root, HibernateCriteriaBuilder criteriaBuilder, And and) {
        return criteriaBuilder.and((Expression)this.createPredicateFromFilter(root, and.left(), criteriaBuilder), (Expression)this.createPredicateFromFilter(root, and.right(), criteriaBuilder));
    }

    private JpaPredicate mapOr(JpaRoot<?> root, HibernateCriteriaBuilder criteriaBuilder, Or or) {
        return criteriaBuilder.or((Expression)this.createPredicateFromFilter(root, or.left(), criteriaBuilder), (Expression)this.createPredicateFromFilter(root, or.right(), criteriaBuilder));
    }

    private <X, Y extends Comparable<? super Y>> JpaPredicate mapGreaterThan(JpaRoot<X> root, HibernateCriteriaBuilder criteriaBuilder, IsGreaterThan isGreaterThan) {
        AttributeMapping attributeMapping = this.metadataAttributeMappings.get(isGreaterThan.key());
        if (attributeMapping != null) {
            Comparable comparisonValue = isGreaterThan.comparisonValue();
            if (attributeMapping.getJavaType().getJavaTypeClass() == String.class && !(comparisonValue instanceof String)) {
                return criteriaBuilder.greaterThan((Expression)this.get(root, isGreaterThan.key()).cast(comparisonValue.getClass()), comparisonValue);
            }
            Object domainValue = this.toDomainValue(attributeMapping.getJavaType(), comparisonValue);
            return criteriaBuilder.greaterThan(this.get(root, isGreaterThan.key()), (Comparable)domainValue);
        }
        return criteriaBuilder.greaterThan((Expression)criteriaBuilder.jsonValue((Expression)root.get(this.unmappedMetadataAttributeMapping.getAttributeName()), (Expression)criteriaBuilder.literal((Object)("$." + isGreaterThan.key())), isGreaterThan.comparisonValue().getClass()), isGreaterThan.comparisonValue());
    }

    private <X, Y extends Comparable<? super Y>> JpaPredicate mapGreaterThanOrEqual(JpaRoot<X> root, HibernateCriteriaBuilder criteriaBuilder, IsGreaterThanOrEqualTo isGreaterThanOrEqualTo) {
        AttributeMapping attributeMapping = this.metadataAttributeMappings.get(isGreaterThanOrEqualTo.key());
        if (attributeMapping != null) {
            Comparable comparisonValue = isGreaterThanOrEqualTo.comparisonValue();
            if (attributeMapping.getJavaType().getJavaTypeClass() == String.class && !(comparisonValue instanceof String)) {
                return criteriaBuilder.greaterThanOrEqualTo((Expression)this.get(root, isGreaterThanOrEqualTo.key()).cast(comparisonValue.getClass()), comparisonValue);
            }
            Object domainValue = this.toDomainValue(attributeMapping.getJavaType(), comparisonValue);
            return criteriaBuilder.greaterThanOrEqualTo(this.get(root, isGreaterThanOrEqualTo.key()), (Comparable)domainValue);
        }
        return criteriaBuilder.greaterThanOrEqualTo((Expression)criteriaBuilder.jsonValue((Expression)root.get(this.unmappedMetadataAttributeMapping.getAttributeName()), (Expression)criteriaBuilder.literal((Object)("$." + isGreaterThanOrEqualTo.key())), isGreaterThanOrEqualTo.comparisonValue().getClass()), isGreaterThanOrEqualTo.comparisonValue());
    }

    private <X, Y extends Comparable<? super Y>> JpaPredicate mapLessThan(JpaRoot<X> root, HibernateCriteriaBuilder criteriaBuilder, IsLessThan isLessThan) {
        AttributeMapping attributeMapping = this.metadataAttributeMappings.get(isLessThan.key());
        if (attributeMapping != null) {
            Comparable comparisonValue = isLessThan.comparisonValue();
            if (attributeMapping.getJavaType().getJavaTypeClass() == String.class && !(comparisonValue instanceof String)) {
                return criteriaBuilder.lessThan((Expression)this.get(root, isLessThan.key()).cast(comparisonValue.getClass()), comparisonValue);
            }
            Object domainValue = this.toDomainValue(attributeMapping.getJavaType(), comparisonValue);
            return criteriaBuilder.lessThan(this.get(root, isLessThan.key()), (Comparable)domainValue);
        }
        return criteriaBuilder.lessThan((Expression)criteriaBuilder.jsonValue((Expression)root.get(this.unmappedMetadataAttributeMapping.getAttributeName()), (Expression)criteriaBuilder.literal((Object)("$." + isLessThan.key())), isLessThan.comparisonValue().getClass()), isLessThan.comparisonValue());
    }

    private <X, Y extends Comparable<? super Y>> JpaPredicate mapLessThanOrEqual(JpaRoot<X> root, HibernateCriteriaBuilder criteriaBuilder, IsLessThanOrEqualTo isLessThanOrEqualTo) {
        AttributeMapping attributeMapping = this.metadataAttributeMappings.get(isLessThanOrEqualTo.key());
        if (attributeMapping != null) {
            Comparable comparisonValue = isLessThanOrEqualTo.comparisonValue();
            if (attributeMapping.getJavaType().getJavaTypeClass() == String.class && !(comparisonValue instanceof String)) {
                return criteriaBuilder.lessThanOrEqualTo((Expression)this.get(root, isLessThanOrEqualTo.key()).cast(comparisonValue.getClass()), comparisonValue);
            }
            Object domainValue = this.toDomainValue(attributeMapping.getJavaType(), comparisonValue);
            return criteriaBuilder.lessThanOrEqualTo(this.get(root, isLessThanOrEqualTo.key()), (Comparable)domainValue);
        }
        return criteriaBuilder.lessThanOrEqualTo((Expression)criteriaBuilder.jsonValue((Expression)root.get(this.unmappedMetadataAttributeMapping.getAttributeName()), (Expression)criteriaBuilder.literal((Object)("$." + isLessThanOrEqualTo.key())), isLessThanOrEqualTo.comparisonValue().getClass()), isLessThanOrEqualTo.comparisonValue());
    }

    private void addInternal(String id, dev.langchain4j.data.embedding.Embedding embedding, TextSegment embedded) {
        this.addAll(Collections.singletonList(id), Collections.singletonList(embedding), embedded == null ? null : Collections.singletonList(embedded));
    }

    public void addAllEntities(List<?> entities) {
        if (Utils.isNullOrEmpty(entities)) {
            log.info("Empty entities - no ops");
            return;
        }
        this.sessionFactory.inStatelessTransaction(session -> session.insertMultiple(entities));
    }

    public void applyEmbeddings(List<? extends E> entities, EmbeddingModel embeddingModel) {
        List<TextSegment> textSegments = this.createTextSegments(entities);
        List embeddings = (List)embeddingModel.embedAll(textSegments).content();
        for (int i = 0; i < entities.size(); ++i) {
            this.embeddingAttributeMapping.setValue(entities.get(i), (Object)((dev.langchain4j.data.embedding.Embedding)embeddings.get(i)).vector());
        }
    }

    public List<TextSegment> createTextSegments(List<? extends E> entities) {
        if (this.embeddedTextAttributeMapping == null) {
            throw new IllegalStateException("Embedding entity [" + this.entityClass.getName() + "] has no text attribute mapping, so can't create text segments");
        }
        ArrayList<TextSegment> textSegments = new ArrayList<TextSegment>(entities.size());
        for (E entity : entities) {
            String text = (String)this.embeddedTextAttributeMapping.getValue(entity);
            if (text == null) {
                textSegments.add(null);
                continue;
            }
            Metadata metadata = new Metadata((Map)this.unmappedMetadataAttributeMapping.getValue(entity));
            for (Map.Entry<String, AttributeMapping> metadataAttribute : this.metadataAttributeMappings.entrySet()) {
                String metadataAttributePath = metadataAttribute.getKey();
                JavaType metadataAttributeJavaType = metadataAttribute.getValue().getJavaType();
                Object metadataValue = metadataAttribute.getValue().getValue(entity);
                if (metadataValue == null) continue;
                if (metadataValue instanceof String) {
                    String string = (String)metadataValue;
                    metadata.put(metadataAttributePath, string);
                    continue;
                }
                if (metadataValue instanceof UUID) {
                    UUID uuid = (UUID)metadataValue;
                    metadata.put(metadataAttributePath, uuid);
                    continue;
                }
                if (metadataValue instanceof Integer) {
                    Integer integerValue = (Integer)metadataValue;
                    metadata.put(metadataAttributePath, integerValue.intValue());
                    continue;
                }
                if (metadataValue instanceof Long) {
                    Long longValue = (Long)metadataValue;
                    metadata.put(metadataAttributePath, longValue.longValue());
                    continue;
                }
                if (metadataValue instanceof Float) {
                    Float floatValue = (Float)metadataValue;
                    metadata.put(metadataAttributePath, floatValue.floatValue());
                    continue;
                }
                if (metadataValue instanceof Double) {
                    Double doubleValue = (Double)metadataValue;
                    metadata.put(metadataAttributePath, doubleValue.doubleValue());
                    continue;
                }
                metadata.put(metadataAttributePath, metadataAttributeJavaType.toString(metadataValue));
            }
            textSegments.add(TextSegment.from((String)text, (Metadata)metadata));
        }
        return textSegments;
    }

    public List<String> generateIds(int n) {
        List ids = (List)this.sessionFactory.fromStatelessTransaction(session -> this.generateIds(n, (SharedSessionContractImplementor)session));
        ArrayList<String> idStrings = new ArrayList<String>(ids.size());
        for (Object id : ids) {
            idStrings.add(this.idType.toString(id));
        }
        return idStrings;
    }

    private ArrayList<Object> generateIds(int n, SharedSessionContractImplementor session) {
        ArrayList<Object> ids;
        if (!this.idGenerator.allowAssignedIdentifiers()) {
            throw new IllegalStateException("Entity does not allow generating identifiers and assigning them separately");
        }
        Generator generator = this.idGenerator;
        if (generator instanceof BeforeExecutionGenerator) {
            BeforeExecutionGenerator beforeExecutionGenerator = (BeforeExecutionGenerator)generator;
            ids = new ArrayList<Object>(n);
            for (int i = 0; i < n; ++i) {
                ids.add(beforeExecutionGenerator.generate(session, null, null, EventType.INSERT));
            }
            return ids;
        }
        if (this.allowUuidGeneration) {
            ids = new ArrayList(n);
            if (String.class.isAssignableFrom(this.idType.getJavaTypeClass())) {
                for (int i = 0; i < n; ++i) {
                    ids.add(Utils.randomUUID());
                }
            } else {
                for (int i = 0; i < n; ++i) {
                    ids.add(UUID.randomUUID());
                }
            }
            return ids;
        }
        throw new IllegalStateException("Can't generate identifiers for identifier type " + this.idType.getJavaTypeClass().getName() + " without a generator");
    }

    public List<String> addAll(List<dev.langchain4j.data.embedding.Embedding> embeddings, List<TextSegment> embedded) {
        if (!this.idGenerator.allowAssignedIdentifiers() || this.idGenerator.generatedOnExecution()) {
            if (Utils.isNullOrEmpty(embeddings)) {
                log.info("Empty embeddings - no ops");
                return Collections.emptyList();
            }
            ValidationUtils.ensureTrue((embedded == null || embeddings.size() == embedded.size() ? 1 : 0) != 0, (String)"embeddings size is not equal to embedded size");
            ArrayList<Object> entities = this.createEntities(embeddings, embedded);
            this.sessionFactory.inStatelessTransaction(session -> {
                if (!this.idGenerator.generatesSometimes() && this.allowUuidGeneration) {
                    SharedSessionContractImplementor sharedSessionContractImplementor = (SharedSessionContractImplementor)session;
                    boolean convertToString = String.class.isAssignableFrom(this.idType.getJavaTypeClass());
                    for (Object entity : entities) {
                        UUID uuid = UUID.randomUUID();
                        Object id = convertToString ? uuid.toString() : uuid;
                        this.entityPersister.setIdentifier(entity, id, sharedSessionContractImplementor);
                    }
                }
                session.insertMultiple((List)entities);
            });
            ArrayList<String> idStrings = new ArrayList<String>(embeddings.size());
            for (Object entity : entities) {
                idStrings.add(this.idType.toString(this.entityPersister.getIdentifier(entity)));
            }
            return idStrings;
        }
        return (List)this.sessionFactory.fromStatelessTransaction(session -> {
            ArrayList<Object> ids = this.generateIds(embeddings.size(), (SharedSessionContractImplementor)session);
            this.addAll((List<Object>)ids, embeddings, embedded, (StatelessSession)session);
            ArrayList<String> idStrings = new ArrayList<String>(ids.size());
            for (Object id : ids) {
                idStrings.add(this.idType.toString(id));
            }
            return idStrings;
        });
    }

    public void addAll(List<String> idStrings, List<dev.langchain4j.data.embedding.Embedding> embeddings, List<TextSegment> embedded) {
        ArrayList<Object> ids = new ArrayList<Object>(idStrings.size());
        for (String id : idStrings) {
            ids.add(this.idType.fromString((CharSequence)id));
        }
        this.sessionFactory.inStatelessTransaction(session -> this.addAll((List<Object>)ids, embeddings, embedded, (StatelessSession)session));
    }

    private ArrayList<Object> createEntities(List<dev.langchain4j.data.embedding.Embedding> embeddings, List<TextSegment> embedded) {
        EntityInstantiator instantiator = this.entityPersister.getRepresentationStrategy().getInstantiator();
        ArrayList<Object> entities = new ArrayList<Object>(embeddings.size());
        Object[] values = new Object[this.entityPersister.getNumberOfAttributeMappings()];
        for (int i = 0; i < embeddings.size(); ++i) {
            values[this.embeddingAttributeMapping.getStateArrayPosition()] = embeddings.get(i).vector();
            if (embedded != null && embedded.get(i) != null) {
                if (this.embeddedTextAttributeMapping != null) {
                    values[this.embeddedTextAttributeMapping.getStateArrayPosition()] = embedded.get(i).text();
                }
                Map metadataMap = Utils.toStringValueMap((Map)embedded.get(i).metadata().toMap());
                for (Map.Entry<String, AttributeMapping> entry : this.metadataAttributeMappings.entrySet()) {
                    Object value;
                    String attributePath = entry.getKey();
                    String stringValue = (String)metadataMap.remove(attributePath);
                    Object object = value = stringValue == null ? null : entry.getValue().getJavaType().fromString((CharSequence)stringValue);
                    if (entry.getValue().getDeclaringType() != this.entityPersister) {
                        if (value == null) continue;
                        throw new IllegalArgumentException("Can't add metadata from TextSegment for attribute path: " + attributePath);
                    }
                    values[entry.getValue().getStateArrayPosition()] = value;
                }
                if (this.unmappedMetadataAttributeMapType != null) {
                    values[this.unmappedMetadataAttributeMapping.getStateArrayPosition()] = metadataMap;
                } else {
                    try {
                        values[this.unmappedMetadataAttributeMapping.getStateArrayPosition()] = OBJECT_MAPPER.writeValueAsString((Object)metadataMap);
                    }
                    catch (JsonProcessingException jsonProcessingException) {
                        throw new RuntimeException(jsonProcessingException);
                    }
                }
            } else {
                if (this.embeddedTextAttributeMapping != null) {
                    values[this.embeddedTextAttributeMapping.getStateArrayPosition()] = null;
                }
                values[this.unmappedMetadataAttributeMapping.getStateArrayPosition()] = null;
                for (Map.Entry entry : this.metadataAttributeMappings.entrySet()) {
                    if (((AttributeMapping)entry.getValue()).getDeclaringType() == this.entityPersister) continue;
                    values[((AttributeMapping)entry.getValue()).getStateArrayPosition()] = null;
                }
            }
            Object entity = instantiator.instantiate();
            this.entityPersister.setValues(entity, values);
            entities.add(entity);
        }
        return entities;
    }

    private void addAll(List<Object> ids, List<dev.langchain4j.data.embedding.Embedding> embeddings, List<TextSegment> embedded, StatelessSession session) {
        if (Utils.isNullOrEmpty(ids) || Utils.isNullOrEmpty(embeddings)) {
            log.info("Empty embeddings - no ops");
            return;
        }
        ValidationUtils.ensureTrue((ids.size() == embeddings.size() ? 1 : 0) != 0, (String)"ids size is not equal to embeddings size");
        ValidationUtils.ensureTrue((embedded == null || embeddings.size() == embedded.size() ? 1 : 0) != 0, (String)"embeddings size is not equal to embedded size");
        if (!this.idGenerator.allowAssignedIdentifiers()) {
            throw new IllegalStateException("Entity does not allow assigning identifiers");
        }
        MutationQuery mutationQuery = session.createMutationQuery(this.insertValues);
        for (int i = 0; i < ids.size(); ++i) {
            mutationQuery.setParameter(this.idAttributeMapping.getAttributeName(), ids.get(i));
            mutationQuery.setParameter(this.embeddingAttributeMapping.getAttributeName(), (Object)embeddings.get(i).vector());
            if (embedded != null && embedded.get(i) != null) {
                if (this.embeddedTextAttributeMapping != null) {
                    mutationQuery.setParameter(this.embeddedTextAttributeMapping.getAttributeName(), (Object)embedded.get(i).text());
                }
                Map metadataMap = Utils.toStringValueMap((Map)embedded.get(i).metadata().toMap());
                for (Map.Entry<String, AttributeMapping> entry : this.metadataAttributeMappings.entrySet()) {
                    String attributePath = entry.getKey();
                    String stringValue = (String)metadataMap.remove(attributePath);
                    Object value = stringValue == null ? null : entry.getValue().getJavaType().fromString((CharSequence)stringValue);
                    mutationQuery.setParameter(attributePath, value);
                }
                if (this.unmappedMetadataAttributeMapType != null) {
                    mutationQuery.setParameter(this.unmappedMetadataAttributeMapping.getAttributeName(), (Object)metadataMap, this.unmappedMetadataAttributeMapType);
                } else {
                    try {
                        mutationQuery.setParameter(this.unmappedMetadataAttributeMapping.getAttributeName(), (Object)OBJECT_MAPPER.writeValueAsString((Object)metadataMap));
                    }
                    catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                if (this.embeddedTextAttributeMapping != null) {
                    mutationQuery.setParameter(this.embeddedTextAttributeMapping.getAttributeName(), null);
                }
                mutationQuery.setParameter(this.unmappedMetadataAttributeMapping.getAttributeName(), null);
                for (String attributePath : this.metadataAttributeMappings.keySet()) {
                    mutationQuery.setParameter(attributePath, null);
                }
            }
            mutationQuery.executeUpdate();
        }
    }

    static {
        boolean isHibernateOrm71 = false;
        try {
            SchemaManager.class.getMethod("truncateTable", String.class);
        }
        catch (NoSuchMethodException e) {
            isHibernateOrm71 = true;
        }
        IS_HIBERNATE_ORM_7_1 = isHibernateOrm71;
    }

    public static class Builder<E> {
        private final Class<E> entityClass;
        private String embeddingAttributeName;
        private String embeddedTextAttributeName;
        private String unmappedMetadataAttributeName;
        private String[] metadataAttributeNames;
        private SessionFactory sessionFactory;
        private DatabaseKind databaseKind;
        private DistanceFunction distanceFunction;

        Builder(Class<E> entityClass) {
            this.entityClass = entityClass;
        }

        public Builder<E> embeddingAttributeName(String embeddingAttributeName) {
            this.embeddingAttributeName = embeddingAttributeName;
            return this;
        }

        public Builder<E> embeddedTextAttributeName(String embeddedTextAttributeName) {
            this.embeddedTextAttributeName = embeddedTextAttributeName;
            return this;
        }

        public Builder<E> unmappedMetadataAttributeName(String unmappedMetadataAttributeName) {
            this.unmappedMetadataAttributeName = unmappedMetadataAttributeName;
            return this;
        }

        public Builder<E> metadataAttributeNames(String ... metadataAttributeNames) {
            this.metadataAttributeNames = metadataAttributeNames;
            return this;
        }

        public Builder<E> sessionFactory(SessionFactory sessionFactory) {
            this.sessionFactory = sessionFactory;
            return this;
        }

        public Builder<E> databaseKind(DatabaseKind databaseKind) {
            this.databaseKind = databaseKind;
            return this;
        }

        public Builder<E> distanceFunction(DistanceFunction distanceFunction) {
            this.distanceFunction = (DistanceFunction)((Object)ValidationUtils.ensureNotNull((Object)((Object)distanceFunction), (String)"distanceFunction"));
            return this;
        }

        public HibernateEmbeddingStore<E> build() {
            DatabaseKind databaseKind;
            String[] metadataAttributeNames;
            String unmappedMetadataAttributeName;
            String embeddedTextAttributeName;
            String embeddingAttributeName;
            DistanceFunction localDistanceFunction = null;
            if (this.embeddingAttributeName == null || this.embeddedTextAttributeName == null || this.unmappedMetadataAttributeName == null || this.metadataAttributeNames == null) {
                EntityType entityType = this.sessionFactory.getMetamodel().entity(this.entityClass);
                SingularAttribute embeddingAttribute = null;
                SingularAttribute embeddedTextAttribute = null;
                SingularAttribute unmappedMetadataAttribute = null;
                LinkedHashSet<String> metadataAttributes = new LinkedHashSet<String>();
                for (SingularAttribute singularAttribute : entityType.getSingularAttributes()) {
                    Member member = singularAttribute.getJavaMember();
                    if (!(member instanceof AnnotatedElement)) continue;
                    AnnotatedElement annotatedElement = (AnnotatedElement)((Object)member);
                    EmbeddingVector embeddingVector = annotatedElement.getAnnotation(EmbeddingVector.class);
                    if (embeddingVector != null) {
                        if (embeddingAttribute != null) {
                            throw new IllegalArgumentException("Multiple @Embedding/@EmbeddingVector annotated attributes [" + embeddingAttribute.getName() + "," + singularAttribute.getName() + "] found on " + this.entityClass.getName() + ". Please specify the explicit embedding vector attribute name instead");
                        }
                        embeddingAttribute = singularAttribute;
                        localDistanceFunction = embeddingVector.distance();
                    }
                    if (annotatedElement.isAnnotationPresent(Embedding.class)) {
                        if (embeddingAttribute != null) {
                            throw new IllegalArgumentException("Multiple @Embedding/@EmbeddingVector annotated attributes [" + embeddingAttribute.getName() + "," + singularAttribute.getName() + "] found on " + this.entityClass.getName() + ". Please specify the explicit embedding vector attribute name instead");
                        }
                        embeddingAttribute = singularAttribute;
                    }
                    if (annotatedElement.isAnnotationPresent(EmbeddedText.class)) {
                        if (embeddedTextAttribute != null) {
                            throw new IllegalArgumentException("Multiple @EmbeddedText annotated attributes [" + embeddedTextAttribute.getName() + "," + singularAttribute.getName() + "] found on " + this.entityClass.getName() + ". Please specify the explicit embedded text attribute name instead");
                        }
                        embeddedTextAttribute = singularAttribute;
                    }
                    if (annotatedElement.isAnnotationPresent(UnmappedMetadata.class)) {
                        if (unmappedMetadataAttribute != null) {
                            throw new IllegalArgumentException("Multiple @UnmappedMetadata annotated attributes [" + unmappedMetadataAttribute.getName() + "," + singularAttribute.getName() + "] found on " + this.entityClass.getName() + ". Please specify the explicit unmapped metadata attribute name instead");
                        }
                        unmappedMetadataAttribute = singularAttribute;
                    }
                    if (!annotatedElement.isAnnotationPresent(MetadataAttribute.class)) continue;
                    HashSet visitedTypes = new HashSet();
                    visitedTypes.add((ManagedType<?>)entityType);
                    this.collectMetadataAttributes(visitedTypes, metadataAttributes, singularAttribute.getName(), singularAttribute.getType());
                }
                if (embeddingAttribute == null) {
                    throw new IllegalArgumentException("Embedding attribute not found on " + this.entityClass.getName() + ". Did you forget to annotate @Embedding on an attribute?");
                }
                if (unmappedMetadataAttribute == null) {
                    throw new IllegalArgumentException("Text metadata attribute not found on " + this.entityClass.getName() + ". Did you forget to annotate @UnmappedMetadata on an attribute?");
                }
                embeddingAttributeName = embeddingAttribute.getName();
                embeddedTextAttributeName = embeddedTextAttribute == null ? null : embeddedTextAttribute.getName();
                unmappedMetadataAttributeName = unmappedMetadataAttribute.getName();
                metadataAttributeNames = metadataAttributes.toArray(new String[0]);
            } else {
                embeddingAttributeName = this.embeddingAttributeName;
                embeddedTextAttributeName = this.embeddedTextAttributeName;
                unmappedMetadataAttributeName = this.unmappedMetadataAttributeName;
                metadataAttributeNames = this.metadataAttributeNames;
            }
            if (this.databaseKind == null) {
                databaseKind = DatabaseKind.determineDatabaseKind(((JdbcServices)this.sessionFactory.unwrap(JdbcServices.class)).getDialect());
                if (databaseKind == null) {
                    throw new IllegalArgumentException("Could not determine DatabaseKind based on dialect. Please configure it explicitly");
                }
            } else {
                databaseKind = this.databaseKind;
            }
            DistanceFunction distanceFunction = this.distanceFunction != null ? this.distanceFunction : (localDistanceFunction != null ? localDistanceFunction : DistanceFunction.COSINE);
            return new HibernateEmbeddingStore<E>(false, this.sessionFactory, databaseKind, this.entityClass, embeddingAttributeName, embeddedTextAttributeName, unmappedMetadataAttributeName, metadataAttributeNames, distanceFunction);
        }

        private void collectMetadataAttributes(Set<ManagedType<?>> visitedTypes, LinkedHashSet<String> metadataAttributes, String path, Type<?> type) {
            if (type instanceof ManagedType) {
                ManagedType managedType = (ManagedType)type;
                if (visitedTypes.add(managedType)) {
                    for (SingularAttribute attribute : managedType.getSingularAttributes()) {
                        AnnotatedElement annotatedElement;
                        Member member = attribute.getJavaMember();
                        if (!(member instanceof AnnotatedElement) || !(annotatedElement = (AnnotatedElement)((Object)member)).isAnnotationPresent(MetadataAttribute.class)) continue;
                        this.collectMetadataAttributes(visitedTypes, metadataAttributes, path + "." + attribute.getName(), attribute.getType());
                        visitedTypes.remove(managedType);
                    }
                }
            } else {
                metadataAttributes.add(path);
            }
        }

        public String toString() {
            return "HibernateEmbeddingStore.HibernateEmbeddingStoreBuilder(sessionFactory=" + this.sessionFactory + ", databaseKind=" + this.databaseKind + ", entityClass=" + this.entityClass.getName() + ", embeddingAttributeName=" + this.embeddingAttributeName + ", embeddedTextAttributeName=" + this.embeddedTextAttributeName + ", unmappedMetadataAttributeName=" + this.unmappedMetadataAttributeName + ", metadataAttributeNames=" + Arrays.toString(this.metadataAttributeNames) + ")";
        }
    }

    public static class DynamicBuilder
    extends BaseBuilder<EmbeddingEntity> {
        private String host;
        private int port;
        private String database;
        private String jdbcUrl;
        private String user;
        private String password;

        DynamicBuilder() {
        }

        public DynamicBuilder host(String host) {
            this.host = host;
            return this;
        }

        public DynamicBuilder port(int port) {
            this.port = port;
            return this;
        }

        public DynamicBuilder database(String database) {
            this.database = database;
            return this;
        }

        public DynamicBuilder jdbcUrl(String jdbcUrl) {
            this.jdbcUrl = jdbcUrl;
            return this;
        }

        public DynamicBuilder user(String user) {
            this.user = user;
            return this;
        }

        public DynamicBuilder password(String password) {
            this.password = password;
            return this;
        }

        public DynamicBuilder databaseKind(DatabaseKind databaseKind) {
            super.databaseKind(databaseKind);
            return this;
        }

        public DynamicBuilder table(String table) {
            super.table(table);
            return this;
        }

        public DynamicBuilder dimension(Integer dimension) {
            super.dimension(dimension);
            return this;
        }

        public DynamicBuilder createIndex(Boolean createIndex) {
            super.createIndex(createIndex);
            return this;
        }

        public DynamicBuilder indexType(String indexType) {
            super.indexType(indexType);
            return this;
        }

        public DynamicBuilder indexOptions(String indexOptions) {
            super.indexOptions(indexOptions);
            return this;
        }

        public DynamicBuilder createTable(Boolean createTable) {
            super.createTable(createTable);
            return this;
        }

        public DynamicBuilder dropTableFirst(Boolean dropTableFirst) {
            super.dropTableFirst(dropTableFirst);
            return this;
        }

        public DynamicBuilder distanceFunction(DistanceFunction distanceFunction) {
            super.distanceFunction(distanceFunction);
            return this;
        }

        public HibernateEmbeddingStore<EmbeddingEntity> build() {
            DatabaseKind databaseKind;
            Configuration cfg = this.createConfiguration();
            if (Utils.isNullOrBlank((String)this.jdbcUrl)) {
                databaseKind = (DatabaseKind)ValidationUtils.ensureNotNull((Object)this.databaseKind, (String)"databaseKind");
                String jdbcUrl = databaseKind.createJdbcUrl(ValidationUtils.ensureNotBlank((String)this.host, (String)"host"), this.port, ValidationUtils.ensureNotBlank((String)this.database, (String)"database"));
                cfg.setProperty("jakarta.persistence.jdbc.url", jdbcUrl);
            } else {
                String jdbcUrl = ValidationUtils.ensureNotBlank((String)this.jdbcUrl, (String)"jdbcUrl");
                databaseKind = DatabaseKind.determineDatabaseKind(jdbcUrl);
                if (databaseKind == null) {
                    throw new IllegalArgumentException("Can't determine DatabaseKind for JDBC URL: " + jdbcUrl);
                }
                cfg.setProperty("jakarta.persistence.jdbc.url", jdbcUrl);
            }
            cfg.setProperty("jakarta.persistence.jdbc.user", ValidationUtils.ensureNotBlank((String)this.user, (String)"user"));
            cfg.setProperty("jakarta.persistence.jdbc.password", ValidationUtils.ensureNotBlank((String)this.password, (String)"password"));
            return new HibernateEmbeddingStore<EmbeddingEntity>(true, this.createSessionFactory(cfg, databaseKind), databaseKind, EmbeddingEntity.class, "embedding", "text", "metadata", null, this.distanceFunction);
        }

        public String toString() {
            return "HibernateEmbeddingStore.DynamicBuilder(jdbcUrl=" + this.jdbcUrl + ", databaseKind=" + this.databaseKind + ", user=" + this.user + ", password=" + (this.password == null ? null : "********") + ", table=" + this.table + ", dimension=" + this.dimension + ", createIndex=" + this.createIndex + ", indexType=" + this.indexType + ", indexOptions=(" + this.indexOptions + "), createTable=" + this.createTable + ", dropTableFirst=" + this.dropTableFirst + ", distanceFunction=" + this.distanceFunction + ")";
        }
    }

    public static class DynamicDatasourceBuilder
    extends BaseBuilder<EmbeddingEntity> {
        private DataSource dataSource;

        DynamicDatasourceBuilder() {
        }

        public DynamicDatasourceBuilder dataSource(DataSource datasource) {
            this.dataSource = datasource;
            return this;
        }

        public DynamicDatasourceBuilder databaseKind(DatabaseKind databaseKind) {
            super.databaseKind(databaseKind);
            return this;
        }

        public DynamicDatasourceBuilder table(String table) {
            super.table(table);
            return this;
        }

        public DynamicDatasourceBuilder dimension(Integer dimension) {
            super.dimension(dimension);
            return this;
        }

        public DynamicDatasourceBuilder createIndex(Boolean createIndex) {
            super.createIndex(createIndex);
            return this;
        }

        public DynamicDatasourceBuilder indexType(String indexType) {
            super.indexType(indexType);
            return this;
        }

        public DynamicDatasourceBuilder indexOptions(String indexOptions) {
            super.indexOptions(indexOptions);
            return this;
        }

        public DynamicDatasourceBuilder createTable(Boolean createTable) {
            super.createTable(createTable);
            return this;
        }

        public DynamicDatasourceBuilder dropTableFirst(Boolean dropTableFirst) {
            super.dropTableFirst(dropTableFirst);
            return this;
        }

        public DynamicDatasourceBuilder distanceFunction(DistanceFunction distanceFunction) {
            super.distanceFunction(distanceFunction);
            return this;
        }

        public HibernateEmbeddingStore<EmbeddingEntity> build() {
            Configuration cfg = this.createConfiguration();
            cfg.getProperties().put("jakarta.persistence.nonJtaDataSource", ValidationUtils.ensureNotNull((Object)this.dataSource, (String)"dataSource"));
            DatabaseKind databaseKind = (DatabaseKind)ValidationUtils.ensureNotNull((Object)this.databaseKind, (String)"databaseKind");
            return new HibernateEmbeddingStore<EmbeddingEntity>(true, this.createSessionFactory(cfg, databaseKind), databaseKind, EmbeddingEntity.class, "embedding", "text", "metadata", null, this.distanceFunction);
        }

        public String toString() {
            return "HibernateEmbeddingStore.DynamicDatasourceBuilder(datasource=" + this.dataSource + ", databaseKind=" + this.databaseKind + ", table=" + this.table + ", dimension=" + this.dimension + ", createIndex=" + this.createIndex + ", indexType=" + this.indexType + ", indexOptions=(" + this.indexOptions + "), createTable=" + this.createTable + ", dropTableFirst=" + this.dropTableFirst + ", distanceFunction=" + this.distanceFunction + ")";
        }
    }

    private static class DynamicEmbeddingStoreAdditionalMappingContributor
    implements AdditionalMappingContributor {
        private final int dimension;

        public DynamicEmbeddingStoreAdditionalMappingContributor(int dimension) {
            this.dimension = dimension;
        }

        public void contribute(AdditionalMappingContributions contributions, InFlightMetadataCollector metadata, ResourceStreamLocator resourceStreamLocator, MetadataBuildingContext buildingContext) {
            ((Column)metadata.getEntityBinding(EmbeddingEntity.class.getName()).getProperty("embedding").getValue().getColumns().get(0)).setArrayLength(Integer.valueOf(this.dimension));
        }

        public String getContributorName() {
            return "Langchain4j Hibernate DynamicEmbeddingStore";
        }
    }

    public static class BaseBuilder<E> {
        protected DatabaseKind databaseKind;
        protected String table;
        protected Integer dimension;
        protected Boolean createIndex;
        protected String indexType;
        protected String indexOptions;
        protected Boolean createTable;
        protected Boolean dropTableFirst;
        protected DistanceFunction distanceFunction = DistanceFunction.COSINE;

        protected Configuration createConfiguration() {
            final int dimension = (Integer)ValidationUtils.ensureNotNull((Object)this.dimension, (String)"dimension");
            Configuration cfg = new Configuration(new BootstrapServiceRegistryBuilder().applyClassLoaderService((ClassLoaderService)new ClassLoaderServiceImpl(){

                public <S> Collection<S> loadJavaServices(Class<S> serviceContract) {
                    return serviceContract == AdditionalMappingContributor.class ? Arrays.asList((Object)new DynamicEmbeddingStoreAdditionalMappingContributor(dimension)) : super.loadJavaServices(serviceContract);
                }
            }).build());
            boolean drop = (Boolean)Utils.getOrDefault((Object)this.dropTableFirst, (Object)false);
            boolean create = (Boolean)Utils.getOrDefault((Object)this.createTable, (Object)false);
            if (drop && create) {
                cfg.setSchemaExportAction(Action.CREATE);
            } else if (drop) {
                cfg.setSchemaExportAction(Action.DROP);
            } else if (create) {
                cfg.setSchemaExportAction(Action.CREATE_ONLY);
            } else {
                cfg.setSchemaExportAction(Action.POPULATE);
            }
            String ormXmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<entity-mappings xmlns=\"https://www.hibernate.org/xsd/orm/mapping\" version=\"7.0\">\n\t<package>dev.langchain4j.store.embedding.hibernate</package>\n    <entity class=\"EmbeddingEntity\">\n        <table name=\"" + ValidationUtils.ensureNotBlank((String)this.table, (String)"table") + "\"/>\n    </entity>\n</entity-mappings>";
            cfg.addInputStream((InputStream)new ReaderInputStream((Reader)new StringReader(ormXmlContent)));
            return cfg;
        }

        protected SessionFactory createSessionFactory(Configuration cfg, DatabaseKind databaseKind) {
            boolean index;
            String setupSql = databaseKind.getSetupSql();
            if (setupSql != null) {
                cfg.setProperty("jakarta.persistence.schema-generation.create-source", (Enum)SourceType.SCRIPT_THEN_METADATA);
                cfg.getProperties().put("jakarta.persistence.schema-generation.create-script-source", new StringReader(setupSql));
            }
            String importSqlContent = (index = ((Boolean)Utils.getOrDefault((Object)this.createIndex, (Object)false)).booleanValue()) ? databaseKind.createIndexDDL(this.distanceFunction, this.indexType, this.table, "embedding", this.indexOptions) : null;
            cfg.getProperties().put("jakarta.persistence.sql-load-script-source", new ReaderInputStream((Reader)new StringReader(importSqlContent == null ? "" : importSqlContent)));
            return cfg.buildSessionFactory();
        }

        public BaseBuilder<E> databaseKind(DatabaseKind databaseKind) {
            this.databaseKind = databaseKind;
            return this;
        }

        public BaseBuilder<E> table(String table) {
            this.table = table;
            return this;
        }

        public BaseBuilder<E> dimension(Integer dimension) {
            this.dimension = dimension;
            return this;
        }

        public BaseBuilder<E> createIndex(Boolean createIndex) {
            this.createIndex = createIndex;
            return this;
        }

        public BaseBuilder<E> indexType(String indexType) {
            this.indexType = indexType;
            return this;
        }

        public BaseBuilder<E> indexOptions(String indexOptions) {
            this.indexOptions = indexOptions;
            return this;
        }

        public BaseBuilder<E> createTable(Boolean createTable) {
            this.createTable = createTable;
            return this;
        }

        public BaseBuilder<E> dropTableFirst(Boolean dropTableFirst) {
            this.dropTableFirst = dropTableFirst;
            return this;
        }

        public BaseBuilder<E> distanceFunction(DistanceFunction distanceFunction) {
            this.distanceFunction = (DistanceFunction)((Object)ValidationUtils.ensureNotNull((Object)((Object)distanceFunction), (String)"distanceFunction"));
            return this;
        }
    }
}

