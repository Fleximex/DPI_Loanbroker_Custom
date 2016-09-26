/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gateway;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.naming.NamingException;
import model.bank.BankInterestReply;
import model.bank.BankInterestRequest;

/**
 *
 * @author Maarten
 */
public abstract class BankGateway
{
    private final MessageSenderGateway sender;
    private final MessageReceiverGateway receiver;
    private BankSerializer serializer;

    public BankGateway(String senderQueue, String receiverQueue) throws JMSException, NamingException
    {
        serializer = new BankSerializer();
        
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
                    BankInterestReply reply = serializer.replyFromString(m.getText());
                    onBankInterestReplyArrived(reply);
                }
                catch (JMSException ex)
                {
                    
                }
            }
            
        });
        receiver.openConnection();
    }    
        
    public boolean sendBankInterestRequest(BankInterestRequest bankInterestRequest)
    {
        try
        {
            Message m = sender.createTextMessage(serializer.requestToString(bankInterestRequest));
            return sender.sendMessage((TextMessage) m);
        }
        catch (JMSException ex)
        {
            Logger.getLogger(BankGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public abstract void onBankInterestReplyArrived(BankInterestReply bankInterestReply);
}