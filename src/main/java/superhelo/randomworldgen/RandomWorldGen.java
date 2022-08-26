package superhelo.randomworldgen;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import superhelo.randomworldgen.world.gen.FeatureConfigHandler;

@Mod(RandomWorldGen.MOD_ID)
public class RandomWorldGen {

	public static final String MOD_ID = "randomworldgen";

	public RandomWorldGen() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(FeatureConfigHandler::setup);
		MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, FeatureConfigHandler::onBiomeLoading);
	}

}
