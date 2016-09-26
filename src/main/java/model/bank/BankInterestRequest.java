package model.bank;

/**
 *
 * This class stores all information about an request from a bank to offer
 * a loan to a specific client.
 */
public class BankInterestRequest {

    private int id;
    private int amount; // the requested loan amount
    private int time; // the requested loan period
    private String bank = "";

    public BankInterestRequest() {
        super();
        this.id = 0;
        this.amount = 0;
        this.time = 0;
    }

    public BankInterestRequest(int id, int amount, int time) {
        super();
        this.id = id;
        this.amount = amount;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }
    
    @Override
    public String toString() {
        return " amount=" + amount + " time=" + time;
    }
}
