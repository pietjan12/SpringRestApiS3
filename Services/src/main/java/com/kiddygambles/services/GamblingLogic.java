package com.kiddygambles.services;

import com.kiddygambles.data.IAccountRepository;
import com.kiddygambles.data.ICaseRepository;
import com.kiddygambles.domain.entities.Case;
import com.kiddygambles.domain.entities.Item;
import com.kiddygambles.domain.entities.WinHistory;
import com.kiddygambles.services.Helper.LootRollHelper;
import com.kiddygambles.services.Helper.RestCallHelper;
import com.kiddygambles.services.Interfaces.IGamblingLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.json.Json;
import java.util.Optional;

import static com.kiddygambles.services.Constants.KiddyAPIConstants.inventoryURL;

public class GamblingLogic implements IGamblingLogic {
    private ICaseRepository caseContext;
    private IAccountRepository accountContext;
    private RestCallHelper restCallHelper;
    private LootRollHelper lootRollHelper;

    @Autowired
    public GamblingLogic(ICaseRepository caseContext, IAccountRepository accountContext, RestCallHelper restCallHelper, LootRollHelper lootRollHelper) {
        this.caseContext = caseContext;
        this.accountContext = accountContext;
        this.restCallHelper = restCallHelper;
        this.lootRollHelper = lootRollHelper;
    }

    @Override
    public WinHistory openCase(String username, String token, int caseID) throws IllegalArgumentException {
        //case ophalen en lootroll doen om item te bepalen.
        Case caseToOpen = getCaseByID(caseID);
        double lootRoll = lootRollHelper.getRandomDoubleRoll(0,100);
        //TODO : GEEN IDEE HOE IK DIT WIL DOEN, ITEMS ZIJN PERCENTAGE BASED EN KAN DUS NIET ZO GETTEN
        Item item = caseToOpen.getItems().get(1);

        WinHistory winHistory = new WinHistory(item, lootRoll);

        //save winning details to case as history
        //caseToOpen.getHistory().add(winHistory);
        //winHistory.setWonCase(caseToOpen);
        caseContext.save(caseToOpen);

        //Save item to winning account
        saveItemToAccount(token, item.getItemID());

        //return winningdetails to user.
        return winHistory;
    }

    private Case getCaseByID(int caseID) {
        Optional<Case> foundCase = caseContext.findById(caseID);

        if(!foundCase.isPresent()) {
            throw new IllegalArgumentException("caseID invalid");
        }
        //case ophalen en lootroll doen om item te bepalen.T
        return foundCase.get();
    }

    /**
     * save item to an account
     * @param token  the token taken from the request header
     * @param itemID the item you want to save to the user
     * @return nothing, an exception with a error message is thrown if anything does not add up
     * @throws RuntimeException  if something went wrong server side
     */
    private void saveItemToAccount(String token, int itemID) {
        //create json string to send as data
        String jsonData = Json.createObjectBuilder()
                .add("itemID", itemID)
                .build().toString();

        ResponseEntity<String> saveItemCall = restCallHelper.makePostRestCall(inventoryURL + "/inventory/add", token, jsonData);

        if(saveItemCall.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Something went wrong on our end, please contact support!");
        }
    }
}
