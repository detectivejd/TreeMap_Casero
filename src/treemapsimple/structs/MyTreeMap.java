package treemapsimple.structs;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
/**
 *
 * @author detectivejd
 * @param <K>
 * @param <V>
 */
public class MyTreeMap<K,V> implements Map<K, V>
{
    private final Comparator<? super K> comparator;
    private final boolean RED   = true;
    private final boolean BLACK = false;
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
    public MyTreeMap(Map<? extends K, ? extends V> m) {
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
    public void clear() {        
        size = 0;
        root = null;
    }
    /*------------------------------------------------------------*/
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
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
                    if(cmp < 0){
                        t = leftOf(t);
                    } else if(cmp > 0) {
                        t = rightOf(t);
                    } else {
                        return t.value = value;
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
        //if (p.left != null && p.right != null) {
        if(leftOf(p) != null && rightOf(p) != null){
            Entry<K,V> s = successor(p);
            p.key = s.key;
            p.value = s.value;
            p = s;
        } 
        Entry<K,V> rep = (leftOf(p) != null ? leftOf(p) : rightOf(p));
        //Entry<K,V> rep = (p.left != null ? p.left : p.right);
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
    private boolean colorOf(Entry<K,V> p) {
        return (p == null ? BLACK : p.color);
    }
    private Entry<K,V> parentOf(Entry<K,V> p) {
        return (p == null ? null: p.parent);
    }
    private void setColor(Entry<K,V> p, boolean c) {
        if (p != null)
            p.color = c;
    }
    private Entry<K,V> leftOf(Entry<K,V> p) {
        return (p == null) ? null: p.left;
    }
    private Entry<K,V> rightOf(Entry<K,V> p) {
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
    
     @Override
    public String toString(){
        if (root == null)
            return "";
        else
            return print(root, 0);
    }
    private String print(Entry<K,V> x, int depth) {
        if (x == null)
            return "";
        else
            return print(x.right, depth + 1) + 
                indent(depth) + x + 
                "\n"+ print(x.left, depth + 1);
            
    }
    private String indent(int s) {
        String result = "";
        for (int i = 0; i < s; i++)
            result += "  ";
        return result;
    }  
    /*------------------------------------------------------------*/
    private Entry<K,V> successor(Entry<K,V> t) {      
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
        public Map.Entry<K,V> next() {
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
    class Entry<K,V> implements Map.Entry<K,V> {
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