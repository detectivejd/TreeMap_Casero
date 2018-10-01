package treemapsimple.test;
import treemapsimple.structs.MyTreeMap;
public class AscendingTailMapTest extends Test
{
    //<editor-fold desc="relleno de datos">
    private MyTreeMap<Integer,String>cargando(){
        MyTreeMap<Integer,String> map = new MyTreeMap();
        map.put(1, "Deborah");
        map.put(2, "Tommy");
        map.put(3, "Franco");
        map.put(4, "Manuela");
        map.put(5, "Miguel");
        map.put(6, "Denisse");
        return map;
    }
    //</editor-fold>
    //<editor-fold desc="pruebas">
    private void probando_cola_normal() throws Exception {
        MyTreeMap<Integer,String> map = cargando();
        MyTreeMap.AscendingSubMap<Integer,String> aux = (MyTreeMap.AscendingSubMap<Integer,String>) map.tailMap(3);
        this.comprobar_que(aux.size() == 4);
        this.comprobar_que(aux.firstKey() != null);
        this.comprobar_que(aux.lastKey() != null);
    }
    private void probando_cola_nula() throws Exception {
        MyTreeMap<Integer,String> map = cargando();
        MyTreeMap.AscendingSubMap<Integer,String> aux = (MyTreeMap.AscendingSubMap<Integer,String>) map.tailMap(null);
        this.comprobar_que(aux.isEmpty());
        this.comprobar_que(aux.firstKey() == null);
        this.comprobar_que(aux.lastKey() == null);
    }
    private void probando_cola_con_clave_inexistente() throws Exception {
        MyTreeMap<Integer,String> map = cargando();
        MyTreeMap.AscendingSubMap<Integer,String> aux = (MyTreeMap.AscendingSubMap<Integer,String>) map.tailMap(12);
        this.comprobar_que(aux.isEmpty());
        this.comprobar_que(aux.firstKey() == null);
        this.comprobar_que(aux.lastKey() == null);
    }
    private void probando_cola_con_clave_grande() throws Exception {
        MyTreeMap<Integer,String> map = cargando();
        MyTreeMap.AscendingSubMap<Integer,String> aux = (MyTreeMap.AscendingSubMap<Integer,String>) map.tailMap(2000);
        this.comprobar_que(aux.isEmpty());
        this.comprobar_que(aux.firstKey() == null);
        this.comprobar_que(aux.lastKey() == null);
    }
    //</editor-fold>
    @Override
    public void test() {
        try {
            probando_cola_normal();
            probando_cola_nula();
            probando_cola_con_clave_inexistente();
            probando_cola_con_clave_grande();
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }    
}