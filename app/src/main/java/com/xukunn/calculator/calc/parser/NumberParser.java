package com.xukunn.calculator.calc.parser;

/**
 * Created by xukun on 2017/12/22.
 */

public class NumberParser implements Parser {
    double num;

    public NumberParser(double num) {
        this.num = num;
    }

    @Override
    public double parse() {
        return num;
    }
}
