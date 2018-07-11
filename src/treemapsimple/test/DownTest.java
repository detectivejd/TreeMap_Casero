package treemapsimple.test;
import treemapsimple.structs.MyTreeMap;
public class DownTest extends Test
{
    MyTreeMap<Integer,String>map; 
    public DownTest() {
        map = new MyTreeMap();
    }
    //<editor-fold desc="relleno de datos">
    private Object[][] restart1(){
        return new Object[][] {
            new Object[]{1,"Deborah"},
            new Object[]{2,"Tommy"},
            new Object[]{3,"Franco"},
            new Object[]{4,"Manuela"},
            new Object[]{5,"Miguel"},
            new Object[]{6,"Denisse"}
        };        
    }        
    private Object[][] elem_1(){
        return new Object[][]{ new Object[]{1,"Deborah"} };        
    }
    private Object[][] elem_2(){
        return new Object[][]{
            new Object[]{1,"Deborah"},
            new Object[]{2,"Franco"}
        };
    }
    private Object[][] elem_3(){
        return new Object[][]{
            new Object[]{1,"Deborah"},
            new Object[]{2,"Franco"},
            new Object[]{3,"Tommy"} 
        };
    }
    //</editor-fold>
    //<editor-fold desc="pruebas">
    private void limpieza_total() throws Exception{
        map.clear();
        this.comprobar_que(map.isEmpty());
    }
    private void probando_borrado(Object[][]arreglo, Object[]criterio) throws Exception{
        map.clear();
        for (Object[] arreglo1 : arreglo) {
            map.put(Integer.valueOf(arreglo1[0].toString()), arreglo1[1].toString());
        }
        for(Object c : criterio){
            if(c == null){
                map.remove(null);
                this.comprobar_que(map.get(null) == null);
            } else {
                map.remove(Integer.valueOf(c.toString()));
                this.comprobar_que(map.get(Integer.valueOf(c.toString())) == null);
            }                        
        }
    }
    //</editor-fold>
    @Override
    public void test() {
        try { 
            probando_borrado(restart1(), new Object[]{1,3,6});
            probando_borrado(restart1(), new Object[]{null});
            probando_borrado(restart1(), new Object[]{7,9});
            probando_borrado(elem_1(), new Object[]{1});
            probando_borrado(elem_2(), new Object[]{1});
            probando_borrado(elem_3(), new Object[]{1});
            limpieza_total();
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }    
}