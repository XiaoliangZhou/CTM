package CGA.naming.impl;



import CGA.naming.DescribedEntity;

import java.util.*;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class DescribedEntitySet<Entity extends org.uma.jmetal.util.naming.DescribedEntity> implements Set<Entity> {
    private final Map<String, Entity> map = new HashMap();

    public DescribedEntitySet() {
    }

    public boolean add(Entity e) {
        Entity stored = this.map.get(e.getName());
        if (stored == null) {
            this.map.put(e.getName(), e);
            return true;
        } else if (stored.equals(e)) {
            return false;
        } else {
            throw new IllegalArgumentException("Cannot add " + e + ", conflicting name with " + stored);
        }
    }

    public boolean addAll(Collection<? extends Entity> c) {
        boolean isModified = false;

        Entity entity;
        for(Iterator var3 = c.iterator(); var3.hasNext(); isModified |= this.add(entity)) {
            entity = (Entity)var3.next();
        }

        return isModified;
    }

    public <E extends Entity> E get(String name) {
        return (E) this.map.get(name);
    }

    public boolean remove(Object o) {
        return this.map.values().remove(o);
    }

    public boolean remove(String name) {
        return this.map.keySet().remove(name);
    }

    public boolean removeAll(Collection<?> c) {
        return this.map.values().removeAll(c);
    }

    public boolean contains(Object o) {
        return this.map.values().contains(o);
    }

    public boolean contains(String name) {
        return this.map.keySet().contains(name);
    }

    public boolean containsAll(Collection<?> c) {
        return this.map.values().containsAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return this.map.values().retainAll(c);
    }

    public int size() {
        return this.map.size();
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    public void clear() {
        this.map.clear();
    }

    public Iterator<Entity> iterator() {
        return this.map.values().iterator();
    }

    public Object[] toArray() {
        return this.map.values().toArray();
    }

    public <T> T[] toArray(T[] a) {
        return this.map.values().toArray(a);
    }

    public String toString() {
        TreeSet<String> displaySet = new TreeSet(new Comparator<String>() {
            public int compare(String s1, String s2) {
                int comparison = s1.compareToIgnoreCase(s2);
                return comparison == 0 ? s1.compareTo(s2) : comparison;
            }
        });
        displaySet.addAll(this.map.keySet());
        return displaySet.toString();
    }
}
