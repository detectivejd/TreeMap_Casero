package treemapsimple.test;
public abstract class Test
{
    protected void comprobar_que(boolean condition) throws Exception{        
        if(!condition){           
            throw new Exception();
        }
    }
    public abstract void test();
}
