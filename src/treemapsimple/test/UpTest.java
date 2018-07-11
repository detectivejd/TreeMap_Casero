package treemapsimple.test;
import treemapsimple.structs.MyTreeMap;
import java.util.Map;
public class UpTest extends Test
{
    MyTreeMap<Integer,String>map; 
    public UpTest() {
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
    private Object[][] zero(){
        return new Object[][] {
            new Object[]{0,"Deborah"},
        };        
    }
    private Object[][] giantKey(){
        return new Object[][]{
            new Object[]{1000,"Deborah"},
            new Object[]{3000,"Franco"},
            new Object[]{6050,"Tommy"} 
        };
    }
    private Object[][] reverse1(){
        return new Object[][]{
            new Object[]{3,"Franco"},
            new Object[]{5,"Miguel"},
            new Object[]{6,"Denisse"},
            new Object[]{4,"Manuela"},
            new Object[]{1,"Deborah"},
            new Object[]{2,"Tommy"}
        };                
    }
    private Object[][] pares1(){
        return new Object[][]{
            new Object[]{2,"Franco"},
            new Object[]{4,"Miguel"},
            new Object[]{6,"Denisse"},
            new Object[]{8,"Manuela"},
            new Object[]{10,"Deborah"},
            new Object[]{12,"Tommy"}
        };                
    }
    private Object[][] primos1(){
        return new Object[][]{
            new Object[]{3,"Franco"},
            new Object[]{5,"Miguel"},
            new Object[]{7,"Denisse"},
            new Object[]{11,"Manuela"},
            new Object[]{13,"Deborah"},
            new Object[]{17,"Tommy"}
        };                
    }
    private Object[][] negativeKey(){
        return new Object[][]{
            new Object[]{-1,"Micaela"},
            new Object[]{-3,"Serafín"},
            new Object[]{-9,"Joaquín"} 
        };
    }
    private Object[][] nullKey(){
        return new Object[][]{
            new Object[]{null,"Felipe"},
            new Object[]{null,"Ramiro"},
            new Object[]{null,"Monetta"} 
        };
    }
    //</editor-fold>
    //<editor-fold desc="pruebas">
    private void probando_insercion_con_claves_existentes_que_deberia_cambiar_sus_valores() throws Exception{
        probando_insercion(restart1());        
        map.put(1, "Micaela");
        map.put(3, "Serafín");
        map.put(5, "Agustin");
        comprobar_que(map.get(1)!= null);
        comprobar_que(map.get(3)!= null);
        comprobar_que(map.get(5)!= null);
    }
    private void probando_insercion_repetida() throws Exception {
        probando_insercion(restart1());
        map.put(1, "Deborah");
        map.put(5, "Miguel");
        map.put(6, "Denisse");
        comprobar_que(map.size() == 6);
    } 
    private Object[][] probando_insercion_cualquiera() throws Exception {
        return new Object[][] {
            new Object[]{3,"Franco"},
            new Object[]{5,"Miguel"},
            new Object[]{2,"Tommy"},
            new Object[]{4,"Manuela"},            
            new Object[]{1,"Deborah"},
            new Object[]{6,"Denisse"}
        };  
    }
    private void probando_insercion(Object[][] arreglo) throws Exception{
        map.clear();
        for (Object[] arreglo1 : arreglo) {
            if(arreglo1[0] == null){
                map.put(null, arreglo1[1].toString());
            } else {
                map.put(Integer.valueOf(arreglo1[0].toString()), arreglo1[1].toString());
            }            
        }
        for(Map.Entry<Integer,String>e : map.entrySet()){
            this.comprobar_que(map.get(e.getKey()) != null);
        }
    }
    //</editor-fold>
    @Override
    public void test() {
        try {
            this.probando_insercion(nullKey());
            probando_insercion_con_claves_existentes_que_deberia_cambiar_sus_valores();
            this.probando_insercion(negativeKey());
            probando_insercion_repetida();
            this.probando_insercion(reverse1());
            this.probando_insercion(giantKey());
            this.probando_insercion(pares1());
            this.probando_insercion(zero());
            this.probando_insercion(primos1());
            this.probando_insercion(probando_insercion_cualquiera());
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }    
}