package org.example;

/**
 * Hello world!
 */
public class SalaryCalc implements SalaryCalcInterface {

    @Override
    public Double calc(Double salary) {
        // 明目张单加
        return salary * 1.2;
    }
}
