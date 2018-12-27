package com.kiddygambles.services;

import com.kiddygambles.data.IAccountRepository;
import com.kiddygambles.data.ICaseRepository;
import com.kiddygambles.domain.entities.Case;
import com.kiddygambles.domain.entities.CaseHistory;
import com.kiddygambles.domain.entities.Item;
import com.kiddygambles.services.Helper.LootRollHelper;
import com.kiddygambles.services.Helper.RestCallHelper;
import com.kiddygambles.services.Interfaces.ICaseLogic;
import org.assertj.core.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.json.Json;
import java.util.List;
import java.util.Optional;

import static com.kiddygambles.services.Constants.KiddyAPIConstants.inventoryURL;

@Service
public class CaseLogic implements ICaseLogic {
    private ICaseRepository caseContext;
    private IAccountRepository accountContext;
    private RestCallHelper restCallHelper;
    private LootRollHelper lootRollHelper;

    @Autowired
    public CaseLogic(ICaseRepository caseContext, IAccountRepository accountContext, RestCallHelper restCallHelper, LootRollHelper lootRollHelper) {
        this.caseContext = caseContext;
        this.accountContext = accountContext;
        this.restCallHelper = restCallHelper;
        this.lootRollHelper = lootRollHelper;
    }

    @Override
    public Case getCase(int id){
        return checkCaseExists(id);
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
    public Iterable<CaseHistory> getWinHistory(int caseID) {
        Case myCase = checkCaseExists(caseID);
        return myCase.getHistory();
    }

    @Override
    public CaseHistory openCase(String username, int caseID) {
        //case ophalen en lootroll doen om item te bepalen.
        Case caseToOpen = getCase(caseID);
        double lootRoll = lootRollHelper.getRandomDoubleRoll(0,100);
        //TODO : GEEN IDEE HOE IK DIT WIL DOEN, ITEMS ZIJN PERCENTAGE BASED EN KAN DUS NIET ZO GETTEN
        Item item = caseToOpen.getItems().get(1);

        CaseHistory winHistory = new CaseHistory(item, lootRoll);

        //save winning details to case as history
        //caseToOpen.getHistory().add(winHistory);
        //winHistory.setWonCase(caseToOpen);
        caseContext.save(caseToOpen);

        //Save item to winning account
        saveItemToAccount(item.getItemID());

        //return winning details to user.
        return winHistory;
    }

    private void saveItemToAccount(int itemID) {
        //create json string to send as data
        String jsonData = Json.createObjectBuilder()
                .add("itemID", itemID)
                .build().toString();

        restCallHelper.makePostRestCall(inventoryURL + "/inventory/add", jsonData);
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
}
