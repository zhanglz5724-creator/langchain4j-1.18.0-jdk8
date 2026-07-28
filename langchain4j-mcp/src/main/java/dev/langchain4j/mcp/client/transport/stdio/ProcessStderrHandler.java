/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.langchain4j.mcp.client.transport.stdio;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ProcessStderrHandler
implements Runnable,
Closeable {
    private final Process process;
    private static final Logger log = LoggerFactory.getLogger(ProcessStderrHandler.class);
    private volatile boolean closed = false;

    public ProcessStderrHandler(Process process) {
        this.process = process;
    }

    @Override
    public void run() {
        try (InputStreamReader inputStreamReader = new InputStreamReader(this.process.getErrorStream());){
            block36: {
                try (BufferedReader reader = new BufferedReader(inputStreamReader);){
                    String line;
                    while ((line = reader.readLine()) != null) {
                        log.debug("[STDERR] {}", (Object)line);
                    }
                }
                catch (IOException e) {
                    if (this.closed) break block36;
                    throw new RuntimeException(e);
                }
            }
            log.debug("ProcessErrorPrinter has finished reading error output from subprocess");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                this.process.getErrorStream().close();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void close() throws IOException {
        this.closed = true;
    }
}

