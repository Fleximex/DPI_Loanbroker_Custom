package model.bank;

import java.util.Random;

/**
 * This class stores information about the bank reply
 *  to a loan request of the specific client
 * 
 */
public class BankInterestReply {

    private int id;
    private double interest; // the loan interest
    private String bankId; // the nunique quote Id
    
    public BankInterestReply()
    {
        this.id = 0;
        this.interest = 0;
        this.bankId = "";
    }
    
    public BankInterestReply(int id, double interest, String bankId) {
        this.id = id;
        this.interest = interest;
        this.bankId = bankId;
    }

    public int getId() {
        return id;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String toString() {
        return " bankID=" + this.bankId + " interest=" + this.interest;
    }
}
