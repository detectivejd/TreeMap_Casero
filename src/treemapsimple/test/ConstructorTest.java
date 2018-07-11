package treemapsimple.test;

import treemapsimple.structs.MyTreeMap;

public class ConstructorTest extends Test
{
    private void creando_map_vacio() throws Exception{
        MyTreeMap<Integer,String>m= new MyTreeMap();
        this.comprobar_que(m.isEmpty());
    }
    private void creando_map_normal() throws Exception {
        MyTreeMap<Integer,String>m1= new MyTreeMap();
        m1.put(1, "Deborah");
        m1.put(2, "Tommy");
        m1.put(3, "Franco");
        m1.put(4, "Manuela");
        this.comprobar_que(m1.size() == 4);
    }
    private void pasar_datos_de_hashmap_a_nuestro_map() throws Exception{
        java.util.HashMap<Integer, String> m = new java.util.HashMap();
        m.put(1, "Agustin");
        m.put(2, "Amanda");
        m.put(3, "Olivia");
        m.put(4, "Maite");
        /*---------------------------------------*/
        MyTreeMap<Integer,String> m3 = new MyTreeMap();
        m3.putAll(m);
        /*---------------------------------------*/
        this.comprobar_que(m3.size() == 4);
    }    
    @Override
    public void test() {
        try {
            creando_map_vacio();
            creando_map_normal();
            pasar_datos_de_hashmap_a_nuestro_map();
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }
}