/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import bank.Bank;
import client.Client;
import javax.jms.JMSException;
import javax.naming.NamingException;
import loanbroker.LoanBroker;

/**
 *
 * @author Maarten
 */
public class main
{    
    public static void main(String[] args) throws JMSException, NamingException
    {
        String clientToBrokerQueue = "ClientToBrokerQueue";
        String brokerToClientQueue = "BrokerToClientQueue";
        String brokerToBankQueue = "BrokerToBankQueue";
        String bankToBrokerQueue = "BankToBrokerQueue";        
        Client client = new Client(clientToBrokerQueue, brokerToClientQueue);
        LoanBroker broker = new LoanBroker(clientToBrokerQueue, brokerToClientQueue, brokerToBankQueue, bankToBrokerQueue);
        Bank abn = new Bank(bankToBrokerQueue, brokerToBankQueue, "ABN");
        Bank rabo = new Bank(bankToBrokerQueue, brokerToBankQueue, "RABO");
        Bank ing = new Bank(bankToBrokerQueue, brokerToBankQueue, "ING");
    }
}
