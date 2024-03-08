package views;

import dao.UserDAO;
import model.User;
import services.GenerateOTP;
import services.SendOTPService;
import services.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Scanner;

public class Welcome {
    public void welcomeScreen() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome to the App");
        System.out.println("Press 1 to Login");
        System.out.println("Press 2 to SignUp");
        System.out.println("Press 3 to Exit");

        int choice = 0;

        try {
            choice = Integer.parseInt(br.readLine());
        } catch (IOException ee) {
            ee.printStackTrace();
        }
        switch (choice) {
            case 1 -> login();
            case 2 -> signUp();
            case 3 -> System.exit(0);
        }
    }

    private void signUp() {

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Name");
        String name = sc.nextLine();
        System.out.println("Enter Email-ID");
        String email = sc.nextLine();
        String genOTP = GenerateOTP.getOTP();
        SendOTPService.sendOTP(email, genOTP);
        System.out.println("Enter the OTP");
        String otp = sc.nextLine();
        if (otp.equals((genOTP))) {
            User user = new User(name, email);
            int response = UserService.saveUser(user);
            switch (response) {
                case 0 -> System.out.println("User registered");
                case 1 -> System.out.println("User already exists");
            }
        }else {
            System.out.println("Wrong OTP");
            System.out.println();
        }
    }

    private void login() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter email");
        String email = sc.nextLine();
        System.out.println("Enter name");
        String name = sc.nextLine();
        try {
            if(UserDAO.isExists(email)) {
                String genOTP = GenerateOTP.getOTP();
                SendOTPService.sendOTP(email, genOTP);
                System.out.println("Enter the otp");
                String otp = sc.nextLine();
                if(otp.equals(genOTP)) {
                    new UserView(email,name).home();

                } else {
                    System.out.println("Wrong OTP");
                    System.out.println();
                }
            } else {
                System.out.println("User not found");
                System.out.println();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    }