package com.saikat.liftsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Scanner;

@EnableScheduling
@SpringBootApplication
public class LiftsystemApplication {

	public static void main(String[] args) {
		if (System.getProperty("building.prompt.done") == null) {
			int floors = promtFloors();
			System.setProperty("building.maxFloors", String.valueOf(floors));
			System.setProperty("building.prompt.done", "true");
		}

		SpringApplication.run(LiftsystemApplication.class, args);
		System.out.println("Lift System is live with maxFloors= " +
				System.getProperty("building.maxFloors"));
	}

	private static int promtFloors(){
		Scanner sc = new Scanner(System.in);
		Integer floors = null;
		System.out.println("Enter the number of floors in the building (>=2): ");
		while(floors == null){
			String line = sc.nextLine().trim();
			try {
				int n = Integer.parseInt(line);
				if(n<2){
					System.out.println("Invalid. Please enter an integer >=2: ");
				} else {
					floors = n;
				}
			} catch (NumberFormatException e){
				System.out.println("Invalid. Please enter an integer >=2: ");
			}
		}
		return floors;
	}

}
