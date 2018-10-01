package treemapsimple.test;
import treemapsimple.structs.MyTreeMap;
public class AscendingSubMapTest extends Test
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
    private void probando_submapa_normal() throws Exception {
        MyTreeMap<Integer,String> map = cargando();
        MyTreeMap.AscendingSubMap<Integer,String> aux = (MyTreeMap.AscendingSubMap<Integer,String>) map.subMap(2,6);
        this.comprobar_que(aux.size() == 4);
        this.comprobar_que(aux.firstKey() != null);
        this.comprobar_que(aux.lastKey() != null);
    }
    private void probando_submapa_nulo() throws Exception {
        MyTreeMap<Integer,String> map = cargando();
        MyTreeMap.AscendingSubMap<Integer,String> aux = (MyTreeMap.AscendingSubMap<Integer,String>) map.subMap(null,null);
        this.comprobar_que(aux.isEmpty());
        this.comprobar_que(aux.firstKey() == null);
        this.comprobar_que(aux.lastKey() == null);
    }
    private void probando_submapa_derecha_nulo() throws Exception {
        MyTreeMap<Integer,String> map = cargando();
        MyTreeMap.AscendingSubMap<Integer,String> aux = (MyTreeMap.AscendingSubMap<Integer,String>) map.subMap(1,null);
        this.comprobar_que(aux.size() == 5);
        this.comprobar_que(aux.firstKey() != null);
        this.comprobar_que(aux.lastKey() != null);
    }
    private void probando_submapa_izquierda_nulo() throws Exception {
        MyTreeMap<Integer,String> map = cargando();
        MyTreeMap.AscendingSubMap<Integer,String> aux = (MyTreeMap.AscendingSubMap<Integer,String>) map.subMap(null,3);
        this.comprobar_que(aux.size() == 2);
        this.comprobar_que(aux.firstKey() != null);
        this.comprobar_que(aux.lastKey() != null);
    }
    private void probando_submapa_claves(int ini,int fin) throws Exception {
        MyTreeMap<Integer,String> map = cargando();
        MyTreeMap.AscendingSubMap<Integer,String> aux = (MyTreeMap.AscendingSubMap<Integer,String>) map.subMap(ini,fin);
        if(!aux.isEmpty()){
            this.comprobar_que(aux.firstKey() != null);
            this.comprobar_que(aux.lastKey() != null);
        } else {
            this.comprobar_que(aux.firstKey() == null);
            this.comprobar_que(aux.lastKey() == null);
        }
    }
    //</editor-fold>
    @Override
    public void test() {
        try {
            probando_submapa_normal();
            probando_submapa_nulo();
            probando_submapa_derecha_nulo();
            probando_submapa_izquierda_nulo();
            probando_submapa_claves(-2,7);
            probando_submapa_claves(4,17);
            probando_submapa_claves(-1,17);
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }    
}