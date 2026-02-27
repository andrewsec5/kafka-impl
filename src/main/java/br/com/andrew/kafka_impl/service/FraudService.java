package br.com.andrew.kafka_impl.service;

import br.com.andrew.kafka_impl.dto.TransactionDTO;
import br.com.andrew.kafka_impl.producer.KafkaProducer;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

import static br.com.andrew.kafka_impl.dto.TransactionDTO.toFraude;

@Service
public class FraudService {

    private final RedisCacheManager cacheManager;
    private final KafkaProducer kafkaProducer;

    private static final String FRAUD_CACHE = "fraudCache";
    private static final String LOOP_CACHE = "loopCache";
    private static final int QTD_TENTATIVAS = 10;
    private final Cache fraudCache;
    private final Cache loopCache;

    public FraudService(RedisCacheManager cacheManager, KafkaProducer kafkaProducer) {
        this.cacheManager = cacheManager;
        this.fraudCache = cacheManager.getCache(FRAUD_CACHE);
        this.loopCache = cacheManager.getCache(LOOP_CACHE);
        this.kafkaProducer = kafkaProducer;
    }

    public boolean isFraud(TransactionDTO request){

        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime msgDate = request.dataHora();
        if(msgDate.isBefore(now.minusSeconds(30))){
            return false;
        }

        String key = request.cartaoId();
        boolean response = true;

        int tentativa = loopCache.get(key, Integer.class) == null ? 0 : loopCache.get(key, Integer.class);

        if(tentativa < QTD_TENTATIVAS && fraudCache.get(key) == null){
            response = false;
        }

        tentativa = tentativa + 1;

        loopCache.put(key, tentativa);
        fraudCache.put(key, null);

        return response;
    }

    public void produceMsg(TransactionDTO request){

            kafkaProducer.fraudDetected(toFraude(request));

    }

}
