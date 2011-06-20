import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import javax.swing.SwingUtilities;

import org.rsbot.event.events.MessageEvent;
import org.rsbot.event.listeners.MessageListener;
import org.rsbot.event.listeners.PaintListener;
import org.rsbot.script.Script;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.methods.Methods;
import org.rsbot.script.methods.Skills;
import org.rsbot.script.util.Timer;
import org.rsbot.script.wrappers.RSArea;
import org.rsbot.script.wrappers.RSItem;
import org.rsbot.script.wrappers.RSObject;
import org.rsbot.script.wrappers.RSTile;
import org.rsbot.script.wrappers.RSTilePath;

@ScriptManifest(authors = { "LastCoder" }, keywords = { "Smithing" }, name = "ArbiSmitherLite", version = 2.0, description = "Start, all options are in GUI.", requiresVersion = 244)
@SuppressWarnings("deprecation")
public class AutoSmith extends Script implements MessageListener, PaintListener {

	static class Gui extends javax.swing.JFrame {

		/**
		 * GUI
		 */
		private static final long serialVersionUID = 1L;
		private final String[] bars = new String[AutoSmith.Materials.length];
		private final String[] locations = new String[AutoSmith.LOCATIONS.length];
		private final HashMap<String, SMLoc> locMap = new HashMap<String, SMLoc>();
		private final HashMap<String, Materials> matMap = new HashMap<String, Materials>();

		// Variables declaration - do not modify
		private javax.swing.JButton jButton1;

		private javax.swing.JComboBox jComboBox1;

		private javax.swing.JComboBox jComboBox2;

		private javax.swing.JLabel jLabel1;
		private javax.swing.JLabel jLabel2;
		private javax.swing.JLabel jLabel3;

		// End of variables declaration
		/**
		 * Creates new form Gui
		 */
		public Gui() {
			initComponents();
		}

		/**
		 * This method is called from within the constructor to initialize the
		 * form. WARNING: Do NOT modify this code. The content of this method is
		 * always regenerated by the Form Editor.
		 */
		private void initComponents() {
			AutoSmith.gui_on = true;
			for (int i = 0; i < AutoSmith.Materials.length; i++) {
				bars[i] = AutoSmith.Materials[i].name;
				matMap.put(bars[i], AutoSmith.Materials[i]);
			}
			for (int i = 0; i < AutoSmith.LOCATIONS.length; i++) {
				locations[i] = AutoSmith.LOCATIONS[i].name;
				locMap.put(locations[i], AutoSmith.LOCATIONS[i]);
			}

			jLabel1 = new javax.swing.JLabel();
			jLabel2 = new javax.swing.JLabel();
			jComboBox1 = new javax.swing.JComboBox();
			jLabel3 = new javax.swing.JLabel();
			jComboBox2 = new javax.swing.JComboBox();
			jButton1 = new javax.swing.JButton();

			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

			jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
			jLabel1.setText("AutoSmither");

			jLabel2.setText("Where:");

			jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(locations));

			jLabel3.setText("Type:");

			jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(bars));

			jButton1.setText("START");
			jButton1.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(final java.awt.event.ActionEvent evt) {
					jButton1ActionPerformed(evt);
				}
			});

			final javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
			getContentPane().setLayout(layout);
			layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(147, 147, 147).addComponent(jLabel1)).addGroup(layout.createSequentialGroup().addGap(37, 37, 37).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addGroup(layout.createSequentialGroup().addComponent(jLabel3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addGroup(layout.createSequentialGroup().addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE))).addContainerGap()));
			layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(26, 26, 26).addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE).addContainerGap()));

			pack();
		}// </editor-fold>

		private void jButton1ActionPerformed(
				final java.awt.event.ActionEvent evt) {
			AutoSmith.location = locMap.get(jComboBox1.getSelectedItem().toString());
			AutoSmith.MATERIAL = matMap.get(jComboBox2.getSelectedItem().toString());
			AutoSmith.gui_on = false;
			setVisible(false);
		}

	}

	static class Materials {
		public String name;
		public int req_lvl;
		public int inv_id;
		public int inv_second_id;
		public int primary;
		public int secondary;

		public Materials(final String name, final int REQ_LVL,
				final int INV_ID, final int INV_SECOND_ID, final int PRIMARY,
				final int SECONDARY) {
			this.name = name;
			req_lvl = REQ_LVL;
			inv_id = INV_ID;
			primary = PRIMARY;
			secondary = SECONDARY;
			inv_second_id = INV_SECOND_ID;
		}
	}

	static class SMLoc {
		public String name;
		public int furnace_id;
		public RSArea bank_area;
		public RSTile[] walk_path;

		public SMLoc(final String name, final int FURNACE_ID,
				final RSArea bankZones, final RSTile[] WALK_PATH) {
			this.name = name;
			furnace_id = FURNACE_ID;
			bank_area = bankZones;
			walk_path = WALK_PATH;
		}
	}

	private enum state {
		BANK, WALK, SMITH, INTERFACE, SLEEP
	}

	private static final SMLoc[] LOCATIONS = new SMLoc[] { new SMLoc("AL_KHARID", 11666, new RSArea(new RSTile(3275, 3175), new RSTile(3264, 3160)), new RSTile[] {
			new RSTile(3271, 3166), new RSTile(3273, 3166),
			new RSTile(3274, 3167), new RSTile(3274, 3169),
			new RSTile(3275, 3172), new RSTile(3276, 3174),
			new RSTile(3276, 3177), new RSTile(3277, 3178),
			new RSTile(3278, 3180), new RSTile(3280, 3182),
			new RSTile(3281, 3183), new RSTile(3281, 3185),
			new RSTile(3280, 3185), new RSTile(3278, 3185),
			new RSTile(3277, 3186), new RSTile(3276, 3186) }) };
	private static final Color COLOR_1 = new Color(0, 0, 0, 155);
	private static final Color COLOR_2 = new Color(0, 0, 0);
	private static final Color COLOR_3 = new Color(255, 255, 255);

	private static final BasicStroke STROKE = new BasicStroke(1);
	private static final Font FONT_1 = new Font("Arial", 0, 17);
	private static final Font FONT_2 = new Font("Arial", 0, 9);
	private static final Materials[] Materials = new Materials[] {
			new Materials("Bronze Bars", 1, 436, 438, 14, 14),
			new Materials("Iron Bars", 15, 440, -1, 28, -1),
			new Materials("Silver Bars", 20, 442, -1, 28, -1),
			new Materials("Steel Bars", 30, 440, 453, 9, 18),
			new Materials("Gold Bars", 40, 444, -1, 28, -1),
			new Materials("Mithril Bars", 50, 447, 453, 12, 14),
			new Materials("Adamant Bars", 70, 449, 453, 4, 24),
			new Materials("Rune Bars", 85, 451, 453, 3, 24) };
	private static SMLoc location;
	private static Materials MATERIAL;
	private long activity_time;
	private static boolean gui_on = false;
	private RSTilePath path;
	private RSTilePath path_back;

	private long startExp;

	private long startTime;

	private long expGained;

	private int expHour;

	private int barsCrafted;

	private boolean busy() {
		return System.currentTimeMillis() - activity_time < 8000;
	}

	private int distanceTo(final int ID) {
		final RSObject obj = objects.getNearest(ID);
		if (obj != null) {
			return calc.distanceTo(obj);
		}
		return -1;
	}

	private state getState() {
		if (interfaces.get(905).isValid()) {
			log("Interface is valid.");
			return state.INTERFACE;
		} else if (inArea(AutoSmith.location.bank_area)) {
			if (inventory.contains(AutoSmith.MATERIAL.inv_id)) {
				if (AutoSmith.MATERIAL.inv_second_id != -1) {
					if (inventory.contains(AutoSmith.MATERIAL.inv_second_id)) {
						log("Inventory contains 1st and 2nd");
						return state.WALK;
					} else {
						return state.BANK;
					}
				} else {
					return state.WALK;
				}
			} else {
				return state.BANK;
			}
		} else if (distanceTo(AutoSmith.location.furnace_id) < 5) {
			if (!busy()) {
				if (inventory.contains(AutoSmith.MATERIAL.inv_id)) {
					return state.SMITH;
				} else {
					return state.WALK;
				}
			} else {
				return state.SLEEP;
			}
		}
		return state.WALK;
	}

	private boolean inArea(final RSArea area) {
		return area.contains(getMyPlayer().getLocation());
	}

	@Override
	public int loop() {
		switch (getState()) {
		case BANK:
			if (!bank.isOpen()) {
				bank.open();
				for (int i = 0; i < 100 && !bank.isOpen(); i++) {
					Methods.sleep(20);
				}
			} else {
				if (AutoSmith.MATERIAL.inv_second_id != -1) {
					if (!inventory.contains(AutoSmith.MATERIAL.inv_id)) {
						if (inventory.getCount() > 1) {
							bank.depositAll();
						}
						bank.withdraw(AutoSmith.MATERIAL.inv_id, AutoSmith.MATERIAL.primary);
						for (int i = 0; i < 100
								&& !inventory.contains(AutoSmith.MATERIAL.inv_id); i++) {
							Methods.sleep(20);
						}
					}
					if (!inventory.contains(AutoSmith.MATERIAL.inv_second_id)) {
						bank.withdraw(AutoSmith.MATERIAL.inv_second_id, AutoSmith.MATERIAL.secondary);
						for (int i = 0; i < 100
								&& !inventory.contains(AutoSmith.MATERIAL.inv_second_id); i++) {
							Methods.sleep(20);
						}
					}
				} else {
					if (!inventory.contains(AutoSmith.MATERIAL.inv_id)) {
						if (inventory.getCount() > 1) {
							bank.depositAll();
						}
						bank.withdraw(AutoSmith.MATERIAL.inv_id, AutoSmith.MATERIAL.primary);
						for (int i = 0; i < 100
								&& !inventory.contains(AutoSmith.MATERIAL.inv_id); i++) {
							Methods.sleep(20);
						}
					}
				}
			}
			break;
		case WALK:
			if (AutoSmith.MATERIAL.inv_second_id != -1) {
				if (inventory.contains(AutoSmith.MATERIAL.inv_id)
						&& inventory.contains(AutoSmith.MATERIAL.inv_second_id)) {
					path.traverse();
					Methods.sleep(20);
				} else {
					path_back.traverse();
					Methods.sleep(20);
				}
			} else {
				if (inventory.contains(AutoSmith.MATERIAL.inv_id)) {
					path.traverse();
				} else {
					path_back.traverse();
				}
			}
			break;
		case SMITH:
			final RSObject furnace = objects.getNearest(AutoSmith.location.furnace_id);
			if (furnace != null) {
				if (!furnace.isOnScreen()) {
					camera.turnTo(furnace);
					for (int i = 0; i < 100 && !furnace.isOnScreen(); i++) {
						Methods.sleep(20);
					}
				} else {
					if (!inventory.isItemSelected()) {
						final RSItem ore = inventory.getItem(AutoSmith.MATERIAL.inv_id);
						if (ore != null) {
							ore.doAction("Use");
						}
						for (int i = 0; i < 100 && !inventory.isItemSelected(); i++) {
							Methods.sleep(20);
						}
					} else {
						furnace.doAction("Furnace");
						Methods.sleep(200);
					}
				}
			}
			break;
		case INTERFACE:
			interfaces.get(905).getComponent(14).doAction("All");
			break;
		case SLEEP:
			Methods.sleep(20);
			break;
		}
		return Methods.random(600, 1200);
	}

	@Override
	public void messageReceived(final MessageEvent e) {
		final String message = e.getMessage();
		if (message.contains("You smelt") || message.contains("You")) {
			activity_time = System.currentTimeMillis();
			barsCrafted++;
		}

	}

	@Override
	public void onRepaint(final Graphics g1) {
		final Graphics2D g = (Graphics2D) g1;
		final long millis = System.currentTimeMillis() - startTime;
		final String time = Timer.format(millis);
		if (skills.getCurrentExp(Skills.SMITHING) - startExp > 0
				&& startExp > 0) {
			expGained = skills.getCurrentExp(Skills.SMITHING) - startExp;
		}
		if (expGained > 0 && millis > 0) {
			expHour = (int) (3600 * expGained / millis);
		}
		g.setColor(AutoSmith.COLOR_1);
		g.fillRect(14, 350, 474, 99);
		g.setColor(AutoSmith.COLOR_2);
		g.setStroke(AutoSmith.STROKE);
		g.drawRect(14, 350, 474, 99);
		g.setFont(AutoSmith.FONT_1);
		g.setColor(AutoSmith.COLOR_3);
		g.drawString("AutoSmither", 209, 374);
		g.setFont(AutoSmith.FONT_2);
		g.drawString("EXP/Hr: " + expHour, 18, 390);
		g.drawString("EXP Gained: " + expGained, 18, 400);
		g.drawString("Time Ran: " + time, 182, 390);
		g.drawString("Status: " + getState().toString(), 182, 400);
		g.drawString("Bars Crafted: " + barsCrafted, 395, 390);
	}

	@Override
	public boolean onStart() {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					new Gui().setVisible(true);
				}
			});
		} catch (InterruptedException ignored) {
		} catch (InvocationTargetException ignored) {
		}
		while (AutoSmith.gui_on) {
			Methods.sleep(20);
		}
		if (AutoSmith.location.equals(AutoSmith.LOCATIONS[0])) {
			log("We are smithing in Al-Kharid.");
		} else if (AutoSmith.location.equals(AutoSmith.LOCATIONS[1])) {
			log("We are smithing in Varrock.");
		}
		path = walking.newTilePath(AutoSmith.location.walk_path);
		path_back = walking.newTilePath(AutoSmith.location.walk_path).reverse();
		startExp = skills.getCurrentExp(Skills.SMITHING);
		startTime = System.currentTimeMillis();
		return game.isLoggedIn();
	}

}
