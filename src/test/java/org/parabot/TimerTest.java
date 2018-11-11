package org.parabot;

import org.junit.Assert;
import org.junit.Test;
import org.parabot.environment.api.utils.Timer;

/**
 * @author JKetelaar
 */
public class TimerTest {

    @Test
    public void test(){
        Timer timer1 = new Timer(0, System.currentTimeMillis() - (1000 * 60 * 60 * 24));
        Assert.assertEquals(timer1.toString(), "1d:00:00:00");

        Timer timer2 = new Timer(0, System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 5));
        Assert.assertEquals(timer2.toString(), "5d:00:00:00");

        Timer timer3 = new Timer(0, System.currentTimeMillis() - (1000 * 60 * 60));
        Assert.assertEquals(timer3.toString(), "01:00:00");

        Timer timer4 = new Timer(0, System.currentTimeMillis() - (1000 * 60 * 60 * 12));
        Assert.assertEquals(timer4.toString(), "12:00:00");
    }
}
