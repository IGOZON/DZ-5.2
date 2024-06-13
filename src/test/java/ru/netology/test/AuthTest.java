package ru.netology.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.data.DataGenerator.Registration.getUser;
import static ru.netology.data.DataGenerator.getRandomLogin;
import static ru.netology.data.DataGenerator.getRandomPassword;


class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Активный и зарегистрированный пользователь")
    void activeAndRegisteredUser() {
        var registeredUser = getRegisteredUser ("active");
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("button.button").click();
        $("h2").shouldHave(Condition.exactText("Личный кабинет")).shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Незарегистрированный пользователь")
    void notRegisteredUser() {
        var notRegisteredUser = getUser ("active");
        $("[data-test-id='login'] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id='password'] input").setValue(notRegisteredUser.getPassword());
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10))
                .shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Заблокированный пользотель")
    void BlockedUser() {
        var registeredUser = getRegisteredUser ("blocked");
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.exactText("Ошибка! Пользователь заблокирован"), Duration.ofSeconds(10))
                .shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Неверный логин")
    void invalidLogin() {
        var registeredUser = getRegisteredUser ("active");
        var wrongLogin = getRandomLogin();
        $("[data-test-id='login'] input").setValue(wrongLogin);
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10))
                .shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Неверный пароль")
    void invalidPassword() {
        var registeredUser = getRegisteredUser ("active");
        var wrongPassword = getRandomPassword();
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(wrongPassword);
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10))
                .shouldBe(Condition.visible);
    }
}


