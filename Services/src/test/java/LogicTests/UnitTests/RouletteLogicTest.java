package LogicTests.UnitTests;

import com.kiddygambles.data.IAccountRepository;
import com.kiddygambles.domain.entities.Account;
import com.kiddygambles.domain.entities.GameHistory;
import com.kiddygambles.services.Helper.LootRollHelper;
import com.kiddygambles.services.RouletteLogic;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RouletteLogicTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Mock
    private IAccountRepository accountContext;
    @Mock
    private LootRollHelper lootRollHelper;

    @InjectMocks
    private RouletteLogic _logic;

    @Before //sets up the mock
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void PlayRouletteSufficientBalanceWon() {
        Account dummyAccount = new Account(1, "bob");
        dummyAccount.setTokens(1000);

        when(accountContext.findByUsername(anyString())).thenReturn(Optional.of(dummyAccount));
        when(accountContext.save(any(Account.class))).thenReturn(dummyAccount);
        when(lootRollHelper.getRandomIntRoll(anyInt(), anyInt())).thenReturn(11);

        GameHistory gameHistory = _logic.playRoulette(dummyAccount.getUsername(), 100, null, "Black");

        verify(accountContext, times(1)).save(dummyAccount);

        Assert.assertEquals(gameHistory.getRolledNumber(), 11);
        Assert.assertEquals(gameHistory.getWonTokens(), 200);
        Assert.assertEquals(gameHistory.isWon(), true);
    }

    @Test
    public void PlayRouletteSufficientBalanceLost() {
        Account dummyAccount = new Account(1, "bob");
        dummyAccount.setTokens(1000);

        when(accountContext.findByUsername(anyString())).thenReturn(Optional.of(dummyAccount));
        when(accountContext.save(any(Account.class))).thenReturn(dummyAccount);
        when(lootRollHelper.getRandomIntRoll(anyInt(), anyInt())).thenReturn(2);

        GameHistory gameHistory = _logic.playRoulette(dummyAccount.getUsername(), 100, null, "Red");

        verify(accountContext, times(1)).save(dummyAccount);

        Assert.assertEquals(gameHistory.getRolledNumber(), 2);
        Assert.assertEquals(gameHistory.getWonTokens(), 0);
        Assert.assertEquals(gameHistory.isWon(), false);
    }

    @Test
    public void PlayRouletteInsufficientBalance() {
        Account dummyAccount = new Account(1, "bob");

        exception.expect(IllegalArgumentException.class);
        when(accountContext.findByUsername(anyString())).thenReturn(Optional.of(dummyAccount));

        _logic.playRoulette(dummyAccount.getUsername(), 100, null,"Black");
    }
}
