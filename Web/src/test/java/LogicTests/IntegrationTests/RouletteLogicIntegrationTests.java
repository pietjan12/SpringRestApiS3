package LogicTests.IntegrationTests;

import com.kiddygambles.app;
import com.kiddygambles.data.IAccountRepository;
import com.kiddygambles.domain.entities.Account;
import com.kiddygambles.domain.entities.GameHistory;
import com.kiddygambles.services.Interfaces.IRouletteLogic;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
public class RouletteLogicIntegrationTests {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Autowired
    private IAccountRepository accountContext;

    @Autowired
    private IRouletteLogic rouletteLogic;

    @Before
    public void setUp() {
        Account account = new Account(1, "pieter");
        account.setTokens(1000);
        accountContext.save(account);
    }

    @Test
    public void testPlayRouletteValid() {
        Account account = accountContext.findByUsername("pieter").get();
        GameHistory gameHistory = rouletteLogic.playRoulette("pieter", 100, null, "Black");

        Assert.assertNotNull(gameHistory);
        Assert.assertNotEquals(1000, account.getTokens());
    }

    @Test
    public void testPlayRouletteInsufficientFunds() {
        exception.expect(IllegalArgumentException.class);

        rouletteLogic.playRoulette("pieter", 10000, null, "Black");
    }
}
