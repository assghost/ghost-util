package com.ghostcat.poi.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ghostcat.poi.annotations.ExportCol;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ExportBean {
    @ExportCol("姓名")
    private String name;

    @ExportCol("性别")
    private String gender;

    @ExportCol("年龄")
    private Integer age;

    @ExportCol("生日")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDate birth;

    @ExportCol("已婚")
    private String isMarried;

    @ExportCol("收入")
    private BigDecimal money;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public LocalDate getBirth() {
        return birth;
    }

    public void setBirth(LocalDate birth) {
        this.birth = birth;
    }

    public String getIsMarried() {
        return isMarried;
    }

    public void setIsMarried(String married) {
        isMarried = married;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }
}
