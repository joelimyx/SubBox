package com.joelimyx.subbox;

import com.joelimyx.subbox.Classes.CheckOutItem;
import com.joelimyx.subbox.dbassethelper.SubBoxHelper;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

/**
 * Created by Joe on 11/9/16.
 */

public class SubBoxHelperTest {
    List<CheckOutItem> testItems = new ArrayList<>();
    @Test
    public void testGetSubtotal()throws Exception{

        testItems.add(new CheckOutItem(1,"one",2,12.43,""));
        testItems.add(new CheckOutItem(2,"two",7,20.3,""));
        testItems.add(new CheckOutItem(3,"three",3,10.56,""));
        testItems.add(new CheckOutItem(4,"forth",6,12.43,""));
        testItems.add(new CheckOutItem(5,"five",1,50.22,""));

        double subtotal = SubBoxHelper.getsInstance(null).getSubtotal(testItems);
        assertEquals(323.44,subtotal,0.01);
    }

    @Test
    public void testCreatePlaceHolderForQuery()throws Exception{
        assertEquals("?,?,?",SubBoxHelper.getsInstance(null).createPlaceHolderForQuery(3));
    }
}
