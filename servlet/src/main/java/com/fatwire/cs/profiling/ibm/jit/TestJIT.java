package com.fatwire.cs.profiling.ibm.jit;
/**
 * Test class to demonstrate JIT compiler issue in IBM JDK.
 * Code here simulates what happens in SysPage.addPage()
 * @author Hareesh Kadlabalu
 */
public final class TestJIT
{
	String boo;
    public static void main( String [] args )
    {
        TestJIT instance = new TestJIT();
        for( int x = 0; x < 2001; x++ )
        {
            instance.doSomething();
        }
    }

    public void doSomething()
    {
//    System.out.println("xx");
        String id = null;
        boolean result = false;
        Trans trans = null;

        try
        {
            trans = new Trans();
            trans.begin();

            id = genId();
            result = someOp( id );

            if( result )
            {
                trans.commit();
                trans = null;
            }
        }
        catch( Exception ex )
        {
            id = null;
            ex.printStackTrace();
        }
        finally
        {
            if ( trans != null )
            {
                result = false;
                trans.rollback();
                id = null;
            }
            doSomeOtherOp( !result );
        }
    }

    private void doSomeOtherOp( boolean failed )
    {
        if( failed )
            System.out.println( "This cannot happen!!" );
    }

    private String genId()
    {
        int x;
        for( x = 0; x < 100; x++ ) { }
        return x + "";
    }

    private boolean someOp( String id )
    {
        int x = Integer.parseInt( id );
        int y;
        for( y = 0; y < x; y++ ) { }
        return y > 0;
    }

    private static class Trans
    {
        void begin() {}
        void commit() {}
        void rollback() {}
    }
}
