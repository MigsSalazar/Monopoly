package edu.illinois.masalzr2.gui;

import java.awt.Color;
import java.util.Scanner;

import edu.illinois.masalzr2.Starter;
import edu.illinois.masalzr2.controllers.Environment;
import edu.illinois.masalzr2.io.GameIo;
import edu.illinois.masalzr2.models.Player;
import edu.illinois.masalzr2.models.Property;
import edu.illinois.masalzr2.models.Street;
import edu.illinois.masalzr2.models.Suite;
import lombok.Data;

@Data
public class TesterFrame {
	
	Environment gv;
	
	public static void main(String[] args){
		TesterFrame tf = new TesterFrame();
		tf.fakeMain();
	}
	
	public void fakeMain() {
		gv = GameIo.newGame();
		if( !Starter.gameSetup(null, gv) )
			return;
		
		Scanner kb = new Scanner(System.in);
		String in = "";
		int num=0;
		do {
			System.out.println("Enter a comman or ? for help: ");
			in = kb.nextLine();
			switch(in) {
			case "?": break;
			case "max grades":
				changeGrades(5);
				gv.paintHousing();
				break;
			case "set grade":
				System.out.println("Enter the grade: ");
				num = kb.nextInt();
				changeGrades(num);
				gv.paintHousing();
				break;
			case "clear grades":
				changeGrades(0);
				gv.paintHousing();
				break;
			case "fine set grade":
				System.out.println("Pick the property position: ");
				num = kb.nextInt();
				Property p = gv.getPropertyAt(num);
				if(p != null) {
					if(p instanceof Street) {
						System.out.println("Enter the grade");
						num = kb.nextInt();
						((Street) p).setGrade(num);
						gv.paintHousing();
					}
				}
				break;
			case "print properties":
				for(Property props : gv.getProperties().values()) {
					System.out.println(props.getName()+" "+props.getColor()+" "+props.getPosition());
				}
				break;
			case "give prop":
				System.out.println("Enter property position: ");
				num = kb.nextInt();
				Property prop = gv.getPropertyAt(num);
				if(prop != null) {
					System.out.println("Enter player ID");
					int pnum = kb.nextInt();
					Player pl = gv.getPlayerByID(pnum);
					if(pl != null) {
						pl.addProp(prop);
						prop.setOwner(pl.getName());
					}
				}
				break;
			case "get color":
				int r, g, b;
				System.out.println("Enter R: ");
				r = kb.nextInt();
				System.out.println("Enter G: ");
				g = kb.nextInt();
				System.out.println("Enter B:");
				b = kb.nextInt();
				System.out.println(""+(new Color(r,g,b).getRGB()));
				break;
			}
		}while(!in.equals("exit"));
		kb.close();
		System.exit(0);
	}

	private void changeGrades(int num) {
		for(Suite s : gv.getSuites().values()) {
			for(Street st : s.sortedByPosition()) {
				st.setGrade(num);
			}
		}
	}
}
