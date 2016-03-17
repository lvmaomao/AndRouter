package cn.campusapp.router;

import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import cn.campusapp.router.tools.ActivityRouteRuleBuilder;

/**
 * Created by kris on 16/3/11.
 */
@RunWith(AndroidJUnit4.class)
public class ActivityRouteRuleBuilderTest {

    @Test
    public void testRuleBuilder(){
        String rule = new ActivityRouteRuleBuilder()
                .setScheme("activity")
                .setHost("main")
                .addKeyValueDefine("id", Integer.class)
                .addKeyValueDefine("l", Long.class)
                .addKeyValueDefine("f", Float.class)
                .addKeyValueDefine("d", Double.class)
                .addKeyValueDefine("s", String.class)
                .addPathSegment("end").build();
        Assert.assertEquals(rule, "/main/:i{id}/:l{l}/:f{f}/:d{d}/:s{s}/end");
    }

    @Test
    public void testIsRouteRuleValid(){
        Assert.assertFalse(ActivityRouteRuleBuilder.isActivityRuleValid("/:f}/fff"));
        Assert.assertTrue(ActivityRouteRuleBuilder.isActivityRuleValid("http://main/hello/:{f}/:i{id}/l"));
        Assert.assertFalse(ActivityRouteRuleBuilder.isActivityRuleValid("/fwww/:"));
        Assert.assertTrue(ActivityRouteRuleBuilder.isActivityRuleValid("activity://www.baidu.com"));
    }
}
