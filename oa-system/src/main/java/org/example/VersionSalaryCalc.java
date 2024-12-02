package org.example;

import java.util.HashMap;
import java.util.Map;

public class VersionSalaryCalc {

    public static Map<String, Object> VERSION = new HashMap<>();

    public void init() throws Exception {
        // add jar版本
        SecurityClassLoader securityClassLoader = new SecurityClassLoader(FileWatcher.addSalary);
        Class<?> addVersion = securityClassLoader.loadClass("org.example.SalaryCalc");
        Object addSalary = addVersion.newInstance();
        VERSION.put("add", addSalary);

        // normal jar版本
        SecurityClassLoader securityClassLoader2 = new SecurityClassLoader(FileWatcher.normalSalary);
        Class<?> normalVersion = securityClassLoader2.loadClass("org.example.SalaryCalc");
        Object normalSalary = normalVersion.newInstance();
        VERSION.put("normal", normalSalary);

        // 引入的salary-system模块的版本
        VERSION.put("default", new SalaryCalc());

        // 猜测一下，这里能不能将class互相转换，比如
        // SalaryCalc salaryCalc = (SalaryCalc) addSalary;
        System.out.println("addSalary == normalSalary = " + addSalary == normalSalary);
    }

    public static Object getSalaryCalc() {
        String version = FileWatcher.getSalaryVersionConfig();
        return VERSION.get(version) == null ? VERSION.get("default") : VERSION.get(version);
    }
}
