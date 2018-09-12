package treemapsimple.structs;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
/**
 *
 * @author detectivejd
 * @param <K>
 * @param <V>
 */
public class MyTreeMap<K,V> implements NavigableMap<K, V>
{
    private final Comparator<? super K> comparator;
    private static boolean RED   = true;
    private static boolean BLACK = false;
    private Entry<K,V> root;
    private int size;
    public MyTreeMap() {
        clear();
        this.comparator = null;
    }
    public MyTreeMap(Comparator<? super K> xcomparator) {
        clear();
        this.comparator = xcomparator;
    }
    public MyTreeMap(java.util.Map<? extends K, ? extends V> m) {
        clear();
        this.comparator = null;
        putAll(m);
    }
    @Override
    public int size() {
        return size;
    }
    @Override
    public boolean isEmpty() {
        return size == 0;        
    }
    @Override
    public final void clear() {        
        size = 0;
        root = null;
    }
    /*------------------------------------------------------------*/
    @Override
    public final void putAll(Map<? extends K, ? extends V> m) {
        m.entrySet().forEach((e) -> {
            put(e.getKey(), e.getValue());
        });
    }
    @Override
    public V put(K key, V value) {
        if(key != null){
            Entry<K,V> t = root;
            if(t == null){
                root = new Entry(key,value,null);
                size = 1;
            } else {
                Entry<K,V> parent = null;                    
                int cmp = 0;
                while (t != null) {
                    parent = t;
                    cmp = checkCompare(t,key);
                    if(cmp == 0){
                        return t.value = value;
                    } else {
                        t = (cmp < 0) ? leftOf(t) : rightOf(t);
                    }
                }
                Entry<K,V> e = new Entry<>(key, value, parent);
                if (cmp < 0) {
                    parent.left = e;
                } else {
                    parent.right = e;
                }
                setColor(e, RED);
                fixUp(e);
                size++;
            }                            
        }
        return null;
    }
    private int checkCompare(Entry<K,V>x, K key){
        if(comparator != null){
            return comparator.compare(key,x.key);
        } else {
            Comparable<? super K> k = (Comparable<? super K>) key;
            return k.compareTo(x.key);
        }
    }
    private void fixUp(Entry<K,V> x) {       
        while (x != null && x != root && colorOf(parentOf(x)) == RED) {
            if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {                
                Entry<K,V> y = rightOf(parentOf(parentOf(x)));
                if (y != null && y.color == RED) {
                    setColor(parentOf(x), RED);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {                    
                    if (x == rightOf(parentOf(x))) {                        
                        x = parentOf(x);
                        rotateLeft(x);
                    } else {
                        setColor(parentOf(x), BLACK);
                        setColor(parentOf(parentOf(x)), RED);
                        rotateRight(x.parent.parent);
                    }
                }
            } else {                
                Entry<K,V> y = leftOf(parentOf(parentOf(x))); 
                if (y != null && colorOf(y) == RED) {
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == leftOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateRight(x);
                    } else {
                        setColor(parentOf(x), BLACK);
                        setColor(parentOf(parentOf(x)), RED);
                        rotateLeft(x.parent.parent);
                    }
                }
            }
        }
        setColor(root, BLACK);
    }
    /*------------------------------------------------------------*/
    @Override
    public boolean containsKey(Object key) {
        return getEntry(key) != null;
    }    
    @Override
    public V get(Object key) {
        Entry<K,V> p = getEntry(key);
        return (p == null ? null : p.value);
    }
    private Entry<K,V>getEntry(Object key){
        if(key != null){
            Entry<K,V> p = root;
            while (p != null) {
                int cmp = checkCompare(p, (K) key);
                if(cmp < 0){
                    p = leftOf(p);
                } else if(cmp > 0) {
                    p = rightOf(p);
                } else {
                    return p;
                } 
            }
        }   
        return null;        
    }
    /*------------------------------------------------------------*/
    @Override
    public boolean containsValue(Object value) {
        for (Entry<K,V> e = getFirstEntry(); e != null; e = successor(e)){
            if (value != null && value.equals(e.getValue())){
                return true;
            }
        }
        return false;
    }
    /*------------------------------------------------------------*/
    @Override
    public V remove(Object key) {
        Entry<K,V> p = getEntry(key);
        if (p == null) {
            return null;
        } else {
            V oldValue = p.value;
            deleteEntry(p);              
            return oldValue;
        }
    }
    private void deleteEntry(Entry<K,V> p) {
        size--;
        if(leftOf(p) != null && rightOf(p) != null){
            Entry<K,V> s = successor(p);
            p.key = s.key;
            p.value = s.value;
            p = s;
        } 
        Entry<K,V> rep = (leftOf(p) != null ? leftOf(p) : rightOf(p));
        if (rep != null) {
            rep.parent = parentOf(p);
            if (parentOf(p) == null) {
                root = rep;
            } else if (p == leftOf(parentOf(p))) {
                p.parent.left  = rep;
            } else {
                p.parent.right = rep; 
            }
            p.left = p.right = p.parent = null;
            if (colorOf(p) == BLACK) {
                fixDown(rep); 
            }
        } else if (parentOf(p) == null) { // return if we are the only node.
            root = null;
        } else { //  No children. Use self as phantom replacement and unlink.
            if (colorOf(p) == BLACK) {
                fixDown(p); 
            }
            if (parentOf(p) != null) {
                if (p == leftOf(parentOf(p))) {
                    p.parent.left = null;
                } else if (p == rightOf(parentOf(p))) {
                    p.parent.right = null; 
                }
                p.parent = null;
            }
        }        
    }
    private void fixDown(Entry<K,V>x){
        while(x != root && colorOf(x) == BLACK) {
            if(x == parentOf(leftOf(x))) {                
                Entry<K,V> sib = parentOf(rightOf(x));
                if(colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rotateLeft(x.parent);
                    sib = parentOf(rightOf(x));
                }
                if(colorOf(leftOf(sib)) == BLACK && colorOf(rightOf(sib)) == BLACK) {
                    setColor(sib, RED);
                    x = parentOf(x);
                } else {
                    if(colorOf(rightOf(sib)) == BLACK) {
                        setColor(leftOf(sib), BLACK);
                        setColor(sib, RED);
                        rotateRight(sib);
                        sib = rightOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(rightOf(sib), BLACK);
                    rotateLeft(x.parent);
                    x = root;
                }
            } else {
                Entry<K,V> sib = leftOf(parentOf(x));                
                if(colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rotateRight(x.parent);
                    sib = leftOf(parentOf(x));
                }
                if(colorOf(rightOf(sib)) == BLACK && colorOf(leftOf(sib)) == BLACK){
                    setColor(sib, RED);
                    x = parentOf(x);
                } else {                    
                    if(colorOf(leftOf(sib)) == BLACK) {
                        setColor(rightOf(sib), BLACK);
                        setColor(sib, RED);
                        rotateLeft(sib);                        
                        sib = leftOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(leftOf(sib), BLACK);
                    rotateRight(x.parent);
                    x = root;
                }
            }
        }
        setColor(x, BLACK);
    }
    /*------------------------------------------------------------*/
    private static <K,V> boolean colorOf(Entry<K,V> p) {
        return (p == null ? BLACK : p.color);
    }
    private static <K,V> Entry<K,V> parentOf(Entry<K,V> p) {
        return (p == null ? null: p.parent);
    }
    private static <K,V> void setColor(Entry<K,V> p, boolean c) {
        if (p != null)
            p.color = c;
    }
    private static <K,V> Entry<K,V> leftOf(Entry<K,V> p) {
        return (p == null) ? null: p.left;
    }
    private static <K,V> Entry<K,V> rightOf(Entry<K,V> p) {
        return (p == null) ? null: p.right;
    }
    /**
     * Do a right rotation around this node.
    */
    private void rotateRight(Entry<K,V> p) {
        Entry<K,V> x = leftOf(p);
        p.left = rightOf(x);
        if (rightOf(x) != null)
            x.right.parent = p;
        x.parent = parentOf(p);        
        if (parentOf(p) == null) {
            root = (Entry)x;
        } else if (p == rightOf(parentOf(p))){ 
            p.parent.right = x;
        } else {
            x.parent.left = x;
        }
        x.right = p;
        p.parent = x;
    }
    /**
     * Do a left rotation around this node.
    */
    private void rotateLeft(Entry<K,V> p) {        
        Entry<K,V> y = rightOf(p);
        p.right = leftOf(y);
        if (leftOf(y) != null)
            y.left.parent = p;
        y.parent = parentOf(p);
        if (parentOf(p) == null) {
            root = (Entry)y;
        } else if (p == leftOf(parentOf(p))) {
            p.parent.left = y;
        } else {
            p.parent.right = y;
        }
        y.left = p;
        p.parent = y;
    }    
    /*------------------------------------------------------------*/
    private int compare(Object k1, Object k2) {
        return comparator == null ? ((Comparable<? super K>)k1).compareTo((K)k2)
            : comparator.compare((K)k1, (K)k2);
    }    
    private Entry<K, V> getEntryAfter(K key, boolean inclusive) {
        Entry<K, V> found = null;
        Entry<K, V> node = root;
        while (node != null && key != null) {
            int c = compare(key, node.getKey());
            if (inclusive && c == 0) {
                return node;
            }
            if (c >= 0) {
                node = rightOf(node);
            } else {
                found = node;
                node = leftOf(node);
            }
        }
        return found;
    }
    private Entry<K, V> getEntryBefore(K key, boolean inclusive) {
        Entry<K, V> found = null;
        Entry<K, V> node = root;
        while (node != null && key != null) {
            int c = compare(key, node.getKey());
            if (inclusive && c == 0) {
                return node;
            }
            if (c <= 0) {
                node = leftOf(node);
            } else {
                found = node;
                node = rightOf(node);
            }
        }
        return found;
    }    
    private static <K,V> Entry<K,V> successor(Entry<K,V> t) {      
        if (t == null)
            return null;
        else if (rightOf(t) != null) {
            Entry<K,V> p = rightOf(t);
            while (leftOf(p) != null)
                p = leftOf(p);
            return p;
        } else {
            Entry<K,V> p = parentOf(t);
            Entry<K,V> ch = t;
            while (p != null && ch == rightOf(p)) {
                ch = p;
                p = parentOf(p);
            }
            return p;
        }
    }
    private Entry<K,V> getFirstEntry() {
        Entry<K,V> p = root;
        if (p != null){
            while(leftOf(p) != null){
                p = leftOf(p);
            }
        }
        return p;
    }
    private  Entry<K,V> getLastEntry() {
        Entry<K,V> p = root;
        if (p != null){
            while(rightOf(p) != null){
                p = rightOf(p);
            }
        }
        return p;
    }
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return new EntrySet();
    }
    @Override
    public Collection<V> values() {
        return new Values();
    }
    @Override
    public Set<K> keySet() {
        return new KeySet();
    }

    @Override
    public Comparator<? super K> comparator() {
        return comparator;
    }

    
    @Override
    public K firstKey() {
        return getFirstEntry().key;
    }

    @Override
    public K lastKey() {
        return lastEntry().getKey();
    }    

    @Override
    public Entry<K, V> lowerEntry(K key) {
        Entry<K,V> e = getEntryBefore(key, false);
        return (e != null) ? e : null;
    }

    @Override
    public K lowerKey(K key) {
        Entry<K,V> e = getEntryBefore(key, false);
        return (e != null) ? e.getKey() : null;
    }

    @Override
    public Entry<K, V> floorEntry(K key) {
        Entry<K,V> e = getEntryBefore(key, true);
        return (e != null) ? e : null;
    }

    @Override
    public K floorKey(K key) {
        Entry<K,V> e = getEntryBefore(key, true);
        return (e != null) ? e.getKey() : null;
    }

    @Override
    public Entry<K, V> ceilingEntry(K key) {
        Entry<K,V> e = getEntryAfter(key, true);
        return (e != null) ? e : null;
    }

    @Override
    public K ceilingKey(K key) {
        Entry<K,V> e = getEntryAfter(key, true);
        return (e != null) ? e.getKey() : null;
    }

    @Override
    public Entry<K, V> higherEntry(K key) {
        Entry<K,V> e = getEntryAfter(key, false);
        return (e != null) ? e : null;
    }

    @Override
    public K higherKey(K key) {
        Entry<K,V> e = getEntryAfter(key, false);
        return (e != null) ? e.getKey() : null;        
    }

    @Override
    public Entry<K, V> firstEntry() {
        return getFirstEntry();
    }

    @Override
    public Entry<K, V> lastEntry() {
        Entry<K,V> p = root;
        if (p != null){
            while(rightOf(p) != null){
                p = rightOf(p);
            }
        }
        return p;
    }

    @Override
    public Entry<K, V> pollFirstEntry() {
        Entry<K,V> p = getFirstEntry();
        if (p != null)
            deleteEntry(p);
        return p;
    }

    @Override
    public Entry<K, V> pollLastEntry() {
        Entry<K,V> p = getLastEntry();
        if (p != null)
            deleteEntry(p);
        return p;
    }

    @Override
    public NavigableMap<K, V> descendingMap() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public NavigableSet<K> navigableKeySet() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public NavigableSet<K> descendingKeySet() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public NavigableMap<K, V> subMap(K fromKey, K toKey) {
        return subMap(fromKey, true, toKey, false);        
    }
    @Override
    public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
        return new NavigableSubMap(this,false,fromKey,fromInclusive,false,toKey,toInclusive);
    }

    @Override
    public NavigableMap<K, V> headMap(K toKey) {
       return headMap(toKey, false);        
    }
    @Override
    public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
        return new NavigableSubMap(this,true,null,true,false,toKey,inclusive);
    }

    @Override
    public NavigableMap<K, V> tailMap(K fromKey) {
        return tailMap(fromKey, true);
        
    }
    @Override
    public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
        return new NavigableSubMap(this,false,fromKey,inclusive,true,null,true);
    }
    
    /*------------------------------------------------------------------*/
    private class EntrySet extends AbstractSet<Map.Entry<K,V>> {
        @Override
        public Iterator<Map.Entry<K,V>> iterator() {
            return new EntryIterator(getFirstEntry());
        }
        @Override
        public int size() {
            return size;
        }
    }
    private class EntryIterator extends PrivateEntryIterator<Map.Entry<K,V>> {
        EntryIterator(Entry<K,V> first) {
            super(first);
        }  
        @Override
        public Entry<K,V> next() {
            return nextEntry();
        }
    }
    /*------------------------------------------------------------*/
    private class Values extends AbstractCollection<V> {
        @Override
        public Iterator<V> iterator() {
            return new ValueIterator(getFirstEntry());
        }
        @Override
        public int size() {
            return size;
        }        
    }
    private class ValueIterator extends PrivateEntryIterator<V> {
        ValueIterator(Entry<K,V> first) {
            super(first);
        }
        @Override
        public V next() {
            return nextEntry().value;
        }
    }
    private class KeySet extends AbstractSet<K> {
        @Override
        public Iterator<K> iterator() {
            return new KeyIterator(getFirstEntry());
        }
        @Override
        public int size() {
            return size;
        }        
    }
    private class KeyIterator extends PrivateEntryIterator<K> {
        KeyIterator(Entry<K,V> first) {
            super(first);
        }
        @Override
        public K next() {
            return nextEntry().key;
        }
    }
    /*------------------------------------------------------------*/
    abstract class PrivateEntryIterator<T> implements Iterator<T> {
        Entry<K,V> next;
        Entry<K,V> last;
        PrivateEntryIterator(Entry<K,V> first) {
            last = null;
            next = first;
        }
        @Override
        public final boolean hasNext() {
            return next != null;
        }
        public Entry<K,V> nextEntry() {
            Entry<K,V> e = next;
            next = successor(e);
            last = e;
            return e;
        }
    }
    /*------------------------------------------------------------*/
    public static class NavigableSubMap<K,V> extends AbstractMap<K,V> implements NavigableMap<K,V>{
        final MyTreeMap<K,V> m;
        final K lo, hi;
        final boolean fromStart, toEnd;
        final boolean loInclusive, hiInclusive;
        NavigableSubMap(MyTreeMap<K, V> xm,
                boolean fromStart, K lo, boolean loInclusive, 
                boolean toEnd, K hi, boolean hiInclusive) {
            if (!fromStart && !toEnd) {
                if(lo == null && hi == null) {
                    xm.clear();
                } else if(lo != null && hi == null) {
                    xm.compare(lo, lo);
                } else if(lo == null && hi != null){
                    xm.compare(hi, hi);
                } else {    
                    if (xm.compare(lo, hi) > 0)
                        throw new IllegalArgumentException("fromKey > toKey");
                }
            } else {
                if (!fromStart) // type check
                    if(lo != null){
                        xm.compare(lo, lo);
                    } else {
                        xm.clear();
                    }
                if (!toEnd)
                    if(hi != null){
                        xm.compare(hi, hi);
                    } else {
                        xm.clear();
                    }
            }
            this.m = xm;
            this.lo = lo;
            this.hi = hi;
            this.fromStart = fromStart;
            this.toEnd = toEnd;
            this.loInclusive = loInclusive;
            this.hiInclusive = hiInclusive;
        }        
        private boolean tooLow(Object key) {
            if (!fromStart) {
                int c = m.compare(key, lo);
                if (c < 0 || (c == 0 && !loInclusive))
                    return true;
            }
            return false;
        }
        private boolean tooHigh(Object key) {
            if (!toEnd && hi != null) {
                int c = m.compare(key, hi);
                if (c > 0 || (c == 0 && !hiInclusive))
                    return true;
            }
            return false;
        }
        private MyTreeMap.Entry<K,V> absLowest() {
            MyTreeMap.Entry<K,V> e;
            if(fromStart){
                e = m.getFirstEntry();
            } else if(loInclusive){
                e = m.getEntryAfter(lo != null ? lo : (!m.isEmpty() ? firstKey() : null),true);
            } else {
                e = m.getEntryAfter(lo,false);
            }
            return (e == null || tooHigh(e.getKey())) ? null : e;
        }
        private MyTreeMap.Entry<K,V> absHighFence() {
            if(toEnd){
                return null;
            } else if(hiInclusive){
                return m.getEntryAfter(hi,false);
            } else {
                return m.getEntryAfter(hi != null ? hi : (!m.isEmpty() ? lastKey() : null),true);
            }
        }
        @Override
        public Set<Map.Entry<K, V>> entrySet() {
            return new EntrySetView();
        }
        @Override
        public Comparator<? super K> comparator() {
            return m.comparator;
        }        
        @Override
        public K firstKey() {
            Entry<K,V> f = firstEntry();
            return (f != null) ? f.getKey() : null;
        }
        @Override
        public K lastKey() {
            Entry<K,V> l = lastEntry();
            return (l != null) ? l.getKey() : null;
        }        
        @Override
        public Entry<K, V> lowerEntry(K key) {
            return m.lowerEntry(key);
        }
        @Override
        public K lowerKey(K key) {
            return m.lowerKey(key);
        }
        @Override
        public Entry<K, V> floorEntry(K key) {
            return m.floorEntry(key);
        }
        @Override
        public K floorKey(K key) {
            return m.floorKey(key);
        }
        @Override
        public Entry<K, V> ceilingEntry(K key) {
            return m.ceilingEntry(key);
        }
        @Override
        public K ceilingKey(K key) {
            return m.ceilingKey(key);
        }
        @Override
        public Entry<K, V> higherEntry(K key) {
            return m.higherEntry(key);
        }
        @Override
        public K higherKey(K key) {
            return m.higherKey(key);
        }
        @Override
        public Entry<K, V> firstEntry() {
            return m.firstEntry();
        }
        @Override
        public Entry<K, V> lastEntry() {
            return m.lastEntry();
        }
        @Override
        public Entry<K, V> pollFirstEntry() {
            return m.pollFirstEntry();
        }
        @Override
        public Entry<K, V> pollLastEntry() {
            return m.pollLastEntry();
        }
        @Override
        public NavigableMap<K, V> descendingMap() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        @Override
        public NavigableSet<K> navigableKeySet() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        @Override
        public NavigableSet<K> descendingKeySet() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        @Override
        public NavigableMap<K, V> subMap(K fromKey, K toKey) {
            return subMap(fromKey, true, toKey, false);
        }
        @Override
        public NavigableMap<K, V> headMap(K toKey) {
            return headMap(toKey, false);
        }
        @Override
        public NavigableMap<K, V> tailMap(K fromKey) {
            return tailMap(fromKey, true);
        }
        @Override
        public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
            return m.subMap(fromKey, fromInclusive, toKey, toInclusive);
        }
        @Override
        public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
            return m.headMap(toKey, inclusive);
        }
        @Override
        public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
            return m.tailMap(fromKey, inclusive);
        }
        private class EntrySetView extends AbstractSet<Map.Entry<K,V>> {
            @Override
            public Iterator<Map.Entry<K,V>> iterator() {
                return new SubMapEntryIterator(absLowest(), absHighFence());                
            }
            @Override
            public int size() {
                int s = 0;
                for(Entry<K,V> e : entrySet()){
                    s++;
                }
                return s;
            }        
        }
        abstract class SubMapIterator<T> implements Iterator<T> {
            MyTreeMap.Entry<K,V> last;
            MyTreeMap.Entry<K,V> next;
            final Object fenceKey;
            SubMapIterator(MyTreeMap.Entry<K,V> first, MyTreeMap.Entry<K,V> fence) {
                last = null;
                next = first;
                fenceKey = fence == null ? new Object() : fence.getKey();
            }
            @Override
            public boolean hasNext() {
                return next != null && next.getKey() != fenceKey;
            }
            public MyTreeMap.Entry<K,V> nextEntry() {
                MyTreeMap.Entry<K,V> e = next;
                next = successor(e);
                last = e;
                return e;
            }        
        }
        class SubMapEntryIterator extends SubMapIterator<Map.Entry<K,V>> {
            public SubMapEntryIterator(MyTreeMap.Entry<K, V> first, MyTreeMap.Entry<K, V> fence) {
                super(first, fence);
            }        
            @Override
            public Map.Entry<K, V> next() {
                return nextEntry();
            }        
        }
    }
    /*------------------------------------------------------------*/        
    static class Entry<K,V> implements Map.Entry<K,V> {
        K key;
        V value;
        Entry<K,V> left;
        Entry<K,V> right;
        Entry<K,V> parent;
        boolean color = BLACK;        
        public Entry(K xkey, V xvalue,Entry<K,V> xparent) {
            this.key = xkey;
            this.value = xvalue;
            this.parent = xparent;
        }        
        @Override
        public K getKey() {
            return key;
        }
        @Override
        public V getValue() {
            return value;
        }
        @Override
        public V setValue(V v) {
            V val = value;
            value = v;
            return val;
        }
        @Override
        public String toString() {
            return "["+ this.getKey() + " -> " + this.getValue() + "]";
        }                  
    }  
}