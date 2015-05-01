package com.novoda.sqliteprovider.demo.domain;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class Groups implements Iterable<Groups.Group> {

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
        private final double total;
        private final int shopId;

        public Group(double total, int shopId) {
            this.total = total;
            this.shopId = shopId;
        }

        public int getShopId() {
            return shopId;
        }

        public String getFormattedTotal() {
            return String.format(Locale.UK, "£%.2f,", total);
        }

        public static Group getNullSafeGroup() {
            return new Group(0, 0);
        }

    }
}
