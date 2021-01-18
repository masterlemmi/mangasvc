package com.lemoncode.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class ChapterValueValidator implements ConstraintValidator<AcceptableChapter, String> {

    private static String regex = "(\\b(https?|ftp|file)://)?[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";
    private static Pattern p = Pattern.compile(regex);


    @Override
    public void initialize(AcceptableChapter constraintAnnotation) {


    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintContext) {

        return isValid(value);


    }

    public static boolean isValid(String value) {
        if (value == null)
            return false;

        if (value.equalsIgnoreCase("latest"))
            return true;


        return p.matcher(value).matches() && value.contains("chapter");
    }

}