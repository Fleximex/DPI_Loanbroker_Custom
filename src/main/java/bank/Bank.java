/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank;

import gateway.LoanBrokerGateway;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
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
public class Bank extends javax.swing.JFrame
{    
    private final LoanBrokerGateway loanBrokerGateway;
    private ArrayList<RequestReply<BankInterestRequest, BankInterestReply>> rrList;
    private DefaultListModel<String> model;

    public Bank(String bankToBrokerQueue, String brokerToBankQueue) throws JMSException, NamingException
    {
        loanBrokerGateway = new LoanBrokerGateway(bankToBrokerQueue, brokerToBankQueue)
        {
            @Override
            public void onBankInterestRequestArrived(BankInterestRequest bankInterestRequest) {
                onBankInterestRequest(bankInterestRequest);
            }

            @Override
            public void onLoanReplyArrived(LoanReply loanReply) {
                //not needed in this context
            }
        };
        rrList = new ArrayList<>();
        model = new DefaultListModel<String>(); 
        initComponents();
        this.getContentPane().setBackground(new Color(180, 185, 210));
        reloadList();
        positionWindow();
        this.setVisible(true);
    }
    
    private void positionWindow()
    {
        Dimension windowSize = Toolkit.getDefaultToolkit().getScreenSize();
        int dx = (int) (windowSize.width / 1.25 - this.getContentPane().getWidth() / 2);
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
        jLabel2 = new javax.swing.JLabel();
        interestfield = new javax.swing.JTextField();
        button = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setLabelFor(this);
        jLabel1.setText("Bank");

        jList1.setModel(model);
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jScrollPane1.setViewportView(jList1);

        jLabel2.setLabelFor(interestfield);
        jLabel2.setText("Interest:");

        button.setText("Send Reply");
        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(button, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                            .addComponent(interestfield))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(interestfield, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(button, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonActionPerformed
        if(!jList1.isSelectionEmpty())
        {
            BankInterestReply bankInterestReply = new BankInterestReply(rrList.get(jList1.getSelectedIndex()).getRequest().getId(), Integer.parseInt(interestfield.getText()), "Abn Bank");
            sendReply(bankInterestReply);
        }        
    }//GEN-LAST:event_buttonActionPerformed
    
    public void reloadList()
    {
        model.clear();
        for(RequestReply<BankInterestRequest, BankInterestReply> lr : rrList)
        {
            model.addElement(lr.toString());;
        }
        jList1.repaint();
    }
    
    private void onBankInterestRequest(BankInterestRequest bankInterestRequest)
    {
        try
        {
            rrList.add(new RequestReply<>(bankInterestRequest, null));
            reloadList();
        } 
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }
    
    public void sendReply(BankInterestReply bankInterestReply)
    {
        if (loanBrokerGateway.sendBankInterestReply(bankInterestReply))
        {
            rrList.set(jList1.getSelectedIndex(), new RequestReply<>(rrList.get(jList1.getSelectedIndex()).getRequest(), bankInterestReply));
            reloadList();
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button;
    private javax.swing.JTextField interestfield;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JList<String> jList1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}