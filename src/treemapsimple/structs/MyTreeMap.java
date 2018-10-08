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
        for (Entry<K,V> e = firstEntry(); e != null; e = getEntryAfter(e.getKey(),false)){
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
        Entry<K,V> nil = new Entry();
        Entry<K,V> y = (leftOf(p) == null || rightOf(p) == null) ? p : higherEntry(p.getKey());
        Entry<K,V> x = (leftOf(y) != null) ? leftOf(y) : (rightOf(y) != null ? rightOf(y) : nil);        
        x.parent = parentOf(y);
        if (parentOf(y) == null) {
            root = (x == nil ? null : x);
        } else {
            if (y == leftOf(parentOf(y))) {
                y.parent.left = (x == nil) ? null : x;
            } else {
                y.parent.right = (x == nil) ? null : x; 
            }
        }       
        if (y != p){
            p = y;
        }
        if (colorOf(y) == BLACK) {
            fixDown(x);
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
            root = x;
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
            root = y;
        } else if (p == leftOf(parentOf(p))) {
            p.parent.left = y;
        } else {
            p.parent.right = y;
        }
        y.left = p;
        p.parent = y;
    }    
    /*------------------------------------------------------------*/
    private int compare(K k1, K k2) {
        return comparator == null ? ((Comparable<? super K>)k1).compareTo((K)k2)
            : comparator.compare(k1, k2);
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
    Iterator<K> descendingKeyIterator() {
        return new DescendingKeyIterator(lastEntry());
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
        return navigableKeySet();
    }
    @Override
    public Comparator<? super K> comparator() {
        return comparator;
    }
    Iterator<K> keyIterator() {
        return new KeyIterator(firstEntry());
    }    
    @Override
    public K firstKey() {
        return firstEntry().getKey();
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
        Entry<K,V> p = root;
        if (p != null){
            while(leftOf(p) != null){
                p = leftOf(p);
            }
        }
        return p;
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
        Entry<K,V> p = firstEntry();
        if (p != null)
            deleteEntry(p);
        return p;
    }
    @Override
    public Entry<K, V> pollLastEntry() {
        Entry<K,V> p = lastEntry();
        if (p != null)
            deleteEntry(p);
        return p;
    }
    @Override
    public NavigableMap<K, V> descendingMap() {
        return new DescendingSubMap(this,true, null, true,true, null, true);
    }
    @Override
    public NavigableSet<K> navigableKeySet() {
        return new KeySet(this);
    }
    @Override
    public NavigableSet<K> descendingKeySet() {
        return descendingMap().navigableKeySet();
    }
    @Override
    public NavigableMap<K, V> subMap(K fromKey, K toKey) {
        return subMap(fromKey, true, toKey, false);        
    }
    @Override
    public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
        return new AscendingSubMap(this,false,fromKey,fromInclusive,false,toKey,toInclusive);
    }
    @Override
    public NavigableMap<K, V> headMap(K toKey) {
       return headMap(toKey, false);        
    }
    @Override
    public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
        return new AscendingSubMap(this,true,null,true,false,toKey,inclusive);
    }
    @Override
    public NavigableMap<K, V> tailMap(K fromKey) {
        return tailMap(fromKey, true);        
    }
    @Override
    public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
        return new AscendingSubMap(this,false,fromKey,inclusive,true,null,true);
    }    
    /*------------------------------------------------------------------*/
    private class EntrySet extends AbstractSet<Map.Entry<K,V>> {
        @Override
        public Iterator<Map.Entry<K,V>> iterator() {
            return new EntryIterator(firstEntry());
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
            return new ValueIterator(firstEntry());
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
            return nextEntry().getValue();
        }
    }
    private class KeySet extends AbstractSet<K> implements NavigableSet<K> {
        private NavigableMap<K, ?> m;
        KeySet(NavigableMap<K,?> map) { 
            m = map; 
        }        
        @Override
        public Iterator<K> iterator() {
            if (m instanceof MyTreeMap)
                return ((MyTreeMap<K,?>)m).keyIterator();
            else
                return ((MyTreeMap.NavigableSubMap<K,?>)m).keyIterator();
        }
        @Override
        public int size() {
            return size;
        }        
        @Override
        public Comparator<? super K> comparator() {
            return m.comparator();
        }
        @Override
        public K lower(K e) { return m.lowerKey(e); }
        @Override
        public K floor(K e) { return m.floorKey(e); }
        @Override
        public K ceiling(K e) { return m.ceilingKey(e); }
        @Override
        public K higher(K e) { return m.higherKey(e); }
        @Override
        public K first() { return m.firstKey(); }
        @Override
        public K last() { return m.lastKey(); }
        @Override
        public K pollFirst() {
            Map.Entry<K,?> e = m.pollFirstEntry();
            return (e == null) ? null : e.getKey();
        }
        @Override
        public K pollLast() {
            Map.Entry<K,?> e = m.pollLastEntry();
            return (e == null) ? null : e.getKey();
        }
        @Override
        public NavigableSet<K> descendingSet() {
            return new KeySet(m.descendingMap());
        }
        @Override
        public Iterator<K> descendingIterator() {
            if (m instanceof MyTreeMap)
                return ((MyTreeMap<K,?>)m).descendingKeyIterator();
            else
                return ((MyTreeMap.NavigableSubMap<K,?>)m).descendingKeyIterator();
        }
        @Override
        public NavigableSet<K> subSet(K fromElement, boolean fromInclusive,
                                      K toElement,   boolean toInclusive) {
            return new KeySet(m.subMap(fromElement, fromInclusive,
                                          toElement,   toInclusive));
        }
        @Override
        public NavigableSet<K> headSet(K toElement, boolean inclusive) {
            return new KeySet(m.headMap(toElement, inclusive));
        }
        @Override
        public NavigableSet<K> tailSet(K fromElement, boolean inclusive) {
            return new KeySet(m.tailMap(fromElement, inclusive));
        }        
        @Override
        public NavigableSet<K> subSet(K fromElement, K toElement) {
            return subSet(fromElement, true, toElement, false);
        }
        @Override
        public NavigableSet<K> headSet(K toElement) {
            return headSet(toElement, false);
        }
        @Override
        public NavigableSet<K> tailSet(K fromElement) {
            return tailSet(fromElement, true);
        }
    }
    private class KeyIterator extends PrivateEntryIterator<K> {
        KeyIterator(Entry<K,V> first) {
            super(first);
        }
        @Override
        public K next() {
            return nextEntry().getKey();
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
            next = higherEntry(e.getKey());
            last = e;
            return e;
        }
        public Entry<K,V> prevEntry() {
            Entry<K,V> e = next;
            next = lowerEntry(e.getKey());
            last = e;
            return e;
        }
    }
    /*------------------------------------------------------------*/    
    abstract static class NavigableSubMap<K,V> extends AbstractMap<K,V> implements NavigableMap<K,V>{
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
                    hi = xm.lastKey();
                } else if(lo == null && hi != null){
                    lo = xm.firstKey();
                    xm.compare(hi, hi);
                } else {    
                    if (xm.compare(lo, hi) > 0){
                        K aux = lo;
                        lo = hi;
                        hi = aux;
                    }
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
        private boolean tooLow(K key) {
            if (!fromStart) {
                int c = m.compare(key, lo);
                if (c < 0 || (c == 0 && !loInclusive))
                    return true;
            }
            return false;
        }
        private boolean tooHigh(K key) {
            if (!toEnd && hi != null) {
                int c = m.compare(key, hi);
                if (c > 0 || (c == 0 && !hiInclusive))
                    return true;
            }
            return false;
        }
        protected MyTreeMap.Entry<K,V> absLowest() {
            MyTreeMap.Entry<K,V> e;
            if(fromStart){
                e = m.firstEntry();
            } else if(loInclusive){
                e = m.ceilingEntry(lo != null ? lo : (!m.isEmpty() ? firstKey() : null));
            } else {
                e = m.higherEntry(lo);
            }
            return (e == null || tooHigh(e.getKey())) ? null : e;
        }
        protected MyTreeMap.Entry<K,V> absHighFence() {
            if(toEnd){
                return null;
            } else if(hiInclusive){
                return m.higherEntry(hi);
            } else {
                return m.ceilingEntry(hi != null ? hi : (!m.isEmpty() ? lastKey() : null));
            }
        }
        protected MyTreeMap.Entry<K,V> absHighest() {
            MyTreeMap.Entry<K,V> e = (toEnd ?  m.lastEntry() :
                (hiInclusive ?  m.floorEntry(hi) : m.lowerEntry(hi)));
            return (e == null || tooLow(e.key)) ? null : e;
        }
        protected MyTreeMap.Entry<K,V> absLowFence() {
            return (fromStart ? null : (loInclusive ?
                m.lowerEntry(lo) : m.floorEntry(lo)));
        }
        protected MyTreeMap.Entry<K,V> absFloor(K key) {
            if (tooHigh(key))
                return absHighest();
            MyTreeMap.Entry<K,V> e = m.floorEntry(key);
            return (e == null || tooLow(e.key)) ? null : e;
        }
        protected MyTreeMap.Entry<K,V> absCeiling(K key) {
            if (tooLow(key))
                return absLowest();
            MyTreeMap.Entry<K,V> e = m.ceilingEntry(key);
            return (e == null || tooHigh(e.key)) ? null : e;
        }
        protected MyTreeMap.Entry<K,V> absHigher(K key) {
            if (tooLow(key))
                return absLowest();
            MyTreeMap.Entry<K,V> e = m.higherEntry(key);
            return (e == null || tooHigh(e.key)) ? null : e;
        }
        protected MyTreeMap.Entry<K,V> absLower(K key) {
            if (tooHigh(key))
                return absHighest();
            MyTreeMap.Entry<K,V> e = m.lowerEntry(key);
            return (e == null || tooLow(e.key)) ? null : e;
        }
        @Override
        public Comparator<? super K> comparator() {
            return m.comparator;
        }
        @Override
        public Set<Map.Entry<K, V>> entrySet() {
            return new EntrySetView();
        }                
        @Override
        public Entry<K, V> firstEntry() {
            return subLowest();
        }
        @Override
        public K firstKey() {
            Entry<K,V> f = firstEntry();
            return (f != null) ? f.getKey() : null;
        }
        @Override
        public Entry<K, V> lastEntry() {
            return subHighest();
        }
        @Override
        public K lastKey() {
            Entry<K,V> l = lastEntry();
            return (l != null) ? l.getKey() : null;
        }        
        @Override
        public Entry<K, V> lowerEntry(K key) {
            return subLower(key);
        }
        @Override
        public K lowerKey(K key) {
            Entry<K,V> lw = lowerEntry(key);
            return (lw != null) ? lw.getKey() : null;
        }
        @Override
        public Entry<K, V> floorEntry(K key) {
            return subFloor(key);
        }
        @Override
        public K floorKey(K key) {
            Entry<K,V> fl = floorEntry(key);
            return (fl != null) ? fl.getKey() : null;
        }
        @Override
        public Entry<K, V> ceilingEntry(K key) {
            return subCeiling(key);
        }
        @Override
        public K ceilingKey(K key) {
            Entry<K,V> c = ceilingEntry(key);
            return (c != null) ? c.getKey() : null;
        }
        @Override
        public Entry<K, V> higherEntry(K key) {
            return subHigher(key);
        }
        @Override
        public K higherKey(K key) {
            Entry<K,V> h = higherEntry(key);
            return (h != null) ? h.getKey() : null;
        }                
        @Override
        public Entry<K, V> pollFirstEntry() {
            MyTreeMap.Entry<K,V> e = subLowest();
            if (e != null)
                m.deleteEntry(e);
            return e;
        }
        @Override
        public Entry<K, V> pollLastEntry() {
            MyTreeMap.Entry<K,V> e = subHighest();
            if (e != null)
                m.deleteEntry(e);
            return e;
        }
        @Override
        public final NavigableMap<K,V> subMap(K fromKey, K toKey) {
            return subMap(fromKey, true, toKey, false);
        }
        @Override
        public final NavigableMap<K,V> headMap(K toKey) {
            return headMap(toKey, false);
        }
        @Override
        public final NavigableMap<K,V> tailMap(K fromKey) {
            return tailMap(fromKey, true);
        }                                
        @Override
        public NavigableSet<K> navigableKeySet() {
            return m.navigableKeySet();
        }
        @Override
        public NavigableSet<K> descendingKeySet() {
            return descendingMap().navigableKeySet();
        }
        abstract Iterator<K> keyIterator();
        abstract Iterator<K> descendingKeyIterator();
        abstract MyTreeMap.Entry<K,V> subLowest();
        abstract MyTreeMap.Entry<K,V> subHighest();
        abstract MyTreeMap.Entry<K,V> subCeiling(K key);
        abstract MyTreeMap.Entry<K,V> subHigher(K key);
        abstract MyTreeMap.Entry<K,V> subFloor(K key);
        abstract MyTreeMap.Entry<K,V> subLower(K key);
        class EntrySetView extends AbstractSet<Map.Entry<K,V>> {
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
                next = m.higherEntry(e.getKey());
                last = e;
                return e;
            }
            final MyTreeMap.Entry<K,V> prevEntry() {
                MyTreeMap.Entry<K,V> e = next;
                next = m.lowerEntry(e.getKey());
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
        class SubMapKeyIterator extends SubMapIterator<K> {
            public SubMapKeyIterator(MyTreeMap.Entry<K, V> first, MyTreeMap.Entry<K, V> fence) {
                super(first, fence);
            }            
            @Override
            public K next() {
                return nextEntry().getKey();
            }
        }
        class DescendingSubMapKeyIterator extends SubMapIterator<K> {
            public DescendingSubMapKeyIterator(MyTreeMap.Entry<K, V> first, MyTreeMap.Entry<K, V> fence) {
                super(first, fence);
            }
            @Override
            public K next() {
                return prevEntry().getKey();
            }            
        }
        class DescendingSubMapEntryIterator extends SubMapIterator<Map.Entry<K,V>> {
            public DescendingSubMapEntryIterator(MyTreeMap.Entry<K, V> first, MyTreeMap.Entry<K, V> fence) {
                super(first, fence);
            }
            @Override
            public Entry<K, V> next() {
                return prevEntry();
            }            
        }
    }
    public static class AscendingSubMap<K,V> extends NavigableSubMap<K,V> {        
        public AscendingSubMap(MyTreeMap<K, V> xm, boolean fromStart, K lo, 
                boolean loInclusive, boolean toEnd, K hi, boolean hiInclusive) {
            super(xm, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
        }
        @Override
        Iterator<K> keyIterator() {
            return new SubMapKeyIterator(absLowest(), absHighFence());
        }        
        @Override
        public NavigableMap<K,V> subMap(K fromKey, boolean fromInclusive,
                                        K toKey,   boolean toInclusive) {
            return new AscendingSubMap<>(m,
                                         false, fromKey, fromInclusive,
                                         false, toKey,   toInclusive);
        }
        @Override
        public NavigableMap<K,V> headMap(K toKey, boolean inclusive) {
            return new AscendingSubMap<>(m,
                                         fromStart, lo,    loInclusive,
                                         false,     toKey, inclusive);
        }
        @Override
        public NavigableMap<K,V> tailMap(K fromKey, boolean inclusive) {
            return new AscendingSubMap<>(m,
                                         false, fromKey, inclusive,
                                         toEnd, hi,      hiInclusive);
        }        
        @Override
        Iterator<K> descendingKeyIterator() {
            return new DescendingSubMapKeyIterator(absHighest(), absLowFence());
        }
        @Override
        public NavigableMap<K, V> descendingMap() {
            return new DescendingSubMap(m,fromStart, lo, loInclusive,
                    toEnd,hi, hiInclusive);
        }
        @Override
        MyTreeMap.Entry<K,V> subLowest() { return absLowest(); }
        @Override
        MyTreeMap.Entry<K,V> subHighest() { return absHighest(); }
        @Override
        MyTreeMap.Entry<K,V> subCeiling(K key) { return absCeiling(key); }
        @Override
        MyTreeMap.Entry<K,V> subHigher(K key) { return absHigher(key); }
        @Override
        MyTreeMap.Entry<K,V> subFloor(K key) { return absFloor(key); }
        @Override
        MyTreeMap.Entry<K,V> subLower(K key) { return absLower(key); }
    }
    public static class DescendingSubMap<K,V>  extends NavigableSubMap<K,V> {
        public DescendingSubMap(MyTreeMap<K, V> xm, boolean fromStart, K lo, 
                boolean loInclusive, boolean toEnd, K hi, boolean hiInclusive) {
            super(xm, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
        }
        @Override
        Iterator<K> keyIterator() {
            return new DescendingSubMapKeyIterator(absHighest(), absLowFence());
        }
        @Override
        public NavigableMap<K,V> subMap(K fromKey, boolean fromInclusive,
                                        K toKey,   boolean toInclusive) {
            return new DescendingSubMap<>(m,
                                          false, toKey,   toInclusive,
                                          false, fromKey, fromInclusive);
        }
        @Override
        public NavigableMap<K,V> headMap(K toKey, boolean inclusive) {
            return new DescendingSubMap<>(m,
                                          false, toKey, inclusive,
                                          toEnd, hi,    hiInclusive);
        }
        @Override
        public NavigableMap<K,V> tailMap(K fromKey, boolean inclusive) {
            return new DescendingSubMap<>(m,
                                          fromStart, lo, loInclusive,
                                          false, fromKey, inclusive);
        }        
        @Override
        public Set<Map.Entry<K,V>> entrySet() {
            return new DescendingEntrySetView();
        }
        @Override
        public NavigableMap<K,V> descendingMap() {
            return new AscendingSubMap<>(m,
                                       fromStart, lo, loInclusive,
                                       toEnd,     hi, hiInclusive);
        }
        @Override
        Iterator<K> descendingKeyIterator() {
            return new SubMapKeyIterator(absLowest(), absHighFence());
        }
        @Override
        MyTreeMap.Entry<K,V> subLowest() { return absHighest(); }
        @Override
        MyTreeMap.Entry<K,V> subHighest() { return absLowest(); }
        @Override
        MyTreeMap.Entry<K,V> subCeiling(K key) { return absFloor(key); }
        @Override
        MyTreeMap.Entry<K,V> subHigher(K key) { return absLower(key); }
        @Override
        MyTreeMap.Entry<K,V> subFloor(K key) { return absCeiling(key); }
        @Override
        MyTreeMap.Entry<K,V> subLower(K key) { return absHigher(key); }
        
        class DescendingEntrySetView extends EntrySetView {
            @Override
            public Iterator<Map.Entry<K,V>> iterator() {
                return new DescendingSubMapEntryIterator(absHighest(), absLowFence());
            }
        }
    }
    class DescendingKeyIterator extends PrivateEntryIterator<K> {
        DescendingKeyIterator(Entry<K,V> first) {
            super(first);
        }
        @Override
        public K next() {
            return prevEntry().getKey();
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
        public Entry() { }        
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
    }  
}