# AndRouter
AndRouter is an Android framework used to map urls to activities or actions. 

## 中文设计文档
[Android路由框架设计与实现](http://sixwolf.net/blog/2016/03/23/Android%E8%B7%AF%E7%94%B1%E6%A1%86%E6%9E%B6%E8%AE%BE%E8%AE%A1/)

## Features
- AndRouter implements activities router, so it only needs to define the activities mapping table and enjoy using url to open activities.
- AndRouter implements Browser router, so it can be used to open a web url. It will use the system browser to open it.
- AndRouter supports adding a user-defined router. 
- RouteManager will use the first router that can open the url in the list to open it. So the router added earlier will have higher priority.
- The default scheme of activities router is "activity", and browser router is "http" and "https". It's allowed to change them.
- It supports to change the behavior of the default activity router and browser router.
- It can only add one object of a router class to the Router, or it will delete the router object that has the same class that you added and add the new one.

## Usage
### Initialize
It needs to be initialized if you want to use the default activity router and browser router. 

### Initialize the ActivityRouter

Here are two methods to initialize the ActivityRouter.

#### Annotation

It supports using Java annotations to map urls to Activities. For example, the code below maps the urls of "activity://second" and "activity://second2" to SecondActivity.

```java
@Router({"activity://second", "activity://second2"})
public class SecondActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }
}

```

And in the Application class of your app, you need to init the ActivityRouter.

```java
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Router.initActivityRouter(getApplicationContext());
    }
}

```

#### Initializer class

Or you can use an IActivityRouteTableInitializer implementation to add the activity router table. In the code below, some routes are added. For example, the route "activity://first/:s{name}/:i{age}/birthday" will map to FirstActivity.class. The route consists of four parts. It's recommended to do this in the Application class of your app.

```java
    Router.initActivityRouter(getApplicationContext(), new IActivityRouteTableInitializer() {
            @Override
            public void initRouterTable(Map<String, Class<? extends Activity>> router) {
            		// only if the host is equal and pathes match, it matches.
            		// The url "activity://first/kris/26/birthday" is one of the matches. "kris" and 26 are values of key "name" and "age". and the "name" value type is string, the age value type is integer.
            	   router.put("activity://first/:s{name}/:i{age}/birthday", FirstActivity.class); 
                router.put("activity://second/:{name}", SecondActivity.class);  
                router.put("activity://third/home/:i{room1}/printdoor/:s{color}", ThirdActivity.class); 
                router.put("activity://four/buy/:s{name}/:f{price}", FourActivity.class);                           
            }
        });

```

It supports to use these two methods together.

### Initialize the BrowserRouter

The same with ActivityRouter. Do this in Application class.

```java
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Router.initBrowserRouter(getApplicationContext());
    }
}

```

### Definition Of Route Url
- Scheme: Normally, define which router to use, the "activity" in the example.
- Host: Normally, define where to go, the "first" in the example.
- Path: Define key value and path, the "/:s{name}/:i{age}/birthday" in the example. The path segments can be divided into two types. One, the fixed path, such as "birthday", it's fixed. And the value key such as the :s{name}, it defines values in the path. In url, it will be replaced with value. For example, ":s{name}" can be replaced with "kris", and it will be set to the intent extras with putExtra("name", "kris"). Then in the routing activity, the value of "name" can be get with getStringExtra("name"). The table below showes the value key format. ":{}" is essential. And the character after ':' defines the type of the value. If the url mathes but the value type not matches, it will throw RuntimeException, which you should pay attention to.

|   key format |  :i{key}  | :f{key} | :l{key}  | :d{key}    |   :s{key} or :{key} | :c{key} |
|:-------:     |:--------: | :------:| :------: | :--------: |   :-------: | :----:|
|   type       |   integer |  float  |   long   |   double   |     string | char|
- Query parameters: It supports adding some option parameters in the Query parameters. But it will not influence the route match. For example, the url "activity://first/kris/26?wifename=marry" matches to "activity://first/:{name}/:i{age}". Then in the FirstActivity, the value of "wifename" can be got.

### Definition of match
If a url matches a route, the url's scheme and host are equal to the route. And the paths except the value key are equal.   
#### Match Examples
If the route is "activity://main/:{name}/feed/:i{page}/list".    
##### Matches:
   
- activity://main/kris/feed/12/list   
- activity://main/bob/feed/13/list   
- activity://main/jim/feed/14/list?q=8   

##### Not Matches

- hello://main/kris/feed/12/list  |            scheme not equal
- activity://second/kris/feed/12/list    |  host not equal
- activity://main/kris/fed/12/list        |   path not match
- activity://main/kris/12/list            |       path not match

### Useage of Activity Router

open with startActivity(), it matches the route as "activity://second/:{name}"

```java
	
 	private void openSecondActivity(){
        Router.open("activity://second/汤二狗");
   }
```
in the SecondActivity you can get the value of name

```java
	public void getTheValueOfName(){
		String name = getIntent().getStringExtra("name")
	}
```
open with animation

```java
    private void openSecondActivityWithVerticalAnim(){
        ActivityRoute activityRoute = (ActivityRoute) Router.getRoute("activity://second/汤二狗");
        activityRoute
                .setAnimation(this, R.anim.in_from_left, R.anim.out_to_right)
                .open();
    }
```
open for result

```java
    private void openSecondActivityForResult(){
        ActivityRoute activityRoute = (ActivityRoute) Router.getRoute("activity://second/汤二狗");
        activityRoute.withOpenMethodStartForResult(this, 200)
                .open();
    }
```
add extra parameters

```java
    private void openSecondActivityWithExtraValue(){
        Date date = new Date();
        ActivityRoute activityRoute = (ActivityRoute) Router.getRoute("activity://third");
        activityRoute
                .withParams("date", date)
                .open();
    }

```
you can get the extra parameters in the ThirdActivity

```java
	Date date = (Date) getIntent().getSerializableExtra("date");
```
### Usage of Browser Router
And you can use Router to open web url.

```java
	Router.open("http://www.baidu.com");
```

### Add your own Router
you can define your own router and route.

```java
private static class TestRouter extends BaseRouter{

        @Override
        public void open(IRoute route) {
            Timber.i(route.getUrl());
        }

        @Override
        public void open(String url) {
            Timber.i(url);
        }

        @Override
        public IRoute getRoute(String url) {
            return new TestRoute(this, url);
        }

        @Override
        public boolean canOpenTheRoute(IRoute route) {
            return (route instanceof TestRoute);
        }

        @Override
        public boolean canOpenTheUrl(String url) {
            return TextUtils.equals(UrlUtils.getScheme(url), "test");
        }

        @Override
        public Class<? extends IRoute> getCanOpenRoute() {
            return TestRoute.class;
        }
    }

    private static class TestRoute extends BaseRoute {

        public TestRoute(IRouter router, String url) {
            super(router, url);
        }
    }
```

And add it to the RouterManager

```java
	Router.addRouter(new TestRouter());
```
## Install
In the build.gradle file of your project. You need to:

Add dependeny to apt:

```java
buildscript {
    dependencies {
        classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
    }
}
```

Add jitpack repository:

```
allprojects {
 repositories {
    jcenter()
    maven { url "https://jitpack.io" }
 }
}
```

Then, in the build.gradle file of your app module, you need to add these dependencies.

```
apply plugin: 'android-apt'

dependencies {
    compile 'com.github.campusappcn.AndRouter:router:1.2.0'
    apt 'com.github.campusappcn.AndRouter:compiler:1.2.0'
}
```

Note: do not add the jitpack.io repository under buildscript

## Feedback
If you have any problems, welcome, and please share any issues.

## License
```
Copyright 2015 杭州树洞网络科技有限公司

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
