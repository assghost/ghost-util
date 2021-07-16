package com.ghostcat.common.util;

import org.springframework.util.ObjectUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GhostStringUtils {

    private static String PARAMS_REGEX = "\\#\\{(.+?)\\}";

    private static Pattern PARAMS_PATTERN = Pattern.compile(PARAMS_REGEX);

    private static ScriptEngineManager scriptEngineManager = new ScriptEngineManager();

    /**
     * 获取模板中的变量名列表
     * @param template
     * @return
     */
    public static List<String> parseTemplate(String template) {

        List<String> params = new ArrayList<>();

        if (null != template) {

            template = template.trim();

            if (!template.isEmpty()) {
                Matcher matcher = PARAMS_PATTERN.matcher(template);

                while (matcher.find()) {
                    //group(0) 匹配正则表达式整体结果
                    params.add(matcher.group());
                }
            }
        }

        return params;
    }

    /**
     * 将模板中的变量替换为对应值
     * @param template
     * @param params
     * @return
     */
    public static String formatTemplate(String template, Map<String, Object> params) {

        if (null != template) {
            template = template.trim();
            if (!template.isEmpty()) {

                StringBuffer sb = new StringBuffer();
                Matcher matcher = PARAMS_PATTERN.matcher(template);

                while (matcher.find()) {
                    String key = matcher.group(1);
                    Object value = params.get(key);

                    String valueStr = " ";
                    if (null != value) {
                        valueStr = String.valueOf(value);
                    }
                    matcher.appendReplacement(sb, valueStr);
                }
                matcher.appendTail(sb);

                return sb.toString();
            }
        }

        return template;
    }

    /**
     * 将变量代入公式进行计算，返回计算结果
     * @param template
     * @param params
     * @return
     * @throws ScriptException
     */
    public static String calcFormula(String template, Map<String, Object> params) throws ScriptException {

        String formual = formatTemplate(template, params);

        ScriptEngine engine = scriptEngineManager.getEngineByName("js");

        return engine.eval(formual).toString();
    }


    /**
     * 将集合转为逗号分隔的字符串
     * @param collection
     * @return
     */
    public static String convertList2Str(Collection collection) {
        StringBuilder sb = new StringBuilder();

        if (ObjectUtils.isEmpty(collection)) {
            for (Object item : collection) {
                if (sb.length() > 0) {
                    sb = sb.append(",");
                }
                sb = sb.append(item);
            }
        }

        return sb.toString();
    }

    /**
     * 将逗号分隔的字符串转为list
     * @param str
     * @return
     */
    public static List<String> convertStr2List(String str) {
        List<String> list = new ArrayList<>();

        if (!ObjectUtils.isEmpty(str)) {
            String[] items = str.split(",");

            for (String item : items) {
                list.add(item);
            }
        }

        return list;
    }

//    public static void main(String[] args) {
//
//        String str = "error #{name} time #{date} thread #{id}";
//
//        Map<String, Object> map = new HashMap<>(3);
//        map.put("name", "Ethan");
//        map.put("date", new Date());
//        map.put("id", 1234556);
//
//
//        System.out.println(formatTemplate(str, map));
//        System.out.println(str);
//    }
}
