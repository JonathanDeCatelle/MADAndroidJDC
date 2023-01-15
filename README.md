# Android - Delaware - Niels Van Hove
Dit is de repository van de Android track and trace applicatie gecreëerd voor Delaware door groep 12.

Met deze applicatie kan je bestellingen plaatsen en deze bestelling dan ook volgen tot hij bij jouw thuis is.

## Opstarten van de applicatie
Een vereiste om deze applicatie te gebruiken, is dat de API moet lopen. Als deze niet online loopt kan je ook de repository clonen van dit project op https://github.com/HoGentTIN/devops-22-23-dotnet-g12 en deze runnen. In de Android repo moet je retrofit variabele vervangen door het volgende

```kotlin
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .client(fakeClient)
    //.client(client)
    .build()
```

De app moet je opstarten door deze repo te clonen en deze dan te openen in Android studio. Hierin kan je dan de app starten als je een emulator hebt geïnstalleerd.

## Bekende bugs
### Order detail pagina
De ETA wordt pas geladen bij het inladen van de Google Maps kaart

![ETA](afbeeldingenREADME/eta.png "ETA")
