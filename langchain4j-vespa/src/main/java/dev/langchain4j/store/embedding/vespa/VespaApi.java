/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  retrofit2.Call
 *  retrofit2.http.DELETE
 *  retrofit2.http.GET
 *  retrofit2.http.Path
 *  retrofit2.http.Query
 */
package dev.langchain4j.store.embedding.vespa;

import dev.langchain4j.store.embedding.vespa.DeleteResponse;
import dev.langchain4j.store.embedding.vespa.QueryResponse;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

interface VespaApi {
    @GET(value="search/{query}")
    public Call<QueryResponse> search(@Path(value="query", encoded=true) String var1);

    @DELETE(value="document/v1/{ns}/{docType}/docid?selection=true")
    public Call<DeleteResponse> deleteAll(@Path(value="ns") String var1, @Path(value="docType") String var2, @Query(value="cluster") String var3);
}

