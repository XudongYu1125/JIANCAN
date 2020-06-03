package com.example.user.jiancan.home.util;

import java.math.BigDecimal;

/**
 * @author june
 */
public class FormatNum {


    /**
     * 格式化数据函数，超过1000显示为1K，超过10000显示1W。
     * @param num 需要转换的数字，需将int通过String.valueOf()转为String类型。
     * */
    public static String formatBigNum( String num ) {

        try {
            StringBuffer sb = new StringBuffer ( );
            BigDecimal b0 = new BigDecimal ("1000");
            BigDecimal b1 = new BigDecimal ("10000");
            BigDecimal b3 = new BigDecimal (num);

            //输出结果
            String formatNum = "";
            //单位
            String unit = "";

            if (b3.compareTo (b0) == -1) {
                sb.append (b3.toString ( ));
            } else if ((b3.compareTo (b0) == 0 || b3.compareTo (b0) == 1)
                    || b3.compareTo (b1) == -1) {
                formatNum = b3.divide (b0).toString ( );
                unit = "k";
            } else if ((b3.compareTo (b1) == 0 && b3.compareTo (b1) == 1)) {
                formatNum = b3.divide (b1).toString ( );
                unit = "w";
            }
            if (!"".equals (formatNum)) {
                int i = formatNum.indexOf (".");
                if (i == -1) {
                    sb.append (formatNum).append (unit);
                } else {
                    i = i + 1;
                    String v = formatNum.substring (i , i + 1);
                    if (!v.equals ("0")) {
                        sb.append (formatNum.substring (0 , i + 1)).append (unit);
                    } else {
                        sb.append (formatNum.substring (0 , i - 1)).append (unit);
                    }
                }
            }
            if (sb.length ( ) == 0) {
                return "0";
            }
            return sb.toString ( );

        } catch (Exception e) {
            e.printStackTrace ( );
            return num;
        }
    }

}
