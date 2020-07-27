import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class Arborescence {

	private String namespace;
	private int successOperation = 0, failOperation = 0;

	public Arborescence(String namespace) {
		this.namespace = namespace;
	}

	public void createFolder(String path, String name) {

		File arborescence = new File(path + name);

		if (arborescence.mkdirs()) {
			System.out.println("✅ Dossier " + name + " créé");
			successOperation++;
		} else {
			System.out.println("❌ Le Dossier " + name + " existe déjà");
			failOperation++;

		}
	}

	public void createHierarchy() {

		createFolder(".\\output\\src\\", "controler");
		createFolder(".\\output\\src\\", "conf");
		createFolder(".\\output\\src\\", "model");

		lineBreak();

	}

	public void lineBreak() {
		System.out.println("\r\n" + //
				"---------------------------------------" + //
				"\r\n");

	}

	public void createFile(String p_fileName, String p_content) {
		createFile(p_fileName, p_content, ".\\output\\");
	}

	public void createFile(String p_fileName, String p_content, String p_path) {
		String fileName = p_fileName;
		try {
			FileWriter htaccess = new FileWriter(p_path + fileName);
			htaccess.write(p_content);
			htaccess.close();

			System.out.println("✅ " + fileName + " a été correctement écrit");
			successOperation++;
		} catch (IOException e) {
			System.out.println("❌ Une erreur a eu lieu avec " + fileName);
			failOperation++;
			e.printStackTrace();
		}
	}

	@SuppressWarnings("resource")
	private void copyFileUsingChannel(String sourceTxt, String destTxt, String fileName) throws IOException {

		File source = new File(sourceTxt + fileName);
		File dest = new File(destTxt + fileName);

		FileChannel sourceChannel = null;
		FileChannel destChannel = null;
		try {
			sourceChannel = new FileInputStream(source).getChannel();
			destChannel = new FileOutputStream(dest).getChannel();
			destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());

			System.out.println("✅ " + sourceTxt + " a été copié");
			successOperation++;

		} finally {

			try {
				sourceChannel.close();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}

			try {
				destChannel.close();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}

		}
	}

	public void copieFichier(String p_fileName) {

		String sourceFolder = ".\\bin\\templateFiles\\";
		String destinationFolder = ".\\output\\";

		String fileName = p_fileName;

		try {
			copyFileUsingChannel(sourceFolder, destinationFolder, fileName);
		} catch (IOException e) {
			System.out.println("❌ Problème avec " + fileName);
			failOperation++;
			e.printStackTrace();
		}
	}

	public void createSystFiles(ArrayList<CreationTable> alct) {

		copieFichier(".htaccess");
		copieFichier("composer.json");

		String corps = "\r\n" + //
				"# API " + namespace + "\r\n" + //
				"\r\n" + //
				"## Description rapide : ...\r\n" + //
				"\r\n" + //
				"### Tables décrites :\r\n" + //
				"\r\n" + //
				"| Table | 🔎 GET | 📝 POST |\r\n" + //
				"|--|--|--|\r\n";

		for (CreationTable creationTable : alct) {
			corps += "| " + creationTable.getTableName() + " | ✔ | ✔ |\r\n";
		}

		corps += " \r\n" + //
				"  \r\n" + //
				"\r\n" + //
				">🐙 Cette API a été auto générée via une application JAVA : [Repo GitHub](https://github.com/GPaddle/APIGenerator/) \r\n"+//
				">🛶 A Propos : [kgdev.works](https://kgdev.works) \r\n" + //
				"\r\n" + //
				"Merci d'avoir utilisé cet utilitaire ✨\r\n";

		createFile("README.md", corps);
		
		lineBreak();

	}

	public void createModels(ArrayList<CreationTable> alct) {

		for (CreationTable creationTable : alct) {

			String nomTable = creationTable.getTableName();
			String corps = "<?php\r\n" + //
					"\r\n" + //
					"namespace " + namespace + "\\model;\r\n" + //
					"\r\n" + //
					"class " + nomTable + " extends \\Illuminate\\Database\\Eloquent\\Model\r\n" + //
					"{\r\n" + //
					"	protected $table = '" + nomTable + "';\r\n" + //
					"	protected $keyType = 'TODO : ex : string ';\r\n" + //
					"	protected $primaryKey = '" + creationTable.getPrimaryKey() + "';\r\n" + //
					"	public $timestamps = false;\r\n" + //
					"	public $incrementing = TODO true or false;\r\n" + //
					"	\r\n" + //
					"	\r\n" + //
					"}\r\n" + //
					"";

			createFile("src\\model\\" + nomTable + ".php", corps);
		}

		lineBreak();

	}

	public void createIndex(ArrayList<CreationTable> alct) {

		String corps = "<?php\r\n" + //
				"\r\n" + //
				"require_once __DIR__ . '/vendor/autoload.php';\r\n" + //
				"\r\n" + //
				"use \\Psr\\Http\\Message\\ServerRequestInterface as Request;\r\n" + //
				"use \\Psr\\Http\\Message\\ResponseInterface as Response;\r\n" + //
				"\r\n" + //
				"\r\n" + //
				"use Illuminate\\Database\\Capsule\\Manager as DB;\r\n" + //
				"use " + namespace + "\\controler\\{";

		corps += "Controler" + alct.get(0).getTableName();

		for (int i = 1; i < alct.size(); i++) {
			CreationTable table = alct.get(i);
			corps += ",Controler" + table.getTableName();
		}

		corps += "};" + "include 'src/conf/errors.php';\r\n" + //
				"\r\n" + //
				"\r\n" + //
				"$db = new DB();\r\n" + //
				"\r\n" + //
				"$db->addConnection(parse_ini_file(\"src/conf/conf.ini\"));\r\n" + //
				"$db->setAsGlobal();\r\n" + //
				"$db->bootEloquent();\r\n" + //
				"\r\n" + //
				"\r\n" + //
				"$configuration = [\r\n" + //
				"	'settings' => $conf\r\n" + //
				"];\r\n" + //
				"\r\n" + //
				"$c = new \\Slim\\Container($configuration);\r\n" + //
				"$app = new \\Slim\\App($c);\r\n" + //
				"\r\n" + //
				"\r\n" + //
				"\r\n" + // Redirection de / vers /docs
				"$app->get('/', function ($request, $response, $args) {\r\n" + //
				"	$uri = $request->getUri()->withPath($this->router->pathFor('doc'));\r\n" + //
				"	return $response->withRedirect((string) $uri);\r\n" + //
				"});" + //
				"\r\n" + // Redirection de /doc vers /docs
				"$app->get('/doc', function ($request, $response, $args) {\r\n" + //
				"	$uri = $request->getUri()->withPath($this->router->pathFor('doc'));\r\n" + //
				"	return $response->withRedirect((string) $uri);\r\n" + //
				"});\r\n" + //
				"\r\n" + //
				"$app->get(\r\n" + //
				"	'/docs[/]',\r\n" + //
				"	" + namespace + "\\controler\\ControlerDoc::class . \":getDoc\"\r\n" + //
				")->setName('doc');";

		for (CreationTable creationTable : alct) {
			String tableName = creationTable.getTableName();

			corps += "\r\n" + //
					"// ------------ " + tableName + " ------------------\r\n" + //
					"\r\n" + //
					"// Methode GET " + //
					"\r\n" + //
					"$app->get(\r\n" + //
					"	'/" + tableName.toLowerCase() + "[/]',\r\n" + //
					"		\\" + namespace + "\\controler\\Controler" + tableName + "::class . \":get" + tableName
					+ "\"\r\n" + //
					")->setName('all" + tableName + "');";

			corps += "\r\n" + //
					"\r\n" + //
					"// Methode POST " + //
					"\r\n" + //
					"$app->post(\r\n" + "	'/" + tableName.toLowerCase() + "[/]',\r\n"
					+ "	function (Request $req, Response $resp, $args) {\r\n" + "		$c = new Controler" + tableName
					+ "($this);\r\n" + "		return $c->post" + tableName + "($req, $resp, $args);\r\n" + "	}\r\n"
					+ ")->setName('post" + tableName + "');";
		}

		corps += "\r\n" + //
				"// ------------ Global ------------------\r\n" + //
				"\r\n" + //
				"$c['notFoundHandler'] = function ($c) {\r\n" + //
				"	return function ($req, $resp) {\r\n" + //
				"		$resp = $resp->withStatus(404);\r\n" + //
				"		$resp->getBody()->write('URI non traitée');\r\n" + //
				"		return $resp;\r\n" + //
				"	};\r\n" + //
				"};\r\n" + //
				"\r\n" + //
				"$c['notAllowedHandler'] = function ($c) {\r\n" + //
				"	return function ($req, $resp, $methods) {\r\n" + //
				"		$resp = $resp\r\n" + //
				"			->withStatus(405)\r\n" + //
				"			->withHeader('Allow', implode(',', $methods));\r\n" + //
				"		$resp->getBody()\r\n" + //
				"			->write('Allowed methods :' .\r\n" + //
				"				implode(',', $methods));\r\n" + //
				"		return $resp;\r\n" + //
				"	};\r\n" + //
				"};\r\n" + //
				"\r\n" + //
				"$app->run();\r\n";

		createFile(".\\index.php", corps);

		lineBreak();
	}

	public void createControlers(ArrayList<CreationTable> alct) {

		for (CreationTable creationTable : alct) {

			String nomTable = creationTable.getTableName();
			String corps = "<?php\r\n" + //
					"\r\n" + //
					"namespace " + namespace + "\\controler;\r\n" + //
					"\r\n" + //
					"use \\" + namespace + "\\model\\" + nomTable + ";\r\n" + //
					"\r\n" + //
					"use \\Psr\\Http\\Message\\ServerRequestInterface as Request;\r\n" + //
					"use \\Psr\\Http\\Message\\ResponseInterface as Response;\r\n" + //
					"\r\n" + //
					"class Controler" + nomTable + "\r\n" + //
					"{\r\n" + //
					"	public function get" + nomTable + "(Request $rq, Response $rs, $args)\r\n" + //
					"	{\r\n" + //
					"		$" + nomTable.toLowerCase() + " = " + nomTable + "::all();\r\n" + //
					"\r\n" + //
					"		$txt = \"{\\\"" + nomTable.toLowerCase() + "\\\":[\";\r\n" + //
					"		foreach ($" + nomTable.toLowerCase() + " as $key => $value) {\r\n" + //
					"			if ($key) {\r\n" + //
					"				$txt .= \",\" . ($value);\r\n" + //
					"			} else {\r\n" + //
					"				$txt .= ($value);\r\n" + //
					"			}\r\n" + //
					"		}\r\n" + //
					"		$txt .= \"]}\";\r\n" + //
					"\r\n" + //
					"		return FormattageDatas::formatDatasToJSON($rs, $txt, 200);\r\n" + //
					"\r\n" + //
					"	}\r\n" + //
					"}\r\n" + //
					"";

			createFile("src\\controler\\" + nomTable + ".php", corps);

		}

		String corpsFormat = "<?php\r\n" + //
				"\r\n" + //
				"namespace course\\controler;\r\n" + //
				"\r\n" + //
				"use \\Psr\\Http\\Message\\ServerRequestInterface as Request;\r\n" + //
				"use \\Psr\\Http\\Message\\ResponseInterface as Response;\r\n" + //
				"\r\n" + //
				"class FormattageDatas\r\n" + //
				"{\r\n" + //
				"	public static function formatDatasToJSON(Response $rs, $datas, $codeRetour)\r\n" + //
				"	{\r\n" + //
				"		$rs = $rs->withStatus($codeRetour)->withHeader('Content-Type', 'application/json');\r\n" + //
				"		$rs->getBody()->write($datas);\r\n" + //
				"		return $rs;\r\n" + //
				"	}\r\n" + //
				"\r\n" + //
				"	public static function formatNotFoundToJSON(Response $rs, $texteErreur)\r\n" + //
				"	{\r\n" + //
				"		$rs = $rs->withStatus(400)->withHeader('Content-Type', 'application/json');\r\n" + //
				"		$rs->getBody()->write($texteErreur);\r\n" + //
				"		return $rs;\r\n" + //
				"	}\r\n" + //
				"}\r\n";

		createFile("src\\controler\\FormattageDatas.php", corpsFormat);

		lineBreak();

	}

	public void createConf() {
		String corps = "driver=EnterYourDBDriver,ex:mysql\r\n" + //
				"username=EnterYourDBUserName\r\n" + //
				"password=EnterYourDBPass\r\n" + //
				"host=\r\n" + //
				"database=EnterYourDBName\r\n" + //
				"charset=EnterYourDBCharset,ex:utf8\r\n" + //
				"collation=EnterYourDBCollation,ex:utf8_unicode_ci\r\n";
		createFile("src\\conf\\conf.ini", corps);

		corps = "<?php\r\n" + //
				"\r\n" + //
				"namespace course\\conf;\r\n" + //
				"\r\n" + //
				"$conf = \r\n" + //
				"[\r\n" + //
				"	'displayErrorDetails' => true\r\n" + //
				"];\r\n";
		createFile("src\\conf\\errors.php", corps);

		lineBreak();
	}

	public void createAllFiles(ArrayList<CreationTable> alct) {

		long start = System.nanoTime();
		int size = alct.size();

		lineBreak();

		this.createHierarchy();
		this.createSystFiles(alct);

		this.createIndex(alct);
		this.createModels(alct);
		this.createControlers(alct);
		this.createConf();

		double elapsedTime = (System.nanoTime() - start) / Math.pow(10, 9);

		if (size > 1) {
			System.out.println("📊 Nombre total de tables : " + size);
		} else {
			System.out.println("📊 Nombre total de table : 1");
		}
		System.out.println("" + //
				"⏱ Création exécutée en " + elapsedTime + " s" + "\r\n" + //
				"✅ Opérations réussies : " + successOperation + "/" + (successOperation + failOperation) + "\r\n" + //
				"❌ Opérations ratées : " + failOperation + "/" + (successOperation + failOperation) + "\r\n" + //
				"\r\n" + //
				"💕 Merci d'avoir utilisé le générateur d'API !");

			ouvertureDossier();
	}

	private void ouvertureDossier() {
		Boolean lancementJAR = Launcher.class.getResource("Launcher.class").toString().contains(".jar");
		String uriRepertoires="";
		try {
			uriRepertoires = new File(".").getCanonicalFile().toString();
			if (lancementJAR) {
				Runtime.getRuntime().exec("explorer.exe /select," + uriRepertoires);
			} else {
				uriRepertoires = new File(".").getCanonicalFile().toString();
				Runtime.getRuntime().exec("explorer.exe /select," + uriRepertoires + "\\output\\index.php");
			}
		} catch (IOException e) {
			System.out.println("Les répertoires sont disponibles à l'adresse suivante :\n" + //
					uriRepertoires + "\\output");
		}

	}

	public int getFailOperation() {
		return failOperation;
	}

	public void setFailOperation(int failOperation) {
		this.failOperation = failOperation;
	}

	public int getsuccessOperation() {
		return successOperation;
	}

	public void setsuccessOperation(int successOperation) {
		this.successOperation = successOperation;
	}
}
