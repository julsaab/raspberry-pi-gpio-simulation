/*
 * **************************************************************
 * ORGANIZATION: American University of Science & Technology
 * BRANCH: Achrafieh, Lebanon
 * FACULTY: Faculty of Arts and Sciences
 * DEPARTMENT: Department of Information & Communications Technology
 *
 * This file is part of the ICT355 Lab.
 * For more information, please visit the university's website: https://aust.edu.lb.
 * **************************************************************
 * %%
 * Copyright ©2020-2021 American University of Science & Technology - Department of Information & Communications Technology.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package lb.edu.aust.ict355l.raspberrypi.gpio.simulation;

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
