package com.xukunn.calculator.calc.parser;

/**
 * Created by xukun on 2017/12/22.
 */

public class DivExpParser extends ExpParser {
    public DivExpParser(Parser exp1, Parser exp2) {
        super(exp1, exp2);
    }

    @Override
    public double parse() {
        return exp1.parse() / exp2.parse();
    }
}
