package LogicTests.UnitTests;

import com.kiddygambles.data.IAccountRepository;
import com.kiddygambles.domain.entities.Account;
import com.kiddygambles.services.AccountLogic;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.security.Principal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountLogicTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Mock
    private IAccountRepository accountContext;

    @InjectMocks
    private AccountLogic _logic;

    @Before //sets up the mock
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findUserExists() {
        Account dummyAccount = new Account(1,"bob");

        when(accountContext.findById(anyInt())).thenReturn(Optional.of(dummyAccount));

        Account returnedAccount = _logic.GetUser(dummyAccount.getAccountID());

        verify(accountContext, times(1)).findById(dummyAccount.getAccountID());
        Assert.assertEquals(returnedAccount.getUsername(), dummyAccount.getUsername());
        Assert.assertEquals(returnedAccount.getAccountID(), dummyAccount.getAccountID());
    }

    @Test
    public void findUserDoesNotExist() {
        exception.expect(IllegalArgumentException.class);

        when(accountContext.findById(anyInt())).thenReturn(Optional.empty());

        _logic.GetUser(1);
    }
}
