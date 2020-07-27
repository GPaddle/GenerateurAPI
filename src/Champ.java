
public class Champ {

	private String nom;
	private String type;
	private String nullProp;

	public Champ(String nom, String type, String nullProp) {
		super();
		this.nom = nom;
		this.type = type;
		this.nullProp = nullProp;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNullProp() {
		return nullProp;
	}

	public void setNullProp(String nullProp) {
		this.nullProp = nullProp;
	}
	
	@Override
	public String toString() {
		return "`"+this.nom+"` "+this.type+" "+nullProp;
	}
}
