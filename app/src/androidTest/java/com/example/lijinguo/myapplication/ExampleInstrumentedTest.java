package com.example.lijinguo.myapplication;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {



        private DataSource ds;

        @Before
        public void setUp(){

            ds = new DataSource(InstrumentationRegistry.getTargetContext());
            ds.open();
        }

        @After
        public void finish() {
            ds.close();
        }

        @Test
        public void testPreConditions() {
            assertNotNull(ds);
        }

        @Test
        public void testUserExist() throws Exception {
            assertFalse(ds.isUserExist("testname", "testpassword"));

        }

        @Test
        public void testCreateUser() throws Exception {
            ds.open();
            ds.createUser("testname", "testpassword");
            assertTrue(ds.isUserExist("testname", "testpassword"));
            ds.close();
        }

        @Test
        public void isNameExist() throws Exception {
            ds.open();
            assertTrue(ds.isNameExist("testname"));
            ds.close();
        }



//    @Test
//    public void deleteUser() throws Exception {
//        DataSource datasource = new DataSource(GlobalVariable.getAppContext());
//        datasource.open();
//        datasource.deleteUser("testname");
//        assertFalse(datasource.isUserExist("testname", "testpassword"));
//    }
//
//    @Test
//    public void getProfileByName() throws Exception {
//        DataSource datasource = new DataSource(GlobalVariable.getAppContext());
//        datasource.open();
//        datasource.createUser("testname2", "testpassword2");
//        User user = datasource.getProfileByName("testname2");
//        assertEquals(user.getUsername(), "testname2");
//        assertEquals(user.getHints(), 0);
//        assertEquals(user.getScore(), 0);
//        assertEquals(user.getPassword(), "testpassword2");
//    }



//    @Te }st
//    public void useAppContext() throws Exception {
//        // Context of the app under test.
//        Context appContext = InstrumentationRegistry.getTargetContext();
//
//        assertEquals("com.example.lijinguo.myapplication", appContext.getPackageName());
//
////        DataSource datasource = new DataSource(appContext);
////        datasource.open();
////        assertFalse(datasource.isUserExist("testname", "testpassword"));
//
//
//




}
