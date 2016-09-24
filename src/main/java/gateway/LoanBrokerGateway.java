/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gateway;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.naming.NamingException;
import model.bank.BankInterestReply;
import model.bank.BankInterestRequest;
import model.loan.LoanReply;
import model.loan.LoanRequest;

/**
 *
 * @author Maarten
 */
public abstract class LoanBrokerGateway
{
    private final MessageSenderGateway sender;
    private final MessageReceiverGateway receiver;
    private LoanSerializer serializerClient;
    private BankSerializer serializerBank;

    public LoanBrokerGateway(String senderQueue, String receiverQueue) throws JMSException, NamingException
    {
        serializerClient = new LoanSerializer();
        serializerBank = new BankSerializer();
        
        sender = new MessageSenderGateway(senderQueue);
        sender.openConnection();
        
        receiver = new MessageReceiverGateway(receiverQueue);
        receiver.setReceivedMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message msg)
            {                
                try
                {
                    if(msg.getJMSDestination().toString().toLowerCase().contains("client"))
                    {
                        TextMessage m = (TextMessage) msg;
                        LoanReply loanReply = serializerClient.replyFromString(m.getText());
                        onLoanReplyArrived(loanReply);
                    }
                    else if(msg.getJMSDestination().toString().toLowerCase().contains("bank"))
                    {
                        TextMessage m = (TextMessage) msg;
                        BankInterestRequest bankInterestRequest = serializerBank.requestFromString(m.getText());
                        onBankInterestRequestArrived(bankInterestRequest);
                    }                    
                    
                }
                catch (JMSException ex)
                {
                    Logger.getLogger(LoanBrokerGateway.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });
        receiver.openConnection();
    }
    
    public boolean sendLoanRequest(LoanRequest loanRequest)
    {
        try
        {
            Message m = sender.createTextMessage(serializerClient.requestToString(loanRequest));
            return sender.sendMessage((TextMessage) m);
        }
        catch (JMSException ex)
        {
            Logger.getLogger(LoanBrokerGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public boolean sendBankInterestReply(BankInterestReply bankInterestReply)
    {
        try
        {
            Message m = sender.createTextMessage(serializerBank.replyToString(bankInterestReply));
            return sender.sendMessage((TextMessage) m);
        }
        catch (JMSException ex)
        {
            Logger.getLogger(LoanBrokerGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public abstract void onLoanReplyArrived(LoanReply loanReply);    
    public abstract void onBankInterestRequestArrived(BankInterestRequest bankInterestRequest);    
}
