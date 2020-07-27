import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreationTable {

	private String table;
	private String tableName;
	private ArrayList<Champ> lChamp;
	private String primaryKey;

	public CreationTable(String table, Champ champ, String primaryKey) {
		super();
		
		this.setTableName(table.split("`")[3]);
		
		this.table = table;
		this.lChamp = new ArrayList<Champ>();
		this.lChamp.add(champ);
		this.primaryKey = primaryKey;
	}

	public CreationTable(String table, ArrayList<Champ> lChamp, String primaryKey) {
		super();
		
		this.setTableName(table.split("`")[3]);
		
		this.table = table;
		this.lChamp = lChamp;
		this.primaryKey = primaryKey;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public ArrayList<Champ> getlChamp() {
		return lChamp;
	}

	public void setlChamp(ArrayList<Champ> lChamp) {
		this.lChamp = lChamp;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	@Override
	public String toString() {
		String s = "";
		s += "CREATE TABLE " + table + " (\n";

		for (Champ champ : lChamp) {
			s += "\t" + champ + ",\n";
		}
		s += "\tPRIMARY KEY (" + primaryKey + ")\n" + //
				");";

		return s;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public static ArrayList<CreationTable> getSample() {
		Champ ch1 = new Champ("codeDiscipline", "VARCHAR(3)", "NOT NULL");
		Champ ch2 = new Champ("discipline", "VARCHAR(100)", "NOT NULL");
		ArrayList<Champ> alChamp1 = new ArrayList<>();
		alChamp1.add(ch1);
		alChamp1.add(ch2);
		CreationTable ct1 = new CreationTable("`APIKAYAK`.`Disciplines`", alChamp1, "codeDiscipline");

		Champ ch3 = new Champ("codePays", "VARCHAR(3)", "NOT NULL");
		Champ ch4 = new Champ("nomPays", "VARCHAR(100)", "NOT NULL");
		ArrayList<Champ> alChamp2 = new ArrayList<>();
		alChamp2.add(ch3);
		alChamp2.add(ch4);
		CreationTable ct2 = new CreationTable("`APIKayak`.`Pays`", alChamp2, "codePays");

		List<CreationTable> lct = Arrays.asList(ct1, ct2);
		ArrayList<CreationTable> alct = new ArrayList<>();
		alct.addAll(lct);
				
		return alct;
	}

}
