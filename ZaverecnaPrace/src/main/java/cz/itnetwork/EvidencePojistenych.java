package cz.itnetwork;

public class EvidencePojistenych {
    public static void main(String[] args) {
        /**
         * Závěrečná práce: Evidence pojištěných verze pro konzoli.
         *
         * Vytvořil: Patrik Lorenc 5/2023 v rámci rekvalifikačního kurzu: Programátor www aplikací v Javě.
         *
         * Oproti jednoduchému zadání jsem přidal následující položky:
         *
         * - Editace položek pojištěného (místo nabídky s volbou položky k editaci, která mi připadala nepřehledná a zdlouhavá
         *   jsem zvolil postupné procházení všech čtyř položek. Položce, kterou chceme zachovat odešleme prázdný řádek.)
         * - Mazání položek v evidenci pomocí zadání indexu, který je zobrazen v seznamu pojištěných.
         *
         * - Fulltextové vyhledávání. S tím, že vyhledané shody označí v rámci řádku s daty pojištěného. Vyhledává i jen části
         *   řetězců a tel. čísel (jména a přijmení označí vždy celé. U čísel označuje i jednotlivé shody - má to určité limity,
         *   aby to alespoň většinou nevyteklo mimo tabulku).
         *   Při zadávání tel. čísel je můžeme libovolně rozdělit a proto vložíme mezi jednotlivá čísla nějaký text k vyhledání.
         *   Počet položek není omezen. Berou se v úvahu položky dlouhé 3 a více znaků (u čísel se vyhodnotí až po spojení rozdělených
         *   částí čísla). Jako odělovače můžeme při vyhledávání mezi jednotlivými čísly a texty použít nejen mezeru, ale take .,:;-
         *   a libovolně je kombinovat....
         *   - z důvodu složitosti vyhledávání a udržení vzhledu tabulek jsem se vyhnul možnosti zadat mezinárodní volbu nebo tel. číslo
         *     v jiné délce než 9. číslic...
         *
         * - Snažil jsem se ošetřit různé varianty nevalidních vstupů dle toho co a kde se zrovna zadává.
         * - Stejně tak jsem se pokusil o vzhled zarovnaný do tabulek kde má záměrně hlavní nabídka přehnaně výraznou hlavičku
         *   (ano také se mi to nelíbí, ale pomáhá to v orientaci mezi tabulkami vzhledem k tomu, že nám Java neumožnila vymazat konzoli).
         * - Pár dalších drobností jako třeba skloňování slova pojištěný ve výpisech dle počtu položek a doplnění velkého prvního
         *   písmena u jména a příjmení když nebude zadáno.
         */
        ObsluhaEvidence obEvidence = new ObsluhaEvidence();
        boolean pokracuj = true;

        do {
            System.out.printf(obEvidence.vratLinku());
            System.out.printf("|    *#############*     EVIDENCE  POJIŠTĚNÝCH     *############*    |\n", " ", " ");
            System.out.printf(obEvidence.vratLinku());
            System.out.printf("|%-20s1 - Přidat nového pojištěného%-19s|\n", " ", " ");
            System.out.printf("|%-20s2 - Vypsat všechny pojištěné%-20s|\n", " ", " ");
            System.out.printf("|%-20s3 - Vyhledat pojištěného%-24s|\n", " ", " ");
            System.out.printf("|%-20s4 - Editovat pojištěného%-24s|\n", " ", " ");
            System.out.printf("|%-20s5 - Vymazat pojištěného%-25s|\n", " ", " ");
            System.out.printf("|%-20s6 - Ukončit program%-29s|\n", " ", " ");
            System.out.printf(obEvidence.vratLinku());
            System.out.print("\nZadejte prosím vaši volbu: ");

            switch (obEvidence.zadejCislo(1, 6, false)) {
                case 1 -> obEvidence.pridejPojisteneho();
                case 2 -> obEvidence.vypisPojistene(true, "Výpis, celkem:");
                case 3 -> obEvidence.vyhledejPojisteneho();
                case 4 -> obEvidence.editujPojisteneho();
                case 5 -> obEvidence.vymazPojisteneho();
                case 6 -> pokracuj = false;
            }
        } while (pokracuj);

        System.out.println("Konec programu, děkuji za vyzkoušení :-)\n");
        System.out.println("Stav uložených dat po ukončení programu: ");
        obEvidence.vypisPojistene(true, "Výpis, celkem:");
    }
}