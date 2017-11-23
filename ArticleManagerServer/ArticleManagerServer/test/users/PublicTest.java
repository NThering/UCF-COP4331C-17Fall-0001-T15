/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package users;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author NThering
 */
public class PublicTest {
    
    public PublicTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of login method, of class Public.
     */
    @Test
    public void testLogin() throws Exception 
    {
        System.out.println("login");
        String username = "";
        String password = "";
        int expResult = 0;
        int result = Public.login(username, password);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of register method, of class Public.
     */
    @Test
    public void testRegister1() throws Exception {
        System.out.println("register 1");
        assertEquals(Public.login("EEEEEEEEE", "zaq1zaq1"), -1);
        registerAndCompare( "EEEEEEEEE", "zaq1zaq1", true );
    }
    
     /**
     * Test of register method, of class Public.
     */
    @Test
    public void testRegister2() throws Exception {
        System.out.println("register 2");
        registerAndCompare( "EEEEEEEEE", "zaq1zaq1", true );
        registerAndCompare( "EEEEEEEEE", "password12345", false );
        registerAndCompare( "Not EEEEEEEEE", "zaq1zaq1", true );
        registerAndCompare( "Seven", "zaq1zaq1", true );
        registerAndCompare( "Six", "zaq1zaq1", true );
        registerAndCompare( "Circle", "zaq1zaq1", true );
        registerAndCompare( "Seven", "zaq1zaq1", false );
        registerAndCompare( "EEEEEEEEE", "zaq1zaq1", false );
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
    @Test
    public void testRegister3() throws Exception {
        System.out.println("register 3");
        assertEquals(Public.login("EEEEEEEEE", "zaq1zaq1"), -1);
        registerAndCompare( "EEEEEEEEE", "zaq1zaq1", true );
        assertEquals(Public.login("EEEEEEEEE", "zaq1zaq1"), 0);
    }
    
    
    void registerAndCompare( String name, String pwd, boolean expect ) throws Exception
    {
        boolean result = Public.register(name, pwd);
        assertEquals(expect, result);
    }
}
