package org.eclipse.smarthome.storage.influxdb;

import org.eclipse.smarthome.core.storage.Storage;
import org.eclipse.smarthome.core.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfluxDBStorageService implements StorageService {

	private final Logger logger = LoggerFactory.getLogger(InfluxDBStorageService.class);
	
    public void activate() {
        logger.debug("Activating InfluxDB storage");
    }

    public void deactivate() {
        logger.debug("Deactivating InfluxDB storage");
    }
	
	@Override
	public <T> Storage<T> getStorage(String name) {
		return new InfluxDBStorage<>();
	}

	@Override
	public <T> Storage<T> getStorage(String name, ClassLoader classLoader) {
		return getStorage(name);
	}

}
