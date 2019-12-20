package cache;

public interface InMemoryCache<T> {
		 
	// 2 hours in Cache
		static final long CACHE_TTL = 6 * 60 * 60 * 1000;
	    T get(String key);
	 
	   
	

}
