#include <bb/cascades/Application>

#include <QLocale>
#include <QTranslator>
#include "applicationui.h"

#include <Qt/qdeclarativedebug.h>

using namespace bb::cascades;

Q_DECL_EXPORT int main(int argc, char **argv)
{
    Application app(argc, argv);

    // Create the Application UI object, this is where the main.qml file
    // is loaded and the application scene is set.
    ApplicationUI *a = new ApplicationUI(&app);

    //Makes the app work

    app.setScene(a->root); //Modified from old ApplicationUI.cpp file from init()
    a->init(); //call the init() function

        // Enter the application main event loop.
    return Application::exec();
}
