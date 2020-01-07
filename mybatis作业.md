

#### 1、mybatis动态sql作用、有哪些动态sql、动态sql执行原理

大大减少了我们编写代码的工作量；

if 、choose、when、otherwise trim、where、set 、 foreach；

![image-20200107002239238](/Users/videopls/Library/Application Support/typora-user-images/image-20200107002239238.png)

以这个sql为例：

<update id="update" parameterType="org.format.dynamicproxy.mybatis.bean.User">
    UPDATE users
    <trim prefix="SET" prefixOverrides=",">
        <if test="name != null and name != ''">
            name = #{name}
        </if>
        <if test="age != null and age != ''">
            , age = #{age}
        </if>
        <if test="birthday != null and birthday != ''">
            , birthday = #{birthday}
        </if>
    </trim>
    where id = ${id}
</update>

parseDynamicTags方法的返回值是一个List，也就是一个Sql节点集合。SqlNode本文一开始已经介绍，分析完解析过程之后会说一下各个SqlNode类型的作用。

1 首先根据update节点(Node)得到所有的子节点，分别是3个子节点

(1)文本节点 \n UPDATE users

(2)trim子节点 ...

(3)文本节点 \n where id = #{id}

2 遍历各个子节点

(1) 如果节点类型是文本或者CDATA，构造一个TextSqlNode或StaticTextSqlNode

(2) 如果节点类型是元素，说明该update节点是个动态sql，然后会使用NodeHandler处理各个类型的子节点。这里的NodeHandler是XMLScriptBuilder的一个内部接口，其实现类包括TrimHandler、WhereHandler、SetHandler、IfHandler、ChooseHandler等。看类名也就明白了这个Handler的作用，比如我们分析的trim节点，对应的是TrimHandler；if节点，对应的是IfHandler...

这里子节点trim被TrimHandler处理，TrimHandler内部也使用parseDynamicTags方法解析节点

3 遇到子节点是元素的话，重复以上步骤

trim子节点内部有7个子节点，分别是文本节点、if节点、是文本节点、if节点、是文本节点、if节点、文本节点。文本节点跟之前一样处理，if节点使用IfHandler处理

遍历步骤如上所示，下面我们看下几个Handler的实现细节。

IfHandler处理方法也是使用parseDynamicTags方法，然后加上if标签必要的属性。

#### 2、mybatis是否支持延迟加载、延迟加载原理

##### 是；

Mybatis仅支持association关联对象和collection关联集合对象的延迟加载，association指的就是一对一，collection指的就是一对多查询。在Mybatis配置文件中，可以配置是否启用延迟加载lazyLoadingEnabled=true|false。

它的原理是，使用CGLIB创建目标对象的代理对象，当调用目标方法时，进入拦截器方法，比如调用a.getB().getName()，拦截器invoke()方法发现a.getB()是null值，那么就会单独发送事先保存好的查询关联B对象的sql，把B查询上来，然后调用a.setB(b)，于是a的对象b属性就有值了，接着完成a.getB().getName()方法的调用。这就是延迟加载的基本原理。

#### 3、mybatis的Executor执行器有哪些、区别

SimpleExecutor：每执行一次update或select，就开启一个Statement对象，用完立刻关闭Statement对象。

ReuseExecutor：执行update或select，以sql作为key查找Statement对象，存在就使用，不存在就创建，用完后，不关闭Statement对象，而是放置于Map内，供下一次使用。简言之，就是重复使用Statement对象。

BatchExecutor：执行update（没有select，JDBC批处理不支持select），将所有sql都添加到批处理中（addBatch()），等待统一执行（executeBatch()），它缓存了多个Statement对象，每个Statement对象都是addBatch()完毕后，等待逐一执行executeBatch()批处理。与JDBC批处理相同。

作用范围：Executor的这些特点，都严格限制在SqlSession生命周期范围内。

#### 4、简述mybatis一二级缓存

一级缓存也叫sqlSession级别的缓存 ，也就是在同一个sqlSession内执行两次多次相同结果的查询语句，只会在第一次时发     出sql查询数据库的数据，然后之后每次从一级缓存中查询数据返回。

二级缓存是mapper级别的缓存，也就是多个sqlSession之间可以实现数据的共享，二级缓存默认是不开启。

#### 5、简述mybatis插件原理、如何写一个插件

###### 插件原理

在四大对象创建的时候
1、每个创建出来的对象不是直接返回的，而是 interceptorChain.pluginAll(parameterHandler);
2、获取到所有的Interceptor（拦截器）（插件需要实现的接口）；调用interceptor.plugin(target);返回target包装后的对象
3、插件机制，我们可以使用插件为目标对象创建一个代理对象；AOP（面向切面）我们的插件可以为四大对象创建出代理对象；代理对象就可以拦截到四大对象的每一个执行；

###### 如何实现一个插件

1、实现Interceptor接口方法

2、确定拦截的签名

3、在配置文件中配置插件

##### 

