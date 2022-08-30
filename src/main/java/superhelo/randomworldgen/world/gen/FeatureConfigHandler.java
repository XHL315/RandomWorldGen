package superhelo.randomworldgen.world.gen;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureSpread;
import net.minecraft.world.gen.feature.Features.Placements;
import net.minecraft.world.gen.feature.TwoLayerFeature;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import superhelo.randomworldgen.RandomWorldGen;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import javax.annotation.Nonnull;

public class FeatureConfigHandler {

	private static final List<ConfigureFeatureHelper> HELPERS = new ArrayList<>();

	public static void setup(FMLCommonSetupEvent e) {
		e.enqueueWork(() -> {
			register("dark_wood_tree",
					Feature.TREE.configured(new BaseTreeFeatureConfig.Builder(
							new SimpleBlockStateProvider(
									defaultBlockState("druidcraft", "darkwood_log").setValue(RotatedPillarBlock.AXIS, Axis.Y)),
									blockStateProvider(getBlock("druidcraft", "darkwood_leaves")
									),
							new BlobFoliagePlacer(FeatureSpread.fixed(3), FeatureSpread.fixed(0), 4),
							new StraightTrunkPlacer(7, 3, 0),
							new TwoLayerFeature(1, 0, 1)).ignoreVines().build()).decorated(Placements.HEIGHTMAP_SQUARE),
					(event) -> event.getName() != null && event.getName().equals(new ResourceLocation("minecraft", "swamp"))
			);
			register("simplytea_sapling",
					Feature.RANDOM_PATCH.configured(new BlockClusterFeatureConfig.Builder(
							new SimpleBlockStateProvider(defaultBlockState("simplytea", "tea_sapling")),
							new SimpleBlockPlacer()
					).tries(10).build()).decorated(Placements.HEIGHTMAP_SQUARE),
					(event) -> event.getName() != null && event.getName().equals(new ResourceLocation("minecraft", "swamp"))
			);
			register("tea_kettle_bush",
					Feature.RANDOM_PATCH.configured(new BlockClusterFeatureConfig.Builder(
							new SimpleBlockStateProvider(defaultBlockState("tea_kettle", "tea_bush")),
							new SimpleBlockPlacer()
					).tries(10).build()).decorated(Placements.HEIGHTMAP_SQUARE),
					(event) -> event.getName() != null && event.getName().equals(new ResourceLocation("minecraft", "swamp"))
			);
		});
	}

	public static void onBiomeLoading(BiomeLoadingEvent event) {
		HELPERS.forEach(helper -> {
			if (helper.canAddFeature(event)) {
				event.getGeneration().addFeature(Decoration.VEGETAL_DECORATION, helper.getCF());
			}
		});
	}

	/**
	 * @param predicate Whether to generate in the current biome
	 */
	private static void register(String id, ConfiguredFeature<?, ?> config, Predicate<BiomeLoadingEvent> predicate) {
		HELPERS.add(new ConfigureFeatureHelper(config, predicate));
		Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, RandomWorldGen.MOD_ID + ":" + id, config);
	}

	@Nonnull
	private static Block getBlock(String modid, String blockName) {
		ResourceLocation blockRL = new ResourceLocation(modid, blockName);
		return Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(blockRL), "Cannot find block: " + blockRL);
	}

	private static BlockState defaultBlockState(String modid, String blockName) {
		return getBlock(modid, blockName).defaultBlockState();
	}

	private static SimpleBlockStateProvider blockStateProvider(@Nonnull Block block) {
		return new SimpleBlockStateProvider(block.defaultBlockState());
	}

	private static boolean inBiomeList(ResourceLocation biomeToCheck, ArrayList<ResourceLocation> biomes) {
		boolean checkResult = true;
		for(ResourceLocation biome: biomes) {
			checkResult = checkResult && biomeToCheck != null && biomeToCheck.equals(biome);
		}
		return checkResult;
	}

	// TODO: 填写待检查群系的List
}
