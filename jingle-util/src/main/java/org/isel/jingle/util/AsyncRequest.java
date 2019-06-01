package org.isel.jingle.util;

import java.util.concurrent.CompletableFuture;

public interface AsyncRequest extends AutoCloseable{
    CompletableFuture<String> getLines(String path);
}
