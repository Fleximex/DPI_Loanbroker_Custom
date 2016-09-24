package model.loan;

/**
 *
 * This class stores all information about a bank offer
 * as a response to a client loan request.
 */
public class LoanReply {

    private int id;
        private double interest; // the interest that the bank offers
        private String bankID; // the unique quote identification

    public LoanReply() {
        super();
        this.id = 0;
        this.interest = 0;
        this.bankID = "";
    }
    public LoanReply(int id, double interest, String bankID) {
        super();
        this.id = id;
        this.interest = interest;
        this.bankID = bankID;
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

    public String getBankID() {
        return bankID;
    }

    public void setBankID(String bankID) {
        this.bankID = bankID;
    }
    
    @Override
    public String toString(){
        return  " bankID="+String.valueOf(bankID) + " interest="+String.valueOf(interest);
    }
}
