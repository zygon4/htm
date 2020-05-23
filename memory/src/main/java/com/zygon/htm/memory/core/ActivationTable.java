package com.zygon.htm.memory.core;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Sets;
import com.zygon.htm.core.Identifier;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 */
public class ActivationTable {

    private final LoadingCache<Identifier, Identifier> cache
            = createCache(1, TimeUnit.SECONDS);

    public final void add(ActivationMessage msg) throws ExecutionException {
        this.cache.put(msg.getDestination(), msg.getDestination());
    }

    public final long getCount() {
        return this.cache.size();
    }

    public final Set<Identifier> getAllIdentifiers() {

        Set<Identifier> sources = Sets.newHashSet();
        // light copy - hopefully this won't cause issues
        sources.addAll(this.cache.asMap().values());

        return sources;
    }

    public final boolean contains(Identifier id) {
        return this.cache.getIfPresent(id) != null;
    }

    public final boolean isEmpty() {
        return this.cache.size() == 0;
    }

    // Expire after write
    private static <T> LoadingCache<T, T> createCache(long timeout, TimeUnit units) {
        return CacheBuilder.newBuilder()
                .expireAfterWrite(timeout, units)
                .build(new CacheLoader<T, T>() {
                    @Override
                    public T load(T key) throws Exception {
                        return key;
                    }
                });
    }
}
