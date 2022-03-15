# GUI Simulated GPIO

## Introduction

This project was created to allow students to simulate General Purpose Input Output (GPIO) triggers on their PC or on a
virtual machine, without the need of having an actual Raspberry Pi. So, simply put, it's Raspberry Pi GPIO Simulator
written in Java.
**This project depends on [Pi4J](https://github.com/Pi4J/pi4j-v1).**

## Usage

There was one idea in mind, it was to make as easy as possible for you to get started. Here's what you need to do to
simulate GPIO correctly:

1. Dependency
   * Go to build.gradle
   * In the dependencies section, paste this
     line: <b>[implementation group: 'com.pi4j', name: 'pi4j-core', version: '1.3'](https://github.com/Pi4J/pi4j-v1) </b>
   * Now click on the "Gradle" tab on the right toolbar at the top
    * Then click on "Reload All Gradle Projects" button
2. The Simulator
    * Copy the Java file "GUISimulatedGpioProvider.java"
    * Paste the previously copied file in your project directory or package
3. Using the Simulator
    * Before creating an instance of GpioController you need to set the GUISimulatedGpioProvider class as the default
      provider
    ```java
   // Set the default provider only if you're not running the program on a Raspberry PI microcontroller
   GpioFactory.setDefaultProvider(new GUISimulatedGpioProvider(() -> GpioFactory.getInstance().shutdown()));
   final GpioController gpio = GpioFactory.getInstance();
    ``` 

That's it!

## Sample Code

```java

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

/**
 * Created on 2/12/2021
 *
 * @author Julien Saab, jps00001@students.aust.edu.lb
 */
public class Main {
	public static void main(String[] args) throws InterruptedException {

		GpioFactory.setDefaultProvider(new GUISimulatedGpioProvider(() -> GpioFactory.getInstance().shutdown()));

		final GpioController gpio = GpioFactory.getInstance();

		// provision gpio pin #01 as an output pin and turn on
		final GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "MyLED", PinState.LOW);
		// set shutdown state for this pin
		pin.setShutdownOptions(true, PinState.LOW);


		final GpioPinDigitalInput inputTest = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04, "INPUT_TEST", PinPullResistance.PULL_DOWN);
		inputTest.setShutdownOptions(true, PinState.LOW, PinPullResistance.PULL_DOWN);

		inputTest.addListener(new GpioPinListenerDigital() {

			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {

				final PinState state = event.getState();

				if (state == PinState.HIGH) {
					pin.high();
				} else {
					pin.low();
				}
			}
		});

		//Keep the program busy.
		//Ths is important when running the program on an actual raspberry pi
		//Because the main will die since there are no user (non daemon) threads running
		do {
			Thread.sleep(2000);
		} while (true);

	}
}

```

----