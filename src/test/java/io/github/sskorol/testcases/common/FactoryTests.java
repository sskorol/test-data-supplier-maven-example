package io.github.sskorol.testcases.common;

import io.github.sskorol.core.DataSupplier;
import io.github.sskorol.model.User;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

@Slf4j
@NoArgsConstructor
public class FactoryTests {

    @Factory(dataProvider = "getConstructorData", dataProviderClass = FactoryTests.class)
    public FactoryTests(final User user) {
        log.info("Factory constructor: {}", user);
    }

    @DataSupplier
    public User getData() {
        return new User("dummyUsername", "dummyPassword");
    }

    @DataSupplier
    public User getConstructorData() {
        return new User("factoryUsername", "factoryPassword");
    }

    @Test(dataProvider = "getData")
    public void shouldDisplayFactoryUser(final User user) {
        log.info("Factory test: {}", user);
    }
}
