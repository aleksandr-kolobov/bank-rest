package com.example.bankcards.config;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApplicationConstant {

    public static final String HEADER_KEY_USER_ID = "User-Id";

    public static final String HEADER_KEY_USER_ROLES = "User-Roles";

    public static final String BEARER = "Bearer ";

    public static final String REGEXP_EMAIL = "^(?=.{8,50}$)(?=[^@]{2,30}@)(?!.*[._-]{2})(?!.*[@._-]$)" +
            "(?!.*[@._-]{2})[a-zA-Z0-9]+(?:[._-][a-zA-Z0-9]+)*@[a-zA-Z0-9-]{2,}\\.[a-z]{2,}$";

    public static final String REGEXP_PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$";

    public static final String REGEXP_NAME = "^[А-Яа-яЁёA-Za-z-]+$";

    public static final String MESSAGE_INCORRECT_NAME = "В имени только буквы!";

    public static final String MESSAGE_INCORRECT_EMAIL = "Некорректный формат почты!";

    public static final String MESSAGE_NO_PARAMETER_AVAILABLE = "Отсутствует параметр!";

    public static final String MESSAGE_PASSWORD_DOES_NOT_SATISFY_REQUIREMENTS =
            "Пароль должен содержать минимум одну заглавную букву, одну строчную букву и одну цифру";

    public static final String MESSAGE_PASSWORD_LENGTH_DOES_NOT_REQUIREMENTS = "Длина пароля от 8 до 64 символов";

}
