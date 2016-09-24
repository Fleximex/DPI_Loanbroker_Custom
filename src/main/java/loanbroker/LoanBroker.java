/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loanbroker;

import gateway.BankGateway;
import gateway.ClientGateway;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.swing.DefaultListModel;
import model.bank.BankInterestReply;
import model.bank.BankInterestRequest;
import model.loan.LoanReply;
import model.loan.LoanRequest;
import model.requestreply.RequestReply;

/**
 *
 * @author Maarten
 */
public class LoanBroker extends javax.swing.JFrame
{
    private final ClientGateway clientGateway;
    private final BankGateway bankGateway;
    private final ArrayList<RequestReply<LoanRequest, BankInterestReply>> rrList;
    private final DefaultListModel<String> model;
    
    public LoanBroker(String clientToBrokerQueue, String brokerToClientQueue, String brokerToBankQueue, String bankToBrokerQueue) throws JMSException, NamingException
    {
        clientGateway = new ClientGateway(brokerToClientQueue, clientToBrokerQueue)
        {
            @Override
            public void onLoanRequestArrived(LoanRequest loanRequest)
            {
                onLoanRequest(loanRequest);
            }
        };
        bankGateway = new BankGateway(brokerToBankQueue, bankToBrokerQueue)
        {
            @Override
            public void onBankInterestRequestArrived(BankInterestReply bankInterestReply)
            {
                onBankInterestReply(bankInterestReply);
            }
        };
        rrList = new ArrayList<>();
        model = new DefaultListModel<>();        
        initComponents();
        this.getContentPane().setBackground(new Color(180, 185, 210));
        reloadList();
        positionWindow();
        this.setVisible(true);
    }
    
    private void positionWindow()
    {
        Dimension windowSize = Toolkit.getDefaultToolkit().getScreenSize();
        int dx = windowSize.width / 2 - this.getContentPane().getWidth() / 2;
        int dy = windowSize.height / 2 - this.getContentPane().getHeight();
        setLocation(dx, dy);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setLabelFor(this);
        jLabel1.setText("Loan Broker");

        jList1.setModel(model);
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jScrollPane1.setViewportView(jList1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
 
    public void reloadList()
    {
        model.clear();
        for(RequestReply<LoanRequest, BankInterestReply> lr : rrList)
        {
            model.addElement(lr.toString());
        }
        jList1.repaint();
    }
    
    private void onLoanRequest(LoanRequest loanRequest) {
        try
        {
            rrList.add(new RequestReply<>(loanRequest, null));
            reloadList();
            bankGateway.sendBankInterestRequest(new BankInterestRequest(loanRequest.getId(), loanRequest.getAmount(), loanRequest.getTime()));
        } 
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }
    
    private void onBankInterestReply(BankInterestReply bankInterestReply)
    {
        int i = 0;
        try
        {
            for (RequestReply<LoanRequest, BankInterestReply> rr : rrList)
            {                
                if (rr.getRequest().getId() == bankInterestReply.getId())
                {
                    rrList.set(i, new RequestReply<>(rr.getRequest(), bankInterestReply));
                }
                i++;
            }
            reloadList();
            clientGateway.sendLoanReply(new LoanReply(bankInterestReply.getId(), bankInterestReply.getInterest(), bankInterestReply.getBankId()));
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList<String> jList1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
