package com.example.tongxin.utils;

import com.example.tongxin.entity.Comment;
import com.example.tongxin.entity.Post;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by djs on 2017/5/1.
 */

public class StringUtils {
    /**
     * 检验邮箱格式是否正确
     * @param target
     * @return
     */
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches();
        }
    }

    /**
     * 计算帖子发布与当前的时间差
     * @param date
     * @return
     */
    public static String timeDiff(Date date){
        String ret = "";
        try{
            Date now = new Date();

            long s = (now.getTime() - date.getTime()) / 1000;
            long count = 0;
            if((count = s / (3600 * 24)) > 0){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                ret = sdf.format(date);
                return ret;
            }else if((count = s / 3600) > 0){
                ret = count + "小时前";
            }else if((count = s / 60) > 0){
                ret = count + "分钟前";
            }else{
                ret = "刚刚";
            }
        }catch (Exception e) {
        }
        return ret;
    }

    public static Date string2date(String s){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date=sdf.parse(s);
            return date;
        }catch (ParseException e){

        }
        return null;
    }
}
