package cache;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import model.Property;

public class PropertyCache implements InMemoryCache<Optional<Property>>{

	
	private volatile Map<String, Property> cache = new ConcurrentHashMap<String, Property>();

	private PropertyCache() {

		populate();

		Thread cleanerThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(CACHE_TTL);
                    cache = new ConcurrentHashMap<String, Property>();
                    populate();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        cleanerThread.setDaemon(true);
        cleanerThread.start();
		
		
	}

	private void populate() {

	}

	public Optional<Property> get(String key) {

		return Optional.ofNullable(cache.get(key));
	}

	
	
}
