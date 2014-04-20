#include "applicationui.h"

#include <bb/cascades/Application>
#include <bb/cascades/QmlDocument>
#include <bb/cascades/AbstractPane>
#include <bb/cascades/DropDown>
#include <bb/cascades/RadioGroup>
#include <bb/cascades/Label>

#include <iostream>

#include "pokemonlist.h"

using namespace bb::cascades;

using std::cerr;
using std::endl;

ApplicationUI::ApplicationUI(bb::cascades::Application *app) :
        QObject(app), m_pokemonList(0)
{

    // Create scene document from main.qml asset, the parent is set
    // to ensure the document gets destroyed properly at shut down.
    QmlDocument *qml = QmlDocument::create("asset:///main.qml").parent(this);

    // Create root object for the UI
    AbstractPane *root = qml->createRootObject<AbstractPane>();

    // Set the handle for the "pokedex" object from QML
    qml->setContextProperty("pokedex", this);

    // Create the "model" to store data about pokemon
    m_pokemonList = new PokemonList(this);
    qml->setContextProperty("pokemon_list", m_pokemonList);

	/* An example on how to locate the dropdown UI control and add an option
	 * See http://developer.blackberry.com/native/reference/cascades/bb__cascades__dropdown.html
	 * to see what else you can do with the DropDown class
	 */

    /* TODO: All of following should be in a separate init() function. You will need to
     * save the root pointer in a member variable so that you can access it later from
     * the init() function
     */

    // Populate the dropdown list of types
    DropDown *dropDown(0);	// pointer to hold the DropDown UI object
    // Search the root QML object for a control named "pokemon_types"
    dropDown = root->findChild<DropDown *>("pokemon_types");

    if (dropDown) { // did we succeed in getting a pointer to the drop down UI control?
        dropDown->add(Option::create().text("Grass").value("1")); // Yes. Add a new option
        dropDown->add(Option::create().text("Poison").value("2")); // Yes. Add a new option
		// TODO: Open types.csv file, parse the csv data and create the drop down list
		// Remove above two lines after that is done
    }
    else {
    	cerr << "failed to find pokemon_types " << endl;
    }

    // Populate radio buttons for language settings
    RadioGroup *radio(0);	// A pointer to hold the RadioGroup UI object
    // Search the root QML object for a control named "pokemon_types"
    radio = root->findChild<RadioGroup *>("pokedex_languages");

    if (radio) { // did we succeed in getting a pointer to the radio button UI control?
    	radio->add(Option::create().text("English").value("9").selected(true)); // Yes. Add a new option and make it selected
    	radio->add(Option::create().text("Japanese").value("1")); // Add another option
		// TODO: Open language_name.csv file, parse the csv data and create the drop down list
		// Remove above two lines after that is done
    }
    else {
    	cerr << "failed to find pokedex_languages " << endl;
    }


    // Set status text
    Label *status(0);	// A pointer to hold the Label UI object
    // Search the root QML object for a control named "status"
    status = root->findChild<Label *>("pokedex_status");
    if (status) { // did we succeed in getting a pointer to the Label UI control?
    	// Yes. Now set the text as appropriate
    	status->setText(QString("Found %1 Pokémon").arg(m_pokemonList->childCount(QVariantList())));
    }
    else {
    	cerr << "failed to find status " << endl;
    }

    // Set created root object as the application scene
    app->setScene(root);
}

void ApplicationUI::typeSelected(int type) {
	cerr << "In typeSelected() with " << "type=" << type << endl;
}

void ApplicationUI::languageSelected(int language) {
	cerr << "In languageSelected() with " << "language=" << language << endl;
}
