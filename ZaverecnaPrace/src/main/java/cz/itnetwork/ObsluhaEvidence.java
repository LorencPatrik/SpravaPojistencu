package cz.itnetwork;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ObsluhaEvidence {
    Scanner sc = new Scanner(System.in, "Utf-8");
    /**
     * DatumDnes využijeme při zadání roku narození v rozsahu 1900 - až do aktuálního roku
     */
    private Evidence evidence;
    private LocalDate datumDnes = LocalDate.now();

    /**
     * Konstruktor. Počáteční inicializace - vytvoření instance s metodami pro správu dat v evidenci pojištěných
     */
    public ObsluhaEvidence() {
        evidence = new Evidence();
    }

    /**
     * Přijme data pro vložení nového pojištěného a zavolá funkci pro jejich uložení do kolekce pojištěných
     */
    public void pridejPojisteneho() {
        System.out.println("\nZadání nového pojištěného.\n");
        String noveJmeno = zadejText("jméno", false);
        String novePrijmeni = zadejText("přijmení", false);
        System.out.printf("Zadejte rok narozeni %d - %d: ", datumDnes.getYear() - 99, datumDnes.getYear());
        int novyRokNarozeni = zadejCislo(datumDnes.getYear() - 99, datumDnes.getYear(), false);
        String noveTelCislo = zadejTel(false);
        Pojisteny pojisteny = evidence.pridejPojisteneho(noveJmeno, novePrijmeni, novyRokNarozeni, noveTelCislo);
        System.out.print("\nZadali jste nového pojištěného: ");
        //zde se mi nehodí formát výpisu jako přes toString(), potřebuju výpis bez pevných mezer mezi jménem a přijmením
        String tel = pojisteny.getTelCislo(); // pro zkrácený zápis v následujícím řádku...
        System.out.printf("%s %s %d tel: %s %s %s\n", pojisteny.getJmeno(), pojisteny.getPrijmeni(), pojisteny.getRokNarozeni(), tel.substring(0, 3), tel.substring(3, 6), tel.substring(6));
        System.out.println("Data byla uložena.\n");
    }

    /**
     * Vypíše seznam všech pojištěných v evidenci
     *
     * @param bezIndexu určí jestli bude výpis obsahovat i indexy pojištěných
     * @param nadpis    část nadpisu do hlavičky tabulky
     */
    public void vypisPojistene(boolean bezIndexu, String nadpis) {
        if (jeEvidencePrazdna())
            return;
        List<Pojisteny> pojisteneOsoby = evidence.vratPojistene();
        System.out.print("\n" + vratLinku());
        if (bezIndexu) {
            System.out.printf("| " + nadpis + " %d %s%-37s|\n", pojisteneOsoby.size(), sklonovani(pojisteneOsoby.size()), " ");
            System.out.print(vratLinku());
            pojisteneOsoby.forEach(System.out::println);
        } else {
            System.out.printf("| " + nadpis + " %d %s%-26s|\n", pojisteneOsoby.size(), sklonovani(pojisteneOsoby.size()), " ");
            System.out.print(vratLinku());
            // zde nemůžeme použít toString() index rozhodí šířku řádku...
            pojisteneOsoby.forEach(osoba -> System.out.printf("| [%d] %-18s%-18s %d     tel: %s %s %s |\n", pojisteneOsoby.indexOf(osoba), osoba.getJmeno(), osoba.getPrijmeni(), osoba.getRokNarozeni(), osoba.getTelCislo().substring(0, 3), osoba.getTelCislo().substring(3, 6), osoba.getTelCislo().substring(6)));
        }
        System.out.println(vratLinku());
    }

    /**
     * Fulltextové vyhledávání. Leze kombinovat čísla, texty i opakovaně s různými oddělovači.
     * - nalezenou shodu označí v řádku s výpisem u tel. čísel značí i části se shodou v číslech... ano, když se vynasnažíte tak
     *   se dostanete mimo tabulku... přesto jsem pravděpodobnou část variant ošetřil.
     * - pomíchané znaky s čísly bez oddělení ignoruje stejně tak zadání pod 3. znaky nebo čísla.
     * - Tel. čísla lze zadat libovolně jde o součet až po spojení. Např: x xx, xx xx xx, xxx xx xx xxx adt...
     * - Proto je potřeba po zadání celého čísla nebo jeho části zadat nějaký text a až potom pokračovat dalším číslem. Takto je
     *   vyhodnoceno kompletní zadání jednoho celého čísla.
     * - Nad vyhledanými položkami vypíše co vše bylo z vašeho zadání k vyhledávání použito.
     */
    public void vyhledejPojisteneho() {
        if (jeEvidencePrazdna())
            return;
        System.out.println("\nFulltextové  vyhledávání  dle  jména, příjmení  a  tel. čísla.  Můžete");
        System.out.println("zadat  libovolný  počet  parametrů. Zadání  tel. čísla  lze  libovolně");
        System.out.println("rozdělit nebo zadat jen část. Mezi jednotlivá tel. čísla vložte jméno,");
        System.out.println("příjmení  nebo jejich  část (jako oddělovač tel. čísel). Zadání kratší");
        System.out.println("než 3. znaky nebo text smíchaný s čísly bez oddělení bude vyřazen.");
        System.out.println("\nZadejte text pro vyhledávání:");
        String zadani = sc.nextLine().trim().toLowerCase();
        String[] splitZadani;
        if (!zadani.isEmpty()) {
            splitZadani = zadani.split("[-.,;: ]+");
        } else {
            System.out.println("Nic nezadáno :-( Tak snad příště...\n");
            return;
        }
        List<String> retezceKVyhledani = new ArrayList<>(); // zde se uloží všechny validní řetězce a čísla k vyhledávání
        StringBuilder pospojovaneCislo = new StringBuilder(); // konstrukce na doporučení IDE používal jsem v cyklu spojování řetězce přes += funguje to, ale IDE mi podtrhává proměnnou
        boolean nasledovalText = false; // rozhodne o tom jestli se má již pospojované číslo z částí zahrnout mezi řetězce k vyhledávání

        for (String polozka : splitZadani) {
            if (polozka.matches("\\d+")) { // každá položka obsahující jen čísla
                if (nasledovalText && pospojovaneCislo.length() > 2) { // číslo pospojované s částí dohromady dlouhé alespoň 3číslice po kterém následovalo zadání písmen
                    retezceKVyhledani.add(String.valueOf(pospojovaneCislo));
                    pospojovaneCislo.replace(0, pospojovaneCislo.length(), polozka); // vložíme aktualní obsah položky - nese další číslo nebo část
                    nasledovalText = false;
                } else {
                    pospojovaneCislo.append(polozka);
                    nasledovalText = false;
                }
            } else {
                nasledovalText = true;
                if (polozka.matches("\\D+") && polozka.length() > 2) // každá položka neobsahující čísla
                    retezceKVyhledani.add(polozka);
            }
        }
        if (pospojovaneCislo.length() > 2) // zbylo-li nám ještě neuložené číslo dlouhé alespoň 3. číslice... (čísla se v cyklu ukládájí jen když následuje textový řetězec)
            retezceKVyhledani.add(String.valueOf(pospojovaneCislo));
        System.out.println("Výrazy použité k hledání:");
        boolean carka = false;
        for (String vyraz: retezceKVyhledani) { // všechny položky, které nejsou čísla budou mít veké první písmeno (při vyhledávání vše .toLowerCase())
            System.out.print((carka ? ", ": "") + vyraz.replaceFirst("\\D", vyraz.substring(0, 1).toUpperCase()));
            carka = true;
        }
        List<Pojisteny> pojisteneOsoby = evidence.vratPojistene();
        System.out.print("\n\n" + vratLinku());
        System.out.printf("| Výsledky vyhledávání:%-46s|\n", " ");
        System.out.print(vratLinku());

        String znak = "*"; // znak kterým označíme z obou stran shody vyhledávání ve výpisu
        String telCislo = "";
        for (Pojisteny pojisteny : pojisteneOsoby) {
            StringBuilder aktualniPojisteny = new StringBuilder(String.valueOf(pojisteny)); // obsahuje text pojisteneho pres toString() - v případě shody zde budeme označovat jednotlivé položky
            boolean shodaTelPoprve = true;
            for (String polozka : retezceKVyhledani) {
                if (pojisteny.getJmeno().toLowerCase().contains(polozka)) // shodné jméno označíme zvoleným znakem
                    aktualniPojisteny.replace(2, pojisteny.getJmeno().length() + 4, znak + pojisteny.getJmeno() + znak);
                if (pojisteny.getPrijmeni().toLowerCase().contains(polozka))  // shodné přijmení označíme zvoleným znakem
                    aktualniPojisteny.replace(20, 20 + pojisteny.getPrijmeni().length() + 2, znak + pojisteny.getPrijmeni() + znak);
                if (pojisteny.getTelCislo().contains(polozka)) { // shodné části tel. čísla označíme zvoleným znakem - zde je to o dost komplikovanější
                    int indexOd = aktualniPojisteny.indexOf(":") + 2; // zadané číslo může být kratší a kdekoliv v rámci tel. čísla proto hledáme dvojtečku před číslem
                    if (shodaTelPoprve) { // poprvé odstraníme tel.č uživatele a přepíšeme novým s označením. V dalších iteracích již jen přepisujeme předchozí označené
                        telCislo = pojisteny.getTelCislo();
                        aktualniPojisteny.replace(indexOd, aktualniPojisteny.length(), ""); // odstraníme původní tel. číslo s odělovači mezerami
                        telCislo = telCislo.replaceFirst(polozka, znak + polozka + znak); // pro opakujicí se sekvenci části čísla označíme jen první nález (rozhodilo by nám to rámeček)
                        aktualniPojisteny.replace(indexOd, indexOd + telCislo.length(), telCislo + " |");
                        shodaTelPoprve = false;
                    } else {
                        indexOd--; // část tel. čísla již byla označena. Výchozí pozice musí jít o znak vlevo blíže k dvojtečce jinak by nám nevešlo další označení do tabulky
                        if (telCislo.indexOf(polozka) > 0) { // nesmíme v dalším kroku sáhnout mimo String - to se stane při opakovaném hledání shodné části čísla. Potom ho již s přibývajícími hvězdičkami nenajde a vrátí -1...
                            if (telCislo.substring(telCislo.indexOf(polozka) - 1, telCislo.indexOf(polozka)).equals("*")) { // je-li znak vlevo od aktuálního označovaného hvězdička (znak označení)
                                telCislo = telCislo.replace("*" + polozka, znak + polozka + znak); // potom ji přepíšeme, aby nebyly dvě vedle sebe (snažíme se udržet šířku celého čísla)
                            } else telCislo = telCislo.replace(polozka, znak + polozka + znak);
                            aktualniPojisteny.replace(indexOd, indexOd + telCislo.length(), telCislo);
                        }
                    }
                }
            }
            if (aktualniPojisteny.lastIndexOf(znak) > -1) // obsahuje-li editovaný řádek znak označení bude vypsán
                System.out.println(aktualniPojisteny);
        }
        System.out.println(vratLinku());
    }

    /**
     * Přijme upravená data od uživatele k editaci vybraného pojištěného a zavolá funkci k jejich uložení do kolekce pojištěných.
     * - Data, která nechceme změnit přeskočíme odentrováním prázdného řádku - to vyžadovalo upravit všechny vstupní metody,
     * aby takový prázdný vstup mohly na vyžádání vrátit a nepovažovaly ho za nevalidní zadání, jako je to v případě ostatních volání.
     */
    public void editujPojisteneho() {
        if (jeEvidencePrazdna())
            return;
        List<Pojisteny> pojisteneOsoby = evidence.vratPojistene();
        vypisPojistene(false, "Výpis k EDITACI,  celkem:");
        System.out.print("Zadejte index pojištěného, jehož data chcete editovat: ");
        int zadanyIndex = zadejCislo(0, pojisteneOsoby.size() - 1, false);
        Pojisteny vybranyPoj = pojisteneOsoby.get(zadanyIndex); // pro kratší zápis následujícího řádku
        System.out.println("\nNyní projdeme jednotlivé položky pojištěného.");
        System.out.println("Nechcete-li zobrazenou položku editovat stiskněte jen klávesu enter.");
        System.out.println("Jakékoliv zadání = editace zobrazené položky.");
        String tel = vybranyPoj.getTelCislo();
        System.out.printf("\nPojištěný před editaci: %s %s %d tel: %s %s %s\n", vybranyPoj.getJmeno(), vybranyPoj.getPrijmeni(), vybranyPoj.getRokNarozeni(), tel.substring(0, 3), tel.substring(3, 6), tel.substring(6));
        String noveJmeno = zadejText("jmeno", true);
        String novePrijmeni = zadejText("příjmení", true);
        System.out.printf("Zadejte rok narozeni %d - %d: ", datumDnes.getYear() - 99, datumDnes.getYear());
        int novyRokNarozeni = zadejCislo(datumDnes.getYear() - 99, datumDnes.getYear(), true);
        String noveTelCislo = zadejTel(true);

        if (noveJmeno.isEmpty() && novePrijmeni.isEmpty() && noveTelCislo.isEmpty() && novyRokNarozeni == -1) {
            System.out.println("\nNezadali jste nic k editaci... Pojištěný zůstal nezměněn.\n");
            return;
        }
        evidence.editujPojisteneho(zadanyIndex, noveJmeno, novePrijmeni, novyRokNarozeni, noveTelCislo);
        tel = vybranyPoj.getTelCislo();
        System.out.printf("\nPojištěný po editaci: %s %s %d tel: %s %s %s\n", vybranyPoj.getJmeno(), vybranyPoj.getPrijmeni(), vybranyPoj.getRokNarozeni(), tel.substring(0, 3), tel.substring(3, 6), tel.substring(6));
        System.out.println("Data byla uložena.\n");
    }

    /**
     * Přijme index od uživatele odpovídající pojištěnému k vymazání a zavolá metodu, která jej vymaže z kolekce pojištěných.
     */
    public void vymazPojisteneho() {
        if (jeEvidencePrazdna())
            return;
        List<Pojisteny> pojisteneOsoby = evidence.vratPojistene();
        vypisPojistene(false, "Výpis k VYMAZÁNÍ, celkem:");
        System.out.print("Zadejte index uživatele, kterého si přejete vymazat: ");
        int zadanyIndex = zadejCislo(0, pojisteneOsoby.size() - 1, false);
        Pojisteny vymazanyPoj = pojisteneOsoby.get(zadanyIndex); // uložíme si následně vymazanou osobu do proměnné a vygenerujeme zprávu k zobrazení (zde se nehodí výpis přes toString())
        String zpravaOVymazanem = String.format("\nPojištěný: %s %s %d byl vymazán.\n", vymazanyPoj.getJmeno(), vymazanyPoj.getPrijmeni(), vymazanyPoj.getRokNarozeni());
        evidence.vymazPojisteneho(zadanyIndex);
        System.out.println(zpravaOVymazanem);
    }

    /**
     * Přijme a vyhodnotí textový řetězec od uživatele.
     *
     * @param povolenyPrazdny boolean dle, kterého rozhodneme jestli může metoda vrátit prázdný řetězec (používáme při editaci
     *                        k přeskočení položky, kterou nechceme editovat)
     * @return vrací zadný text nebo prázdný řetězec je-li to dovoleno
     */
    private String zadejText(String text, boolean povolenyPrazdny) {
        System.out.print("Zadejte " + text + " max. 16. znaků: ");
        boolean validniZadani = false;
        String zadanyText;
        do {
            zadanyText = sc.nextLine().trim();
            if (povolenyPrazdny && zadanyText.length() == 0)
                return zadanyText;
            if (zadanyText.matches("\\D+") && zadanyText.length() >= 3 && zadanyText.length() <= 16) {
                zadanyText = zadanyText.substring(0, 1).toUpperCase() + zadanyText.substring(1); // případná oprava nezadaného prvního velkého písmena
                validniZadani = true;
            } else System.out.print("Chybné zadání. Zadejte text (3 - 16 znaků) bez čísel: ");
        } while (!validniZadani);
        return zadanyText;
    }

    /**
     * Přijme od uživatele tel. číslo 9. číslic dlouhé jako řetězec. Můžeme jej zadat libovolně rozdělené různými oddělovači.
     *
     * @param povolenyPrazdny boolean dle, kterého rozhodneme jestli může metoda vrátit prázdný řetězec (používáme při editaci
     *                        k přeskočení položky, kterou nechceme editovat)
     * @return vrací zadané číslo nebo prázdný řetězec je-li to dovoleno
     */
    private String zadejTel(boolean povolenyPrazdny) {
        System.out.print("Zadejte tel. můžete ho libovolně rozdělit (9. čísel): ");
        boolean validniZadani = false;
        String upravenyText;
        do {
            upravenyText = "";
            String zadanyText = sc.nextLine().trim();
            if (povolenyPrazdny && zadanyText.isEmpty())
                return zadanyText;
            String[] splitZadani2;
            if (!zadanyText.isEmpty()) {
                splitZadani2 = zadanyText.split("[-.,;: ]+");
                for (String polozka : splitZadani2) {
                    if (polozka.matches("\\d+"))
                        upravenyText += polozka;
                }
                if (upravenyText.length() == 9)
                    validniZadani = true;
                else System.out.print("Chybné zadání. Zadejte celkem 9 číslic: ");

            } else {
                System.out.print("Nic nezadáno.... Zkuste to znovu: ");
            }
        } while (!validniZadani);
        return upravenyText;
    }

    /**
     * Přijme číselný vstup od uživatele v zadaném rozsahu. Jinak informuje o nesprávném zadání a vyžaduje zadání znovu dokud
     * není zadání validní.
     *
     * @param min             minimální hodnota pro zadání
     * @param max             maximální hodnota pro zadání
     * @param povolenyPrazdny boolean dle, kterého rozhodneme jestli může metoda vrátit prázdný vstup (zde -1. Používáme při
     *                        editaci k přeskočení položky, kterou nechceme editovat)
     * @return vrací int v zadaném rozsahu nebo -1 je-li dovoleno vrátit prázdný vstup
     */
    public int zadejCislo(int min, int max, boolean povolenyPrazdny) {
        boolean spravneZadani = false;
        String textoveZadani; //pro vyhodnocení samotného enter které při editaci uživatele považujeme za přeskočení na další položku bez editace dané položky
        int zadani = -1;
        while (!spravneZadani) {
            try {
                textoveZadani = sc.nextLine().trim();
                if (povolenyPrazdny && textoveZadani.isEmpty())
                    return zadani;
                zadani = Integer.parseInt(textoveZadani);
                if (zadani < min || zadani > max)
                    throw new Exception("Zadání mimo rozsah. Zadejte " + min + "-" + max + ":"); //zde by to chtělo výpis bez odřádkování... :-(
                else spravneZadani = true;
            } catch (NumberFormatException e) {
                System.out.println("Zadán nevhodný znak. Prosím zadejte znovu:");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return zadani;
    }

    /**
     * Vrací správně vyskloňované slovo pojištěný dle počtu zobrazených položek (voláme po vyhledávání).
     *
     * @param pocet počet zobrazených položek
     * @return správně vyskloňovaný tvar slova pojištěný s jinou délkou řetězce při pocet = 10+
     */
    private String sklonovani(int pocet) {
        String slovo = switch (pocet) { // ano, toto vše lze zadat přímo do return bez proměnné, ale je potom celý blok označený zeleně a vypadá to nezvykle...
            case 1 -> "pojištěný:   ";
            case 2, 3, 4 -> "pojištění:   ";
            case 0, 5, 6, 7, 8, 9 ->
                    "pojištěných: "; // z důvodu stejně širokého řádku jako u 10+ pojištěných (mezera navíc)
            default -> "pojištěných:";
        };
        return slovo;
    }

    /**
     * Ověří zda-li máme v evidenci pojištěné. Jinak vypíše oznámení a vrátí boolean pro další vyhodnocení.
     *
     * @return boolean dle toho jestli je kolekce pojištěných prázdná
     */
    private boolean jeEvidencePrazdna() {
        List<Pojisteny> pojisteneOsoby = evidence.vratPojistene();
        if (pojisteneOsoby.isEmpty()) {
            System.out.println("\nSeznam pojištěných je prázdný. Zadejte nového pojištěného...\n");
            return true;
        }
        return false;
    }

    /**
     * @return vrací stále se v aplikaci vyskytující řádek k zobrazení. Hlavičky a ukončení tabulek.
     */
    public String vratLinku() {
        return String.format("----------------------------------------------------------------------\n");
    }
}
