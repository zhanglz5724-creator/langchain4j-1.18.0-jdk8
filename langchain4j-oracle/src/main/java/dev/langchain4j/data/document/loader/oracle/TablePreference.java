/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnore
 *  com.fasterxml.jackson.annotation.JsonProperty
 */
package dev.langchain4j.data.document.loader.oracle;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TablePreference {
    private String owner;
    private String tablename;
    private String colname;

    @JsonIgnore
    public boolean isValid() {
        return this.owner != null && this.tablename != null && this.colname != null;
    }

    @JsonProperty(value="owner")
    public void setOwner(String owner) {
        this.owner = owner;
    }

    @JsonProperty(value="tablename")
    public void setTableName(String tablename) {
        this.tablename = tablename;
    }

    @JsonProperty(value="colname")
    public void setColumnName(String colname) {
        this.colname = colname;
    }

    @JsonProperty(value="owner")
    public String getOwner() {
        return this.owner;
    }

    @JsonProperty(value="tablename")
    public String getTableName() {
        return this.tablename;
    }

    @JsonProperty(value="colname")
    public String getColumnName() {
        return this.colname;
    }
}

