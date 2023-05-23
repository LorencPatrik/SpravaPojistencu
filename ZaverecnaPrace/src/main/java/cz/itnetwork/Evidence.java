package cz.itnetwork;

import java.util.List;
import java.util.ArrayList;

public class Evidence {
    /**
     * Zde místo databáze :-( pár přednastavených pojištěných, aby jsem nemuseli pokaždé nejdříve naplnit kolekci daty...
     */
    private List<Pojisteny> pojisteneOsoby = new ArrayList<>();

    public Evidence() {
        pojisteneOsoby.add(new Pojisteny("Antonín", "Jurášek", 1960, "772163892"));
        pojisteneOsoby.add(new Pojisteny("Martin", "Zahradil", 1983, "608172489"));
        pojisteneOsoby.add(new Pojisteny("Jaroslav", "Bystrý", 1970, "772076248"));
        pojisteneOsoby.add(new Pojisteny("Bohumil", "Červinka", 2008, "608173549"));
        pojisteneOsoby.add(new Pojisteny("Marcel", "Dvořáček", 1989, "775128634"));
        pojisteneOsoby.add(new Pojisteny("Miroslav", "Michalec", 2015, "773658978"));
        pojisteneOsoby.add(new Pojisteny("Robert", "Novotný", 1977, "773658978"));
    }

    /**
     * @return vrátí kompletní kolekci pojištěných
     */
    public List<Pojisteny> vratPojistene() {
        return pojisteneOsoby;
    }

    /**
     * Vymaže zvoleného pojištěného z kolekce pojištěných.
     * @param zadanyIndex index pojištěného v kolekci pojištěných
     */
    public void vymazPojisteneho(int zadanyIndex) {
        pojisteneOsoby.remove(zadanyIndex);// nehlídám jestli k vymazání došlo, ale hlídám rozsah zadaného indexu. Nemělo by být možné sáhnout mimo kolekci.
    }

    /**
     * Přidá nového pojištěného do kolekce pojištěných.
     * @return vráti nově přidaného pojištěného
     */
    public Pojisteny pridejPojisteneho(String jmeno, String prijmeni, int rokNarozeni, String telCislo) {
        pojisteneOsoby.add(new Pojisteny(jmeno, prijmeni, rokNarozeni, telCislo));
        return pojisteneOsoby.get(pojisteneOsoby.size() - 1);
    }

    /**
     * Nastaví nové hodnoty parametrů pojištěného. Každý z parametrů bude změněn pokud není prázdný (u Roku narození když není -1).
     * @param zadanyIndex index pojištěného, kterého budeme editovat
     */
    public void editujPojisteneho(int zadanyIndex, String jmeno, String prijmeni, int rokNarozeni, String telCislo) {
        Pojisteny pojisteny = pojisteneOsoby.get(zadanyIndex);
        if (jmeno.length() > 0)
            pojisteny.setJmeno(jmeno);
        if (prijmeni.length() > 0)
            pojisteny.setPrijmeni(prijmeni);
        if (rokNarozeni != -1)
            pojisteny.setRokNarozeni(rokNarozeni);
        if (telCislo.length() > 0) // musíme otestovat i poslední podmínku - nevíme, která je prázdná....
            pojisteny.setTelCislo(telCislo);
    }
}