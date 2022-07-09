package com.zamron;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.zamron.engine.task.Task;
import com.zamron.engine.task.TaskManager;
import com.zamron.engine.task.impl.PlayerDeathTask;
import com.zamron.model.Animation;
import com.zamron.model.PlayerRights;
import com.zamron.model.Position;
import com.zamron.world.World;
import com.zamron.world.content.PlayerPunishment;
import com.zamron.world.content.WellOfGoodwill;
import com.zamron.world.content.PlayerPunishment.Jail;
import com.zamron.world.content.clan.ClanChatManager;
import com.zamron.world.content.grandexchange.GrandExchangeOffers;
import com.zamron.world.entity.impl.player.Player;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.awt.event.*;





public class Panel extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6688741257614555809L;
	private JPanel contentPane;
	private JTextField textField;
	
	

	/**
	 * Launch the application.
	 */
	public static void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Panel frame = new Panel();
				    // UIManager.setLookAndFeel("org.pushingpixals.substance.api.skin.SubstanceTwilightLookAndFeel");  
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
	public Panel() {
		setTitle("Control Panel");
		setBounds(100, 100, 408, 456);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(87, 25, 194, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(156, 11, 92, 14);
		contentPane.add(lblUsername);
		
		JLabel lblPunishment = new JLabel("Punishment");
		lblPunishment.setBounds(73, 60, 104, 14);
		contentPane.add(lblPunishment);
		
		/*JProgressBar progressBar = new JProgressBar();
		progressBar.setBounds(10, 372, 372, 35);
		contentPane.add(progressBar);
		progressBar.setIndeterminate(true);*/
		
		JButton btnIpban = new JButton("IPBan");
		btnIpban.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				IPban();
			}
		});
		btnIpban.setBounds(0, 85, 89, 23);
		contentPane.add(btnIpban);
		
		JButton btnMute = new JButton("Mute");
		btnMute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mute();
			}
		});
		btnMute.setBounds(99, 85, 89, 23);
		contentPane.add(btnMute);
		
		JButton btnBan = new JButton("Ban");
		btnBan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ban();
			}
		});
		btnBan.setBounds(0, 119, 89, 23);
		contentPane.add(btnBan);
		
		JButton btnJail = new JButton("Jail");
		btnJail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				jail();
			}
		});
		btnJail.setBounds(0, 153, 89, 23);
		contentPane.add(btnJail);
		
		JButton btnKill = new JButton("Kill");
		btnKill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				kill();
			}
		});
		btnKill.setBounds(99, 119, 89, 23);
		contentPane.add(btnKill);
		
		JButton btnFreezeunfreeze = new JButton("Freeze");
		btnFreezeunfreeze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				freeze();
			}
		});
		btnFreezeunfreeze.setBounds(99, 153, 89, 23);
		contentPane.add(btnFreezeunfreeze);
		
		JLabel lblItemManagment = new JLabel("Item Managment");
		lblItemManagment.setBounds(261, 60, 104, 14);
		contentPane.add(lblItemManagment);
		
		JButton btnGiveItem = new JButton("Give Item");
		btnGiveItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				giveItem();
			}
		});
		
		btnGiveItem.setBounds(204, 85, 89, 23);
		contentPane.add(btnGiveItem);
		/*
		JButton btnTakeItem = new JButton("Take Item");
		btnTakeItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				takeItem();
			}
		});
		
		btnTakeItem.setBounds(303, 85, 89, 23);
		contentPane.add(btnTakeItem);
		*/
		JButton btnGiveAll = new JButton("Give All");
		btnGiveAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				giveAll();
			}
		});
		
		btnGiveAll.setBounds(204, 119, 89, 23);
		contentPane.add(btnGiveAll);
		/*
		JButton btnTakeAll = new JButton("Take All");
		btnTakeAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				takeAll();
			}
		});
		
		btnTakeAll.setBounds(303, 119, 89, 23);
		contentPane.add(btnTakeAll);
		*/
		JLabel lblTeleportation = new JLabel("Teleportation");
		lblTeleportation.setBounds(73, 206, 104, 14);
		contentPane.add(lblTeleportation);
		
		JButton btnTeleport = new JButton("Teleport");
		btnTeleport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				teleport();
			}
		});
		btnTeleport.setBounds(57, 231, 89, 23);
		contentPane.add(btnTeleport);
		
		JButton btnSendhome = new JButton("Tele All");
		btnSendhome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				teleAll();
			}
		});
		btnSendhome.setBounds(57, 265, 89, 23);
		contentPane.add(btnSendhome);
		
		JLabel lblFunPanel = new JLabel("Fun Panel");
		lblFunPanel.setBounds(261, 177, 56, 14);
		contentPane.add(lblFunPanel);
		
		JButton btnMakeDance = new JButton("Make Dance");
		btnMakeDance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				makeDance();
			}
		});
		btnMakeDance.setBounds(231, 202, 114, 23);
		contentPane.add(btnMakeDance);
		
		JButton btnDanceAll = new JButton("Dance All");
		btnDanceAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				danceAll();
			}
		});
		btnDanceAll.setBounds(231, 236, 114, 23);
		contentPane.add(btnDanceAll);
		
		JButton btnForceChat = new JButton("Force Chat");
		btnForceChat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				forceChat();
			}
		});
		btnForceChat.setBounds(231, 270, 114, 23);
		contentPane.add(btnForceChat);
		
		JButton btnSmite = new JButton("Promote: Mod");
		btnSmite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				smite();
			}
		});
		btnSmite.setBounds(231, 304, 114, 23);
		contentPane.add(btnSmite);
		
		JButton btnFuckUp = new JButton("Promote: Donator");
		btnFuckUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fuckUp();
			}
		});
		btnFuckUp.setBounds(231, 338, 114, 23);
		contentPane.add(btnFuckUp);
		
		JButton btnNewButton = new JButton("Shutdown Server");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				shutdown();
			}
		});
		btnNewButton.setBounds(10, 299, 194, 62);
		contentPane.add(btnNewButton);
		
	}
	
	public String getUsernameInput() {
		return textField.getText();
	}
	
	public void ban() {
		String name = getUsernameInput();
	Player target = World.getPlayerByName(name);
	if (target != null) {
		PlayerPunishment.ban(name);
		System.out.println("Console: Successfully Banned "
				+ name + ".");
		JOptionPane.showMessageDialog(null, "Successfully Banned "+name, "Console", JOptionPane.PLAIN_MESSAGE);
	} else {
		JOptionPane.showMessageDialog(null, name+" Doesn't Exist", "Console", JOptionPane.ERROR_MESSAGE);
		System.out.println("Console: Player Doesn't Exist");
		}
	}

	public void forceChat() {
		String name = getUsernameInput();
	Player target = World.getPlayerByName(name);
	if (target != null) {
		String chat = JOptionPane.showInputDialog("What Do You Want Him To Say?");
		target.forceChat(chat);
		System.out.println("Console: Forcing "+name+" To Say "+chat+"!");
		JOptionPane.showMessageDialog(null, "Forcing "+name+" To Say "+chat+"!", "Console", JOptionPane.PLAIN_MESSAGE);
	} else {
		JOptionPane.showMessageDialog(null, name+" Doesn't Exist", "Console", JOptionPane.ERROR_MESSAGE);
		System.out.println("Console: Player Doesn't Exist");
		}
	}
	
	public void shutdown() {
		String name = getUsernameInput();
	World.getPlayerByName(name);
		String sht = JOptionPane.showInputDialog("Shudown Delay?");
		int delay = Integer.parseInt(sht);
		TaskManager.submit(new Task(delay) {
			@Override
			protected void execute() {
				for (Player player : World.getPlayers()) {
					if (player != null) {
						World.deregister(player);
					}
				}
				WellOfGoodwill.save();
				GrandExchangeOffers.save();
				ClanChatManager.save();
				GameServer.getLogger().info("Update task finished!");
				stop();
			}
		});
		System.out.println("Console: Shutting Server Down!");
		JOptionPane.showMessageDialog(null, "Shutting Server Down!", "Console", JOptionPane.PLAIN_MESSAGE);
	}
	
	public void makeDance() {
		String name = getUsernameInput();
	Player target = World.getPlayerByName(name);
	if (target != null) {
		target.setAnimation(new Animation(7071));
		System.out.println("Console: You Have Made "+name+" Dance!");
		JOptionPane.showMessageDialog(null, "You Have Made "+name+" Dance!", "Console", JOptionPane.PLAIN_MESSAGE);
	} else {
		JOptionPane.showMessageDialog(null, name+" Doesn't Exist", "Console", JOptionPane.ERROR_MESSAGE);
		System.out.println("Console: Player Doesn't Exist");
		}
	}
	
	public void smite() {
		String name = getUsernameInput();
	Player target = World.getPlayerByName(name);
	if (target != null) {
		target.setRights(PlayerRights.forId(1));
		System.out.println("Console: You Have promoted "+name+"!");
		JOptionPane.showMessageDialog(null, "You Are A Mod, "+name+"!", "Console", JOptionPane.PLAIN_MESSAGE);
	} else {
		JOptionPane.showMessageDialog(null, name+" Doesn't Exist", "Console", JOptionPane.ERROR_MESSAGE);
		System.out.println("Console: Player Doesn't Exist");
		}
	}
	
	public void fuckUp() {
		String name = getUsernameInput();
	Player target = World.getPlayerByName(name);
	if (target != null) {
		target.setRights(PlayerRights.forId(5));
		System.out.println("Console: You Have Made "+name+" a donator!");
		JOptionPane.showMessageDialog(null, "You Have promoted "+name+"!", "Console", JOptionPane.PLAIN_MESSAGE);
	} else {
		JOptionPane.showMessageDialog(null, name+" Doesn't Exist", "Console", JOptionPane.ERROR_MESSAGE);
		System.out.println("Console: Player Doesn't Exist");
		}
	}
	
	public void IPban() {
		String name = getUsernameInput();
	Player target = World.getPlayerByName(name);
	if (target != null) {
		final String bannedIP = target.getHostAddress();
		PlayerPunishment.addBannedIP(bannedIP);
		System.out.println("Console: Successfully IP Banned "
				+ name + ".");
		JOptionPane.showMessageDialog(null, "Successfully IP Banned "+name, "Console", JOptionPane.PLAIN_MESSAGE);
	} else {
		JOptionPane.showMessageDialog(null, name+" Doesn't Exist!", "Console", JOptionPane.ERROR_MESSAGE);
		System.out.println("Console: Player Doesn't Exist!");
		}
	}
	
	public void mute() {
		String name = getUsernameInput();
		//String player2 = Misc.formatText(wholeCommand.substring(5));
	Player target = World.getPlayerByName(name);
	if (target != null) {
		PlayerPunishment.mute(name);
		System.out.println("Console: Muted "
				+ name + ".");
		JOptionPane.showMessageDialog(null, "Successfully Muted "+name, "Console", JOptionPane.PLAIN_MESSAGE);
	} else {
		JOptionPane.showMessageDialog(null, name+" Doesn't Exist!", "Console", JOptionPane.ERROR_MESSAGE);
		System.out.println("Console: Player Doesn't Exist!");
		}
	}
	
	public void kill() {
		String name = getUsernameInput();
	Player target = World.getPlayerByName(name);
	if (target != null) {
		TaskManager.submit(new PlayerDeathTask(target));
		System.out.println("Console: Killed "
				+ name + ".");
		JOptionPane.showMessageDialog(null, "Successfully Killed "+name, "Console", JOptionPane.PLAIN_MESSAGE);
	} else {
		JOptionPane.showMessageDialog(null, name+" Doesn't Exist!", "Console", JOptionPane.ERROR_MESSAGE);
		System.out.println("Console: Player Doesn't Exist!");
		}
	}
	public void jail() {
		String name = getUsernameInput();
	Player target = World.getPlayerByName(name);
	if (target != null) {
		Jail.jailPlayer(target);
		System.out.println("Console: Jailed "
				+ name + ".");
		JOptionPane.showMessageDialog(null, "Successfully Jailed "+name, "Console", JOptionPane.PLAIN_MESSAGE);
	} else {
		JOptionPane.showMessageDialog(null, name+" Doesn't Exist!", "Console", JOptionPane.ERROR_MESSAGE);
		System.out.println("Console: Player Doesn't Exist!");
		}
	}
	
	public void freeze() {
		String name = getUsernameInput();
	Player target = World.getPlayerByName(name);
	if (target != null) {
		target.isFrozen();
		System.out.println("Console: Frozen "
				+ name + ".");
		JOptionPane.showMessageDialog(null, "Successfully Frozen "+name, "Console", JOptionPane.PLAIN_MESSAGE);
	} else {
		JOptionPane.showMessageDialog(null, name+" Doesn't Exist!", "Console", JOptionPane.ERROR_MESSAGE);
		System.out.println("Console: Player Doesn't Exist!");
		}
	}
	
	public void giveItem() {
		String name = getUsernameInput();
	Player target = World.getPlayerByName(name);
	if (target != null) {
		String id = JOptionPane.showInputDialog("Item Id");
		String quantity = JOptionPane.showInputDialog("Item Amount");
		int item = Integer.parseInt(id);
		int amount = Integer.parseInt(quantity);
		target.getInventory().add(item, amount);
		System.out.println("Console: Given Item To "
				+ name + ".");
		JOptionPane.showMessageDialog(null, "Given Item To "+name, "Console", JOptionPane.PLAIN_MESSAGE);
	} else {
		JOptionPane.showMessageDialog(null, name+" Doesn't Exist!", "Console", JOptionPane.ERROR_MESSAGE);
		System.out.println("Console: Player Doesn't Exist!");
		}
	}
	
	public void teleport() {
		String name = getUsernameInput();
	Player target = World.getPlayerByName(name);
	if (target != null) {
		String x = JOptionPane.showInputDialog("Coordinate X");
		String y = JOptionPane.showInputDialog("Coordinate Y");
		String h = JOptionPane.showInputDialog("Height Level");
		int coordx = Integer.parseInt(x);
		int coordy = Integer.parseInt(y);
		int height = Integer.parseInt(h);
		Position position = new Position(coordx, coordy, height);
		target.moveTo(position);
		System.out.println("Console: Teleported "+name+" To "+coordx+", "+coordy+", "+height);
		JOptionPane.showMessageDialog(null, "Console: Teleported "+name+" To "+coordx+", "+coordy+", "+height, "Console", JOptionPane.PLAIN_MESSAGE);
	} else {
		JOptionPane.showMessageDialog(null, name+" Doesn't Exist!", "Console", JOptionPane.ERROR_MESSAGE);
		System.out.println("Console: Player Doesn't Exist!");
		}
	}
	
	public void teleAll() {
		String name = getUsernameInput();
	World.getPlayerByName(name);
		String x = JOptionPane.showInputDialog("Coordinate X");
		String y = JOptionPane.showInputDialog("Coordinate Y");
		String h = JOptionPane.showInputDialog("Height Level");
		int coordx = Integer.parseInt(x);
		int coordy = Integer.parseInt(y);
		int height = Integer.parseInt(h);
		for (Player teleall : World.getPlayers()) {
			Position position = new Position(coordx, coordy, height);
			teleall.moveTo(position);
		}
		System.out.println("Console: Teleported Everyone To "+coordx+", "+coordy+", "+height);
		JOptionPane.showMessageDialog(null, "Console: Teleported Everyone To "+coordx+", "+coordy+", "+height, "Console", JOptionPane.PLAIN_MESSAGE);
}
	
	public void danceAll() {

		for (Player players : World.getPlayers()) {
			if (players == null)
				continue;
			players.setAnimation(new Animation(7071));
			players.forceChat("[NEW PLAYER] Welcome to Zamron");
      }
	}
	
	public void giveAll() {
		String name = getUsernameInput();
	World.getPlayerByName(name);
		String id = JOptionPane.showInputDialog("Item Id");
		String quantity = JOptionPane.showInputDialog("Item Amount");
		int item = Integer.parseInt(id);
		int amount = Integer.parseInt(quantity);
		for (Player giveall : World.getPlayers()) {
			giveall.getInventory().add(item, amount);
		}
		System.out.println("Console: Given Item "+item+" To All Players");
		JOptionPane.showMessageDialog(null, "Given Item "+item+" To All Players", "Console", JOptionPane.PLAIN_MESSAGE);
	}

}