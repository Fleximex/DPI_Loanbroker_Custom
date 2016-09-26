/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.loanbroker;

import java.util.ArrayList;
import java.util.Collections;
import model.bank.BankInterestReply;

/**
 *
 * @author Maarten
 */
public class Aggregator
{
    private int id;
    private ArrayList<BankInterestReply> bankInterestReplies;
    private int count = 0;

    public Aggregator()
    {
        this.bankInterestReplies = new ArrayList<>();
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }
    
    public void addReply(BankInterestReply bankInterestReply)
    {
        this.bankInterestReplies.add(bankInterestReply);
    }
    
    public void upCount()
    {
        this.count++;
    }
    
    public boolean allAnswered()
    {
        return this.bankInterestReplies.size() == count;
    }
    
    public BankInterestReply getBestInterest()
    {
        Collections.sort(bankInterestReplies);
        return bankInterestReplies.get(0);
    }
}
