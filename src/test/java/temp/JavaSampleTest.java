package temp;

import org.junit.*;

import static org.junit.Assert.*;//静态导入


/**
 * JavaSample Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>二月 23, 2017</pre>
 */
public class JavaSampleTest {


    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: main(String[] args)
     */
    @Ignore
    public void testMain() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: add(int a, int b)
     */
    @Test(timeout = 2)
    public void testAdd() throws Exception {
//TODO: Test goes here...
        assertEquals(30, new JavaSample().add(3, 10));
    }

    /**
     * Method: sub(int a, int b)
     */
    @Test(expected = NullPointerException.class)
    public void testSub() throws Exception {
//TODO: Test goes here...
        new JavaSample().sub(2, 2);
    }


} 
