/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.mariadb.jdbc.Driver
 */
package dev.langchain4j.store.embedding.mariadb;

import java.sql.SQLException;
import org.mariadb.jdbc.Driver;

class MariaDbValidator {
    MariaDbValidator() {
    }

    public static String validateAndEnquoteIdentifier(String identifier, boolean alwaysQuote) {
        try {
            return Driver.enquoteIdentifier((String)identifier, (boolean)alwaysQuote);
        }
        catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}

