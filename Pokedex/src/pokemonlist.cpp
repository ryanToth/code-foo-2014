/*
 * PokemonList.cpp
 *
 *  Created on: 2013-11-02
 *      Author: Jagath
 */

#include "pokemonlist.h"
#include <iostream>
using std::cerr;

/*
 * PokemonList is derive from DataModel which provides the base class for the "model"
 * in the model-view-controller pattern used by the ListView UI control
*/

PokemonList::PokemonList(QObject* parent, QString type, QString lang) : bb::cascades::DataModel(parent) {

	m_typeIndex = type;
	m_langIndex = lang;
	m_numberOfPokemon = 0;
	start_up = true;

}

/* Return the number of child items contained in a given entry
 * Since we only implement a flat list, we must return 0 for any sub item.
*/

int PokemonList::childCount(const QVariantList& indexPath) {
	// For indexPath, See http://developer.blackberry.com/native/reference/cascades/bb__cascades__datamodel.html

	// Get the number of poekmon being outputted

	int i = pokeNum();

	// Return the item count if asking for top level count
	if (indexPath.empty())
		return i;	// TODO: Return the correct number of pokemon

	// No sub levels
	return 0;
}

// Used for displaying multi-level lists
bool PokemonList::hasChildren(const QVariantList& indexPath) {
	if (indexPath.empty())
		return true; // The root node always has children
	else
		return false;
}

// Return the data for the item given by indexPath
QVariant PokemonList::data(const QVariantList& indexPath) {

	if (start_up) {
		pokemon_list = getPokemonInfo();
		int count = pokeNum();

		for (int i = 0; i < count; i++) {
			pokemon_list[i] = getTypeNames(pokemon_list[i]);
			pokemon_list[i] = getAbilityAndVersionNames(pokemon_list[i]);
		}
	}

	start_up = false;

	int i = indexPath[0].toInt();  // Get the menu index
	QVariantMap data;


	Pokemon used = pokemon_list[i];

	data["a_name"] = used.names;
	data["type"] = used.types;
	data["titlebar"] = used.names_page;
	data["hp"] = used.hp;
	data["z_attack"] = used.attack;
	data["def"] = used.defense;
	data["specAttack"] = used.special_attack;
	data["specDef"] = used.special_defense;
	data["speed"] = used.speed;
	data["totalSP"] = used.total_stats;
	data["height"] = used.height;
	data["weight"] = used.weight;
	data["version"] = used.version;
	data["ability"] = used.ability;
	data["description"] = used.description;
	data["hpLabel"] = statLang("1");
	data["z_attackLabel"] = statLang("2");
	data["defenseLabel"] = statLang("3");
	data["specAttackLabel"] = statLang("4");
	data["specDefLabel"] = statLang("5");
	data["speedLabel"] = statLang("6");

	// Adds all information needed to be displayed on item page to the data QVariantMap

	return data;			// Return the name as a QVariant object
}

PokemonList::~PokemonList() {
	// TODO: Delete any object that were created
}

// Counts how many pokemon of the chosen type
int PokemonList::pokeNum()
{
	int count(0);
	m_numberOfPokemon = 0;

	QFile file("app/native/assets/data/pokemon_types.csv");
		if (file.open(QIODevice::ReadOnly | QIODevice::Text)) {

			QTextStream in(&file);
			while (!in.atEnd()) {
				QString line = in.readLine();
				if (line.split(",")[0].length() < 5 && line.split(",")[2] == "1")
					m_numberOfPokemon++;

				// Counts how many total pokemon there are

				if (line.split(",")[0].toInt() < m_numberOfPokemon+1 && line.split(",")[1] == m_typeIndex) {
					count++;
				}
			}
		}

		else {
			cerr <<"Failed to open pokemon_types.cvs: " << file.error() << endl;
			exit(0);
			//Exits the program if the file isn't found
		}


		if (m_typeIndex == "0")
			return m_numberOfPokemon; // Returns the number of all pokemon for All Type

		else {
			return count; // Returns number of selected type
		}
}

Pokemon* PokemonList::getPokemonInfo()
{
	int count = pokeNum();
	Pokemon *pokemon = new Pokemon[m_numberOfPokemon];
	Pokemon *sendList = new Pokemon[count];
	int i(0);
	bool a = false;


	{
		QFile file("app/native/assets/data/pokemon_types.csv");
			if (file.open(QIODevice::ReadOnly | QIODevice::Text)) {

				QTextStream in(&file);
				while (!in.atEnd()) {
					QString line = in.readLine();
					if (a == true && line.split(",")[0].toInt() < 719 && i < 718) {

						if (line.split(",")[2] == "2")
						{
							i--;
							pokemon[i].type_id_2 = line.split(",")[1];
						}

						pokemon[i].id = line.split(",")[0];

						if (line.split(",")[2] == "1")
							pokemon[i].type_id_1 = line.split(",")[1];

						if (i == 717)
							pokemon[i].type_id_2 = "5";

						i++;
					}
					a = true;
				}
			}

			// Gets both types for all pokemon
			else {
				cerr <<"Failed to open pokemon_types.cvs: " << file.error() << endl;
				exit(0);
			}
	}

	i = 0;

	QFile fil("app/native/assets/data/pokemon_species_names.csv");
		if (fil.open(QIODevice::ReadOnly | QIODevice::Text)) {

			QTextStream in(&fil);
			while (!in.atEnd()) {
				QString line = in.readLine();

				if (i < m_numberOfPokemon && line.split(",")[1] == m_langIndex)
				{
					pokemon[i].names = line.split(",")[2];
					pokemon[i].names_page = line.split(",")[2];
					pokemon[i].lang_check = true;
				}

				if (i < m_numberOfPokemon && m_langIndex != "9" && line.split(",")[1] == "9" && pokemon[i].lang_check == false)
				{
					// If the name isn't in the selected language, get it in english and add [English]
					pokemon[i].names_page = line.split(",")[2] + " [English]";
					pokemon[i].names = line.split(",")[2];
				}

				if (line.split(",")[1] == "9")
					i++;
			}
		}
		// Gets the names of every pokemon

		else {
			cerr <<"Failed to open pokemon_species_names.cvs: " << fil.error() << endl;
			exit(0);
		}

		int j = 0;
		int numberOfThisType = m_numberOfPokemon;

		if (m_typeIndex != "0")
		{
			for (i = 0; i < m_numberOfPokemon; i++)
			{
				if (pokemon[i].type_id_1 == m_typeIndex || pokemon[i].type_id_2 == m_typeIndex)
				{
					sendList[j].type_id_1 = pokemon[i].type_id_1;
					sendList[j].type_id_2 = pokemon[i].type_id_2;
					sendList[j].id = pokemon[i].id;
					sendList[j].names = pokemon[i].names;
					sendList[j].names_page = pokemon[i].names_page;
					j++;
				}
			}
			// Returns only the pokemon of the selected type
			pokemon = sendList;
			numberOfThisType = j;
		}

		j = 0;

		//Gets the stats for the pokemon that are going to be displayed in the list view
		QFile Statsfile("app/native/assets/data/pokemon_stats.csv");
			if (Statsfile.open(QIODevice::ReadOnly | QIODevice::Text)) {

				QTextStream in(&Statsfile);
				while (!in.atEnd()) {
					QString line = in.readLine();
					if (j < numberOfThisType && line.split(",")[0] == pokemon[j].id) {

						if (line.split(",")[1] == "1")
							pokemon[j].hp = line.split(",")[2];

						else if (line.split(",")[1] == "2")
							pokemon[j].attack = line.split(",")[2];

						else if (line.split(",")[1] == "3")
							pokemon[j].defense = line.split(",")[2];

						else if (line.split(",")[1] == "4")
							pokemon[j].special_attack = line.split(",")[2];

						else if (line.split(",")[1] == "5")
							pokemon[j].special_defense = line.split(",")[2];

						else {
							pokemon[j].speed = line.split(",")[2];
							pokemon[j].total_stats = QString::number(pokemon[j].special_attack.toInt() + pokemon[j].speed.toInt() + pokemon[j].special_defense.toInt() + pokemon[j].attack.toInt() +pokemon[j].defense.toInt() + pokemon[j].hp.toInt());
							j++;
						}
					}
				}
			}

			else {
				cerr <<"Failed to open pokemon_stats.cvs: " << fil.error() << endl;
				exit(0);
			}

			j = 0;

			// Gets the heights and weights of the pokemon. If they arent in the cvs, make them "unavailable"
			QFile file("app/native/assets/data/pokemon.csv");
			if (file.open(QIODevice::ReadOnly | QIODevice::Text)) {

				QTextStream in(&file);
				while (!in.atEnd()) {
					QString line = in.readLine();
					if (j < numberOfThisType && line.split(",")[0] == pokemon[j].id) {

						pokemon[j].height = QString::number((line.split(",")[3]).toFloat()/10);
						pokemon[j].weight = QString::number((line.split(",")[4]).toFloat()/10);

						if (pokemon[j].height == "0")
							pokemon[j].height = "Unavailable";

						if (pokemon[j].weight == "0")
							pokemon[j].weight = "Unavailable";

						j++;
					}
				}
			}

			else {
				cerr <<"Failed to open pokemon.cvs: " << file.error() << endl;
				exit(0);
			}

			j = 0;

			// Get the ability id for each pokemnon
			QFile iy("app/native/assets/data/pokemon_abilities.csv");
			if (iy.open(QIODevice::ReadOnly | QIODevice::Text)) {

				QTextStream in(&iy);
				while (!in.atEnd()) {
					QString line = in.readLine();

					if (j != 0 && j-1 < numberOfThisType && line.split(",")[0] == pokemon[j-1].id && line.split(",")[2] == "0" && line.split(",")[3] == "2") {
						pokemon[j-1].ability_2 = line.split(",")[1];
					}

					if (j < numberOfThisType && line.split(",")[0] == pokemon[j].id && line.split(",")[3] == "1") {
						pokemon[j].ability = line.split(",")[1];
						j++;
					}

				}
			}

			else {
				cerr <<"Failed to open pokemon_abilities.cvs: " << iy.error() << endl;
				exit(0);
			}

			j = 0;

			/* Gets the description for each pokemon, if there is a comma in the description, recognize that
			 * and make sure the whole description is added
			*/

			QFile ile("app/native/assets/data/pokemon_species_flavor_text.csv");
			if (ile.open(QIODevice::ReadOnly | QIODevice::Text)) {

				QTextStream in(&ile);
				while (!in.atEnd()) {
					QString line = in.readLine();

					if (j < numberOfThisType && pokemon[j].version_check == false && line.split(",")[0] == pokemon[j].id && (m_langIndex == line.split(",")[2] || (m_langIndex != "5" && "9" == line.split(",")[2])))
					{
						pokemon[j].description = "";

						for (int i = 3; i < line.split(",").length(); i++)
						{
							if (i == 3)
								pokemon[j].description = line.split(",")[i];

							else pokemon[j].description = pokemon[j].description + ", " + line.split(",")[i];
						}

						j++;
					}

					if (j != 0 && j-1 < numberOfThisType && line.split(",")[0] == pokemon[j-1].id && pokemon[j-1].version_check == false) {
						pokemon[j-1].version = line.split(",")[1];
						pokemon[j-1].version_check = true;
					}
				}
			}

			else {
				cerr <<"Failed to open pokemon_species_flavor_text.cvs: " << ile.error() << endl;
				exit(0);
			}

		// Returns all the pokemon if All Types is selected and only
			//the ones of the selected type if a type is selected

		return pokemon;
}

Pokemon PokemonList::getTypeNames(Pokemon pokemon)
{
	TypeSet type1, type2;
	type1.type = pokemon.type_id_1;
	type2.type = pokemon.type_id_2;

	// Gets the names of each type in the selected language
	QFile fil("app/native/assets/data/type_names.csv");
		if (fil.open(QIODevice::ReadOnly | QIODevice::Text)) {

			QTextStream in(&fil);
			while (!in.atEnd()) {
				QString line = in.readLine();

				if (line.split(",")[0] == type1.type && line.split(",")[1] == m_langIndex)
				{
					pokemon.type1 = line.split(",")[2];
					type1.lang_check = true;
				}

				if (m_langIndex != "9" && line.split(",")[1] == "9" && type1.lang_check == false && line.split(",")[0] == type1.type)
					pokemon.type1 = line.split(",")[2];
				// Gets the type in english if the selected language isn't there


				if (line.split(",")[0] == type2.type && line.split(",")[1] == m_langIndex)
				{
					pokemon.type2 = line.split(",")[2];
					type2.lang_check = true;
				}

				if (m_langIndex != "9" && line.split(",")[1] == "9" && type2.lang_check == false && line.split(",")[0] == type2.type)
					pokemon.type2 = line.split(",")[2];
				// Gets the type in english if the selected language isn't there

			}
		}

		else {
			cerr <<"Failed to open type_names.cvs: " << fil.error() << endl;
			exit(0);
		}

		if (pokemon.type2 != "")
			pokemon.types = pokemon.type1 + ", " + pokemon.type2;

		else pokemon.types = pokemon.type1;


		if ((pokemon.id).toInt() < 10)
			pokemon.names_page = "#00" + pokemon.id + " " + pokemon.names_page;

		else if ((pokemon.id).toInt() < 100)
			pokemon.names_page = "#0" + pokemon.id + " " + pokemon.names_page;

		else
			pokemon.names_page = "#" + pokemon.id + " " + pokemon.names_page;

		// Adds pokemon id number to the beginning of the name and nothing after for the item page screen

		if ((pokemon.id).toInt() < 10)
			pokemon.names = "#" + ("00"+pokemon.id) + " " + pokemon.names + " [" + pokemon.types +"]";

		else if ((pokemon.id).toInt() < 100)
			pokemon.names = "#" + ("0"+pokemon.id) + " " + pokemon.names + " [" + pokemon.types +"]";

		else
			pokemon.names = "#" + pokemon.id + " " + pokemon.names + " [" + pokemon.types +"]";

		// Adds the id number of the pokemon to the beginning of the name and the type after

	return pokemon;
}

Pokemon PokemonList::getAbilityAndVersionNames(Pokemon pokemon)
{
	bool langCheck = false;

		QString also = QString::number(pokemon.version.toInt()+1);

		QFile il("app/native/assets/data/version_names.csv");
		if (il.open(QIODevice::ReadOnly | QIODevice::Text)) {

			QTextStream in(&il);
			while (!in.atEnd()) {
				QString line = in.readLine();

				if (line.split(",")[0] == pokemon.version)
					pokemon.version = line.split(",")[2];

				if (line.split(",")[0] == also)
					also = line.split(",")[2];
			}
		}

		// Get both versions that the pokemon first apeared in

		else {
			cerr <<"Failed to open version_names.cvs: " << il.error() << endl;
			exit(0);
		}

		pokemon.version = pokemon.version + " and " + also;

		pokemon.version = pokemon.version.split(" ")[0] + " " + pokemon.version.split(" ")[1] + " " + pokemon.version.split(" ")[2];

		langCheck = false;
		bool langCheck_2(false);

		// Get the ability name in the selected language or in english if unavailable
		QFile li("app/native/assets/data/ability_names.csv");
		if (li.open(QIODevice::ReadOnly | QIODevice::Text)) {

			QTextStream in(&li);
			while (!in.atEnd()) {
				QString line = in.readLine();
				if (line.split(",")[0] == pokemon.ability && m_langIndex == line.split(",")[1])
				{
					pokemon.ability = line.split(",")[2];
					langCheck = true;
				}

				if (line.split(",")[0] == pokemon.ability && line.split(",")[1] == "9" && langCheck == false)
					pokemon.ability = line.split(",")[2];

				if (line.split(",")[0] == pokemon.ability_2 && m_langIndex == line.split(",")[1])
				{
					pokemon.ability_2 = line.split(",")[2];
					langCheck_2 = true;
				}

				if (line.split(",")[0] == pokemon.ability_2 && line.split(",")[1] == "9" && langCheck_2 == false)
					pokemon.ability_2 = line.split(",")[2];
			}
		}

		else {
			cerr <<"Failed to open ability_names.cvs: " << li.error() << endl;
			exit(0);
		}

		if (pokemon.ability_2 != "")
			pokemon.ability = pokemon.ability + ", " + pokemon.ability_2;

		return pokemon;
}

// Allows access the the private member variables for selected type and language

QString PokemonList::getLang()
{
	return m_langIndex;
}

void PokemonList::setLang(QString language)
{
	m_langIndex = language;
	start_up = true;
}

QString PokemonList::getType()
{
	return m_typeIndex;
}

void PokemonList::setType(QString type)
{
	m_typeIndex = type;
	start_up = true;
}

QString PokemonList::statLang(QString statIndex)
{
	QString statName;
	bool language_check = true;

	QFile file("app/native/assets/data/stat_names.csv");
	if (file.open(QIODevice::ReadOnly | QIODevice::Text)) {

		QTextStream in(&file);
		while (!in.atEnd()) {
			QString line = in.readLine();

			if (line.split(",")[0] == statIndex && line.split(",")[1] == m_langIndex)
			{
				statName = line.split(",")[2] +":";
				language_check = false;
			}

			else if (line.split(",")[1] == "9" && language_check && line.split(",")[0] == statIndex)
				statName = line.split(",")[2] + ":";

			}
		}

	// Gets the stat names in different languages, defaulting to english

	else {
		cerr <<"Failed to open stat_names.cvs: " << file.error() << endl;
		exit(0);
	}

	return statName;
}
