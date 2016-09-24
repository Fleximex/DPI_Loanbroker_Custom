/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gateway;

import com.google.gson.Gson;
import model.bank.BankInterestReply;
import model.bank.BankInterestRequest;
import model.loan.LoanReply;
import model.loan.LoanRequest;

/**
 *
 * @author Maarten
 */
public class BankSerializer
{
    private final Gson gson;

    public BankSerializer()
    {
        gson = new Gson();
    }
    
    public String requestToString(BankInterestRequest bankInterestRequest)
    {
        return gson.toJson(bankInterestRequest);
    }
    
    public BankInterestRequest requestFromString(String requestString)
    {
        return gson.fromJson(requestString, BankInterestRequest.class);
    }
    
    public String replyToString(BankInterestReply bankInterestReply)
    {
        return gson.toJson(bankInterestReply);
    }
    
    public BankInterestReply replyFromString(String replyString)
    {
        return gson.fromJson(replyString, BankInterestReply.class);
    }
}
