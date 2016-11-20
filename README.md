# TranslationService
Service for message translation

##Endpoints
###/packages/{packageId} [GET]
####Gets a translationsPackage by it's ID.

**Response**:
```json
{ 
  "version": 3,
  "translations": {
    "WELCOME": {
      "EN": "Welcome to Exorath!",
      "FR": "Bienvenue à Exorath!",
      "NL": "Welkom bij Exorath!",
      "ES": "Bienvenido a Exorath!",
      "DE": "Willkommen zu exorath!"
    },
    "JOIN_SUFFIX": {
      "EN": "joined the game",
      "FR": "a rejoint le jeu",
      "NL": "heeft het spel gejoined",
      "ES": "se unió al juego",
      "DE": "schloss sich an"
    }
  }
}
```
- version (number): the current version of the translationsPackage
- translations (jsonobjcet): All the translations by their key

###/packages/{packageId}/version [GET]
####Gets the version of a translationsPackage.

**Response**:
```json
{"version": 3}
```
- version (number): the current version of the translationsPackage

###/packages/{packageId} [PUT]
####Updates the package if the version is the most recent.
If the version is smaller or equal to the current version of the translation package, the update will not happen and success=false will be returned.

**Body**:
```json
{"version": 4,
  "translations": {
    "WELCOME": {
      "EN": "Welcome to Exorath!",
      "FR": "Bienvenue à Exorath!",
      "NL": "Welkom bij Exorath!",
      "ES": "Bienvenido a Exorath!",
      "DE": "Willkommen zu exorath!"
    },
    "JOIN_SUFFIX": {
      "EN": "joined the game",
      "FR": "a rejoint le jeu",
      "NL": "heeft het spel gejoined",
      "ES": "se unió al juego",
      "DE": "schloss sich an"
    }
  },
    "LEAVE_SUFFIX": {
      "EN": "left the game",
      "FR": "a quitté le jeu",
      "NL": "verliet het spel",
      "ES": "abandonó el juego",
      "DE": "verließ das Spiel"
    }
  }
}
```

**Response**:
```json
{"success": true}
```
- success (boolean): Whether or not the player will be teleported to a server.
- err (string)[OPTIONAL]: An error string that describes why the update failed. Only provided when success=*false*
