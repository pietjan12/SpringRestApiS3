package com.kiddygambles.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kiddygambles.data.IAccountRepository;
import com.kiddygambles.data.ICaseHistoryRepository;
import com.kiddygambles.data.ICaseRepository;
import com.kiddygambles.domain.DTO.CaseDTO;
import com.kiddygambles.domain.DTO.CaseHistoryDTO;
import com.kiddygambles.domain.DTO.ItemDTO;
import com.kiddygambles.domain.Enum.Rarity;
import com.kiddygambles.domain.entities.Account;
import com.kiddygambles.domain.entities.Case;
import com.kiddygambles.domain.entities.CaseHistory;
import com.kiddygambles.domain.entities.Item;
import com.kiddygambles.services.Helper.LootRollHelper;
import com.kiddygambles.services.Helper.RestCallHelper;
import com.kiddygambles.services.Helper.TokenHelper;
import com.kiddygambles.services.Interfaces.ICaseLogic;
import org.assertj.core.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.json.Json;
import java.util.*;
import java.util.stream.Collectors;

import static com.kiddygambles.services.Constants.KiddyAPIConstants.inventoryURL;

@Service
public class CaseLogic implements ICaseLogic {
    //repos
    private ICaseHistoryRepository caseHistoryContext;
    private ICaseRepository caseContext;
    private IAccountRepository accountContext;

    //Helper classes for common functions
    private RestCallHelper restCallHelper;
    private LootRollHelper lootRollHelper;
    private TokenHelper tokenHelper;

    @Autowired
    public CaseLogic(ICaseHistoryRepository caseHistoryContext, ICaseRepository caseContext, IAccountRepository accountContext, TokenHelper tokenHelper, RestCallHelper restCallHelper, LootRollHelper lootRollHelper) {
        this.caseHistoryContext = caseHistoryContext;
        this.caseContext = caseContext;
        this.accountContext = accountContext;
        this.tokenHelper = tokenHelper;
        this.restCallHelper = restCallHelper;
        this.lootRollHelper = lootRollHelper;
    }

    @Override
    public CaseDTO getCase(int id){
        Case myCase = checkCaseExists(id);

        Map<Integer, ItemDTO> itemDTOMap = new HashMap<>();
        List<CaseHistory> recentHistory;
        List<CaseHistoryDTO> history = new ArrayList<>();
        List<ItemDTO> items = new ArrayList<>();

        //Get more details of items within case
        for(Item i : myCase.getItems()) {
            ResponseEntity<ItemDTO> item = restCallHelper.makeGetRestCall(inventoryURL + "/item/" + i.getItemID(), ItemDTO.class);
            ItemDTO itemDTO = item.getBody();
            itemDTO.setRarity(i.getRarity());
            items.add(itemDTO);
            //Add item to a hashmap for faster lookup
            itemDTOMap.put(i.getItemID(), itemDTO);
        }

        //get recent history from list with a limit of 10
        recentHistory = myCase.getHistory().subList(Math.max(myCase.getHistory().size() - 12, 0), myCase.getHistory().size());
        //reverse list so the newest item is first
        Collections.reverse(recentHistory);

        for(CaseHistory ch : recentHistory) {
            //find itemDTO equivalent for item
            ItemDTO item = itemDTOMap.get(ch.getItem().getItemID());
            CaseHistoryDTO caseHistoryDTO = new CaseHistoryDTO(ch.getId(), ch.getRolledNumber(), item);
            history.add(caseHistoryDTO);
        }

        return new CaseDTO(myCase.getId(), myCase.getName(), myCase.getDescription(), myCase.getImage(), myCase.getPrice(), items, history);
    }

    @Override
    public List<Case> getAllCases(){
        List<Case> allCases = caseContext.findAll();
        //Controleren of lijst niet leeg is
        if(allCases.size() <= 0) {
            throw new NullPointerException("No cases found");
        }

        return allCases;
    }

    @Override
    public Case createCase(String caseName, String caseDescription, int price) {
        checkInput(caseName, caseDescription, price);

        Case caseToCreate = new Case(caseName, caseDescription, price);
        return caseContext.save(caseToCreate);
    }

    @Override
    public void deleteCase(int caseID) {
        caseContext.delete(checkCaseExists(caseID));
    }

    @Override
    public Case updateCase(int caseID, String caseName, String caseDescription, int price) {
        checkInput(caseName, caseDescription, price);

        Case foundCase = checkCaseExists(caseID);
        foundCase.setName(caseName);
        foundCase.setDescription(caseDescription);
        foundCase.setPrice(price);

        return caseContext.save(foundCase);
    }

    @Override
    public CaseHistory openCase(String username, int caseID) {
        //check if case exists
        Case caseToOpen = checkCaseExists(caseID);

        //check if user has enough tokens to open case
        tokenHelper.hasEnoughTokens(username, caseToOpen.getPrice());

        //lootroll doen om item te bepalen.
        double lootRoll = lootRollHelper.getRandomDoubleRoll(0,100);
        Item winningItem = generateWinningItem(lootRoll, caseToOpen.getItems());

        //create winhistory
        CaseHistory winHistory = new CaseHistory(winningItem, lootRoll);

        //save winning details to case as history
        caseToOpen.getHistory().add(winHistory);
        winHistory.setWonCase(caseToOpen);
        caseHistoryContext.save(winHistory);

        //Save item to winning account
        saveItemToAccount(username, winningItem.getItemID());

        //remove tokens from account if everything went correctly
        tokenHelper.removeTokens(username, caseToOpen.getPrice());

        //return winningdetails to user.
        return winHistory;
    }

    //================================================================================
    // PRIVATE METHODS
    //================================================================================
    private void saveItemToAccount(String username, int itemID) {
        Account account = checkAccountExists(username);

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode myWonItem = mapper.createObjectNode();
        myWonItem.put("itemID", itemID);
        myWonItem.put("accountID", account.getAccountID());

        restCallHelper.makePostRestCall(inventoryURL + "/inventory", myWonItem.toString());
    }

    private Account checkAccountExists(String username) {
        Optional<Account> foundAccount = accountContext.findByUsername(username);

        if(!foundAccount.isPresent()) {
            throw new NullPointerException("Account with username + " + username + " Not found");
        }

        return foundAccount.get();
    }

    private void checkInput(String caseName, String caseDescription, int price) {
        if(Strings.isNullOrEmpty(caseName) || Strings.isNullOrEmpty(caseDescription) || price <= 0) {
            throw new IllegalArgumentException("Case parameters name, description and items cannot be null!");
        }
    }

    private Case checkCaseExists(int caseID) {
        Optional<Case> foundCase = caseContext.findById(caseID);

        if(!foundCase.isPresent()) {
            throw new NullPointerException("Case with id + " + String.valueOf(caseID) + " Not found");
        }

        return foundCase.get();
    }

    private Item generateWinningItem(double rolledNumber, List<Item> caseItems) {
        Rarity rarity;

        if(rolledNumber >= 90) {
            //generate LEGENDARY item
            rarity = Rarity.LEGENDARY;
        } else if(rolledNumber >= 75) {
            //generate RARE item
            rarity = Rarity.RARE;
        } else if(rolledNumber >= 50) {
            //generate UNCOMMON item
            rarity = Rarity.UNCOMMON;
        } else {
            //generate COMMON item
            rarity = Rarity.COMMON;
        }

        List<Item> itemsWithRarity = caseItems.stream().filter(i -> i.getRarity() == rarity).collect(Collectors.toList());
        return itemsWithRarity.get(lootRollHelper.getRandomIntRoll(0, itemsWithRarity.size() - 1));
    }
}
