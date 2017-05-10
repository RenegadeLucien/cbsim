package cbsim;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Simulator extends Application{

	public static void main(String args[])
	{
		launch(args);
	}
	
	public String simCombat(Player p, Enemy e)
	{
		double phc;
		double pdmg;
		double ehc;
		double edmg;
		
		int paff;
		int maff ;
		
		pdmg = p.ad * p.aadpt * 3 / 100;
		edmg = (e.maxhit / 2)*(1-p.reduc/100.0);
		
		if (p.weakness == true)
			maff = 90;
		else if (p.style.equals("Melee"))
			maff = e.affmelee;
		else if (p.style.equals("Ranged"))
			maff = e.affranged;
		else
			maff = e.affmage;
		
		if (p.style.equals(e.style))
			paff = 55;
		else if ((p.style.equals("Melee") && e.style.equals("Ranged"))
				|| (p.style.equals("Ranged") && e.style.equals("Magic"))
				|| (p.style.equals("Magic") && e.style.equals("Melee")))
			paff = 45;
		else
			paff = 65;
		
		int eta = (int) Math.floor(e.acc + (0.00008*Math.pow(e.accstat, 3) + 4*e.accstat + 40));
		int etd = (int) Math.floor(e.armor + (0.00008*Math.pow(e.def, 3) + 4*e.def + 40));
		
		phc = Math.min(maff*p.acc/etd, 100.0)/100.0;
		ehc = Math.min(paff*eta/p.arm, 100.0)/100.0;
		
		int ticksTaken = -1;
		double playerLP = p.maxhp + p.healing;
		double enemyLP = e.lp;
		int kills = 0;
		
		while (playerLP > 0)
		{
			ticksTaken += 1;
			if (ticksTaken == 0)
				enemyLP -= phc*p.auto/2;
			if (ticksTaken % 3 == 1)
				enemyLP -= phc*pdmg;
			if (ticksTaken % e.atkspd == 0 && ticksTaken != 0)
				playerLP -= ehc*edmg;
			if (enemyLP <= 0)
			{
				double damage = p.maxhp + p.healing - playerLP;
				if (damage == 0)
				{
					kills = 9001;
					break;
				}
				kills = (int) ((p.maxhp + p.healing)/damage);
				break;
			}
		}
		
		String results;
		
		if (kills >= 9001)
			results = "You can kill these monsters indefinitely. Perhaps try something a little stronger?";
		else
			results = "You can kill approximately " + kills + " of these monsters before you must bank.";
		
		return results;
		
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		GridPane root = new GridPane();
		
		root.getColumnConstraints().add(new ColumnConstraints(125));
		root.getColumnConstraints().add(new ColumnConstraints(125));
		root.getColumnConstraints().add(new ColumnConstraints(125));
		root.getColumnConstraints().add(new ColumnConstraints(125));
		root.getColumnConstraints().add(new ColumnConstraints(125));
		root.getColumnConstraints().add(new ColumnConstraints(125));
		
		Text nameText = new Text("Player");
		
		final ToggleGroup stylegroup = new ToggleGroup();
		RadioButton playermelee = new RadioButton("Melee");
		playermelee.setToggleGroup(stylegroup);
		RadioButton playerranged = new RadioButton("Ranged");
		playerranged.setToggleGroup(stylegroup);
		RadioButton playermagic = new RadioButton("Magic");
		playermagic.setToggleGroup(stylegroup);
		playermagic.setSelected(true);
		
		Text accText = new Text("Enter accuracy:");
		TextField accEntry = new TextField();
		
		Text adText = new Text("Enter ability damage:");
		TextField adEntry = new TextField();
		
		Text autoText = new Text("Auto-attack damage:");
		TextField autoEntry = new TextField();
		
		Text aadptText = new Text("Enter AADPT:");
		TextField aadptEntry = new TextField();
		
		Text armText = new Text("Enter armor:");
		TextField armEntry = new TextField();
		
		Text weakText = new Text("Using specific weakness?");
		CheckBox weakness = new CheckBox();
		
		Text hpText = new Text("Enter maximum LP:");
		TextField hpEntry = new TextField();
		
		Text healText = new Text("Enter total LP in healing:");
		TextField healEntry = new TextField();
		
		Text reducText = new Text("Enter damage reduction:");
		TextField reducEntry = new TextField();
		
		Text enemy = new Text("Enemy");
		
		final ToggleGroup enemygroup = new ToggleGroup();
		RadioButton enemymelee = new RadioButton("Melee");
		enemymelee.setToggleGroup(enemygroup);
		RadioButton enemyranged = new RadioButton("Ranged");
		enemyranged.setToggleGroup(enemygroup);
		RadioButton enemymagic = new RadioButton("Magic");
		enemymagic.setToggleGroup(enemygroup);
		enemymelee.setSelected(true);
		
		Text enemyAcc = new Text("Enter enemy accuracy:");
		TextField enemyAccEntry = new TextField();
		
		Text enemyMax = new Text("Enter enemy max hit:");
		TextField enemyMaxEntry = new TextField();
		
		Text enemySpeed = new Text("Enemy attack speed:");
		TextField enemySpeedEntry = new TextField();
		
		Text enemyArmor = new Text("Enter enemy armor:");
		TextField enemyArmorEntry = new TextField();
		
		Text enemyLP = new Text("Enter enemy max LP:");
		TextField enemyLPEntry = new TextField();
		
		Text enemyAccStat = new Text("Enemy accuracy stat LVL:");
		TextField enemyAccStatEntry = new TextField();
		
		Text enemyDef = new Text("Enemy defence level:");
		TextField enemyDefEntry = new TextField();
		
		Text enemyAffWeak = new Text("Enemy weakness affinity:");
		TextField enemyAffWeakEntry = new TextField();
		
		Text enemyAffMelee = new Text("Enemy melee affinity:");
		TextField enemyAffMeleeEntry = new TextField();
		
		Text enemyAffRanged = new Text("Enemy ranged affinity:");
		TextField enemyAffRangedEntry = new TextField();
		
		Text enemyAffMagic = new Text("Enemy magic affinity:");
		TextField enemyAffMagicEntry = new TextField();
		
		Button sim = new Button("Simulate Combat");
		sim.setOnAction(new EventHandler<ActionEvent>() {
			 
            @Override
            public void handle(ActionEvent event) {
            	if (root.getChildren().size() > 49)
            		root.getChildren().remove(root.getChildren().size()-1);
            	
                Player p = new Player();
                if (playermelee.isSelected())
                	p.style = "Melee";
                else if (playerranged.isSelected())
                	p.style = "Ranged";
                else if (playermagic.isSelected())
                	p.style = "Magic";
                p.acc = Integer.parseInt(accEntry.getText());
                p.ad = Integer.parseInt(adEntry.getText());
                p.auto = Integer.parseInt(autoEntry.getText());
                p.aadpt = Double.parseDouble(aadptEntry.getText());
                p.arm = Integer.parseInt(armEntry.getText());
                p.weakness = weakness.isSelected();
                p.maxhp = Integer.parseInt(hpEntry.getText());
                p.healing = Integer.parseInt(healEntry.getText());
                p.reduc = Double.parseDouble(reducEntry.getText());
                
                Enemy e = new Enemy();
                if (enemymelee.isSelected())
                	e.style = "Melee";
                else if (enemyranged.isSelected())
                	e.style = "Ranged";
                else if (enemymagic.isSelected())
                	e.style = "Magic";
                e.acc = Integer.parseInt(enemyAccEntry.getText());
                e.maxhit = Integer.parseInt(enemyMaxEntry.getText());
                e.atkspd = Integer.parseInt(enemySpeedEntry.getText());
                e.armor = Integer.parseInt(enemyArmorEntry.getText());
                e.lp = Integer.parseInt(enemyLPEntry.getText());
                e.accstat = Integer.parseInt(enemyAccStatEntry.getText());
                e.def = Integer.parseInt(enemyDefEntry.getText());
                e.affweakness = Integer.parseInt(enemyAffWeakEntry.getText());
                e.affmelee = Integer.parseInt(enemyAffMeleeEntry.getText());
                e.affranged = Integer.parseInt(enemyAffRangedEntry.getText());
                e.affmage = Integer.parseInt(enemyAffMagicEntry.getText());
                
                String simulate = simCombat(p,e);
                Text display = new Text(simulate);
                root.add(display, 1, 14);
            }
        });
		
		
		stage.setTitle("RuneScape Combat Simulator v0.1.0 (by Iron Lucien)");
		stage.setScene(new Scene(root, 800, 600));
		root.setHgap(10);
		root.setVgap(10);
		
		root.add(nameText, 1, 0);
		root.add(playermelee, 0, 1);
		root.add(playerranged, 1, 1);
		root.add(playermagic, 2, 1);
		root.add(accText, 0, 2);
		root.add(accEntry, 1, 2);
		root.add(adText, 0, 3);
		root.add(adEntry, 1, 3);
		root.add(autoText, 0, 4);
		root.add(autoEntry, 1, 4);
		root.add(aadptText, 0, 5);
		root.add(aadptEntry, 1, 5);
		root.add(armText, 0, 6);
		root.add(armEntry, 1, 6);
		root.add(weakText, 0, 7);
		root.add(weakness, 1, 7);
		root.add(hpText, 0, 8);
		root.add(hpEntry, 1, 8);
		root.add(healText, 0, 9);
		root.add(healEntry, 1, 9);
		root.add(reducText, 0, 10);
		root.add(reducEntry, 1, 10);
		root.add(enemy, 4, 0);
		root.add(enemymelee, 3, 1);
		root.add(enemyranged, 4, 1);
		root.add(enemymagic, 5, 1);
		root.add(enemyAcc, 3, 2);
		root.add(enemyAccEntry, 4, 2);
		root.add(enemyMax, 3, 3);
		root.add(enemyMaxEntry, 4, 3);
		root.add(enemySpeed, 3, 4);
		root.add(enemySpeedEntry, 4, 4);
		root.add(enemyArmor, 3, 5);
		root.add(enemyArmorEntry, 4, 5);
		root.add(enemyLP, 3, 6);
		root.add(enemyLPEntry, 4, 6);
		root.add(enemyAccStat, 3, 7);
		root.add(enemyAccStatEntry, 4, 7);
		root.add(enemyDef, 3, 8);
		root.add(enemyDefEntry, 4, 8);
		root.add(enemyAffWeak, 3, 9);
		root.add(enemyAffWeakEntry, 4, 9);
		root.add(enemyAffMelee, 3, 10);
		root.add(enemyAffMeleeEntry, 4, 10);
		root.add(enemyAffRanged, 3, 11);
		root.add(enemyAffRangedEntry, 4, 11);
		root.add(enemyAffMagic, 3, 12);
		root.add(enemyAffMagicEntry, 4, 12);
		root.add(sim, 1, 11);
		
        stage.show();	
	}

}
