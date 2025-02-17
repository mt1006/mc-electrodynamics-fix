package electrodynamics.common.reloadlistener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Consumer;

import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import electrodynamics.Electrodynamics;
import electrodynamics.common.packet.types.PacketSetClientThermoGenSources;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PacketDistributor.PacketTarget;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.ForgeRegistries;

public class ThermoelectricGeneratorHeatRegister extends SimplePreparableReloadListener<JsonObject> {

	public static ThermoelectricGeneratorHeatRegister INSTANCE = null;

	public static final String FOLDER = "machines";
	public static final String FILE_NAME = "thermo_gen_heat_sources";

	protected static final String JSON_EXTENSION = ".json";
	protected static final int JSON_EXTENSION_LENGTH = JSON_EXTENSION.length();

	private static final Gson GSON = new Gson();

	private final HashMap<Fluid, Double> heatSources = new HashMap<>();

	private final HashMap<TagKey<Fluid>, Double> tags = new HashMap<>();

	private final Logger logger = Electrodynamics.LOGGER;

	@Override
	protected JsonObject prepare(ResourceManager manager, ProfilerFiller profiler) {
		JsonObject combined = new JsonObject();

		List<Entry<ResourceLocation, Resource>> resources = new ArrayList<>(manager.listResources(FOLDER, ThermoelectricGeneratorHeatRegister::isJson).entrySet());
		Collections.reverse(resources);

		for (Entry<ResourceLocation, Resource> entry : resources) {
			ResourceLocation loc = entry.getKey();
			final String namespace = loc.getNamespace();
			final String filePath = loc.getPath();
			final String dataPath = filePath.substring(FOLDER.length() + 1, filePath.length() - JSON_EXTENSION_LENGTH);

			final ResourceLocation jsonFile = new ResourceLocation(namespace, dataPath);

			Resource resource = entry.getValue();
			try (final InputStream inputStream = resource.open(); final Reader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));) {
				final JsonObject json = (JsonObject) GsonHelper.fromJson(GSON, reader, JsonElement.class);

				json.entrySet().forEach(set -> {

					if (combined.has(set.getKey())) {
						combined.remove(set.getKey());
					}

					combined.add(set.getKey(), set.getValue());
				});

			} catch (RuntimeException | IOException exception) {
				logger.error("Data loader for {} could not read data {} from file {} in data pack {}", FOLDER, jsonFile, loc, resource.sourcePackId(), exception);
			}

		}
		return combined;
	}

	@Override
	protected void apply(JsonObject json, ResourceManager manager, ProfilerFiller profiler) {
		heatSources.clear();
		tags.clear();

		json.entrySet().forEach(set -> {

			String key = set.getKey();
			Double value = set.getValue().getAsDouble();

			if (key.contains("#")) {

				key = key.substring(1);

				tags.put(FluidTags.create(new ResourceLocation(key)), value);

			} else {

				heatSources.put(ForgeRegistries.FLUIDS.getValue(new ResourceLocation(key)), value);

			}

		});

	}

	public void generateTagValues() {

		tags.forEach((tag, value) -> {
			ForgeRegistries.FLUIDS.tags().getTag(tag).forEach(fluid -> {

				if (!heatSources.containsKey(fluid)) {
					heatSources.put(fluid, value);
				}

			});
		});

		tags.clear();
	}

	public void setClientValues(HashMap<Fluid, Double> fuels) {
		this.heatSources.clear();
		this.heatSources.putAll(fuels);
	}

	public ThermoelectricGeneratorHeatRegister subscribeAsSyncable(final SimpleChannel channel) {
		MinecraftForge.EVENT_BUS.addListener(getDatapackSyncListener(channel));
		return this;
	}

	public HashMap<Fluid, Double> getHeatSources() {
		return heatSources;
	}

	public boolean isHeatSource(Fluid fluid) {
		return heatSources.containsKey(fluid);
	}
	
	public double getHeatMultiplier(Fluid fluid) {
		return heatSources.getOrDefault(fluid, 0.0);
	}

	private Consumer<OnDatapackSyncEvent> getDatapackSyncListener(final SimpleChannel channel) {
		return event -> {
			generateTagValues();
			ServerPlayer player = event.getPlayer();
			PacketSetClientThermoGenSources packet = new PacketSetClientThermoGenSources(heatSources);
			PacketTarget target = player == null ? PacketDistributor.ALL.noArg() : PacketDistributor.PLAYER.with(() -> player);
			channel.send(target, packet);
		};
	}

	private static boolean isJson(final ResourceLocation filename) {
		return filename.getPath().contains(FILE_NAME + JSON_EXTENSION);
	}

}
