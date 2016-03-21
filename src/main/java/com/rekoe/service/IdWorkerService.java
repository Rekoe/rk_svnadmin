/** Copyright 2010-2012 Twitter, Inc.*/
package com.rekoe.service;

import org.nutz.ioc.loader.annotation.IocBean;

/**
 * An object that generates IDs. This is broken into a separate class in case we
 * ever want to support multiple worker threads per process
 * 
 * @author yumin
 */
@IocBean(name = "idFactory")
public class IdWorkerService {

	private final long twepoch = 1288834974657L;
	private final long workerIdBits = 10L;
	private final long datacenterIdBits = 5L;
	private final long maxWorkerId = -1L ^ (-1L << workerIdBits);
	private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
	private final long sequenceBits = 12L;
	private final long workerIdShift = sequenceBits;
	private final long datacenterIdShift = sequenceBits + workerIdBits;
	private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
	private final long sequenceMask = -1L ^ (-1L << sequenceBits);

	private long lastTimestamp = -1L;
	private long workerId = 0;
	private long datacenterId = 0;
	private long sequence = 0L;

	public IdWorkerService() {
		long workerId = 1L;
		long datacenterId = 5;
		if (workerId > maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(String.format("workerId can't be greater than %d or less than 0.", maxWorkerId));
		}
		if (datacenterId > maxDatacenterId || datacenterId < 0) {
			throw new IllegalArgumentException(String.format("datacenterId can't be greater than %d or less than 0.", maxDatacenterId));
		}
		this.workerId = workerId;
		this.datacenterId = datacenterId;
	}

	public IdWorkerService(long workerId, long datacenterId) {
		if (workerId > maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(String.format("workerId can't be greater than %d or less than 0.", maxWorkerId));
		}
		if (datacenterId > maxDatacenterId || datacenterId < 0) {
			throw new IllegalArgumentException(String.format("datacenterId can't be greater than %d or less than 0.", maxDatacenterId));
		}
		this.workerId = workerId;
		this.datacenterId = datacenterId;
	}

	public long getId() {
		long id = nextId();
		return id;
	}

	public long getWorkerId() {
		return workerId;
	}

	public long getDatacenterId() {
		return datacenterId;
	}

	public long getTimestamp() {
		return System.currentTimeMillis();
	}

	public synchronized long nextId() {
		long id = 0L;
		long timestamp = timeGen();
		if (timestamp < lastTimestamp) {
			throw new RuntimeException(String.format("clock moved backwards. refusing to generate id for %d milliseconds.", lastTimestamp - timestamp));
		}
		if (timestamp == lastTimestamp) {
			sequence = (1 + sequence) & sequenceMask;
			if (0 == sequence) {
				timestamp = tilNextMillis(lastTimestamp);
			}
		} else {
			sequence = 0;
		}
		lastTimestamp = timestamp;
		id = ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift) | (workerId << workerIdShift) | sequence;
		return id;
	}

	protected long tilNextMillis(long lastTimestamp) {
		long timestamp = timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = timeGen();
		}
		return timestamp;
	}

	protected long timeGen() {
		return System.currentTimeMillis();
	}

	private static final long workerMask = 4190208;
	private static final long datacenterMask = 130023424;

	public static void main(String args[]) {
		long workerId = 1023L;
		long datacenterId = 5;
		IdWorkerService worker = new IdWorkerService(workerId, datacenterId);
		long id = worker.nextId();
		System.out.println((id & workerMask) >> 12);
		System.out.println((id & datacenterMask) >> 22);
	}
}
