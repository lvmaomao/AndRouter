# AndRouter
AndRouter is a android framework used to map url to activities or actions. 

## Features
- AndRouter implement activities router, so your can define your own activities mapping table and use url to open activities.
- Androuter implement Browser router. So you can use it to open a web url. It will use the system browser to open it.
- AndRouter support adding user-defined router. 
- RouteManager will use the first router which can open the url to open it. So the router added earlier will have higher priority.
- The default schme of activities router is "activity", and broswer router is "http" and "https". You are allowed to change them.
- You can change the behaviour of the default activity router and browser router.
- You can only add one object of a router class to the Router. Or it will delete the router object which has the same class of that you added and add the new one.

## Usage
### Initialize
If you want to use the default activity router and browser router, you should initialize them.   

```java
    Router.initActivityRouter(getApplicationContext(), new IActivityRouteTableInitializer() {
            @Override
            public void initRouterTable(Map<String, Class<? extends Activity>> router) {
                router.put("activity://second/:{name}", SecondActivity.class);
            }
        });
// if you want to change the default scheme of ActivityRouter
//        Router.initActivityRouter(getApplicationContext(), "hello", new IActivityRouteTableInitializer() {
//            @Override
//            public void initRouterTable(Map<String, Class<? extends Activity>> router) {
//                router.put("hello://second/:{name}", SecondActivity.class);
//            }
//        });
    Router.initBrowserRouter(getApplicationContext());

```

As we can see, I add a route to the default activities router. It will route the "activity://second/:{name}" to SecondActivity. Here you may not understand :{name}. This is the activity format i define.     
Route url are make up with four part as we know. 

- Scheme: Normally, define which router to use.
- Host: Normally, define where to go
- path: define key value and path. For example if you what to go to the SecondActivity and pass the value name as "Kris". You can define the route as "activity://second/:{name}" and the url as "activity://second/Kris", the default activities router will put the string value kris with key name to the Intent extras. And you can specify the type of the key value. If it not match, it will throw a RuntimeException. The support key types are showed in the table below. The :{} is essential.

|   key format |  :i{key}  | :f{key} | :l{key}  | :d{key}    |   :s{key} or :{key} | :c{key} |
|:-------:     |:--------: | :------:| :------: | :--------: |   :-------: | :----:|
|   type       |   integer |  float  |   long   |   double   |     string | char|
- Query parameters: you can add some option parameters in the Query parameters. It will also add to the Intent extras. But it will not influence the route match.

### Useage of Activity Router
If you have initialize the Android Router, you can use it to open activity url like the code below.

```java
 private void openSecondActivity(){
        Router.open("activity://second/汤二狗");
    }

    private void openSecondActivityWithVerticalAnim(){
        ActivityRoute activityRoute = (ActivityRoute) Router.getRoute("activity://second/汤二狗");
        activityRoute
                .setAnimation(this, R.anim.in_from_left, R.anim.out_to_right)
                .open();
    }

    private void openSecondActivityWithHorizontalAnim(){
        ActivityRoute activityRoute = (ActivityRoute) Router.getRoute("activity://second/汤二狗");
        activityRoute
                .setAnimation(this, R.anim.in_from_top, R.anim.out_to_bottom)
                .open();
    }

    private void openSecondActivityForResult(){
        ActivityRoute activityRoute = (ActivityRoute) Router.getRoute("activity://second/汤二狗");
        activityRoute.withOpenMethodStartForResult(this, 200)
                .open();
    }

    private void openSecondActivityWithExtraValue(){
        Date date = new Date();
        ActivityRoute activityRoute = (ActivityRoute) Router.getRoute("activity://third");
        activityRoute
                .withParams("date", date)
                .open();
    }

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
Users of your library will need add the jitpack.io repository:

```
allprojects {
 repositories {
    jcenter()
    maven { url "https://jitpack.io" }
 }
}
```

and:

```
dependencies {
    compile 'com.github.campusappcn:AndRouter:1.0.0'
}
```

Note: do not add the jitpack.io repository under buildscript

## Feedback
If you have any problem, welcome to give some issues.

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