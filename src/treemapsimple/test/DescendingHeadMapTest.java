package treemapsimple.test;
import treemapsimple.structs.MyTreeMap;
public class DescendingHeadMapTest extends Test
{
    MyTreeMap<Integer,String>map;
    public DescendingHeadMapTest() {
        map = new MyTreeMap();
        this.cargando();
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
    private void probando_cabeza_normal() throws Exception {
        MyTreeMap.DescendingSubMap<Integer,String> aux = (MyTreeMap.DescendingSubMap<Integer,String>) map.descendingMap().headMap(3);
        this.comprobar_que(aux.size() == 3);
        this.comprobar_que(aux.firstKey() != null);
        this.comprobar_que(aux.lastKey() != null);
    }
    private void probando_cabeza_nula() throws Exception {
        MyTreeMap.DescendingSubMap<Integer,String> aux = (MyTreeMap.DescendingSubMap<Integer,String>) map.descendingMap().headMap(null);
        this.comprobar_que(aux.isEmpty());
        this.comprobar_que(aux.firstKey() == null);
        this.comprobar_que(aux.lastKey() == null);
    }
    private void probando_cabeza_con_clave_inexistente() throws Exception {
        MyTreeMap.DescendingSubMap<Integer,String> aux = (MyTreeMap.DescendingSubMap<Integer,String>) map.descendingMap().headMap(12);
        this.comprobar_que(aux.isEmpty());
        this.comprobar_que(aux.firstKey() == null);
        this.comprobar_que(aux.lastKey() == null);
    }
    private void probando_cabeza_con_clave_grande() throws Exception {
        MyTreeMap.DescendingSubMap<Integer,String> aux = (MyTreeMap.DescendingSubMap<Integer,String>) map.descendingMap().headMap(2000);
        this.comprobar_que(aux.isEmpty());
        this.comprobar_que(aux.firstKey() == null);
        this.comprobar_que(aux.lastKey() == null);
    }
    //</editor-fold>
    @Override
    public void test() {
        try {
            probando_cabeza_normal();
            probando_cabeza_nula();
            probando_cabeza_con_clave_inexistente();
            probando_cabeza_con_clave_grande();
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }    
}