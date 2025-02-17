package electrodynamics.api.gas;

import java.util.function.Predicate;

import javax.annotation.Nonnull;

import electrodynamics.api.capability.types.gas.IGasHandler;
import electrodynamics.api.gas.utils.IGasTank;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class GasTank implements IGasTank, IGasHandler {

	protected Predicate<GasStack> isGasValid;
	private double capacity;
	private double maxTemperature;
	private int maxPressure;
	@Nonnull
	private GasStack gas = GasStack.EMPTY;
	
	public GasTank(double capacity, double maxTemperature, int maxPressure) {
		this(capacity, maxTemperature, maxPressure, gas -> true);
	}

	public GasTank(double capacity, double maxTemperature, int maxPressure, Predicate<GasStack> isGasValid) {
		this.capacity = capacity;
		this.maxTemperature = maxTemperature;
		this.maxPressure = maxPressure;
		this.isGasValid = isGasValid;
	}

	public GasTank setValidator(Predicate<GasStack> predicate) {
		isGasValid = predicate;
		return this;
	}

	public void setGas(GasStack gas) {
		this.gas = gas;
	}

	@Override
	public GasStack getGas() {
		return gas;
	}

	@Override
	public double getGasAmount() {
		return gas.getAmount();
	}

	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}

	@Override
	public double getCapacity() {
		return capacity;
	}

	public void setMaxTemperature(double temperature) {
		maxTemperature = temperature;
	}

	@Override
	public double getMaxTemperature() {
		return maxTemperature;
	}

	public void setMaxPressure(int pressure) {
		maxPressure = pressure;
	}

	@Override
	public int getMaxPressure() {
		return maxPressure;
	}

	@Override
	public boolean isGasValid(GasStack other) {
		return isGasValid.test(other);
	}

	@Override
	public double fill(GasStack resource, GasAction action) {

		if (resource.isEmpty()) {
			return 0;
		}

		if (!isGasValid(resource)) {
			return 0;
		}

		if (isEmpty()) {

			double accepted = resource.getAmount() > getCapacity() ? getCapacity() : resource.getAmount();

			if (action == GasAction.EXECUTE) {

				setGas(resource.copy());

				onChange();

				if (getGas().getTemperature() > getMaxTemperature()) {

					onOverheat();

				}

				if (getGas().getPressure() > getMaxPressure()) {

					onOverpressure();

				}
				
				if(resource.isCondensed()) {
					
					onGasCondensed();
					
				}

			}

			return accepted;

		} else {

			if (!getGas().isSameGas(resource)) {
				return 0;
			}

			double canTake = GasStack.getMaximumAcceptance(getGas(), resource, getCapacity());

			if (canTake == 0) {
				return 0;
			}

			if (action == GasAction.EXECUTE) {

				GasStack accepted = resource.copy();

				accepted.setAmount(canTake);

				GasStack equalized = GasStack.equalizePresrsureAndTemperature(getGas(), accepted);

				setGas(equalized);

				onChange();

				if (getGas().getTemperature() > getMaxTemperature()) {

					onOverheat();

				}

				if (getGas().getPressure() > getMaxPressure()) {

					onOverpressure();

				}
				
				if(getGas().isCondensed()) {
					
					onGasCondensed();
					
				}

			}

			return canTake;

		}

	}

	@Override
	public GasStack drain(double amount, GasAction action) {

		if (isEmpty() || amount == 0) {
			return GasStack.EMPTY;
		}

		double taken = getGas().getAmount() > amount ? amount : getGas().getAmount();

		GasStack takenStack = new GasStack(getGas().getGas(), taken, getGas().getTemperature(), getGas().getPressure());

		if (action == GasAction.EXECUTE) {

			getGas().shrink(taken);

			if (getGas().getAmount() == 0) {

				setGas(GasStack.EMPTY);

			} else {

				setGas(getGas());

			}

			onChange();
		}

		return takenStack;
	}

	@Override
	public GasStack drain(GasStack resource, GasAction action) {

		if (resource.isEmpty() || !getGas().isSameGas(resource) || !getGas().isSamePressure(resource) || !getGas().isSameTemperature(resource)) {
			return GasStack.EMPTY;
		}

		return drain(resource.getAmount(), action);
	}

	@Override
	public double heat(double deltaTemperature, GasAction action) {

		if (getGas().isAbsoluteZero() && deltaTemperature < 0) {
			return -1;
		}

		GasStack updated = getGas().copy();

		updated.heat(deltaTemperature);

		if (updated.getAmount() > getCapacity()) {
			return -1;
		}

		if (action == GasAction.EXECUTE) {

			setGas(updated);

			onChange();

			if (getGas().getTemperature() > getMaxTemperature()) {

				onOverheat();

			}

		}

		return getCapacity() - updated.getAmount();
	}

	@Override
	public double bringPressureTo(int atm, GasAction action) {

		if (getGas().isVacuum() && atm < GasStack.VACUUM) {
			return -1;
		}

		GasStack updated = getGas().copy();

		updated.bringPressureTo(atm);

		if (updated.getAmount() > getCapacity()) {

			return -1;

		}

		if (action == GasAction.EXECUTE) {

			setGas(updated);

			onChange();

			if (getGas().getPressure() > getMaxPressure()) {

				onOverpressure();

			}

		}

		return getCapacity() - updated.getAmount();

	}

	public void onChange() {

	}

	public void onOverheat() {

	}

	public void onOverpressure() {

	}
	
	public void onGasCondensed() {
		
	}

	public boolean isEmpty() {
		return gas.isEmpty();
	}

	public CompoundTag writeToNbt() {
		CompoundTag tag = new CompoundTag();
		tag.put("gasstack", getGas().writeToNbt());
		tag.putDouble("capacity", getCapacity());
		tag.putDouble("maxtemp", getMaxTemperature());
		tag.putInt("maxpres", getMaxPressure());
		return tag;
	}

	public static GasTank readFromNbt(CompoundTag tag) {

		GasStack stack = GasStack.readFromNbt(tag.getCompound("gasstack"));

		GasTank tank = new GasTank(tag.getDouble("capacity"), tag.getDouble("maxtemp"), tag.getInt("maxpres"));

		tank.setGas(stack);

		return tank;

	}

	public void writeToBuffer(FriendlyByteBuf buffer) {

		getGas().writeToBuffer(buffer);

		buffer.writeDouble(getCapacity());

		buffer.writeDouble(getMaxTemperature());

		buffer.writeInt(getMaxPressure());

	}

	public static GasTank readFromBuffer(FriendlyByteBuf buffer) {

		GasStack stack = GasStack.readFromBuffer(buffer);

		GasTank tank = new GasTank(buffer.readDouble(), buffer.readDouble(), buffer.readInt());

		tank.setGas(stack);

		return tank;

	}

	@Override
	public String toString() {
		return "Gas: " + getGas().toString() + ", Capacity: " + getCapacity() + " mB, Max Temp: " + getMaxTemperature() + " K, Max Pressure: " + getMaxPressure() + " ATM";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof GasTank other) {

			return getGas().equals(obj) && getCapacity() == other.getCapacity() && getMaxTemperature() == other.getMaxTemperature() && getMaxPressure() == other.getMaxPressure();

		}
		return false;
	}

	@Override
	public int getTanks() {
		return 1;
	}

	@Override
	public GasStack getGasInTank(int tank) {
		return getGas();
	}

	@Override
	public double getTankCapacity(int tank) {
		return getCapacity();
	}

	@Override
	public double getTankMaxTemperature(int tank) {
		return getMaxTemperature();
	}

	@Override
	public int getTankMaxPressure(int tank) {
		return getMaxPressure();
	}

	@Override
	public boolean isGasValid(int tank, GasStack gas) {
		return isGasValid(gas);
	}

	@Override
	public double fillTank(int tank, GasStack gas, GasAction action) {
		return fill(gas, action);
	}

	@Override
	public GasStack drainTank(int tank, GasStack gas, GasAction action) {
		return drain(gas, action);
	}

	@Override
	public GasStack drainTank(int tank, double maxFill, GasAction action) {
		return drain(maxFill, action);
	}

	@Override
	public double heat(int tank, double deltaTemperature, GasAction action) {
		return heat(deltaTemperature, action);
	}

	@Override
	public double bringPressureTo(int tank, int atm, GasAction action) {
		return bringPressureTo(atm, action);
	}

}
