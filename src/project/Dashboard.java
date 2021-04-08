package project;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.IOException;
import java.net.http.HttpResponse;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import api.ApiRequest;

public class Dashboard extends JFrame {

	private JPanel contentPane;
	private JLabel userDetail;
	private JLabel[] sessionPanel = new JLabel[5]; // maximum 5 subject

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Dashboard frame = new Dashboard();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Dashboard() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 500, 500);
		
		userDetail = new JLabel();
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(5,5));
		setContentPane(contentPane);
		
		userDetail.setText("hi");
		userDetail.setText("Hello");
		this.add(userDetail);
		
		try {
			create_subPanel();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void create_subPanel() throws IOException, InterruptedException {
		// retrieve all subject from API
		HttpResponse<String> response = ApiRequest.get("/subject");

	    System.out.println("Part 1\n----");
//	    System.out.println(request.uri());
	    System.out.println("Response code: " + response.statusCode()); // Status code of 4xx or 5xx indicates an error with the request or with the server, respectively.
	    System.out.println("Full JSON response: " + response.body());
	    System.out.println("----\n\n");
		
		}

}
