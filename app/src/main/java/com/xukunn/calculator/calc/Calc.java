package com.xukunn.calculator.calc;

import android.text.TextUtils;
import android.util.Log;


import com.xukunn.calculator.calc.parser.AddExpParser;
import com.xukunn.calculator.calc.parser.DivExpParser;
import com.xukunn.calculator.calc.parser.MulExpParser;
import com.xukunn.calculator.calc.parser.NumberParser;
import com.xukunn.calculator.calc.parser.Parser;
import com.xukunn.calculator.calc.parser.SubExpParser;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

/**
 * 计算器
 * Created by xukun on 2017/12/22.
 */

public class Calc {
    private static final String TAG = "Calc";

    public static final String add = "+";
    public static final String sub = "-";
    public static final String div = "÷";
    public static final String mul = "×";

    /**
     * 23
     * 23.45
     * 0
     * 12+
     * 34+
     * 1+23.02
     * 表达式求值
     */
    public static double eval(String str) {


        double result = 0;
        if (TextUtils.isEmpty(str)) {
            return result;
        }
        //空格处理
        String formatStr = str.trim();
        formatStr = formatStr.replace(" ", "");
        //去掉最后的运算符
        if (formatStr.endsWith(add) || formatStr.endsWith(sub) || formatStr.endsWith(div) || formatStr.endsWith(mul)) {
            formatStr = formatStr.substring(0, formatStr.length() - 1);
        }
        //两个运算符连着的情况
        if (formatStr.endsWith(add) || formatStr.endsWith(sub) || formatStr.endsWith(div) || formatStr.endsWith(mul)) {
            return Double.NaN;
        }
        //格式化表达式
        formatStr = formatStr.replace(add, "," + add + ",");
        formatStr = formatStr.replace(sub, "," + sub + ",");
        formatStr = formatStr.replace(div, "," + div + ",");
        formatStr = formatStr.replace(mul, "," + mul + ",");

        Log.i(TAG, "eval: str" + str + ",formatStr:" + formatStr);
        //纯数字,不用计算
        if (!formatStr.contains(add) && !formatStr.contains(sub) && !formatStr.contains(div) && !formatStr.contains(mul)) {
            return Double.parseDouble(formatStr);
        }
        //计算
        String[] split = formatStr.split(",");
        Parser parser1, parser2;//运算符左右两边的值
        //第一步,优先级,先计算乘除
        Deque<String> deque = new ArrayDeque<>();
        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            if (add.equals(s) || sub.equals(s)) {
                deque.addLast(s);
            } else if (mul.equals(s)) {
                String str1 = deque.removeLast();
                parser1 = new NumberParser(Double.parseDouble(str1));
                parser2 = new NumberParser(Double.parseDouble(split[++i]));
                double temp = new MulExpParser(parser1, parser2).parse();
                deque.addLast(new Double(temp).toString());
            } else if (div.equals(s)) {
                String str1 = deque.removeLast();
                parser1 = new NumberParser(Double.parseDouble(str1));
                parser2 = new NumberParser(Double.parseDouble(split[++i]));
                double temp = new DivExpParser(parser1, parser2).parse();
                deque.addLast(new Double(temp).toString());
            } else {
                deque.addLast(s);
            }
        }
        //第二步,计算加减
        Stack<Parser> stack = new Stack<>();

        while (!deque.isEmpty()) {
            String s = deque.removeFirst();
            if (add.equals(s)) {
                parser1 = stack.pop();
                parser2 = new NumberParser(Double.parseDouble(deque.removeFirst()));
                stack.push(new AddExpParser(parser1, parser2));
            } else if (sub.equals(s)) {
                parser1 = stack.pop();
                parser2 = new NumberParser(Double.parseDouble(deque.removeFirst()));
                stack.push(new SubExpParser(parser1, parser2));
            } else if (mul.equals(s)) {
                parser1 = stack.pop();
                parser2 = new NumberParser(Double.parseDouble(deque.removeFirst()));
                stack.push(new MulExpParser(parser1, parser2));
            } else if (div.equals(s)) {
                parser1 = stack.pop();
                parser2 = new NumberParser(Double.parseDouble(deque.removeFirst()));
                stack.push(new DivExpParser(parser1, parser2));
            } else {
                stack.push(new NumberParser(Double.parseDouble(s)));
            }
        }
        result = stack.pop().parse();

        return result;
    }


}
