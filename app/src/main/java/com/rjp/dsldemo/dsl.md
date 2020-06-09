    **** 原创不易，转载注明出处 ****

android原生App最大的痛点就是更新周期长，稍有改动，就需要发布新版本，加上审核，最快也要3天后才能让用户看到新模块。

如果能通过后台下发数据创建View，执行操作，就可以很灵活的动态控制页面。

如果下发json，View的数据很好处理，因为android提供的控件类型是有限的，只要枚举出来就可以。但是一个App的操作太多太多，但是不是无限多呢？也不是。

### 一、 只有View

假设开发一个App只需要展示基础控件。

```json
    {
        "name": "TV",
        "content": "文本",
        "color": "#333333",
        "size": "16"
    }
```

明显这是一个TextView，指定了显示的文字、颜色和大小。

```json
    {
        "name": "IV",
        "width": "100",
        "height": "100",
        "url": "https://bugly.qq.com/v2/image?id=2c06cba9-7d27-4f1c-8b0d-b932c33deaf3"
    }
```

这个就可以生成一个ImageView。如果更细化，还可以指定margin和padding，脑补css。

同理，如果是容器布局，可以扩展一下，加一个子类集合：

```json
    {
        "name": "VLL",
        "children": [
            {
                "name": "TV"
            },
            {
                "name": "TV"
            },
            {
                "name": "TV"
            }
        ]
    }
```

"VLL"可以提前协议好是Vertical的LinearLayout，children就是子View的集合。

有了控件还需要一个页面承载，页面也能看成View，但是页面需要有更多的功能，全放View会导致View的属性过多，所以也能做一层抽象，

```json
    {
        "contextName": "home",
        "layout": {
        
        }
    }
```

"contextName"能唯一标记一个页面，比如登录页面可以标记为"login"，"layout"实质就是一个View，字段和上面的基础控件一致。

有了上面的规则，现在可以尝试做一个有三个文本的首页：

```json
    {
        "contextName": "home",
        "layout": {
            "name": "VLL",
            "children": [
                {
                    "name": "TV",
                    "content": "打开页面",
                    "color": "#333333",
                    "size": "16"
                },
                {
                    "name": "TV",
                    "content": "弹出Toast",
                    "color": "#333333",
                    "size": "16"
                },
                {
                    "name": "TV",
                    "content": "请求网络",
                    "color": "#333333",
                    "size": "16"
                }
            ]
        }
    }
```

### 二、 View的响应

第一步已经能自动填充控件了，但是如果真想点击第二个TextView去弹出一个Toast，怎么处理呢？可以尝试在View的数据里面指定一个动作：

```json
    {
         "name": "TV",
         "content": "弹出Toast",
         "color": "#333333",
         "size": "16",
         "action": {
            "name": "toast",
            "msg": "弹出一下"
         }
    }
```

这样点击的时候就可以解析出一个Toast的动作。当然Action是需要提前穷举的，还是前面说的，一个App的动作肯定不是无限的。比如跳转一个页面：

```json
    {
         "name": "TV",
         "content": "打开页面",
         "color": "#333333",
         "size": "16",
         "action": {
            "name": "open",
            "nextPage": {
                "contextName": "detail",
                "layout": {}
            }
         }
    }
```

"nextPage"已经能自动生成第二个页面了。甚至于，请求也是一个Action：

```json
    {
         "name": "TV",
         "content": "请求网络",
         "color": "#333333",
         "size": "16",
         "action": {
            "name": "request",
            "url": "https://xxx.com",
            "params": {
                "name": "rjp",
                "age": "18"
            }
         }
    }
```

如果你已经封装了请求，上面的数据已经够去请求一下了，但是请求回来的数据呢？这就是说，有时候Action的动作是有后续动作的，有一种嵌套关系：

```json
    {
         "name": "TV",
         "content": "请求网络",
         "color": "#333333",
         "size": "16",
         "action": {
            "name": "request",
            "url": "https://xxx.com",
            "params": {
                "name": "rjp",
                "age": "18"
            },
            "action": {
              "name": "setData"
            }
         }
    }
```

"request"的后续有一个"setData"的动作。这就不好处理了，因为每个页面的业务数据都是独特的，数据模型无法统一。所以需要一个中间层，能对后台下发的数据进行标准化输出：

```java
    public class DataBean {
        private String a;
        private String b;
        private String c;
        private String d;
        private String e;
        private String f;
        private String g;
    }
```

也就是说，不管我请求哪个接口，返回的数据永远是abcdefg，我也不关心字段究竟代表什么。

那怎么知道下发的数据应该填充到哪个控件呢？可以通过给控件设置一个value，来指定需要的数据：

```json
    {
         "name": "TV",
         "content": "请求网络",
         "color": "#333333",
         "size": "16",
         "value": "a",
         "action": {
            "name": "request",
            "url": "https://xxx.com",
            "params": {
                "name": "rjp",
                "age": "18"
            },
            "action": {
                "name": "setData"
            }
         }
    }
```

这样点击完请求数据，如果数据里面带上了"a": "后台数据"，就将数据填到这个TextView上。填充首先想到的就是遍历页面的根View，但是随着页面复杂化，非常耗时，可以参考局部刷新的做法，对有value属性的View进行缓存，只要遍历缓存集合就行了，非常高效。

### 三、 拼多多

说了这么多还没有一个完整的例子，下面一步步来实现。

```java
    public class PageActivity extends AppCompatActivity implements IPage {
    
        private List<View> viewCache;
    
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_page);
    
            FrameLayout pageContainer = findViewById(R.id.page_container);
            Intent intent = getIntent();
            if (intent != null && intent.hasExtra("nextPage")) {
                String nextPage = intent.getStringExtra("nextPage");
                PageBean pageBean = JSONObject.parseObject(nextPage, PageBean.class);
                if (pageBean != null) {
                    viewCache = new ArrayList<>();
                    pageContainer.addView(LayoutFactory.createView(this, pageBean.getLayout()));
                }
            }
        }
    
        @Override
        public Context getContext() {
            return this;
        }
    
        @Override
        public List<View> getViewCache() {
            return viewCache;
        }
    }
```

创建一个简单的页面容器Activity，布局只有一个FrameLayout，从上一个页面接收json，这个json描述整个Page，当然也可以通过接口请求获取，测试阶段直接从Assets读取。

上面的关键是获取到json转成PageBean结构：

```java
    public class PageBean {
        private String contextName;
        private ViewBean layout;
    
        public ViewBean getLayout() {
            return layout;
        }
    
        public void setLayout(ViewBean layout) {
            this.layout = layout;
        }
    
        public String getContextName() {
            return contextName;
        }
    
        public void setContextName(String contextName) {
            this.contextName = contextName;
        }
    }
```

ViewBean:

```java
    public class ViewBean {
    
        private String id;
        private String name;
        private String content;
        private String color;
        private String value;
        private float size = 14.0f;
        private int width;
        private int height;
        private List<ViewBean> children;
        private String action;
        private String url;
        private String itemType;
        
    }
```

ViewBean存在一个问题就是所有的属性都糅合在一个数据结构里，会造成浪费，解决办法是一个类型的View给一个Bean，然后设置ViewType，但是那是优化时考虑的问题，目前只使用一个。

拿到了Layout就可以通过简单工厂模式开始渲染布局了：

```java
    public class LayoutFactory {
    
        public static View createView(IPage page, ViewBean viewBean) {
            String name = viewBean.getName();
            switch (name) {
                case ViewType.VLL:
                    return createLinearLayout(page, viewBean, true);
                case ViewType.HLL:
                    return createLinearLayout(page, viewBean, false);
                case ViewType.TV:
                    return createTextView(page, viewBean);
                case ViewType.IV:
                    return createImageView(page, viewBean);
                default:
                    return new View(page.getContext());
            }
        }
    
        private static View createLinearLayout(IPage page, ViewBean viewBean, boolean isVertical) {
            LinearLayout vll = new LinearLayout(page.getContext());
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            vll.setLayoutParams(layoutParams);
            vll.setOrientation(isVertical ? LinearLayout.VERTICAL : LinearLayout.HORIZONTAL);
            List<ViewBean> children = viewBean.getChildren();
            if (children != null && children.size() > 0) {
                int size = children.size();
                for (int i = 0; i < size; i++) {
                    vll.addView(createView(page, children.get(i)));
                }
            }
            return vll;
        }
        
        private static View createImageView(IPage page, ViewBean viewBean) {
            ImageView imageView = new ImageView(page.getContext());
            imageView.setTag(R.id.image_tag_id, viewBean);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(viewBean.getWidth(), viewBean.getHeight());
            imageView.setLayoutParams(layoutParams);
            String url = viewBean.getUrl();
            if (!TextUtils.isEmpty(url)) {
                Glide.with(page.getContext()).load(url).into(imageView);
            }
            if (!TextUtils.isEmpty(viewBean.getValue())) {
                List<View> viewCache = page.getViewCache();
                if (viewCache != null) {
                    viewCache.add(imageView);
                }
            }
            return imageView;
        }
    
        /**
         * 创建一个TextView
         *
         * @param page
         * @param viewBean
         * @return
         */
        private static View createTextView(IPage page, ViewBean viewBean) {
            TextView tv = new TextView(page.getContext());
            try {//多个try保证某一个脏数据不会导致view整体加载失败
                tv.setTextColor(Color.parseColor(viewBean.getColor()));
            } catch (Exception e) {
                e.printStackTrace();
                tv.setTextColor(Color.parseColor("#333333"));
            }
            try {
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, viewBean.getSize());
            } catch (Exception e) {
                e.printStackTrace();
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14.0f);
            }
            if (!TextUtils.isEmpty(viewBean.getValue())) {
                List<View> viewCache = page.getViewCache();
                if (viewCache != null) {
                    viewCache.add(tv);
                }
            }
            tv.setText(viewBean.getContent());
            tv.setTag(viewBean);
            tv.setOnClickListener(v -> {
                ViewBean bean = (ViewBean) v.getTag();
                IAction action = ActionFactory.createAction(page, bean.getAction());
                action.action(bean.getAction());
            });
            return tv;
        }
    }
```

注意填充的过程中判断value是否存在，存在直接存到viewCache集合里，后面设置数据能用上。

createView的方法传入了一个IPage接口，这个接口是为了方便获取context上下文和viewCache集合：

```java
    public interface IPage {
    
        Context getContext();
    
        List<View> getViewCache();
    }
```

createTextView方法下设置了点击事件的监听，当点击的时候会触发Action，定义了IAction接口：

```java
    public interface IAction {
        void action(ActionBean action);
    }
```

在点击的时候拿到action数据，通过ActionFactory简单工厂生成对应的Action实体，先看一个简单的Toast怎么实现：

```java
    public class ToastAction implements IAction {
    
        private IPage page;
    
        public ToastAction(IPage page) {
            this.page = page;
        }
    
        @Override
        public void action(ActionBean action) {
            Toast.makeText(page.getContext(), action.getMsg(), Toast.LENGTH_SHORT).show();
        }
    }
```

这样整体App的Toast就都可以通过指定name = "toast"完成了。复杂一点的，比如请求Action之后，设置数据Action：

```java
    public class RequestAction implements IAction {
    
        private IPage page;
    
        public RequestAction(IPage page) {
            this.page = page;
        }
    
        @Override
        public void action(ActionBean action) {
            if (action != null) {
                try {
                    Thread.sleep(2_000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String response = "{\n" +
                        "      \"a\": \"这是通过请求获取的数据，真的！\"\n" +
                        "    }";
                String lastAction = action.getAction();
                ActionBean lastActionBean = JSONObject.parseObject(lastAction, ActionBean.class);
                lastActionBean.setResponse(response);
                ActionFactory.createAction(page, lastAction).action(lastActionBean);
            }
        }
    }
```

因为请求还要接入请求框架，直接模拟请求返回的数据了。执行设置数据Action的时候携带上请求回来的response：

```java
    public class SetDataAction implements IAction {
    
        private IPage page;
    
        public SetDataAction(IPage page) {
            this.page = page;
        }
    
        @Override
        public void action(ActionBean action) {
            if (action != null) {
                String response = action.getResponse();
                if (!TextUtils.isEmpty(response)) {
                    DataBean dataBean = JSONObject.parseObject(response, DataBean.class);
                    List<View> viewCache = page.getViewCache();
                    if (viewCache != null && viewCache.size() > 0) {
                        int size = viewCache.size();
                        for (int i = 0; i < size; i++) {
                            View view = viewCache.get(i);
                            bindViewData(view, dataBean);
                        }
                    }
                }
            }
        }
    
        /**
         * 绑定页面数据
         *
         * @param view
         * @param dataBean
         */
        private void bindViewData(View view, DataBean dataBean) {
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                ViewBean viewBean = (ViewBean) textView.getTag();
                String keyData = dataBean.getData(viewBean.getValue());
                textView.setText(keyData);
            } else if (view instanceof ImageView) {
                //TODO 
            }
        }
    }
```

不需要反复遍历根节点，就能获取到设置了value = "a"的TextView，再拿到DataBean的a值，绑定上就可以看到结果了。

复杂的ListView绑定也是可以实现的。








