package com.novoda.sqliteprovider.demo.domain;

import java.util.Iterator;
import java.util.List;

public class Groups implements Iterable<Groups.Group>{

	private final List<Group> groups;
	
	public Groups(List<Group> groups) {
		this.groups = groups;
	}

	public Group get(int position) {
		return groups.get(position);
	}
	
	@Override
	public Iterator<Group> iterator() {
		return groups.iterator();
	}
	
	public static class Group {
		private final int count;
		private final int shopId;
		
		public Group(int count, int shopId) {
			this.shopId = shopId;
			this.count = count;
		}
		
		public int getShopId() {
			return shopId;
		}

		public int getCount() {
			return count;
		}

		public static Group getNullSafeGroup() {
			return new Group(0, 0);
		}
	}
}