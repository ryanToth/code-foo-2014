#ifndef ApplicationUI_H_
#define ApplicationUI_H_

#include <QObject>

namespace bb
{
    namespace cascades
    {
        class Application;
    }
}

/*!
 * @brief Application object
 *
 *
 */
class PokemonList; // forward declaration to avoid including header

class ApplicationUI : public QObject
{
    Q_OBJECT
public:
    ApplicationUI(bb::cascades::Application *app);
    // callables from QML
    Q_INVOKABLE void typeSelected(int type);
    Q_INVOKABLE void languageSelected(int language);

    virtual ~ApplicationUI() { }
private:
    PokemonList *m_pokemonList;
};

#endif /* ApplicationUI_H_ */
