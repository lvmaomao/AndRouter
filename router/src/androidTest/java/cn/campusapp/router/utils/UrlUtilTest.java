package cn.campusapp.router.utils;

import junit.framework.Assert;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import cn.campusapp.router.BaseUnitTest;

/**
 * Created by kris on 16/3/11.
 * TODO 目前不支持在值内有：
 *
 *
 */
public class UrlUtilTest extends BaseUnitTest {

    @Test
    public void testGetPathSegments(){
        List<String> segs = null;
        segs = UrlUtils.getPathSegments("activity://popo.com/happy/picasso/:c{id}/:{key}");
        Assert.assertEquals(segs.size(), 4);


        segs = UrlUtils.getPathSegments("http://www.baidu.com/happy/picasso/12312332232312/f");
        Assert.assertEquals(segs.size(), 4);
    }


    @Test
    public void testGetHost(){
        String host = UrlUtils.getScheme("cn.campusapp://www.baidu.com/picasso/ffff");
        Assert.assertEquals(host, "cn.campusapp");
    }


    @Test
    public void testGetOptionParams(){
        Map<String, String> ret = null;
        ret = UrlUtils.getParameters("http://www.baidu.com/happy?cc=ffff&w=123&cp=1");
        Assert.assertEquals(ret.get("cc"), "ffff");
        Assert.assertEquals(ret.get("w"), "123");
        Assert.assertEquals(ret.get("cp"), "1");
    }

}
