package treemapsimple.test;
import treemapsimple.structs.MyTreeMap;
public class ContainsKeyTest extends Test
{
    MyTreeMap<Integer,String>map;
    public ContainsKeyTest() {
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
    private void probando_verificacion_normal() throws Exception {
        this.comprobar_que(map.containsKey(1));
        this.comprobar_que(map.containsKey(3));
        this.comprobar_que(map.containsKey(5));        
    }
    private void probando_verificacion_con_claves_nulas_que_no_debería_obtener_nada() throws Exception {
        this.comprobar_que(!map.containsKey(null));       
    }
    private void probando_verificacion_con_claves_inexistentes_que_debería_dar_falso() throws Exception {
        this.comprobar_que(!map.containsKey(7));
        this.comprobar_que(!map.containsKey(8));
        this.comprobar_que(!map.containsKey(9));        
    }
    private void probando_verificacion_con_claves_negativas_que_debería_dar_falso() throws Exception {
        this.comprobar_que(!map.containsKey(-7));
        this.comprobar_que(!map.containsKey(-8));
        this.comprobar_que(!map.containsKey(-9));            
    }
    //</editor-fold>
    @Override
    public void test() {
        try {
            probando_verificacion_normal();
            probando_verificacion_con_claves_nulas_que_no_debería_obtener_nada();
            probando_verificacion_con_claves_inexistentes_que_debería_dar_falso();
            probando_verificacion_con_claves_negativas_que_debería_dar_falso();
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }    
}