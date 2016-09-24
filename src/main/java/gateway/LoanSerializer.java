/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gateway;

import com.google.gson.Gson;
import model.loan.LoanReply;
import model.loan.LoanRequest;

/**
 *
 * @author Maarten
 */
public class LoanSerializer
{
    private final Gson gson;

    public LoanSerializer()
    {
        gson = new Gson();
    }
    
    public String requestToString(LoanRequest loanRequest)
    {
        return gson.toJson(loanRequest);
    }
    
    public LoanRequest requestFromString(String requestString)
    {
        return gson.fromJson(requestString, LoanRequest.class);
    }
    
    public String replyToString(LoanReply loanReply)
    {
        return gson.toJson(loanReply);
    }
    
    public LoanReply replyFromString(String replyString)
    {
        return gson.fromJson(replyString, LoanReply.class);
    }
}
