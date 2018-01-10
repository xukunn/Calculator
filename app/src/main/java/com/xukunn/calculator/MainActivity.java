package com.xukunn.calculator;

import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xukunn.calculator.calc.Calc;
import com.xukunn.calculator.util.StringUtil;
import com.xukunn.calculator.util.ToastUtils;
import com.xukunn.calculator.util.ViewUtil;
import com.xukunn.calculator.widget.LastInputEditText;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    public static final String Max = "999999.99";

    private EditText etPhone;
    private EditText etExpression;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        afterViews();
    }

    private void assignViews() {

        findViewById(R.id.tv7).setOnClickListener(inputListener);
        findViewById(R.id.tv8).setOnClickListener(inputListener);
        findViewById(R.id.tv9).setOnClickListener(inputListener);
        findViewById(R.id.tv4).setOnClickListener(inputListener);
        findViewById(R.id.tv5).setOnClickListener(inputListener);
        findViewById(R.id.tv6).setOnClickListener(inputListener);
        findViewById(R.id.tv1).setOnClickListener(inputListener);
        findViewById(R.id.tv2).setOnClickListener(inputListener);
        findViewById(R.id.tv3).setOnClickListener(inputListener);
        findViewById(R.id.tv0).setOnClickListener(inputListener);
        findViewById(R.id.tv00).setOnClickListener(inputListener);
        findViewById(R.id.tvPoint).setOnClickListener(inputListener);
        findViewById(R.id.plus).setOnClickListener(inputListener);
        findViewById(R.id.minus).setOnClickListener(inputListener);
        findViewById(R.id.multi).setOnClickListener(inputListener);
        findViewById(R.id.divide).setOnClickListener(inputListener);

        findViewById(R.id.delete).setOnClickListener(funcClickListerner);
        findViewById(R.id.calculate).setOnClickListener(funcClickListerner);
        findViewById(R.id.reset).setOnClickListener(funcClickListerner);

        etPhone = (LastInputEditText) findViewById(R.id.et_phone);
        etExpression = (LastInputEditText) findViewById(R.id.et_expression);
        tvResult = (TextView) findViewById(R.id.tv_result);
    }


    private boolean cursorOnMoney = true;

    protected void afterViews() {
        assignViews();
        ViewUtil.disableShowSoftInput(etExpression);
        ViewUtil.disableShowSoftInput(etPhone);
        etExpression.requestFocus();

        etExpression.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                cursorOnMoney = true;
                return false;
            }
        });
        etPhone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                cursorOnMoney = false;
                return false;
            }
        });


    }



    private View.OnClickListener inputListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView textview = (TextView) v;
            String text = textview.getText().toString().trim();
            if (cursorOnMoney) {
                appendMoney(text);
            } else {
                appendPhone(text);
            }

        }
    };

    private View.OnClickListener funcClickListerner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText et = cursorOnMoney ? etExpression : etPhone;
            int viewId = v.getId();
            if (viewId == R.id.delete) {
                String trim = et.getText().toString().trim();
                if (TextUtils.isEmpty(trim)) {
                    return;
                }
                et.setText(trim.substring(0, trim.length() - 1));
                calculate();
            } else if (viewId == R.id.calculate) {
                ToastUtils.showLongToast(MainActivity.this, "调接口");
            } else if (viewId == R.id.reset) {
                et.setText("");
                if (cursorOnMoney) {
                    tvResult.setText("0.00");
                }
            } else {

            }

        }
    };

    private void calculate() {
        double value = Calc.eval(etExpression.getText().toString().trim());
        tvResult.setText(formatMoney(value));
    }

    private void appendMoney(String text) {
        Editable exp = etExpression.getText();
        String expStr = exp.toString();
        if (Pattern.compile("^[\\s\\S]*[0-9]+$").matcher(expStr).matches()) {//数字结尾
            //小数点的校验 0.9 0.99 0.9+9
            if (Pattern.compile("^[\\s\\S]*\\.[0-9]+$").matcher(expStr).matches()) {//0.9
                if (StringUtil.contains(text, ".,00")) {
                    return;
                }
            }
            if (Pattern.compile("^[\\s\\S]*\\.[0-9]{2}$").matcher(expStr).matches()) {//8.09
                if (StringUtil.contains(text, "0,1,2,3,4,5,6,7,8,9,.,00")) {
                    return;
                }
            }

        } else if (Pattern.compile("^[\\s\\S]*[\\+\\-\\÷\\×]+$").matcher(expStr).matches()) {//运算符结尾
            if (StringUtil.contains(text, "+,-,÷,×,.,00")) {
                return;
            }

        } else if (Pattern.compile("^[\\s\\S]*[\\.]+$").matcher(expStr).matches()) {//.结尾
            if (StringUtil.contains(text, "+,-,÷,×,.")) {
                return;
            }

        } else {//null
            if (StringUtil.contains(text, "+,-,÷,×,.,00")) {
                return;
            }
        }
        //预算
        boolean beyond = beyondMax(expStr + text);
        if (beyond) {
            return;
        }
        exp.append(text);
        calculate();

    }

    private boolean beyondMax(String string) {
        double value = Calc.eval(string);
        if (Double.isInfinite(value) || Double.isNaN(value)) {//除零处理
            return false;
        }
        BigDecimal result = new BigDecimal(value);
        BigDecimal max = new BigDecimal(Max);

        return (result.compareTo(max) > 0);
    }

    private void appendPhone(String text) {
        Editable exp = etPhone.getText();
        //输入校验
        String phone = exp.toString();
        if (StringUtil.contains(text, ".,00,+,-,÷,×,C")) {
            return;
        }
        if (phone.length() >= 11) {
            return;
        }
        exp.append(text);
    }

    public String formatMoney(double value) {
        if (Double.isInfinite(value) || Double.isNaN(value)) {//除零处理
            return "不能除0";
        }
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format(new BigDecimal(value));
    }
}
