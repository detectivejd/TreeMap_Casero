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
    // usaremos la variable comparator para determinar el órden del árbol
    private final Comparator<? super K> comparator;
    // RED y BLACK las usamos como mecánicas de los árboles red and black
    private static boolean RED   = true;
    private static boolean BLACK = false;
    // root es la raíz de nuestro árbol hash
    private Entry<K,V> root;
    // con size obtendremos la cantidad de entradas(nodos) del árbol
    private int size;
    /**
     * Construye una nueva instancia de TreeMap ordenando sus entradas
     * por la clave de forma creciente
     */
    public MyTreeMap() {
        clear();
        this.comparator = null;
    }
    /**
     * Construye una nueva instancia de TreeMap con un comparador
     * pasado por parámetro para establecer la ordenación (usando las claves
     * para ordenar)
     * 
     * @param xcomparator 
     */
    public MyTreeMap(Comparator<? super K> xcomparator) {
        clear();
        this.comparator = xcomparator;
    }
    /**
     * Construye una nueva instancia de TreeMap pasando un Map el cuál
     * usaremos los datos de éste y ordenaremos de forma creciente según
     * las claves
     * 
     * @param m 
     */
    public MyTreeMap(java.util.Map<? extends K, ? extends V> m) {
        clear();
        this.comparator = null;
        putAll(m);
    }
    /**
     * Devuelve la cantidad de entradas(nodos) almacenadas en el árbol
     * 
     * @return int -> entero
     */
    @Override
    public int size() {
        return size;
    }
    /**
     * Determina si nuestro árbol está vacio o no.
     * 
     * @return boolean -> verdadero o falso 
     */
    @Override
    public boolean isEmpty() {
        return size == 0;        
    }
    /**
     * Método para limpiar nuestro árbol por completo
     */
    @Override
    public final void clear() {        
        size = 0;
        root = null;
    }
    /*------------------------------------------------------------*/
    /**
     * Método para almacenar todos las entradas del map pasado por
     * parámetro a nuestro árbol.
     * 
     * @param m 
     */
    @Override
    public final void putAll(Map<? extends K, ? extends V> m) {
        m.entrySet().forEach((e) -> {
            put(e.getKey(), e.getValue());
        });
    }
    /**
     * Función creada para almacenar entradas(nodos) a nuestro árbol
     * 
     * @param key
     * @param value
     * @return V -> valor 
     */
    @Override
    public V put(K key, V value) {
        /* 
            sí la clave es distinta de nula, la función seguirá 
            ejecutandose, de lo contrario no pasará nada.
        */
        if(key != null){
            // creamos una variable t a la cual le pasamos la raíz.
            Entry<K,V> t = root;
            /* 
                sí t es nula instanciamos la raíz con nuestra primera
                entrada ingresada.
            */
            if(t == null){
                root = new Entry(key,value,null);
                size = 1;
            } else {
                /*
                    de lo contrario creamos una entrada llamada parent que será
                    nula además de crear un entero llamado cmp con valor 0
                */
                Entry<K,V> parent = null;                    
                int cmp = 0;
                /*
                    mientras t sea distinto de nulo, parent tendrá el contenido
                    de t, además cmp tendrá asignado el resultado de la función
                    checkCompare pasados t y key como parámetros.
                    Sí cmp es 0 es porque ya está una entrada con esa clave y
                    tendrá que reemplazar el valor e terminar la función, de lo
                    contrario a t le asignamos el resultado de un operador
                    ternario como condición que si cmp es menor a 0 obtendremos
                    el izquierdo de t, caso contrario (siendo mayor a 0) 
                    obtendremos el derecho de t, así hasta terminar el while.
                */
                while (t != null) {
                    parent = t;
                    cmp = checkCompare(t,key);
                    if(cmp == 0){
                        return t.value = value;
                    } else {
                        t = (cmp < 0) ? leftOf(t) : rightOf(t);
                    }
                }
                /*
                    Una vez terminado el while creamos una nueva entrada
                    a la cual le asignamos la clave y valor pasados por
                    parámetros además del parent (pariente).
                    sí en el último recorrido cmp mantuvo un valor negativo
                    la entrada nueva estará posicionada en el izquierdo de
                    parent de lo contrario quedará posicionado en la parte
                    derecha de parent
                */
                Entry<K,V> e = new Entry<>(key, value, parent);
                if (cmp < 0) {
                    parent.left = e;
                } else {
                    parent.right = e;
                }
                /*
                    ponemos a nuestro entrada creada color rojo, balanceamos
                    nuestro árbol luego de almacenar la entrada y se incrementa
                    la variable size.
                */
                setColor(e, RED);
                fixUp(e);
                size++;
            }                            
        }
        return null;
    }
    /**
     * Función utilizada para comparar y ordenar las entradas(nodos) de nuestro 
     * árbol
     * 
     * @param x
     * @param key
     * @return int -> entero 
     */
    private int checkCompare(Entry<K,V>x, K key){
        /*
            si comparator es distinto de nulo, retornamos el resultado de
            la comparación usando la función compare de comparador pasándole
            key y la clave de x. De lo contario creamos una variable de tipo 
            comparable a la cual le pasamos key haciendo downcasting, por 
            último usamos la función compareTo de k pasando la clave de x 
            cómo parámetro y retornamos su resultado.
        */
        if(comparator != null){
            return comparator.compare(key,x.getKey());
        } else {
            Comparable<? super K> k = (Comparable<? super K>) key;
            return k.compareTo(x.getKey());
        }
    }
    /**
     * Método utilizado para balancear el árbol luego de la inserción
     * 
     * @param x -> Entry<K,V> (entrada)
     */
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
    /**
     * Verifica si existe una entrada con la clave pasada por parámetro o no
     * 
     * @param key
     * @return boolean -> verdadero o falso 
     */
    @Override
    public boolean containsKey(Object key) {
        return getEntry(key) != null;
    }
    /**
     * Obtendremos el valor de una entrada mediante la clave pasada por
     * parámetro
     * 
     * @param key
     * @return V -> valor
     */
    @Override
    public V get(Object key) {
        Entry<K,V> p = getEntry(key);
        return (p == null ? null : p.value);
    }
    /**
     * Función privada para obtener una entrada mediante una clave
     * pasada por parámetro
     * 
     * @param key
     * @return Entry<K,V> -> entrada
     */
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
    /**
     * Verifica si existe una entrada con el valor pasado por parámetro o no
     * 
     * @param value
     * @return boolean -> verdadero o falso 
     */
    @Override
    public boolean containsValue(Object value) {
        for (Entry<K,V> e = firstEntry(); e != null; e = higherEntry(e.getKey())){
            if (value != null && value.equals(e.getValue())){
                return true;
            }
        }
        return false;
    }
    /*------------------------------------------------------------*/
    /**
     * Función que usaremos para borrar entradas (nodos) de nuestro árbol
     * mediante una clave pasada por parámetro
     * 
     * @param key
     * @return V -> valor 
     */
    @Override
    public V remove(Object key) {
        /*
            Creamos una variable de tipo Entry llamada p a al cual le pasamos
            el resultado de la función de getEntry a la cual le pasamos la
            key por parámetro. 
            Si p es nulo la función terminaría y retorna nulo, de lo contrario 
            creamos una variable Value oldvalue y le pasamos el valor de p, 
            luego usamos el método deleteEntry para eliminar internamente p y 
            retorno oldValue en la función 
        */
        Entry<K,V> p = getEntry(key);
        if (p == null) {
            return null;
        } else {
            V oldValue = p.value;
            deleteEntry(p);            
            return oldValue;
        }
    }
    /**
     * Método utilizado para eliminar internamente la entrada(nodo) del
     * árbol mediante una entrada pasada por parámetro
     * 
     * @param p 
     */
    private void deleteEntry(Entry<K,V> p) {
        // Decrementa la cantidad de elementos en el árbol.
        size--;
        // Creamos una nueva entrada vacía.
        Entry<K,V> tmp = new Entry();
        /*
            Creamos una entrada llamada y y mediante un operador ternario si 
            la izquierda o derecha de p es nula que y tenga el valor de p de 
            lo contrario de obtenga al siguiente de p.
        */
        Entry<K,V> y = (leftOf(p) == null || rightOf(p) == null) ? p : higherEntry(p.getKey());
        /*
            Luego creo otra entrada llamada x y mediante operador ternario
            si la izquierda de y no es nula, x tendrá como valor la izquierda
            de y de lo contrario otro operador ternario dentro del primero que
            si la derecha de y no es nula que x obtenga como valor la derecha
            de y  de lo contrario obtendrá la entrada vacía.
        */
        Entry<K,V> x = (leftOf(y) != null) ? leftOf(y) : (rightOf(y) != null ? rightOf(y) : tmp);        
        // Al pariente de x se le asignará como valor el pariente de y
        x.parent = parentOf(y);
        /*
            Sí el pariente de y es nulo, la raíz del árbol se le asignará como
            valor mediante operador ternario que si x es una entrada vacía
            la raíz será nula de lo contrario x será el valor de la raíz
        */
        if (parentOf(y) == null) {
            root = (x == tmp ? null : x);
        } else {
            /*
                Ahora si el pariente de y no es nulo, entramos en otra
                condición que si y es igual al izquierdo del pariente de y
                el izquierdo del pariente de y tendrá como valor mediente
                operador ternario si x es una entrada vacía será nulo sino
                su valor será x
            */
            if (y == leftOf(parentOf(y))) {
                y.parent.left = (x == tmp) ? null : x;
            } else {
                /*
                    De lo contrario el derecho del pariente de y tendrá como
                    valor por operador ternario si x es una entrada vacía
                    su valor será nulo sino será x
                */
                y.parent.right = (x == tmp) ? null : x; 
            }
        }
        // Si y es distinto de p, a p se le asignará como valor y
        if (y != p){
            p = y;
        }
        /*  Y si el color de y es negro, el árbol será balanceado para 
            actualizarse luego de la eliminación
        */
        if (colorOf(y) == BLACK) {
            fixDown(x);
        }
    }
    /**
     * Método utilizado para balancear el árbol luego de la eliminación
     * 
     * @param x -> Entry<K,V> (entrada)
     */
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
    /**
     * Función estática para verificar el color de la entrada (nodo)
     * mediante una entrada pasada por parámetro
     * 
     * @param <K>
     * @param <V>
     * @param p
     * @return boolean -> verdadero o falso 
     */
    private static <K,V> boolean colorOf(Entry<K,V> p) {
        return (p == null ? BLACK : p.color);
    }
    /**
     * Función estática para obtener el pariente de una entrada(nodo)
     * mediante una entrada pasada por parámetro
     * 
     * @param <K>
     * @param <V>
     * @param p
     * @return Entry<K,V> -> entrada (nodo)
     */
    private static <K,V> Entry<K,V> parentOf(Entry<K,V> p) {
        return (p == null ? null: p.parent);
    }
    /**
     * Método estático para cambiar el color de una entrada(nodo) pasados
     * una entrada y un color por parámetros
     * 
     * @param <K>
     * @param <V>
     * @param p
     * @param c 
     */
    private static <K,V> void setColor(Entry<K,V> p, boolean c) {
        if (p != null)
            p.color = c;
    }
    /**
     * Función estática para obtener la izquierda de una entrada(nodo)
     * mediante una entrada pasada por parámetro
     * 
     * @param <K>
     * @param <V>
     * @param p
     * @return Entry<K,V> -> entrada (nodo)
     */
    private static <K,V> Entry<K,V> leftOf(Entry<K,V> p) {
        return (p == null) ? null: p.left;
    }
    /**
     * Función estática para obtener la derecha de una entrada(nodo)
     * mediante una entrada pasada por parámetro
     * 
     * @param <K>
     * @param <V>
     * @param p
     * @return Entry<K,V> -> entrada (nodo)
     */
    private static <K,V> Entry<K,V> rightOf(Entry<K,V> p) {
        return (p == null) ? null: p.right;
    }    
    /**
     * Método que rota el árbol a la derecha sobre la entrada pasada por
     * parámetro. Las rotaciones conservan el ordenamiento de claves de 
     * sus entradas (nodos) hijas.
     * 
     * @param p 
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
     * Método que rota el árbol a la izquierda sobre la entrada pasada 
     * por parámetro. Las rotaciones conservan el ordenamiento de claves 
     * de sus entradas (nodos) hijas.
     * 
     * @param p 
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
    /**
     * Función utilizada para comparar y ordenar las entradas(nodos) de los 
     * sub árboles
     * 
     * @param k1
     * @param k2
     * @return int -> entero
     */    
    private int compare(K k1, K k2) {
        return comparator == null ? ((Comparable<? super K>)k1).compareTo((K)k2)
            : comparator.compare(k1, k2);
    }
    /**
     * Devuelve el último nodo que es estrictamente mayor que la clave dada.
     * 
     * @param key
     * @param inclusive
     * @return Entry<K, V> -> entrada(nodo)
     */
    private Entry<K, V> getEntryAfter(K key, boolean inclusive) {
        Entry<K, V> found = null;
        Entry<K, V> node = root;
        while (node != null && key != null) {
            int c = checkCompare(node, key);
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
    /**
     * Devuelve el último nodo que es estrictamente menor que la clave dada.
     * 
     * @param key
     * @param inclusive
     * @return Entry<K, V> -> entrada(nodo)
     */
    private Entry<K, V> getEntryBefore(K key, boolean inclusive) {
        Entry<K, V> found = null;
        Entry<K, V> node = root;
        while (node != null && key != null) {
            int c = checkCompare(node, key);
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
    /**
     * Devuelve un conjunto de todas las claves almacenadas en 
     * nuestro árbol hash de forma decreciente
     * 
     * @return Iterator<K> -> conjunto de claves
     */
    Iterator<K> descendingKeyIterator() {
        return new DescendingKeyIterator(lastEntry());
    }
    /**
     * Devuelve un conjunto de las entradas almacenadas en 
     * nuestro árbol hash
     * 
     * @return Set<Map.Entry<K, V>> -> conjunto de entradas
     */
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return new EntrySet();
    }
    /**
     * Devuelve una colección de los valores almacenados de 
     * nuestro árbol hash
     * 
     * @return Collection<V> -> colección de valores
     */
    @Override
    public Collection<V> values() {
        return new Values();
    }
    /**
     * Devuelve un conjunto de todas las claves almacenadas en 
     * nuestro árbol hash
     * 
     * @return Set<K> -> tupla de claves
     */
    @Override
    public Set<K> keySet() {
        return navigableKeySet();
    }
    /**
     * Obtiene el comparador que usamos para ordenar el árbol hash
     * 
     * @return Comparator<? super K> -> comparador basado en claves
     */
    @Override
    public Comparator<? super K> comparator() {
        return comparator;
    }
    /**
     * Devuelve un conjunto de todas las claves almacenadas en 
     * nuestro árbol hash de forma creciente
     * 
     * @return Iterator<K> -> conjunto de claves
     */
    Iterator<K> keyIterator() {
        return new KeyIterator(firstEntry());
    }
    /**
     * Devuelve la clave de la primer entrada del árbol hash
     * 
     * @return K -> clave 
     */
    @Override
    public K firstKey() {
        return firstEntry().getKey();
    }
    /**
     * Devuelve la entrada de la primer entrada del árbol hash
     * 
     * @return Entry<K, V> -> entrada (nodo) 
     */
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
    /**
     * Devuelve la clave de la última entrada del árbol hash
     * 
     * @return K -> clave 
     */
    @Override
    public K lastKey() {
        return lastEntry().getKey();
    }
    /**
     * Devuelve la entrada de la última entrada del árbol hash
     * 
     * @return K -> clave 
     */
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
    /**
     * Devuelve una entrada mayor estrictamente menor que la clave dada.
     * 
     * @param key
     * @return Entry<K, V> -> entrada (nodo)
     */
    @Override
    public Entry<K, V> lowerEntry(K key) {
        Entry<K,V> e = getEntryBefore(key, false);
        return (e != null) ? e : null;
    }
    /**
     * Devuelve una clave de una entrada mayor estrictamente menor que la 
     * clave dada.
     * 
     * @param key
     * @return K -> clave
     */
    @Override
    public K lowerKey(K key) {
        Entry<K,V> e = getEntryBefore(key, false);
        return (e != null) ? e.getKey() : null;
    }
    /**
     * Devuelve una entrada asociada con la clave mayor menor o igual que la 
     * clave dada
     * 
     * @param key
     * @return Entry<K, V> -> entrada (nodo)
     */
    @Override
    public Entry<K, V> floorEntry(K key) {
        Entry<K,V> e = getEntryBefore(key, true);
        return (e != null) ? e : null;
    }
    /**
     * Devuelve una clave de una entrada asociada con la clave mayor menor 
     * o igual que la clave dada
     * 
     * @param key
     * @return K -> clave
     */
    @Override
    public K floorKey(K key) {
        Entry<K,V> e = getEntryBefore(key, true);
        return (e != null) ? e.getKey() : null;
    }
    /**
     * Devuelve una entrada con la menor clave mayor o igual que la clave dada.
     * 
     * @param key
     * @return Entry<K, V> -> entrada (nodo)
     */
    @Override
    public Entry<K, V> ceilingEntry(K key) {
        Entry<K,V> e = getEntryAfter(key, true);
        return (e != null) ? e : null;
    }
    /**
     * Devuelve una clave de una entrada con la menor clave mayor o igual 
     * que la clave dada.
     * 
     * @param key
     * @return K -> clave
     */
    @Override
    public K ceilingKey(K key) {
        Entry<K,V> e = getEntryAfter(key, true);
        return (e != null) ? e.getKey() : null;
    }
    /**
     * Devuelve una entrada con la clave menos estrictamente mayor que la 
     * clave dada.
     * 
     * @param key
     * @return Entry<K, V> -> entrada (nodo).
     */
    @Override
    public Entry<K, V> higherEntry(K key) {
        Entry<K,V> e = getEntryAfter(key, false);
        return (e != null) ? e : null;
    }
    /**
     * Devuelve una clave de una entrada con la clave menos estrictamente 
     * mayor que la clave dada.
     * 
     * @param key
     * @return Entry<K, V> -> entrada (nodo).
     */
    @Override
    public K higherKey(K key) {
        Entry<K,V> e = getEntryAfter(key, false);
        return (e != null) ? e.getKey() : null;        
    }
    /**
     * Elimina y devuelve una entrada con la clave mínima en este mapa.
     * 
     * @return Entry<K, V> -> entrada (nodo)
     */    
    @Override
    public Entry<K, V> pollFirstEntry() {
        Entry<K,V> p = firstEntry();
        if (p != null)
            deleteEntry(p);
        return p;
    }
    /**
     * Elimina y devuelve una entrada con la clave más grande en este mapa.
     * 
     * @return Entry<K, V> -> entrada (nodo)
     */
    @Override
    public Entry<K, V> pollLastEntry() {
        Entry<K,V> p = lastEntry();
        if (p != null)
            deleteEntry(p);
        return p;
    }
    /**
     * Devuelve una vista de NavigableSet de orden inverso de las claves 
     * contenidas en este árbol.
     * 
     * @return NavigableMap<K, V> -> mapa navegable 
     */
    @Override
    public NavigableMap<K, V> descendingMap() {
        return new DescendingSubMap(this,true, null, true,true, null, true);
    }
    /**
     * Devuelve una vista de NavigableSet de las claves contenidas en este 
     * árbol.
     * 
     * @return NavigableSet<K> -> tupla navagable
     */
    @Override
    public NavigableSet<K> navigableKeySet() {
        return new KeySet(this);
    }
    /**
     * Devuelve una vista de NavigableSet de orden inverso de las claves 
     * contenidas en este mapa.
     * 
     * @return NavigableSet<K> -> tupla navagable
     */
    @Override
    public NavigableSet<K> descendingKeySet() {
        return descendingMap().navigableKeySet();
    }
    /**
     * Devuelve una subárbol cuyas claves van de un período de inicio
     * a fin
     * 
     * @param fromKey
     * @param toKey
     * @return NavigableMap<K, V> -> mapa navegable
     */
    @Override
    public NavigableMap<K, V> subMap(K fromKey, K toKey) {
        return subMap(fromKey, true, toKey, false);        
    }
    /**
     * Función interna que devuelve un subárbol cuyas claves van desde un
     * período de inicio a fin
     * 
     * @param fromKey
     * @param fromInclusive
     * @param toKey
     * @param toInclusive
     * @return NavigableMap<K, V> -> mapa navegable
     */
    @Override
    public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
        return new AscendingSubMap(this,false,fromKey,fromInclusive,false,toKey,toInclusive);
    }
    /**
     * Devuelve un subárbol cuyas claves son estrictamente menores que la clave
     * pasada por parámetro.
     * 
     * @param toKey
     * @return NavigableMap<K, V> -> mapa navegable
     */
    @Override
    public NavigableMap<K, V> headMap(K toKey) {
       return headMap(toKey, false);        
    }
    /**
     * función interna que devuelve un subárbol cuyas claves son estrictamente 
     * menores que la clave pasada por parámetro.
     * 
     * @param toKey
     * @return NavigableMap<K, V> -> mapa navegable
     */
    @Override
    public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
        return new AscendingSubMap(this,true,null,true,false,toKey,inclusive);
    }
    /**
     * Devuelve un subárbol cuyas claves son mayores o iguales a la pasada
     * por parámetro.
     * 
     * @param fromKey
     * @return NavigableMap<K, V> -> mapa navegable
     */
    @Override
    public NavigableMap<K, V> tailMap(K fromKey) {
        return tailMap(fromKey, true);        
    }
    /**
     * Función interna que devuelve un subárbol cuyas claves son mayores 
     * o iguales a la pasada por parámetro.
     * 
     * @param fromKey
     * @param inclusive
     * @return NavigableMap<K, V> -> mapa navegable
     */
    @Override
    public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
        return new AscendingSubMap(this,false,fromKey,inclusive,true,null,true);
    }    
    /*------------------------------------------------------------------*/
    /**
     * EntrySet es una clase interna que utilizamos para las iteraciones
     * (recorridos que hacemos con foreach) de las entradas
     */
    private class EntrySet extends AbstractSet<Map.Entry<K,V>> {
        /**
         * Personaliza el recorrido de las entradas
         * 
         * @return Map.Entry<K, V> -> recorrido de entradas
         */
        @Override
        public Iterator<Map.Entry<K,V>> iterator() {
            return new EntryIterator(firstEntry());
        }
        /**
         * Misma idea de la función size de nuestro árbol
         * 
         * @return int -> entero 
         */
        @Override
        public int size() {
            return size;
        }
    }
    /**
     * Clase interna para dar estilo al recorrido de las entradas
     */
    private class EntryIterator extends PrivateEntryIterator<Map.Entry<K,V>> {
        /**
         * Construye una instancia del iterador de entradas pasándole
         * una entrada (nodo) como parámetro (el primero del árbol) 
         * 
         * @param first 
         */
        EntryIterator(Entry<K,V> first) {
            super(first);
        }
        /**
         * Devuelve la siguiente entrada del recorrido
         * 
         * @return V -> valor 
         */
        @Override
        public Entry<K,V> next() {
            return nextEntry();
        }
    }
    /*------------------------------------------------------------*/
    /**
     * Values es una clase interna que utilizamos para las iteraciones
     * (recorridos que hacemos con foreach) de los valores
     */
    private class Values extends AbstractCollection<V> {
        /**
         * Personaliza el recorrido de los valores
         * 
         * @return Iterator<V> -> recorrido de valores
         */
        @Override
        public Iterator<V> iterator() {
            return new ValueIterator(firstEntry());
        }
        /**
         * Misma idea de la función size de nuestro árbol
         * 
         * @return int -> entero 
         */
        @Override
        public int size() {
            return size;
        }        
    }
    /**
     * Clase interna para dar estilo al recorrido de los valores
     */
    private class ValueIterator extends PrivateEntryIterator<V> {
        /**
         * Construye una instancia del iterador de valores pasándole
         * una entrada (nodo) como parámetro (el primero del árbol) 
         * 
         * @param first 
         */
        ValueIterator(Entry<K,V> first) {
            super(first);
        }
        /**
         * Devuelve el siguiente valor del recorrido
         * 
         * @return V -> valor 
         */
        @Override
        public V next() {
            return nextEntry().getValue();
        }
    }
    /**
     * KeySet es una clase interna que utilizamos para las iteraciones
     * (recorridos que hacemos con foreach) de las claves
    */
    private class KeySet extends AbstractSet<K> implements NavigableSet<K> {
        // variable interna para delegar la mayoría de funcionalidades del map
        private NavigableMap<K, ?> m;
        /**
         * Construye una nueva instancia del iterador de claves a partir
         * del mapa que le pasamos por parámetro
         * 
         * @param map 
         */
        KeySet(NavigableMap<K,?> map) { 
            m = map; 
        }
        /**
         * Personaliza el recorrido de las claves
         * 
         * @return Iterator<K> -> recorrido de claves
         */
        @Override
        public Iterator<K> iterator() {
            if (m instanceof MyTreeMap)
                return ((MyTreeMap<K,?>)m).keyIterator();
            else
                return ((MyTreeMap.NavigableSubMap<K,?>)m).keyIterator();
        }
        /**
         * Misma idea de la función size de nuestro árbol
         * 
         * @return int -> entero 
         */
        @Override
        public int size() {
            return size;
        }
        /**
         * Obtiene el comparador usado para las ordenaciones. (Delega del 
         * map interno)
         * 
         * @return Comparator<? super K> -> comparador basado en claves 
        */        
        @Override
        public Comparator<? super K> comparator() {
            return m.comparator();
        }
        /**
         * Devuelve un elemento, el mayor estrictamente menor que el pasado 
         * por parámetro. (delega del map interno)
         * 
         * @param e
         * @return K -> clave 
         */
        @Override
        public K lower(K e) { return m.lowerKey(e); }
        /**
         * Devuelve un elemento, el de la clave mayor menor o igual que 
         * la clave dada. (delega del map interno) 
         * 
         * @param key
         * @return K -> clave
        */
        @Override
        public K floor(K e) { return m.floorKey(e); }
        /**
         * Devuelve un elemento, el de menor clave mayor o igual que el 
         * parámetro pasado. (Delega del map interno)
         * 
         * @param key
         * @return K -> clave
        */
        @Override
        public K ceiling(K e) { return m.ceilingKey(e); }
        /**
         * Devuelve un elemento, el menos estrictamente mayor que el 
         * parámetro pasado. (Delega del map interno)
         * 
         * @param key
         * @return K -> entrada (nodo).
        */
        @Override
        public K higher(K e) { return m.higherKey(e); }
        /**
         * Devuelve el primer elemento del árbol hash. (Delega del map interno)
         * 
         * @return K -> clave 
        */
        @Override
        public K first() { return m.firstKey(); }
        /**
         * Devuelve el último elemento del árbol hash. (Delega del map interno)
         * 
         * @return K -> clave 
        */
        @Override
        public K last() { return m.lastKey(); }
        /**
         * Elimina y devuelve el primer elemento del árbol hash. (Delega del
         * map interno)
         * 
         * @return K -> clave
        */
        @Override
        public K pollFirst() {
            Map.Entry<K,?> e = m.pollFirstEntry();
            return (e == null) ? null : e.getKey();
        }
        /**
         * Elimina y devuelve el último elemento del árbol hash. (Delega del
         * map interno)
         * 
         * @return K -> clave
        */
        @Override
        public K pollLast() {
            Map.Entry<K,?> e = m.pollLastEntry();
            return (e == null) ? null : e.getKey();
        }
        /**
         * Devuelve un conjunto de todos los elementos de nuestro árbol hash 
         * de forma decreciente. (Delega del map interno).
         * 
         * @return Iterator<K> -> conjunto de claves
        */
        @Override
        public NavigableSet<K> descendingSet() {
            return new KeySet(m.descendingMap());
        }
        /**
         * Devuelve un conjunto de todas los elementos del árbol hash 
         * de forma decreciente. (Delega del map interno)
         * 
         * @return Iterator<K> -> conjunto de claves
        */
        @Override
        public Iterator<K> descendingIterator() {
            if (m instanceof MyTreeMap)
                return ((MyTreeMap<K,?>)m).descendingKeyIterator();
            else
                return ((MyTreeMap.NavigableSubMap<K,?>)m).descendingKeyIterator();
        }
        /**
         * Devuelve un subárbol cuyos elementos van desde un período de inicio 
         * a fin. (Delega del map interno)
         * 
         * @param fromElement
         * @param toElement
         * @return NavigableSet<K> -> tupla navegable
         */
        @Override
        public NavigableSet<K> subSet(K fromElement, K toElement) {
            return subSet(fromElement, true, toElement, false);
        }
        /**
         * Función interna que devuelve un subárbol cuyos elementos van desde 
         * un período de inicio a fin. (Delega del map interno)
         * 
         * @param fromElement
         * @param fromInclusive
         * @param toElement
         * @param toInclusive
         * @return NavigableSet<K> -> tupla navegable
         */
        @Override
        public NavigableSet<K> subSet(K fromElement, boolean fromInclusive,
                                      K toElement,   boolean toInclusive) {
            return new KeySet(m.subMap(fromElement, fromInclusive,
                                          toElement,   toInclusive));
        }
        /**
         * Devuelve un subárbol cuyos elementos son estrictamente menores que 
         * el que fue pasado por parámetro. (Delega del map interno)
         * 
         * @param toElement
         * @return NavigableSet<K> -> tupla navegable
        */
        @Override
        public NavigableSet<K> headSet(K toElement) {
            return headSet(toElement, false);
        }
        /**
         * Función interna que eevuelve un subárbol cuyas elementos son 
         * estrictamente menores que el que fue pasado por parámetro. 
         * (Delega del map interno)
         * 
         * @param toElement
         * @param inclusive
         * @return NavigableSet<K> -> tupla navegable
         */
        @Override
        public NavigableSet<K> headSet(K toElement, boolean inclusive) {
            return new KeySet(m.headMap(toElement, inclusive));
        }
        /**
         * Devuelve un subárbol cuyos elementos son mayores o iguales al pasado
         * por parámetro. (delega del map interno)
         * 
         * @param fromElement
         * @return NavigableSet<K> -> tupla navegable
         */
        @Override
        public NavigableSet<K> tailSet(K fromElement) {
            return tailSet(fromElement, true);
        }
        /**
         * Función interna que devuelve un subárbol cuyos elementos son mayores 
         * o iguales al pasado por parámetro. (delega del map interno)
         * 
         * @param fromElement
         * @param inclusive
         * @return NavigableSet<K> -> tupla navegable
         */
        @Override
        public NavigableSet<K> tailSet(K fromElement, boolean inclusive) {
            return new KeySet(m.tailMap(fromElement, inclusive));
        }                               
    }
    /**
     * Clase interna para dar estilo al recorrido de las claves
     */
    private class KeyIterator extends PrivateEntryIterator<K> {
        /**
         * Construye una instancia del iterador de claves pasándole
         * una entrada (nodo) como parámetro (el primero del árbol) 
         * 
         * @param first 
         */
        KeyIterator(Entry<K,V> first) {
            super(first);
        }
        /**
         * Obtiene la siguiente clave del recorrido
         * 
         * @return K -> clave
         */
        @Override
        public K next() {
            return nextEntry().getKey();
        }
    }
    /*------------------------------------------------------------*/
    /**
     * Clase abstracta que usamos para los distintos tipos de 
     * recorridos empleados en nuestra estructura de datos
     * 
     * @param <E> 
     */
    abstract class PrivateEntryIterator<T> implements Iterator<T> {
        Entry<K,V> next;
        Entry<K,V> last;
        /**
         * Construye una nueva iteración de entradas
         */
        PrivateEntryIterator(Entry<K,V> first) {
            last = null;
            next = first;
        }
        /**
         * Verifica si hay una siguiente entrada
         * 
         * @return boolean -> verdadero o falso 
         */
        @Override
        public final boolean hasNext() {
            return next != null;
        }
        /**
         * Obtiene la entrada próxima, y también es una función 
         * sobreexplotada para los recorridos ;)
         * 
         * @return Entry<K,V> -> entrada (nodo)
         */
        public Entry<K,V> nextEntry() {
            Entry<K,V> e = next;
            next = higherEntry(e.getKey());
            last = e;
            return e;
        }
        /**
         * Obtiene la entrada anterior, y usado por las iteraciones de modo
         * decreciente
         * 
         * @return Entry<K,V> -> entrada (nodo)
         */       
        public Entry<K,V> prevEntry() {
            Entry<K,V> e = next;
            next = lowerEntry(e.getKey());
            last = e;
            return e;
        }
    }
    /*------------------------------------------------------------*/    
    /**
     * Clase abstracta interna que usamos para los subárboles en las
     * funciones headMap, tailMap y subMap
     * 
     * 
     * @param <K>
     * @param <V> 
     */
    abstract static class NavigableSubMap<K,V> extends AbstractMap<K,V> implements NavigableMap<K,V>{
        final MyTreeMap<K,V> m;
        final K lo, hi;
        final boolean fromStart, toEnd;
        final boolean loInclusive, hiInclusive;
        /**
         * Construye una nueva instancia de NavigableSubMap dependiendo
         * de cómo hayan sido pasados los parámetros.
         * 
         * @param xm
         * @param fromStart
         * @param lo
         * @param loInclusive
         * @param toEnd
         * @param hi
         * @param hiInclusive 
         */
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
        //funciones internas        
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
        /*
         * Versiones absolutas de operaciones de relación. Las subclases se 
         * asignan a estas usando "sub" con el mismo nombre, versiones que 
         * invierten sentidos para mapas descendentes.
        */
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
        protected MyTreeMap.Entry<K,V> absHighest() {
            MyTreeMap.Entry<K,V> e = (toEnd ?  m.lastEntry() :
                (hiInclusive ?  m.floorEntry(hi) : m.lowerEntry(hi)));
            return (e == null || tooLow(e.key)) ? null : e;
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
        /**
         * Devuelva la cerca baja absoluta para el recorrido descendente
         * 
         * @return Entry<K,V> -> entrada (nodo)
         */
        protected MyTreeMap.Entry<K,V> absLowFence() {
            return (fromStart ? null : (loInclusive ?
                m.lowerEntry(lo) : m.floorEntry(lo)));
        }
        /**
         * Devuelve la cerca alta absoluta para el recorrido ascendente.
         * 
         * @return Entry<K,V> -> entrada (nodo)
         */
        protected MyTreeMap.Entry<K,V> absHighFence() {
            if(toEnd){
                return null;
            } else if(hiInclusive){
                return m.higherEntry(hi);
            } else {
                return m.ceilingEntry(hi != null ? hi : (!m.isEmpty() ? lastKey() : null));
            }
        }
        @Override
        public Comparator<? super K> comparator() {
            return m.comparator;
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
        /**
         * Devuelve el iterador ascendente desde la perspectiva de este submapa.
         * 
         * @return Iterator<K> -> Conjunto de claves
         */
        abstract Iterator<K> keyIterator();
        /**
         * Devuelve el iterador descendente desde la perspectiva de este submapa.
         * 
         * @return Iterator<K> -> Conjunto de claves 
         */
        abstract Iterator<K> descendingKeyIterator();
        /*
         * Métodos abstractos definidos en clases ascendentes vs descendentes.
         * Estos retransmiten a las versiones absolutas apropiadas.
        */
        abstract MyTreeMap.Entry<K,V> subLowest();
        abstract MyTreeMap.Entry<K,V> subHighest();
        abstract MyTreeMap.Entry<K,V> subCeiling(K key);
        abstract MyTreeMap.Entry<K,V> subHigher(K key);
        abstract MyTreeMap.Entry<K,V> subFloor(K key);
        abstract MyTreeMap.Entry<K,V> subLower(K key);
        /**
         * Devuelve una vista de entradas del subárbol
         * 
         * @return Set<Map.Entry<K, V>> -> conjunto de entradas
         */
        @Override
        public Set<Map.Entry<K, V>> entrySet() {
            return new EntrySetView();
        }
        /**
         * Clase interna para el recorrido de las entradas del subárbol
         */
        class EntrySetView extends AbstractSet<Map.Entry<K,V>> {
            /**
             * Devuelve un iterador para el recorrido del subárbol
             * 
             * @return Iterator<Map.Entry<K,V>> -> iteración de entradas
             */
            @Override
            public Iterator<Map.Entry<K,V>> iterator() {
                return new SubMapEntryIterator(absLowest(), absHighFence());                
            }
            /**
             * Devuelve la cantidad de elementos del subárbol
             * 
             * @return int -> entero 
             */
            @Override
            public int size() {
                int s = 0;
                for(Entry<K,V> e : entrySet()){
                    s++;
                }
                return s;
            }        
        }
        /**
         * Clase abstracta utilizada para los personalizar los recorridos del
         * subárbol
         * 
         * @param <T> 
         */
        abstract class SubMapIterator<T> implements Iterator<T> {
            MyTreeMap.Entry<K,V> last;
            MyTreeMap.Entry<K,V> next;
            final Object fenceKey;
            /**
             * Construye una nueva instancia del iterador de submapas mediante
             * parámetros de inicio a fin
             * 
             * @param first
             * @param fence 
             */
            SubMapIterator(MyTreeMap.Entry<K,V> first, MyTreeMap.Entry<K,V> fence) {
                last = null;
                next = first;
                fenceKey = fence == null ? new Object() : fence.getKey();
            }
            /**
             * Verifica si hay una siguiente entrada en el recorrido
             * 
             * @return boolean -> verdadero o falso 
             */
            @Override
            public boolean hasNext() {
                return next != null && next.getKey() != fenceKey;
            }
            /**
             * Obtiene la siguiente entrada del subárbol
             * 
             * @return Entry<K,V> -> entrada
             */
            public MyTreeMap.Entry<K,V> nextEntry() {
                MyTreeMap.Entry<K,V> e = next;
                next = m.higherEntry(e.getKey());
                last = e;
                return e;
            }
            /**
             * Obtiene la entrada anterior del subárbol
             * 
             * @return Entry<K,V> -> entrada
             */
            public MyTreeMap.Entry<K,V> prevEntry() {
                MyTreeMap.Entry<K,V> e = next;
                next = m.lowerEntry(e.getKey());
                last = e;
                return e;
            }
        }
        /**
         * Clase interna para dar estilo al recorrido de las entradas del
         * subárbol.
        */
        class SubMapEntryIterator extends SubMapIterator<Map.Entry<K,V>> {
            /**
             * Construye una nueva instancia del iterador de entradas mediante
             * un inicio y un fin como parámetros
             * 
             * @param first
             * @param fence 
             */
            public SubMapEntryIterator(MyTreeMap.Entry<K, V> first, MyTreeMap.Entry<K, V> fence) {
                super(first, fence);
            }
            /**
             * Devuelve la siguiente entrada del recorrido
             * 
             * @return Map.Entry<K, V> -> entrada (nodo)
             */
            @Override
            public Map.Entry<K, V> next() {
                return nextEntry();
            }        
        }
        /**
         * Clase interna para dar estilo al recorrido de las claves del
         * subárbol.
        */
        class SubMapKeyIterator extends SubMapIterator<K> {
            /**
             * Construye una nueva instancia del iterador de claves mediante
             * un inicio y un fin como parámetros
             * 
             * @param first
             * @param fence 
             */
            public SubMapKeyIterator(MyTreeMap.Entry<K, V> first, MyTreeMap.Entry<K, V> fence) {
                super(first, fence);
            }
            /**
             * Devuelve la siguiente clave del recorrido
             * 
             * @return K -> clave 
             */
            @Override
            public K next() {
                return nextEntry().getKey();
            }
        }
        /**
         * Clase interna para dar estilo al recorrido de las claves del
         * subárbol en forma decreciente.
        */
        class DescendingSubMapKeyIterator extends SubMapIterator<K> {
            /**
             * Construye una nueva instancia del iterador de claves mediante
             * un inicio y un fin como parámetros
             * 
             * @param first
             * @param fence 
             */
            public DescendingSubMapKeyIterator(MyTreeMap.Entry<K, V> first, MyTreeMap.Entry<K, V> fence) {
                super(first, fence);
            }
            /**
             * Devuelve la siguiente clave del decreciente recorrido 
             * 
             * @return K -> clave 
             */
            @Override
            public K next() {
                return prevEntry().getKey();
            }            
        }
        /**
         * Clase interna para dar estilo al recorrido de las entradas del
         * subárbol en forma decreciente.
        */
        class DescendingSubMapEntryIterator extends SubMapIterator<Map.Entry<K,V>> {
            /**
             * Construye una nueva instancia del iterador de claves mediante
             * un inicio y un fin como parámetros
             * 
             * @param first
             * @param fence 
             */
            public DescendingSubMapEntryIterator(MyTreeMap.Entry<K, V> first, MyTreeMap.Entry<K, V> fence) {
                super(first, fence);
            }
            /**
             * Devuelve la siguiente entrada del decreciente recorrido 
             * 
             * @return K -> clave 
             */
            @Override
            public Entry<K, V> next() {
                return prevEntry();
            }            
        }
    }
    /**
     * Clase definida para tener el subárbol de forma creciente
     * 
     * @param <K>
     * @param <V> 
     */
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
    /**
     * Clase definida para tener el subárbol de forma decreciente
     * 
     * @param <K>
     * @param <V> 
     */
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
        /**
         * Clase interna para el recorrido de las entradas del subárbol
         */
        class DescendingEntrySetView extends EntrySetView {
            @Override
            public Iterator<Map.Entry<K,V>> iterator() {
                return new DescendingSubMapEntryIterator(absHighest(), absLowFence());
            }
        }
    }
    /**
     * Clase interna para obtener el recorrido de las claves de forma
     * decreciente
     */     
    class DescendingKeyIterator extends PrivateEntryIterator<K> {
        /**
         * Construye el iterador de claves decreciente a apartir de una
         * entrada (nodo) pasado por parámetro (el último del árbol)
         * 
         * @param first 
         */
        DescendingKeyIterator(Entry<K,V> first) {
            super(first);
        }
        /**
         * Obtiene la siguiente clave del decreciente recorrido
         * 
         * @return K -> clave
         */
        @Override
        public K next() {
            return prevEntry().getKey();
        }
    }
    /*------------------------------------------------------------*/
    /**
     * Clase interna para definir las entradas clave/valor que 
     * almacenaremos en nuestra estructura de datos
     * 
     * @param <K>
     * @param <V> 
     */
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