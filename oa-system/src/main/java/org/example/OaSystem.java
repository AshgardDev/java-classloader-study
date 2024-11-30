package org.example;

/**
 * Hello world!
 */
public class OaSystem {

    public static void main(String[] args) throws InterruptedException {
        // 系统预发放的工资
        Double salary = 2000.0;
        // 实际发放的工资
        Double money;
        // 每隔一段时间计算一次薪水
        while (true) {
            money = new SalaryCalc().calc(salary);
            System.out.println("实际到手的工资：" + money);
            Thread.sleep(2000);
        }
    }
}
