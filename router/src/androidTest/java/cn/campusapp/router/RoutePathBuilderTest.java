package cn.campusapp.router;

import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import cn.campusapp.router.exception.NotAllKeySetException;
import timber.log.Timber;

/**
 * Created by kris on 16/3/11.
 */
@RunWith(AndroidJUnit4.class)
public class RoutePathBuilderTest {

    @Test
    public void testBuildPath(){
        String rule = "/main/:i{id}/b/:s{des}/h/:l{age}/k";
        try {
            String path = new RoutePathBuilder(rule)
                    .withKeyValue("id", 1)
                    .withKeyValue("des", "sss")
                    .withKeyValue("age", 11111111l)
                    .build();
            Assert.assertEquals("/main/1/b/sss/h/11111111/k", path);
        } catch (NotAllKeySetException e) {
            Timber.e(e, "");
            Assert.fail();
        }

        try{
            String path = new RoutePathBuilder(rule)
                    .withKeyValue("id", 2)
                    .build();

            Assert.fail();
        } catch (NotAllKeySetException e){
            Timber.e(e, "");
        }


    }

}
