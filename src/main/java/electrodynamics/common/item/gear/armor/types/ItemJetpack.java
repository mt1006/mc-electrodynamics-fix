package electrodynamics.common.item.gear.armor.types;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.mojang.datafixers.util.Pair;

import electrodynamics.api.References;
import electrodynamics.api.capability.types.fluid.RestrictedFluidHandlerItemStack;
import electrodynamics.client.ClientRegister;
import electrodynamics.client.keys.KeyBinds;
import electrodynamics.client.render.model.armor.types.ModelJetpack;
import electrodynamics.common.item.gear.armor.ICustomArmor;
import electrodynamics.common.packet.NetworkHandler;
import electrodynamics.common.packet.types.PacketJetpackFlightServer;
import electrodynamics.common.packet.types.PacketRenderJetpackParticles;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.prefab.utilities.CapabilityUtils;
import electrodynamics.prefab.utilities.NBTUtils;
import electrodynamics.prefab.utilities.TextUtils;
import electrodynamics.registers.ElectrodynamicsFluids;
import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.network.PacketDistributor;

public class ItemJetpack extends ArmorItem {

	public static final Fluid EMPTY_FLUID = Fluids.EMPTY;
	public static final int MAX_CAPACITY = 30000;

	public static final int USAGE_PER_TICK = 1;
	public static final double VERT_SPEED_INCREASE = 0.5;
	public static final double TERMINAL_VERTICAL_VELOCITY = 1;

	private static final String ARMOR_TEXTURE_LOCATION = References.ID + ":textures/model/armor/jetpack.png";

	public static final float OFFSET = 0.1F;

	public static final String DELTA_Y_KEY = "prevdeltay";
	public static final String WAS_HURT_KEY = "washurt";

	public ItemJetpack() {
		super(Jetpack.JETPACK, EquipmentSlot.CHEST, new Item.Properties().tab(References.CORETAB).stacksTo(1));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			@Override
			public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> properties) {

				ModelJetpack<LivingEntity> model = new ModelJetpack<>(ClientRegister.JETPACK.bakeRoot());

				model.crouching = properties.crouching;
				model.riding = properties.riding;
				model.young = properties.young;

				return model;
			}
		});
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
		return new RestrictedFluidHandlerItemStack(stack, stack, MAX_CAPACITY, getWhitelistedFluids());
	}

	@Override
	public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
		if (allowedIn(tab)) {
			items.add(new ItemStack(this));
			if (!CapabilityUtils.isFluidItemNull()) {
				ItemStack full = new ItemStack(this);
				Fluid fluid = getWhitelistedFluids().getSecond().get(0);
				full.getCapability(CapabilityUtils.getFluidItemCap()).ifPresent(h -> ((RestrictedFluidHandlerItemStack) h).fillInit(new FluidStack(fluid, MAX_CAPACITY)));
				items.add(full);

			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flagIn) {
		staticAppendHoverText(stack, world, tooltip, flagIn);
		super.appendHoverText(stack, world, tooltip, flagIn);
	}

	protected static void staticAppendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flagIn) {
		if (!CapabilityUtils.isFluidItemNull()) {
			stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(h -> tooltip.add(Component.literal(h.getFluidInTank(0).getAmount() + " / " + MAX_CAPACITY + " mB").withStyle(ChatFormatting.GRAY)));
		}
		// cheesing sync issues one line of code at a time
		if (stack.hasTag()) {
			int mode = stack.getTag().getInt(NBTUtils.MODE);
			Component modeTip = switch (mode) {
			case 0 -> TextUtils.tooltip("jetpack.mode").withStyle(ChatFormatting.GRAY).append(TextUtils.tooltip("jetpack.moderegular").withStyle(ChatFormatting.GREEN));
			case 1 -> TextUtils.tooltip("jetpack.mode").withStyle(ChatFormatting.GRAY).append(TextUtils.tooltip("jetpack.modehover").withStyle(ChatFormatting.AQUA));
			case 2 -> TextUtils.tooltip("jetpack.mode").withStyle(ChatFormatting.GRAY).append(TextUtils.tooltip("jetpack.modeoff").withStyle(ChatFormatting.RED));
			default -> Component.literal("");
			};
			tooltip.add(modeTip);
		} else {
			tooltip.add(TextUtils.tooltip("jetpack.mode").withStyle(ChatFormatting.GRAY).append(TextUtils.tooltip("jetpack.moderegular").withStyle(ChatFormatting.GREEN)));
		}
	}

	@Override
	public void onArmorTick(ItemStack stack, Level world, Player player) {
		super.onArmorTick(stack, world, player);
		armorTick(stack, world, player, OFFSET, false);
	}

	protected static void armorTick(ItemStack stack, Level world, Player player, float particleZ, boolean isCombat) {
		if (world.isClientSide) {
			ArmorItem item = (ArmorItem) stack.getItem();
			if (item.getSlot() == EquipmentSlot.CHEST && stack.hasTag()) {
				boolean isDown = KeyBinds.jetpackAscend.isDown();
				int mode = stack.hasTag() ? stack.getTag().getInt(NBTUtils.MODE) : 0;
				boolean enoughFuel = stack.getCapability(CapabilityUtils.getFluidItemCap()).map(m -> m.getFluidInTank(0).getAmount() >= ItemJetpack.USAGE_PER_TICK).orElse(false);
				if (enoughFuel) {
					if (mode == 0 && isDown) {
						double deltaY = moveWithJetpack(ItemJetpack.VERT_SPEED_INCREASE, ItemJetpack.TERMINAL_VERTICAL_VELOCITY, player, stack);
						renderClientParticles(world, player, particleZ);
						sendPacket(player, true, deltaY);
					} else if (mode == 1 && isDown) {
						double deltaY = moveWithJetpack(ItemJetpack.VERT_SPEED_INCREASE / 2, ItemJetpack.TERMINAL_VERTICAL_VELOCITY / 2, player, stack);
						renderClientParticles(world, player, particleZ);
						sendPacket(player, true, deltaY);

					} else if (mode == 1 && player.getFeetBlockState().isAir()) {
						double deltaY = hoverWithJetpack(player, stack);
						renderClientParticles(world, player, particleZ);
						sendPacket(player, true, deltaY);
					} else {
						sendPacket(player, false, player.getDeltaMovement().y);
					}
				} else {
					sendPacket(player, false, player.getDeltaMovement().y);
				}
			} else {
				sendPacket(player, false, player.getDeltaMovement().y);
			}
		} else {
			CompoundTag tag = stack.getOrCreateTag();
			boolean hasRan = tag.getBoolean(NBTUtils.USED);
			tag.putBoolean(NBTUtils.PLAYING_SOUND, tag.getBoolean(NBTUtils.PLAYING_SOUND));
			if (hasRan) {
				drainHydrogen(stack);
				NetworkHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), new PacketRenderJetpackParticles(player.getUUID(), isCombat));
				player.resetFallDistance();
			}
		}
	}

	@Override
	public boolean canBeDepleted() {
		return false;
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}

	@Override
	public boolean isValidRepairItem(ItemStack stack1, ItemStack stack2) {
		return false;
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		return staticIsBarVisible(stack);
	}

	protected static boolean staticIsBarVisible(ItemStack stack) {
		return stack.getCapability(CapabilityUtils.getFluidItemCap()).map(m -> {
			RestrictedFluidHandlerItemStack cap = (RestrictedFluidHandlerItemStack) m;
			return 13.0 * cap.getFluidInTank(0).getAmount() / cap.getTankCapacity(0) < 13.0;
		}).orElse(false);
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		return staticGetBarWidth(stack);
	}

	protected static int staticGetBarWidth(ItemStack stack) {
		return (int) Math.round(stack.getCapability(CapabilityUtils.getFluidItemCap()).map(h -> {
			RestrictedFluidHandlerItemStack cap = (RestrictedFluidHandlerItemStack) h;
			return 13.0 * cap.getFluidInTank(0).getAmount() / cap.getTankCapacity(0);
		}).orElse(13.0));
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged;
	}

	public Pair<List<ResourceLocation>, List<Fluid>> getWhitelistedFluids() {
		return staticGetWhitelistedFluids();
	}

	protected static Pair<List<ResourceLocation>, List<Fluid>> staticGetWhitelistedFluids() {
		List<ResourceLocation> tags = new ArrayList<>();
		List<Fluid> fluids = new ArrayList<>();
		tags.add(ElectrodynamicsTags.Fluids.HYDROGEN.location());
		fluids.add(ElectrodynamicsFluids.fluidHydrogen);
		return Pair.of(tags, fluids);
	}

	protected static double moveWithJetpack(double speed, double termVelocity, Player player, ItemStack jetpack) {

		Vec3 movement = player.getDeltaMovement();

		if (player.hasImpulse && wasEntityHurt(jetpack)) {
			movement = new Vec3(movement.x, getPrevDeltaY(jetpack), movement.z);
		}

		double ySum = movement.y + speed;
		double absY = Math.min(Math.abs(ySum), termVelocity);
		double newY = Math.signum(ySum) * absY;
		Vec3 currMovement = new Vec3(movement.x, newY, movement.z);

		player.setDeltaMovement(currMovement);
		player.resetFallDistance();
		// we keep track of the previous delta y for impulses
		return currMovement.y;
	}

	protected static double hoverWithJetpack(Player player, ItemStack jetpack) {

		Vec3 movement = player.getDeltaMovement();

		if (player.hasImpulse && wasEntityHurt(jetpack)) {
			movement = new Vec3(movement.x, getPrevDeltaY(jetpack), movement.z);
		}

		if (player.isShiftKeyDown()) {
			movement = new Vec3(movement.x, -0.3, movement.z);
		} else {
			movement = new Vec3(movement.x, 0, movement.z);
		}
		player.setDeltaMovement(movement);
		player.resetFallDistance();
		// we keep track of the previous delta y for impulses
		return movement.y;
	}

	public static void renderClientParticles(Level world, Player player, float particleZ) {
		Vec3 worldPosition = player.position();
		float rad = (float) (processDeg(player.yBodyRot) / 180.0F * Math.PI);
		double cosY = Mth.cos(rad);
		double sinY = Mth.sin(rad);
		double xOffCos = cosY * 0.2;
		double xOffSin = sinY * 0.2;
		double zOffCos = cosY * particleZ;
		double zOffSin = sinY * particleZ;
		double xRight = worldPosition.x - xOffCos + zOffSin;
		double xLeft = worldPosition.x + xOffCos + zOffSin;
		double zRight = worldPosition.z - zOffCos - xOffSin;
		double zLeft = worldPosition.z - zOffCos + xOffSin;
		double y = worldPosition.y + (player.isShiftKeyDown() ? 0.5 : 0.8);
		for (int i = 0; i < 10; i++) {
			world.addParticle(ParticleTypes.FLAME, xRight, y, zRight, 0, -2D, 0);
			// world.sendParticles(ParticleTypes.FLAME, xRight, y , zRight, 0, 0.0D, -2D,
			// 0.0D, 2D);
		}
		for (int i = 0; i < 10; i++) {
			world.addParticle(ParticleTypes.FLAME, xLeft, y, zLeft, 0, -2D, 0);
			// world.sendParticles(ParticleTypes.FLAME, xLeft, y, zLeft, 0, 0.0D, -2D, 0.0D,
			// 2D);
		}
	}

	protected static void drainHydrogen(ItemStack stack) {
		stack.getCapability(CapabilityUtils.getFluidItemCap()).ifPresent(h -> h.drain(ItemJetpack.USAGE_PER_TICK, FluidAction.EXECUTE));
	}

	protected static void sendPacket(Player player, boolean state, double prevDeltaMove) {
		NetworkHandler.CHANNEL.sendToServer(new PacketJetpackFlightServer(player.getUUID(), state, prevDeltaMove));
	}

	protected static double getPrevDeltaY(ItemStack stack) {
		return stack.getOrCreateTag().getDouble(DELTA_Y_KEY);
	}

	protected static boolean wasEntityHurt(ItemStack stack) {
		return stack.getOrCreateTag().getBoolean(WAS_HURT_KEY);
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		return ARMOR_TEXTURE_LOCATION;
	}

	// we need to do this based upon some testing I did
	private static float processDeg(float deg) {
		if (deg > 180) {
			return deg - 360;
		} else if (deg < 180) {
			return deg + 360;
		} else {
			return deg;
		}
	}

	public enum Jetpack implements ICustomArmor {
		JETPACK;

		@Override
		public int getDurabilityForSlot(EquipmentSlot slotIn) {
			return 100;
		}

		@Override
		public int getDefenseForSlot(EquipmentSlot slotIn) {
			return 1;
		}

		@Override
		public SoundEvent getEquipSound() {
			return SoundEvents.ARMOR_EQUIP_IRON;
		}

		@Override
		public String getName() {
			return References.ID + ":jetpack";
		}

		@Override
		public float getToughness() {
			return 0.0F;
		}

		@Override
		public float getKnockbackResistance() {
			return 0.0F;
		}

	}

}
