#include "applicationui.h"
#include <bb/cascades/Application>
#include <bb/cascades/QmlDocument>
#include <bb/cascades/AbstractPane>
#include <bb/cascades/DropDown>
#include <bb/cascades/RadioGroup>
#include <bb/cascades/Label>
#include <bb/cascades/ListView>

#include <iostream>

#include "pokemonlist.h"

using namespace bb::cascades;

using std::cerr;
using std::endl;

// Checks to make sure that certain functions only happen when the language is changed
bool LanguageSwitch = false;

// Sets the default language
QString DefaultLang = "9";

// Makes sure certain functions only happen at start up
bool StartUp = true;

ApplicationUI::ApplicationUI(bb::cascades::Application *app) :
		QObject(app), m_pokemonList(0) {
	// Create scene document from main.qml asset, the parent is set
	// to ensure the document gets destroyed properly at shut down.
	QmlDocument *qml = QmlDocument::create("asset:///main.qml").parent(this);

	// Create root object for the UI
	root = qml->createRootObject<AbstractPane>();

	// Set the handle for the "pokedex" object from QML
	qml->setContextProperty("pokedex", this);

	/* Can change the default language here in the third input for the constructor
	 * Also makes sure that the default type is all types, indicated by the "0"
	 */
	// Create the "model" to store data about pokemon
	m_pokemonList = new PokemonList(this, "0", DefaultLang);

	qml->setContextProperty("pokemon_list", m_pokemonList);

}

void ApplicationUI::typeSelected(int type) {
	cerr << "In typeSelected() with " << "type=" << type << endl;

	QString num = QString::number(type);

	// Sets the new type in the PokemonList variable
	m_pokemonList->setType(num);

	// Updates status text
	Label *status(0);	// A pointer to hold the Label UI object
	// Search the root QML object for a control named "status"
	status = root->findChild<Label *>("pokedex_status");
	if (status) { // did we succeed in getting a pointer to the Label UI control?
		// Yes. Now set the text as appropriate
		status->setText(
				QString("Found %1 Pokemon").arg(
						m_pokemonList->childCount(QVariantList())));
	} else {
		cerr << "failed to find status " << endl;
	}

	// Updates the PokÈmonList (or the model) with only the pokemon of the selected type showing

	ListView *pokeList(0);
	pokeList = root->findChild<ListView*>(); //Selects the Listview
	pokeList->resetDataModel(); //unloads the data
	pokeList->setDataModel(m_pokemonList); //reloads the data
}

void ApplicationUI::languageSelected(int language) {
	cerr << "In languageSelected() with " << "language=" << language << endl;

	QString num = QString::number(language);

	// Changes the language memeber variable

	m_pokemonList->setLang(num);

	// Updates the model to show the new model in the selected language

	ListView *pokeList(0);
	pokeList = root->findChild<ListView*>(); //Selects the Listview
	pokeList->resetDataModel(); //unloads the data
	pokeList->setDataModel(m_pokemonList); //reloads the data

	LanguageSwitch = true;

	/* This only happens after start up to avoid an infinite loop at start up
	 * Also allows for the drop down list to be updated with the new language
	 */
	if (!StartUp)
		init();
}

void ApplicationUI::init() {
	// Populate the dropdown list of types
	DropDown *dropDown(0);	// pointer to hold the DropDown UI object
	// Search the root QML object for a control named "pokemon_types"
	dropDown = root->findChild<DropDown *>("pokemon_types");

	TypeSet type_list[18];
	int i = 0;

	// Make an array of strings that hold all of the types in whatever language was selected

	QFile file("app/native/assets/data/type_names.csv");
	if (file.open(QIODevice::ReadOnly | QIODevice::Text)) {

		QTextStream in(&file);
		while (!in.atEnd()) {
			QString line = in.readLine();
			if (line.split(",")[1] == m_pokemonList->getLang() && i < 18) { //checks that the type is in english
				type_list[i].type = line.split(",")[2]; //populates list with the proper word from the split line (the type)
				type_list[i].lang_check = true;
			}

			if (i < 18 && line.split(",")[1] == "9"
					&& type_list[i].lang_check == false)
				type_list[i].type = line.split(",")[2] + " [English]";

			if (line.split(",")[1] == "9")
				i++;
		}
	}
	/* This block gets all of the types in the selected language and adds english to
	 * the end of types that are not available in the selected language
	 */

	else {
		cerr << "Failed to open type_names.cvs: " << file.error() << endl;
		exit(0);
	}

	dropDown->removeAll();

	if (m_pokemonList->getType() == "0"
			&& (DefaultLang == "9" || m_pokemonList->getLang() == "9"))
		dropDown->add(
				Option::create().text("All Types").value(0).selected(true));

	else if (m_pokemonList->getType() == "0"
			&& (DefaultLang == "1" || m_pokemonList->getLang() == "1")) {
		QFile file("app/native/assets/data/japanese_all_types.csv");
		if (file.open(QIODevice::ReadOnly | QIODevice::Text)) {

			QTextStream in(&file);
			while (!in.atEnd()) {
				QString line = in.readLine();
				dropDown->add(
						Option::create().text(line).value(0).selected(true));
			}
		} else {
			cerr << "Failed to open japanese_all_types.cvs: " << file.error()
					<< endl;
			exit(0);
		}
	}

	// Adds all types to the drop down and only if the language was switched

	if (dropDown) { // did we succeed in getting a pointer to the drop down UI control?
		for (i = 0; i < 18; i++) {
			if (m_pokemonList->getType() == type_list[i].type)
				dropDown->add(
						Option::create().text(type_list[i].type).value(i + 1).selected(
								true)); //Adds each type with a different corresponding value from int i

			else
				dropDown->add(
						Option::create().text(type_list[i].type).value(i + 1));
		}
	}

	// Populates the rest of the drop down

	LanguageSwitch = false;

	// Populate radio buttons for language settings
	RadioGroup *radio(0);	// A pointer to hold the RadioGroup UI object
	// Search the root QML object for a control named "pokemon_types"
	radio = root->findChild<RadioGroup *>("pokedex_languages");

	if (radio) {
		QFile file("app/native/assets/data/language_names.csv");
		if (file.open(QIODevice::ReadOnly | QIODevice::Text)) {

			QTextStream in(&file);
			while (!in.atEnd()) {
				QString line = in.readLine();
				if (line.split(",")[1] == "9"
						&& line.split(",")[0] == DefaultLang && StartUp) {
					radio->add(
							Option::create().text(line.split(",")[2]).value(
									line.split(",")[0]).selected(true));
				}
				if (line.split(",")[1] == "9"
						&& (line.split(",")[0] != DefaultLang || !StartUp)) {
					radio->add(
							Option::create().text(line.split(",")[2]).value(
									line.split(",")[0]));
				}
			}
		} else {
			cerr << "Failed to open language_names.cvs: " << file.error()
					<< endl;
			exit(0);
		}
	}

	else {
		cerr << "failed to find pokedex_languages " << endl;
	}

	StartUp = false;

	// Updates the status to show how many pokemon are being displayed
	// Set status text
	Label *status(0);	// A pointer to hold the Label UI object
	// Search the root QML object for a control named "status"
	status = root->findChild<Label *>("pokedex_status");
	if (status) { // did we succeed in getting a pointer to the Label UI control?
		// Yes. Now set the text as appropriate
		status->setText(
				QString("Found %1 Pokemon").arg(
						m_pokemonList->childCount(QVariantList())));
	} else {
		cerr << "failed to find status " << endl;
	}
}
