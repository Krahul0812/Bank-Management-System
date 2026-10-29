package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PinChange extends JFrame implements ActionListener {

    JPasswordField pin, repin;
    JButton change, back;
    String pinnumber;

    public PinChange(String pinnumber) {
        this.pinnumber = pinnumber;

        setLayout(null);

        try {
            ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/atm.jpg"));
            Image i2 = i1.getImage().getScaledInstance(900, 900, Image.SCALE_DEFAULT);
            ImageIcon i3 = new ImageIcon(i2);
            JLabel image = new JLabel(i3);
            image.setBounds(0, 0, 900, 900);
            add(image);

            JLabel text = new JLabel("CHANGE YOUR PIN");
            text.setForeground(Color.WHITE);
            text.setFont(new Font("System", Font.BOLD, 16));
            text.setBounds(250, 280, 500, 35);
            image.add(text);

            JLabel pintext = new JLabel("New PIN:");
            pintext.setForeground(Color.WHITE);
            pintext.setFont(new Font("System", Font.BOLD, 16));
            pintext.setBounds(165, 320, 180, 25);
            image.add(pintext);

            pin = new JPasswordField();
            pin.setFont(new Font("Raleway", Font.BOLD, 25));
            pin.setBounds(330, 320, 180, 25);
            image.add(pin);

            JLabel repintext = new JLabel("Re-Enter New PIN:");
            repintext.setForeground(Color.WHITE);
            repintext.setFont(new Font("System", Font.BOLD, 16));
            repintext.setBounds(165, 360, 180, 25);
            image.add(repintext);

            repin = new JPasswordField();
            repin.setFont(new Font("Raleway", Font.BOLD, 25));
            repin.setBounds(330, 360, 180, 25);
            image.add(repin);

            change = new JButton("CHANGE");
            change.setBounds(355, 485, 150, 30);
            change.addActionListener(this);
            image.add(change);

            back = new JButton("BACK");
            back.setBounds(355, 520, 150, 30);
            back.addActionListener(this);
            image.add(back);

        } catch (Exception e) {
            System.out.println("Error loading PinChange UI: " + e);
        }

        setSize(900, 900);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == change) {
            try {
                String npin = new String(pin.getPassword()).trim();
                String rpin = new String(repin.getPassword()).trim();

                if (!npin.equals(rpin)) {
                    JOptionPane.showMessageDialog(null, "Entered PIN does not match");
                    return;
                }

                if (npin.equals("")) {
                    JOptionPane.showMessageDialog(null, "Please enter new PIN");
                    return;
                }

                if (rpin.equals("")) {
                    JOptionPane.showMessageDialog(null, "Please re-enter new PIN");
                    return;
                }

                Conn conn = new Conn();
                String q1 = "update bank set pin = '" + rpin + "' where pin = '" + pinnumber + "'";
                String q2 = "update login set pin = '" + rpin + "' where pin = '" + pinnumber + "'";
                String q3 = "update signupthree set pin = '" + rpin + "' where pin = '" + pinnumber + "'";

                conn.s.executeUpdate(q1);
                conn.s.executeUpdate(q2);
                conn.s.executeUpdate(q3);

                JOptionPane.showMessageDialog(null, "PIN changed successfully");

                setVisible(false);
                new Transactions(rpin).setVisible(true);

            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            setVisible(false);
            new Transactions(pinnumber).setVisible(true);
        }
    }

    public static void main(String[] args) {
        new PinChange("");
    }
}