package org.eclipse.smarthome.storage.influxdb;

import static org.eclipse.smarthome.storage.influxdb.InfluxDBStorageConstants.DATABASE;
import static org.eclipse.smarthome.storage.influxdb.InfluxDBStorageConstants.PASSWORD;
import static org.eclipse.smarthome.storage.influxdb.InfluxDBStorageConstants.URL;
import static org.eclipse.smarthome.storage.influxdb.InfluxDBStorageConstants.USER_NAME;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.eclipse.smarthome.core.storage.Storage;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.dto.QueryResult.Result;
import org.influxdb.dto.QueryResult.Series;

public class InfluxDBStorage<T> implements Storage<T> {

	private final InfluxDB influxDB;
	
	public InfluxDBStorage() {
		influxDB = InfluxDBFactory.connect(URL, USER_NAME, PASSWORD);
	}
	
	@Override
	public T put(String key, T value) {
		Point point = Point.measurement(key)
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .field("value", value)
                .build();
		influxDB.write(DATABASE, "default", point);
		return value;
	}

	@Override
	public T remove(String key) {
		Query query = new Query("DROP FROM " + key, DATABASE);
		influxDB.query(query);
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get(String key) {
		Query query = new Query("SELECT value FROM " + key + " LIMIT 1", DATABASE);
		QueryResult queryResult = influxDB.query(query);
		List<Result> results = queryResult.getResults();
		for(Result result : results) {
			Series series = result.getSeries().get(0);
			return (T) series.getValues().get(0);
		}
		return null;
	}

	@Override
	public Collection<String> getKeys() {
		Query query = new Query("SHOW MEASUREMENTS", DATABASE);
		Set<String> measurements = new HashSet<String>();
		QueryResult queryResult = influxDB.query(query);
		List<Result> results = queryResult.getResults();
		for(Result result : results) {
			for(Series series : result.getSeries()) {
				measurements.add(series.getName());
			}
		}
		return measurements;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<T> getValues() {
		Query query = new Query("SELECT value FROM /.*/ LIMIT 1", DATABASE);
		Set<T> measurements = new HashSet<T>();
		QueryResult queryResult = influxDB.query(query);
		List<Result> results = queryResult.getResults();
		for(Result result : results) {
			Series series = result.getSeries().get(0);
			measurements.add((T)series.getValues().get(0)); 
		}
		return measurements;
	}

}
