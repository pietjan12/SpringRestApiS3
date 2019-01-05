package LogicTests.UnitTests;

import com.kiddygambles.data.IAccountRepository;
import com.kiddygambles.data.ICaseHistoryRepository;
import com.kiddygambles.data.ICaseRepository;
import com.kiddygambles.domain.DTO.CaseDTO;
import com.kiddygambles.domain.DTO.ItemDTO;
import com.kiddygambles.domain.Enum.Rarity;
import com.kiddygambles.domain.entities.Account;
import com.kiddygambles.domain.entities.Case;
import com.kiddygambles.domain.entities.CaseHistory;
import com.kiddygambles.domain.entities.Item;
import com.kiddygambles.services.CaseLogic;
import com.kiddygambles.services.Helper.LootRollHelper;
import com.kiddygambles.services.Helper.RestCallHelper;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.kiddygambles.services.Constants.KiddyAPIConstants.inventoryURL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
public class CaseLogicTest {
    //Add exception handler
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    //Mock repos
    @Mock
    private ICaseHistoryRepository caseHistoryContext;
    @Mock
    private ICaseRepository caseContext;
    @Mock
    private IAccountRepository accountContext;
    //Mock helpers
    @Mock
    private RestCallHelper restCallHelper;
    @Mock
    private LootRollHelper lootRollHelper;

    @InjectMocks
    private CaseLogic _logic;

    @Before //sets up the mock
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void TestGetCaseExists() {
        //setup dummy items
        Case dummyCase = new Case("bob", "mooi", 10);
        Item dummyItem = new Item(1, Rarity.LEGENDARY);
        CaseHistory dummyCaseHistory = new CaseHistory(dummyItem, 94.1);
        dummyCase.getHistory().add(dummyCaseHistory);
        dummyCase.getItems().add(dummyItem);
        //Dummy response entity from external API
        ResponseEntity<ItemDTO> dummyResponse = new ResponseEntity<>(new ItemDTO(1, "mooi item", 10), HttpStatus.OK);

        when(caseContext.findById(anyInt())).thenReturn(Optional.of(dummyCase));
        when(restCallHelper.makeGetRestCall(inventoryURL + "/item/1", ItemDTO.class)).thenReturn(dummyResponse);

        CaseDTO returnedCase = _logic.getCase(1);
        verify(caseContext, times(1)).findById(1);
        verify(restCallHelper, times(1)).makeGetRestCall(inventoryURL + "/item/1", ItemDTO.class);

        Assert.assertEquals(dummyCase.getName(), returnedCase.getName());
        Assert.assertEquals(dummyCase.getPrice(), returnedCase.getPrice());
        Assert.assertEquals(dummyCase.getItems().get(0).getItemID(), returnedCase.getItems().get(0).getItemID());
    }

    @Test
    public void TestGetCaseDoesNotExist() {
        exception.expect(NullPointerException.class);

        when(caseContext.findById(anyInt())).thenReturn(Optional.empty());

        _logic.getCase(1);
    }

    @Test
    public void TestGetAllCases() {
        List<Case> dummyCasesList = new ArrayList<>();
        dummyCasesList.add(new Case("bob", "mooi", 10));
        dummyCasesList.add(new Case("piet","prachtig", 20));

        when(caseContext.findAll()).thenReturn(dummyCasesList);

        List<Case> returnedList = _logic.getAllCases();

        Assert.assertEquals(returnedList.size() ,dummyCasesList.size());
    }

    @Test
    public void CreateCaseValid() {
        Case dummyCase = new Case("bob", "mooi", 10);

        when(caseContext.save(any(Case.class))).thenReturn(dummyCase);

        Case returnedCase = _logic.createCase("bob", "mooi",10);

        verify(caseContext, times(1)).save(any(Case.class));
        Assert.assertEquals(dummyCase.getName(), returnedCase.getName());
    }

    @Test
    public void CreateCaseInvalid() {
        exception.expect(IllegalArgumentException.class);

        _logic.createCase("bob", "mooi", 0);
    }

    @Test
    public void DeleteCaseValid() {
        Case dummyCase = new Case("bob","mooi",10);

        when(caseContext.findById(anyInt())).thenReturn(Optional.of(dummyCase));

        _logic.deleteCase(1);
    }

    @Test
    public void DeleteCaseInvalid() {
        exception.expect(NullPointerException.class);

        when(caseContext.findById(anyInt())).thenReturn(Optional.empty());

        _logic.deleteCase(1);
    }

    @Test
    public void UpdateCaseValid() {
        Case dummyCase = new Case("bob","mooi",10);

        when(caseContext.findById(anyInt())).thenReturn(Optional.of(dummyCase));
        when(caseContext.save(any(Case.class))).thenReturn(dummyCase);

        Case returnedCase = _logic.updateCase(1, "piet", "mooi", 10);

        Assert.assertEquals(dummyCase.getId(), returnedCase.getId());
        Assert.assertNotEquals("bob", returnedCase.getName());
    }

    @Test
    public void UpdateCaseInvalid() {
        exception.expect(NullPointerException.class);

        when(caseContext.findById(anyInt())).thenReturn(Optional.empty());

        _logic.updateCase(1,"piet","mooi",10);
    }

    @Test
    public void OpenCaseEnoughTokens() {
        Account dummyAccount = new Account(1, "pieter");
        dummyAccount.setTokens(1000);
        Case dummyCase = new Case("bob","mooi",10);
        Item dummyItem = new Item(1, Rarity.LEGENDARY);
        dummyCase.getItems().add(dummyItem);
        CaseHistory caseHistoryDummy = new CaseHistory(dummyItem, 94.0);
        ResponseEntity<String> dummyResponse = new ResponseEntity<>("",HttpStatus.OK);

        when(caseContext.findById(anyInt())).thenReturn(Optional.of(dummyCase));
        when(accountContext.findByUsername(anyString())).thenReturn(Optional.of(dummyAccount));
        when(accountContext.save(any(Account.class))).thenReturn(dummyAccount);
        when(lootRollHelper.getRandomDoubleRoll(anyInt(),anyInt())).thenReturn(94.0);
        when(caseHistoryContext.save(any(CaseHistory.class))).thenReturn(caseHistoryDummy);
        when(restCallHelper.makePostRestCall(anyString(), anyString())).thenReturn(dummyResponse);

        CaseHistory returnedCaseHistory = _logic.openCase(dummyAccount.getUsername(), 1);

        verify(caseHistoryContext, times(1)).save(any(CaseHistory.class));
        Assert.assertEquals(caseHistoryDummy.getItem(), returnedCaseHistory.getItem());
        Assert.assertEquals(caseHistoryDummy.getRolledNumber(), returnedCaseHistory.getRolledNumber());
    }

    @Test
    public void OpenCaseInsufficientTokens() {
        Account dummyAccount = new Account(1, "pieter");
        dummyAccount.setTokens(0);
        Case dummyCase = new Case("bob","mooi",10);

        exception.expect(IllegalArgumentException.class);

        when(caseContext.findById(anyInt())).thenReturn(Optional.of(dummyCase));
        when(accountContext.findByUsername(anyString())).thenReturn(Optional.of(dummyAccount));

        _logic.openCase(dummyAccount.getUsername(), 1);
    }


}
