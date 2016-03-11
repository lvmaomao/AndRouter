package cn.campusapp.router;

import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by kris on 16/3/11.
 */
@RunWith(AndroidJUnit4.class)
public class RouteRuleBuilderTest {

    @Test
    public void testRuleBuilder(){
        String rule = new RouteRuleBuilder()
                .addPath("main")
                .addParameter("id", Integer.class)
                .addParameter("l", Long.class)
                .addParameter("f", Float.class)
                .addParameter("d", Double.class)
                .addParameter("s", String.class)
                .addPath("end").build();
        Assert.assertEquals(rule, "/main/:i{id}/:l{l}/:f{f}/:d{d}/:s{s}/end");
    }

    @Test
    public void testIsRouteRuleValid(){
        Assert.assertFalse(RouteRuleBuilder.isRouteRuleValid("/:f}/fff"));
        Assert.assertTrue(RouteRuleBuilder.isRouteRuleValid("/main/hello/:{f}/:i{id}/l"));
        Assert.assertFalse(RouteRuleBuilder.isRouteRuleValid("/fwww/:"));
        Assert.assertTrue(RouteRuleBuilder.isRouteRuleValid("/"));
    }
}
