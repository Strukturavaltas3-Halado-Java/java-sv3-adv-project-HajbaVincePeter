# Vizsgaremek

## Leírás

Itt le tudjátok írni, hogy mi az alapötlet, miért választottátok ezt a feladatot stb stb....

---

## Felépítés

### Entitás 1

A `Entitás 1` entitás a következő attribútumokkal rendelkezik:

* id
* ... (milyen elvárások vannak az attribútummal szemben pl nem üres, pozitív stb stb)

Vágpontok: 

| HTTP metódus | Végpont                 | Leírás                                                                 |
| ------------ | ----------------------- | ---------------------------------------------------------------------- |
| GET          | `"/api/entities"`        | lekérdezi az összes entitást                                         |
| GET          | `"/api/entities/{id}"`   | lekérdez egy entitást `id` alapján                                      |

Ide még leírhattok sepciális üzleti logikát, pl a dátum nem lehet nagyobb az előzőnél stb stb


---

### Entitás 2 

A `Entitás 2 ` entitás a következő attribútumokkal rendelkezik:

* `id`
* ...

A `Entitás 1` és a `Entitás 2` entitások között kétirányú, 1-n kapcsolat van.

Végpontok:

| HTTP metódus | Végpont                 | Leírás                                                                 |
| ------------ | ----------------------- | ---------------------------------------------------------------------- |
| GET          | `"/api/secondentities"`        | lekérdezi az összes entitást                                         |
| GET          | `"/api/secondentities/{id}"`   | lekérdez egy entitást `id` alapján                                      |


Ide még leírhattok sepciális üzleti logikát, pl a dátum nem lehet nagyobb az előzőnél stb stb

---

## Technológiai részletek

Itt le tudjátok írni, hogy háromrétű, MariaDb, SwaggerUI, Repository, Service, Controller, Docker, Nem kell hogy hosszú legyen. 

---
