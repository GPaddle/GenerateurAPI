import java.util.ArrayList;
import java.util.Scanner;

public class Launcher {

	public static void main(String[] args) {

		Boolean test = true;

		ArrayList<CreationTable> alct = CreationTable.getSample();

		Scanner sc = new Scanner(System.in);
		System.out.println("Entrez le nom de l'API (namespace)\r\n\r\n");

		Arborescence arb;

		arb = test ? new Arborescence("Test") : new Arborescence(sc.nextLine());

		arb.createAllFiles(alct);

		sc.close();
	}
}
