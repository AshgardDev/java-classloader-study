package org.example;

import java.io.File;

/**
 * Hello world!
 */
public class OaSystem {

    public static void main(String[] args) throws Exception {
        // 系统预发放的工资
        Double salary = 2000.0;
        // 实际发放的工资
        Double money;
        // 为了提升效率，只加载一次
        // SecurityClassLoader securityClassLoader = new SecurityClassLoader(file);

        // 启动文件更新监听线程
        FileWatcher.reloadConfig();
        FileWatcher.start();

        // 每隔一段时间计算一次薪水
        while (true) {
            // money = calcSalary(salary, securityClassLoader);
            money = calcSalary(salary);
            System.out.println("预发工资：" + salary + ",实际到手的工资：" + money);
            Thread.sleep(2000);
        }
    }

    private static Double calcSalary(Double salary, SecurityClassLoader securityClassLoader) throws Exception {
        // 通过正常的URLClassLoader加载
        /*
        URL salaryJarUrl = file.toURI().toURL();
        URL[] urls = new URL[]{salaryJarUrl};
        // 加密后这里获取用原来URLClassLoader加载jar会失败，需要重写类加载器，新建SecurityClassLoader，并实现
        URLClassLoader classLoader = new URLClassLoader(urls);
        Class clazz = classLoader.loadClass("org.example.SalaryCalc");
        Object obj = clazz.newInstance();
        return (Double) clazz.getMethod("calc", Double.class).invoke(obj, salary);
         */

        // 通过自定义的加密的类加载器加载
        Class<?> clazz = securityClassLoader.loadClass("org.example.SalaryCalc");
        Object obj = clazz.newInstance();
        return (Double) clazz.getMethod("calc", Double.class).invoke(obj, salary);
    }

    // 为了更好实现热加载，方法变更
    private static Double calcSalary(Double salary) throws Exception {
        File file = FileWatcher.getSalaryVersion();
        // 通过自定义的加密的类加载器加载,且每次都生成新的，这样就能每次都生成新的类加载器，每次都会加载一遍
        SecurityClassLoader securityClassLoader = new SecurityClassLoader(file);
        Class<?> clazz = securityClassLoader.loadClass("org.example.SalaryCalc");
        Object obj = clazz.newInstance();
        return (Double) clazz.getMethod("calc", Double.class).invoke(obj, salary);
    }

}
