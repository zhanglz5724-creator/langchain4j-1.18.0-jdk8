/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.model.language.LanguageModel
 *  dev.langchain4j.model.output.Response
 */
package dev.langchain4j.model.oracle;

import dev.langchain4j.model.language.LanguageModel;
import dev.langchain4j.model.output.Response;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OracleSummaryLanguageModel
implements LanguageModel {
    private final Connection conn;
    private final String pref;
    private final String proxy;

    public OracleSummaryLanguageModel(Connection conn, String pref) {
        this.conn = conn;
        this.pref = pref;
        this.proxy = "";
    }

    public OracleSummaryLanguageModel(Connection conn, String pref, String proxy) {
        this.conn = conn;
        this.pref = pref;
        this.proxy = proxy;
    }

    public Response<String> generate(String input) {
        String text = "";
        try {
            PreparedStatement stmt;
            String query;
            if (this.proxy != null && !this.proxy.isEmpty()) {
                query = "begin utl_http.set_proxy(?); end;";
                stmt = this.conn.prepareStatement(query);
                try {
                    stmt.setObject(1, this.proxy);
                    stmt.execute();
                }
                finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }
            }
            query = "select dbms_vector_chain.utl_to_summary(?, json(?)) data from dual";
            stmt = this.conn.prepareStatement(query);
            try {
                Clob clob = this.conn.createClob();
                clob.setString(1L, input);
                stmt.setObject(1, clob);
                stmt.setObject(2, this.pref);
                try (ResultSet rs = stmt.executeQuery();){
                    while (rs.next()) {
                        text = rs.getString("data");
                    }
                }
            }
            finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
        }
        catch (SQLException ex) {
            throw new RuntimeException("cannot get summary", ex);
        }
        return Response.from(text);
    }
}

