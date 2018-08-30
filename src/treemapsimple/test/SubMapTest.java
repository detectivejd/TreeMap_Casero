package treemapsimple.test;
import treemapsimple.structs.MyTreeMap;
public class SubMapTest extends Test
{
    MyTreeMap<Integer,String>map;
    public SubMapTest() {
        map = new MyTreeMap();        
    }
    //<editor-fold desc="relleno de datos">
    private void cargando(){
        map.put(1, "Deborah");
        map.put(2, "Tommy");
        map.put(3, "Franco");
        map.put(4, "Manuela");
        map.put(5, "Miguel");
        map.put(6, "Denisse");
    }
    //</editor-fold>
    //<editor-fold desc="pruebas">
    private void probando_submapa_normal() throws Exception {
        this.cargando();
        MyTreeMap.NavigableSubMap<Integer,String> aux = (MyTreeMap.NavigableSubMap<Integer,String>) map.subMap(2,6);
        this.comprobar_que(aux.size() == 4);
        this.comprobar_que(aux.firstKey() != null);
        this.comprobar_que(aux.lastKey() != null);
    }
    private void probando_submapa_nulo() throws Exception {
        this.cargando();
        MyTreeMap.NavigableSubMap<Integer,String> aux = (MyTreeMap.NavigableSubMap<Integer,String>) map.subMap(null,null);
        this.comprobar_que(aux.isEmpty());
        this.comprobar_que(aux.firstKey() == null);
        this.comprobar_que(aux.lastKey() == null);
    }
    private void probando_submapa_derecha_nulo() throws Exception {
        this.cargando();
        MyTreeMap.NavigableSubMap<Integer,String> aux = (MyTreeMap.NavigableSubMap<Integer,String>) map.subMap(1,null);
        this.comprobar_que(aux.size() == 5);
        this.comprobar_que(aux.firstKey() != null);
        this.comprobar_que(aux.lastKey() != null);
    }
    private void probando_submapa_izquierda_nulo() throws Exception {
        this.cargando();
        MyTreeMap.NavigableSubMap<Integer,String> aux = (MyTreeMap.NavigableSubMap<Integer,String>) map.subMap(null,3);
        this.comprobar_que(aux.size() == 2);
        this.comprobar_que(aux.firstKey() != null);
        this.comprobar_que(aux.lastKey() != null);
    }
    private void probando_submapa_claves_inexistente(int ini,int fin, int largo) throws Exception {
        this.cargando();
        MyTreeMap.NavigableSubMap<Integer,String> aux = (MyTreeMap.NavigableSubMap<Integer,String>) map.subMap(ini,fin);
        this.comprobar_que(aux.size() == largo);
        this.comprobar_que(aux.firstKey() != null);
        this.comprobar_que(aux.lastKey() != null);
    }
    //</editor-fold>
    @Override
    public void test() {
        try {
            probando_submapa_normal();
            probando_submapa_nulo();
            probando_submapa_derecha_nulo();
            probando_submapa_izquierda_nulo();
            probando_submapa_claves_inexistente(-2,5,4);
            probando_submapa_claves_inexistente(-2,5,4);
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
}
