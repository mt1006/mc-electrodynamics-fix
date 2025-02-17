package electrodynamics.prefab.properties;

import java.util.ArrayList;

public class PropertyManager {
	private ArrayList<Property<?>> properties = new ArrayList<>();
	private boolean isDirty = false;

	public <T> Property<T> addProperty(Property<T> prop) {
		properties.add(prop);
		prop.setManager(this);
		return prop;
	}

	public ArrayList<Property<?>> getProperties() {
		return properties;
	}

	public ArrayList<Property<?>> getDirtyProperties() {
		ArrayList<Property<?>> dirty = new ArrayList<>();
		for (Property<?> prop : properties) {
			if (prop.isDirty()) {
				dirty.add(prop);
			} else {
				dirty.add(null);
			}
		}
		return dirty;
	}

	public void clean() {
		isDirty = false;
		properties.forEach(Property::clean);
	}

	public void update(int indexOf, Object value) {
		properties.get(indexOf).setAmbigous(value);
	}

	public boolean isDirty() {
		return isDirty;
	}

	public void setDirty() {
		isDirty = true;
	}

	@Override
	public String toString() {
		String string = "";
		for (int i = 0; i < properties.size(); i++) {
			string = string + i + ": " + properties.get(i).toString() + "\n";
		}
		return string;
	}
}
