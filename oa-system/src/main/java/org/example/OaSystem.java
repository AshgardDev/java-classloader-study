package org.example;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Hello world!
 */
public class OaSystem {

    public static void main(String[] args) throws Exception {
        // 系统预发放的工资
        Double salary = 2000.0;
        // 实际发放的工资
        Double money;
        // 每隔一段时间计算一次薪水
        while (true) {
            money = calcSalary(salary);
            System.out.println("预发工资：" + salary + ",实际到手的工资：" + money);
            Thread.sleep(2000);
        }
    }

    private static Double calcSalary(Double salary) throws Exception {
        File file = new File("/Users/hbj/Study/java-classloader-study/salary-system/target/salary-system-encry-1.0-SNAPSHOT.jar");
        URL salaryJarUrl = file.toURI().toURL();
        URL[] urls = new URL[]{salaryJarUrl};
        URLClassLoader classLoader = new URLClassLoader(urls);
        Class clazz = classLoader.loadClass("org.example.SalaryCalc");
        Object obj = clazz.newInstance();
        return (Double) clazz.getMethod("calc", Double.class).invoke(obj, salary);
    }
}
