package cn.campusapp.router;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import cn.campusapp.androuter.DebugActivity;
import cn.campusapp.router.route.ActivityRoute;
import cn.campusapp.router.router.ActivityRouter;
import cn.campusapp.router.router.IRouter;

/**
 * Created by kris on 16/3/11.
 */
@RunWith(AndroidJUnit4.class)
public class RouteTest {

    ActivityTestRule<DebugActivity> rule = new ActivityTestRule<DebugActivity>(DebugActivity.class);

    @Test
    public void testRouteBuilder(){
        String path = "activity://www.baidu.com/happy/hello?pp=23";
        long longvalue = 12312312312313l;
        char charvalue = 'c';
        CharSequence charSequence = "charsequence";
        String string = "string";
        float floatValue = 0.34f;
        double doubleValue = 321313131242345234.3241321d;
        int intValue = 1223;

        DebugActivity debugActivity = rule.getActivity();
        IRouter router = new ActivityRouter();
        ActivityRoute route = new ActivityRoute.Builder(router)
                .setUrl(path)
                .withParams("longvalue", longvalue)
                .withParams("charvalue", charvalue)
                .withParams("charsequence", charSequence)
                .withParams("string", string)
                .withParams("float", floatValue)
                .withParams("double", doubleValue)
                .withParams("int", intValue)
                .withAnimation(debugActivity, R.anim.in_from_left, R.anim.out_to_right)
                .build();

        Assert.assertEquals(route.getPath(), path);
        Assert.assertEquals(route.getExtras().getLong("longvalue"), longvalue);
        Assert.assertEquals(route.getExtras().getChar("charvalue"), charvalue);
        Assert.assertEquals(route.getExtras().getCharSequence("charsequence"), charSequence);
        Assert.assertEquals(route.getExtras().getString("string"), string);
        Assert.assertEquals(route.getExtras().getFloat("float"), floatValue);
        Assert.assertEquals(route.getExtras().getDouble("double"), doubleValue);
        Assert.assertEquals(route.getExtras().getInt("int"), intValue);
        Assert.assertEquals(route.getActivity(), debugActivity);
        Assert.assertEquals(route.getInAnimation(), R.anim.in_from_left);
        Assert.assertEquals(route.getOutAnimation(), R.anim.out_to_right);
    }




}
