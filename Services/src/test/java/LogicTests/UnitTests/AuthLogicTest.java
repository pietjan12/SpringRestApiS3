package LogicTests.UnitTests;

import com.kiddygambles.data.IAccountRepository;
import com.kiddygambles.domain.entities.Account;
import com.kiddygambles.services.AuthLogic;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthLogicTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Mock
    private IAccountRepository accountContext;

    @InjectMocks
    private AuthLogic _logic;

    @Before //sets up the mock
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void GetUserAlreadyExists() {
        Account dummyAccount = new Account(1,"bob");

        when(accountContext.findById(anyInt())).thenReturn(Optional.of(dummyAccount));

        UserDetails principal = _logic.loadUserByID(dummyAccount.getAccountID(), dummyAccount.getUsername());

        verify(accountContext, times(1)).findById(dummyAccount.getAccountID());
        Assert.assertNotNull(principal);
        Assert.assertEquals(dummyAccount.getUsername(), principal.getUsername());
    }

    @Test
    public void GetUserDidNotExistYet() {
        Account dummyAccount = new Account(1,"bob");

        when(accountContext.save(any(Account.class))).thenReturn(dummyAccount);

        UserDetails principal = _logic.loadUserByID(dummyAccount.getAccountID(), dummyAccount.getUsername());

        verify(accountContext, times(1)).save(any(Account.class));
        Assert.assertNotNull(principal);
        Assert.assertEquals(dummyAccount.getUsername(), principal.getUsername());
    }
}
