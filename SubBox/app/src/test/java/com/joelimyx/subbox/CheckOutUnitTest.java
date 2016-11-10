package com.joelimyx.subbox;

import com.joelimyx.subbox.Classes.CheckOutItem;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by Joe on 11/4/16.
 */

public class CheckOutUnitTest {
    CheckOutItem mTestCheckout = new CheckOutItem(1,"test",3,14.32,"");
    //Test add or remove
    @Test
    public void testAddroMinusCount()throws Exception{
        mTestCheckout.addOrMinusCount('-');
        assertEquals(2,mTestCheckout.getCount());
        mTestCheckout.addOrMinusCount('+');
        assertEquals(3,mTestCheckout.getCount());
    }

    //Test subtotal
    @Test
    public void testGetSubtotalPrice()throws Exception{
        assertEquals(42.96,mTestCheckout.getSubtotalPrice(),0.01);
    }
}
