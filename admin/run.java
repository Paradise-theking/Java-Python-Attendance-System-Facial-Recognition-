package admin;

import javax.swing.*;

import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class run extends JFrame {
    private Panel contentPane; 
    private DefaultTableModel model;

    public static void main(String[] args) { 
        EventQueue.invokeLater(() -> {
            try {
                run frame = new run();
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public run() {
       setUndecorated(true);
        setTitle("Set/Update Courses");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 550, 500);
        contentPane = new Panel();
        contentPane.setForeground(new Color(0, 0, 102));
        contentPane.setBackground(new Color(0, 0, 0));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Panel for the title and icon
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(0, 0, 51));
        titlePanel.setBounds(0, 0, 550, 51);
        contentPane.add(titlePanel);
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JLabel lblTitle = new JLabel("Take Attendance");
        lblTitle.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\edit.png"));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI Semibold", Font.BOLD, 22));
        titlePanel.add(lblTitle);

        // Scroll pane for displaying output
        JTextArea outputTextArea = new JTextArea(10, 30);
        outputTextArea.setFont(new Font("Monospaced", Font.BOLD, 14));
        outputTextArea.setForeground(new Color(255, 255, 255));
        outputTextArea.setBackground(new Color(0, 0, 0));
        outputTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputTextArea);
        scrollPane.setBounds(50, 100, 450, 300);
        contentPane.add(scrollPane);

        // Button to run the script
        JButton btnRunScript = new JButton("Start");
        btnRunScript.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\bell_26px.png"));
        btnRunScript.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnRunScript.setEnabled(false); // Disable button during script execution

                // Execute the script in a separate thread
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Full path to the Anaconda Python executable
                            String pythonPath = "C:\\Users\\PARADISE\\anaconda3\\python.exe"; // Windows example

                            // Command to run the Python script
                            String scriptPath = "C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\FACIAL RECOGNITION\\attendance.py";
                            String command = pythonPath + " " + scriptPath;

                            // Create a process builder
                            ProcessBuilder processBuilder = new ProcessBuilder(pythonPath, scriptPath);
                            processBuilder.redirectErrorStream(true);

                            // Start the process
                            Process process = processBuilder.start();
                            System.out.println("Python process started.");

                            // Read the output continuously
                            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                            String line;
                            while ((line = reader.readLine()) != null) {
                                final String outputLine = line; // Capture line in final variable for thread safety
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        outputTextArea.append(outputLine + "\n"); // Append each line to the JTextArea
                                    }
                                });
                            }

                            // Wait for the process to complete
                            int exitCode = process.waitFor();
                            System.out.println("Python script execution finished with exit code: " + exitCode);

                        } catch (IOException ex) {
                            ex.printStackTrace();
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    outputTextArea.append("Error running script: " + ex.getMessage() + "\n"); // Append error message
                                }
                            });
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        } finally {
                            // Enable the button after script execution completes
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    btnRunScript.setEnabled(true);
                                }
                            });
                        }
                    }
                }).start();
            }
        });
        btnRunScript.setForeground(new Color(0, 0, 0));
        btnRunScript.setBackground(new Color(0, 191, 255));
        btnRunScript.setFont(new Font("Segoe UI Semilight", Font.BOLD, 16));
        btnRunScript.setBounds(200, 420, 122, 30);
        contentPane.add(btnRunScript);
        
        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		 setVisible(false);
                 AdminDashboard object = new AdminDashboard();
                 object.setVisible(true);
        	}
        });
        btnBack.setIcon(new ImageIcon("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\AttendanceSystem\\icon\\logout.png"));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("Segoe UI Semilight", Font.BOLD, 16));
        btnBack.setBackground(Color.BLACK);
        btnBack.setBounds(60, 420, 115, 30);
        contentPane.add(btnBack);
    }
}
