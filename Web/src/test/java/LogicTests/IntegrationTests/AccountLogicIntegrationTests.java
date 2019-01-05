package LogicTests.IntegrationTests;

import com.kiddygambles.app;
import com.kiddygambles.data.IAccountRepository;
import com.kiddygambles.domain.entities.Account;
import com.kiddygambles.services.Interfaces.IAccountLogic;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {app.class})
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
public class AccountLogicIntegrationTests {
    @Autowired
    private IAccountRepository accountContext;

    @Autowired
    private IAccountLogic accountLogic;

    @Before
    public void setUp() {
        accountContext.save(new Account(1, "pieter"));
    }

    @Test
    public void testGetUserByID() {
        Account account = accountLogic.GetUser(1);

        Assert.assertNotNull(account);
        Assert.assertEquals("pieter", account.getUsername());
    }
}
