# RestPost-app (REST based parcel service web-application) - REST alapú csomagküldő szolgáltatás
___
## Leírás


Az alkalmazás egy csomagküldő szolgáltatás magfunkcióját modellezi - szállítmányok létrehozását vendégfelhasználóként: kiindulási és érkezési címek, csomagok létrehozása és az ezekből összeállított szállítmány megrendelése és visszakeresése egyedi azonosító alapján.
A jelenlegi munkám során is részeben hasonló szállítmányozási alkalmazások támogatásával foglalkozom.

Alapfunkcióként a címek és csomagok létrehozhatóak, módosíthatóak és törölhetőek - az ezekből összeállított szállítmány pedig - amennyiben minden szükséges elemet tartalmaz - véglegesíthető, illetve később az ekkor megkapott publikus egyedi azonosítója alapján - a nyomonkövetést modellezendő - visszakereshető. 
Az alkalmazás végpontjai egy-egy szállítmány weboldalon történő létrehozásának lépéseihez alkalmazkodnak.

---

## Felépítés
___
### Address (cím) entitás

Az `Address` (szülő) entitás a következő attribútumokkal rendelkezik:

* `id`: Long (generált) 
* `name`: String  - a feladó/címzett neve
* `streatName` : String  - utca, házszám, stb.
* `city` : String - település
* `country` : Country (enum) - ország


Az ebből származó `AddressWithPostalCode`  (gyermek) entitásnak a fentieken kívül még egy attribútuma van:

* `postalCode`: String - irányítószám
      - Az egyes országok eltérő irányítószám formátumait az ország-enum tárolja és az alkalmazás ez alapján ellenőrzi a bevitt értéket.

A szintén az `Address`-ből származó `AddressIrish` (gyermek) entitásnak pedig a következő saját tulajdonsága:

* `county` County (enum) - megye
  - Írországban a megye helyettesíti az irányítószámot  


Végpontok:

| HTTP metódus | Végpont                 | Leírás                                              |
|--------------|-------------------------|-----------------------------------------------------|
| GET          | `"/api/addresses"`      | lekérdezi az összes címet                           |
| GET          | `"/api/addresses/{id}"` | lekérdez egy címet `id` alapján                     |
| GET          | `"/api/addresses/countries"` | lekérdezi az ország enum összes adatát              |
| GET          | `"/api/addresses/counties"` | lekérdezi az írországi megye enum összes adatát     |
| POST         | `"/api/addresses"`      | létrehoz egy kizárólag országot tartalmazó címet    |
| PUT          | `"/api/addresses/{id}/ie"` | feltölt/módosít egy írországi címet                 |
| PUT          | `"/api/addresses/{id}/non-ie"` | feltölt/módosít egy irányítószámot tartalmazó címet |
| DELETE       | `"/api/addresses/{id}"` | töröl egy címet `id` alapján                        |

A címek esetében öröklődésre volt szükség, hogy a különböző attribútumokkal rendelkező entitásokat jellemzően egységesen lehessen kezelni.

---

### Package (csomag) entitás

A `Package` entitás a következő attribútumokkal rendelkezik:

* `id` : Long (generált)
* `weight` : Integer - a csomag tömege kg-ban megadva. 
  - 100-nál nem nagyobb pozitív értéket vehet fel.
   
* `shipment` : Shipment  - a szállítmány, amelynek része lehet az adott csomag. 
  - Null érték esetén értelemszerűen nem tartozik szállítmányhoz.

 
A `Package` és a `Shipment` entitások között kétirányú, n-1 kapcsolat van.

Végpontok:

| HTTP metódus | Végpont                                 | Leírás                                                          |
|--------------|-----------------------------------------|-----------------------------------------------------------------|
| GET          | `"/api/packages"`                       | lekérdezi az összes csomagot                                    |
| GET          | `"/api/packages/{id}"`                  | lekérdez egy csomagot `id` alapján                              |
| GET          | `"/api/packages/shipment/{shipmentId}"` | lekérdezi egy szállítmány csomaglistáját a `shipmentId` alapján |
| POST         | `"/api/packages"`                       | létrehoz egy csomagot                                           |
| PUT          | `"/api/packages"`                       | felülírja egy csomag adatait                                    |
| DELETE       | `"/api/packages/{id}"`                  | töröl egy csomagot `id` alapján                                 |


A csomag adatainak (tömegének) felülírását csak abban az esetben hajtja végre az alkalmazás, amennyiben a kérés törzsében pontosan meghatároztuk, hogy mely szállítmányhoz tartozik - vagy nem tartozik egyikhez sem (null érték).

Törölni kizárólag szállítmányhoz nem tartozó csomagot lehet.

___

### Shipment (szállítmány) entitás

A `Shipment` entitás a következő attribútumokkal rendelkezik:

* `id` : Long (generált),
* `trackingNumber`: String - a véglegesített szállítmány UUID-ja
* `from`: Address - a feladási cím
* `to`: Address - a kézbesítési cím
* `shippingDate` : LocalData - a feladás dátuma (megadáskor, véglegesítéskor csak jövőbeli lehet)
* `packages` : Set<Package>  - a szállítmányt alkotó csomagok 

A `Shipment` és az `Address` entitás között két darab egyirányú n-1 kapcsolat van definiálva.
A `Shipment` és a  `Package` entitás között kétirányú 1-n jellegű a kapcsolat.

Végpontok:

| HTTP metódus | Végpont                                             | Leírás                                                         |
|--------------|-----------------------------------------------------|----------------------------------------------------------------|
| GET          | `"/api/shipments"`                                  | lekérdezi az összes szállítmányt                               |
| GET          | `"/api/shipments/{trackingnumber}"`                 | lekérdez egy csomagot `trackingnumber` alapján                 |
| POST         | `"/api/shipments"`                                  | létrehoz egy szállítmányt                                      |
| PUT          | `"/api/shipments/{id}"`                             | felülírja egy szállítmány adatait                              |
| PUT          | `"/api/shipments/{id}/process"`                     | véglegesíti a szállítmányt                                     |
| PUT          | `"/api/shipments/{shipmentId}/package/{packageId}"` | hozzáad egy csomagot `packageId` a szállítmányhoz `shipmentId` |
| DELETE       | `"/api/shipments/{id}"`                             | töröl egy szállítmányt `id` alapján                            |


A szállítmány csak akkor véglegesíthető, ha nincs hiányzó adata - sem közvetlenül, sem a tartalmazott objektumokban. Az ezt vizsgáló rekurzív algoritmus a Dto tetszőleges faszerkezetű bővítésének vizsgálatára is alkalmas.

A tervezett feladási dátum jövőbeliségét véglegesítéskor újra ellenőrzzük. 

A szállítmány törlése törli a hozzá tartozó csomagokat de nem törli a címeit.

---

## Technológiai részletek


* Háromrétegű REST-api alkalmazás - megfelelő repository, service és kontroller osztályokkal.
* Az adatbázis MariaDb, a migrációt a Liquibase végzi.
* Az entitások és dto-k között a Mapstruct fordít. 
* A nullértékek keresését reflectiont használó rekurzív algoritmussal oldottam meg.
* A gyökérkönyvtárban található Docker- és a docker-compose fájlok révén Docker-konténerekben is futtatható.
* Tesztkörnyezetet és dokumentációt a Swagger UI biztosít az alkalmazás számára.

---

