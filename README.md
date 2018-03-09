# ObjectBox
## 说明 
* greenrobot公司出品，greenDao、EventBus同样出自该公司
* 区别于SQL，没有复杂的代码，没学习过SQL也不要紧，只要调用一些简单的API就能实现数据的增删改查
* ObjectBox是该公司针对性能提升新出的数据库，据官网称优于对比测试的所有嵌入型数据库5-15倍
* 跨平台使用
* ......
## 配置
1. 在项目的build文件中添加如下依赖
<pre><code>
buildscript {

    ext.objectboxVersion = '1.4.3'
    
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
    dependencies {
        ...
        classpath "io.objectbox:objectbox-gradle-plugin:$objectboxVersion"
    }
}
</pre></code>
2. 在module的build文件中添加如下依赖
<pre><code>
apply plugin: 'io.objectbox'

android {
    ...
    defaultConfig {
        ...
        //显示debug信息，以下这些代码可加可不加
        javaCompileOptions {
            annotationProcessorOptions {
                arguments = ['objectbox.debug': 'true']
            }
        }
    }
    ...
}
</pre></code>
## 生成实体类，先创建一个bean类（具体内部实现往下看），再Make Project一下（此步骤不能少）
bean类中主要是两个注解，@Entity和@Id
* @Entity	 这个对象需要持久化（加在bean类的最上方）
* @Id	 这个对象的主键（会自增长，按照一定格式，下面的实例会给出）
* @Index  这个对象中的索引（对经常大量进行查询的字段创建索引，会提高你的查询性能）
* @NameInDb	 有的时候数据库中的字段跟你的对象字段不匹配的时候，可以使用此注解
* @Transient  如果你有某个字段不想被持久化，可以使用此注解
* @Relation	 做一对多，多对一的注解
<pre><code>
@Entity
public class UserBean implements Serializable {
    @Id
    private long id;
    //其他自定义的参数
    ...
    //实现相应的get、set方法即可
}
</pre></code>
## 在Application中初始化BoxStore，并开放获取这个BoxStore的方法
<pre><code>
public class ObApplication extends Application {

    private static BoxStore boxStore;

    @Override
    public void onCreate() {
        super.onCreate();
        initObjectBox();
    }

    private void initObjectBox() {
        boxStore = MyObjectBox.builder().androidContext(this).build();
    }

    public static BoxStore getBoxStore() {
        return boxStore;
    }
}
</pre></code>
## 增删改查功能的实现
首先，需要在View中获取到BoxStore这个对象
<pre><code>
userBeanBox = ObApplication.getBoxStore().boxFor(UserBean.class);
</pre></code>
* 增和改语法相同
<pre><code>
//可以是一个实体类，也可以是个实体类的集合
 userBeanBox.put(userBean);
</pre></code>
* 删(可以是根据id删除表中某一条数据)
<pre><code>
userBeanBox.remove(id);
</pre></code>
也可以删除该表中全部数据
<pre><code>
userBeanBox.removeAll();
</pre></code>
* 查
<pre><code>
userBeanBox.query().equal(UserBean_.sex, sex).and()
                    .equal(UserBean_.name, name).and()
                    .equal(UserBean_.old, old)
                    .build().find();
</pre></code>
上面代码中的.and()是逻辑和的意思，也有.or()是逻辑或，.equal()是查找的条件，其中的参数分别是特征（以 类名_.参数 格式填写）和具体要查找的值，
.find()是查找符合条件的List，而.findFirst()是该List中的第一个。
