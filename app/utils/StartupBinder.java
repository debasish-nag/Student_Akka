package utils;

import javax.inject.Singleton;

import com.google.inject.AbstractModule;

import store.StudentRepositary;

@Singleton
public class StartupBinder  extends AbstractModule {
	
	@Override
	protected void configure() {
		
		bind(CouchbaseConfig.class).asEagerSingleton();
	}
	

}
