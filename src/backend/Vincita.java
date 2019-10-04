package backend;

public enum Vincita {
	Ambo,
	Terna,
	Quaterna,
	Cinquina,
	Tombola;
	
	// restituisce la vincita successiva a quella indicata
	public static Vincita next(final Vincita v) {
		if (v == null)
			return Ambo;
		else if (v == Ambo)
			return Terna;
		else if (v == Terna)
			return Quaterna;
		else if (v == Quaterna)
			return Cinquina;
		else if (v == Cinquina)
			return Tombola;
		else // if (v == Tombola)
			return null;
	}
}
