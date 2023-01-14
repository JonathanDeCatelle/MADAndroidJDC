# DevOps groep 12 - Jonathan De Catelle - Delaware - Android applicatie
Dit is de repository van de Android track and trace applicatie gecreëerd voor Delaware door groep 12 em een fork gemaakt en aangepast door Dylan Bracke. De reden hiervoor een faulty eindcommit door een teamlid dus wordt een vorige versie teruggezet en werden er nog aanapssingen gemaakt om alle functionaliteit terug te krijgen.

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

## Console
De console knop is enkel beschikbaar ter demo functionaliteit.


## Bekende bugs
### Order detail pagina

1. De ETA bij bestelling verder dan de status BESTELD worden pas geladen bij het inladen van de Google Maps kaart.

![ETA](afbeeldingenREADME/eta.png "ETA")
