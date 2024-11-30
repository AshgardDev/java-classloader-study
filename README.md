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





