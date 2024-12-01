package org.example;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Hello world!
 */
public class OaSystem {

    private static String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIAQg9QOQppogL4cctdeO+XEQ/R18Iy0FDX2nRLVwYlEUck90rekLXyMKSvdsrMISFxmIP4vuA7yE1Ty9fbEimtI0LF65wPqDjkLwntSPu/uVp9nDlKtijo0oV0vCM94scNjwI2Yvt8TCYE0Id2gQ+F+kbc7X/Bj1lK4DyLvuCftAgMBAAECgYB1Xi1jiFo0Jzhug6YgicW9c12QSxLV8ShguI8GNw9znUCTfeyDz5y8e7wz7rAa8qlWvWbZbqRyVhuvjCguK7xq/iNUJN1BK0edlICxnC1vIcIDQxpH4bmZVhp/kf+QTcBx4coSwgcbAnq9eTeJB/9VJd4HEZLS3KMn6FXN1SMDQQJBAM3o+pOBB+0dJP2LEdYCO9fe0DAUbTQ9OGy4eoaL0jTVV+r9uylCxnWDPqtVfj5HI02BRkDIGI3oCvlqGiUTJt0CQQCfN7bv6rNG7P3SzOL5sLXhntet0QfSo3gxwfblrhExmT2fUSa7kME85+lCOZTntBWxj12KbO30WIARRdIOiIxRAkAE+tp8kMVRTcAkRaXDyEAMQ35De2r8tXJU5s1Hzb/iuTosG8DYCZ5lHyx31lR8SxomeLbrQCuf2vnQ+GjzdpgdAkEAkPY0HCovQhNCYmxi931Zi+uIaVwY6EIni/4Sojmis+rWvTgwsLNyDNcFfGNa3L8MxVZHj2HwIPl22MMeJldSkQJAVJhGhHOdxOWnOsw5bZM4Io//xW2fLvaA1BnFd2PbEUZ+KCWlcfAsDZDDVh8Pqz8Conl+6YqFOrPCHZk6zn8bJg==";

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
        SecurityClassLoader securityClassLoader = new SecurityClassLoader(file);
        Class<?> clazz = securityClassLoader.loadClass("org.example.SalaryCalc");
        Object obj = clazz.newInstance();
        return (Double) clazz.getMethod("calc", Double.class).invoke(obj, salary);
    }
}
