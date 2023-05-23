package cz.itnetwork;

import java.time.LocalDate;

public class Pojisteny {
    /**
     * obsah atributů je zde zřejmý z jejich názvu (datumDnes se použije k výpočtu věku - samosebou by to šlo vylepšit o přesný
     * věk kdyby byly data o narození kompletní).
     */
    private String jmeno;
    private String prijmeni;
    private int rokNarozeni;
    private String telCislo;
    private LocalDate datumDnes = LocalDate.now();


    public Pojisteny(String jmeno, String prijmeni, int rokNarozeni, String telCislo) {
        this.jmeno = jmeno;
        this.prijmeni = prijmeni;
        this.rokNarozeni = rokNarozeni;
        this.telCislo = telCislo;
    }

    /**
     * Následují gettery a settery...
     */
    public String getJmeno() {
        return jmeno;
    }

    public String getPrijmeni() {
        return prijmeni;
    }

    public String getTelCislo() {
        return telCislo;
    }

    public int getRokNarozeni() {
        return rokNarozeni;
    }

    public void setJmeno(String jmeno) {
        this.jmeno = jmeno;
    }

    public void setRokNarozeni(int rokNarozeni) {
        this.rokNarozeni = rokNarozeni;
    }

    public void setPrijmeni(String prijmeni) {
        this.prijmeni = prijmeni;
    }

    public void setTelCislo(String telCislo) {
        this.telCislo = telCislo;
    }

    /**
     * Překrytá metoda toString tak, aby dokázala vypsat obsah atributů, které nese instance.
     *
     * @return vrací String s obsahem atributů instance.
     */
    @Override
    public String toString() {//z důvodu vyhledávání tel. čísla i jeho části (a označování nalezené části v řetězci s číslem) je mám uložené bez mezer a ty zobrazuji až při vypisování zde...
        return String.format("| %-18s%-18s%d(%2d let)  tel: %s %s %s |", jmeno, prijmeni, rokNarozeni, (datumDnes.getYear() - rokNarozeni), telCislo.substring(0, 3), telCislo.substring(3, 6), telCislo.substring(6));
    }
}
