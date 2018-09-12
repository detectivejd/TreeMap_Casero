package treemapsimple.test;
import treemapsimple.structs.MyTreeMap;
public class EntryTest extends Test
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
    private void probando_first_last_normal() throws Exception {
        MyTreeMap<Integer,String> map = cargando();
        comprobar_que(map.firstEntry() != null && map.firstKey() == 1);
        comprobar_que(map.lastEntry() != null && map.lastKey() == 6);
    }
    private void probando_first_last_poll() throws Exception {
        MyTreeMap<Integer,String> map = cargando();        
        comprobar_que(map.firstEntry() == map.pollFirstEntry());
        comprobar_que(map.lastEntry() == map.pollLastEntry());
        comprobar_que(map.firstEntry() != null && map.firstKey() != 1);
        comprobar_que(map.lastEntry() != null && map.lastKey() != 6);
        comprobar_que(map.size() == 4);
    }
    private void probando_ceiling_higher_normal() throws Exception {
        MyTreeMap<Integer,String> map = cargando();
        comprobar_que(map.ceilingEntry(3) != null && map.ceilingKey(3) == 3);
        comprobar_que(map.higherEntry(3) != null && map.higherKey(3) == 4);
    }
    private void probando_ceiling_higher_nulo() throws Exception {
        MyTreeMap<Integer,String> map = cargando();
        comprobar_que(map.ceilingEntry(null) == null && map.ceilingKey(null) == null);
        comprobar_que(map.higherEntry(null) == null && map.higherKey(null) == null);
    }
    private void probando_ceiling_higher_poll() throws Exception {
        MyTreeMap<Integer,String> map = cargando();
        comprobar_que(map.ceilingEntry(1) != null && map.ceilingKey(1) == 1);
        comprobar_que(map.higherEntry(1) != null && map.higherKey(1) == 2);
        comprobar_que(map.ceilingEntry(5) != null && map.ceilingKey(5) == 5);
        comprobar_que(map.higherEntry(5) != null && map.higherKey(5) == 6);
        map.pollFirstEntry();
        map.pollLastEntry();
        comprobar_que(map.ceilingEntry(1) != null && map.ceilingKey(1) == 2);
        comprobar_que(map.higherEntry(1) != null && map.higherKey(1) == 2);
        comprobar_que(map.ceilingEntry(5) != null && map.ceilingKey(5) == 5);
        comprobar_que(map.higherEntry(5) == null && map.higherKey(5) == null);
        comprobar_que(map.size() == 4);
    }
    private void probando_floor_lower_normal() throws Exception {
        MyTreeMap<Integer,String> map = cargando();
        comprobar_que(map.floorEntry(3) != null && map.floorKey(3) == 3);
        comprobar_que(map.lowerEntry(3) != null && map.lowerKey(3) == 2);
    }
    private void probando_floor_lower_nulo() throws Exception {
        MyTreeMap<Integer,String> map = cargando();
        comprobar_que(map.floorEntry(null) == null && map.floorKey(null) == null);
        comprobar_que(map.lowerEntry(null) == null && map.lowerKey(null) == null);
    }
    private void probando_floor_lower_poll() throws Exception {
        MyTreeMap<Integer,String> map = cargando();
        comprobar_que(map.floorEntry(3) != null && map.floorKey(3) == 3);
        comprobar_que(map.lowerEntry(3) != null && map.lowerKey(3) == 2);
        map.pollFirstEntry();
        map.pollLastEntry();
        comprobar_que(map.floorEntry(1) == null && map.floorKey(1) == null);
        comprobar_que(map.lowerEntry(1) == null && map.lowerKey(1) == null);
        comprobar_que(map.floorEntry(5) != null && map.floorKey(5) == 5);
        comprobar_que(map.lowerEntry(5) != null && map.lowerKey(5) == 4);
        comprobar_que(map.size() == 4);
    }
    //</editor-fold>
    @Override
    public void test() {
        try{
            probando_first_last_normal();
            probando_first_last_poll();
            probando_ceiling_higher_normal();
            probando_ceiling_higher_nulo();
            probando_ceiling_higher_poll();
            probando_floor_lower_normal();
            probando_floor_lower_nulo();
            probando_floor_lower_poll();
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }    
}