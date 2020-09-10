package com.gentics.mesh.search.index;

import java.util.function.Predicate;

import com.gentics.mesh.search.BucketableElement;

public class BucketPartition {

	private long start;
	private long end;

	public BucketPartition(long start, long end) {
		this.start = start;
		this.end = end;
	}

	/**
	 * Start of partiton.
	 * 
	 * @return
	 */
	public long start() {
		return start;
	}

	/**
	 * End of partition.
	 * 
	 * @return
	 */
	public long end() {
		return end;
	}

	/**
	 * Filter all vertices which are within the partition.
	 * 
	 * @return
	 */
	public Predicate<BucketableElement> filter() {
		return element -> {
			Integer bucketId = element.getBucketId();
			return isWithin(bucketId);
		};
	}

	/**
	 * Test whether the given bucketId is within the bounds of the partition
	 * 
	 * @param bucketId
	 * @return
	 */
	private boolean isWithin(Integer bucketId) {
		return bucketId <= end && bucketId >= start;
	}

	@Override
	public String toString() {
		return "Partition: [" + start + "/" + end + "]";
	}
}
