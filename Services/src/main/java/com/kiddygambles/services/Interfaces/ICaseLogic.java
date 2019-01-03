package com.kiddygambles.services.Interfaces;

import com.kiddygambles.domain.DTO.CaseDTO;
import com.kiddygambles.domain.entities.Case;
import com.kiddygambles.domain.entities.CaseHistory;
import com.kiddygambles.domain.entities.Item;

import java.util.List;

public interface ICaseLogic {
    /**
     * Get case by id
     * @param id the id of the case
     * @return found case
     * @throws IllegalArgumentException if case could not be found
     */
    CaseDTO getCase(int id);

    /**
     * Get all cases
     * @return a list of cases
     */
    Iterable<Case>  getAllCases();

    /**
     * create a new case
     * @param caseName the name of the case
     * @param caseDescription the description of the case
     * @param price the cost of the case
     * @return the created case
     * @throws IllegalArgumentException if any of the parameters are missing
     */
    Case createCase(String caseName, String caseDescription, int price);

    /**
     * delete a case
     * @param caseID the id of the case you wish to delete.
     */
    void deleteCase(int caseID);

    /**
     * updates an existing case
     * @param caseID the id of the case you wish to update
     * @param caseName the name of the case
     * @param caseDescription the description of the case
     * @param price the cost of the case
     * @return the created case
     * @throws IllegalArgumentException if any of the parameters are missing
     * @throws IllegalArgumentException if case could not be found
     */
    Case updateCase(int caseID, String caseName, String caseDescription, int price);

    /**
     * Open a case
     * @param username the user who wants to open a case
     * @param caseID the case you wish to open
     * @return
     */
    CaseHistory openCase(String username, int caseID);
}
