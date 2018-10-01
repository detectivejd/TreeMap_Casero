package treemapsimple.test;
import treemapsimple.structs.MyTreeMap;
public class DescendingSubMapTest extends Test
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
        MyTreeMap.DescendingSubMap<Integer,String> aux = (MyTreeMap.DescendingSubMap<Integer,String>) map.descendingMap().subMap(6,2);
        this.comprobar_que(aux.size() == 4);
        this.comprobar_que(aux.firstKey() != null);
        this.comprobar_que(aux.lastKey() != null);
    }
    private void probando_submapa_nulo() throws Exception {
        MyTreeMap<Integer,String> map = cargando();
        MyTreeMap.DescendingSubMap<Integer,String> aux = (MyTreeMap.DescendingSubMap<Integer,String>) map.descendingMap().subMap(null,null);
        this.comprobar_que(aux.isEmpty());
        this.comprobar_que(aux.firstKey() == null);
        this.comprobar_que(aux.lastKey() == null);
    }
    private void probando_submapa_derecha_nulo() throws Exception {
        MyTreeMap<Integer,String> map = cargando();
        MyTreeMap.DescendingSubMap<Integer,String> aux = (MyTreeMap.DescendingSubMap<Integer,String>) map.descendingMap().subMap(1,null);
        this.comprobar_que(aux.isEmpty());
        this.comprobar_que(aux.firstKey() == null);
        this.comprobar_que(aux.lastKey() == null);
    }
    private void probando_submapa_izquierda_nulo() throws Exception {
        MyTreeMap<Integer,String> map = cargando();
        MyTreeMap.DescendingSubMap<Integer,String> aux = (MyTreeMap.DescendingSubMap<Integer,String>) map.descendingMap().subMap(null,3);
        this.comprobar_que(aux.size() == 3);
        this.comprobar_que(aux.firstKey() != null);
        this.comprobar_que(aux.lastKey() != null);
    }
    private void probando_submapa_claves(int ini,int fin) throws Exception {
        MyTreeMap<Integer,String> map = cargando();
        MyTreeMap.DescendingSubMap<Integer,String> aux = (MyTreeMap.DescendingSubMap<Integer,String>) map.descendingMap().subMap(ini,fin);
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
            probando_submapa_claves(7,-2);
            probando_submapa_claves(17,4);
            probando_submapa_claves(4,-1);
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }
}