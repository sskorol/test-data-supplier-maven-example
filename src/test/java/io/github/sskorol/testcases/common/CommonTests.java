package io.github.sskorol.testcases.common;

import io.github.sskorol.dataprovider.DataSupplier;
import io.github.sskorol.model.User;
import io.github.sskorol.pages.LoginPage;
import io.qameta.allure.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Stream;

import static io.github.sskorol.core.PageFactory.open;
import static java.util.Arrays.asList;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;

public class CommonTests {

    @DataSupplier
    public User getData() {
        return new User("dummyUsername", "dummyPassword");
    }

    @DataSupplier
    public Stream<User> getStreamData() {
        return Stream.of(
                new User("dummyUsername2", "dummyPassword2"),
                new User("dummyUsername1", "dummyPassword1"))
                     .sorted(comparing(User::getUsername));
    }

    @DataSupplier
    public List<String> getCollectionData() {
        return asList("badUsername1", "badUsername2");
    }

    @DataSupplier(extractValues = true)
    public String[] getArrayData(final Method method) {
        return Stream.of(method.getName(), "badPassword1", "badPassword2").toArray(String[]::new);
    }

    @BeforeMethod
    public void prepareEnvironment(final Method method) {
        retrieveData(method);
    }

    @AfterMethod
    public void cleanUpEnvironment(final Method method) {
        cleanUpData(method);
    }

    @Issue("21")
    @Feature("Authorization")
    @Story("ALR-23")
    @TmsLink("24")
    @Owner("sskorol")
    @Severity(SeverityLevel.CRITICAL)
    @Test(dataProvider = "getData")
    public void loginWithRegisteredUser(final User user) {
        open(LoginPage.class)
                .loginWith(user);
    }

    @Test
    @Flaky
    @Owner("baev")
    @Severity(SeverityLevel.BLOCKER)
    public void shouldFailOnAssertion() {
        assertThat(true).as("Dummy status").isFalse();
    }

    @Test
    @Flaky
    @Owner("sskorol")
    @Severity(SeverityLevel.MINOR)
    public void shouldThrowUnhandledException() {
        throw new IllegalArgumentException("Unable to parse some value");
    }

    @Feature("Authorization")
    @Story("ALR-24")
    @Severity(SeverityLevel.NORMAL)
    @Test(dataProvider = "getCollectionData")
    public void shouldFailOnSubStep(final String username) {
        open(LoginPage.class)
                .typeUsername(username)
                .cancelPopup();
    }

    @Flaky
    @Feature("Authorization")
    @Story("ALR-25")
    @Severity(SeverityLevel.MINOR)
    @Test(dataProvider = "getArrayData")
    public void shouldFailOnAssertionSubStep(final String... passwords) {
        assertThat(passwords[0])
                .isEqualTo("shouldFailOnAssertionSubStep");

        open(LoginPage.class)
                .typePassword(Stream.of(passwords).collect(joining(",")))
                .isPopupDisplayed();
    }

    @Feature("Payments")
    @Story("ALR-26")
    @TmsLink("25")
    @Issue("27")
    @Severity(SeverityLevel.BLOCKER)
    @Test(dataProvider = "getStreamData")
    public void shouldDisplayUserBalance(final User user) {
        open(LoginPage.class)
                .loginWith(user)
                .displayBalance();
    }

    @Step("Retrieve DB data for {method.name}.")
    private void retrieveData(final Method method) {
        // not implemented
    }

    @Step("Cleanup DB data for {method.name}.")
    private void cleanUpData(final Method method) {
        // not implemented
    }
}
