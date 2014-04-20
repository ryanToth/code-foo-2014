/*
 * PokemonList.h
 *
 *  Created on: 2013-11-02
 *      Author: Jagath
 */

#ifndef POKEMONLIST_H_
#define POKEMONLIST_H_

#include <bb/cascades/DataModel>

// Structure for types, only needed to find out if the type is available in given language
// using the bool check variable

struct TypeSet {
	TypeSet() : lang_check(false) {}
	QString type;
	bool lang_check;
};

/* Structure to hold all the information about each pokemon and some variables to help with
 * recognizing if the names are available in a given language
 */

struct Pokemon {
		Pokemon() : lang_check(false), version_check(false), start_up(true) {}
		QString id;
		QString names;
		QString names_page;
		QString type_id_1;
		QString type_id_2;
		QString type1;
		QString type2;
		QString types;
		QString ability;
		QString ability_2;
		QString hp;
		QString attack;
		QString defense;
		QString special_attack;
		QString special_defense;
		QString speed;
		QString total_stats;
		QString height;
		QString weight;
		QString description;
		QString version;
		bool lang_check;
		bool version_check; // Makes it so the first occurrence of the pokemon in the .cvs file is stored
		bool start_up;
};


class PokemonList: public bb::cascades::DataModel {
	Q_OBJECT
public:
	PokemonList(QObject *parent = 0, QString m_typeIndex = "0", QString m_langIndex = "9");

private:
	// Private member variables for type and language so people can;t screw around with them
	QString m_langIndex;
	QString m_typeIndex;

public:
	// Required interface implementation
	virtual int childCount(const QVariantList& indexPath);
	virtual bool hasChildren(const QVariantList& indexPath);
	virtual QVariant data(const QVariantList& indexPath);

	virtual ~PokemonList();

	int pokeNum();
	Pokemon* getPokemonInfo();
	Pokemon getTypeNames(Pokemon pokemon);
	Pokemon getAbilityAndVersionNames(Pokemon pokemon);
	QString getLang();
	void setLang(QString language);
	QString getType();
	void setType(QString type);

	QString statLang(QString statIndex);

	/* Functions to clean up the code so that everything doesn't exist in one part of the .cpp file
	 * getTypeNames function and getAbilityAndVersionNames could have been melded into one function but it would
	 * most likely cause more problems because the code was originally designed to need both functons
	 * to be seperate
	 */

	Pokemon *pokemon_list;
	bool start_up;
	int m_numberOfPokemon;

	/* Makes the pokemon_list a memeber variable so it doesnt need to be passed as a variable
	 * in a few of the functions above
	 */
};
#endif /* POKEMONLIST_H_ */
