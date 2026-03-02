package br.com.andrew.kafka_impl.service;

import br.com.andrew.kafka_impl.dto.TransactionDTO;
import br.com.andrew.kafka_impl.producer.KafkaProducer;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;

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
        String key = request.id();
        boolean response = true;

        int tentativa = loopCache.get(key, Integer.class) == null ? 0 : loopCache.get(key, Integer.class);

        if(tentativa < QTD_TENTATIVAS && fraudCache.get(key) == null){
            response = false;
        }

        if(tentativa < QTD_TENTATIVAS){
            response = fraudePorComercio(request);
        }

        tentativa = tentativa + 1;

        loopCache.put(key, tentativa);
        fraudCache.put(key, request.comerciante());

        return response;
    }

    public void produceMsg(TransactionDTO request){

            kafkaProducer.fraudDetected(toFraude(request));

    }

    public boolean fraudePorComercio(TransactionDTO request){
        String key = request.id();
        String comercio = request.comerciante();

        String obj = fraudCache.get(key, String.class);


        if(obj != null ){
            return comercio.equals(obj);
        }

        return false;

    }

}
