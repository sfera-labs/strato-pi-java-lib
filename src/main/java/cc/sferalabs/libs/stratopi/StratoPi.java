/**
 * 
 */
package cc.sferalabs.libs.stratopi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class StratoPi {

	public static final int GPIO_WATCHDOG_ENABLE = 6;
	public static final int GPIO_WATCHDOG_HEARTBEAT = 5;
	public static final int GPIO_WATCHDOG_TIMEOUT = 12;
	public static final int GPIO_SHUTDOWN = 16;
	public static final int GPIO_BUZZER = 20;
	public static final int GPIO_BATTERY = 26;

	/**
	 * @throws IOException
	 */
	public StratoPi() throws IOException {
		writeExport(GPIO_WATCHDOG_ENABLE);
		writeExport(GPIO_WATCHDOG_HEARTBEAT);
		writeExport(GPIO_WATCHDOG_TIMEOUT);
		writeExport(GPIO_SHUTDOWN);
		writeExport(GPIO_BUZZER);
		writeExport(GPIO_BATTERY);

		writeDirection(GPIO_WATCHDOG_ENABLE, "out");
		writeDirection(GPIO_WATCHDOG_HEARTBEAT, "out");
		writeDirection(GPIO_WATCHDOG_TIMEOUT, "in");
		writeDirection(GPIO_SHUTDOWN, "out");
		writeDirection(GPIO_BUZZER, "out");
		writeDirection(GPIO_BATTERY, "in");
	}

	/**
	 * @throws IOException
	 */
	private void writeExport(int gpio) throws IOException {
		if (!Files.isDirectory(Paths.get("/sys/class/gpio/gpio" + gpio))) {
			try (BufferedWriter writer = Files.newBufferedWriter(
					Paths.get("/sys/class/gpio/export"), StandardCharsets.US_ASCII)) {
				writer.write("" + gpio);
			}
		}
	}

	/**
	 * @param gpio
	 * @param dir
	 * @throws IOException
	 */
	private void writeDirection(int gpio, String dir) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(
				Paths.get("/sys/class/gpio/gpio" + gpio + "/direction"),
				StandardCharsets.US_ASCII)) {
			writer.write(dir);
		}
	}

	/**
	 * @param gpio
	 * @return
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	private boolean getValue(int gpio) throws IOException, IllegalStateException {
		try (BufferedReader reader = Files.newBufferedReader(
				Paths.get("/sys/class/gpio/gpio" + gpio + "/value"), StandardCharsets.US_ASCII)) {
			int val = reader.read();
			switch (val) {
			case '0':
				return false;
			case '1':
				return true;
			default:
				throw new IllegalStateException("" + val);
			}
		}
	}

	/**
	 * @param gpio
	 * @param high
	 * @throws IOException
	 */
	private void setValue(int gpio, boolean high) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(
				Paths.get("/sys/class/gpio/gpio" + gpio + "/value"), StandardCharsets.US_ASCII)) {
			writer.write(high ? '1' : '0');
		}
	}

	/**
	 * @return
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public boolean getWatchdogEnable() throws IllegalStateException, IOException {
		return getValue(GPIO_WATCHDOG_ENABLE);
	}

	/**
	 * @return
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public boolean getWatchdogHeartbeat() throws IllegalStateException, IOException {
		return getValue(GPIO_WATCHDOG_HEARTBEAT);
	}

	/**
	 * @return
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public boolean getWatchdogTimeout() throws IllegalStateException, IOException {
		return getValue(GPIO_WATCHDOG_TIMEOUT);
	}

	/**
	 * @return
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public boolean getShutdown() throws IllegalStateException, IOException {
		return getValue(GPIO_SHUTDOWN);
	}

	/**
	 * @return
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public boolean getBuzzer() throws IllegalStateException, IOException {
		return getValue(GPIO_BUZZER);
	}

	/**
	 * @return
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public boolean getBattery() throws IllegalStateException, IOException {
		return getValue(GPIO_BATTERY);
	}

	/**
	 * @param enabled
	 * @throws IOException
	 */
	public void setWatchdogEnable(boolean enabled) throws IOException {
		setValue(GPIO_WATCHDOG_ENABLE, enabled);
	}

	/**
	 * @param high
	 * @throws IOException
	 */
	public void setWatchdogHeartbeat(boolean high) throws IOException {
		setValue(GPIO_WATCHDOG_HEARTBEAT, high);
	}

	/**
	 * @throws IOException
	 */
	public void heartbeat() throws IOException {
		setWatchdogHeartbeat(!getWatchdogHeartbeat());
	}

	/**
	 * @throws IOException
	 */
	public void shutdown() throws IOException {
		setValue(GPIO_SHUTDOWN, true);
	}

	/**
	 * @param on
	 * @throws IOException
	 */
	public void setBuzzer(boolean on) throws IOException {
		setValue(GPIO_BUZZER, on);
	}

}
