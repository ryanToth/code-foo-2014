import bb.cascades 1.2

Page {
    property alias titlebar: titlebar.title
    property alias type: type_label.text // "type" property of this screen is mapped to the text field of the UI element with the id of "type_label"
    property alias hp: hp_label.text
    property alias z_attack: z_attack_label.text
    property alias def: def_label.text
    property alias specAttack: specAttack_label.text
    property alias specDef: specDef_label.text
    property alias speed: speed_label.text
    property alias totalSP: totalSP_label.text
    property alias height: height_label.text
    property alias weight: weight_label.text
    property alias version: version_label.text
    property alias ability: ability_label.text
    property alias description: description_label.text
    property alias hpLabel: hpLabel_label.text
    property alias z_attackLabel: z_attackLabel_label.text
    property alias defenseLabel: defenseLabel_label.text
    property alias specAttackLabel: specAttackLabel_label.text
    property alias specDefLabel: specDefLabel_label.text
    property alias speedLabel: speedLabel_label.text

    // TODO: create other aliases that will be set from main.qml
    
    titleBar: TitleBar {
        id: titlebar
    }
    Container {
        Container { // Make a container to show pokemon type
            layout: StackLayout { // Lay out sub items left-to-right
                orientation: LayoutOrientation.LeftToRight
            }
            Label { 
                text: "Type: "
                textStyle.fontWeight: FontWeight.Bold
                textStyle.color: Color.DarkRed // Show this text
            } 
	        Label {
                id: type_label // Name this as type_label so that the property alias above can set the text property of this item
            } 
        }
        
        // TODO: Add other containers to display other stats of the pokemon
        Container { // Make a container to show pokemon ability
            layout: StackLayout { // Lay out sub items left-to-right
                orientation: LayoutOrientation.LeftToRight
            }
            Label { 
                text: "Ability: "
                textStyle.fontWeight: FontWeight.Bold
                textStyle.color: Color.DarkRed // Show this text
            } 
            Label {
                id: ability_label // Name this as ability_label so that the property alias above can set the text property of this item
            } 
        }
        
        Container { // Make a container to show pokemon hp
            layout: StackLayout { // Lay out sub items left-to-right
                orientation: LayoutOrientation.LeftToRight
            }
            Label { 
                id: hpLabel_label
                textStyle.fontWeight: FontWeight.Bold
                textStyle.color: Color.DarkRed // Show this text
            } 
            Label {
                id: hp_label // Name this as hp_label so that the property alias above can set the text property of this item
            } 
        }
        
        Container { // Make a container to show pokemon attack
            layout: StackLayout { // Lay out sub items left-to-right
                orientation: LayoutOrientation.LeftToRight
            }
            Label { 
                id: z_attackLabel_label
                textStyle.fontWeight: FontWeight.Bold
                textStyle.color: Color.DarkRed // Show this text
            } 
            Label {
                // Need the "z" so it doesn't default to the attack in List View
                id: z_attack_label // Name this as z_attack_label so that the property alias above can set the text property of this item
            } 
        }
        
        Container { // Make a container to show pokemon defense
            layout: StackLayout { // Lay out sub items left-to-right
                orientation: LayoutOrientation.LeftToRight
            }
            Label { 
                id: defenseLabel_label
                textStyle.fontWeight: FontWeight.Bold
                textStyle.color: Color.DarkRed // Show this text
            } 
            Label {
                id: def_label // Name this as def_label so that the property alias above can set the text property of this item
            } 
        }
        
        Container { // Make a container to show pokemon special attack
            layout: StackLayout { // Lay out sub items left-to-right
                orientation: LayoutOrientation.LeftToRight
            }
            Label { 
                id: specAttackLabel_label
                textStyle.fontWeight: FontWeight.Bold
                textStyle.color: Color.DarkRed // Show this text
            } 
            Label {
                id: specAttack_label // Name this as specAttack_label so that the property alias above can set the text property of this item
            } 
        }
        
        Container { // Make a container to show pokemon special defense
            layout: StackLayout { // Lay out sub items left-to-right
                orientation: LayoutOrientation.LeftToRight
            }
            Label { 
                id: specDefLabel_label
                textStyle.fontWeight: FontWeight.Bold
                textStyle.color: Color.DarkRed // Show this text
            } 
            Label {
                id: specDef_label // Name this as specDef_label so that the property alias above can set the text property of this item
            } 
        }
        
        Container { // Make a container to show pokemon speed
            layout: StackLayout { // Lay out sub items left-to-right
                orientation: LayoutOrientation.LeftToRight
            }
            Label { 
                id: speedLabel_label
                textStyle.fontWeight: FontWeight.Bold
                textStyle.color: Color.DarkRed // Show this text
            } 
            Label {
                id: speed_label // Name this as speed_label so that the property alias above can set the text property of this item
            } 
        }
        
        Container { // Make a container to show pokemon total stat points
            layout: StackLayout { // Lay out sub items left-to-right
                orientation: LayoutOrientation.LeftToRight
            }
            Label { 
                text: "Total Stat Points: "
                textStyle.fontWeight: FontWeight.Bold
                textStyle.color: Color.DarkRed // Show this text
            } 
            Label {
                id: totalSP_label // Name this as totalSP_label so that the property alias above can set the text property of this item
            } 
        }
        
        Container { // Make a container to show pokemon height in m
            layout: StackLayout { // Lay out sub items left-to-right
                orientation: LayoutOrientation.LeftToRight
            }
            Label { 
                text: "Height (m): "
                textStyle.fontWeight: FontWeight.Bold
                textStyle.color: Color.DarkRed // Show this text
            } 
            Label {
                id: height_label // Name this as height_label so that the property alias above can set the text property of this item
            } 
        }
        
        Container { // Make a container to show pokemon weight in kg
            layout: StackLayout { // Lay out sub items left-to-right
                orientation: LayoutOrientation.LeftToRight
            }
            Label { 
                text: "Weight (kg): "
                textStyle.fontWeight: FontWeight.Bold
                textStyle.color: Color.DarkRed // Show this text
            } 
            Label {
                id: weight_label // Name this as weight_label so that the property alias above can set the text property of this item
            } 
        }
        
        Container { // Make a container to show the version introduced
            layout: StackLayout { // Lay out sub items left-to-right
                orientation: LayoutOrientation.LeftToRight
            }
            Label { 
                text: "Versions Introduced: "
                textStyle.fontWeight: FontWeight.Bold
                textStyle.color: Color.DarkRed // Show this text
            } 
            Label {
                id: version_label // Name this as version_label so that the property alias above can set the text property of this item
            } 
        }
        
        Container { // Make a container to show description
            layout: StackLayout { // Lay out sub items left-to-right
                orientation: LayoutOrientation.LeftToRight
            }
            Label { 
                text: "Description: "
                textStyle.fontWeight: FontWeight.Bold
                textStyle.color: Color.DarkRed // Show this text
            } 
        }
        
        Container { // Make a container to show the version introduced
            layout: StackLayout { // Lay out sub items left-to-right
                orientation: LayoutOrientation.LeftToRight
            }
            Label {
                // Will make the description show up on multiple lines instead of being cut off
                multiline: true
                id: description_label
            
            } 
        }
}
}