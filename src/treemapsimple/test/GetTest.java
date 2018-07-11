package treemapsimple.test;

import treemapsimple.structs.MyTreeMap;

public class GetTest extends Test
{
    MyTreeMap<Integer,String>map;
    public GetTest() {
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
    private void probando_busqueda_normal() throws Exception {
        this.comprobar_que(map.get(1) != null);
        this.comprobar_que(map.get(3) != null);
        this.comprobar_que(map.get(5) != null);                
    }
    private void probando_busqueda_con_claves_nulas_que_no_debería_obtener_nada() throws Exception {
        this.comprobar_que(map.get(null) == null);                
    }
    private void probando_busqueda_con_claves_inexistentes_que_no_debería_obtener_nada() throws Exception {
        this.comprobar_que(map.get(7) == null);
        this.comprobar_que(map.get(9) == null);        
    }
    private void probando_busqueda_con_claves_negativas_que_no_debería_obtener_nada() throws Exception {
        this.comprobar_que(map.get(-1) == null);
        this.comprobar_que(map.get(-7) == null);
        this.comprobar_que(map.get(-5) == null);
        this.comprobar_que(map.get(-9) == null);        
    }
    private void probando_busqueda_vacia() throws Exception {
        map.clear();
        this.comprobar_que(map.get(1) == null);        
    }
    //</editor-fold>
    @Override
    public void test() {
        try {
            probando_busqueda_normal();
            probando_busqueda_con_claves_nulas_que_no_debería_obtener_nada();
            probando_busqueda_con_claves_inexistentes_que_no_debería_obtener_nada();
            probando_busqueda_con_claves_negativas_que_no_debería_obtener_nada();
            probando_busqueda_vacia();
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }    
}