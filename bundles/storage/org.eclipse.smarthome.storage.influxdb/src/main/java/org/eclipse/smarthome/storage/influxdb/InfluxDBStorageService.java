package org.eclipse.smarthome.storage.influxdb;

import org.eclipse.smarthome.core.storage.Storage;
import org.eclipse.smarthome.core.storage.StorageService;

public class InfluxDBStorageService implements StorageService {

	@Override
	public <T> Storage<T> getStorage(String name) {
		return new InfluxDBStorage<>();
	}

	@Override
	public <T> Storage<T> getStorage(String name, ClassLoader classLoader) {
		return getStorage(name);
	}

}
