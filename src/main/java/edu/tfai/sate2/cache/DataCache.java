package edu.tfai.sate2.cache;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.io.Files;
import edu.tfai.sate2.parsers.XMLParser;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import static com.google.common.collect.Lists.newArrayList;
import static org.joda.time.LocalDateTime.now;

@Slf4j
public class DataCache<T> {

    private final Cache<String, T> cache;

    private final Optional<String> fileToStore;

    private final int timeToLiveHours;

    @Data
    class StoreRecord<T> {
        private final LocalDateTime timeStamp;
        private final String key;
        private final T record;
    }

    public DataCache(long timeToLive, TimeUnit timeUnit) {
        this(timeToLive, timeUnit, Optional.<String>absent());
    }

    public DataCache(long timeToLive, TimeUnit timeUnit, Optional<String> fileToStore) {
        timeToLiveHours = (int) timeUnit.toHours(timeToLive);
        cache = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(timeToLiveHours, TimeUnit.HOURS)
                .build();

        this.fileToStore = fileToStore;
        //issaugotas i diska praras cache prasme ir niekada jo neismes.

        loadCache(fileToStore);
    }

    private void loadCache(Optional<String> fileToStore) {
        if (!fileToStore.isPresent()) {
            return;
        }
        XMLParser xstream = new XMLParser();
        try {
            FileInputStream in = new FileInputStream(fileToStore.get());
            List<StoreRecord> obj = (List<StoreRecord>) xstream.fromXML(in);
            for (StoreRecord<T> record : obj) {
                LocalDateTime expireTime = record.getTimeStamp().plusSeconds(timeToLiveHours);
                if (now().isBefore(expireTime))
                    store(record.getKey(), record.getRecord());
            }
            in.close();
        } catch (Exception ex) {
            log.debug("loading cache from file:" + this.fileToStore, ex);
        }
    }


    private void storeCache(Optional<String> fileToStore) {
        if (!fileToStore.isPresent()) {
            return;
        }
        XMLParser xstream = new XMLParser();
        List<StoreRecord> storeList = newArrayList();
        try {
            FileOutputStream fos = new FileOutputStream(fileToStore.get());
            ConcurrentMap<String, T> map = cache.asMap();
            for (String key : map.keySet()) {
                storeList.add(new StoreRecord(now(), key, map.get(key)));
            }
            xstream.toXML(storeList, fos);
            fos.flush();
            fos.close();
        } catch (Exception ex) {
            log.error("Storing cache to file:" + this.fileToStore, ex);
        }
    }


    public void store(String messageId, T entry) {
        log.debug("Storing to cache:" + messageId + " Object:" + entry);
        cache.put(messageId, entry);
        storeCache(fileToStore);
    }

    public T retrieve(String messageId) {
        T ifPresent = cache.getIfPresent(messageId);
        log.debug("Retrieved from cache:" + messageId + " Object:" + ifPresent);
        return ifPresent;
    }

    public void clearCache() {
        cache.cleanUp();
        log.debug("Cache cleaned up");
    }
}
