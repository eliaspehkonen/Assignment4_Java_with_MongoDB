import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.types.ObjectId;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class UserForm {
    private JFrame frame;
    private JPanel panel;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JTextField textfield1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public UserForm() {
        frame = new JFrame();
        panel = new JPanel(new GridLayout(0, 2, 10, 10));
        label1 = new JLabel("ID:");
        label2 = new JLabel("Name:");
        label3 = new JLabel("Age:");
        label4 = new JLabel("City:");
        textfield1 = new JTextField(20);
        textField2 = new JTextField(20);
        textField3 = new JTextField(20);
        textField4 = new JTextField(20);

        button1 = new JButton("Add");
        button2 = new JButton("Read");
        button3 = new JButton("Update");
        button4 = new JButton("Delete");

        initializeDatabase();

        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // Parse the age string to an integer
                    int age = Integer.parseInt(textField3.getText());
                    int id = Integer.parseInt(textfield1.getText());

                    // Create the document with the parsed age
                    Document doc = new Document("id", id)
                            .append("name", textField2.getText())
                            .append("age", age)  // Store age as an integer
                            .append("city", textField4.getText());
                    collection.insertOne(doc);
                    JOptionPane.showMessageDialog(frame, "User added successfully!");
                    clearFields();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Failed to add user: Invalid age or id format", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Failed to add user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Document doc = collection.find(eq("_id", new ObjectId(textfield1.getText()))).first();
                    if (doc != null) {
                        JOptionPane.showMessageDialog(frame, doc.toJson(), "Found user: ", JOptionPane.INFORMATION_MESSAGE);
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(frame, "No matching documents found.");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Failed to read user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int age = Integer.parseInt(textField3.getText());
                    int id = Integer.parseInt(textfield1.getText());

                    Document doc = new Document("id", id)
                            .append("name", textField2.getText())
                            .append("age", age)
                            .append("city", textField4.getText());
                    collection.updateOne(eq("id", id), new Document("$set", doc));
                    Document checkDoc = collection.find(eq("id", id)).first();
                    if (checkDoc != null) {
                        JOptionPane.showMessageDialog(frame, "User updated successfully!");
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Failed to update user: No matching document found.");
                    }
                } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Failed to update user: Invalid age or id format", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Failed to update user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        button4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(textfield1.getText());
                    collection.deleteOne(eq("id", id));
                    Document checkDoc = collection.find(eq("id", id)).first();
                    if (checkDoc == null) {
                        JOptionPane.showMessageDialog(frame, "User deleted successfully!");
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Failed to delete user: Document still exists.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Failed to delete user: Invalid id format", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Failed to delete user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        // Adding pairs of labels and text fields
        panel.add(label1);
        panel.add(textfield1);
        panel.add(label2);
        panel.add(textField2);
        panel.add(label3);
        panel.add(textField3);
        panel.add(label4);
        panel.add(textField4);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonsPanel.add(button1);
        buttonsPanel.add(button2);
        buttonsPanel.add(button3);
        buttonsPanel.add(button4);

        frame.add(panel, BorderLayout.CENTER);
        frame.add(buttonsPanel, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("MongoDB CRUD Operations");
        frame.pack();
        frame.setVisible(true);

//        initializeDatabase();
    }

    private void initializeDatabase() {
        try {
            mongoClient = MongoClients.create("mongodb+srv://<username>:<password>@cluster0.5qihclf.mongodb.net/");
            database = mongoClient.getDatabase("Assignment4");
            collection = database.getCollection("users");
            JOptionPane.showMessageDialog(frame, "Connected to the database successfully!");
            System.out.println("Connected to the database successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Failed to connect to the database: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            System.out.println("Failed to connect to the database: " + e.getMessage());
        }
    }

    private void clearFields() {
        textfield1.setText("");
        textField2.setText("");
        textField3.setText("");
        textField4.setText("");
    }

    public static void main(String[] args) {
        new UserForm();
    }
}
