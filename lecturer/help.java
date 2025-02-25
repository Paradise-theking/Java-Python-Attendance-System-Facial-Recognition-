package lecturer;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import javax.swing.*;

import admin.timetable;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class help extends JFrame {

    private MediaPlayer mediaPlayer;
    private JFXPanel jfxPanel;
    private JButton playButton;
    private JButton pauseButton;
    private JButton stopButton;
    
    private static final String VIDEO_PATH = "C:/Users/PARADISE/Videos/111.mp4"
    		+ "";
    private JButton playButton_1;
    private JButton btnPdf;

    public help() {
        initUI();
    }

    private void initUI() {
        setTitle("Video Player Example");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create buttons
        playButton = new JButton("Play");
        playButton.setForeground(new Color(255, 255, 255));
        playButton.setBackground(new Color(0, 0, 0));
        playButton.setFont(new Font("Segoe UI Semilight", Font.BOLD, 16));
        pauseButton = new JButton("Pause");
        pauseButton.setFont(new Font("Segoe UI Semilight", Font.BOLD, 16));
        pauseButton.setForeground(new Color(255, 255, 255));
        pauseButton.setBackground(new Color(0, 0, 128));
        stopButton = new JButton("Stop");
        stopButton.setBackground(new Color(128, 0, 0));
        stopButton.setForeground(new Color(255, 255, 255));
        stopButton.setFont(new Font("Segoe UI Semilight", Font.BOLD, 16));

        // Add action listeners
        playButton.addActionListener(e -> playVideo());
        pauseButton.addActionListener(e -> pauseVideo());
        stopButton.addActionListener(e -> stopVideo());

        // Create toolbar
        JToolBar toolBar = new JToolBar();
        toolBar.setBackground(new Color(0, 0, 0));
        
        playButton_1 = new JButton("Back");
        playButton_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		setVisible(false);
	            LecturerDashboard object = new LecturerDashboard();
	            object.setVisible(true);
        	}
        });
        playButton_1.setForeground(Color.WHITE);
        playButton_1.setFont(new Font("Segoe UI Semilight", Font.BOLD, 16));
        playButton_1.setBackground(new Color(128, 0, 0));
        toolBar.add(playButton_1);
        toolBar.add(playButton);
        toolBar.add(pauseButton);
        toolBar.add(stopButton);
        getContentPane().add(toolBar, BorderLayout.NORTH);
        
        btnPdf = new JButton("PDF");
        btnPdf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Specify the path to the PDF file
                File pdfFile = new File("C:\\Users\\PARADISE\\Desktop\\sgele\\YEAR IV\\Project\\Manual\\manual.pdf");

                if (pdfFile.exists()) { // Check if the file exists
                    try {
                        if (Desktop.isDesktopSupported()) { // Check if the desktop is supported
                            Desktop.getDesktop().open(pdfFile); // Open the PDF file in the default viewer
                        } else {
                            JOptionPane.showMessageDialog(help.this, "Desktop is not supported. Cannot open PDF.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(help.this, "Failed to open PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(help.this, "The specified PDF file does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnPdf.setForeground(Color.WHITE);
        btnPdf.setFont(new Font("Segoe UI Semilight", Font.BOLD, 16));
        btnPdf.setBackground(new Color(0, 0, 139));
        toolBar.add(btnPdf);

        // Add a panel for the JavaFX content
        Panel fxPanel = new Panel();
        fxPanel.setForeground(new Color(0, 0, 128));
        fxPanel.setBackground(new Color(51, 0, 51));
        jfxPanel = new JFXPanel();
        fxPanel.add(jfxPanel, BorderLayout.CENTER);
        getContentPane().add(fxPanel, BorderLayout.CENTER);

        // Ensure mediaPlayer is stopped when the window is closed
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                Platform.runLater(() -> {
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                    }
                });
                dispose();
            }
        });
    }

    private void playVideo() {
        Platform.runLater(() -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }

            File videoFile = new File(VIDEO_PATH);
            if (!videoFile.exists()) {
                JOptionPane.showMessageDialog(this, "Video file not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Media media = new Media(videoFile.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            MediaView mediaView = new MediaView(mediaPlayer);

            BorderPane root = new BorderPane();
            root.setCenter(mediaView);

            Scene scene = new Scene(root, 1200, 800);
            jfxPanel.setScene(scene);
            mediaPlayer.setAutoPlay(true);
        });
    }

    private void pauseVideo() {
        Platform.runLater(() -> {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }
        });
    }

    private void stopVideo() {
        Platform.runLater(() -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            help example = new help();
            example.setVisible(true);
        });
    }
}
