package com.xukunn.calculator.calc.parser;

/**
 * 表达式解析器
 * Created by xukun on 2017/12/22.
 */

public abstract class ExpParser implements Parser {
    Parser exp1, exp2;

    public ExpParser(Parser exp1, Parser exp2) {
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

}

