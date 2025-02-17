package electrodynamics.registers;

import electrodynamics.api.References;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.inventory.container.item.ContainerGuidebook;
import electrodynamics.common.inventory.container.item.ContainerSeismicScanner;
import electrodynamics.common.inventory.container.tile.ContainerBatteryBox;
import electrodynamics.common.inventory.container.tile.ContainerCarbyneBatteryBox;
import electrodynamics.common.inventory.container.tile.ContainerChargerGeneric;
import electrodynamics.common.inventory.container.tile.ContainerChemicalCrystallizer;
import electrodynamics.common.inventory.container.tile.ContainerChemicalMixer;
import electrodynamics.common.inventory.container.tile.ContainerCoalGenerator;
import electrodynamics.common.inventory.container.tile.ContainerCombustionChamber;
import electrodynamics.common.inventory.container.tile.ContainerCoolantResavoir;
import electrodynamics.common.inventory.container.tile.ContainerCreativeFluidSource;
import electrodynamics.common.inventory.container.tile.ContainerCreativePowerSource;
import electrodynamics.common.inventory.container.tile.ContainerDO2OProcessor;
import electrodynamics.common.inventory.container.tile.ContainerElectricArcFurnace;
import electrodynamics.common.inventory.container.tile.ContainerElectricArcFurnaceDouble;
import electrodynamics.common.inventory.container.tile.ContainerElectricArcFurnaceTriple;
import electrodynamics.common.inventory.container.tile.ContainerElectricFurnace;
import electrodynamics.common.inventory.container.tile.ContainerElectricFurnaceDouble;
import electrodynamics.common.inventory.container.tile.ContainerElectricFurnaceTriple;
import electrodynamics.common.inventory.container.tile.ContainerElectrolyticSeparator;
import electrodynamics.common.inventory.container.tile.ContainerFermentationPlant;
import electrodynamics.common.inventory.container.tile.ContainerFluidVoid;
import electrodynamics.common.inventory.container.tile.ContainerHydroelectricGenerator;
import electrodynamics.common.inventory.container.tile.ContainerLithiumBatteryBox;
import electrodynamics.common.inventory.container.tile.ContainerMineralWasher;
import electrodynamics.common.inventory.container.tile.ContainerMotorComplex;
import electrodynamics.common.inventory.container.tile.ContainerO2OProcessor;
import electrodynamics.common.inventory.container.tile.ContainerO2OProcessorDouble;
import electrodynamics.common.inventory.container.tile.ContainerO2OProcessorTriple;
import electrodynamics.common.inventory.container.tile.ContainerQuarry;
import electrodynamics.common.inventory.container.tile.ContainerSeismicRelay;
import electrodynamics.common.inventory.container.tile.ContainerSolarPanel;
import electrodynamics.common.inventory.container.tile.ContainerTankGeneric;
import electrodynamics.common.inventory.container.tile.ContainerWindmill;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ElectrodynamicsMenuTypes {
	public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, References.ID);

	public static final RegistryObject<MenuType<ContainerCoalGenerator>> CONTAINER_COALGENERATOR = MENU_TYPES.register(SubtypeMachine.coalgenerator.tag(), () -> new MenuType<>(ContainerCoalGenerator::new));
	public static final RegistryObject<MenuType<ContainerElectricFurnace>> CONTAINER_ELECTRICFURNACE = MENU_TYPES.register(SubtypeMachine.electricfurnace.tag(), () -> new MenuType<>(ContainerElectricFurnace::new));
	public static final RegistryObject<MenuType<ContainerElectricFurnaceDouble>> CONTAINER_ELECTRICFURNACEDOUBLE = MENU_TYPES.register(SubtypeMachine.electricfurnacedouble.tag(), () -> new MenuType<>(ContainerElectricFurnaceDouble::new));
	public static final RegistryObject<MenuType<ContainerElectricFurnaceTriple>> CONTAINER_ELECTRICFURNACETRIPLE = MENU_TYPES.register(SubtypeMachine.electricfurnacetriple.tag(), () -> new MenuType<>(ContainerElectricFurnaceTriple::new));
	public static final RegistryObject<MenuType<ContainerElectricArcFurnace>> CONTAINER_ELECTRICARCFURNACE = MENU_TYPES.register(SubtypeMachine.electricarcfurnace.tag(), () -> new MenuType<>(ContainerElectricArcFurnace::new));
	public static final RegistryObject<MenuType<ContainerElectricArcFurnaceDouble>> CONTAINER_ELECTRICARCFURNACEDOUBLE = MENU_TYPES.register(SubtypeMachine.electricarcfurnacedouble.tag(), () -> new MenuType<>(ContainerElectricArcFurnaceDouble::new));
	public static final RegistryObject<MenuType<ContainerElectricArcFurnaceTriple>> CONTAINER_ELECTRICARCFURNACETRIPLE = MENU_TYPES.register(SubtypeMachine.electricarcfurnacetriple.tag(), () -> new MenuType<>(ContainerElectricArcFurnaceTriple::new));
	public static final RegistryObject<MenuType<ContainerO2OProcessor>> CONTAINER_O2OPROCESSOR = MENU_TYPES.register("o2oprocessor", () -> new MenuType<>(ContainerO2OProcessor::new));
	public static final RegistryObject<MenuType<ContainerO2OProcessorDouble>> CONTAINER_O2OPROCESSORDOUBLE = MENU_TYPES.register("o2oprocessordouble", () -> new MenuType<>(ContainerO2OProcessorDouble::new));
	public static final RegistryObject<MenuType<ContainerO2OProcessorTriple>> CONTAINER_O2OPROCESSORTRIPLE = MENU_TYPES.register("o2oprocessortriple", () -> new MenuType<>(ContainerO2OProcessorTriple::new));
	public static final RegistryObject<MenuType<ContainerDO2OProcessor>> CONTAINER_DO2OPROCESSOR = MENU_TYPES.register("do2oprocessor", () -> new MenuType<>(ContainerDO2OProcessor::new));
	public static final RegistryObject<MenuType<ContainerBatteryBox>> CONTAINER_BATTERYBOX = MENU_TYPES.register(SubtypeMachine.batterybox.tag(), () -> new MenuType<>(ContainerBatteryBox::new));
	public static final RegistryObject<MenuType<ContainerLithiumBatteryBox>> CONTAINER_LITHIUMBATTERYBOX = MENU_TYPES.register(SubtypeMachine.lithiumbatterybox.tag(), () -> new MenuType<>(ContainerLithiumBatteryBox::new));
	public static final RegistryObject<MenuType<ContainerFermentationPlant>> CONTAINER_FERMENTATIONPLANT = MENU_TYPES.register(SubtypeMachine.fermentationplant.tag(), () -> new MenuType<>(ContainerFermentationPlant::new));
	public static final RegistryObject<MenuType<ContainerMineralWasher>> CONTAINER_MINERALWASHER = MENU_TYPES.register(SubtypeMachine.mineralwasher.tag(), () -> new MenuType<>(ContainerMineralWasher::new));
	public static final RegistryObject<MenuType<ContainerChemicalMixer>> CONTAINER_CHEMICALMIXER = MENU_TYPES.register(SubtypeMachine.chemicalmixer.tag(), () -> new MenuType<>(ContainerChemicalMixer::new));
	public static final RegistryObject<MenuType<ContainerChemicalCrystallizer>> CONTAINER_CHEMICALCRYSTALLIZER = MENU_TYPES.register(SubtypeMachine.chemicalcrystallizer.tag(), () -> new MenuType<>(ContainerChemicalCrystallizer::new));
	public static final RegistryObject<MenuType<ContainerChargerGeneric>> CONTAINER_CHARGER = MENU_TYPES.register("genericcharger", () -> new MenuType<>(ContainerChargerGeneric::new));
	public static final RegistryObject<MenuType<ContainerTankGeneric>> CONTAINER_TANK = MENU_TYPES.register("generictank", () -> new MenuType<>(ContainerTankGeneric::new));
	public static final RegistryObject<MenuType<ContainerCombustionChamber>> CONTAINER_COMBUSTION_CHAMBER = MENU_TYPES.register("combustionchamber", () -> new MenuType<>(ContainerCombustionChamber::new));
	public static final RegistryObject<MenuType<ContainerSolarPanel>> CONTAINER_SOLARPANEL = MENU_TYPES.register("solarpanel", () -> new MenuType<>(ContainerSolarPanel::new));
	public static final RegistryObject<MenuType<ContainerWindmill>> CONTAINER_WINDMILL = MENU_TYPES.register("windmill", () -> new MenuType<>(ContainerWindmill::new));
	public static final RegistryObject<MenuType<ContainerHydroelectricGenerator>> CONTAINER_HYDROELECTRICGENERATOR = MENU_TYPES.register("hydroelectricgenerator", () -> new MenuType<>(ContainerHydroelectricGenerator::new));
	public static final RegistryObject<MenuType<ContainerCreativePowerSource>> CONTAINER_CREATIVEPOWERSOURCE = MENU_TYPES.register("creativepowersource", () -> new MenuType<>(ContainerCreativePowerSource::new));
	public static final RegistryObject<MenuType<ContainerCreativeFluidSource>> CONTAINER_CREATIVEFLUIDSOURCE = MENU_TYPES.register("creativefluidsource", () -> new MenuType<>(ContainerCreativeFluidSource::new));
	public static final RegistryObject<MenuType<ContainerFluidVoid>> CONTAINER_FLUIDVOID = MENU_TYPES.register("fluidvoid", () -> new MenuType<>(ContainerFluidVoid::new));
	public static final RegistryObject<MenuType<ContainerSeismicScanner>> CONTAINER_SEISMICSCANNER = MENU_TYPES.register("seismicdetector", () -> new MenuType<>(ContainerSeismicScanner::new));
	public static final RegistryObject<MenuType<ContainerElectrolyticSeparator>> CONTAINER_ELECTROLYTICSEPARATOR = MENU_TYPES.register("electrolyticseparator", () -> new MenuType<>(ContainerElectrolyticSeparator::new));
	public static final RegistryObject<MenuType<ContainerCarbyneBatteryBox>> CONTAINER_CARBYNEBATTERYBOX = MENU_TYPES.register(SubtypeMachine.carbynebatterybox.tag(), () -> new MenuType<>(ContainerCarbyneBatteryBox::new));
	public static final RegistryObject<MenuType<ContainerSeismicRelay>> CONTAINER_SEISMICRELAY = MENU_TYPES.register("seismicrelay", () -> new MenuType<>(ContainerSeismicRelay::new));
	public static final RegistryObject<MenuType<ContainerCoolantResavoir>> CONTAINER_COOLANTRESAVOIR = MENU_TYPES.register("coolantresavoir", () -> new MenuType<>(ContainerCoolantResavoir::new));
	public static final RegistryObject<MenuType<ContainerMotorComplex>> CONTAINER_MOTORCOMPLEX = MENU_TYPES.register("motorcomplex", () -> new MenuType<>(ContainerMotorComplex::new));
	public static final RegistryObject<MenuType<ContainerQuarry>> CONTAINER_QUARRY = MENU_TYPES.register("quarry", () -> new MenuType<>(ContainerQuarry::new));
	public static final RegistryObject<MenuType<ContainerGuidebook>> CONTAINER_GUIDEBOOK = MENU_TYPES.register("guidebook", () -> new MenuType<>(ContainerGuidebook::new));

}
