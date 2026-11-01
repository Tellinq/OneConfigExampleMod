package org.polyfrost.example.client;

import org.polyfrost.example.ExampleConstants;
import org.polyfrost.oneconfig.api.config.v1.Config;
import org.polyfrost.oneconfig.api.config.v1.ValueFormatter;
import org.polyfrost.oneconfig.api.config.v1.annotations.Dropdown;
import org.polyfrost.oneconfig.api.config.v1.annotations.MasterToggle;
import org.polyfrost.oneconfig.api.config.v1.annotations.Slider;
import org.polyfrost.oneconfig.api.config.v1.annotations.Switch;

/**
 * The main Config entrypoint that extends the Config type and initializes your config options.
 * See <a href="https://docsv1.polyfrost.org/configuration/available-options">this link</a> for more config Options
 */
public class ExampleConfig extends Config {

    // The switch shown on the mod's card in the OneConfig menu. It's saved but doesn't appear on the config page.
    @MasterToggle
    public static boolean enabled = true;

    @Switch(title = "Example Switch")
    public static boolean exampleSwitch = false; // The default value for the boolean Switch.

    @Slider(
            title = "Example Slider",
            min = 0f, max = 100f, // Minimum and maximum values for the slider.
            step = 10, // The amount of steps that the slider should have.
            formatter = PercentFormatter.class // Changes how the value is displayed.
    )
    public static float exampleSlider = 50f; // The default value for the float Slider.

    @Dropdown(
            title = "Example Dropdown", // Name of the Dropdown
            options = {"Option 1", "Option 2", "Option 3", "Option 4"} // Options available.
    )
    public static int exampleDropdown = 1; // Default option (in this case "Option 2")

    public static final ExampleConfig INSTANCE = new ExampleConfig(); // The instance of the Config.

    public ExampleConfig() {
        super(ExampleConstants.ID + ".json", ExampleConstants.NAME, Category.OTHER); // TODO: Change your category here.
    }

    // Shows the slider's value as a percentage. OneConfig creates it through its no-argument constructor.
    public static class PercentFormatter implements ValueFormatter {
        @Override
        public String format(Number value) {
            return value.intValue() + "%";
        }
    }

}

