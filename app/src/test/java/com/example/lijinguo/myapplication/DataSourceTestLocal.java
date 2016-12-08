package com.example.lijinguo.myapplication;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by lijinguo on 11/10/16.
 */
public class DataSourceTestLocal extends AndroidTestCase{
    private DataSource ds;

    @Override
    public void setUp() throws Exception{
        super.setUp();
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        ds = new DataSource(context);

    }

    @Override
    public void tearDown() throws Exception {
        ds.close();
        super.tearDown();
    }

    @Test
    public void testUserExist() throws Exception {
        ds.open();
        assertFalse(ds.isUserExist("testname", "testpassword"));
        ds.close();
    }

    @Test
    public void createUser() throws Exception {
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

}