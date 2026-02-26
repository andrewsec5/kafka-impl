package br.com.andrew.kafka_impl.service;

import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FraudService {

    private final RedisCacheManager cacheManager;

    private static final String FRAUD_CACHE = "fraudCache";
    private static final String LOOP_CACHE = "loopCache";
    private static final int QTD_TENTATIVAS = 3;
    private final Cache fraudCache;
    private final Cache loopCache;

    public FraudService(RedisCacheManager cacheManager) {
        this.cacheManager = cacheManager;
        this.fraudCache = cacheManager.getCache(FRAUD_CACHE);
        this.loopCache = cacheManager.getCache(LOOP_CACHE);
    }

    public boolean isFraud(String key){
        boolean response = true;

        int tentativa = loopCache.get(key, Integer.class) == null ? 0 : loopCache.get(key, Integer.class);

        if(tentativa < QTD_TENTATIVAS && fraudCache.get(key) == null){
            response = false;
        }

        tentativa = tentativa + 1;

        loopCache.put(key, Integer.valueOf(tentativa));
        fraudCache.put(key, null);

        return response;
    }

    public void produceMsg(String newKey, LocalDateTime now){

    }

}
