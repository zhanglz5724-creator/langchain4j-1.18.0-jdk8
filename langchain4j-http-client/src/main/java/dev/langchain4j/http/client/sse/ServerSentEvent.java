/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.langchain4j.internal.Utils
 */
package dev.langchain4j.http.client.sse;

import dev.langchain4j.internal.Utils;
import java.util.Objects;

public class ServerSentEvent {
    private final String event;
    private final String data;

    public ServerSentEvent(String event, String data) {
        this.event = event;
        this.data = data;
    }

    public String event() {
        return this.event;
    }

    public String data() {
        return this.data;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        ServerSentEvent that = (ServerSentEvent)obj;
        return Objects.equals(this.event, that.event) && Objects.equals(this.data, that.data);
    }

    public int hashCode() {
        return Objects.hash(this.event, this.data);
    }

    public String toString() {
        return "ServerSentEvent { event = " + Utils.quoted((Object)this.event) + ", data = " + Utils.quoted((Object)this.data) + " }";
    }
}

