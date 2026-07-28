/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.agent.tool.SearchBehavior
 *  dev.langchain4j.agent.tool.ToolSpecification
 *  dev.langchain4j.internal.Utils
 *  dev.langchain4j.service.IllegalConfigurationException
 *  dev.langchain4j.service.tool.ToolExecutor
 *  dev.langchain4j.service.tool.ToolProvider
 *  dev.langchain4j.service.tool.ToolProviderRequest
 *  dev.langchain4j.service.tool.ToolProviderResult
 *  dev.langchain4j.service.tool.ToolProviderResult$Builder
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.mcp;

import dev.langchain4j.agent.tool.SearchBehavior;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.mcp.McpToolExecutor;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.resourcesastools.McpResourcesAsToolsPresenter;
import dev.langchain4j.service.IllegalConfigurationException;
import dev.langchain4j.service.tool.ToolExecutor;
import dev.langchain4j.service.tool.ToolProvider;
import dev.langchain4j.service.tool.ToolProviderRequest;
import dev.langchain4j.service.tool.ToolProviderResult;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class McpToolProvider
implements ToolProvider {
    private static final Logger log = LoggerFactory.getLogger(McpToolProvider.class);
    private static final Map<String, Object> SEARCH_BEHAVIOR_ALWAYS_VISIBLE = Collections.singletonMap("searchBehavior", SearchBehavior.ALWAYS_VISIBLE);
    private final CopyOnWriteArrayList<McpClient> mcpClients;
    private final boolean failIfOneServerFails;
    private final AtomicReference<BiPredicate<McpClient, ToolSpecification>> mcpToolsFilter;
    private final Function<ToolExecutor, ToolExecutor> toolWrapper;
    private final McpResourcesAsToolsPresenter resourcesAsToolsPresenter;
    private final AtomicReference<BiFunction<McpClient, ToolSpecification, String>> toolNameMapper;
    private final AtomicReference<BiFunction<McpClient, ToolSpecification, ToolSpecification>> toolSpecificationMapper;
    private final Set<String> alwaysVisibleToolNames;

    private McpToolProvider(Builder builder) {
        this.mcpClients = new CopyOnWriteArrayList(builder.mcpClients);
        this.failIfOneServerFails = (Boolean)Utils.getOrDefault((Object)builder.failIfOneServerFails, (Object)false);
        this.mcpToolsFilter = new AtomicReference<BiPredicate>(builder.mcpToolsFilter);
        this.toolWrapper = builder.toolWrapper;
        this.resourcesAsToolsPresenter = builder.resourcesAsToolsPresenter;
        this.toolNameMapper = new AtomicReference<BiFunction>(builder.toolNameMapper);
        this.toolSpecificationMapper = new AtomicReference<BiFunction>(builder.toolSpecificationMapper);
        this.alwaysVisibleToolNames = Utils.copy((Set)builder.alwaysVisibleToolNames);
    }

    protected McpToolProvider(List<McpClient> mcpClients, boolean failIfOneServerFails, BiPredicate<McpClient, ToolSpecification> mcpToolsFilter, Function<ToolExecutor, ToolExecutor> toolWrapper, McpResourcesAsToolsPresenter resourcesAsToolsPresenter, BiFunction<McpClient, ToolSpecification, String> toolNameMapper, BiFunction<McpClient, ToolSpecification, ToolSpecification> toolSpecificationMapper) {
        this.mcpClients = new CopyOnWriteArrayList<McpClient>(mcpClients);
        this.failIfOneServerFails = failIfOneServerFails;
        this.mcpToolsFilter = new AtomicReference<BiPredicate<McpClient, ToolSpecification>>(mcpToolsFilter);
        this.toolWrapper = toolWrapper;
        this.resourcesAsToolsPresenter = resourcesAsToolsPresenter;
        this.toolNameMapper = new AtomicReference<BiFunction<McpClient, ToolSpecification, String>>(toolNameMapper);
        this.toolSpecificationMapper = new AtomicReference<BiFunction<McpClient, ToolSpecification, ToolSpecification>>(toolSpecificationMapper);
        this.alwaysVisibleToolNames = Collections.emptySet();
    }

    public void addMcpClient(McpClient client) {
        Objects.requireNonNull(client);
        this.mcpClients.add(client);
    }

    public void removeMcpClient(McpClient client) {
        this.mcpClients.remove(client);
    }

    public void addFilter(BiPredicate<McpClient, ToolSpecification> filter) {
        Objects.requireNonNull(filter);
        BiPredicate<McpClient, ToolSpecification> currentFilter = this.mcpToolsFilter.get();
        while (!this.mcpToolsFilter.compareAndSet(currentFilter, currentFilter.and(filter))) {
            currentFilter = this.mcpToolsFilter.get();
        }
    }

    public void setFilter(BiPredicate<McpClient, ToolSpecification> filter) {
        Objects.requireNonNull(filter);
        BiPredicate<McpClient, ToolSpecification> currentFilter = this.mcpToolsFilter.get();
        while (!this.mcpToolsFilter.compareAndSet(currentFilter, filter)) {
            currentFilter = this.mcpToolsFilter.get();
        }
    }

    public void setToolNameMapper(BiFunction<McpClient, ToolSpecification, String> toolNameMapper) {
        this.toolNameMapper.set(toolNameMapper);
    }

    public void setToolSpecificationMapper(BiFunction<McpClient, ToolSpecification, ToolSpecification> toolSpecificationMapper) {
        this.toolSpecificationMapper.set(toolSpecificationMapper);
    }

    public void resetFilters() {
        this.setFilter((mcp, tool) -> true);
    }

    public ToolProviderResult provideTools(ToolProviderRequest request) {
        return this.provideTools(request, this.mcpToolsFilter.get());
    }

    protected ToolProviderResult provideTools(ToolProviderRequest request, BiPredicate<McpClient, ToolSpecification> mcpToolsFilter) {
        return this.provideTools(request, mcpToolsFilter, this.mcpClients);
    }

    protected ToolProviderResult provideTools(ToolProviderRequest request, List<McpClient> consideredMcpClients) {
        return this.provideTools(request, this.mcpToolsFilter.get(), consideredMcpClients);
    }

    protected ToolProviderResult provideTools(ToolProviderRequest request, BiPredicate<McpClient, ToolSpecification> mcpToolsFilter, List<McpClient> consideredMcpClients) {
        ToolProviderResult.Builder builder = ToolProviderResult.builder();
        for (McpClient mcpClient : consideredMcpClients) {
            try {
                for (ToolSpecification originalSpec : mcpClient.listTools()) {
                    if (!mcpToolsFilter.test(mcpClient, originalSpec)) continue;
                    BiFunction<McpClient, ToolSpecification, String> nameMapper = this.toolNameMapper.get();
                    BiFunction<McpClient, ToolSpecification, ToolSpecification> specificationMapper = this.toolSpecificationMapper.get();
                    ToolSpecification newSpec = nameMapper != null ? originalSpec.toBuilder().name(nameMapper.apply(mcpClient, originalSpec)).build() : (specificationMapper != null ? specificationMapper.apply(mcpClient, originalSpec) : originalSpec);
                    if (this.alwaysVisibleToolNames.contains(newSpec.name())) {
                        newSpec = McpToolProvider.addSearchBehaviorMetadata(newSpec);
                    }
                    McpToolExecutor defaultToolExecutor = new McpToolExecutor(mcpClient, originalSpec.name());
                    builder.add(newSpec, this.toolWrapper.apply(defaultToolExecutor));
                }
            }
            catch (IllegalConfigurationException e) {
                throw e;
            }
            catch (Exception e) {
                if (this.failIfOneServerFails) {
                    throw new RuntimeException("Failed to retrieve tools from MCP server", e);
                }
                log.warn("Failed to retrieve tools from MCP server", (Throwable)e);
            }
        }
        if (this.resourcesAsToolsPresenter != null) {
            List<McpClient> mcpClientsUnmodifiable = Collections.unmodifiableList(this.mcpClients);
            ToolSpecification listResourcesToolSpec = this.resourcesAsToolsPresenter.createListResourcesSpecification();
            if (this.alwaysVisibleToolNames.contains(listResourcesToolSpec.name())) {
                listResourcesToolSpec = McpToolProvider.addSearchBehaviorMetadata(listResourcesToolSpec);
            }
            builder.add(listResourcesToolSpec, this.toolWrapper.apply(this.resourcesAsToolsPresenter.createListResourcesExecutor(mcpClientsUnmodifiable)));
            ToolSpecification getResourceToolSpec = this.resourcesAsToolsPresenter.createGetResourceSpecification();
            if (this.alwaysVisibleToolNames.contains(getResourceToolSpec.name())) {
                getResourceToolSpec = McpToolProvider.addSearchBehaviorMetadata(getResourceToolSpec);
            }
            builder.add(getResourceToolSpec, this.toolWrapper.apply(this.resourcesAsToolsPresenter.createGetResourceExecutor(mcpClientsUnmodifiable)));
        }
        return builder.build();
    }

    private static ToolSpecification addSearchBehaviorMetadata(ToolSpecification toolSpecification) {
        return toolSpecification.toBuilder().metadata(Utils.merge((Map[])new Map[]{toolSpecification.metadata(), SEARCH_BEHAVIOR_ALWAYS_VISIBLE})).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    private static class ToolsNameFilter
    implements BiPredicate<McpClient, ToolSpecification> {
        private final List<String> toolNames;

        private ToolsNameFilter(String ... toolNames) {
            this(Arrays.asList(toolNames));
        }

        private ToolsNameFilter(List<String> toolNames) {
            this.toolNames = toolNames;
        }

        @Override
        public boolean test(McpClient mcpClient, ToolSpecification tool) {
            return this.toolNames.stream().anyMatch(name -> name.equals(tool.name()));
        }
    }

    public static class Builder {
        private McpResourcesAsToolsPresenter resourcesAsToolsPresenter;
        private List<McpClient> mcpClients;
        private Boolean failIfOneServerFails;
        private BiPredicate<McpClient, ToolSpecification> mcpToolsFilter = (mcp, tool) -> true;
        private Function<ToolExecutor, ToolExecutor> toolWrapper = Function.identity();
        private BiFunction<McpClient, ToolSpecification, String> toolNameMapper;
        private BiFunction<McpClient, ToolSpecification, ToolSpecification> toolSpecificationMapper;
        private Set<String> alwaysVisibleToolNames;

        public Builder mcpClients(List<McpClient> mcpClients) {
            this.mcpClients = mcpClients;
            return this;
        }

        public Builder mcpClients(McpClient ... mcpClients) {
            return this.mcpClients(Arrays.asList(mcpClients));
        }

        public Builder filter(BiPredicate<McpClient, ToolSpecification> mcpToolsFilter) {
            this.mcpToolsFilter = this.mcpToolsFilter.and(mcpToolsFilter);
            return this;
        }

        public Builder filterToolNames(List<String> toolNames) {
            return this.filter(new ToolsNameFilter(toolNames));
        }

        public Builder filterToolNames(String ... toolNames) {
            return this.filter(new ToolsNameFilter(toolNames));
        }

        public Builder failIfOneServerFails(boolean failIfOneServerFails) {
            this.failIfOneServerFails = failIfOneServerFails;
            return this;
        }

        public Builder toolWrapper(Function<ToolExecutor, ToolExecutor> toolWrapper) {
            this.toolWrapper = toolWrapper;
            return this;
        }

        public Builder resourcesAsToolsPresenter(McpResourcesAsToolsPresenter resourcesAsToolsPresenter) {
            this.resourcesAsToolsPresenter = resourcesAsToolsPresenter;
            return this;
        }

        public Builder toolNameMapper(BiFunction<McpClient, ToolSpecification, String> toolNameMapper) {
            if (this.toolSpecificationMapper != null) {
                throw new IllegalArgumentException("It is forbidden to set both a toolNameMapper and a toolSpecificationMapper at the same time.");
            }
            this.toolNameMapper = toolNameMapper;
            return this;
        }

        public Builder toolSpecificationMapper(BiFunction<McpClient, ToolSpecification, ToolSpecification> toolSpecificationMapper) {
            if (this.toolNameMapper != null) {
                throw new IllegalArgumentException("It is forbidden to set both a toolNameMapper and a toolSpecificationMapper at the same time.");
            }
            this.toolSpecificationMapper = toolSpecificationMapper;
            return this;
        }

        public Builder alwaysVisibleToolNames(Set<String> alwaysVisibleToolNames) {
            this.alwaysVisibleToolNames = alwaysVisibleToolNames;
            return this;
        }

        public Builder alwaysVisibleToolNames(String ... alwaysVisibleToolNames) {
            return this.alwaysVisibleToolNames(new HashSet<String>(Arrays.asList(alwaysVisibleToolNames)));
        }

        public McpToolProvider build() {
            return new McpToolProvider(this);
        }
    }
}

