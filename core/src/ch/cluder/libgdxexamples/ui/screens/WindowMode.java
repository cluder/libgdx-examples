package ch.cluder.libgdxexamples.ui.screens;

public enum WindowMode {
	WINDOWED, BORDERLESS, FULLSCREEN;

	@Override
	public String toString() {
		switch (this) {
		case WINDOWED:
			return "Windowed";
		case FULLSCREEN:
			return "Fullscreen";
		case BORDERLESS:
			return "Windowed Borderless";
		default:
			return super.toString();
		}
	}

	static public WindowMode fromLabel(String searchLabel) {
		for (WindowMode w : WindowMode.values()) {
			if (searchLabel.contentEquals(w.toString())) {
				return w;
			}
		}
		throw new IllegalArgumentException("Label" + searchLabel + " not found");
	}

}
