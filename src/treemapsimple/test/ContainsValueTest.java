package treemapsimple.test;
import treemapsimple.structs.MyTreeMap;
public class ContainsValueTest extends Test
{
    MyTreeMap<Integer,String>map;
    public ContainsValueTest() {
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
    private void probando_verificacion_normal() throws Exception{
        this.comprobar_que(map.containsValue("Deborah"));
        this.comprobar_que(map.containsValue("Franco"));
        this.comprobar_que(map.containsValue("Miguel"));            
    }
    private void probando_verificacion_con_claves_nulas_que_no_debería_obtener_nada() throws Exception{
        this.comprobar_que(!map.containsValue(null));            
    }
    private void probando_verificacion_con_claves_inexistentes_que_debería_dar_falso() throws Exception{
        this.comprobar_que(!map.containsValue("Pepe"));
        this.comprobar_que(!map.containsValue("Luis"));            
    }
    //</editor-fold>
    @Override
    public void test() {
        try {
            probando_verificacion_normal();
            probando_verificacion_con_claves_nulas_que_no_debería_obtener_nada();
            probando_verificacion_con_claves_inexistentes_que_debería_dar_falso();
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }    
}