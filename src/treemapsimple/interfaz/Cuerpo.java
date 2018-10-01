package treemapsimple.interfaz;
import treemapsimple.test.*;
public class Cuerpo {
    public static void main(String[] args) {
        Test t1 = null;
        t1 = new ConstructorTest();
        t1.test();
        t1 = new UpTest();
        t1.test();
        t1 = new GetTest();
        t1.test();
        t1 = new ContainsKeyTest();
        t1.test();
        t1 = new ContainsValueTest();
        t1.test();
        t1 = new DownTest();
        t1.test();
        t1 = new EntryTest();
        t1.test();
        t1 = new AscendingHeadMapTest();
        t1.test();
        t1 = new AscendingTailMapTest();
        t1.test();        
        t1 = new AscendingSubMapTest();
        t1.test();
        t1 = new DescendingHeadMapTest();
        t1.test();
        t1 = new DescendingTailMapTest();
        t1.test();
        t1 = new DescendingSubMapTest();
        t1.test();
    }
}