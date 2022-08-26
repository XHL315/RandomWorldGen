package superhelo.randomworldgen.world.gen;

import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.function.Predicate;

public class ConfigureFeatureHelper {

	private final ConfiguredFeature<?, ?> configuredFeature;
	private final Predicate<BiomeLoadingEvent> predicate;

	public ConfigureFeatureHelper(ConfiguredFeature<?, ?> configuredFeature, Predicate<BiomeLoadingEvent> predicate) {
		this.predicate = predicate;
		this.configuredFeature = configuredFeature;
	}

	public boolean canAddFeature(BiomeLoadingEvent event) {
		return predicate.test(event);
	}

	public ConfiguredFeature<?, ?> getCF() {
		return configuredFeature;
	}

}
