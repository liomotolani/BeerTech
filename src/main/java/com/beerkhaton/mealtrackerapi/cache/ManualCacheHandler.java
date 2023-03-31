package com.beerkhaton.mealtrackerapi.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class ManualCacheHandler {

    // Redis will be the best for this

    private final Map<String, String> resetCache = new HashMap<>();

    public void addToCache(String key, String value) { resetCache.put(key,value);}

    public void removeCache(String key) {resetCache.remove(key);}

    public String getFromCache(String key){ return resetCache.get(key);}
    
}
