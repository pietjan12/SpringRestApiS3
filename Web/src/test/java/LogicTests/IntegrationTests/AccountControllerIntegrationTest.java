package LogicTests.IntegrationTests;

import com.kiddygambles.app;
import com.kiddygambles.data.IAccountRepository;
import com.kiddygambles.domain.entities.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {app.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class AccountControllerIntegrationTest {
    @Autowired
    IAccountRepository accountContext;

    @Autowired
    TestRestTemplate template = new TestRestTemplate();

    @Before
    public void setUp() {
        accountContext.save(new Account(1, "testrole"));

        //Auth token van testrole meegeven.
        //Aangezien ik niet veel controller tests zal maken is het onlogisch om twee varianten van spring security te maken (een test profiel een normaal profiel)
        template.getRestTemplate().setInterceptors(
            Collections.singletonList((request, body, execution) -> {
                request.getHeaders()
                        .add("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0cm9sZSIsInNjb3BlcyI6WyJBRE1JTiJdLCJ1c2VySUQiOjM0fQ.LZEMgHYrpupGJw3WD_jICwu0MqBLVfwWdTq9TLIMY8njJsL46sEeDEhl88pGC4XrB06xfsyp3g-DN6uKjxR7WA");
                return execution.execute(request, body);
            })
        );
    }

    @Test
    public void GetAccountByID() {
        ResponseEntity<Account> receivedAccount = template.getForEntity("/accounts/1", Account.class);
        Account account = receivedAccount.getBody();

        Assert.assertNotNull(account);
        Assert.assertEquals(account.getUsername(), "testrole");
        Assert.assertEquals(receivedAccount.getStatusCode(), HttpStatus.OK);
    }
}
