package LogicTests.UnitTests;

import com.kiddygambles.data.IAccountRepository;
import com.kiddygambles.domain.entities.Account;
import com.kiddygambles.services.Helper.RestCallHelper;
import com.kiddygambles.services.TokenLogic;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TokenLogicTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Mock
    private IAccountRepository accountContext;

    @Mock
    private RestCallHelper restCallHelper;

    @InjectMocks
    private TokenLogic _logic;

    @Before //sets up the mock
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void BuyTokenSufficientBalance() {
        ResponseEntity<String> dummyResponse = new ResponseEntity<>("", HttpStatus.OK);
        Account dummyAccount = new Account(1, "bob");

        when(accountContext.findByUsername(anyString())).thenReturn(Optional.of(dummyAccount));
        when(accountContext.save(dummyAccount)).thenReturn(dummyAccount);
        when(restCallHelper.makePostRestCall(anyString(), anyString())).thenReturn(dummyResponse);

        _logic.buyToken(dummyAccount.getUsername(),10, 1000);

        verify(accountContext, times(1)).save(dummyAccount);
        Assert.assertEquals(dummyAccount.getTokens(), 1000);


    }

    @Test
    public void BuyTokenInsufficientBalance() {
        ResponseEntity<String> dummyResponse = new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        Account dummyAccount = new Account(1, "bob");

        exception.expect(IllegalArgumentException.class);
        when(accountContext.findByUsername(anyString())).thenReturn(Optional.of(dummyAccount));
        when(restCallHelper.makePostRestCall(anyString(), anyString())).thenReturn(dummyResponse);

        _logic.buyToken(dummyAccount.getUsername(),10, 1000);
    }

    @Test
    public void BuyTokenInvalidParameters() {
        Account dummyAccount = new Account(1, "");

        exception.expect(IllegalArgumentException.class);
        when(accountContext.findByUsername("bob")).thenReturn(Optional.of(dummyAccount));

        _logic.buyToken(dummyAccount.getUsername(), 10, 1000);
    }
}
