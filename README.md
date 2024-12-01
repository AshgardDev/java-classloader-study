# java-classloader-study
通过图灵的程序员修改工资例子学习类加载器

# 1.类加载器机制
网上很多，不赘述，基本双亲委派机制
Bootstrap classloader -- JAVA_HOME/lib
Ext classloader -- jre/lib/ext
App classloader -- classpath路径下

类加载机制：
1.每个类加载器对他加载的类都一个缓存
2.向上委托查找，向下委托加载

# 2.类的加载过程：
![img.png](img.png)
步骤看图，这里想说的是类加载有个方法
classloader.loadClass(className, resolve)
resolve其实对应的就是上图绿色部份，值false代表不验证解析了，也就是有风险。

# 3.故事背景
有一oa系统，每个月都要定时计算程序元工资。
OaSystem 代表oa系统
SalaryCalc 代表工资计算系统

# 3.1 有一个程序员想要修改工资计算方法，偷偷加工资
他就想给工资加2成，直接在SalaryCalc上 salary*1.2
肯定会被发现，想个办法，抽出到一个jar包里，不再oa系统代码里

那怎么读取jar呢，肯定不能直接引入module了，不然还是能看到源代码，要单独引入jar，并读取jar内容
那就要采用类加载器加载jar包了

# 3.2 jar包可以反编译，需要加密
jar方式虽然隐蔽，但也是可以被反编译的，需要混淆一下
所以在生成salary jar的时候，需要对class文件做一下加密
比如可以修改文件后缀，添加混淆代码等
这里简单演示一下，请看class-encry模块，对生成的salary-system.jar执行ClassEntry方法进行加密，生成salary-system-encry-1.0.jar
PS:这里要先把oa-system模块中引入的salary-system模块移除！

# 3.3 开发自定义的类加载器，实现解密功能
其实就是实现SecurityClassLoader，继承SecureClassLoader，并实现loadClass重载方法
具体实现看代码
这样子，整套薪资的逻辑就被加密到jar包中，切不能反编译查看，还能正常解密加载，实现薪资计算功能。

# 3.4 要是公司突然要核算工资，必须要把代码改回去，怎么办？
需要准备另外一个jar : salary-system-encry-2.0.jar, 所有jar包如下：
salary-system-encry-1.0.jar  -- 偷偷加工资
salary-system-encry-2.0.jar  -- 正常发工资
然后快速替换，怎么快速替换呢？ 
使用文件变化监听，来根据配置选择哪个版本
如果重启oa-system动作太大了，每次这么做很快就会被发现的，怎么办？需要热加载
可是，又出现一个问题，类加载是有缓存的，如果一个类加载过了，就不会再加载了，怎么办？需要新建一个类加载器，重新加载
让我们来实现代码！

最终效果：
预发工资：2000.0,实际到手的工资：2400.0
预发工资：2000.0,实际到手的工资：2400.0
预发工资：2000.0,实际到手的工资：2400.0
预发工资：2000.0,实际到手的工资：2400.0
预发工资：2000.0,实际到手的工资：2400.0
预发工资：2000.0,实际到手的工资：2400.0
config.properties has changed. Reloading...
Configuration reloaded:
预发工资：2000.0,实际到手的工资：2000.0
预发工资：2000.0,实际到手的工资：2000.0
预发工资：2000.0,实际到手的工资：2000.0
预发工资：2000.0,实际到手的工资：2000.0

上述效果是实现了，可是这样一直new类加载器的方式，会导致出现大量的没用垃圾类，这也是热更新带来的问题。


