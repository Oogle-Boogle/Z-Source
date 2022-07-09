package com.zamron.util;

import com.zamron.model.GameMode;
import com.zamron.model.Item;
import com.zamron.model.Position;
import com.zamron.world.content.PlayerPanel;
import com.zamron.world.content.combat.CombatContainer.CombatHit;
import com.zamron.world.entity.impl.player.Player;
import org.jboss.netty.buffer.ChannelBuffer;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.SecureRandom;
import java.text.NumberFormat;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

public class Misc {

	public static <T extends Enum<?>> T randomEnum(Class<T> clazz){
		SecureRandom random = new SecureRandom();
		int x = random.nextInt(clazz.getEnumConstants().length);
		return clazz.getEnumConstants()[x];
	}

	public static String toFormattedMS(long time) {
		return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(time),
				TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));
	}

	public static String toFormattedHMS(long time) {
		return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(time), 
				TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time)),
				TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));
	}
	
	public static String convertMsToTime(long ms) {
    	if (ms >= 600000)
    		return String.format("%02d:%02d:%02d", TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(ms)), TimeUnit.MILLISECONDS.toMinutes(ms) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(ms)), TimeUnit.MILLISECONDS.toSeconds(ms) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(ms)));
    	return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(ms) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(ms)), TimeUnit.MILLISECONDS.toSeconds(ms) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(ms)));
	}

	public static String stripIngameFormat(String string) {
		/*string = string.toLowerCase();
		for (int i = 0; i < 31; i++) {
			if (i == 10) {
				System.out.println("Hey, we're  i = 10. Continuing.");
				continue;
			}
			System.out.println("i = "+i);

			if (string.contains("<img="+i+">")) {
				string = string.replaceAll("<img="+i+">", "");
			}
		}*/
		string = string.replaceAll("@pup@", "<col=CE0CED>");
		string = string.replaceAll("@red@", "");
		string = string.replaceAll("@gre@", "");
		string = string.replaceAll("@blu@", "");
		string = string.replaceAll("@yel@", "");
		string = string.replaceAll("@cya@", "");
		string = string.replaceAll("@mag@", "");
		string = string.replaceAll("@whi@", "");
		string = string.replaceAll("@lre@", "");
		string = string.replaceAll("@dre@", "");
		string = string.replaceAll("@bla@", "");
		string = string.replaceAll("@or1@", "");
		string = string.replaceAll("@or2@", "");
		string = string.replaceAll("@or3@", "");
		string = string.replaceAll("@gr1@", "");
		string = string.replaceAll("@gr2@", "");
		string = string.replaceAll("@gr3@", "");
		string = string.replaceAll("@red@", "");
		string = string.replaceAll("@gre@", "");
		string = string.replaceAll("@blu@", "");
		string = string.replaceAll("@yel@", "");
		string = string.replaceAll("@cya@", "");
		string = string.replaceAll("@mag@", "");
		string = string.replaceAll("@whi@", "");
		string = string.replaceAll("@lre@", "");
		string = string.replaceAll("@dre@", "");
		string = string.replaceAll("@bla@", "");
		string = string.replaceAll("@or1@", "");
		string = string.replaceAll("@or2@", "");
		string = string.replaceAll("@or3@", "");
		string = string.replaceAll("@gr1@", "");
		string = string.replaceAll("@gr2@", "");
		string = string.replaceAll("@gr3@", "");
		string = string.replaceAll("@cr1@", "");
		string = string.replaceAll("@cr2@", "");
		string = string.replaceAll("@cr3@", "");
		string = string.replaceAll("@dev@", "");
		string = string.replaceAll("@con@", "");
		string = string.replaceAll("@vet@", "");
		string = string.replaceAll("@mem@", "");
		string = string.replaceAll("@sup@", "");
		string = string.replaceAll("@str@", "~~");
		string = string.replaceAll("@end@", "~~");
		string = string.replaceAll("<img=12>", ":information_source:");
		//string = string.replaceAll("<img=121>", ":star:");
		//string = string.replaceAll("<img=7>", ":moneybag:");
		string = string.replaceAll("@sta@", "");
		string = string.replaceAll("@com@", "");
		string = string.replaceAll("@unc@", "");
		string = string.replaceAll("@rar@", "");
		string = string.replaceAll("@epi@", "");
		string = string.replaceAll("@leg@", "");
		/* emojis */
		string = string.replace(":)", ":smiley:");
		string = string.replace(":(", ":frowning2:");
		string = string.replace(":|", ":neutral_face:");
		//string = string.replaceAll(":D", ":smile:");
		//string = string.replace(":d", ":smile:");
		//string = string.replace(":c", ":frowning:");
		//string = string.replace(":l", ":neutral_face:");
		//string = string.replace(":S", ":blush:");
		//string = string.replace(":s", ":blush:");
		//string = string.replace(":O", ":open_mouth:");
		//string = string.replace(":o", ":open_mouth:");
		string = string.replace(":0", ":open_mouth:");
		string = string.replace(":$", ":blush:");
		string = string.replace(";)", ":wink:");
		string = string.replace(":/", ":rolling_eyes:");
		string = string.replace("(y)", ":thumbsup:");
		string = string.replace("(Y)", ":thumbsup:");
		string = string.replace("(n)", ":thumbsdown:");
		string = string.replace("(N)", ":thumbsdown:");
		//string = string.replace(":p", ":stuck_out_tongue:");
		//string = string.replace(":P", ":stuck_out_tongue:");
		string = string.replace("<3", ":heart:");
		string = string.replace("(L)", ":heart_eyes:");
		string = string.replace(":'(", ":cry:");
		string = string.replace("(a)", ":angel:");
		string = string.replace("(A)", ":angel:");
		//string = string.replace("a q p", ":poop:");
		/* bye emojis */


		while (string.contains("<") && string.contains(">")) {
			int start = string.lastIndexOf("<");
			int end = string.lastIndexOf(">");
			int length = string.length();
			string = string.substring(0, start) + string.substring(end, length);
		}

		while (string.contains("<") && !string.contains(">")) {
			string = string.replaceAll("<", "");
		}

		while (string.contains(">") && !string.contains("<")) {
			string = string.replace(">", "");
		}

		while (string.startsWith(" ")) {
			string = string.substring(1, string.length());
		}

		string = string.replaceAll("@everyone", "@everybody");

		return string;
	}
	
	/**
	 * Added by Emerald(not written tho)
	 */
	
	
	private static final NavigableMap<Long, String> suffixes = new TreeMap<> ();
	static {
	  suffixes.put(1_000L, "k");
	  suffixes.put(1_000_000L, "M");
	  suffixes.put(1_000_000_000L, "B");
	  suffixes.put(1_000_000_000_000L, "T");
	  suffixes.put(1_000_000_000_000_000L, "Q");
	}

	public static final String formatAmount(long amount) {
		String format = "Too high!";
		if (amount >= 0 && amount < 100000) {
			format = String.valueOf(amount);
		} else if (amount >= 100000 && amount < 1000000) {
			format = amount / 1000 + "K";
		} else if (amount >= 1000000 && amount < 1000000000L) {
			format = amount / 1000000 + "M";
		} else if (amount >= 1000000000L && amount < 1000000000000L) {
			format = amount / 1000000000 + "B";
		} else if (amount >= 1000000000000L && amount < 10000000000000000L) {
			format = amount / 1000000000000L + "T";
		} else if (amount >= 10000000000000000L && amount < 1000000000000000000L) {
			format = amount / 1000000000000000L + "QD";
		} else if (amount >= 1000000000000000000L && amount < Long.MAX_VALUE) {
			format = amount / 1000000000000000000L + "QT";
		}
		return format;
	}
	
	public static String formatNumber(long value) {
	  //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
	  if (value == Long.MIN_VALUE) return formatNumber(Long.MIN_VALUE + 1);
	  if (value < 0) return "-" + formatNumber(-value);
	  if (value < 1000) return Long.toString(value); //deal with easy case

	  Entry<Long, String> e = suffixes.floorEntry(value);
	  Long divideBy = e.getKey();
	  String suffix = e.getValue();

	  long truncated = value / (divideBy / 10); //the number part of the output times 10
	  boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
	  return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
	}
	
	public static void deIronman(Player player) {
		
		if(player.getGameMode() == GameMode.IRONMAN) {
			player.setGameMode(GameMode.NORMAL);
			player.getPacketSender().sendIronmanMode(0);
			PlayerPanel.refreshPanel(player);
			player.sendMessage("@red@You are no longer an ironman");
		} else {
			player.sendMessage("@red@Tf dude ur not even an ironman tho?");
			return;
		}
	}

	/** Random instance, used to generate pseudo-random primitive types. */
	public static final Random RANDOM = new Random(System.currentTimeMillis());

	private static ZonedDateTime zonedDateTime;
	public static final int HALF_A_DAY_IN_MILLIS = 43200000;
	
	public static final String sendCashToString(long j) {
        if (j >= 0 && j < 10000)
            return String.valueOf(j);
        else if (j >= 10000 && j < 10000000)
            return j / 1000 + "K";
        else if (j >= 10000000 && j < 999999999)
            return j / 1000000 + "M";
        else
            return Long.toString(j);
    }

	public static final String formatBigNumberLol(long j) {
		if (j >= 0 && j < 10000)
			return String.valueOf(j);
		else if (j >= 10000 && j < 10000000)
			return j / 1000 + "K";
		else if (j >= 10000000 && j < 999999999)
			return j / 1000000 + "M";
		else
			return Long.toString(j);
	}

	public static CombatHit[] concat(CombatHit[] a, CombatHit[] b) {
		int aLen = a.length;
		int bLen = b.length;
		CombatHit[] c= new CombatHit[aLen+bLen];
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);
		return c;
	}

	public static List<Player> getCombinedPlayerList(Player p) {
		List<Player> plrs = new LinkedList<Player>();
		for(Player localPlayer : p.getLocalPlayers()) {
			if(localPlayer != null)
				plrs.add(localPlayer);
		}
		plrs.add(p);
		return plrs;
	}

	public static String getCurrentServerTime() {
		zonedDateTime = ZonedDateTime.now();
		int hour = zonedDateTime.getHour();
		String hourPrefix = hour < 10 ? "0"+hour+"" : ""+hour+"";
		int minute = zonedDateTime.getMinute();
		String minutePrefix = minute < 10 ? "0"+minute+"" : ""+minute+"";
		return ""+hourPrefix+":"+minutePrefix+"";
	}

	public static String getTimePlayed(long totalPlayTime) {
		final int sec = (int) (totalPlayTime / 1000), h = sec / 3600, m = sec / 60 % 60, s = sec % 60;
		return (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s);
	}

	public static int getMinutesPlayed(Player p) {
		long totalPlayTime = p.getTotalPlayTime() + (p.getRecordedLogin().elapsed());
		int sec = (int) (totalPlayTime / 1000), h = sec / 3600, m = sec / 60 % 60;
		for(int i = 0; i < h; i++)
			m+=60;
		return m;
	}

	public static String getHoursPlayed(long totalPlayTime) {
		final int sec = (int) (totalPlayTime / 1000), h = sec / 3600;
		return (h < 10 ? "0" + h : h) + "h";
	}

	public static int getMinutesPassed(long t) {
		int seconds=(int) ((t/1000)%60);
		int minutes=(int) (((t-seconds)/1000)/60);
		return minutes;
	}

	public static Item[] concat(Item[] a, Item[] b) {
		int aLen = a.length;
		int bLen = b.length;
		Item[] c= new Item[aLen+bLen];
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);
		return c;
	}

	public static Player getCloseRandomPlayer(List<Player> plrs) {
		int index = Misc.getRandom(plrs.size() - 1);
		if(index > 0) 
			return plrs.get(index);
		return null;
	}

	public static byte directionDeltaX[] = new byte[]{ 0, 1, 1, 1, 0,-1,-1,-1 };
	public static byte directionDeltaY[] = new byte[]{ 1, 1, 0,-1,-1,-1, 0, 1 };	
	public static byte xlateDirectionToClient[] = new byte[]{ 1, 2, 4, 7, 6, 5, 3, 0 };

	public static int getRandom(int range) {
		return (int) (java.lang.Math.random() * (range + 1));
	}

	public static int direction(int srcX, int srcY, int x, int y) {
		double dx = (double) x - srcX, dy = (double) y - srcY;
		double angle = Math.atan(dy / dx);
		angle = Math.toDegrees(angle);
		if (Double.isNaN(angle))
			return -1;
		if (Math.signum(dx) < 0)
			angle += 180.0;
		return (int) ((((90 - angle) / 22.5) + 16) % 16);
		/*int changeX = x - srcX; int changeY = y - srcY;
		for (int j = 0; j < directionDeltaX.length; j++) {
			if (changeX == directionDeltaX[j] &&
				changeY == directionDeltaY[j])
				return j;

		}
		return -1;*/
	}

	public static String ucFirst(String str) {
		str = str.toLowerCase();
		if (str.length() > 1) {
			str = str.substring(0, 1).toUpperCase() + str.substring(1);
		} else {
			return str.toUpperCase();
		}
		return str;
	}

	public static String format(int num) {
		return NumberFormat.getInstance().format(num);
	}

	public static String formatText(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (i == 0) {
				s = String.format("%s%s", Character.toUpperCase(s.charAt(0)),
						s.substring(1));
			}
			if (!Character.isLetterOrDigit(s.charAt(i))) {
				if (i + 1 < s.length()) {
					s = String.format("%s%s%s", s.subSequence(0, i + 1),
							Character.toUpperCase(s.charAt(i + 1)),
							s.substring(i + 2));
				}
			}
		}
		return s.replace("_", " ");
	}

	public static String getTotalAmount(int j) {
		if (j >= 10000 && j < 10000000) {
			return j / 1000 + "K";
		} else if (j >= 10000000 && j <= 2147483647) {
			return j / 1000000 + "M";
		} else {
			return "" + j + " coins";
		}
	}

	public static String formatPlayerName(String str) {
		String str1 = Misc.ucFirst(str);
		str1.replace("_", " ");
		return str1;
	}

	public static String insertCommasToNumber(String number) {
		return number.length() < 4 ? number : insertCommasToNumber(number
				.substring(0, number.length() - 3))
				+ ","
				+ number.substring(number.length() - 3, number.length());
	}

	private static char decodeBuf[] = new char[4096];

	public static String textUnpack(byte packedData[], int size) {
		int idx = 0, highNibble = -1;
		for (int i = 0; i < size * 2; i++) {
			int val = packedData[i / 2] >> (4 - 4 * (i % 2)) & 0xf;
		if (highNibble == -1) {
			if (val < 13)
				decodeBuf[idx++] = xlateTable[val];
			else
				highNibble = val;
		} else {
			decodeBuf[idx++] = xlateTable[((highNibble << 4) + val) - 195];
			highNibble = -1;
		}
		}

		return new String(decodeBuf, 0, idx);
	}

	public static char xlateTable[] = { ' ', 'e', 't', 'a', 'o', 'i', 'h', 'n',
		's', 'r', 'd', 'l', 'u', 'm', 'w', 'c', 'y', 'f', 'g', 'p', 'b',
		'v', 'k', 'x', 'j', 'q', 'z', '0', '1', '2', '3', '4', '5', '6',
		'7', '8', '9', ' ', '!', '?', '.', ',', ':', ';', '(', ')', '-',
		'&', '*', '\\', '\'', '@', '#', '+', '=', '\243', '$', '%', '"',
		'[', ']' };

	public static String anOrA(String s) {
		s = s.toLowerCase();
		if(s.equalsIgnoreCase("anchovies") || s.equalsIgnoreCase("soft clay") || s.equalsIgnoreCase("cheese") || s.equalsIgnoreCase("ball of wool") || s.equalsIgnoreCase("spice") || s.equalsIgnoreCase("steel nails") || s.equalsIgnoreCase("snape grass") || s.equalsIgnoreCase("coal"))
			return "some";
		if(s.startsWith("a") || s.startsWith("e") || s.startsWith("i") || s.startsWith("o") || s.startsWith("u")) 
			return "an";
		return "a";
	}

	@SuppressWarnings("rawtypes")
	public static Class[] getClasses(String packageName) throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}
		ArrayList<Class> classes = new ArrayList<Class>();
		for (File directory : dirs) {
			classes.addAll(findClasses(directory, packageName));
		}
		return classes.toArray(new Class[classes.size()]);
	}

	@SuppressWarnings("rawtypes")
	private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
		List<Class> classes = new ArrayList<Class>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
			}
		}
		return classes;
	}

	public static int randomMinusOne(int length) {
		return getRandom(length - 1);
	}

	public static String removeSpaces(String s) {
		return s.replace(" ", "");
	}

	public static int getMinutesElapsed(int minute, int hour, int day, int year) {
		Calendar i = Calendar.getInstance();

		if (i.get(1) == year) {
			if (i.get(6) == day) {
				if (hour == i.get(11)) {
					return i.get(12) - minute;
				}
				return (i.get(11) - hour) * 60 + (59 - i.get(12));
			}

			int ela = (i.get(6) - day) * 24 * 60 * 60;
			return ela > 2147483647 ? 2147483647 : ela;
		}

		int ela = getElapsed(day, year) * 24 * 60 * 60;

		return ela > 2147483647 ? 2147483647 : ela;
	}

	public static int getDayOfYear() {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int days = 0;
		int[] daysOfTheMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		if ((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0)) {
			daysOfTheMonth[1] = 29;
		}
		days += c.get(Calendar.DAY_OF_MONTH);
		for (int i = 0; i < daysOfTheMonth.length; i++) {
			if (i < month) {
				days += daysOfTheMonth[i];
			}
		}
		return days;
	}

	public static int getYear() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.YEAR);
	}
	
	/**
	 * @param min
	 *            The minimum integer expected (inclusive)
	 * @param max
	 *            The maximum integer expected (inclusive)
	 * @return A random number min-max inclusive.
	 */
	public static int random(int min, int max) {
		return getRandom(max - min + 1) + min;
	}

	public static int random(int i) {
		return getRandom(i);
	}
	/**
	 * Sets up money representing it as 1000K (1,000,000)
	 * 
	 * @param quantity
	 *            the money
	 * @return the money
	 */
	public static String setupMoney(long quantity) {
		if (quantity < -1) {
			quantity = Long.MAX_VALUE;
		}
		return currency(quantity) + " (" + formatLong(quantity) + " gp)";
	}

	/**
	 * Gets symbol for money
	 * 
	 * @param quantity
	 *            the amount
	 * @return the symbol
	 */
	public static String currency(final long quantity) {
		if (quantity >= 10000 && quantity < 10000000) {
			return quantity / 1000 + "K";
		} else if (quantity >= 10000000 && quantity <= Integer.MAX_VALUE) {
			return quantity / 1000000 + "M";
		} else if (quantity > Integer.MAX_VALUE && quantity <= Long.MAX_VALUE) {
			return quantity / 10000000 + "B";
		} else {
			return "" + quantity + " gp";
		}
	}

	/**
	 * Formats numerals
	 * 
	 * @param num
	 *            the number
	 * @return formated number
	 */
	public static String formatLong(final long num) {
		return NumberFormat.getInstance().format(num);
	}

	public static int random(final int[] collection) {
		return collection[random(collection.length - 1)];
	}

	public static Item randomItem(final Item[] collection) {
		return collection[random(collection.length - 1)];
	}


	public static String random(final String[] collection) {
		return collection[random(collection.length - 1)];
	}

	public static Position random(final Position[] collection) {
		return collection[random(collection.length - 1)];
	}

	public static int getElapsed(int day, int year) {
		if (year < 2013) {
			return 0;
		}

		int elapsed = 0;
		int currentYear = Misc.getYear();
		int currentDay = Misc.getDayOfYear();

		if (currentYear == year) {
			elapsed = currentDay - day;
		} else {
			elapsed = currentDay;

			for (int i = 1; i < 5; i++) {
				if (currentYear - i == year) {
					elapsed += 365 - day;
					break;
				} else {
					elapsed += 365;
				}
			}
		}

		return elapsed;
	}

	public static boolean isWeekend() {
		int day = Calendar.getInstance().get(7);
		return (day == 1) || (day == 6) || (day == 7);
	}

	/**
	 * Returns a pseudo-random {@code int} value between inclusive
	 * <code>min</code> and exclusive <code>max</code>.
	 * 
	 * <br>
	 * <br>
	 * This method is thread-safe. </br>
	 * 
	 * @param min
	 *            The minimum inclusive number.
	 * @param max
	 *            The maximum exclusive number.
	 * @return The pseudo-random {@code int}.
	 * @throws IllegalArgumentException
	 *             If the specified range is less <tt>0</tt>
	 */
	public static int exclusiveRandom(int min, int max) {
		if (max <= min) {
			max = min + 1;
		}
		return RANDOM.nextInt((max - min)) + min;
	}

	/**
	 * Returns a pseudo-random {@code int} value between inclusive <tt>0</tt>
	 * and exclusive <code>range</code>.
	 * 
	 * <br>
	 * <br>
	 * This method is thread-safe. </br>
	 * 
	 * @param range
	 *            The exclusive range.
	 * @return The pseudo-random {@code int}.
	 * @throws IllegalArgumentException
	 *             If the specified range is less <tt>0</tt>
	 */
	public static int exclusiveRandom(int range) {
		return exclusiveRandom(0, range);
	}

	/**
	 * Returns a pseudo-random {@code int} value between inclusive
	 * <code>min</code> and inclusive <code>max</code>.
	 * 
	 * @param min
	 *            The minimum inclusive number.
	 * @param max
	 *            The maximum inclusive number.
	 * @return The pseudo-random {@code int}.
	 * @throws IllegalArgumentException
	 *             If {@code max - min + 1} is less than <tt>0</tt>.
	 * @see {@link #exclusiveRandom(int)}.
	 */
	public static int inclusiveRandom(int min, int max) {
		if (max < min) {
			max = min + 1;
		}
		return exclusiveRandom((max - min) + 1) + min;
	}

	/**
	 * Returns a pseudo-random {@code int} value between inclusive <tt>0</tt>
	 * and inclusive <code>range</code>.
	 * 
	 * @param range
	 *            The maximum inclusive number.
	 * @return The pseudo-random {@code int}.
	 * @throws IllegalArgumentException
	 *             If {@code max - min + 1} is less than <tt>0</tt>.
	 * @see {@link #exclusiveRandom(int)}.
	 */
	public static int inclusiveRandom(int range) {
		return inclusiveRandom(0, range);
	}

	public static byte[] readFile(File s) {
		try {
			FileInputStream fis = new FileInputStream(s);
			FileChannel fc = fis.getChannel();
			ByteBuffer buf = ByteBuffer.allocate((int) fc.size());
			fc.read(buf);
			buf.flip();
			fis.close();
			return buf.array();
		} catch (Exception e) {
			//System.out.println("FILE : " + s.getName() + " missing.");
			return null;
		}
	}

	public static byte[] getBuffer(File f) throws Exception
	{
		if(!f.exists())
			return null;
		byte[] buffer = new byte[(int) f.length()];
		DataInputStream dis = new DataInputStream(new FileInputStream(f));
		dis.readFully(buffer);
		dis.close();
		byte[] gzipInputBuffer = new byte[999999];
		int bufferlength = 0;
		GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(buffer));
		do {
			if(bufferlength == gzipInputBuffer.length)
			{
				//System.out.println("Error inflating data.\nGZIP buffer overflow.");
				break;
			}
			int readByte = gzip.read(gzipInputBuffer, bufferlength, gzipInputBuffer.length - bufferlength);
			if(readByte == -1)
				break;
			bufferlength += readByte;
		} while(true);
		byte[] inflated = new byte[bufferlength];
		System.arraycopy(gzipInputBuffer, 0, inflated, 0, bufferlength);
		buffer = inflated;
		if(buffer.length < 10)
			return null;
		return buffer;
	}

	public static int getTimeLeft(long start, int timeAmount, TimeUnit timeUnit) {
		start -= timeUnit.toMillis(timeAmount);
		long elapsed = System.currentTimeMillis() - start;
		int toReturn = timeUnit == TimeUnit.SECONDS ? (int) ((elapsed / 1000) % 60) - timeAmount : (int) ((elapsed / 1000) / 60) - timeAmount;
		if(toReturn <= 0)
			toReturn = 1;
		return timeAmount - toReturn;
	}

	/**
	 * Converts an array of bytes to an integer.
	 * 
	 * @param data
	 *            the array of bytes.
	 * @return the newly constructed integer.
	 */
	public static int hexToInt(byte[] data) {
		int value = 0;
		int n = 1000;
		for (int i = 0; i < data.length; i++) {
			int num = (data[i] & 0xFF) * n;
			value += num;
			if (n > 1) {
				n = n / 1000;
			}
		}
		return value;
	}

	public static Position delta(Position a, Position b) {
		return new Position(b.getX() - a.getX(), b.getY() - a.getY());
	}

	/**
	 * Picks a random element out of any array type.
	 * 
	 * @param array
	 *            the array to pick the element from.
	 * @return the element chosen.
	 */
	public static <T> T randomElement(T[] array) {
		return array[(int) (RANDOM.nextDouble() * array.length)];
	}

	/**
	 * Picks a random element out of any list type.
	 * 
	 * @param list
	 *            the list to pick the element from.
	 * @return the element chosen.
	 */
	public static <T> T randomElement(List<T> list) {
		return list.get((int) (RANDOM.nextDouble() * list.size()));
	}

	/**
	 * Reads string from a data input stream.
	 * @param inputStream 	The input stream to read string from.
	 * @return 				The String value.
	 */
	public static String readString(ChannelBuffer buffer) {
		StringBuilder builder = null;
		
		
		
		try {
			@SuppressWarnings("unused")
			byte data;
			builder = new StringBuilder();
			byte b;
			while ((b = buffer.readByte()) != 10 && b < 200) {
				builder.append((char) b);
			}
		} catch(IndexOutOfBoundsException e) {

		}
		return builder.toString();
	}
	
	public static String readString(Player player, ChannelBuffer buffer) {
		StringBuilder builder = null;
		//int size = buffer.readByte();
		
		//if (size > Byte.MAX_VALUE) {
		//	World.sendStaffMessage("<col=ff0000>WARNING: "+player.getUsername()+" is trying to send invalid packet values! (exploiting)");
		//	return null;
		//}
		try {
			@SuppressWarnings("unused")
			byte data;
			builder = new StringBuilder();
			byte b;
			while ((b = buffer.readByte()) != 10 && b < 200) {
				builder.append((char) b);
			}
		} catch(IndexOutOfBoundsException e) {

		}
		return builder.toString();
	}

	public static final String[] BLOCKED_WORDS = {
		"devious", "firepk", "ventrilica", "ventrili",
		"hade5", "hades5", "pvplegacy", "junglepk", "runeinsanit",
		"n3t", "n e t", "n et", "ne t", "c  0 m", "c 0 m", "C 0  m",
		"c  o  m", "runeinsanity", "runeinsanit", "runeinsan",
		"rune-insanity", "rune-insanit", ".c0", ".c0m",
		"legendsdomain", "createaforum", "vampirez", "-scape",  "os-base",
		"rswebclients", "pvplegacy", "junglepk", ".n3t", ".c0m", "c0m",
		"n3t", ".tk", ",net", ",runescape", "pvpmaster", "pvpmasters",
		"PvPMasters.", ",org", ",com", ",be", ",nl", ",info", "dspk",
		".info", ".org", ".tk", ".net", ".com", ".co.uk", ".be", ".nl",
		".dk", ".co.cz", ".co", ".us", ".biz", ".eu", ".de", ".cc", ". n e  t",
		".i n f o", ".o r g", ".t k", ".n e t", ".c o m", ".c o . u k",
		".b e", ".n l", ".d k", ".c o . c z", ".c o", ".u s", ".b i z",
		".e u", ".d e", ".c c", ".(c)om", "(.)", "kandarin", "o r g", "www", 
		"w w w", "w.w.w", "voidrsps", "void-ps", "desolace", "ikov", "soulsplit", 
		"soulspawn", "atiloc", "<img", "@cr", "i k o v", "ik0v", "<img=", "@cr", ":tradereq:", ":duelreq:",
		"<col=", "<shad=", "hostilityps", "hostilityp", "hostility ps"
		, "0s-base", "os -base", "os - base", "o s b a s e", "o s - b a s e" };


	public static boolean blockedWord(String string) {
		for(String s : BLOCKED_WORDS) {
			if(string.contains(s)) {
				return true;
			}
		}
		return false;
	}
	
	public static String filterMessage(Player player, String message) {
		for (String auto : BLOCKED_WORDS) {
			if (message.toLowerCase().contains(auto) && !player.getRights().isSeniorStaff()) {
				String replace = "";
				for (int i = 0; i < auto.length(); i++)
					replace += "*";
				message = message.replaceAll(auto, replace);
			}
		}
		return message;
	}

	public static double getDoubleRoundedUp(double doubleNumber) {
		return Math.ceil(doubleNumber);
	}

	public static double getDoubleRoundedDown(double doubleNumber) {
		return (double) ((int) doubleNumber);
	}
}
