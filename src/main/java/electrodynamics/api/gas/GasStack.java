package electrodynamics.api.gas;

import electrodynamics.registers.ElectrodynamicsRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

/**
 * An implementation of a FluidStack-like object for gases 
 * 
 * @author skip999
 *
 */
public class GasStack {
	
	public static final GasStack EMPTY = new GasStack(Gas.empty());
	
	public static final int ABSOLUTE_ZERO = 1; //zero technically, but that makes volumes a pain in the ass
	public static final double VACUUM = 1; // zero technically, but that makes volumes a pain in the ass
	
	private Gas gas = Gas.empty();
	private double amount = 0; //mB
	private double temperature = 257; //Kelvin room temperature (20 C)
	private int pressure = 1; //ATM
	
	private boolean isEmpty = false;
	
	public GasStack(Gas gas) {
		this.gas = gas;
		if(gas == Gas.empty()) {
			isEmpty = true;
		}
	}
	
	public GasStack(Gas gas, double amount, double temperature, int pressure) {
		this(gas);
		this.amount = amount;
		this.temperature = temperature;
		this.pressure = pressure;
	}
	
	public Gas getGas() {
		return gas;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public double getTemperature() {
		return temperature;
	}
	
	public int getPressure() {
		return pressure;
	}
	
	public GasStack copy() {
		return new GasStack(gas, amount, temperature, pressure);
	}
	
	public void setAmount(double amount) {
		if(isEmpty()) {
			throw new UnsupportedOperationException("An empty Gas Stack cannot be modified");
		}
		this.amount = amount;
	}
	
	public void shrink(double amount) {
		if(isEmpty()) {
			throw new UnsupportedOperationException("An empty Gas Stack cannot be modified");
		}
		if(this.amount >= amount) {
			this.amount -= amount;
		}
		if(amount == 0) {
			gas = Gas.empty();
			isEmpty = true;
		}
	}
	
	public void grow(double amount) {
		if(isEmpty()) {
			throw new UnsupportedOperationException("An empty Gas Stack cannot be modified");
		}
		this.amount += amount;
	}
	
	/**
	 * A negative temperature is analogous to cooling
	 * 
	 * Temperatures cannot drop below 1 degree Kelvin
	 * 
	 * @param deltaTemp The change in temperature
	 */
	public void heat(double deltaTemp) {
		amount += getVolumeChangeFromHeating(deltaTemp);
		temperature += deltaTemp;
	}
	
	/**
	 * Sets the pressure of this GasStack to the desired pressure and updates the volume accordingly
	 * 
	 * @param atm
	 */
	public void bringPressureTo(int atm) {
		amount += getVolumeChangeFromPressurizing(atm);
		pressure = atm;
	}
	
	public double getVolumeChangeFromHeating(double deltaTemp) {
		if(isEmpty()) {
			throw new UnsupportedOperationException("An empty Gas Stack cannot be modified");
		}
		if(isAbsoluteZero() && deltaTemp < 0 || temperature + deltaTemp < ABSOLUTE_ZERO) {
			throw new UnsupportedOperationException("The temperature cannot drop below absolute zero");
		}
		
		double change = deltaTemp / temperature;
		
		if(change == 1) {
			return 0;
		}
		
		if(change > 1) {
			change *= -1;
		}
		
		return amount * change;
		
	}
	
	public double getVolumeChangeFromPressurizing(int atm) {
		if(isEmpty()) {
			throw new UnsupportedOperationException("An empty Gas Stack cannot be modified");
		}
		
		if(isVacuum() || atm < VACUUM) {
			throw new UnsupportedOperationException("You cannot have a pressure less than " + VACUUM);
		}
		
		double change = -(double) atm / (double) pressure;
		
		return amount * change;
	}
	
	public boolean isEmpty() {
		return this == EMPTY || isEmpty;
	}
	
	public boolean isSameGas(GasStack other) {
		return this.gas.equals(other.gas);
	}
	
	public boolean isSameAmount(GasStack other) {
		return amount == other.amount;
	}
	
	public boolean isSameTemperature(GasStack other) {
		return temperature == other.temperature;
	}
	
	public boolean isSamePressure(GasStack other) {
		return pressure == other.pressure;
	}
	
	public boolean isAbsoluteZero() {
		return temperature == ABSOLUTE_ZERO;
	}
	
	public boolean isVacuum() {
		return pressure == VACUUM;
	}
	
	public boolean isCondensed() {
		return temperature <= gas.getCondensationTemp();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof GasStack other) {
			return other.gas.equals(gas) && other.amount == amount && other.temperature == temperature && other.pressure == pressure;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return gas.toString() + ", amount: " + amount + " mB, temp: " + temperature + " K, pressure: " + pressure + " ATM";
	}
	
	//This is assumed to be a new tag
	public CompoundTag writeToNbt() {
		CompoundTag tag = new CompoundTag();
		tag.putString("name", ElectrodynamicsRegistries.gasRegistry().getKey(this.gas).toString());
		tag.putDouble("amount", amount);
		tag.putDouble("temperature", temperature);
		tag.putInt("pressure", pressure);
		return tag;
	}
	
	public static GasStack readFromNbt(CompoundTag tag) {
		Gas gas = ElectrodynamicsRegistries.gasRegistry().getValue(new ResourceLocation(tag.getString("name")));
		double amount = tag.getDouble("amount");
		double temperature = tag.getDouble("temperature");
		int pressure = tag.getInt("pressure");
		return new GasStack(gas, amount, temperature, pressure);
	}
	
	public void writeToBuffer(FriendlyByteBuf buffer) {
		buffer.writeRegistryId(ElectrodynamicsRegistries.gasRegistry(), gas);
		buffer.writeDouble(amount);
		buffer.writeDouble(temperature);
		buffer.writeInt(pressure);
	}
	
	public static GasStack readFromBuffer(FriendlyByteBuf buffer) {
		Gas gas = buffer.readRegistryId();
		double amount = buffer.readDouble();
		double temperature = buffer.readDouble();
		int pressure = buffer.readInt();
		return new GasStack(gas, amount, temperature, pressure);
	}
	
	/**
	 * Equalizes the temperature of two gas stacks to their respective median values
	 * and adjusts the volume of the resulting stack accordingly
	 * 
	 * The gas with the greater volume becomes the ruling pressure
	 * 
	 * It is assumed you have checked none of the gas stacks are unmodifiable
	 * 
	 * @param stack1 : The first stack
	 * @param stack2 : The second stack
	 * @return A gas stack that has the average temperature and pressure of the two stacks with the corresponding volume 
	 */
	public static GasStack equalizePresrsureAndTemperature(GasStack stack1, GasStack stack2) {
		
		
		int newPressure = stack1.getAmount() > stack2.getAmount() ? stack1.getPressure() : stack2.getPressure();
		
		double medianTemperature = (double) (stack1.temperature + stack2.temperature) / 2.0;
		
		double deltaT1 = medianTemperature - stack1.temperature;
		double deltaT2 = medianTemperature - stack2.temperature;
		
		stack1.bringPressureTo(newPressure);
		stack2.bringPressureTo(newPressure);
		
		stack1.heat(deltaT1);
		stack2.heat(deltaT2);
		
		GasStack stack = new GasStack(stack1.getGas());
		
		stack.setAmount(stack1.getAmount() + stack2.getAmount());
		stack.temperature = medianTemperature;
		stack.pressure = stack1.getPressure();
		
		return stack;
		
	}
	
	/**
	 * Determines how much gas from stack 2 could be accepted into a container once stack1 and stack2 have equalized temperatures and pressures
	 * 
	 * The gas stack with the greater volume becomes the ruling pressure
	 * 
	 * It is assumed you have checked none of the gas stacks are unmodifiable
	 * 
	 * @param stack1 : The existing GasStack in the container
	 * @param stack2 : The gas attempting to be inserted into the container
	 * @param maximumAccept : The capacity of the container
	 * @return How much of stack2 could be accepted before the temperatures and pressures equalize
	 */
	public static double getMaximumAcceptance(GasStack stack1, GasStack stack2, double maximumAccept) {
		
		int rulingPressure = stack1.getAmount() > stack2.getAmount() ? stack1.getPressure() : stack2.getPressure();
		double medianTemperature = (double) (stack1.temperature + stack2.temperature) / 2.0;
		
		double deltaT1 = medianTemperature - stack1.temperature;
		double deltaT2 = medianTemperature - stack2.temperature;
		
		double deltaP1 = (double) rulingPressure / (double) stack1.pressure;
		
		if(deltaP1 > 1) {
			deltaP1 *= -1;
		} else if (deltaP1 == 1) {
			deltaP1 = 0;
		}

		double remaining = maximumAccept - (stack1.amount + stack1.amount * deltaP1 + stack1.amount * (deltaT1 / stack1.temperature));
		
		if(remaining <= 0) {
			return 0;
		}
		
		double deltaP2 = (double) rulingPressure / (double) stack2.pressure;
		
		if(deltaP2 > 1) {
			deltaP2 *= -1;
		} else if (deltaP2 == 1) {
			deltaP2 = 0;
		}
		
		double deltaTemp2 = (deltaT2 / stack2.temperature);
		
		double newT2Volume = stack2.amount + stack2.amount * deltaTemp2 + stack2.amount * deltaP2;
		
		if(newT2Volume <= remaining) {
			return stack2.amount;
		}
		
		if(deltaTemp2 + deltaP2 == -1) {
			deltaTemp2 += 0.00001;
		}
		
		return remaining / (1.0 + deltaP2 + deltaTemp2);
		
	}

}
