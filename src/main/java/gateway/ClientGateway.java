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
import model.loan.LoanReply;
import model.loan.LoanRequest;

/**
 *
 * @author Maarten
 */
public abstract class ClientGateway
{
    private final MessageSenderGateway sender;
    private final MessageReceiverGateway receiver;
    private LoanSerializer serializer;

    public ClientGateway(String senderQueue, String receiverQueue) throws JMSException, NamingException
    {
        serializer = new LoanSerializer();
        
        sender = new MessageSenderGateway(senderQueue);
        sender.openConnection();
        
        receiver = new MessageReceiverGateway(receiverQueue);
        receiver.setReceivedMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message msg)
            {
                try
                {
                    TextMessage m = (TextMessage) msg;
                    LoanRequest loanRequest = serializer.requestFromString(m.getText());
                    onLoanRequestArrived(loanRequest);
                }
                catch (JMSException ex)
                {
                    
                }
            }
            
        });
        receiver.openConnection();
    }
    
    public boolean getBankInterest(LoanRequest loanRequest)
    {
        try
        {
            Message m = sender.createTextMessage(serializer.requestToString(loanRequest));
            return sender.sendMessage((TextMessage) m);
        }
        catch (JMSException ex)
        {
            Logger.getLogger(ClientGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public boolean sendLoanReply(LoanReply loanReply)
    {
        try
        {
            Message m = sender.createTextMessage(serializer.replyToString(loanReply));
            return sender.sendMessage((TextMessage) m);
        }
        catch (JMSException ex)
        {
            Logger.getLogger(ClientGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public abstract void onLoanRequestArrived(LoanRequest loanRequest);
}
