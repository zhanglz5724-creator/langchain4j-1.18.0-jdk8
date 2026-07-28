/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.core.JsonProcessingException
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  dev.langchain4j.data.embedding.Embedding
 *  dev.langchain4j.data.segment.TextSegment
 *  dev.langchain4j.model.embedding.DimensionAwareEmbeddingModel
 *  dev.langchain4j.model.output.Response
 *  oracle.jdbc.OracleConnection
 */
package dev.langchain4j.model.oracle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.data.document.splitter.oracle.Chunk;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.DimensionAwareEmbeddingModel;
import dev.langchain4j.model.oracle.Embedding;
import dev.langchain4j.model.output.Response;
import java.sql.Array;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import oracle.jdbc.OracleConnection;

public class OracleEmbeddingModel
extends DimensionAwareEmbeddingModel {
    private final Connection conn;
    private final String pref;
    private final String proxy;
    private boolean batching = true;

    public OracleEmbeddingModel(Connection conn, String pref) {
        this.conn = conn;
        this.pref = pref;
        this.proxy = "";
    }

    public OracleEmbeddingModel(Connection conn, String pref, String proxy) {
        this.conn = conn;
        this.pref = pref;
        this.proxy = proxy;
    }

    public void setBatching(boolean batching) {
        this.batching = batching;
    }

    public boolean getBatching() {
        return this.batching;
    }

    public static boolean loadOnnxModel(Connection conn, String dir, String onnxFile, String modelName) throws SQLException {
        boolean result = false;
        String query = "begin\n  dbms_data_mining.drop_model(?, force => true);\n  dbms_vector.load_onnx_model(?, ?, ?,\n  json('{\"function\" : \"embedding\", \"embeddingOutput\" : \"embedding\" , \"input\": {\"input\": [\"DATA\"]}}'));\nend;";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setObject(1, modelName);
        stmt.setObject(2, dir);
        stmt.setObject(3, onnxFile);
        stmt.setObject(4, modelName);
        stmt.execute();
        result = true;
        return result;
    }

    public Response<List<dev.langchain4j.data.embedding.Embedding>> embedAll(List<TextSegment> textSegments) {
        List<String> texts = textSegments.stream().map(TextSegment::text).collect(Collectors.toList());
        try {
            return this.embedTexts(texts);
        }
        catch (JsonProcessingException | SQLException ex) {
            throw new RuntimeException("cannot get embedding", ex);
        }
    }

    private Response<List<dev.langchain4j.data.embedding.Embedding>> embedTexts(List<String> inputs) throws SQLException, JsonProcessingException {
        ArrayList<dev.langchain4j.data.embedding.Embedding> embeddings = new ArrayList<dev.langchain4j.data.embedding.Embedding>();
        if (this.proxy != null && !this.proxy.isEmpty()) {
            String query = "begin utl_http.set_proxy(?); end;";
            try (PreparedStatement stmt = this.conn.prepareStatement(query);){
                stmt.setObject(1, this.proxy);
                stmt.execute();
            }
        }
        if (!this.batching) {
            for (String input : inputs) {
                this.embed(input, this.pref, embeddings);
            }
        } else {
            List<Object> elements = this.toClobList(this.conn, inputs);
            Array arr = ((OracleConnection)this.conn).createOracleArray("SYS.VECTOR_ARRAY_T", (Object)elements.toArray());
            this.embed(arr, this.pref, embeddings);
        }
        return Response.from(embeddings);
    }

    private void embed(Object obj, String pref, List<dev.langchain4j.data.embedding.Embedding> embeddings) throws SQLException, JsonProcessingException {
        String query = "select t.column_value as data from dbms_vector_chain.utl_to_embeddings(?, json(?)) t";
        try (PreparedStatement stmt = this.conn.prepareStatement(query);){
            stmt.setObject(1, obj);
            stmt.setObject(2, pref);
            try (ResultSet rs = stmt.executeQuery();){
                while (rs.next()) {
                    String text = rs.getString("data");
                    ObjectMapper mapper = new ObjectMapper();
                    Embedding dbmsEmbedding = (Embedding)mapper.readValue(text, Embedding.class);
                    dev.langchain4j.data.embedding.Embedding embedding = new dev.langchain4j.data.embedding.Embedding(this.toFloatArray(dbmsEmbedding.getVector()));
                    embeddings.add(embedding);
                }
            }
        }
    }

    private List<Object> toClobList(Connection conn, List<String> inputs) throws JsonProcessingException, SQLException {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<Object> chunks = new ArrayList<Object>();
        for (int i = 0; i < inputs.size(); ++i) {
            Chunk chunk = new Chunk();
            chunk.setId(i);
            chunk.setData(inputs.get(i));
            String jsonString = objectMapper.writeValueAsString((Object)chunk);
            Clob clob = conn.createClob();
            clob.setString(1L, jsonString);
            chunks.add(clob);
        }
        return chunks;
    }

    private float[] toFloatArray(String embedding) {
        String str = embedding.replace("[", "").replace("]", "");
        String[] strArr = str.split(",");
        float[] floatArr = new float[strArr.length];
        for (int i = 0; i < strArr.length; ++i) {
            floatArr[i] = Float.parseFloat(strArr[i]);
        }
        return floatArr;
    }
}

