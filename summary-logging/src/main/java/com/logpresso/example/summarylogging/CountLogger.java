package com.logpresso.example.summarylogging;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.araqne.cron.PeriodicJob;
import org.araqne.logstorage.Log;
import org.araqne.logstorage.LogCallback;
import org.araqne.logstorage.LogStorage;

@PeriodicJob("*/5 * * * *")
@Component(name = "count-logger-example")
@Provides(specifications = { Runnable.class })
public class CountLogger implements Runnable, LogCallback {

	private static final String MYLOGSTAT = "mylogstat";

	private ConcurrentMap<Date, AtomicLong> dayCounts = new ConcurrentHashMap<Date, AtomicLong>();

	@Requires
	private LogStorage storage;

	@Validate
	public void start() {
		storage.ensureTable(MYLOGSTAT, "v3p");
		storage.addLogListener(this);
	}

	@Invalidate
	public void stop() {
		if (storage != null)
			storage.removeLogListener(this);
	}

	// flush stat log for every 5min
	@Override
	public void run() {
		for (Date d : dayCounts.keySet()) {
			AtomicLong count = dayCounts.get(d);
			long value = count.getAndSet(0);
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("count", value);
			storage.write(new Log(MYLOGSTAT, d, data));
		}
	}

	@Override
	public void onLog(Log log) {
		AtomicLong value = new AtomicLong(1);
		AtomicLong old = dayCounts.putIfAbsent(log.getDay(), value);
		if (old != null)
			old.incrementAndGet();
	}
}
